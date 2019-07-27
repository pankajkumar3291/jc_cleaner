package android.com.provider.apiResponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
public class UpdateBioServiceModel implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public UpdateBioServiceModel setId(String id) {
        this.id = id;
        return this;
    }

}
