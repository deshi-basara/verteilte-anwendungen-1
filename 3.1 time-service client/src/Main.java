public class Main {

    public static void main(String[] args) {
        TimeServiceClient clientTelnet = new TimeServiceClient("127.0.0.1");
        String date = clientTelnet.dateFromServer();
        String time = clientTelnet.timeFromServer();

        System.out.println(time + " " + date);
    }
}
