public class Main {

    public static void main(String[] args) {
        String response = HttpClient.get("http://www.heise.de");
        System.out.println(response);
    }
}
