import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //Testing Network class from AccuWeather API
        String apiKey = "JF2eXKsL6C6qjsTAryd4KNiU7ToM0jJM";
        String url = "http://dataservice.accuweather.com/locations/v1/postalcodes/search?";

        Map<String, String> map = new HashMap<>();
        map.put("apikey", apiKey);
        map.put("q", "46975");

        Network network = new Network();
        network.httpGet(url, map);
    }
}
