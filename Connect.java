package test;

import java.sql.*;


public class Connect {

	private final String USERNAME = "root";
	private final String PASSWORD = "";
	private final String HOSTNAME = "localhost:3306";
	private final String DATABASE = "transaction";
	private final String CONNECTION = String.format("jdbc:mysql://%s/%s", HOSTNAME , DATABASE);
	
	private Connection con;
	private Statement st;
	
	private Connect() {
		try {
			con = DriverManager.getConnection(CONNECTION,USERNAME,PASSWORD);
			st = con.createStatement();
		} catch (SQLException e) {

			e.printStackTrace();
		}
	
	}
private static Connect instance;
	
	public static Connect getInstance() {
		
		if (instance==null) {
			instance = new Connect();
		}
		return instance;
	}
	//SHOW DATA
	public ResultSet rs;
	public ResultSetMetaData rsm;
	
	// function select
	public ResultSet executeQuery(String query) {
		try {
			rs = st.executeQuery(query);
			rsm = rs.getMetaData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return rs;
		}
	// Function MOdif
	
	public void executeUpdate (String query) {
	try {
		st.execute(query);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
		
	}
	}
