package net.ypsilon;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.TimeZone;
import java.util.regex.*;

public class Parser {

	private StringBuffer content = new StringBuffer();
	private long         processTime;


	private void readFile(String filename) throws IOException{
        BufferedReader in = new BufferedReader( new FileReader(filename));
        String line = null;
        while((line = in.readLine()) != null){
        	if (line.trim().length() > 0){
        	  // reverse storing - for the search
        	  content.insert(0,line);
        	}
        }
	}

	public String getContent(){
		return content.toString();
	}

/*

[16:56:00]
[16:56:00] *------------------------------*
[16:56:00] Folding@Home Gromacs Core
[16:56:00] Version 1.90 (March 8, 2006)
[16:56:00]
[16:56:00] Preparing to commence simulation
[16:56:00] - Assembly optimizations manually forced on.
[16:56:00] - Not checking prior termination.
[16:56:00] - Expanded 293054 -> 1461493 (decompressed 498.7 percent)
[16:56:01]
[16:56:01] Project: 3041 (Run 9, Clone 342, Gen 52)
[16:56:01]
[16:56:01] Assembly optimizations on if available.
[16:56:01] Entering M.D.
[16:56:21] (Starting from checkpoint)
[16:56:21] Protein: p3041_supervillin-03
[16:56:21]
[16:56:21] Writing local files
[16:56:21] Completed 183513 out of 5000000 steps  (4)
[16:56:21] Extra SSE boost OK.

*/

	private void analyzeLog(ProjectStat projectStat){

	  String contentStr = content.toString();

  	  //  Project header
	  // [16:56:01] Project: 3041 (Run 9, Clone 342, Gen 52)

	  Pattern p = Pattern.compile("] Project: (\\d{1,4})");
	  Matcher m = p.matcher(contentStr);

      // Project ID
	  boolean found = m.find();
	  if (found){
	      projectStat.setId( m.group(1) );
	  };

	  // search last percent value
	  // [16:56:21] Completed 183513 out of 5000000 steps  (4)
	  p = Pattern.compile("\\[(\\d{2,2}):(\\d{2,2}):(\\d{2,2})\\] Completed (\\d{1,}) out of (\\d{1,}) steps  \\((\\d{1,2})");

	  // last step
	  m = p.matcher(contentStr);

      found = m.find();

	  if (found){
	  	 projectStat.setCurrentStep( Integer.parseInt(m.group(4)));
		 projectStat.setTotalSteps( Integer.parseInt(m.group(5)));
		 projectStat.setCurrentPersantage(Integer.parseInt(m.group(6)));

		 Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		 cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(m.group(1)));
		 cal.set(Calendar.MINUTE, Integer.parseInt(m.group(2)));
		 cal.set(Calendar.SECOND, Integer.parseInt(m.group(3)));
		 projectStat.setCurrentDate(cal);
	  };

	  // previous step info - we need it for PPD

	  found = m.find();
      if (found){
	    Calendar calNext = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
	    calNext.set(Calendar.HOUR_OF_DAY, Integer.parseInt(m.group(1)));
	    calNext.set(Calendar.MINUTE, Integer.parseInt(m.group(2)));
        calNext.set(Calendar.SECOND, Integer.parseInt(m.group(3)));
	    projectStat.setSecondDate(calNext);
	    projectStat.setState(ProjectStat.S_WORKING);
   	   }

      // check if the client is really down.
	  if (contentStr.startsWith("Folding@Home Client Shutdown.")){
			 projectStat.setState(ProjectStat.S_SHUTDOWN);
	  }
	  else
	  {
		  // TODO
		  // check if the WU hangs - need to check :last timestamp, step time and the current date
		  // if  the current timestamp > last timestamp + 2(?)*step time  - the client hangs
		  // and we need to restart it
          // TODO
	  }


	}


	public ProjectStat parse(String filename) throws IOException{

   	    long starttime = System.currentTimeMillis();

		readFile(filename);
		ProjectStat projectStat =  new ProjectStat();
	    projectStat.setPool(new ProjectPool(null));
        analyzeLog(projectStat);

        processTime = System.currentTimeMillis() - starttime;

        return projectStat;
	}

	public int getLinesCount(){
		return content.length();
	}


	public long getProcessTime() {
		return processTime;
	}


	public static void main(String[] args) throws IOException {

	   String filename = args[0];

  	   Parser parser = new Parser();

	   ProjectStat projectStat = parser.parse( filename );

       System.out.println("============ project info ========================================================");
       System.out.println("(" + projectStat.getComputerName() + ") " +
    		              "Project ID: " + projectStat.getId() +
    		              " Credit: " + projectStat.getCurrentProject().getCredit() + "\n" +
    		              "Filename: " + filename);
       System.out.println("----------------------------------------------------------------------------------");

	   SimpleDateFormat df = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );

	   System.out.println("Current Status  : " + projectStat.getState());
	   System.out.println("Current Progress: " + projectStat.getCurrentPersantage() + "% ("  + projectStat.getCurrentStep() + " of " + projectStat.getTotalSteps() + ")" );
	   System.out.println("Last update time: " + df.format(projectStat.getCurrentDate().getTime()) );
	   System.out.println("PredictedDate   : " + df.format(projectStat.getPredictedFinalDate().getTime()) );

	   Formatter fmt = new Formatter();
	   System.out.println(
			   fmt.format("Step (hh:mm:ss) : %02d:%02d:%02d",
			   new Object[] {new Integer(projectStat.getStepTimeInHours()), new Integer(projectStat.getStepTimeInMins()), new Integer(projectStat.getStepTimeInSecs())} ));
	   System.out.println("PPD             : " + projectStat.getPpd() );

       System.out.println("============ end =================================================================");


//       System.out.println("Processing time = " + (endtime - starttime) + " ms.");
	}

}
