import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;

public class Myserver{
	
	public static void main(String args[])throws Exception
	{
		final ServerSocket server = new ServerSocket(8080);
		System.out.println("Listning for connection on port 8080 ...");
		while(true)
		{
			Socket clientSocket = server.accept();
			
			
			// ########## Read the Request ##########	
			
			BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			String line = br.readLine();
			int first=0;
			String filename="";
			while(!line.isEmpty())
			{
				if(first==0)
				filename=line;
				System.out.println(">> "+line);
				line = br.readLine();
				first++;
			}
			
			
			// ########## Prepare HTTP response ##########
			
			int start_index = filename.indexOf("/");
			int last_index = filename.lastIndexOf(" HTTP");
			filename=filename.substring(start_index+1,last_index);
			String httpResponse = filename;
			
			//File Reading
			try
			{
				File myFile = new File(filename);
				byte[] resarray = new byte [(int)myFile.length()];
				FileInputStream fis = new FileInputStream(myFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(resarray,0,resarray.length);
				
				// ########## Send the required response ##########
				
				clientSocket.getOutputStream().write(("HTTP/1.1 200 OK\r\n\r\n").getBytes("UTF-8"));
				clientSocket.getOutputStream().write(resarray,0,resarray.length);
				
				// ########## Close current socket ##########
			}
			catch(Exception e)
			{
				File myFile = new File("404.html");
				byte[] resarray = new byte [(int)myFile.length()];
				FileInputStream fis = new FileInputStream(myFile);
				BufferedInputStream bis = new BufferedInputStream(fis);
				bis.read(resarray,0,resarray.length);
				
				// ########## Send the required response ##########
				
				clientSocket.getOutputStream().write(("HTTP/1.1 200 OK\r\n\r\n").getBytes("UTF-8"));
				clientSocket.getOutputStream().write(resarray,0,resarray.length);
			}
			
			clientSocket.close();
		}
	}//End of main
}//End of Class
		
