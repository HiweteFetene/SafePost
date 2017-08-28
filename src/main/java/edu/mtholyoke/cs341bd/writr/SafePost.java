package main.java.edu.mtholyoke.cs341bd.writr;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * @author jfoley, Hiwete Fetene, shanzehagrawala
 */
public class SafePost implements Comparable<SafePost> {
	  long timeStamp;
	  String messageText;
	  String username;
	 // String title;
	  String category;
	  int uid;
	  String file;
	  
	  List<SafeComment> commentList;

	  /**
	   * Create a message and init its time stamp.
	   * @param text the text of the message.
	   */
	  public SafePost(String username, String category, String message, int id, String file) {
		this.username = username;
		//this.title = title;
		this.uid = id;
		this.category = category;
	    messageText = message;
	    timeStamp = System.currentTimeMillis();
	    commentList = new ArrayList();
	    this.file=file;
	  }
	  
	  public  SafePost(String input) {
		  String[] pieces = input.split("\t");
		  timeStamp = Long.parseLong(pieces[0]);
		  messageText = returnTabsToNormal(pieces[1]);
		  category=returnTabsToNormal(pieces[2]);
		  username=returnTabsToNormal(pieces[3]);
		  
	 }
	  
	  //
	  String fixUpTabs(String input) {
		  try {
			return URLEncoder.encode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	  }
	  
	  //
	  String returnTabsToNormal(String input) {
		try {
			return URLDecoder.decode(input, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError(e);
		}
	  }
	  
	  String convertToString() {
		  return timeStamp + "\t" + fixUpTabs(messageText) + "\t" + fixUpTabs(username) + "\t"   + fixUpTabs(file);
	  }
	  
	
	  /**
	   * get the username of the author of message
	   * @return an username
	   */
	  public String getUsername() 
	  {
			return username;
		}
	  public String getFile()
	  {
		  return file;
	  }
	  
	  /**
	   * append the HTML of each post
	   * @param output HTML string
	   */
	  public void appendHTML(StringBuilder output) 
	  {
		  output
	      .append("<div class=\"message\">")
	      .append("<table><tr><td>")
	      .append("<img src='").append(file).append("' width=400 height=300 />")
	      .append("<div><a href=user/" + username + "><span class=\"username\">").append(username)
	      .append("</span></a></div><div><span class=\"datetime\">").append(Util.dateToEST(timeStamp))
	      .append("</span></div></td><td class=\"content\"><a class=\"post-link\" href=post/" + this.uid + "/><span class=\"category\">")
	      .append("Your category name is: " + category+"</span></a><br>"+messageText)
	  	  .append("<br><a class=\"post-link\" href=post/" + this.uid + "/><span class=\"cmt-notice\">")
	      .append(Integer.toString(commentList.size()) + " comment(s)")
	      .append("</span></a>")
	      .append("</td></tr></table>")
	      .append("</div></a>");
	  }
	  
	  /**
	   * get the comment list of the post
	   * @return a commentList
	   */
	  public List<SafeComment> getCommentList() {
		  return this.commentList;
	  }
	  
	  /**
	   * add a new comment to its comment list
	   * @param newComment the new comment being added
	   */
	  public void addNewComment(SafeComment newComment) {
		  this.commentList.add(newComment);
	  }

	  /**
	   * Sort newer messages to top by default.
	   * @param o the other message to compare to.
	   * @return comparator of (this, o).
	   */
	  @Override
	  public int compareTo(@Nonnull SafePost o) {
	    return -Long.compare(timeStamp, o.timeStamp);
	  }
	}
