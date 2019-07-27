package android.com.provider.apiResponses.displayProviderProfile;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DisplayProfile {

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;

    @SerializedName("isError")
    @Expose
    private Boolean isError;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("payLoad")
    @Expose
    private String payLoad;


    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public Boolean getIsError() {
        return isError;
    }

    public void setIsError(Boolean isError) {
        this.isError = isError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }


}