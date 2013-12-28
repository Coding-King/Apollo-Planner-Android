package com.apolloplanner.type;

import java.util.ArrayList;
import java.util.List;


public class Class {
	
	public String name, teacher, website;
	public List<Assignment> assignments;
	public long numPosts, id;
	
	public Class(long id, String name, String teacher, String website, long numPosts) {
		this.id = id;
		this.name = name;
		this.teacher = teacher;
		this.website = website;
		this.website = website;
		this.numPosts = numPosts;
		
		assignments = new ArrayList<Assignment>();
	}
	
	public boolean isEmpty() {
		if (assignments.size() == 0)
			return true;
		else
			return false;
	}
	

	
}
