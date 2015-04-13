import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by simon on 09.04.15.
 */
public class HttpClient {

    /**
     * Initiates a GET-request to the handend inputUrl after validating the url and
     * returns the response's head & body.
     * @param inputUrl
     * @return
     */
    public static String get(String inputUrl) {
        String baseUrl = "";
        StringBuilder response = null;

        // try to parse the url
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

    /**
     * Builds an url with port.
     * @param inputUrl
     * @return
     * @throws MalformedURLException
     */
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

    /**
     * Sends a GET-request to the handend inputUrl after validating the url and
     * returns the response's head & body.
     * @param baseUrl
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
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

    /**
     * Checks if the handed inputUrl is a valid url, by validating the request's response-code.
     * @param inputUrl
     * @return
     */
    public static boolean doesUrlExist(String inputUrl) {

        try {
            // open a connection to the handed url
            URL url = new URL(inputUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            // check response code
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

    /**
     * Returns the html-content of a handed inputUrl, if the url exists.
     * @param inputUrl
     * @return
     */
    public static String getUrlContent(String inputUrl) {

        try {
            // open a connection to the handed url
            URL url = new URL(inputUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            // valid request?
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

        return "";
    }

    /**
     * Trys to request the last modification date of a html-document.
     * If "-1" there was nothing found.
     * @param inputUrl
     * @return
     */
    public static long getChangeDate(String inputUrl) {
        try {
            // open a connection to the handed url
            URL url = new URL(inputUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();

            // valid request?
            int resCode = con.getResponseCode();
            if(resCode == 200) {
                long lastMod = con.getLastModified();

                return lastMod;
            }
        } catch(MalformedURLException e) {
            return -1;
        } catch(IOException e) {
            return -1;
        }

        return -1;

    }
}
