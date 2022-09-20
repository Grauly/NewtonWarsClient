package grauly.newtonwarsclient.network;

import grauly.newtonwarsclient.NWView;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConnectionHandler implements Runnable {

    public static final ConcurrentLinkedQueue<String> outgoingMessages = new ConcurrentLinkedQueue<>();

    private final String ip;
    private final int port;
    private AtomicBoolean running = new AtomicBoolean(true);
    private static Socket socket;
    private NWView window;
    private Thread readerThread;
    private ConnectionReader reader;
    private Thread writerThread;
    private ConnectionWriter writer;
    private Thread selfThread;

    public ConnectionHandler(String ip, int port, NWView window) {
        this.ip = ip;
        this.port = port;
        this.window = window;
        selfThread = new Thread(this);
        selfThread.start();
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(ip, port);
            socket.setSoTimeout(100);
            ConnectionHandler.socket = socket;

            reader = new ConnectionReader(ConnectionHandler.socket,window);
            readerThread = new Thread(reader);
            readerThread.start();

            writer = new ConnectionWriter(ConnectionHandler.socket);
            writerThread = new Thread(writer);
            writerThread.start();

            while (running.get()) {

            }
            System.out.println("Socket Thread closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void stop() throws InterruptedException, IOException {
        System.out.println("Waiting for threads to close (0/3) - reader");
        if(reader != null)
            reader.close();
        if(readerThread != null)
            readerThread.join();
        System.out.println("Waiting for threads to close (1/3) - writer");
        if(writer != null)
            writer.close();
        if(writerThread != null)
            writerThread.join();
        System.out.println("Waiting for threads to close (2/3) - socket");
        running.set(false);
        if(socket != null)
            socket.close();
        if(selfThread != null)
            selfThread.join();
        System.out.println("Waiting for threads to close (3/3) - done");
    }
}
