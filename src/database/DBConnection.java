package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    public static void main(String[] args) {
        final String DB_Name ="UniLink";

        try(Connection con = getConnection(DB_Name)) {
            System.out.println("Connection to database " + DB_Name + " created successfully")    ;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/" + dbName, "SA", "");

        return con;
    }

}
