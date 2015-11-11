package pkg722project;

import java.util.ArrayList;

public class Papers extends DBEntity {

	private String title;
	private String pAbstract;
	private String citation;

	public Papers() {
	}

	/**
	 * @see pkg722project.DbEntity#DbEntity(java.lang.String)
	 * This constructor calls super() then modifies the SQL statements to fit this class
	 * @param _dbPwd 
	 */
	public Papers(String _dbPwd) {
		super(_dbPwd);
		this.sqlFetch = String.format(this.sqlFetch, "title,abstract,citation", "Papers");
		this.sqlPut = String.format(this.sqlPut, "Papers", "title=?,abstract=?,citation=?");
		this.sqlPost = String.format(this.sqlPost, "Papers", "?,?,?");
		this.sqlDelete = String.format(this.sqlDelete, "Papers");
	}

	public Papers(String _dbPwd, int _id) {
		super(_dbPwd, _id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getpAbstract() {
		return pAbstract;
	}

	public void setpAbstract(String pAbstract) {
		this.pAbstract = pAbstract;
	}

	public String getCitation() {
		return citation;
	}

	public void setCitation(String citation) {
		this.citation = citation;
	}

	/**
	 * @see pkg722project.DbEntity#assignFields(java.util.ArrayList)
	 * @param rs 
	 */
	@Override
	protected void assignFields(ArrayList<ArrayList<String>> rs) {
		this.title = (rs.get(1)).get(1);
		this.pAbstract = (rs.get(2)).get(2);
		this.citation = (rs.get(3)).get(3);
	}

	/**
	 * @see pkg722project.DbEntity#getMemberFields()
	 * @return 
	 */
	@Override
	protected ArrayList<String> getMemberFields() {
		ArrayList<String> fields = new ArrayList();
		fields.add(this.title);
		fields.add(this.pAbstract);
		fields.add(this.citation);
		return fields;
	}

	/**
	 * Loads details of radio-button-selected paper into the user interface to allow editing
	 * @return ArrayList<String> list of fields describing the paper to be edited
	 */
	public ArrayList<String> editPaper() throws DLException {
		ArrayList<ArrayList<String>> paper;
		ArrayList<String> returnArray = new ArrayList();
		try {
			MySQLDatabase db = MySQLDatabase.getInstance();
			ArrayList values = new ArrayList();
			values.add(this.id);
			paper = db.getData("SELECT title, abstract, citation FROM papers WHERE id = ?", values);
			ArrayList<ArrayList<String>> keywords = db.getData("SELECT keyword FROM paper_keywords WHERE id = ?", values);
			for (int i = 0; i < paper.get(0).size(); i++) {
				returnArray.add(paper.get(0).get(i));
			}

			String keyword = "";
			for (int i = 0; i < keywords.size(); i++) {
				for (int j = 0; j < keywords.get(0).size(); j++) {
					keyword = keyword + keywords.get(i).get(j) + ",";
				}
			}
			returnArray.add(keyword);

		} catch (RuntimeException e) {
			throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in editPaper() of Faculty");
		}
		return returnArray;
	}

	/**
	 * Deletes a radio-button-selected paper from the papers table
	 * @throws DLException 
	 */
	public void deletePaper() throws DLException {
		MySQLDatabase db = MySQLDatabase.getInstance();
		try {
			ArrayList values = new ArrayList();
			values.add(this.id);
			db.setData("DELETE FROM papers WHERE id = ?;", values);
		} catch (RuntimeException e) {
			throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in deletePaper() of Papers");
		}
	}

	/**
	 * Updates the papers table to reflect updated details for a paper. Updates keywords in paper_keywords.
	 * @param title
	 * @param pAbstract
	 * @param citation
	 * @param keywords
	 * @param facultyId
	 * @throws DLException 
	 */
	public void save(String title, String pAbstract, String citation, String keywords, int facultyId) throws DLException {
		MySQLDatabase db = MySQLDatabase.getInstance();
		try {
			ArrayList values = new ArrayList();
			values.add(title);
			values.add(pAbstract);
			values.add(citation);
			values.add(this.id);

			ArrayList oldKeywords = new ArrayList();
			oldKeywords.add(this.id);

			String[] kws = keywords.split(",");
			ArrayList keywordList = new ArrayList();
			keywordList.add(this.id);
			keywordList.add(kws[0]);

			db.startTrans();
			db.setData("UPDATE papers SET title = ?, abstract = ?, citation = ? WHERE id = ?;", values);
			db.setData("DELETE FROM paper_keywords WHERE id = ?", oldKeywords);
			for (String kw : kws) {
				keywordList.set(1, kw);
				db.setData("INSERT INTO paper_keywords VALUES(?, ?);", keywordList);
			}
			db.endTrans();
		} catch (RuntimeException e) {
			throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in save() of Papers");
		}
	}

	/**
	 * Adds a paper to the papers table, adds keywords to paper_keywords table
	 * @param keywords
	 * @param facultyId
	 * @throws DLException 
	 */
	public void addPaper(String keywords, int facultyId) throws DLException {
		MySQLDatabase db = MySQLDatabase.getInstance();
		try {
			ArrayList values = new ArrayList();
			values.add(this.id);
			values.add(title);
			values.add(pAbstract);
			values.add(citation);

			ArrayList authorship = new ArrayList();
			authorship.add(facultyId);
			authorship.add(this.id);

			ArrayList keywordList = new ArrayList();
			String[] kws = keywords.split(",");
			keywordList.add(this.id);
			keywordList.add(kws[0]);

			db.startTrans();
			db.setData("INSERT INTO papers(id, title, abstract, citation) VALUES(?, ?, ?, ?);", values);
			db.setData("INSERT INTO authorship(facultyId, paperId) VALUES(?, ?)", authorship);
			for (String kw : kws) {
				keywordList.set(1, kw);
				db.setData("INSERT INTO paper_keywords VALUES(?, ?);", keywordList);
			}
			db.endTrans();
		} catch (RuntimeException e) {
			throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in addPaper() of Papers");
		}
	}
}