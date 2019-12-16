/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import DTO.FlightInfoDTO;
import DTOs.AgentsDTOs;
import DTOs.FlightInfoDTOs;
import DTOs.ItinerariesDTOs;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.json.JSONArray;

/**
 *
 * @author Younes
 */
public class DataFromSkyscanner 
    
{

    public DataFromSkyscanner() {
    }

    public String getSessionKey(String outboundDate, String cabinClass, String originPlace, String destinationPlace, int adults) throws UnirestException {
        HttpResponse<JsonNode> response = Unirest.post("https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/v1.0")
                .header("X-RapidAPI-Host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                .header("X-RapidAPI-Key", "4dfa3d7cb0msh7701660655f1502p13c7cbjsn3a351650d218")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("outboundDate", outboundDate) // ("outboundDate", "2019-12-01")
                .field("cabinClass", cabinClass) // ("cabinClass", "business")
                .field("children", 0)
                .field("infants", 0)
                .field("country", "DK")
                .field("currency", "DKK")
                .field("locale", "da-DK")
                .field("originPlace", originPlace) // ("originPlace", "SFO-sky")
                .field("destinationPlace", destinationPlace) //("destinationPlace", "LHR-sky")
                .field("adults", adults) // ("adults", 1)
                .asJson();

        String sessionKey = "";
        String location = "";

        for (Map.Entry<String, List<String>> entry : response.getHeaders().entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            if (key.equals("Location")) {
                location = value.toString();
            }

        }

        System.out.println("----- location: " + location);
        for (int i = location.length() - 37; i < location.length() - 1; i++) {
            sessionKey += location.charAt(i);
        }

        return sessionKey;
    }

    public List<FlightInfoDTOs> getFlightData(String outboundDate, String cabinClass, String originPlace, String destinationPlace, int adults) throws UnirestException {
        String sessionkey = getSessionKey(outboundDate, cabinClass, originPlace, destinationPlace, adults);
        HttpResponse<JsonNode> response = Unirest.get(
                "https://skyscanner-skyscanner-flight-search-v1.p.rapidapi.com/apiservices/pricing/uk2/v1.0/"
                + sessionkey + "?pageIndex=0&pageSize=10")
                .header("X-RapidAPI-Host", "skyscanner-skyscanner-flight-search-v1.p.rapidapi.com")
                .header("X-RapidAPI-Key", "4dfa3d7cb0msh7701660655f1502p13c7cbjsn3a351650d218")
                .asJson();

        JSONArray listItineraries = response.getBody().getObject().getJSONArray("Itineraries");
        JSONArray listLegs = response.getBody().getObject().getJSONArray("Legs");
        JSONArray listPricingOptions;
        JSONArray listAgents;
        String outboundLegId;
        double price;
        String deeplinkUrl;
        int agents;
        List<ItinerariesDTOs> listItinerariesDTO = new ArrayList();

        // Flightinfo
        String id;
        String departure;
        String arrival;
        int duration;
        List<FlightInfoDTOs> listFlightInfoDTO = new ArrayList();

        //Agent info
        List<AgentsDTOs> listAgentsDTO = new ArrayList<>();
        JSONArray listAgentsId = response.getBody().getObject().getJSONArray("Agents");
        int agentsId;
        String agentsName;
        String imageUrl;

        for (int i = 0; i < listItineraries.length(); i++) {
            listPricingOptions = (JSONArray) listItineraries.getJSONObject(i).get("PricingOptions");
            outboundLegId = listItineraries.getJSONObject(i).get("OutboundLegId").toString();
            for (int j = 0; j < listPricingOptions.length(); j++) {
                price = (double) listPricingOptions.getJSONObject(j).get("Price");
                deeplinkUrl = listPricingOptions.getJSONObject(j).get("DeeplinkUrl").toString();
                listAgents = (JSONArray) listPricingOptions.getJSONObject(j).get("Agents");
                agents = (int) listAgents.getInt(0);
//                System.out.println("-----AGENTS: " + agents);
                ItinerariesDTOs itinerariesDTO = new ItinerariesDTOs(outboundLegId, price, deeplinkUrl, agents);
                listItinerariesDTO.add(itinerariesDTO);
                // System.out.println("DTO----------- " + itinerariesDTO);
            }
            //System.out.println("Legs: " + listLegs.length() + "---- Itine: " + listItineraries.length());
        }

        for (int i = 0; i < listAgentsId.length(); i++) {
            agentsId = (int) listAgentsId.getJSONObject(i).get("Id");
            agentsName = listAgentsId.getJSONObject(i).get("Name").toString();
            imageUrl = listAgentsId.getJSONObject(i).get("ImageUrl").toString();
            AgentsDTOs agentsDTO = new AgentsDTOs(agentsId, agentsName, imageUrl);
            listAgentsDTO.add(agentsDTO);
        }

        for (int i = 0; i < listLegs.length(); i++) {
            id = listLegs.getJSONObject(i).get("Id").toString();
            departure = listLegs.getJSONObject(i).get("Departure").toString();
            arrival = listLegs.getJSONObject(i).get("Arrival").toString();
            duration = (int) listLegs.getJSONObject(i).get("Duration");

            for (int j = 0; j < listItinerariesDTO.size(); j++) {
                if (listItinerariesDTO.get(j).getOutboundLegId().equals(id)) {
                            FlightInfoDTOs flightInfoDTO = new FlightInfoDTOs(id, originPlace, destinationPlace, departure, arrival, duration, listItinerariesDTO.get(j).getPrice(), listItinerariesDTO.get(j).getDeeplinkUrl());
                    for (int k = 0; k < listAgentsDTO.size(); k++) {
                        if (listAgentsDTO.get(k).getId() == listItinerariesDTO.get(j).getAgents()) {
                            flightInfoDTO.setAgentsName(listAgentsDTO.get(k).getName());
                            flightInfoDTO.setImageUrl(listAgentsDTO.get(k).getImageUrl());
                        }
                    }
                            listFlightInfoDTO.add(flightInfoDTO);
                            System.out.println(flightInfoDTO);

                }

            }
        }

        return listFlightInfoDTO;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException, UnirestException {
        DataFromSkyscanner swap = new DataFromSkyscanner();
        System.out.println(swap.getFlightData("2019-12-13", "business", "SFO-sky", "LHR-sky", 1));

    }

}
