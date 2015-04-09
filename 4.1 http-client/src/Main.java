public class Main {

    public static void main(String[] args) {
        String response = HttpClient.get("http://www.i-was-perfect.net");
        System.out.println(response);
    }
}
