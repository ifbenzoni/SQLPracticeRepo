package testing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateTable {
	
	public static void main( String args[] ) {
		
		 String url = "jdbc:mysql://localhost:3306/dbPractice";
		 String username = "java";
		 String password = "password";

		 System.out.println("Connecting database...");

		 try (Connection connection = DriverManager.getConnection(url, username, password)) {
		     System.out.println("Database connected!");
		     
		     createTable(connection);
		     deleteTable(connection);
		 } catch (SQLException e) {
		     throw new IllegalStateException("Cannot connect the database!", e);
		 }
		 
	}

	public static void createTable(Connection con) throws SQLException {
	    String query = "CREATE TABLE Category1 ("
	    	+ "id BIGINT NOT NULL AUTO_INCREMENT, "
	    	+ "item VARCHAR(255), "
	    	+ "description VARCHAR(255), "
	    	+ "PRIMARY KEY (id)"
	    	+ ")";
	    try (Statement stmt = con.createStatement()) {
	    	stmt.executeUpdate(query);
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
	}
	
	public static void deleteTable(Connection con) throws SQLException {
		String query = "DROP TABLE Category1";
	    try (Statement stmt = con.createStatement()) {
	    	stmt.executeUpdate(query);
	    } catch (SQLException e) {
	    	e.printStackTrace();
	    }
}

}
