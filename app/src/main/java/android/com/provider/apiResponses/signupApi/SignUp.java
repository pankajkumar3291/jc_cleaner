package android.com.provider.apiResponses.signupApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class SignUp{

    @SerializedName("isSuccess")
    @Expose
    private Boolean isSuccess;

    @SerializedName("isError")
    @Expose
    private Boolean isError;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("payload")
    @Expose
    private Payload payload;

    public Boolean getIsSuccess(){
        return isSuccess;
    }
    public void setIsSuccess(Boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public Boolean getIsError(){
        return isError;
    }

    public void setIsError(Boolean isError){
        this.isError = isError;
    }

    public String getMessage(){

        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public Payload getPayload(){
        return payload;
    }

    public void setPayload(Payload payload){
        this.payload = payload;
    }

}