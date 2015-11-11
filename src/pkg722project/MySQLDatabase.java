package pkg722project;

import java.sql.*;
import java.util.*;

public class MySQLDatabase { //Database connector class

	private static final MySQLDatabase INSTANCE = new MySQLDatabase();

	private static Connection conn = null; //Connection object
	private final String userName;
	private final String password;
	private final String url;
	private HashMap<String, PreparedStatement> prepString;

	private MySQLDatabase() {
		this.url = "jdbc:mysql://127.0.0.1/722Project";
		this.password = "student";
		this.userName = "root";
		this.prepString = new HashMap();
	}

	public static MySQLDatabase getInstance() {
		return INSTANCE;
	}

	/**
	 * Attempt to open database connection.
	 * @return boolean if connection was successful
	 * @throws DLException 
	 */
	public boolean connect() throws DLException { //makes connected to database
		try {
			Class.forName("com.mysql.jdbc.Driver"); //sets driver 
		} catch (ClassNotFoundException e) {
			throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Driver error in connect()");
		}

		try {
			MySQLDatabase.conn = DriverManager.getConnection(this.url, this.userName, this.password);
			return true;
		} catch (SQLException e) {
			throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "URL=" + url, "Connection error in MySQLDatabase");
		}
	}

	/**
	 * Attempt to close database connection.
	 * @return boolean if close was successful
	 * @throws DLException 
	 */
	public boolean close() throws DLException { //closes connected to database
		try {
			if (MySQLDatabase.conn != null) {
				MySQLDatabase.conn.close();
			}
			return true;
		} catch (SQLException se) {
			throw new DLException(se, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in close() of MySQLDatabase");
		}
	}

	/**
	 * Retrieves query results from database
	 * @param sql the statement to query
	 * @return ArrayList<ArrayList<String>> list of results
	 * @throws DLException 
	 */
	public ArrayList<ArrayList<String>> getData(String sql) throws DLException {
		try {
			Statement stmnt = MySQLDatabase.conn.createStatement();
			ResultSet rs = stmnt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			ArrayList<ArrayList<String>> arry = new ArrayList<>();

			while (rs.next()) {
				ArrayList<String> temp = new ArrayList(numCols);
				for (int i = 1; i <= numCols; i++) {
					temp.add(rs.getString(i));
				}
				arry.add(temp);
			}
			return arry;
		} catch (SQLException | RuntimeException se) {
			throw new DLException(se, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in getData() of MySQLDatabase", "SQL = " + sql);
		}
	}

	/**
	 * @see pkg722project.MySQLDatabase#getData(java.lang.String)
	 * @param b whether or not to return column names
	 * @throws DLException 
	 */
	public ArrayList<ArrayList<String>> getData(String sql, boolean b) throws DLException {

		ArrayList<ArrayList<String>> aList = this.getData(sql);

		if (!b) {
			return aList;
		} else {
			aList.add(0, new ArrayList());
			try {
				Statement stmnt = MySQLDatabase.conn.createStatement();
				ResultSet rs = stmnt.executeQuery(sql);
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String colName = rsmd.getColumnName(i);
					aList.get(0).add(colName);
				}
			} catch (SQLException e) {
				throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in MySQLDatabase boolean getData()");
			}

			return aList;
		}

	}

	/**
	 * @see pkg722project.MySQLDatabase#getData(java.lang.String)
	 * @param values for PreparedStatement
	 * @throws DLException 
	 */
	public ArrayList<ArrayList<String>> getData(String sql, ArrayList values) throws DLException {
		try {
			PreparedStatement stmt = this.prepare(sql, values);
			ResultSet rs = stmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int numCols = rsmd.getColumnCount();

			ArrayList<ArrayList<String>> arry = new ArrayList<>();

			while (rs.next()) {
				ArrayList<String> temp = new ArrayList(numCols);
				for (int i = 1; i <= numCols; i++) {
					temp.add(rs.getString(i));
				}
				arry.add(temp);
			}

			return arry;
		} catch (SQLException | RuntimeException se) {
			throw new DLException(se, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in getData() of MySQLDatabase", "SQL = " + sql);
		}
	}

	/**
	 * Executes updates, deletes, and inserts into database
	 * @param sql statement to execute
	 * @return boolean if successful
	 * @throws DLException 
	 */
	public boolean setData(String sql) throws DLException {
		try {
			Statement stmnt = MySQLDatabase.conn.createStatement();
			return stmnt.execute(sql);
		} catch (SQLException se) {
			throw new DLException(se, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in setData() of MySQLDatabase", "SQL = " + sql);
		}
	}

	/**
	 * @see pkg722project.MySQLDatabase#setData(java.lang.String)
	 * @param values to use for PreparedStatement
	 * @throws DLException 
	 */
	public boolean setData(String sql, ArrayList values) throws DLException {
		try {
			PreparedStatement stmt = this.prepare(sql, values);
			return stmt.execute();
		} catch (SQLException se) {
			throw new DLException(se, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in setData() of MySQLDatabase", "SQL = " + sql);
		}
	}

	/**
	 * Compile a Prepared Statement.
	 * @param sql statement to prepare
	 * @param values for preparation
	 * @return PreparedStatement
	 * @throws DLException 
	 */
	public PreparedStatement prepare(String sql, ArrayList values) throws DLException {
		PreparedStatement stmt;
		try {
			if (prepString.containsKey(sql)) {
				stmt = prepString.get(sql);
				for (int i = 0; i < values.size(); i++) {
					stmt.setObject(i + 1, values.get(i));
				}
			} else {
				stmt = MySQLDatabase.conn.prepareStatement(sql);
				for (int i = 0; i < values.size(); i++) {
					stmt.setObject(i + 1, values.get(i));
					prepString.put(sql, stmt);
				}
			}
			return stmt;
		} catch (SQLException e) {
			throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in MySQLDatabase prepare()");
		}
	}

	/**
	 * Execute the input statement and values as a prepared statement
	 * @param sql statement to prepare
	 * @param values for preparation
	 * @return int return value of @link java.sql.PreparedStatement#executeUpdate()
	 * @throws DLException 
	 */
	public int executeStmt(String sql, ArrayList values) throws DLException {
		try {
			PreparedStatement stmt = this.prepare(sql, values);
			return stmt.executeUpdate();
		} catch (SQLException se) {
			throw new DLException(se, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in MySQLDatabase executeStmt()");
		}

	}

	/**
	 * Initiate a new transaction for this database.
	 * @throws DLException 
	 */
	public void startTrans() throws DLException {
		try {
			MySQLDatabase.conn.setAutoCommit(false);
		} catch (SQLException se) {
			throw new DLException(se, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in MySQLDatabase startTrans()");
		}
	}

	/**
	 * Attempt to commit current transaction.
	 * @throws DLException 
	 */
	public void endTrans() throws DLException {
		try {
			MySQLDatabase.conn.commit();
			MySQLDatabase.conn.setAutoCommit(true);
		} catch (SQLException se) {
			throw new DLException(se, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in MySQLDatabase endTrans()");
		}
	}

	/**
	 * Attempt to rollback current transaction
	 * @throws DLException 
	 */
	public void rollbackTrans() throws DLException {
		try {
			MySQLDatabase.conn.rollback();
			MySQLDatabase.conn.setAutoCommit(true);
		} catch (SQLException se) {
			throw new DLException(se, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in MySQLDatabase rollbackTrans()");
		}
	}
}