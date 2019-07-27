package android.com.provider.models;

public class CompletedAppointments {

    String title;
    String service;
    String address;
    String date;
    String time;


    public CompletedAppointments(String title, String service, String address, String date, String time) {
        this.title = title;
        this.service = service;
        this.address = address;
        this.date = date;
        this.time = time;
    }


    public String getTitle() {
        return title;
    }

    public String getService() {
        return service;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }


}
