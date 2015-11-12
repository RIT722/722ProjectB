package pkg722project;

import java.util.ArrayList;

public class PaperKeywords extends DBEntity {

	private ArrayList<String> keywords;
	
	public PaperKeywords() {
	}

	/**
	 * @see pkg722project.DbEntity#DbEntity(java.lang.String)
	 * This constructor calls super() then modifies the SQL statements to fit this class
	 * @param _dbPwd 
	 */
	public PaperKeywords(String _dbPwd) {
		super(_dbPwd);
		this.sqlFetch = String.format(this.sqlFetch, "keyword", "Paper_Keywords");
		this.sqlPut = String.format(this.sqlPut, "Paper_Keywords", "keyword=?");
		this.sqlPost = String.format(this.sqlPost, "Paper_Keywords", "?");
		this.sqlDelete = String.format(this.sqlDelete, "Paper_Keywords");
	}

	public PaperKeywords(String _dbPwd, int _id) {
		super(_dbPwd, _id);
	}

	public ArrayList<String> getKeywords() {
		return keywords;
	}

	public void addKeyword(String keyword) {
		this.keywords.add(keyword);
	}
	
	/**
	 * @see pkg722project.DbEntity#assignFields(java.util.ArrayList)
	 * @param rs 
	 */
	@Override
	protected void assignFields(ArrayList<ArrayList<String>> rs) {
		for (String kw : rs.get(1)) {
			this.keywords.add(kw);
		}
	}
	
	/**
	 * @see pkg722project.DbEntity#getMemberFields()
	 * @return 
	 */
	@Override
	protected ArrayList<String> getMemberFields(){
		ArrayList<String> fields = new ArrayList();
		fields.add(this.keyword);
		return fields;
	}
}