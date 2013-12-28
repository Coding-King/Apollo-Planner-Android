package com.apolloplanner.type;

public class Post {
	public String date, time, message, file, fileTitle, feedName;
	public long feedId; // track feedId only for summary posts, do not use with regular posts just say -1
	public boolean hasFile;
	
	public Post(String date, String time, String message, String file, String fileTitle, long feedId, String feedName) {
		this.date = date;
		this.time = time;
		this.message = message;
		this.file = file;
		this.fileTitle = fileTitle;
		this.feedId = feedId;
		this.feedName = feedName;
		
		if (file.equals("nofile")) {
			this.file = "";
			this.fileTitle = "";
			hasFile = false;
		} else {
			hasFile = true;
		}
	}
}
