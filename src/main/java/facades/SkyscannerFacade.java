package facades;
import DTO.AgentsDTO;
import DTO.CarriersDTO;
import DTO.FlightInfoDTO;
import DTO.ItinerariesDTO;
import DTO.PlacesDTO;
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
import org.json.JSONArray;

public class SkyscannerFacade {

    private static ExecutorService executor = Executors.newCachedThreadPool();
    Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static SkyscannerFacade instance;
    private String key = "04df80bf11msh07708f02a1040d1p1d7092jsn6ec6decfc602";
    private String host ="skyscanner-skyscanner-flight-search-v1.p.rapidapi.com";
    
    /* private String[] PLACES = {"?query=Copenhagen", "?query=London", "?query=Rome", "?query=Milano", "?query=Berlin"};

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
*/    
    public String fetchPlaces(String query) throws MalformedURLException, IOException {
        String result ="";
        String defaultUrl = "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/autosuggest/v1.0/UK/GBP/en-GB/?query=";
        String fullUrl = defaultUrl + query;
            URL url = new URL(fullUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json;charset=UTF-8");
            con.setRequestProperty("x-rapidapi-key",key);
            con.setRequestProperty("x-rapidapi-host",host);
           
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
    
    public String createSession(
            String outboundDate, 
            String cabinClass,
            String originPlace, 
            String destinationPlace, 
            int adults,
            String inboundDate,
            int children,
            int infants
           )throws UnirestException{ // for a live flight search
        HttpResponse<JsonNode> response = Unirest.post("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/v1.0")
                .header("X-RapidAPI-Host", host)
                .header("X-RapidAPI-Key", key)
                .header("Content-Type", "application/x-www-form-urlencoded")
                //Required parameters for session 
                .field("outboundDate", outboundDate) // "2019-09-01"
                .field("cabinClass", cabinClass) //  "business"
                .field("country", "US") // "US"
                .field("currency", "USD") // "USD"
                .field("locale", "en-US") // "en-US"
                .field("originPlace", originPlace) // "SFO-sky"
                .field("destinationPlace", destinationPlace) //"LHR-sky"
                .field("adults", adults) // 2
                //Optional parameters for session
                .field("inboundDate", inboundDate) // "2019-09-10"
                .field("children", children) // 2
                .field("infants", infants) // 1 
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
    
    public List <FlightInfoDTO> getFlightSearch(
            String outboundDate, 
            String cabinClass,
            String originPlace, 
            String destinationPlace, 
            int adults,
            String inboundDate,
            int children,
            int infants
           )throws UnirestException{
           String sessionKey = createSession(outboundDate, cabinClass, originPlace, destinationPlace, adults, inboundDate, children, infants);
         HttpResponse<JsonNode> response = Unirest.get(
                "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/uk2/v1.0/"
                + sessionKey + "?pageIndex=0&pageSize=10")
                .header("X-RapidAPI-Host", host)
                .header("X-RapidAPI-Key", key)
                .asJson();
         
         
         JSONArray itineraries = response.getBody().getObject().getJSONArray("Itineraries");
         JSONArray segments = response.getBody().getObject().getJSONArray("Segments");
         JSONArray pricingOptions;
         JSONArray carriers;
         
         
         // Itineraries
         String inboundLegId;
         String outboundLegId;
         double price;
         int agents;
         String deeplinkUrl;
         List<ItinerariesDTO> itinerariesList = new ArrayList();
         
         // FlightInfo
         String id = null;
         String departure;
         String arrival;
         int duration;
         int carrier;
         List<FlightInfoDTO> flightInfoList = new ArrayList();
         
         // Carriers
         List <CarriersDTO> carriersList = new ArrayList();
         carriers = response.getBody().getObject().getJSONArray("Carriers");
         int carriersId;
         String code;
         String carrierName;
         String imageUrlCarriers;
         
         // Agents
         List<AgentsDTO> agentsList = new ArrayList();
         JSONArray agentsIdList = response.getBody().getObject().getJSONArray("Agents");
         int agentsId;
         String agentsName;
         String imageUrlAgents;
        
         
         
         
         for(int i = 0; i < itineraries.length(); i++){
             pricingOptions = (JSONArray) itineraries.getJSONObject(i).get("PricingOptions");
             outboundLegId = itineraries.getJSONObject(i).get("OutboundLegId").toString();
             inboundLegId = itineraries.getJSONObject(i).get("InboundLegId").toString();
             
             for(int j = 0; j < pricingOptions.length(); j++){
                price = (double) pricingOptions.getJSONObject(j).get("Price");
                deeplinkUrl = pricingOptions.getJSONObject(j).get("DeeplinkUrl").toString();
                agentsIdList = (JSONArray) pricingOptions.getJSONObject(j).get("Agents");
                agents = (int) agentsIdList.getInt(0);
                
                ItinerariesDTO itinerariesDTO = new ItinerariesDTO(outboundLegId, inboundLegId, price, agents, deeplinkUrl);
                itinerariesList.add(itinerariesDTO);
             }
             
         }
         
         for(int i = 0; i < agentsIdList.length(); i++){
             agentsId = (int) agentsIdList.getJSONObject(i).get("Id");
             agentsName = agentsIdList.getJSONObject(i).get("Name").toString();
             imageUrlAgents = agentsIdList.getJSONObject(i).get("ImageUrl").toString();
             AgentsDTO agentsDTO = new AgentsDTO(agentsId, agentsName, imageUrlAgents);
             agentsList.add(agentsDTO);
         }
         
         for(int i = 0; i < carriers.length(); i++){
             carriersId = (int) carriers.getJSONObject(i).get("Id");
             code = carriers.getJSONObject(i).get("Code").toString();
             carrierName = carriers.getJSONObject(i).get("Name").toString();
             imageUrlCarriers = carriers.getJSONObject(i).get("ImageUrl").toString();
             CarriersDTO carriersDTO = new CarriersDTO(carriersId, code, carrierName, imageUrlCarriers);
             carriersList.add(carriersDTO);
         }
         
         for(int i = 0; i < segments.length(); i++){
             departure = segments.getJSONObject(i).get("DepartureDateTime").toString();
             arrival = segments.getJSONObject(i).get("ArrivalDateTime").toString();
             duration = (int) segments.getJSONObject(i).get("Duration");
             carrier = (int) segments.getJSONObject(i).get("Carrier");
             
         for(int j = 0; j < itinerariesList.size(); j++){
             if(itinerariesList.get(j).getOutboundLegId().equals(id)){
             FlightInfoDTO flightInfo = new FlightInfoDTO(id, originPlace, destinationPlace, departure, arrival, duration,
                     itinerariesList.get(j).getPrice(), itinerariesList.get(j).getDeeplinkUrl(), carriersList.get(j).getName(), agentsList.get(j).getImageUrlAgents());
                     
                for(int k = 0; k < agentsList.size(); k++){
                    if(agentsList.get(k).getId() == itinerariesList.get(j).getAgents()){
                    flightInfo.setAgentsName(agentsList.get(k).getName());
                    flightInfo.setImageUrl(agentsList.get(k).getImageUrlAgents());
                    }
                    
                for(int l = 0; l < carriersList.size(); l++){
                    if(carriersList.get(l).getId() == carrier){
                        flightInfo.setCarrierName(carriersList.get(l).getName());
                    }
                }
                    flightInfoList.add(flightInfo);
                    System.out.println(flightInfo);
                
                }
             
             }
         
         }
             
         }
         
         return flightInfoList;
         
    }
    
        public static void main(String[] args) throws InterruptedException, ExecutionException, UnirestException {
        SkyscannerFacade skyFacade = new SkyscannerFacade();
        // System.out.println(skyFacade.getFlightSearch("2019-12-20", "business", "SFO-sky", "LHR-sky", "2019-12-03", 3)); 
        
    
    
}

    
}

