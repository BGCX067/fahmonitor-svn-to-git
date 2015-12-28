package net.ypsilon;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Formatter;

import org.apache.log4j.Logger;

public class FAHCheckJob{

    private static  Logger logger = Logger.getLogger("net.ypsilon.FAHCheckJob");

    private String    logFileName;
    private String    ftpFileName;
    private FTPUpload ftp;
	private String    logStr = "";

    public String getFtpFileName() {
		return ftpFileName;
	}

	public void setFtpFileName(String ftpFileName) {
		this.ftpFileName = ftpFileName;
	}

	public String getLogFileName() {
		return logFileName;
	}

	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	public FTPUpload getFtp() {
		return ftp;
	}

	public void setFtp(FTPUpload ftp) {
		this.ftp = ftp;
	}


	private void sendToLogAndFTP(String str) {
		logger.info(str);
		logStr = logStr.concat(str) + "<br>";
	}


	public void executeJob() throws IOException {

    	  Parser parser = new Parser();

    	  ProjectStat projectStat = parser.parse( logFileName );

    	  logStr  = "";

    	  sendToLogAndFTP("============ project info ==============================");

    	  sendToLogAndFTP("(" + projectStat.getComputerName() + ") " +
        	              "Project ID: " + projectStat.getId() +
        	              " Credit: " + projectStat.getCurrentProject().getCredit());

    	  sendToLogAndFTP("Log: " + logFileName);
    	  sendToLogAndFTP("--------------------------------------------------------");

    	  SimpleDateFormat df = new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss" );
    	  sendToLogAndFTP("Current Status  : " + projectStat.getState());
    	  sendToLogAndFTP("Current Progress: " + projectStat.getCurrentPersantage() + "% ("  + projectStat.getCurrentStep() + " of " + projectStat.getTotalSteps() + ")" );

    	  sendToLogAndFTP("Last Update Date: " + df.format(projectStat.getCurrentDate().getTime()) );
    	  sendToLogAndFTP("Predicted Date  : " + df.format(projectStat.getPredictedFinalDate().getTime()) );

    	  Formatter fmt = new Formatter();
    	  sendToLogAndFTP(
    			   fmt.format("Step (hh:mm:ss) : %02d:%02d:%02d",
    			   new Object[] {new Integer(projectStat.getStepTimeInHours()), new Integer(projectStat.getStepTimeInMins()), new Integer(projectStat.getStepTimeInSecs())} ).toString());

    	  sendToLogAndFTP("PPD             : " + projectStat.getPpd() );

//    	  logger.info("Processing time = " + parser.getProcessTime() + " ms.");

    	  sendToLogAndFTP("============ end ===================================");

    	  ftp.UploadFile( logStr, ftpFileName);

    }


}