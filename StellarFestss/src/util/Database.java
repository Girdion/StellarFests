package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private final String USERNAME = "root";
    private final String PASSWORD = "";
    private final String DATABASE = "stellarfest";
    private final String HOST = "localhost:3306";
    private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOST, DATABASE);
    
    @SuppressWarnings("exports")
	public ResultSet rs;
	@SuppressWarnings("exports")
	public ResultSetMetaData rsm;
    private Connection con;
    private Statement st;
    private static Database connect;

    public static Database getInstance() {
        if (connect == null) {
            connect = new Database(); // Assign the newly created instance to connect
        }
        return connect;
    }


    private Database() {
        try {
            con = DriverManager.getConnection(CONNECTION, USERNAME, PASSWORD);
            st = con.createStatement();

            // Check if the connection is successful
            if (con != null) {
                System.out.println("Connected to the database successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
        }
    }

    // Method to test the connection
    public boolean testConnection() {
        try {
            // Try executing a simple query to verify the connection
            if (con != null && !con.isClosed()) {
                // Using a simple query to verify the connection
                ResultSet rs = st.executeQuery("SELECT 1");
                if (rs.next()) {
                    return true; // Connection is successful
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Connection failed
    }

    @SuppressWarnings("exports")
	public ResultSet execQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

    public void execUpdate(String query) {
        try {
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PreparedStatement prepareStatement(String query) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }

    public void close() {
        try {
            if (st != null) st.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
