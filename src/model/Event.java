package model;


import model.exception.InvalidOfferPriceException;
import model.exception.InvalidUserReply;
import model.exception.PostCloseException;
import java.time.LocalDate;
import java.util.Date;

public class Event extends Post {
    private String venue;
    private LocalDate date;
    private int capacity;
    private int attendeeCount;
    public static int idGen=0;


    public Event(String title, String description, String creatorID, String venue, LocalDate date, int capacity, String image) {
        super("EVE"+ String.format("%03d" , ++idGen), title, description, creatorID, image);
        this.venue = venue;
        this.date = date;
        this.capacity = capacity;
        attendeeCount = 0;
    }


    public String getVenue() {
        return venue;
    }
    public LocalDate getDate() {
        return date;
    }
    public int getCapacity( ) {
        return capacity;
    }
    public int getAttendeeCount( ) {
        return attendeeCount;
    }

    public void setVenue(String upVenue) { this.venue = upVenue;}
    public void setCapacity(int upCapacity) { this.capacity = upCapacity;}
    public void setDate(LocalDate upDate) { this.date = upDate;}


    @Override
    public String getPostDetails() {
        return super.getPostDetails() + String.format("%-20s %-20s %n%-20s %-20s %n%-20s %-20s %n%-20s %-20s %n", "Venue:", venue, "Date:", date, "Capacity:", capacity, "Attendees:", attendeeCount);
    }


    @Override
    public void handleReply(Reply reply) throws PostCloseException, InvalidOfferPriceException, InvalidUserReply {
        if (capacity > attendeeCount) {
            super.addReplyToPost(reply);
            if(++attendeeCount == capacity)
                    super.setStatus();
        }
        else
            throw new InvalidUserReply("Capacity is full!");
    }


    @Override
    public String getReplyDetails() {
        String attendList = "";
        for (Reply r: super.getReplyList()) {
            attendList = attendList  + r.getResponderID() + ", ";
        }
        if (attendList.isEmpty()) attendList = "Empty";

        return String.format("%-20s %-20s\n", "Attendee list:", attendList);
    }

}

