package pkg722project;

import java.util.ArrayList;

/**
 * Abstract class defining any entity in our database.
 * @author Chris
 */
public abstract class DBEntity {

	protected int id;
	protected String dbPwd;
	protected String sqlFetch;
	protected String sqlPut;
	protected String sqlPost;
	protected String sqlDelete;
	
	/**
	 * Default empty constructor
	 */
	public DBEntity() {
	}
	
	/**
	 * Constructor that initializes the database for this object but does not otherwise define it.
	 * @param _dbPwd
	 */
	public DBEntity(String _dbPwd) {
		this();
		this.dbPwd = _dbPwd;
		this.sqlFetch = "SELECT %1 FROM %2 WHERE id = ?";
		this.sqlPut = "UPDATE %1 SET %2 WHERE id = ?;";
		this.sqlPost = "INSERT INTO %1 VALUES(%2);";
		this.sqlDelete = "DELETE FROM %1 WHERE id = ?;";
	}
	
	/**
	 * Constructor that initializes the database and id for this object.
	 * @param _dbPwd
	 * @param _id
	 */
	public DBEntity(String _dbPwd, int _id) {
		this(_dbPwd);
		this.id = _id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int newId) {
		this.id = newId;
	}
	
	/**
	 * Retrieves the data field values that this object's id correspond to in the database.
	 * @throws DLException
	 */
	public void fetch() throws DLException {
		MySQLDatabase db = MySQLDatabase.getInstance();
        ArrayList<String> idParam = new ArrayList();
        idParam.add(Integer.toString(this.id));
        try{
            ArrayList<ArrayList<String>> rs = db.getData(this.sqlFetch, idParam);
			this.assignFields(rs);
        }
        catch(RuntimeException e){
            throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis()/1000), "SQL string = " + this.sqlFetch, "Error in fetch() of Equipment");
        }
	}

	/**
	 * Sends an SQL statement to the database to updater the fields that this object's id corresponds to.
	 * @throws DLException
	 */
	public void put() throws DLException {
		MySQLDatabase db = MySQLDatabase.getInstance();        
        try{
            ArrayList<String> values = this.getMemberFields();
            db.setData(this.sqlPut, values);
        }
        catch(RuntimeException e){
            throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis()/1000), "Error in put() of Equipment");
        }
	}
	
	/**
	 * Sends an SQL statement to the database to insert this object's values into the database.
	 * @throws DLException
	 */
	public void post() throws DLException {
		MySQLDatabase db = MySQLDatabase.getInstance();        
        try{
            ArrayList<String> values = this.getMemberFields();
            db.setData(this.sqlPost, values);
        }
        catch(RuntimeException e){
            throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis()/1000), "Error in post() of Equipment");
        }
	}
	
	/**
	 * Sends an SQL statement to the database to delete the entry this object corresponds to from the database.
	 * @throws DLException
	 */
	public void delete() throws DLException {
		MySQLDatabase db = MySQLDatabase.getInstance();        
        try{   
            ArrayList<String> idParam = new ArrayList();
            idParam.add(Integer.toString(this.id));
            db.setData(this.sqlDelete, idParam);
        }
        catch(RuntimeException e){
            throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis()/1000), "Error in delete() of Equipment");
        }
	}
	
	/**
	 * Assigns results of this.fetch() to member variables.
	 * Implemented by extending class.
	 * @param rs result set obtained from this.fetch()
	 */
	protected abstract void assignFields(ArrayList<ArrayList<String>> rs);

	/**
	 * Returns a list of data layer member variables as Strings (except id).
	 * Implemented by extending class.
	 * @return ArrayList<String> data layer member variables in String form
	 */
	protected abstract ArrayList<String> getMemberFields();
}