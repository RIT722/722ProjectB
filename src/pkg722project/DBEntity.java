package pkg722project;

import java.util.ArrayList;

public abstract class DBEntity {
	private int id;
	private String dbPwd;
	private String sqlFetch = "SELECT %1 FROM %2 WHERE id = %3";
	
	public DBEntity() {
	}
	
	public DBEntity(String _dbPwd) {
		this();
		this.dbPwd = _dbPwd;
		this.sqlFetch = "";
	}
	
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
	
	public void fetch() {
		//logic to connect to database, call db.getData()
		MySQLDatabase msd = MySQLDatabase.getInstance();        
        String sql = "SELECT * FROM Equipment WHERE EquipID = ?;"; //*****THIS NEEDS TO BE CUSTOMIZED TO SUBCLASS
        ArrayList<String> ID = new ArrayList();
        ID.add(Integer.toString(EquipID));
        try{
            ArrayList<ArrayList<String>> rs = msd.getData(sql, ID);
            EquipmentName = (rs.get(1)).get(1);
            EquipmentDescription = (rs.get(1)).get(2);
            EquipmentCapacity = Integer.parseInt((rs.get(1)).get(3));
            return true;
        }
        catch(RuntimeException e){
            throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis()/1000), "SQL string = " + sql, "Error in fetch() of Equipment");
        }
	}
	
	public void put() {
		//function logic
	}
	
	public void post() {
		//function logic
	}
	
	public void delete() {
		//function logic
	}
	
}