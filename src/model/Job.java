package model;

import model.exception.InvalidOfferPriceException;
import model.exception.InvalidUserReply;
import model.exception.PostCloseException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Job extends Post{
    private double proposedPrice;
    private double lowestOffer;
    public static int idGen=0;


    public Job(String title, String description, String creatorID, String image, double proposedPrice) {
        super("JOB"+ String.format("%03d" , ++idGen), title, description, creatorID, image);
        this.proposedPrice = proposedPrice;
        this.lowestOffer = 0;
    }


    public double getProposedPrice() {
        return proposedPrice;
    }


    public double getLowestOffer() {
        return lowestOffer;
    }


    @Override
    public String getPostDetails() {
        String offer = (lowestOffer>0) ? Double.toString(lowestOffer) : "NO OFFER";
        return super.getPostDetails() + String.format("%-20s %-20s %n%-20s %-20s %n", "Proposed price:", proposedPrice, "Lowest offer:", offer);
    }


    @Override
    public void handleReply(Reply reply) throws PostCloseException, InvalidUserReply, InvalidOfferPriceException {
        boolean success = false;
        double temp = lowestOffer;
        if (temp == 0) temp = proposedPrice;
        if (temp > reply.getValue() && reply.getValue() < proposedPrice) {
            super.addReplyToPost(reply);
            lowestOffer = reply.getValue();
        }
        else
            throw new InvalidOfferPriceException("Your offer is higher than proposed price or lowest offer!");
    }


    @Override
    public String getReplyDetails() {
        String offerList = "";

        ArrayList<Reply> copyRelpList = super.getReplyList();
        Comparator<Reply> compareByOffer = (Reply r1, Reply r2) -> Double.compare(r1.getValue(), r2.getValue());
        Collections.sort(copyRelpList,compareByOffer);

        for (Reply r: copyRelpList) {
            offerList = offerList  + String.format("%-10s %-10s %n", r.getResponderID() + ":", r.getValue());
        }

        offerList = (offerList.equalsIgnoreCase("")) ? "\n\nOffer History : EMPTY\n " : "\n\n-- Offer History --\n" + offerList;

        return offerList;
    }
}

