package android.com.provider.apiResponses.providerFullDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayLoad {

    @SerializedName("UserDetails")
    @Expose
    private UserDetails userDetails;

    @SerializedName("Address")
    @Expose
    private String address;

    @SerializedName("State")
    @Expose
    private String state;

    @SerializedName("Cityname")
    @Expose
    private String cityname;

    @SerializedName("Zipcode")
    @Expose
    private String zipcode;

    @SerializedName("State_id")
    @Expose
    private String State_id;

    @SerializedName("City_id")
    @Expose
    private String City_id;


    @SerializedName("Zipcode_id")
    @Expose
    private String Zipcode_id;

    @SerializedName("Service_name")
    @Expose
    private String Service_name;


    public String getService_name() {
        return Service_name;
    }

    public void setService_name(String service_name) {
        Service_name = service_name;
    }


    public String getZipcode_id() {
        return Zipcode_id;
    }

    public void setZipcode_id(String zipcode_id) {
        Zipcode_id = zipcode_id;
    }


    public String getCity_id() {
        return City_id;
    }

    public void setCity_id(String city_id) {
        City_id = city_id;
    }


    public String getState_id() {
        return State_id;
    }

    public void setState_id(String state_id) {
        State_id = state_id;
    }


    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

}