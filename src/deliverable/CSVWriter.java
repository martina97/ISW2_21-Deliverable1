package deliverable;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

import entities.Ticket;

import java.util.logging.Level;


public class CSVWriter {
	
	
	
	static Logger logger = Logger.getLogger(CSVWriter.class.getName());

	
	public static void writeCsv(String filePath, SortedMap<Month, ArrayList<String>> ticketMonthMap) {
		 
		
	    logger.log(Level.INFO, "starting write user.csv file: {0}.",filePath); 

		  try (
		   FileWriter fileWriter = new FileWriter(filePath)) {
		   
		   fileWriter.append("Month ; Ticket ID\n");
		   for(Entry<Month, ArrayList<String>> entry : ticketMonthMap.entrySet()) {
			   Month month = entry.getKey();
			   for(String listTicketId : ticketMonthMap.get(month)) {
				   fileWriter.append(String.valueOf(month));
				   fileWriter.append(";");
				   fileWriter.append(listTicketId);
				   fileWriter.append("\n");
			   }
		   }
		   
		   
		  } catch (Exception ex) {
			  logger.log(Level.SEVERE,"Error in csv writer");
			  ex.printStackTrace();
		  
		  }
		 }
	
public static void writeCsv2(String filePath, TreeMap<Integer, ArrayList<Month>> csvEntries) {
		 
		
	    logger.log(Level.INFO, "starting write user.csv file: {0}.",filePath); 

		  try (
		   FileWriter fileWriter = new FileWriter(filePath)) {
		   
		   fileWriter.append("Month-Year ; Number of fixed Tickets\n");
		   for (Entry<Integer, ArrayList<Month>> entry : csvEntries.entrySet()) {
			   ArrayList<Month> monthList = entry.getValue();
			   Integer year = entry.getKey();
			   fileWriter.append("JANUARY - "  + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.JANUARY).toString());
			   fileWriter.append("\n");
			   fileWriter.append("FEBRUARY - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.FEBRUARY).toString());
			   fileWriter.append("\n");
			   fileWriter.append("MARCH - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.MARCH).toString());
			   fileWriter.append("\n");
			   fileWriter.append("APRIL - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.APRIL).toString());
			   fileWriter.append("\n");
			   fileWriter.append("MAY - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.MAY).toString());
			   fileWriter.append("\n");
			   fileWriter.append("JUNE - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.JUNE).toString());
			   fileWriter.append("\n");
			   fileWriter.append("JULY - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.JULY).toString());
			   fileWriter.append("\n");
			   fileWriter.append("AUGUST - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.AUGUST).toString());
			   fileWriter.append("\n");
			   fileWriter.append("SEPTEMBER - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.SEPTEMBER).toString());
			   fileWriter.append("\n");
			   fileWriter.append("OCTOBER - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.OCTOBER).toString());
			   fileWriter.append("\n");
			   fileWriter.append("NOVEMBER - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.NOVEMBER).toString());
			   fileWriter.append("\n");
			   fileWriter.append("DECEMBER - " + year);
			   fileWriter.append(";");
			   fileWriter.append(Utils.countOccurrences(monthList,Month.DECEMBER).toString());
			   fileWriter.append("\n");
		   }
		   
		   
		   
		  } catch (Exception ex) {
			  logger.log(Level.SEVERE,"Error in csv writer");
			  ex.printStackTrace();
		  
		  }
		 }	
	
	public static void main(String[] args) {
		 
		 // main
		 }
		 
		 
}
