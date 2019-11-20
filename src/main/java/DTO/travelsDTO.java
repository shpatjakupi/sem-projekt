package DTO;


import java.io.Serializable;

public class travelsDTO implements Serializable /* tjek */{

   private int placeid;
   private String PlaceName;
   private String CountryId;
   private String CityId;
   private String CountryName;

    public travelsDTO(int placeid, String PlaceName, String CountryId, String CityId, String CountryName) {
        this.placeid = placeid;
        this.PlaceName = PlaceName;
        this.CountryId = CountryId;
        this.CityId = CityId;
        this.CountryName = CountryName;
    }

    @Override
    public String toString() {
        return "travelsDTO{" + "placeid=" + placeid + ", PlaceName=" + PlaceName + ", CountryId=" + CountryId + ", CityId=" + CityId + ", CountryName=" + CountryName + '}';
    }



   

}