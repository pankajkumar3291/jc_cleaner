package android.com.provider.models;

import java.io.Serializable;

public class OpenApportunity implements Serializable {

    private String title;
    private String services;
    private String address;
    private String Date;
    private String Time;
    private String jobId;
    private String custometId;


    public OpenApportunity(String title, String services, String address, String date, String time, String job_Id, String customet_Id) {

        this.title = title;
        this.services = services;
        this.address = address;
        this.Date = date;
        this.Time = time;
        this.jobId = job_Id;
        this.custometId = customet_Id;
    }


    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getCustometId() {
        return custometId;
    }

    public void setCustometId(String custometId) {
        this.custometId = custometId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }


}
