package main.java.edu.mtholyoke.cs341bd.writr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * a model to hold the MessageList of server
 * @author Hiwete and shanzehagrawala
 *
 */
public class SafeModel 
{
	Vector<SafePost> messageList;
	BufferedReader in;

	
	
	/**
	 * Initializes the messageList a new vector
	 * @throws IOException 
	 */
	public SafeModel() throws IOException {
		//File file = new File("post.txt");
		in=new BufferedReader(new FileReader("post.txt"));
	        if(in==null){
	        	readPosts();
	        }
		messageList = new Vector<>();
		

	}
	
	
	

	/**
	 * Getter for the messageList
	 * @return the Vector of messageList
	 */
	public Vector<SafePost> getMessageList() 
	{
		return this.messageList;
	}
	
	
	/**
	 * Get the size of the messageList
	 * @return an int - size of the list
	 */
	public int getNumMessages() {
		return this.messageList.size();
	}
	
	
	
//	public HashSet<GutenbergBook> flagBook(String bookID) throws IOException {
//
//
//		try(PrintWriter writer = new PrintWriter(new FileWriter("flagged.txt", true))) {
//
//			writer.println(bookID);
//
//		} catch(Exception e) {
//
//
//		}
//
//		@SuppressWarnings("resource")
//
//		BufferedReader in = new BufferedReader(new FileReader("flagged.txt"));
//
//		String line ="";
//
//		//String line = in.readLine(); // <-- read whole line
//
//		String a = "";
//
//
//
//		while ( (line = in.readLine()) != null)
//
//		{    
//
//			StringTokenizer tk = new StringTokenizer(line);
//
//			if (tk.hasMoreTokens()) {
//
//				a = tk.nextToken();
//
//			}
//
//			System.out.println("flagged: " + a );
//
//			flaggedBooks.add(library.get(a));
//
//		}
//
//
//		return flaggedBooks;
//
//	}
	
	/**
	 * Method that creates a postList with a particular user's posts
	 * @param username, a String
	 * @return postList - the list with the user's posts
	 * @throws IOException 
	 */
	public Vector<SafePost> postList(String username) throws IOException 
	{
		Vector<SafePost> postList = new Vector<>();

		for (int i = 0; i<messageList.size(); i++)
		{	
			if ( messageList.get(i).getUsername().equals(username))
				
			{
				
				postList.add(messageList.get(i));
				
			}
		}
		
		return postList;
		
	}
	
	public  void savePosts() throws IOException
	{
		try(PrintWriter writer = new PrintWriter(new FileWriter("post.txt", true))) {
			
			System.out.println("Adding to message list");
			for(int i = 0; i<messageList.size(); i++){
				String post =messageList.get(i).convertToString();
				System.out.println("THIS IS POST:" +post);
				writer.println(post);
			
			}

		} catch(Exception e) {


		}
		
	}
	
	
	public void readPosts() throws IOException{

		@SuppressWarnings("resource")

		BufferedReader in = new BufferedReader(new FileReader("post.txt"));

		String line ="";

		//String line = in.readLine(); // <-- read whole line

		String a = "";

		while ( (line = in.readLine()) != null)

		{    
			SafePost post= new SafePost(line); 
			

			StringTokenizer tk = new StringTokenizer(line);

			if (tk.hasMoreTokens()) {

				a = tk.nextToken();
				System.out.println("THIS IS POST I NREAD:" +post);
				messageList.add(post);
			}
		}
	}

		
	
	

	
	/**
	 * get a particular post in the messageList
	 * @param numPost index of the post in the messageList
	 * @return a post
	 */
	public SafePost getPost(int numPost) {
		return this.messageList.get(numPost);
	}
}
