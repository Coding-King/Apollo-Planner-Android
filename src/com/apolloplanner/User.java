package com.apolloplanner;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.apolloplanner.type.Assignment;
import com.apolloplanner.type.Class;
import com.apolloplanner.type.Feed;
import com.apolloplanner.type.Post;


public class User {

	long id;
	String name;
	String surname;
	
	public List<Class> classes;
	public List<Feed> feeds;
	public List<Post> feedSummary;
	
	public User(String XMLresource) {

		classes = new ArrayList<Class>();
		feeds = new ArrayList<Feed>();
		feedSummary = new ArrayList<Post>();
		
		//Parse XML
		Document doc = getDomElement(XMLresource);

		//User Data
		NodeList userNode = doc.getElementsByTagName("user");
		Element e = (Element) userNode.item(0);
		name = getValue(e, "first");
		surname = getValue(e, "last");
		id = Long.valueOf(getValue(e, "id"));
		
		//Classes Data
		Class allClasses = new Class(-1, "All Classes", "", "none", 0);
		NodeList mainClassNode = doc.getElementsByTagName("classes");
		Element classesElement = (Element) mainClassNode.item(0);
		NodeList classesNode = classesElement.getElementsByTagName("class");
		for (int i = 0; i < classesNode.getLength(); i++) {
			Element classData = (Element) classesNode.item(i);
			long id = Long.valueOf(getValue(classData, "id"));
			String className = getValue(classData, "name");
			String ownerName = getValue(classData, "owner_name");
			String website = getValue(classData, "website");
			long numPosts = Long.valueOf(getValue(classData, "num_posts"));
			Class addClass = new Class(id, className, ownerName, website, numPosts);
			//Iterate Posts
			NodeList mainAssignmentsNode = classData.getElementsByTagName("posts");
			Element assignmentsElement = (Element) mainAssignmentsNode.item(0);
			NodeList assignmentsNode = assignmentsElement.getElementsByTagName("post");
			for (int j = 0; j < assignmentsNode.getLength(); j ++) {
				Element assignmentData = (Element) assignmentsNode.item(j);
				String dateFrom = getValue(assignmentData, "date_from");
				String dateTo = getValue(assignmentData, "date_to");
				String message = getValue(assignmentData, "message");
				String file = getValue(assignmentData, "file");
				String fileTitle = getValue(assignmentData, "file_title");
				addClass.assignments.add(new Assignment(dateFrom, dateTo, message, file, fileTitle, className));
				if (j == 0)
					allClasses.assignments.add(new Assignment(dateFrom, dateTo, message, file, fileTitle, className));
			}
			classes.add(addClass);
		}
		classes.add(0, allClasses);
		allClasses.numPosts = classesNode.getLength();
		
		//Feeds Data
		NodeList mainFeedNode = doc.getElementsByTagName("feeds");
		Element feedsElement = (Element) mainFeedNode.item(0);
		NodeList feedsNode = feedsElement.getElementsByTagName("feed");
		for (int i = 0; i < feedsNode.getLength(); i++) {
			Element feedData = (Element) feedsNode.item(i);
			long id = Long.valueOf(getValue(feedData, "id"));
			String className = getValue(feedData, "name");
			String ownerName = getValue(feedData, "owner_name");
			String website = getValue(feedData, "website");
			long numPosts = Long.valueOf(getValue(feedData, "num_posts"));
			Feed addFeed = new Feed(id, className, ownerName, website, numPosts);
			//Iterate Posts
			NodeList mainPostsNode = feedData.getElementsByTagName("posts");
			Element postsElement = (Element) mainPostsNode.item(0);
			NodeList postsNode = postsElement.getElementsByTagName("post");
			for (int j = 0; j < postsNode.getLength(); j ++) {
				Element postData = (Element) postsNode.item(j);
				String date = getValue(postData, "date");
				String message = getValue(postData, "message");
				String file = getValue(postData, "file");
				String fileTitle = getValue(postData, "file_title");
				String[] dateTime = date.split(" ");
				date = dateTime[0];
				String time = dateTime[1];
				addFeed.posts.add(new Post(date, time, message, file, fileTitle, -1, className));
			}
			feeds.add(addFeed);
		}
		
		//Feed Summary Data
		NodeList summaryNode = doc.getElementsByTagName("feed_summary");
		Element summaryElement = (Element) summaryNode.item(0);
		NodeList summariesNode = summaryElement.getElementsByTagName("post");
		for (int i = 0; i < summariesNode.getLength(); i ++) {
			Element postData = (Element) summariesNode.item(i);
			long feedId = Long.valueOf(getValue(postData, "feed_id"));
			String feedName = getValue(postData, "feed_name");
			String date = getValue(postData, "date");
			String message = getValue(postData, "message");
			String file = getValue(postData, "file");
			String fileTitle = getValue(postData, "file_title");
			String[] dateTime = date.split(" ");
			date = dateTime[0];
			String time = dateTime[1];
			feedSummary.add(new Post(date, time, message, file, fileTitle, feedId, feedName));
		}
		feeds.add(0, new Feed(-1, "All Feeds", "", "none", 0));
		feeds.get(0).posts = feedSummary;
		feeds.get(0).numPosts = feedSummary.size();
	}
	
	/**
	 * Turn String into XMLdocument
	 */
	static public Document getDomElement(String xml){
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
 
            InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xml));
                doc = db.parse(is); 
 
            } catch (ParserConfigurationException e) {
                return null;
            } catch (SAXException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
            return doc;
    }

	/**
	 * Returns an element
	 */
	public String getValue(Element item, String str) {
		NodeList n = item.getElementsByTagName(str);
		return this.getElementValue(n.item(0));
	}

	/**
	 * Helps return element
	 */
	public final String getElementValue(Node elem) {
		Node child;
		if (elem != null) {
			if (elem.hasChildNodes()) {
				for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}
}
