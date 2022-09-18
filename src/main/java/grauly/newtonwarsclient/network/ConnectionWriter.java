package grauly.newtonwarsclient.network;

import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionWriter implements Runnable, Closeable {

    private Socket socket;
    private boolean running = true;

    public ConnectionWriter(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        while(running) {
            if(!socket.isClosed()) {
                try(PrintWriter writer = new PrintWriter(socket.getOutputStream())) {
                    while (!socket.isClosed() && running) {
                        if(!ConnectionHandler.outgoingMessages.isEmpty()) {
                            writer.println(ConnectionHandler.outgoingMessages.poll());
                            writer.flush();
                        }
                    }
                } catch (IOException e) {
                    running = false;
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Writer Thread closed");
    }

    @Override
    public void close() throws IOException {
        running = false;
    }
}
