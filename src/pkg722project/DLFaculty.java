package pkg722project;

import java.util.ArrayList;

public class DLFaculty extends DBEntity {

	private String fName;
	private String lName;
	private String password;
	private String email;
	private boolean askHelp;
	private String sqlPostStudent;

	public DLFaculty() {
		super();
	}

	/**
	 * @see pkg722project.DbEntity#DbEntity(java.lang.String) This constructor
	 * calls super() then modifies the SQL statements to fit this class
	 * @param _dbPwd
	 */
	public DLFaculty(String _dbPwd) {
		super(_dbPwd);
		this.sqlFetch = "SELECT fName,lName,password,email,askHelp FROM Faculty WHERE id=?";
		this.sqlPut = "UPDATE Faculty SET fName=?,lName=?,password=?,email=?,askHelp=? WHERE id=?";
		this.sqlPost = "INSERT INTO Faculty VALUES (?,?,?,?,?,?)";
		this.sqlDelete = "DELETE FROM Faculty WHERE id=?";
		this.sqlPostStudent = "INSERT INTO Student VALUES (?,?)";
	}

	public DLFaculty(String _dbPwd, int _id) {
		super(_dbPwd, _id);
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean wantsHelp() {
		return askHelp;
	}

	public void setAskHelp(boolean askHelp) {
		this.askHelp = askHelp;
	}

	public void postStudent(String studentName) throws DLException {
		MySQLDatabase db = MySQLDatabase.getInstance();        
        try{
            ArrayList<String> values = new ArrayList<>();
			values.add(studentName);
			values.add(Integer.toString(this.id));
            db.setData(this.sqlPostStudent, values);
        }
        catch(RuntimeException e){
            throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis()/1000), "Error in post() of Equipment");
        }
	}
	
	/**
	 * @see pkg722project.DbEntity#assignFields(java.util.ArrayList)
	 * @param rs
	 */
	@Override
	protected void assignFields(ArrayList<ArrayList<String>> rs) {
		this.fName = (rs.get(1)).get(1);
		this.lName = (rs.get(2)).get(2);
		this.password = (rs.get(3)).get(3);
		this.email = (rs.get(4)).get(4);
		this.askHelp = Boolean.parseBoolean((rs.get(5)).get(5));
	}

	/**
	 * @see pkg722project.DbEntity#getMemberFields()
	 * @return
	 */
	@Override
	protected ArrayList<String> getMemberFields() {
		ArrayList<String> fields = new ArrayList();
		fields.add(this.fName);
		fields.add(this.lName);
		fields.add(this.password);
		fields.add(this.email);
		fields.add(Boolean.toString(this.askHelp));
		return fields;
	}
}