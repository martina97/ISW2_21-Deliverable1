package deliverable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import entities.Ticket;

import org.json.JSONArray;

public class GetJIRAInfo {

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

// ritorna la lista di ticket con le corrispondenti resolutionDate e creationDate
  public static SortedMap<Month, ArrayList<String>> retrieveTickets() throws JSONException, IOException {
	  
	  String projName ="DAFFODIL";
	   Integer j = 0;
	   Integer i = 0;
	   Integer total = 1;
	   Integer myYear; 
	   TreeMap<Month, ArrayList<String>> ticketMonthMap = new TreeMap<>();
	   JSONArray issues ;
	 /// RITORNA UNA LISTA DI TICKET
	 ArrayList<Ticket> ticketList = new ArrayList<>();
	 ArrayList<LocalDateTime> resolDateList = new ArrayList<>();
      //Get JSON API for closed bugs w/ AV in the project
      do {
         //Only gets a max of 1000 at a time, so must do this multiple times if bugs >1000
         j = i + 1000;
         String url = "https://issues.apache.org/jira/rest/api/2/search?jql=project=%22"
                + projName + "%22AND%22issueType%22=%22Bug%22AND(%22status%22=%22closed%22OR"
                + "%22status%22=%22resolved%22)AND%22resolution%22=%22fixed%22&fields=key,resolutiondate,versions,created&startAt="
                + i.toString() + "&maxResults=" + j.toString();
         JSONObject json = readJsonFromUrl(url);
         
         issues = json.getJSONArray("issues");
         total = json.getInt("total");
         for (; i < total && i < j; i++) {
            //Iterate through each bug
            String key = issues.getJSONObject(i%1000).get("key").toString();
            LocalDateTime resolutionDate= LocalDateTime.parse(issues.getJSONObject(i%1000).getJSONObject("fields").getString("resolutiondate").substring(0,16));
            Ticket ticket = new Ticket(key, resolutionDate);
            ticketList.add(ticket);
            resolDateList.add(resolutionDate);
         }  
      } while (i < total);  
      
      // trovo l'anno in cui c'è stata la maggior parte dei ticket risolti
      myYear = getMostFrequentYear(resolDateList);
      
      // creo mappa avente come chiave l'id del ticket e come valore il mese relativo all'anno più frequente
      getTicketMonthMap(myYear, ticketList, ticketMonthMap);
      System.out.println(ticketMonthMap);
      
      return ticketMonthMap;
   }

  
  
  public static Integer getMostFrequentYear(List<LocalDateTime> resolDateList) {
	  
	  TreeMap<Integer, Integer> yearsAndTickets = new TreeMap<>();
	  for (int i = 0; i<resolDateList.size();i++) {
		  Integer year = resolDateList.get(i).getYear();
		  if (!yearsAndTickets.containsKey(year)) {
			  //l'anno non e presente nell'Hash Map, quindi lo aggiungo
			  yearsAndTickets.put(year, 1);
		  }
		  else {
			  //l'anno e presente nell'Hash Map, quindi incremento il valore associato all'anno
			  yearsAndTickets.put(year, yearsAndTickets.get(year) + 1);
		  }
	  }
	  //yearsAndTickets.forEach((key, value) -> logger.log(Level.INFO, key + "= " + value + "\n\n"));
	  yearsAndTickets.forEach((key, value) -> logger.log(Level.INFO, "key: {0} --> value: {1} ",new Object[] { key, value,}));

	  // trovo l'anno in cui ci sono stati più ticket risolti
	  Integer myYear = Collections.max(yearsAndTickets.entrySet(), Map.Entry.comparingByValue()).getKey();
	  logger.log(Level.INFO,"\n\n{0}", myYear);
	return myYear;
  }
  
  
  public static void getTicketMonthMap(Integer myYear, List<Ticket> ticketList, SortedMap<Month, ArrayList<String>> ticketMonthMap) {
	  Integer count = 0;
	  for (int i =0; i<ticketList.size(); i++) {
		  Ticket ticket = ticketList.get(i);
		  if (ticket.getResolutionDate().getYear() == 2020) {
			  count++;
			  Month myMonth = ticket.getResolutionDate().getMonth();
			  String ticketID = ticket.getID();
			  ticketMonthMap.putIfAbsent(myMonth, new ArrayList<String>());
			  ticketMonthMap.get(myMonth).add(ticketID);
		  }
	  }
	  System.out.println(ticketMonthMap.size());
	  System.out.println("count = " + count );
	  ticketMonthMap.forEach((key, value) -> logger.log(Level.INFO, "key: {0} --> value: {1} ",new Object[] { key, value,}));
	  
	  
  }
  
  public static void main(String[] args) throws IOException, JSONException {
		// Do nothing because is a main method

	}
 
}
