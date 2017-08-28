package main.java.edu.mtholyoke.cs341bd.writr;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

/**
 * HTML templates of the web
 * @author jfoley, vivianle, shanzehagrawala
 *
 */
public class SafeTemplates {
	 
	/**
	   * Made this a function so that we can have the submit form at the top & bottom of the page.
	   * <a href="http://www.w3schools.com/html/html_forms.asp">Tutorial about Forms</a>
	   * @param output where to write our HTML to
	   */
	public void printWritrPostForm(PrintWriter output) {
		output.println("<div class=\"form\">");
	    output.println("  <form action=\"submitPost\" method=\"POST\">");
	    output.println("     <input type=\"text\" size=10 name=\"user\" placeholder=\"Name\" /></div>");
	    output.println("     <div><textarea rows=\"3\" cols=\"52\" type=\"text\" placeholder=\"Type your Poetry here ...\" name=\"message\"></textarea></div>");
	    output.println("     <input type=\"text\" size=10 name=\"user\" placeholder=\"Name\" /></div>");
	    //output.println("     <div><textarea rows=\"3\" cols=\"30\" type=\"text\" placeholder=\"\" name=\"category\"></textarea></div>");
	    output.println("     <input type=\"submit\" value=\"Submit!\" />");
	    output.println("  </form>");
	    output.println("</div>");
	  }
	
	
	public void printFileForm(PrintWriter output) {
		output.println("<div class=\"form\">");
	    output.println("  <form action=\"submitPost\" method=\"POST\" enctype=\"multipart/form-data\" >");
	   // enctype="multipart/form-data">
	    output.println("     <input type=\"file\" name=\"fileUpload\"  />");
		   // enctype="multipart/form-data">
	    output.println("</form>");
	    output.println("</div>");
	  }
	
	public void printArtistForm(PrintWriter output) {
		output.println("<div class=\"form\">");
	    output.println("  <form action=\"loginArtist\" method=\"POST\">");
	    output.println("     <input type=\"submit\" name=\"userArtist\" value=\"Enter as Artist\" />");
	    output.println("  </form>");
	    output.println("</div>");
	  }
	
	public void printVisitorForm(PrintWriter output) {
		output.println("<div class=\"form\">");
	    output.println("  <form action=\"loginVisitor\" method=\"POST\">");
	    output.println("     <input type=\"submit\" name=\"userArtist\" value=\"Enter as Visitor\" />");
	    output.println("  </form>");
	    output.println("</div>");
	  }
	
	public void submitArtCategory(PrintWriter output) {
		output.println("<div class=\"form\">");
	    logoutButton(output);

	    output.println("  <form action=\"submitPost\" method=\"POST\" enctype=\"multipart/form-data\">");
	   // output.println("     <input type=\"submit\" value=\"Logout!\" /></div></div>");

	    output.println("<p>Choose your catagory</p>");

	    output.println(" <select name =\"category\" />" );
	    output.println(" <option value=\"love\">Love</option> />");
	    output.println(" <option value=\"gender\">Gender</option>  />");
	    output.println(" <option value=\"war\">War</option> />");
	    output.println(" <option value=\"sexuality\">Sexuality</option>  />");
	    output.println("</select>");
	    output.println("     <input type=\"text\" size=20 name=\"user\" placeholder=\"Enter username\" /></div>");
	    output.println("     <input type=\"file\" name=\"fileUpload\"  />");
	    output.println("     <input type=\"email\" name=\"emailUpload\"  />");
	    output.println("     <div><textarea rows=\"3\" cols=\"52\" type=\"text\" placeholder=\"Type your message here ...\" name=\"message\"></textarea></div>");
	    output.println("     <input type=\"submit\" value=\"Submit!\" /></div></div>");
	    
	  //  printFileForm(output);

	     output.println("</span></p>");

	    output.println("  </form>");
	    output.println("</div>");
	  }
	
	
	public void searchAsVisitor(PrintWriter output) {
		output.println("<div class=\"form\">");
	    logoutButton(output);

	    output.println("  <form action=\"searchVisitor\" method=\"GET\">");
	    output.println(" <select name =\"category\" />" );
	    output.println(" <option value=\"none\">None</option> />");

	    output.println(" <option value=\"love\">Love</option> />");
	    output.println(" <option value=\"gender\">Gender</option>  />");
	    output.println(" <option value=\"love\">War</option> />");
	    output.println(" <option value=\"gender\">Sexuality</option>  />");
	    output.println("</select>");
	   // VisitorSearchByCatagory(output);
	    output.println("     <div><textarea rows=\"3\" cols=\"30\" type=\"text\" placeholder=\"Enter artist name.\" name=\"searchArtist\"></textarea></div>");
	    output.println("     <input type=\"submit\" value=\"Search by Artist\" /></div></div>");
//	    VisitorSearchByCatagory(output);
	    output.println("  </form>");
	    output.println("</div>");
	  }
	
	public void VisitorSearchByCatagory(PrintWriter output)
	{
		output.println("<div class=\"form\">");
	    output.println("  <form action=\"searchCategory\" method=\"GET\">");
	    output.println("     <input type=\"submit\" value=\"Search by Catagory\" /></div></div>");
	    output.println("  </form>");
	    output.println("</div>");
	}
	
	public void logoutButton(PrintWriter output) {
		output.println("<div class=\"form\">");
	    output.println("  <form action=\"logout\" method=\"POST\">");
	    output.println("     <input type=\"submit\" value=\"Logout\" /></div></div>");
	    output.println("  </form>");
	    output.println("</div>");
	  }
	
	  
	  /**
	   * Made this a function so that we can have the submit form at the top & bottom of the page.
	   * <a href="http://www.w3schools.com/html/html_forms.asp">Tutorial about Forms</a>
	   * @param output where to write our HTML to
	   */
	  public void printWritrCommentForm(PrintWriter output, int uid) {
	    output.println("<div class=\"form cmt-form\">");
	    output.println("  <form action=\"submitComment\" method=\"POST\">");
	    output.println("     <input type=\"hidden\" name=\"uid\" value=" + uid + " />");
	    output.println("     <div id=\"cmt-form\"><textarea rows=\"3\" cols=\"52\" type=\"text\" placeholder=\"Type your comment here ...\" name=\"comment\"></textarea>");
	    output.println("     <div><input type=\"text\" size=10 name=\"commentUser\" placeholder=\"Name\" />");
	    output.println("     <input type=\"submit\" value=\"Comment!\" /></div></div>");
	    output.println("  </form>");
	    output.println("</div>");
	  }
	  
	  /**
	   * get the static URL of a file
	   * @param resource name of file
	   * @return a path of file
	   */
	  public String getStaticURL(String resource) {
		    return "static/"+resource;
	  }
	  
	  /**
	   * HTML top boilerplate; put in a function so that I can use it for all the pages I come up with.
	   * @param html where to write to; get this from the HTTP response.
	   * @param title the title of the page, since that goes in the header.
	   */
	  public void printWritrPageStart(PrintWriter html, String title, String metaURL) {
	    html.println("<html>");
	    html.println("  <head>");
	    html.println("    <title>"+title+"</title>");
	    html.println("    "+metaURL);
	    html.println("    <link type=\"text/css\" rel=\"stylesheet\" href=\""+getStaticURL("writr.css")+"\">");
	    html.println("  </head>");
	    html.println("  <body>");
	    html.println("  <a href=\"/\" id='logo'><h1 class=\"logo\">Safe Space</h1></a>");
	  }
	  
	  /**
	   * HTML bottom boilerplate; close all the tags we open in printWritrPageStart.
	   * @param html where to write to; get this from the HTTP response.
	   */
	  public void printWritrPageEnd(PrintWriter html) {
	    html.println("  </body>");
	    html.println("</html>");
	  }
	  
	  /**
	   * reponse page each time user submit a new post or comment
	   * @param resp http response object -- where we respond to the user.
	   * @param path path to redirect after response page
	   * @param metaURL URL of server
	   */
	  public void returnResponsePage(HttpServletResponse resp, String path, String metaURL) {

	      // Respond!
	      try (PrintWriter html = resp.getWriter()) {
	        printWritrPageStart(html, "Art: Submitted!", path);
	        // Print actual redirect directive:
	        html.println("<meta http-equiv=\"refresh\" content=\"3; url=/front"  + path + "\">");

	        // Thank you, link.
	        html.println("<div class=\"body\">");
	        html.println("<div class=\"thanks\">");
	        html.println("<p>Thanks for your Submission!</p>");
	        html.println("<a href=\"" + path + "/\">Back to the previous page...</a> (automatically redirect in 3 seconds).");
	        html.println("</div>");
	        html.println("</div>");

	        printWritrPageEnd(html);

	      } catch (IOException ignored) {
	        // Don't consider a browser that stops listening to us after submitting a form to be an error.
	      }
	}
}
