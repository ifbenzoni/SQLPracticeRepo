package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * controller for connecting to database
 * 
 * @author Isaiah
 */
public class APIController {
	
	/**
	 * adds table to database
	 * @param name name of new table
	 */
	public void newCategory(String name) {
		
		String url = "jdbc:mysql://localhost:3306/dbPractice";
		String username = "java";
		String password = "password";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
		    
			String query = "CREATE TABLE " + name + " ("
			    	+ "id BIGINT NOT NULL AUTO_INCREMENT, "
			    	+ "item VARCHAR(255), "
			    	+ "description VARCHAR(255), "
			    	+ "PRIMARY KEY (id)"
			    	+ ")";
			try (Statement stmt = connection.createStatement()) {
				stmt.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		 
	}
	
	/**
	 * removes table from database
	 * @param name name of table to remove
	 */
	public void deleteCategory(String name) {
		
		String url = "jdbc:mysql://localhost:3306/dbPractice";
		String username = "java";
		String password = "password";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			
			String query = "DROP TABLE " + name;
			try (Statement stmt = connection.createStatement()) {
				stmt.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		 
	}
	
	/**
	 * gets list of all tables in database
	 * @return list of tables (categories)
	 */
	public List<String> getCategories() {
		
		String url = "jdbc:mysql://localhost:3306/dbPractice";
		String username = "java";
		String password = "password";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			
			String query = "SELECT TABLE_NAME "
					+ "FROM INFORMATION_SCHEMA.TABLES "
					+ "WHERE TABLE_TYPE = 'BASE TABLE' "
					+ "AND TABLE_SCHEMA = 'dbPractice'";
			try (Statement stmt = connection.createStatement()) {
				ResultSet resultSet = stmt.executeQuery(query);
				List<String> categoryTitles = new ArrayList<String>();
				while(resultSet.next()) {
					String tableName = resultSet.getString("TABLE_NAME");
					categoryTitles.add(tableName);
				}
				return categoryTitles;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		
		return null;
		
	}
	
	/**
	 * adds item to table
	 * @param category name of table
	 * @param itemName name of item
	 * @param description description of item
	 */
	public void addItem(String category, String itemName, String description) {
		
		String url = "jdbc:mysql://localhost:3306/dbPractice";
		String username = "java";
		String password = "password";
		
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			
			String query = "INSERT INTO " + category + " (item, description) "
					+ "VALUE (\"" + itemName + "\", \"" + description + "\")";
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} catch (SQLException e) {
			throw new IllegalArgumentException("Cannot connect the database!", e);
		}
		
	}
	
	/**
	 * removes item from specified category based on id
	 * @param category name of table
	 * @param id id of row to remove
	 */
	public void removeItem(String category, Integer id) {
		
		String url = "jdbc:mysql://localhost:3306/dbPractice";
		String username = "java";
		String password = "password";
		
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			
			String query = "DELETE FROM " + category + " "
					+ "WHERE id=" + id;
			try (Statement statement = connection.createStatement()) {
				statement.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} catch (SQLException e) {
			throw new IllegalArgumentException("Cannot connect the database!", e);
		}
		
	}
	
	/**
	 * get data from specified table
	 * @param Category name of table
	 * @return 2d array of data
	 */
	public String[][] getData(String Category) {
		
		String url = "jdbc:mysql://localhost:3306/dbPractice";
		String username = "java";
		String password = "password";

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			
			String query = "SELECT COUNT(*) AS recordCount FROM " + Category;
			String query2 = "SELECT * FROM " + Category;
			try (Statement stmt = connection.createStatement()) {
				
				ResultSet resultSet = stmt.executeQuery(query);
				resultSet.next();
				int size = resultSet.getInt("recordCount");
				String[][] data = new String[size][3];
				
				resultSet = stmt.executeQuery(query2);
				int row = 0;
				while(resultSet.next()) {
					String id = resultSet.getString("id");
					String itemName = resultSet.getString("item");
					String description = resultSet.getString("description");
					data[row][0] = id;
					data[row][1] = itemName;
					data[row][2] = description;
					row++;
				}
				return data;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot connect the database!", e);
		}
		
		return null;
		
	}

}
