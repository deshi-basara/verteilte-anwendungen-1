import java.io.*;
import java.net.Socket;

/**
 * Connection class.
 * Manages input-/output-message-events for a connected client.
 */
public class Connection extends Thread {

    private Socket client = null;
    private int clientId = 0;
    private boolean clientOk = true;
    private BufferedWriter clientWriter = null;
    private BufferedReader clientReader = null;
    private final String welcomeMsg = "time service";

    /**
     * Class constructor.
     * Sets the client (Socket) and clientId for the object.
     * Tries to initiate buffered input-/output-streams.
     * @param client Connected client
     * @param clientId Id of the connected client
     */
    public Connection(Socket client, int clientId) {
        // set client
        this.client = client;
        this.clientId = clientId;

        // set writer & reader
        try {
            OutputStream clientOutput = this.client.getOutputStream();
            InputStream clientInput = this.client.getInputStream();

            this.clientWriter = new BufferedWriter(new OutputStreamWriter(clientOutput));
            this.clientReader = new BufferedReader(new InputStreamReader(clientInput));
        } catch(IOException e) {
            e.printStackTrace();
            this.clientOk = false;
        }

        System.out.println("New client [" + this.clientId + "] connected");
    }

    public void run() {
        // only execute Thread as long as the client is ok/connected
        //while(this.clientOk) {

            try {
                // welcome the new client
                sendMsg(this.welcomeMsg);

                // start listening for client messages
                msgListener();
            } catch(IOException e) {
                // print stack-trace and stop the Thread-execution
                e.printStackTrace();
                this.clientOk = false;
            }

        //}
    }

    /**
     * Listens for new input message-events by the client.
     * Reacts on "date" and "time", closes the client's connection on unknown events.
     */
    private void msgListener() {

        try {
            String clientMsg;

            // listen for messages until the client closes the connection
            while((clientMsg = this.clientReader.readLine()) != null) {

                // check if client sent an event-message
                if(clientMsg.equals("date")) {
                    System.out.println("Client [" + this.clientId + "] requested the current date");
                    sendMsg(Clock.date());
                }
                else if(clientMsg.equals("time")) {
                    System.out.println("Client [" + this.clientId + "] requested the current time");
                    sendMsg(Clock.time());
                }
                else {
                    System.out.println("No matching event ... closing connection of client [" + this.clientId + "]");

                    this.client.close();
                    this.clientOk = false;
                    return;
                }

            }
        } catch(IOException e) {
            // print stack-trace and stop the Thread-execution
            e.printStackTrace();
            this.clientOk = false;
        }

    }

    /**
     * Sends a message-string to the connected client via a BufferedWriter.
     * @param msg Message
     */
    private void sendMsg(String msg) throws IOException {
        this.clientWriter.write(msg);
        this.clientWriter.newLine();
        this.clientWriter.flush();
    }
}
