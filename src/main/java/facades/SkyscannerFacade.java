package facades;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import javafx.util.Pair;
import org.glassfish.jersey.internal.guava.ExecutionError;

public class SkyscannerFacade {

    private static ExecutorService executor = Executors.newCachedThreadPool();
    Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static SkyscannerFacade instance;
    private String key = "04df80bf11msh07708f02a1040d1p1d7092jsn6ec6decfc602";
    private String host ="skyscanner-skyscanner-flight-search-v1.p.rapidapi.com";
    private String[] PLACES = {"?query=Copenhagen", "?query=London", "?query=Rome", "?query=Milano", "?query=Berlin"};

    public Map<String, String> getAllPlaces() throws InterruptedException, ExecutionError, TimeoutException, ExecutionException {
        Map<String, String> result = new HashMap();
        Queue<Future<Pair<String, String>>> queue = new ArrayBlockingQueue(PLACES.length);

        for (final String place : PLACES) {
            Future<Pair<String, String>> future = executor.submit(new Callable<Pair<String, String>>() {
                @Override
                public Pair<String, String> call() throws Exception {
                    return new Pair(place.substring(0, place.length() - 1), getPlaceData(url + place));
                }
            });
            queue.add(future);
        }

        while (!queue.isEmpty()) {
            Future<Pair<String, String>> epPair = queue.poll();
            if (epPair.isDone()) {
                result.put(epPair.get().getKey(), epPair.get().getValue());
            } else {
                queue.add(epPair);
            }
        }
        executor.shutdown();
        return result;
    
}
    
    public String getPlaces(String place) throws MalformedURLException, IOException {
        String result ="";
        String defaultUrl = "\"https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/UK/GBP/en-GB/\"";
        String fullUrl = defaultUrl + place;
            URL url = new URL(fullUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json;charset=UTF-8");
            con.setRequestProperty("x-rapidapi-key",key);
            con.setRequestProperty("x-rapidapi-host",host);
            //connection.setRequestProperty("user-agent", "Application");
            try (Scanner scan = new Scanner(con.getInputStream())) {
                String response = "";
                while (scan.hasNext()) {
                    response += scan.nextLine();
                }
                Gson gson = new Gson();
                result = gson.fromJson(response, JsonObject.class).toString();
            } catch (Exception e) {
              result = "";
        }
            return result;
    }
    
    
    
    public String createSession(String inboundDate, String cabinClass, String originPlace, String destinationPlace, String outboundDate, int adults) throws UnirestException{
        HttpResponse<JsonNode> response = Unirest.post("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/v1.0")
                .header("X-RapidAPI-Host", host)
                .header("X-RapidAPI-Key", key)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("inboundDate", inboundDate) // "2019-09-10"
                .field("cabinClass", cabinClass) //  "business"
                .field("children", 0)
                .field("infants", 0)
                .field("country", "US")
                .field("currency", "USD")
                .field("locale", "en-US")
                .field("originPlace", originPlace) // "SFO-sky"
                .field("destinationPlace", destinationPlace) //"LHR-sky"
                .field("outboundDate", outboundDate) // "2019-09-01"
                .field("adults", adults) // 1
                .asJson();
    
    String sessionKey = "";
    String location = "";
    
    for(Map.Entry<String, List<String>> entry : response.getHeaders().entrySet()) {
        Object key = entry.getKey();
        Object value = entry.getValue();
        
        if(key.equals("Location")) {
            location = value.toString();
    }
        
}
        System.out.println("location:" + location);
        for(int i = location.length() - 37; i < location.length() - 1; i++) {
            sessionKey += location.charAt(i);
}
        
        return sessionKey;
     
}
    
        
    
    
}
    


