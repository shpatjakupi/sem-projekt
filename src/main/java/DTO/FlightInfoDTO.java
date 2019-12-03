/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author Younes
 */
public class FlightInfoDTO {
    private String id;
    private String origin;
    private String destination;
    private String departure;
    private String arrival;
    private int flightDuration;
    private double price;
    private String deepLinkUrl;
    private String agentsName;
    private String carrierName;
    private String imageUrl;
    
    //private String carrierName; 

    public FlightInfoDTO(String id, String origin, String destination, String departure, String arrival, int flightDuration, double price, String deepLinkUrl, String agentsName, String imageUrl) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.departure = departure;
        this.arrival = arrival;
        this.flightDuration = flightDuration;
        this.price = price;
        this.deepLinkUrl = deepLinkUrl;
        this.agentsName = agentsName;
        this.carrierName = carrierName;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public int getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(int flightDuration) {
        this.flightDuration = flightDuration;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDeepLinkUrl() {
        return deepLinkUrl;
    }

    public void setDeepLinkUrl(String deepLinkUrl) {
        this.deepLinkUrl = deepLinkUrl;
    }

    public String getAgentsName() {
        return agentsName;
    }

    public void setAgentsName(String agentsName) {
        this.agentsName = agentsName;
    }

    public String getCarrierName() {
        return carrierName;
    }

    public void setCarrierName(String carrierName) {
        this.carrierName = carrierName;
    }
    
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    
    
    
    

}