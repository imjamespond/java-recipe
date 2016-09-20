package test;

import java.net.*;
import java.io.*;

/**
* A complete Java class that demonstrates how to read content (text) from a URL
* using the Java URL and URLConnection classes.
* @author alvin alexander, devdaily.com
*/

public class UrlConnectionTest {

	public static void main(String[] args) {
		String ip = "+223.67.188.231";
		if(ip.indexOf('+')==0){
			ip = ip.substring(1, ip.length());
		}
		String output  = getUrlContents("http://ip.taobao.com/service/getIpInfo.php?ip="+ip);

	    System.out.println(output);
	}
	private static String getUrlContents(String theUrl)
	  {
	    StringBuilder content = new StringBuilder();
	 
	    // many of these calls can throw exceptions, so i've just
	    // wrapped them all in one try/catch statement.
	    try
	    {
	      // create a url object
	      URL url = new URL(theUrl);
	 
	      // create a urlconnection object
	      URLConnection urlConnection = url.openConnection();
	 
	      // wrap the urlconnection in a bufferedreader
	      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
	 
	      String line;
	 
	      // read from the urlconnection via the bufferedreader
	      while ((line = bufferedReader.readLine()) != null)
	      {
	        content.append(line + "\n");
	      }
	      bufferedReader.close();
	    }
	    catch(Exception e)
	    {
	      e.printStackTrace();
	    }
	    return content.toString();
	  }
}



