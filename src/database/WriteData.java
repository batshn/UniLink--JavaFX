package database;
import main.UniLinkGUI;
import model.*;

import java.sql.*;


public class WriteData {
        final static String DB_NAME = "UniLink";
        final static String TABLE_POST = "post";
        final static String TABLE_REPLY = "reply";

        public static void  insertData() {
            try (Connection con = DBConnection.getConnection(DB_NAME);
                 Statement stmt = con.createStatement();
            ) {
                String query = "TRUNCATE TABLE " + TABLE_POST;
                stmt.executeUpdate(query);

                query = "TRUNCATE TABLE " + TABLE_REPLY;
                stmt.executeUpdate(query);


                if(UniLinkGUI.postList.isEmpty() == false) {
                    PreparedStatement ps,psRep;
                    int cntPost = 0;
                    int cntReply = 0;
                    Double dnull = null;
                    Integer inull = null;
                   for(Post pt: UniLinkGUI.postList) {
                       ps = con.prepareStatement("INSERT INTO "+TABLE_POST+" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
                       ps.setString(1, pt.getId());
                       ps.setString(2, pt.getTitle());
                       ps.setString(3, pt.getDescription());
                       ps.setString(4, pt.getCreatorID());
                       ps.setString(5, pt.getStatus().toString());
                       ps.setString(6, pt.getImage());
                       if (pt instanceof Event) {
                           ps.setString(7, ((Event)pt).getVenue());
                           ps.setDate(8, Date.valueOf(((Event)pt).getDate()));
                           ps.setInt(9, ((Event)pt).getCapacity());
                       }
                       else {
                           ps.setNull(7, Types.VARCHAR);
                           ps.setNull(8, Types.DATE);
                           ps.setNull(9, Types.INTEGER);
                       }

                       if (pt instanceof Sale) {
                           ps.setDouble(10, ((Sale)pt).getAskPrice());
                           ps.setInt(11, ((Sale)pt).getMinRaise());
                       }
                       else {
                           ps.setNull(10, Types.DOUBLE);
                           ps.setNull(11, Types.INTEGER);
                       }

                       if (pt instanceof Job)
                           ps.setDouble(12, ((Job)pt).getProposedPrice());
                       else
                           ps.setNull(12, Types.DOUBLE);

                       ps.executeUpdate();
                       cntPost++;

                       // insert into reply
                       for(Reply r : pt.getReplyList()) {
                            psRep = con.prepareStatement("INSERT INTO "+TABLE_REPLY+" VALUES(?, ?, ?)");
                            psRep.setString(1, r.getPostID());
                            psRep.setDouble(2, r.getValue());
                            psRep.setString(3, r.getResponderID());
                            psRep.executeUpdate();
                            cntReply++;
                       }
                   }

                    System.out.println(String.valueOf(cntPost) +" posts and " + String.valueOf(cntReply) +" replies have been recorded.");
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

}
