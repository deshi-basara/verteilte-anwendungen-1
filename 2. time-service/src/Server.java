import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class.
 * Initiates the Socket-Server and manages connected clients.
 */
public class Server {

    private final int serverPort = 10075;
    private ServerSocket server = null;
    private int clientCounter = 0;

    /**
     * Class constructor.
     * Initiates a new Socket-Server, that creates a new Thread for every newly
     * connected client.
     */
    public Server() {

        // initiate our socket
        try {
            this.server = new ServerSocket(this.serverPort);

            System.out.println("Server is running on port: " + this.serverPort);
        } catch(IOException e) {
            e.printStackTrace();
        }

        // start listening for connections as long as the socket is running
        while(!this.server.isClosed()) {
            Socket client = null;

            try {
                // accept only one socket-connection at the time
                client = this.server.accept();

                // create a new Thread for the connected client and wait for input
                Connection connectedClient = new Connection(client, ++this.clientCounter);
                connectedClient.start();

            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

}
