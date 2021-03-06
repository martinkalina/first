package cz.jpower8.scheduler.model.condition;

import java.io.File;

import cz.jpower8.scheduler.model.ICondition;

/**
 * Checks for local file.
 * 
 * @author Martin Kalina
 *
 */
public class LocalFileExist implements ICondition {

	private String path;
	
	public LocalFileExist(String string) {
		path = string;
	}
	public LocalFileExist() {
	}

	@Override
	public boolean evaluate() {
		try {
			return new File(path).exists();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	@Override
	public String toString() {
		return "LOCAL_FILE_EXIST('" + path  + "')";
	}

}
