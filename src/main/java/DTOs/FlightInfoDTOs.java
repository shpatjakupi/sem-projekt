
package DTOs;


public class FlightInfoDTOs 
    

{
    private String id;
    private String startDestination;
    private String endDestination;
    private String departure;
    private String arrival;
    private int duration;
    private double price;
    private String deeplinkUrl;
    private String agentsName;
    private String imageUrl;

    public FlightInfoDTOs() {
    }

    public FlightInfoDTOs(String id, String startDestination, String endDestination, String departure, String arrival, int duration, double price, String deeplinkUrl) {
        this.id = id;
        this.startDestination = startDestination;
        this.endDestination = endDestination;
        this.departure = departure;
        this.arrival = arrival;
        this.duration = duration;
        this.price = price;
        this.deeplinkUrl = deeplinkUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDestination() {
        return startDestination;
    }

    public void setStartDestination(String startDestination) {
        this.startDestination = startDestination;
    }

    public String getEndDestination() {
        return endDestination;
    }

    public void setEndDestination(String endDestination) {
        this.endDestination = endDestination;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
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

    public String getAgentsName() {
        return agentsName;
    }

    public void setAgentsName(String agentsName) {
        this.agentsName = agentsName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "FlightInfoDTO{" + "id=" + id + ", startDestination=" + startDestination + ", endDestination=" + endDestination + ", departure=" + departure + ", arrival=" + arrival + ", duration=" + duration + ", price=" + price + ", deeplinkUrl=" + deeplinkUrl + ", agentsName=" + agentsName + ", imageUrl=" + imageUrl + '}';
    }
    
    


    
    
}