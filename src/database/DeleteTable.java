package database;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeleteTable {
    public static void main(String[] args) throws SQLException {

        final String DB_NAME = "UniLink";
        final String TABLE_POST = "post";
        final String TABLE_REPLY = "reply";

        //use try-with-resources Statement
        try (Connection con = DBConnection.getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate("DROP TABLE post");

            if (result == 0)
                System.out.println("Table " + TABLE_POST + " has been deleted successfully");
            else
                System.out.println("Table " + TABLE_POST + " was not deleted");

            result = stmt.executeUpdate("DROP TABLE reply");

            if (result == 0)
                System.out.println("Table " + TABLE_REPLY + " has been deleted successfully");
            else
                System.out.println("Table " + TABLE_REPLY + " was not deleted");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
