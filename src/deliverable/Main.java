package deliverable;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jgit.api.errors.GitAPIException;

import org.eclipse.jgit.revwalk.RevCommit;

import entities.Ticket;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import java.util.SortedMap;
import java.util.TreeMap;


public class Main {
	
	private static Logger logger = Logger.getLogger(Main.class.getName());
	private static Path repoPath = Paths.get("D:/Programmi/Eclipse/eclipse-workspace/daffodil");
	private static List<Ticket> ticketList;
	
   public static void main(String[] args) throws IllegalStateException, GitAPIException, IOException {
	   
	   TreeMap<Integer, ArrayList<Month>> csvEntries = new TreeMap<>();
	   ticketList = GetJIRAInfo.retrieveTickets();
	   
	   List<RevCommit> commitList = GetGitInfo.getAllCommit(repoPath);
	   findCommitTicket(commitList, ticketList);
	   createEntriesCsv(csvEntries);
	   CSVWriter.writeCsv(csvEntries);

   	}
   

   
   
   public static void findCommitTicket(List<RevCommit> commitList,List<Ticket> ticketList ) {
	   /**
	    * Per ogni ticket presente in ticketList trovo la resolution date guardando la data dell'ultimo commit 
	    * in cui e' presente il ticket.
	    * 
	    * @param commitList		lista di tutti i commit del progetto
	    * @param ticketList		lista di tutti i ticket del progetto
	    */
	   
	   ArrayList<LocalDate> commitDateList = new ArrayList<>();
	   ArrayList<LocalDate> resolutionDates = new ArrayList<>();
	   TreeMap<Integer, ArrayList<Month>> yearMonthMap  = new TreeMap<>();
	   for (Ticket ticket : ticketList) {
		   for (RevCommit rev : commitList) {
			   String commit = rev.getFullMessage();
			   String ticketID1 = ticket.getID();
			   String ticketID2 = ticketID1.replace("DAFFODIL","DFDL");

			   if (commit.contains(ticketID1 +",") || commit.contains(ticketID1 +"\r") || commit.contains(ticketID1 +"\n")|| commit.contains(ticketID1 + " ") || commit.contains(ticketID1 +":")
							 || commit.contains(ticketID1 +".")|| commit.contains(ticketID1 +")")|| commit.contains(ticketID2 +")") || commit.contains(ticketID2 + ",") || commit.endsWith(ticketID1) || commit.endsWith(ticketID2) ||
							 commit.contains(ticketID2 + "\r")|| commit.contains(ticketID2+"\n") || commit.contains(ticketID2 + " ") || commit.contains(ticketID2 + ":") || commit.contains(ticketID2 + ".")) {				   
				   
				   LocalDate commitDate = rev.getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				   commitDateList.add(commitDate);
			   }
		   }
		   

		   if (!commitDateList.isEmpty()) {
			   /*
			    * In commitDateList ci sono tutte le date dei commit in cui e' presente il ticket. 
			    * Ordino le date dalla più vecchia alla piu' recente, e setto come resolution date la data 
			    * piu' recente, quindi l'ultimo elemento di commitDateList.
			    */
			   Collections.sort(commitDateList);
			   LocalDate resolutionDate = commitDateList.get(commitDateList.size()-1); // data più recente
			   ticket.setResolutionDate(resolutionDate);
		   }
		   
		   commitDateList.clear();
		   	   
	   }
   
	   Iterator<Ticket> ticket = ticketList.iterator();
   	   
	   //rimuovo dalla lista dei ticket tutti i ticket che non hanno una resolutionDate, ossia che non hanno nessun commit associato
	   while (ticket.hasNext()) {
		   Ticket t = ticket.next();
		   
		   if (t.getResolutionDate() == null) {
			   ticket.remove();
		   }
		   else {
			   resolutionDates.add(t.getResolutionDate());
			   Integer year = t.getResolutionDate().getYear();
			   yearMonthMap.putIfAbsent(year, new ArrayList<>());
			   yearMonthMap.get(year).add(t.getResolutionDate().getMonth());
		   }
	   }
	   yearMonthMap.forEach((key, value) -> logger.log(Level.INFO, "key: {0} --> value: {1} ",new Object[] { key, value,}));
   }
   
   
   
   
   public static SortedMap<Integer, ArrayList<Month>> createEntriesCsv(SortedMap<Integer, ArrayList<Month>> csvEntries ) {
	   
	   for(Ticket ticket : ticketList) {
		   int year = ticket.getResolutionDate().getYear();
		   Month month = ticket.getResolutionDate().getMonth();
		   csvEntries.putIfAbsent(year, new ArrayList<>());
		   csvEntries.get(year).add(month);
	   }
	   return csvEntries;
   }
   
   
}