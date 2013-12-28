package com.apolloplanner.type;

public class Assignment {
	public String dateFrom, dateTo, message, file, fileTitle, className;
	public boolean hasFile;
	
	public Assignment(String dateFrom, String dateTo, String message, String file, String fileTitle, String className) {
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
		this.message = message;
		this.file = file;
		this.fileTitle = fileTitle;
		this.className = className;
		
		if (file.equals("nofile")) {
			hasFile = false;
			this.file = "";
			this.fileTitle = "";
		} else {
			hasFile = true;
		}
	}
}
