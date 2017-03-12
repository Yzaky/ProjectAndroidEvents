package etien.projectandroidevents;

public class OneEvent {

    public String title;
    public String description;
    public String start_time;
    public double longitude;
    public double latitude;

    public OneEvent(){}

    public OneEvent( String title, String description, String start_time,
                     double longitude, double latitude){
        this.title = title;
        this.description = description;
        this.start_time = start_time;
        this.longitude = longitude;
        this.latitude = latitude;
    }

}
