package deliverable;

import java.util.logging.Logger;

import java.util.logging.Level;

public class Main {
	static Logger logger = Logger.getLogger(Main.class.getName());
	
   public static void main(String[] args) {
	    logger.log(Level.INFO, "matteo"); // oppure  logger.info("matteo");
	    //se errore -->  logger.severe("matteo");
   }
}