package DTO;


import java.io.Serializable;

public class PlacesDTO implements Serializable /* tjek */{

   private int placeid;
   private String PlaceName;
   private String CountryId;
   private String RegionId;
   private String CityId;
   private String CountryName;

    public PlacesDTO(int placeid, String PlaceName, String CountryId, String RegionId, String CityId, String CountryName) {
        this.placeid = placeid;
        this.PlaceName = PlaceName;
        this.CountryId = CountryId;
        this.RegionId = RegionId;
        this.CityId = CityId;
        this.CountryName = CountryName;
    }

    @Override
    public String toString() {
        return "PlacesDTO{" + "placeid=" + placeid + ", PlaceName=" + PlaceName + ", CountryId=" + CountryId + ", CityId=" + CityId + ", CountryName=" + CountryName + '}';
    }

    public int getPlaceid() {
        return placeid;
    }

    public void setPlaceid(int placeid) {
        this.placeid = placeid;
    }

    public String getPlaceName() {
        return PlaceName;
    }

    public void setPlaceName(String PlaceName) {
        this.PlaceName = PlaceName;
    }

    public String getCountryId() {
        return CountryId;
    }

    public void setCountryId(String CountryId) {
        this.CountryId = CountryId;
    }

    public String getRegionId() {
        return RegionId;
    }

    public void setRegionId(String RegionId) {
        this.RegionId = RegionId;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String CityId) {
        this.CityId = CityId;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String CountryName) {
        this.CountryName = CountryName;
    }

    


   

}