package database;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        final String DB_NAME = "UniLink";
        final String TABLE_POST = "POST";
        final String TABLE_REPLY = "REPLY";

        try (Connection con = DBConnection.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {


            // create post table
            int  result = stmt.executeUpdate("CREATE TABLE post("
                                                                    + "post_id varchar(10) not null,"
                                                                    + "title varchar(100) not null,"
                                                                    + "description varchar(200) ,"
                                                                    + "creator_id varchar(3) not null,"
                                                                    + "status varchar(6) not null,"
                                                                    + "image varchar(10) not null,"
                                                                    + "ev_venue varchar(100) null,"
                                                                    + "ev_date datetime null,"
                                                                    + "ev_capacity int null,"
                                                                    + "sl_asking_price float null,"
                                                                    + "sl_minimum_raise int null,"
                                                                    + "jb_proposed_price float null,"
                                                                    + "PRIMARY KEY(post_id))");
            if (result ==0 )
                System.out.println("Table " + TABLE_POST + " has been created successfully");
            else
                System.out.println("Table " + TABLE_POST + " is not created");


            // create reply table
            result = stmt.executeUpdate("CREATE TABLE reply("
                    + "post_id varchar(10) not null,"
                    + "amount float null,"
                    + "responder_id varchar(10) not null)");

            if (result ==0 )
                System.out.println("Table " + TABLE_REPLY + " has been created successfully");
            else
                System.out.println("Table " + TABLE_REPLY + " is not created");



        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
