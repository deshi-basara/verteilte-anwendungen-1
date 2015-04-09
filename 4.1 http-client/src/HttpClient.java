import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by simon on 09.04.15.
 */
public class HttpClient {

    public static String get(String inputUrl) {
        String baseUrl = "";
        StringBuilder response = null;

        try {
            baseUrl = buildUrl(inputUrl);
        } catch(MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            response = sendGetRequest(inputUrl);
            System.out.println("Connected to URL: " + inputUrl);
        } catch(MalformedURLException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    private static String buildUrl(String inputUrl) throws MalformedURLException {
        URL url = new URL(inputUrl.trim());

        String host = url.getHost();
        int port = url.getPort();
        if(port == -1) {
            // no port found, set to '80' as default
            port = 80;
        }

        return host + ":" + port;
    }

    private static StringBuilder sendGetRequest(String baseUrl) throws MalformedURLException, IOException {
        URL url = new URL(baseUrl);

        // open a connection to the handed url
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        int resCode = con.getResponseCode();
        StringBuilder resMsg = new StringBuilder();
        // everything went well
        if(resCode == 200) {
            // get headers
            Map<String, List<String>> headerFields = con.getHeaderFields();
            // iterate through all map entries
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                if (entry.getKey() == null) {
                    continue;
                }
                resMsg.append(entry.getKey()).append(": ");

                List<String> headerValues = entry.getValue();
                Iterator<String> it = headerValues.iterator();
                if (it.hasNext()) {
                    resMsg.append(it.next());

                    while (it.hasNext()) {
                        resMsg.append(", ").append(it.next());
                    }
                }

                resMsg.append("\n");
            }

            // get response
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            // add response lines to the resMsg-string
            resMsg.append("\n");
            String resLine;
            while((resLine = reader.readLine()) != null) {
                resMsg.append(resLine);
                resMsg.append("\n");
            }

            reader.close();
        }
        else if(resCode == 404) {
            resMsg.append("URL not found");
        }

        return resMsg;
    }

    public static boolean doesUrlExist(String inputUrl) {

        try {
            // open a connection to the handed url
            URL url = new URL(inputUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            if(con.getResponseCode() == 404) {
                return false;
            }
        } catch(MalformedURLException e) {
            return false;
        } catch(IOException e) {
            return false;
        }

        return true;
    }

    public static String getUrlContent(String inputUrl) {

        try {
            // open a connection to the handed url
            URL url = new URL(inputUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int resCode = con.getResponseCode();
            if(resCode == 200) {
                // get response
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                // add response lines to the resMsg-string
                StringBuilder resMsg = new StringBuilder();
                String resLine;
                while((resLine = reader.readLine()) != null) {
                    resMsg.append(resLine);
                    resMsg.append("\n");
                }

                return resMsg.toString();
            }
        } catch(MalformedURLException e) {
            return "";
        } catch(IOException e) {
            return "";
        }
    }

    public static long getChangeDate(String inputUrl) {
        try {
            // open a connection to the handed url
            URL url = new URL(inputUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            int resCode = con.getResponseCode();
            if(resCode == 200) {

                long lastMod = con.getLastModified();
            }
        } catch(MalformedURLException e) {
            return null;
        } catch(IOException e) {
            return null;
        }
    }
}
