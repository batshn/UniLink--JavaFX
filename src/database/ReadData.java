package database;
import main.UniLinkGUI;
import model.*;

import java.sql.*;
import java.time.LocalDate;

public class ReadData {
    final static String DB_NAME = "UniLink";
    final static String TABLE_POST = "post";
    final static String TABLE_REPLY = "reply";

    public static void  readPost() {
        try (Connection con = DBConnection.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            ResultSet postRows = stmt.executeQuery("SELECT * FROM " +TABLE_POST+ ";");

            while (postRows.next()) {
                Status st = Status.CLOSED;
                if (postRows.getString("status").equals("OPEN"))
                    st =Status.OPEN;

                if(postRows.getString("post_id").substring(0,3).equals("EVE")) {
                    Post ev = new Event(postRows.getString("post_id").stripTrailing(),
                                        postRows.getString("title").stripTrailing(),
                                        postRows.getString("description").stripTrailing(),
                                        postRows.getString("creator_id").stripTrailing(),
                                        st,
                                        postRows.getString("ev_venue").stripTrailing(),
                                        postRows.getDate("ev_date").toLocalDate(),
                                        postRows.getInt("ev_capacity"),
                                        0,
                                        postRows.getString("image").stripTrailing());
                    ((Event)ev).setAttendeeCount((int)readReply(ev,stmt));
                    UniLinkGUI.postList.add(ev);
                } else if (postRows.getString("post_id").substring(0,3).equals("SAL")) {
                    Post sal = new Sale(postRows.getString("post_id").stripTrailing(),
                            postRows.getString("title").stripTrailing(),
                            postRows.getString("description").stripTrailing(),
                            postRows.getString("creator_id").stripTrailing(),
                            st,
                            postRows.getDouble("sl_asking_price"),
                            postRows.getInt("sl_minimum_raise"),
                            0,
                            postRows.getString("image").stripTrailing());
                    ((Sale)sal).setHighOffer(readReply(sal,stmt));
                    UniLinkGUI.postList.add(sal);
                } else
                {
                    Post job = new Job(postRows.getString("post_id").stripTrailing(),
                            postRows.getString("title").stripTrailing(),
                            postRows.getString("description").stripTrailing(),
                            postRows.getString("creator_id").stripTrailing(),
                            st,
                            postRows.getDouble("jb_proposed_price"),
                            0,
                            postRows.getString("image").stripTrailing());
                    ((Job)job).setLowestOffer(readReply(job,stmt));
                    UniLinkGUI.postList.add(job);
                }

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static double readReply(Post post, Statement stdet) throws SQLException {
        double result = 0, maxValue = 0, minValue = 0 ;
       ResultSet replyRows = stdet.executeQuery("SELECT * FROM " +TABLE_REPLY+ " WHERE post_id = '" +post.getId()+ "';");
        while (replyRows.next()) {
            Reply rp = new Reply(replyRows.getString("post_id"), replyRows.getDouble("amount"), replyRows.getString("responder_id"));
            post.addReplyToPostFromDbOrFile(rp);

            if(maxValue< replyRows.getDouble("amount"))
                maxValue = replyRows.getDouble("amount");

            if (minValue >  replyRows.getDouble("amount"))
                minValue =  replyRows.getDouble("amount");
        }

        if(post instanceof Event)
            result = replyRows.getRow();
        else if( post instanceof Sale)
            result = maxValue;
        else
            result = minValue;

        return result;
    }

}
