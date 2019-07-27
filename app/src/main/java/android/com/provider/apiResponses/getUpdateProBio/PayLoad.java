package android.com.provider.apiResponses.getUpdateProBio;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayLoad {

    @SerializedName("user_id")
    @Expose
    private Integer userId;

    @SerializedName("bio")
    @Expose
    private String bio;

    @SerializedName("starttime")
    @Expose
    private String starttime;

    @SerializedName("endtime")
    @Expose
    private String endtime;

    @SerializedName("servicetypes")
    @Expose
    private List<Servicetype> servicetypes = null;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public List<Servicetype> getServicetypes() {
        return servicetypes;
    }

    public void setServicetypes(List<Servicetype> servicetypes) {
        this.servicetypes = servicetypes;
    }

}