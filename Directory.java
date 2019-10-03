package com.metacube.vcp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Directory class to store details regarding a directory for example name and its sub-directories and its parent directory
 * @author Mohit Sahu
 *
 */
public class Directory {
	private String directoryName;
	private List<Directory> subDirectory;
	private Directory parentDirectory;
	private Date date;
	
	public Directory(String directoryName, Directory parentDirectory) {
		this.directoryName = directoryName;
		this.parentDirectory = parentDirectory;
		this.subDirectory = new ArrayList<Directory>();
		this.date = new Date();
	}

	public List<Directory> getSubDirectories() {
		return subDirectory;
	}
	
	public String getDirectoryName() {
		return directoryName;
	}

	public Date getDate() {
		return date;
	}
	
	public Directory getParentDirectory(){
		return parentDirectory;
	}
}
