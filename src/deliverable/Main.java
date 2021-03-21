package deliverable;

import java.util.logging.Logger;

import org.json.JSONException;


import java.io.IOException;
import java.time.Month;
import java.util.ArrayList;
import java.util.SortedMap;

public class Main {
	
	static Logger logger = Logger.getLogger(Main.class.getName());
	
   public static void main(String[] args) throws IOException, JSONException {
	   
	   
	   SortedMap<Month, ArrayList<String>> ticketMonthMap = GetJIRAInfo.retrieveTickets();
	   
	   String filePath = "D:\\" + "Programmi\\Eclipse\\eclipse-workspace\\ISW2_21-Deliverable1\\csv\\TicketsAndMonths.csv";

	   CSVWriter.writeCsv(filePath, ticketMonthMap);
	   //trovo l'anno in cui ci sono stati la maggior parte di ticket risolti
   }
}