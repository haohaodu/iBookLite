package sample;

public class Order {
    protected String order_number;
    protected String user_email;
    protected String ISBN13;
    protected String quantity;
    protected String order_total;
    protected String order_time;
    protected String tracking_number;
    protected String current_location;

    public Order() {

    }

    public Order (String orderNum, String userEmail, String isbn, String q, String orderTotal, String orderTime, String trackingNumber, String currentLocation) {
        order_number = orderNum;
        user_email = userEmail;
        ISBN13 = isbn;
        quantity = q;
        order_total = orderTotal;
        order_time = orderTime;
        tracking_number = trackingNumber;
        current_location = currentLocation;
    }

    public String getTrackingNumber() {
        return tracking_number;
    }
    public String getCurrentLocation() {
        return current_location;
    }
}