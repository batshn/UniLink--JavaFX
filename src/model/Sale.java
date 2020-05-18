package model;

import model.exception.InvalidOfferPriceException;
import model.exception.InvalidUserReply;
import model.exception.PostCloseException;

import java.util.*;

public class Sale extends Post {
    private static final long serialVersionUID = -8126848360600802908L;
    private double askPrice;
    private double highestOffer;
    private int minRaise;
    public static int idGen=0;


    public Sale(String title, String description, String creatorID, double askPrice, int minRaise, String image) {
        super("SAL"+ String.format("%03d" , ++idGen), title, description, creatorID, Status.OPEN, image);
        this.askPrice = askPrice;
        this.minRaise = minRaise;
        highestOffer = 0;
    }

    // constructor from db or file
    public Sale(String id, String title, String description, String creatorID, Status status ,double askPrice, int minRaise, double highestOffer ,String image) {
        super(id, title, description, creatorID, status ,image);
        this.askPrice = askPrice;
        this.minRaise = minRaise;
        this.highestOffer = highestOffer;
        ++idGen;
    }


    public double getAskPrice() {
        return askPrice;
    }
    public double getHighestOffer() {
        return highestOffer;
    }
    public int getMinRaise() {
        return minRaise;
    }

    public void setAskPrice(double upAskPrice){ this.askPrice = upAskPrice;}
    public void setMinRaise(int upMinRaise){ this.minRaise = upMinRaise;}
    public void setHighOffer(double upHighOffer){ this.highestOffer = upHighOffer;}


    @Override
    public String getPostDetails() {
        String offer = (highestOffer>0) ? Double.toString(highestOffer) : "NO OFFER";
        return super.getPostDetails() + String.format("%-20s %-20s %n%-20s %-20s %n", "Minimum raise:", minRaise, "Highest offer:", offer);
    }


    @Override
    public void handleReply(Reply reply) throws PostCloseException, InvalidUserReply, InvalidOfferPriceException {
        if (reply.getValue() - highestOffer >= minRaise) {
                super.addReplyToPost(reply);
                if(reply.getValue() >= askPrice) {
                    super.setStatus();
                }
                highestOffer = reply.getValue();
        }
        else
            throw new InvalidOfferPriceException("Your offer is less than minimum raise!");

    }


    @Override
    public String getReplyDetails() {
        String offerList = "";

        ArrayList<Reply> copyRelpList = super.getReplyList();
        Comparator<Reply> compareByOffer = (Reply r1, Reply r2) -> Double.compare(r1.getValue(), r2.getValue());
        Collections.sort(copyRelpList,compareByOffer.reversed());

        for (Reply r: copyRelpList) {
            offerList = offerList  + String.format("%-10s %-10s %n", r.getResponderID() + ":", r.getValue());
        }

        offerList = (offerList.equalsIgnoreCase("")) ? "\n\nOffer History : EMPTY\n " : "\n\n-- Offer History --\n" + offerList;

        return offerList;
    }






}

