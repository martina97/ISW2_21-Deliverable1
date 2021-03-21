package deliverable;

import java.io.FileWriter;
import java.time.Month;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.logging.Level;


public class CSVWriter {
	
	
	
	static Logger logger = Logger.getLogger(CSVWriter.class.getName());

	
	public static void writeCsv(String filePath, TreeMap<Month, ArrayList<String>> ticketMonthMap) {
		 
		
	    logger.log(Level.INFO, "starting write user.csv file: {0}.",filePath); // oppure  logger.info("matteo");

		  try (
		   FileWriter fileWriter = new FileWriter(filePath)) {
		   
		   fileWriter.append("Month ; Ticket ID\n");
		   for(Month month : ticketMonthMap.keySet()) {
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
	
	
	
	
	public static void main(String[] args) {
		 
		 // main
		 }
		 
		 
}
