package main.java.edu.mtholyoke.cs341bd.writr;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;

import javax.imageio.ImageIO;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Vector;

/**
 * @author  Hiwete Fetene, shanzehagrawala
 */
public class SafeServer extends AbstractHandler {
	String metaURL;
	Server jettyServer;
	SafeTemplates templates;
	SafeModel data;
	int imageCounter=0;

	/**
	 * the server of the web
	 * 
	 * @param baseURL
	 *            URL of server
	 * @param port
	 *            port to open server
	 * @throws IOException
	 *             if something goes wrong
	 */
	public SafeServer(String baseURL, int port) throws IOException {

		templates = new SafeTemplates();
		data = new SafeModel();

		this.metaURL = "<base href=\"" + baseURL + "\">";
		jettyServer = new Server(port);

		// We create a ContextHandler, since it will catch requests for us under
		// a specific path.
		// This is so that we can delegate to Jetty's default ResourceHandler to
		// serve static files, e.g. CSS & images.
		ContextHandler staticCtx = new ContextHandler();
		staticCtx.setContextPath("/static");
		ResourceHandler resources = new ResourceHandler();
		resources.setBaseResource(Resource.newResource("static/"));
		staticCtx.setHandler(resources);

		// This context handler just points to the "handle" method of this
		// class.
		ContextHandler defaultCtx = new ContextHandler();
		defaultCtx.setContextPath("/");
		defaultCtx.setHandler(this);

		// Tell Jetty to use these handlers in the following order:
		ContextHandlerCollection collection = new ContextHandlerCollection();
		collection.addHandler(staticCtx);
		collection.addHandler(defaultCtx);
		jettyServer.setHandler(collection);
	}

	/**
	 * Once everything is set up in the constructor, actually start the server
	 * here:
	 * 
	 * @throws Exception
	 *             if something goes wrong.
	 */
	public void run() throws Exception {
		jettyServer.start();
		jettyServer.join(); // wait for it to finish here! We're using threads
							// behind the scenes; so this keeps the main thread
							// around until something can happen!
	}

	/**
	 * The main callback from Jetty.
	 * 
	 * @param resource
	 *            what is the user asking for from the server?
	 * @param jettyReq
	 *            the same object as the next argument, req, just cast to a
	 *            jetty-specific class (we don't need it).
	 * @param req
	 *            http request object -- has information from the user.
	 * @param resp
	 *            http response object -- where we respond to the user.
	 * @throws IOException
	 *             -- If the user hangs up on us while we're writing back or
	 *             gave us a half-request.
	 * @throws ServletException
	 *             -- If we ask for something that's not there, this might
	 *             happen.
	 */
	@Override
	public void handle(String resource, Request jettyReq, HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		System.out.println("Content-type: "+req.getContentType());
		if(req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) {
			MultipartConfigElement multipartConfigElement = new MultipartConfigElement("/tmp");
			jettyReq.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, multipartConfigElement);
		}
		System.out.println(jettyReq.getAttribute(Request.__MULTIPART_CONFIG_ELEMENT));
		
		System.out.println(jettyReq);

		String method = req.getMethod();
		String path = req.getPathInfo();
		String user = findUserName(req); // "visitor" or authorname or null

		if (user == null) {
			// show them login page
			// or handle the login form only
			// POST && login -> change them to visitor or user
			// everything else -> login page
			/**
			 * If user clicks on enter as artist
			 * take user to login artist
			 *create cookie user
			 *redirect to /welcomeartist??
			 */
			if ("POST".equals(method) && "/loginArtist".equals(path)) 
			{
				String username = ""; // get from form;
				//assert (!username.equals("visitor"));
				resp.addCookie(new Cookie("user", username));
				resp.sendRedirect("/welcomeArtist");
				return;
			} 
			/**
			 * otherwise if user clicks on login visitor
			 * create cookie visitor
			 * redirect to the page that is always there
			 */
			else if ("POST".equals(method) && "/loginVisitor".equals(path)) {
				resp.addCookie(new Cookie("user", "visitor"));
				resp.sendRedirect("/welcomeVisitor");
				return;
			}

			else {

				returnNewPage(resp);
			}

			return; // don't let them see anything else
		}
		
	else{

		boolean canPost = !user.equals("visitor");
		System.out.println(user);

		if (user.equals("visitor") && "GET".equals(method) && "/welcomeVisitor".equals(path))

		{
			System.out.println("Im a visitor");
			returnVisitorPage(resp);
		}
		
		if (canPost && user.equals("artist") && "GET".equals(method) && "/welcomeArtist".equals(path))
		{
			//returnFrontPage( resp);

			returnArtistPage(resp);
		}

		if ("POST".equals(method) && "/logout".equals(path))

		{
			resp.addCookie(new Cookie("user", null));
			resp.sendRedirect("/login");
		}

		
		if(!canPost&& "GET".equals(method) && "/searchVisitor".equals(path)) 
		{
			submitVisitorSearch(req, resp);
			//resp.sendError(400, "Can't post as a visitor");
			return;
		}
	 if(!canPost && ("GET".equals(method) && "/searchCategory".equals(path)))
		{
			submitVisitorCatagorySearch(req, resp);
		//resp.sendError(400, "Can't post as a visitor");
		return;
		}
		
		// submit a new post
		if ("POST".equals(method) && "/submitPost".equals(path)) {
			
				
				
				submitPostPage(jettyReq, resp);

		}
		// submit a new comment
		else if (canPost && "POST".equals(method) && "/submitComment".equals(path))

		{
			submitCommentPage(req, resp);
		}
		// bring up a particular post and its comments
		else if ("GET".equals(method) && path.matches("/post/\\d+/?"))

		{
			int numPost = 0;
			if (path.endsWith("/")) {
				numPost = Integer.parseInt(path.substring(6, path.length() - 1));
			} else
				numPost = Integer.parseInt(path.substring(6));

			if (numPost <= data.getNumMessages())
				returnPostPage(resp, numPost);
			else
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found.");
		}

		// brings up a particular user's posts
		else if ("GET".equals(method) && path.matches("/user/.+"))

		{
			String user1 = path.substring(6);

			Vector<SafePost> userMessage = data.postList(user1);
			if (userMessage.size() > 0) {
				returnSearchPage(resp, userMessage);
			}

			else {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found.");
			}

		}
		// bring up the front page
		else

			returnFrontPage(resp);
		}
	}

	

	private String findUserName(HttpServletRequest req) {
		Cookie[] thingsSaved = req.getCookies();
		if (thingsSaved == null)
			return null;
		for (Cookie cookie : thingsSaved) {
			if (cookie.getName().equals("user")) {
				return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * bring up a page with all posts of an user
	 * 
	 * @param resp
	 *            http response object -- where we respond to the user.
	 * @param userMessage
	 *            all posts of the user
	 * @throws IOException
	 *             -- If the user hangs up on us while we're writing back or
	 *             gave us a half-request.
	 * @throws ServletException
	 *             -- If we ask for something that's not there, this might
	 *             happen.
	 */
	private void returnSearchPage(HttpServletResponse resp, Vector<SafePost> searchList)
			throws IOException, ServletException {
		try (PrintWriter html = resp.getWriter()) {
			templates.printWritrPageStart(html, "Safe Place", metaURL);

			// print out all the posts of user
			html.println("<div class=\"body\">");

			ArrayList<SafePost> sortedList = new ArrayList<>(searchList);
			Collections.sort(sortedList);

			StringBuilder userHTML = new StringBuilder();

			// iterate thorough all posts of user and print out
			for (int i = 0; i < sortedList.size(); i++) {
				sortedList.get(i).appendHTML(userHTML);
			}

			html.println(userHTML);
			html.println("</div>");

			templates.printWritrPageEnd(html);
		}

	}

	/**
	 * bring up a particular post and its comments
	 * 
	 * @param resp
	 *            http response object -- where we respond to the user.
	 * @param numPost
	 *            uid of post
	 * @throws IOException
	 *             -- If the user hangs up on us while we're writing back or
	 *             gave us a half-request.
	 * @throws ServletException
	 *             -- If we ask for something that's not there, this might
	 *             happen.
	 */
	public void returnPostPage(HttpServletResponse resp, int numPost) throws IOException, ServletException {
		try (PrintWriter html = resp.getWriter()) {
			templates.printWritrPageStart(html, "Safe Place", metaURL);

			// print out the particular post
			html.println("<div class=\"body\">");

			SafePost writrMessage = data.getPost(numPost - 1);

			StringBuilder messageHTML = new StringBuilder();
			writrMessage.appendHTML(messageHTML);

			html.println(messageHTML);
			html.println("</div>");

			html.println("<div class=\"body comment\">");

			ArrayList<SafeComment> comments = new ArrayList<>(writrMessage.getCommentList());

			// iterate through all comments and print out
			StringBuilder commentHTML = new StringBuilder();
			for (SafeComment writrComment : comments) {
				writrComment.appendHTML(commentHTML);
			}
			html.println(commentHTML);
			html.println("</div>");

			// print out the form to write new comments
			templates.printWritrCommentForm(html, numPost - 1);

			templates.printWritrPageEnd(html);
		}
	}

	/**
	 * bring up the front page of the website
	 * 
	 * @param resp
	 *            http response object -- where we respond to the user.
	 * @throws IOException
	 *             -- If the user hangs up on us while we're writing back or
	 *             gave us a half-request.
	 * @throws ServletException
	 *             -- If we ask for something that's not there, this might
	 *             happen.
	 */
	public void returnFrontPage(HttpServletResponse resp) throws IOException, ServletException {
		try (PrintWriter html = resp.getWriter()) {
			templates.printWritrPageStart(html, "Writr", metaURL);

			// Print the form at the top of the page
			templates.submitArtCategory(html);
			// templates.printWritrPostForm(html);

			// Print all of our messages
			html.println("<div class=\"body\">");

			// get a copy to sort:
			ArrayList<SafePost> messages = new ArrayList<>(data.getMessageList());
			Collections.sort(messages);

			StringBuilder messageHTML = new StringBuilder();
			for (SafePost writrMessage : messages) {
				writrMessage.appendHTML(messageHTML);
			}
			html.println(messageHTML);
			html.println("</div>");

			// when we have a big page,
			if (messages.size() > 25) {
				// Print the submission form again at the bottom of the page
				templates.printWritrPostForm(html);
			}
			templates.printWritrPageEnd(html);
		}
	}

	public void returnNewPage(HttpServletResponse resp) throws IOException, ServletException {
		try (PrintWriter html = resp.getWriter()) {
			templates.printWritrPageStart(html, "Writr", metaURL);

			// Print the form at the top of the page
			templates.printArtistForm(html);
			templates.printVisitorForm(html);

			// Print all of our messages
			html.println("<div class=\"body\">");

			html.println("</div>");

			templates.printWritrPageEnd(html);
		}
	}

	public void returnVisitorPage(HttpServletResponse resp) throws IOException, ServletException {
		try (PrintWriter html = resp.getWriter()) {
			templates.printWritrPageStart(html, "Writr", metaURL);

			// Print the form at the top of the page
			templates.searchAsVisitor(html);
			// templates.printWritrPostForm(html);

			// Print all of our messages
			html.println("<div class=\"body\">");

			// get a copy to sort:
			ArrayList<SafePost> messages = new ArrayList<>(data.getMessageList());
			Collections.sort(messages);

			StringBuilder messageHTML = new StringBuilder();
			for (SafePost writrMessage : messages) {
				writrMessage.appendHTML(messageHTML);
			}
			html.println(messageHTML);
			html.println("</div>");

			// when we have a big page,
			if (messages.size() > 25) {
				// Print the submission form again at the bottom of the page
				templates.printWritrPostForm(html);
			}
			templates.printWritrPageEnd(html);
		}
	}
	
	
	
	public void returnArtistPage(HttpServletResponse resp) throws IOException, ServletException {
		try (PrintWriter html = resp.getWriter()) {
			templates.printWritrPageStart(html, "Writr", metaURL);

			// Print the form at the top of the page
			templates.submitArtCategory(html);
			 templates.printWritrPostForm(html);

			// Print all of our messages
			html.println("<div class=\"body\">");

			// get a copy to sort:
//			ArrayList<SafePost> messages = new ArrayList<>(data.getMessageList());
//			Collections.sort(messages);
//
//			StringBuilder messageHTML = new StringBuilder();
//			for (SafePost writrMessage : messages) {
//				writrMessage.appendHTML(messageHTML);
//			}
//			html.println(messageHTML);
//			html.println("</div>");

			// when we have a big page,
			//if (messages.size() > 25) {
				// Print the submission form again at the bottom of the page
				templates.printWritrPostForm(html);
		//	}
			templates.printWritrPageEnd(html);
		}
	}

	/**
	 * When a user submits (enter key) or pressed the "Comment!" button, we'll
	 * get their request in here. This is called explicitly from handle, above.
	 * 
	 * @param req
	 *            -- we'll grab the form parameters from here.
	 * @param resp
	 *            -- where to write their "success" page.
	 * @throws IOException
	 *             again, real life happens.
	 * @throws ServletException 
	 */
	
	private void submitVisitorSearch(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException 
	{
		
			Map<String, String[]> parameterMap = req.getParameterMap();
			System.out.println("visitor search submitted");

		String visitorSearchUser = Util.join(parameterMap.get("searchArtist"));
		String visitorSearchCategory = Util.join(parameterMap.get("category"));
		
		

		Vector<SafePost> VisitorsearchuserList = data.postList(visitorSearchUser);
		Vector<SafePost> VisitorSearchCategorhyList = data.postList(visitorSearchCategory);

		if (visitorSearchUser!=null&&visitorSearchCategory.equals("none") ) {
			
			{
			//System.out.println("visitor search submitted");
			returnSearchPage(resp, VisitorsearchuserList);
			}
//			
//		 if (visitorSearchUser==null&& !(visitorSearchCategory.equals("none")))
//			{
//				returnSearchPage(resp, VisitorSearchCategorhyList);
//
//			}
//		 
//		 if (visitorSearchUser!=null&& visitorSearchCategory.equals("none")==false)
//			{
//				returnSearchPage(resp, VisitorsearchuserList);
//
//				returnSearchPage(resp, VisitorSearchCategorhyList);
//
//			}
		}
		
//		else if(VisitorSearchCategorhyList.size()>0&& visitorSearchUser==null && visitorSearchCategory!=null)
//		{
//			returnSearchPage(resp, VisitorSearchCategorhyList);
//
//		}
//		else if(VisitorSearchCategorhyList.size()>0&& visitorSearchUser==null && visitorSearchCategory!=null)
//		{
//			returnSearchPage(resp, VisitorSearchCategorhyList);
//			returnSearchPage(resp, VisitorsearchuserList);
//
//		}
//		
	}
	
	private void submitVisitorCatagorySearch(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException 
	{
			Map<String, String[]> parameterMap = req.getParameterMap();
	
		String visitorSearchCategory = Util.join(parameterMap.get("category"));
	
		Vector<SafePost> VisitoruserCategory = data.postList(visitorSearchCategory);
		if (VisitoruserCategory.size() > 0) {
			returnSearchPage(resp, VisitoruserCategory);
		}
	}
	private void submitCommentPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		Map<String, String[]> parameterMap = req.getParameterMap();

		// if for some reason, we have multiple "message" fields in our form,
		// just put a space between them, see Util.join.
		// Note that comment comes from the name="comment" parameter in our
		// <input> elements on our form.
		String comment = Util.join(parameterMap.get("comment"));
		String username = Util.join(parameterMap.get("commentUser"));
		int uid = Integer.parseInt(Util.join(parameterMap.get("uid")));
		String path = "post/" + Integer.toString(uid + 1);

		if ((!username.equals("")) && (!comment.equals(""))) {

			// Good, got new comment from form.
			resp.setStatus(HttpServletResponse.SC_ACCEPTED);
			data.getPost(uid)
					.addNewComment(new SafeComment(username, comment, data.getPost(uid).getCommentList().size() + 1));

			templates.returnResponsePage(resp, path, metaURL);
			return;
		}
		// user submitted something weird.
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad user.");
	}

	/**
	 * When a user submits (enter key) or pressed the "Write!" button, we'll get
	 * their request in here. This is called explicitly from handle, above.
	 * 
	 * @param req
	 *            -- we'll grab the form parameters from here.
	 * @param resp
	 *            -- where to write their "success" page.
	 * @throws IOException
	 *             again, real life happens.
	 * @throws ServletException 
	 */
	private void submitPostPage(Request req, HttpServletResponse resp) throws IOException, ServletException {
		
		
		Map<String, String[]> parameterMap = req.getParameterMap();

		// if for some reason, we have multiple "message" fields in our form,
		// just put a space between them, see Util.join.
		// Note that message comes from the name="message" parameter in our
		// <input> elements on our form.
		String message = Util.join(parameterMap.get("message"));
		String title = Util.join(parameterMap.get("title"));
		String username = Util.join(parameterMap.get("user"));
		String categoryName = Util.join(parameterMap.get("category"));
		//String fileName = Util.join(parameterMap.get("fileUpload"));
		 
		
		   for (Part p : req.getParts()) {
			   System.out.println("PART! "+p.getName()+" "+p.getContentType());
		   }
		   BufferedImage img = ImageIO.read(req.getPart("fileUpload").getInputStream());
		   
		   String imagePath = "static/image/"+imageCounter+".jpeg";
		ImageIO.write(img, "jpeg", new File(
	                imagePath));
		imageCounter++;

		   
		   
		   System.out.println(categoryName);
		if ((message != null) && (!username.equals(""))) {
			//write onto file and add file string onto post
			

			// Good, got new message from form.
			resp.setStatus(HttpServletResponse.SC_ACCEPTED);
			data.getMessageList().add(new SafePost(username, categoryName, message, data.getNumMessages() + 1, imagePath) );
			data.savePosts();
			
			templates.returnResponsePage(resp, "front", metaURL);

			return;
		}
		
		// user submitted something weird.
		resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad user.");
	}
}



