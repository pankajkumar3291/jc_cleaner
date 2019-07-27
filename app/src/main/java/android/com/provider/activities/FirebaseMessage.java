package android.com.provider.activities;
public class FirebaseMessage {
    public FirebaseMessage() {
    }
    long id;
    String message;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
