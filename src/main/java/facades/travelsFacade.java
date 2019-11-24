package facades;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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

public class travelsFacade {

    private static ExecutorService executor = Executors.newCachedThreadPool();
    //Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static travelsFacade instance;
    private String url = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/UK/GBP/en-GB/";
    private String[] PLACES = {"?query=Copenhagen", "?query=London", "?query=Rome", "?query=Milano", "?query=Berlin"};

    public Map<String, String> apiDataAll() throws InterruptedException, ExecutionError, TimeoutException, ExecutionException {
        Map<String, String> result = new HashMap();
        Queue<Future<Pair<String, String>>> queue = new ArrayBlockingQueue(PLACES.length);

        for (final String place : PLACES) {
            Future<Pair<String, String>> future = executor.submit(new Callable<Pair<String, String>>() {
                @Override
                public Pair<String, String> call() throws Exception {
                    return new Pair(place.substring(0, place.length() - 1), getTravelData(url + place));
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
    
    public String getTravelData(String url) throws MalformedURLException, IOException {
        String result = "";
        try {
            URL travelURL = new URL(url);
            HttpURLConnection con = (HttpURLConnection) travelURL.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json;charset=UTF-8");
            con.setRequestProperty("x-rapidapi-host","skyscanner-skyscanner-flight-search-v1.p.rapidapi.com");
            con.setRequestProperty("x-rapidapi-key","04df80bf11msh07708f02a1040d1p1d7092jsn6ec6decfc602");
            //connection.setRequestProperty("user-agent", "Application");
            try (Scanner scan = new Scanner(con.getInputStream())) {
                String response = "";
                while (scan.hasNext()) {
                    response += scan.nextLine();
                }
                Gson gson = new Gson();
                result = gson.fromJson(response, JsonObject.class).toString();
            }
        } catch (Exception e) {
            result = "";
        }
        return result;
    }

    
}
