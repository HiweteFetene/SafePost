package main.java.edu.mtholyoke.cs341bd.writr;

import javax.annotation.Nonnull;

/**
 * Represent a comment in a post
 * @author vivianle and shanzehagrawala
 *
 */
public class SafeComment implements Comparable<SafePost> {
  long timeStamp;
  String commentText;
  String username;
  int uid;

  /**
   * Create a message and init its time stamp.
   * @param text the text of the message.
   */
  public SafeComment(String username, String comment, int uid) {
	this.username = username;
	this.commentText = comment;
    timeStamp = System.currentTimeMillis();
    this.uid = uid;
  }
  
  /**
   * append the HTML of each comment
   * @param output HTML string
   */
  public void appendHTML(StringBuilder output) {
    
    output
    .append("<div class=\"message\">")
    .append("<table><tr><td>")
    .append("<div><span class=\"username\">").append(username)
    .append("</span></div><div><span class=\"datetime\">").append(Util.dateToEST(timeStamp))
    .append("</span></div></td><td class=\"content\"><span>")
    
    .append(commentText+"</span>")
    .append("</td></tr></table>")
    .append("</div>");
  }

  /**
   * Sort older to top by default.
   * @param o the other message to compare to.
   * @return comparator of (this, o).
   */
  @Override
  public int compareTo(@Nonnull SafePost o) {
    return Long.compare(timeStamp, o.timeStamp);
  }
}
