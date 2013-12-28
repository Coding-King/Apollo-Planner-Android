package com.apolloplanner.type;

import java.util.ArrayList;
import java.util.List;


public class Feed {

	public String name, teacher, website;
	public List<Post> posts;
	public long numPosts, id;
	
	public Feed(long id, String name, String teacher, String website, long numPosts) {
		this.id = id;
		this.name = name;
		this.teacher = teacher;
		this.website = website;
		this.numPosts = numPosts;
		
		posts = new ArrayList<Post>();
	}
	
	public boolean isEmpty() {
		if (posts.size() == 0)
			return true;
		else
			return false;
	}
	
}
