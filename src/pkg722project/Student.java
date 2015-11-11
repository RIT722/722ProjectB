package pkg722project;

import java.util.ArrayList;


public class Student extends DBEntity {
	private String name;
	private int facultyId;
	
	public Student() {
	}

	/**
	 * @see pkg722project.DbEntity#DbEntity(java.lang.String)
	 * This constructor calls super() then modifies the SQL statements to fit this class
	 * @param _dbPwd 
	 */
	public Student(String _dbPwd) {
		super(_dbPwd);
		this.sqlFetch = String.format(this.sqlFetch,"name,facultyId","Student");
		this.sqlPut = String.format(this.sqlPut,"Student","name=?,facultyId=?");
		this.sqlPost = String.format(this.sqlPost,"Student","?,?");
		this.sqlDelete = String.format(this.sqlDelete,"Student");
	}

	public Student(String _dbPwd, int _id) {
		super(_dbPwd, _id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFacultyId() {
		return facultyId;
	}

	public void setFacultyId(int facultyId) {
		this.facultyId = facultyId;
	}
	
	/**
	 * @see pkg722project.DbEntity#assignFields(java.util.ArrayList)
	 * @param rs 
	 */
	@Override
	protected void assignFields(ArrayList<ArrayList<String>> rs) {
		this.name = (rs.get(1)).get(1);
		this.facultyId = Integer.parseInt((rs.get(2)).get(2));
	}
	
	/**
	 * @see pkg722project.DbEntity#getMemberFields()
	 * @return 
	 */
	@Override
	protected ArrayList<String> getMemberFields(){
		ArrayList<String> fields = new ArrayList();
		fields.add(this.name);
		fields.add(Integer.toString(this.facultyId));
		return fields;
	}
}
