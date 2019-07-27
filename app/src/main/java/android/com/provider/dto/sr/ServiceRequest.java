package android.com.provider.dto.sr;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServiceRequest {

    @Expose
    @SerializedName("servicetype")
    private List<Servicetype> servicetype;

    @Expose
    @SerializedName("endtime")
    private String endtime;

    @Expose
    @SerializedName("starttime")
    private String starttime;

    @Expose
    @SerializedName("bio")
    private String bio;

    @Expose
    @SerializedName("id")
    private String id;



    public List<Servicetype> getServicetype() {
        return servicetype;
    }


    public void setServicetype(List<Servicetype> servicetype) {
        this.servicetype = servicetype;
    }


    public String getEndtime() {
        return endtime;
    }


    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }


    public String getStarttime() {
        return starttime;
    }


    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }


    public String getBio() {
        return bio;
    }


    public void setBio(String bio) {
        this.bio = bio;
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public static class Servicetype {

        @Expose
        @SerializedName("id")
        private int id;

        public Servicetype(Integer id) {
            this.id=id;
        }


        public int getId() {
            return id;
        }


        public void setId(int id) {
            this.id = id;
        }

    }


}
