package pkg722project;

public class Faculty extends DBEntity {
	private String fName;
	private String lName;
	private String password;
	private String email;
	private boolean askHelp;

	public Faculty() {
		super();
	}
	
	public Faculty(String _dbPwd) {
		super(_dbPwd);
	}

	public Faculty(String _dbPwd, int _id) {
		super(_dbPwd, _id);
	}

	/**
	 * @return the fName
	 */
	public String getfName() {
		return fName;
	}

	/**
	 * @param fName the fName to set
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}

	/**
	 * @return the lName
	 */
	public String getlName() {
		return lName;
	}

	/**
	 * @param lName the lName to set
	 */
	public void setlName(String lName) {
		this.lName = lName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the askHelp
	 */
	public boolean wantsHelp() {
		return askHelp;
	}

	/**
	 * @param askHelp the askHelp to set
	 */
	public void setAskHelp(boolean askHelp) {
		this.askHelp = askHelp;
	}

	public void fetch() {
		super.fetch();
		//more code
	}
	
	public void put() {
		super.put();
	}
			
	public void post() {
		super.post();
	}
			
	public void delete() {
		super.delete();
	}
}