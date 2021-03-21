package deliverable;

import java.util.logging.Logger;

import org.json.JSONException;

import entities.Ticket;

import java.io.IOException;
import java.text.ParseException;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Level;

public class Main {
	
	private static List<Ticket> ticketList;
	private static TreeMap<Month, ArrayList<String>> ticketMonthMap;

	static Logger logger = Logger.getLogger(Main.class.getName());
	
   public static void main(String[] args) throws IOException, JSONException, ParseException {
	    //logger.log(Level.INFO, "matteo"); // oppure  logger.info("matteo");
	    //se errore -->  logger.severe("matteo");
	   //GetGITInfo.main(args);
	   
	   // metto in ticketList tutti i ticket del progetto
	   //ticketList = GetJIRAInfo.retrieveTickets();
	   //GetJIRAInfo.retrieveTickets();
	   ticketMonthMap = GetJIRAInfo.retrieveTickets();
	   
	   String filePath = "D:\\Programmi\\Eclipse\\eclipse-workspace\\ISW2_21-Deliverable1\\csv\\TicketsAndMonths.csv";

	   CSVWriter.writeCsv(filePath, ticketMonthMap);
	   //trovo l'anno in cui ci sono stati la maggior parte di ticket risolti
   }
}