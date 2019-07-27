package android.com.provider.apiResponses.checkInRequest;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckInPayload {

    @SerializedName("CheckIn Time")
    @Expose
    private String checkInTime;

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

}