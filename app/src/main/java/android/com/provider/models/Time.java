package android.com.provider.models;

public class Time {


    public String name;
    public String time;

    public Time(String shahzeb, String s) {

        this.name = shahzeb;
        this.time = s;


    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
