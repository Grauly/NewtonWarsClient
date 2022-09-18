package grauly.newtonwarsclient.network;

import grauly.newtonwarsclient.NWView;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ConnectionReader implements Runnable, Closeable {

    private Socket socket;
    private NWView window;
    private boolean running = true;

    public ConnectionReader(Socket socket, NWView window) {
        this.socket = socket;
        this.window = window;
    }

    @Override
    public void run() {
        while (running) {
            if(!socket.isClosed()) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));) {
                    while (!socket.isClosed() && running) {
                        try {
                            window.postUpdate(reader.readLine());
                        } catch (SocketTimeoutException e) {

                        }
                    }
                } catch (IOException e) {
                    running = false;
                    throw new RuntimeException(e);
                }
            }
        }
        System.out.println("Reader Thread closed");
    }

    @Override
    public void close() throws IOException {
        running = false;
    }
}
