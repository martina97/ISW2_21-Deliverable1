package deliverable;

import java.util.logging.Logger;

import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;

public class Main {
	//static Logger logger = Logger.getLogger(Main.class.getName());
	
   public static void main(String[] args) throws IOException, JSONException, ParseException {
	    //logger.log(Level.INFO, "matteo"); // oppure  logger.info("matteo");
	    //se errore -->  logger.severe("matteo");
	   getReleaseInfo.main(args);
	   RetrieveTicketsID.main(args);
   }
}