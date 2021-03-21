package deliverable;

import java.io.FileWriter;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;


public class CSVWriter {
	
	
	
	
	
	public static void writeCsv(String filePath, TreeMap<Month, ArrayList<String>> ticketMonthMap) {
		 
		
		  
		//Log.infoLog("starting write user.csv file: " + filePath);
		System.out.println("starting write user.csv file: " + filePath);
		  try (
		   FileWriter fileWriter = new FileWriter(filePath)) {
		   
		   fileWriter.append("Month ; Ticket ID\n");
		   for(Month month : ticketMonthMap.keySet()) {
			   for(String listTicketId : ticketMonthMap.get(month)) {
				   System.out.println("mese = " + month + "ticketID = " + listTicketId);
				   fileWriter.append(String.valueOf(month));
				   fileWriter.append(";");
				   fileWriter.append(listTicketId);
				   fileWriter.append("\n");
			   }
		   }
		   
		  } catch (Exception ex) {
	       System.out.println("Error in csv writer");
		   ex.printStackTrace();
		  
		  }
		 }
	
	
	
	
	public static void main(String[] args) {
		 
		 // main
		 }
		 
		 
}
