package deliverable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import entities.Ticket;

import org.json.JSONArray;

public class GetJIRAInfo {



   private static String readAll(Reader rd) throws IOException {
	      StringBuilder sb = new StringBuilder();
	      int cp;
	      while ((cp = rd.read()) != -1) {
	         sb.append((char) cp);
	      }
	      return sb.toString();
	   }

   public static JSONArray readJsonArrayFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
         String jsonText = readAll(rd);
         JSONArray json = new JSONArray(jsonText);
         return json;
       } finally {
         is.close();
       }
   }

   public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
      InputStream is = new URL(url).openStream();
      try {
         BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
         String jsonText = readAll(rd);
         JSONObject json = new JSONObject(jsonText);
         return json;
       } finally {
         is.close();
       }
   }

// ritorna la lista di ticket con le corrispondenti resolutionDate e creationDate
  public static TreeMap<Month, ArrayList<String>> retrieveTickets() throws JSONException, IOException {
	  
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
            //System.out.println(key + "\tdate = " + resolutionDate );
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

      //System.out.println(listaTicket.get(0).getID());
      //return ticketList;
      return ticketMonthMap;
   }

  
  
  public static Integer getMostFrequentYear(ArrayList<LocalDateTime> resolDateList) {
	  
	  TreeMap<Integer, Integer> capitalCities = new TreeMap<>();
	  for (int i = 0; i<resolDateList.size();i++) {
		  Integer year = resolDateList.get(i).getYear();
		  if (!capitalCities.containsKey(year)) {
			  //l'anno non e presente nell'Hash Map, quindi lo aggiungo
			  capitalCities.put(year, 1);
		  }
		  else {
			  //l'anno e presente nell'Hash Map, quindi incremento il valore associato all'anno
			  capitalCities.put(year, capitalCities.get(year) + 1);
		  }
		  //System.out.println(resolDateList.get(i).getYear());
	  }
	  System.out.println(capitalCities);
	  //tils.printTreeMap(capitalCities);
	  capitalCities.forEach((key, value) -> System.out.println(key + "= " + value + "\n\n"));

	  // trovo l'anno in cui ci sono stati più ticket risolti
	  Integer myYear = Collections.max(capitalCities.entrySet(), Map.Entry.comparingByValue()).getKey();
	  System.out.println(myYear);
	  return myYear;
  }
  
  
  public static void getTicketMonthMap(Integer myYear, ArrayList<Ticket> ticketList, TreeMap<Month, ArrayList<String>> ticketMonthMap) {
	  Integer count = 0;
	  for (int i =0; i<ticketList.size(); i++) {
		  Ticket ticket = ticketList.get(i);
		  if (ticket.getResolutionDate().getYear() == 2020) {
			  count++;
			  Month myMonth = ticket.getResolutionDate().getMonth();
			  String ticketID = ticket.getID();
			  ticketMonthMap.putIfAbsent(myMonth, new ArrayList<String>());
			  ticketMonthMap.get(myMonth).add(ticketID);
			  System.out.println("La data del ticket + : " + ticket.getResolutionDate());
		  }
	  }
	  System.out.println(ticketMonthMap.size());
	  System.out.println("count = " + count );
	  ticketMonthMap.forEach((key, value) -> System.out.println(key + "= " + value + "\n\n"));
	  
	  
  }
  
  public static void main(String[] args) throws IOException, JSONException {
		// Do nothing because is a main method

	}
 
}
