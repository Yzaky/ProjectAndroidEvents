package etien.projectandroidevents;

public class OneEvent {

    public String title;
    public String description;
    public String start_time;
    public double longitude;
    public double latitude;
    public String country_name;
    public String region_name;
    public String city_name;
    public String venue_address;
    public String image_url;

    public OneEvent(){}

    public OneEvent( String title, String description, String start_time,
                     double longitude, double latitude,
                     String country_name, String region_name, String city_name,
                     String venue_address, String image_url ){
        this.title = title;
        this.description = description;
        this.start_time = start_time;
        this.longitude = longitude;
        this.latitude = latitude;
        this.country_name = country_name;
        this.region_name = region_name;
        this.city_name = city_name;
        this.venue_address = venue_address;
        this.image_url = image_url;

    }

}
