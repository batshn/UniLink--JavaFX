package model;

import model.exception.InvalidOfferPriceException;
import model.exception.InvalidUserReply;
import model.exception.PostCloseException;

import java.io.Serializable;
import java.util.*;
import java.lang.String;

public abstract class Post implements Serializable {
    private static final long serialVersionUID = -5691819770409351189L;

    private String id;
    private String title;
    private String description;
    private String creatorID;
    private Status status;
    private String image;
    private ArrayList<Reply> replyList;


    public Post(String id, String title, String description, String creatorID, Status status ,String image) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creatorID = creatorID;
        this.status = status;
        if(image.compareTo("yes") == 0)
            this.image = id;
        else
            this.image = image;


        replyList = new ArrayList<Reply>();
    }


    public String getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getCreatorID() {
        return creatorID;
    }
    public Status getStatus() {
        return status;
    }
    public String getImage() {
        return image;
    }

    public void setID(String upID) { this.id = upID;}
    public void setTitle(String upTitle) {
        this.title = upTitle;
    }
    public void setDescription(String upDesc) {
        this.description = upDesc;
    }
    public void setImage(String upImage) {
        this.image = upImage;
    }


    public void setStatus() throws PostCloseException {
        if(status == Status.OPEN)
            status = Status.CLOSED;
        else
            throw new PostCloseException("Post is already closed!");
    }


    public ArrayList<Reply> getReplyList() {
        return replyList;
    }


    public abstract void handleReply(Reply reply) throws PostCloseException, InvalidUserReply, InvalidOfferPriceException;
    public abstract String getReplyDetails();


    public String getPostDetails() {
        return String.format("%-20s %-20s %n%-20s %-20s %n%-20s %-20s %n%-20s %-20s %n%-20s %-20s %n", "ID:", id, "Title:", title, "Desciption:", description, "Creator ID:", creatorID, "Status:", status);
    }


    public void addReplyToPost(Reply rp) throws PostCloseException, InvalidUserReply {
        boolean canRespond = true;
        if (rp.getResponderID().compareTo(this.creatorID) == 0) {
            throw new InvalidUserReply("Post creator cannot reply own post!");
        }
        else if (this.status == Status.CLOSED) {
            throw new PostCloseException("Post is closed!");
        }
        else {
            for (Reply r : replyList) {
                if (r.getPostID().compareTo(rp.getPostID()) == 0 && r.getResponderID().compareTo(rp.getResponderID()) == 0) {
                    canRespond = false;
                    break;
                }
            }
            if (canRespond)
                this.replyList.add(rp);
            else
                throw new InvalidUserReply("You have already replied this post!");
        }

    }

    // reply from db or file
    public void addReplyToPostFromDbOrFile(Reply rp) {
        this.replyList.add(rp);
    }

}
