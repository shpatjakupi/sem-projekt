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
public class ItinerariesDTO {
    
    private String outboundLegId;
    private String inboundLegId;
    private double price;
    private String deeplinkUrl;

    public ItinerariesDTO(String outboundLegId, String inboundLegId, double price, String deeplinkUrl) {
        this.outboundLegId = outboundLegId;
        this.inboundLegId = inboundLegId;
        this.price = price;
        this.deeplinkUrl = deeplinkUrl;
    }

    public String getOutboundLegId() {
        return outboundLegId;
    }

    public void setOutboundLegId(String outboundLegId) {
        this.outboundLegId = outboundLegId;
    }

    public String getInboundLegId() {
        return inboundLegId;
    }

    public void setInboundLegId(String inboundLegId) {
        this.inboundLegId = inboundLegId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDeeplinkUrl() {
        return deeplinkUrl;
    }

    public void setDeeplinkUrl(String deeplinkUrl) {
        this.deeplinkUrl = deeplinkUrl;
    }
    
    
    
}
