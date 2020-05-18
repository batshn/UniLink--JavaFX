package model;


import java.io.Serializable;

public class Reply implements Serializable {
    private static final long serialVersionUID = 4004449628106466515L;
    private String postID;
    private double value;
    private String responderID;


    public Reply(String postID, double value, String responderID) {
        this.postID = postID;
        this.value = value;
        this.responderID =  responderID;
    }


    public String getPostID() {
        return postID;
    }


    public double getValue() {
        return value;
    }


    public String getResponderID( ) {
        return responderID;
    }

}

