package pkg722project;

import java.util.ArrayList;

public final class DBController {

	private DBController() {
	}

	//returns the list of titles a faculty member has published
	public static ArrayList<ArrayList<String>> getPaperList(int facId) throws DLException {
		ArrayList<ArrayList<String>> titles;
		try {
			MySQLDatabase db = MySQLDatabase.getInstance();
			ArrayList values = new ArrayList();
			values.add(facId);
			titles = db.getData("SELECT papers.id, papers.title FROM (faculty join authorship on faculty.id = authorship.facultyid) JOIN papers ON authorship.paperID = papers.id WHERE faculty.id = ?", values);
		} catch (RuntimeException e) {
			throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in getPaperList() of Equipment");
		}
		return titles;
	}

	//Inserts a student into the student table using a faculty object
	public static void addStudent(int facId, String studentName) throws DLException {
		DLFaculty f = new DLFaculty("12345",facId);
		try {
			f.fetch();
			f.postStudent(studentName);
		} catch (DLException e) {
			throw e;
		}
	}

	//Used in DLFaculty search screen to display list of student employees
	public static ArrayList<String> getStudents(int facId) throws DLException {
		ArrayList<String> studentList = new ArrayList();

		try {
			MySQLDatabase db = MySQLDatabase.getInstance();
			ArrayList values = new ArrayList();
			values.add(facId);

			ArrayList<ArrayList<String>> students = db.getData("SELECT name FROM student WHERE id = ?", values);

			for (String student : students.get(0)) {
				studentList.add(student);
			}
		} catch (RuntimeException e) {
			throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis() / 1000), "Error in needHelp() of Faculty");
		}

		return studentList;
	}

	//Loads details of radio-button-selected paper into the user interface to allow editing
	//Used also in search by faculty to display paper details
	public static ArrayList<String> getPaperByID(int id) throws DLException {
		ArrayList<ArrayList<String>> paper;
		ArrayList<String> returnArray = new ArrayList();
		try {
			MySQLDatabase msd = MySQLDatabase.getInstance();
			ArrayList values = new ArrayList();
			values.add(id);
			paper = msd.getData("SELECT title, abstract, citation FROM papers WHERE id = ?", values);
			ArrayList<ArrayList<String>> keywords = msd.getData("SELECT keyword FROM paper_keywords WHERE id = ?", values);
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
	
	public static ArrayList<ArrayList<String>> searchByKeywords(String keyword) throws DLException{
        ArrayList<ArrayList<String>> paperList;

        try{
            MySQLDatabase msd = MySQLDatabase.getInstance();  
            ArrayList values = new ArrayList();
            values.add(keyword);
            paperList = msd.getData("SELECT papers.id, title FROM papers JOIN paper_keywords ON papers.id = paper_keywords.id WHERE keyword = ?", values);
            
        }
        catch(RuntimeException e){
            throw new DLException(e, "Unix time: " + String.valueOf(System.currentTimeMillis()/1000), "Error in searchByKeywords() of Faculty");
        }
        return paperList;
    }
}
