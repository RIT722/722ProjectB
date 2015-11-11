package pkg722project;

import java.io.*;
import java.util.*;

public class DLException extends Exception {

	private Exception e;
	private String[] msgs;

	/**
	 * Base constructor for uncustomized exception
	 * @param e the original raised exception
	 */
	public DLException(Exception e) {
		this.e = e;
		this.log();
	}

	/**
	 * @see pkg722project.DLException#DLException(java.lang.Exception)
	 * @param msgs Additional messages specified by exception creator
	 */
	public DLException(Exception e, String... msgs) {
		this.e = e;
		this.msgs = msgs;
		this.log();
	}

	/**
	 * Writes the original exception's details to an external logfile for later review.
	 */
	public void log() {
		try {
			File logfile = new File("LogFile.txt");
			if (!logfile.exists()) {
				logfile.createNewFile();
			}

			FileWriter FW = new FileWriter(logfile, true);
			Date date = new Date();
			FW.write(date.toString() + " " + e.toString() + " ");

			for (String msg : msgs) {
				FW.write("\n");
				FW.write(msg);
			}

			FW.write("\n");

			FW.write("Stack Trace: ");
			PrintWriter PW = new PrintWriter(FW);
			e.printStackTrace(PW);

			FW.write("\n");

			FW.flush();
			FW.close();
		} catch (IOException IE) {
			System.out.println(IE.getMessage());
		}
	}
}