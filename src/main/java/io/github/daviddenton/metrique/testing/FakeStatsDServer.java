package io.github.daviddenton.metrique.testing;


import io.github.daviddenton.metrique.Host;
import io.github.daviddenton.metrique.Port;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import static io.github.daviddenton.metrique.Port.port;
import static java.util.Arrays.copyOfRange;

public class FakeStatsDServer {
    private final Port port;
    private final Consumer<String> messageReceiver;
    private DatagramReceiving datagramReceiving;

    public FakeStatsDServer(Port greyLogPort, Consumer<String> statReceiver) {
        this.port = greyLogPort;
        this.messageReceiver = statReceiver;
    }

    public FakeStatsDServer(Port port) {
        this(port, StatsDReceiver.Printing);
    }

    public void start() throws Exception {
        datagramReceiving = new DatagramReceiving(port, messageReceiver);
        new Thread(datagramReceiving).start();
    }

    public void stop() throws Exception {
        datagramReceiving.stop();
    }

    private static class DatagramReceiving implements Runnable {
        private static final String STOP_MESSAGE = "STOP";

        private final Port port;

        private final CountDownLatch stopLatch = new CountDownLatch(1);
        private final Consumer<String> messageReceiver;

        private DatagramReceiving(Port port, Consumer<String> messageReceiver) {
            this.port = port;
            this.messageReceiver = messageReceiver;
        }

        public void run() {
            try {
                boolean running = true;
                DatagramSocket socket = new DatagramSocket(port.value);
                while (running) {
                    byte[] inBuffer = new byte[1024 * 10];
                    DatagramPacket packet = new DatagramPacket(inBuffer, inBuffer.length);
                    socket.receive(packet);

                    if (STOP_MESSAGE.equals(new String(copyOfRange(inBuffer, 0, STOP_MESSAGE.getBytes().length)))) {
                        running = false;
                    } else {
                        messageReceiver.accept(new String(inBuffer));
                    }
                }
                socket.close();
                stopLatch.countDown();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public void stop() throws Exception {
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(new DatagramPacket(STOP_MESSAGE.getBytes(), STOP_MESSAGE.getBytes().length, Host.localhost.socketAddress(port)));
            datagramSocket.close();
            stopLatch.await();
        }
    }

    public static void main(String[] args) throws Exception {
        new FakeStatsDServer(port(8125)).start();
    }
}
