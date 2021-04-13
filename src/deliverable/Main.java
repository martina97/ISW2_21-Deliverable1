package deliverable;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.InitCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.PersonIdent;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.json.JSONException;

import entities.Ticket;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TimeZone;
import java.util.TreeMap;


public class Main {
	
	private static Logger logger = Logger.getLogger(Main.class.getName());
	private static final String REPO = "D:/Programmi/Eclipse/eclipse-workspace/ISW2_21-Deliverable1/.git";
	private static final String REPO2 ="D:/Programmi/Eclipse/eclipse-workspace/daffodil/.git";
	private static Path repoPath = Paths.get("D:/Programmi/Eclipse/eclipse-workspace/ISW2_21-Deliverable1");
	private static Path repoPath2 = Paths.get("D:/Programmi/Eclipse/eclipse-workspace/daffodil");
	private static ArrayList<Ticket> ticketList;

	private static Repository repository;

   public static void main(String[] args) throws IllegalStateException, GitAPIException, IOException, ParseException {
	   
	   TreeMap<Integer, ArrayList<Month>> csvEntries = new TreeMap<>();
	   ticketList = GetJIRAInfo.retrieveTickets2();
	   ArrayList<RevCommit> commitList = new ArrayList<>();
	   String filePath = "D:\\" + "Programmi\\Eclipse\\eclipse-workspace\\ISW2_21-Deliverable1\\csv\\TicketsAndMonths.csv";

	   //CSVWriter.writeCsv(filePath, ticketMonthMap);
	   //trovo l'anno in cui ci sono stati la maggior parte di ticket risolti
	   
	   getGitINFO.getAllCommit(repoPath2, commitList);
	   ArrayList<LocalDate> resolutionDates = findCommitTicket(commitList, ticketList);
	   System.out.println("\n\nresolutionDates size == " + resolutionDates.size());
	   //createEntriesCsv(resolutionDates);
	   createEntriesCsv2(csvEntries);
	   
	   
	   CSVWriter.writeCsv2(filePath, csvEntries);


   	}
   
   public static TreeMap<Integer, ArrayList<Month>> createEntriesCsv2(TreeMap<Integer, ArrayList<Month>> csvEntries ) {
	   
	   
	   for(Ticket ticket : ticketList) {
		   int year = ticket.getResolutionDate().getYear();
		   Month month = ticket.getResolutionDate().getMonth();
		   csvEntries.putIfAbsent(year, new ArrayList<Month>());
		   csvEntries.get(year).add(month);
	   }
	   
	   for (Entry<Integer, ArrayList<Month>> entry : csvEntries.entrySet()) {
		   System.out.println("Key: " + entry.getKey() + ". Value: " + entry.getValue());
		}
	   
	   return csvEntries;
   }
   
   
   
   public static ArrayList<LocalDate> findCommitTicket(ArrayList<RevCommit> commitList,ArrayList<Ticket> ticketList ) throws IOException {
	   
	   ArrayList<LocalDate> commitDateList = new ArrayList<>();
	   ArrayList<LocalDate> resolutionDates = new ArrayList<>();
	   TreeMap<Integer, ArrayList<Month>> yearMonthMap  = new TreeMap<>();
	   FileWriter myWriter = new FileWriter("filename.txt");
	   Integer count = 0;
	   for (Ticket ticket : ticketList) {
		   count++;
		   System.out.println("IL TICKET E' : " + ticket.getID());
		   for (RevCommit rev : commitList) {
			   String commit = rev.getFullMessage();
			   //System.out.println("MESSAGGIO COMMIT : \n" + commit.getFullMessage() + "\n\n\n");
			   //myWriter.write(commit.getFullMessage() + "\n\n\n");
			   String ticketID1 = ticket.getID();
			   String ticketID2 = ticketID1.replace("DAFFODIL","DFDL");


			   //if (commit.getFullMessage().contains(ticketID1) || commit.getFullMessage().contains(ticketID2)) {
			   if (commit.contains(ticketID1 +",") || commit.contains(ticketID1 +"\r") || commit.contains(ticketID1 +"\n")|| commit.contains(ticketID1 + " ") || commit.contains(ticketID1 +":")
							 || commit.contains(ticketID1 +".")|| commit.contains(ticketID1 +")")|| commit.contains(ticketID2 +")") || commit.contains(ticketID2 + ",") || commit.endsWith(ticketID1) || commit.endsWith(ticketID2) ||
							 commit.contains(ticketID2 + "\r")|| commit.contains(ticketID2+"\n") || commit.contains(ticketID2 + " ") || commit.contains(ticketID2 + ":") || commit.contains(ticketID2 + ".")) {				   
				   //System.out.println(rev.getId());
				   //System.out.println(commit);

				   /*
				   PersonIdent authorIdent = commit.getAuthorIdent();
				   Date authorDate = authorIdent.getWhen();
				   TimeZone authorTimeZone = authorIdent.getTimeZone();
				   PersonIdent committerIdent = commit.getCommitterIdent();
				   Instant data = Instant.ofEpochSecond(commit.getCommitTime());
				   LocalDateTime ldt = LocalDateTime.ofInstant(Instant.ofEpochSecond(commit.getCommitTime()), ZoneOffset.UTC);
				   */
				   LocalDate commitDate = rev.getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				   commitDateList.add(commitDate);
				   //System.out.println("commitDate = " + commitDate);


				   
				   //System.out.println(ldt + "mese : " + ldt.getMonth() + "anno : " + ldt.getYear());
			   }
		   }
		   

		   if (commitDateList.size() != 0) {
			   //System.out.println("lista = " + commitDateList);
			   Collections.sort(commitDateList);
			   System.out.println("\n\nlista ordinata = " + commitDateList);
			   LocalDate resolutionDate = commitDateList.get(commitDateList.size()-1);
			   System.out.println("data pi� recente = " + resolutionDate);
			   ticket.setResolutionDate(resolutionDate);

		   }
		   

		   commitDateList.clear();
		   

		   System.out.println("################################\n\n");
	   
	   }
	   System.out.println("ALEEEEEEEEEE " + count);
   
	   Iterator<Ticket> ticket = ticketList.iterator();
   	   
	   //rimuovo dalla lista dei ticket tutti i ticket che non hanno una resolutionDate, ossia che non hanno nessun commit associato
	   while (ticket.hasNext()) {
		   Ticket t = ticket.next();
		   //System.out.println("TICKET ID: " + t.getID() + " ---> " + t.getResolutionDate() + "\n\n");
		   
		   if (t.getResolutionDate() == null) {
			   ticket.remove();
		   }
		   else {
			   resolutionDates.add(t.getResolutionDate());
			   Integer year = t.getResolutionDate().getYear();
			   yearMonthMap.putIfAbsent(year, new ArrayList<Month>());
			   yearMonthMap.get(year).add(t.getResolutionDate().getMonth());
			   
		   }
		   
	   }
	   yearMonthMap.forEach((key, value) -> logger.log(Level.INFO, "key: {0} --> value: {1} ",new Object[] { key, value,}));
	   myWriter.close();
	   return resolutionDates;
   }
   
   
   public static void createEntriesCsv(ArrayList<LocalDate> resDates) {
	   
	  
	   
   }
   

   
   
   
   
   
   
   
}