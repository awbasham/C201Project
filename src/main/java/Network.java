import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Network {

    public String httpGet(String baseUrl, Map<String, String> params) {
        StringBuilder baseUrlBuilder = new StringBuilder(baseUrl);
        for(Map.Entry<String, String> entry : params.entrySet()) {
            baseUrlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        baseUrl = baseUrlBuilder.toString();

        try {
            URL url = new URL(baseUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            System.out.println("Response code: " + con.getResponseCode());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        } catch(Exception e) {
            System.out.println("Error Network.java: " + e.getMessage());
        }

        return null;
    }
}
