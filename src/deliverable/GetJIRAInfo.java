package deliverable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import entities.Ticket;

import org.json.JSONArray;

public class GetJIRAInfo {
	
	private GetJIRAInfo() {}


	static Logger logger = Logger.getLogger(GetJIRAInfo.class.getName());


   private static String readAll(Reader rd) throws IOException {
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }


   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		InputStream is = new URL(url).openStream();
		try (

			// BufferedReader rd = new BufferedReader(new InputStreamReader(is,
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()))) {

			String jsonText = readAll(rd);
			return (new JSONObject(jsonText));
		} finally {
			is.close();
		}
	}

 
//ritorna la lista di ticket con le corrispondenti resolutionDate e creationDate
 public static List<Ticket> retrieveTickets() {
	  
	  String projName ="DAFFODIL";
	   Integer j = 0;
	   Integer i = 0;
	   Integer total = 1;
	   JSONArray issues ;
	 /// RITORNA UNA LISTA DI TICKET
	 ArrayList<Ticket> ticketList = new ArrayList<>();
     //Get JSON API for closed bugs w/ AV in the project
     do {
        //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
        j = i + 1000;
        String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
               + projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
               + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
               + i.toString() + "&maxResults=" + j.toString();
        try 
        {
        JSONObject json = readJsonFromUrl(url);
        
        issues = json.getJSONArray("issues");
        total = json.getInt("total");
        for (; i < total && i < j; i++) {
           //Iterate through each bug
           String key = issues.getJSONObject(i%1000).get("key").toString();
           Ticket ticket = new Ticket(key);
           ticketList.add(ticket);
           // TICKET ID = ticket.getID()
        
        } 
     }
	
		catch (JSONException e) 
		{
			logger.log(Level.SEVERE,"Error during JSON document analysis.");
			System.exit(1);		  
		} 
		catch (IOException e) 
		{
			logger.log(Level.SEVERE,"Error reading JSON file.");
			System.exit(1);		  
		}
	} 
     	while (i < total);  
          
     
     return ticketList;
  }
  
 
}
