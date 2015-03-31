import java.io.*;
import java.net.Socket;

/**
 * Created by simon on 31.03.15.
 */
public class TimeServiceClient {

    /**
     * Connection details
     */
    private Socket socket = null;
    private String socketAddress = "127.0.0.1";
    private final int socketPort = 10075;

    /**
     * Streams & event-messages
     */
    private BufferedWriter serverWriter = null;
    private BufferedReader serverReader = null;
    private final String dateMsg = "date";
    private final String timeMsg = "time";
    private final String welcomeMsg = "time service";

    /**
     * Class constructor.
     * Establishes a connection with the specified socketAddress and
     * sets the input-/output-reader.
     * @param socketAddress Address of the socket we want to connect to.
     */
    public TimeServiceClient(String socketAddress) {
        // set the address
        this.socketAddress = socketAddress;

        // try to establish a connection to the socket
        try {
            this.socket = new Socket(this.socketAddress, this.socketPort);
        } catch(IOException e) {
            e.printStackTrace();
        }

        // set writer & reader
        try {
            OutputStream serverOutput = this.socket.getOutputStream();
            InputStream serverInput = this.socket.getInputStream();
            this.serverWriter = new BufferedWriter(new OutputStreamWriter(serverOutput));
            this.serverReader = new BufferedReader(new InputStreamReader(serverInput));
        } catch(IOException e) {
            e.printStackTrace();
        }

        System.out.println("A connection to [" + this.socketAddress + ":" + this.socketPort +
                                "] was established");
    }

    /**
     * Requests the current date from the time-service.
     * @return serverRes Server-response to our the request.
     */
    public String dateFromServer() {
        // send a date-request-event to the server
        sendMsg(this.dateMsg);

        // listen for the server-response
        return listenMsg();
    }

    /**
     * Requests the current time from the time-service.
     * @return serverRes Server-response to our the request.
     */
    public String timeFromServer() {
        // send a time-request-event to the server
        sendMsg(this.timeMsg);

        // listen for the server-response
        return listenMsg();
    }

    /**
     * Listens for messages sent by server.
     * Returns all message-strings other than the "welcome-message".
     * @return serverRes Server-response to the request.
     */
    private String listenMsg() {

        // listen for the server-response
        try {
            String serverRes;

            while((serverRes = this.serverReader.readLine()) != null) {
                System.out.println("Server response: " + serverRes);

                // stop listening if we receive another response-message than the "welcome message" (which has
                // to be the requested data).
                if(serverRes.indexOf(this.welcomeMsg) == -1) {
                    return serverRes;
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Sends a message-string to the connected server via a BufferedWriter.
     * @param msg Message
     */
    private void sendMsg(String msg) {
        try {
            this.serverWriter.write(msg);
            this.serverWriter.newLine();
            this.serverWriter.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
