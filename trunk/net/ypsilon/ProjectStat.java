
/*
   Current project info
*/

package net.ypsilon;

import java.net.InetAddress;
import java.util.Calendar;

public class ProjectStat {

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

	public static final int S_UNKNOWN    = 0;  //
	public static final int S_WORKING    = 1;  // [XX:XX:XX] Completed 770000 out of 1000000 steps  (77%)
	public static final int S_FINISHED   = 2;  // [XX:XX:XX] Finished a frame (1)
	public static final int S_SHUTDOWN   = 3;  // Folding@Home Client Shutdown.
	public static final int S_COMPLETED  = 4;  // A WU has been completed
	public static final int S_HANGS      = 5;  // Folding@Home Client hangs.

	private int            state = S_UNKNOWN;
	private String         id    = "UNKNOWN"; // Project Number for example - Project: 3041 (Run 9, Clone 342, Gen 52)

	private ProjectPool    pool;
	private ProjectDetails currentProject;

	private int            currentStep = 0;
	private int            totalSteps  = 0;
	private int            currentPersantage = 0;
	private Calendar       currentDate;

    // need to calculate the current PPD
	private int            secondStep = 0;
	private Calendar       secondDate;

	private int            stepTimeInSecTotal = 0;
	private int            stepTimeInSecs     = 0;
	private int            stepTimeInMins     = 0;
	private int            stepTimeInHours    = 0;

	private String         fileName;

	//---------------------------------------------------------------------------------------

	public void setId( String id ){
		this.id = id;
		currentProject = pool.find(Integer.parseInt(id));
	}

	public String getId(){
		return this.id;
	}

	//---------------------------------------------------------------------------------------

	public void setCurrentStep( int currentStep ){
		this.currentStep = currentStep;
	}

	public int getCurrentStep(){
		return this.currentStep;
	}

	//---------------------------------------------------------------------------------------

	public void setTotalSteps( int totalSteps ){
		this.totalSteps = totalSteps;
	}

	public int getTotalSteps(){
		return this.totalSteps;
	}

	//---------------------------------------------------------------------------------------

	public long getPpd(){

		if ((currentProject.getCredit() > 0) && (stepTimeInSecTotal>0)) {

			//return 1768*(60*60*24/100)/(13*60+41);
			return currentProject.getCredit() * (60*60*24/100) / stepTimeInSecTotal;
			
		}
		else
		  return 0;
	}

//	---------------------------------------------------------------------------------------

	public Calendar getCurrentDate(){
		return this.currentDate;
	}

	public void setCurrentDate( Calendar currentDate ){
		this.currentDate = currentDate;
	}

	//---------------------------------------------------------------------------------------

	public void setSecondStep( int secondStep ){

		this.secondStep = secondStep;

	}


	public int getSecondStep(){
		return this.secondStep;
	}

	//---------------------------------------------------------------------------------------

	public Calendar getSecondDate(){
		return this.secondDate;
	}

	public void setSecondDate( Calendar secondDate ){

		this.secondDate = secondDate;

		stepTimeInSecTotal = (int) ((currentDate.getTime().getTime() - secondDate.getTime().getTime())/1000);
		if (stepTimeInSecTotal > 0){
  		  stepTimeInHours    = stepTimeInSecTotal / 3600;
		  stepTimeInMins     = (stepTimeInSecTotal - (stepTimeInHours*3600)) / 60;
		  stepTimeInSecs     = stepTimeInSecTotal % 60;
		}
	}

	//---------------------------------------------------------------------------------------

	public void setCurrentPersantage( int currentPersantage ){
		this.currentPersantage = currentPersantage;
	}

	public int getCurrentPersantage(){
		return this.currentPersantage;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getStepTimeInHours() {
		return stepTimeInHours;
	}

	public void setStepTimeInHours(int stepTimeInHours) {
		this.stepTimeInHours = stepTimeInHours;
	}

	public int getStepTimeInMins() {
		return stepTimeInMins;
	}

	public void setStepTimeInMins(int stepTimeInMins) {
		this.stepTimeInMins = stepTimeInMins;
	}

	public int getStepTimeInSecs() {
		return stepTimeInSecs;
	}

	public void setStepTimeInSecs(int stepTimeInSecs) {
		this.stepTimeInSecs = stepTimeInSecs;
	}

	public int getStepTimeInSecTotal() {
		return stepTimeInSecTotal;
	}

	public void setStepTimeInSecTotal(int stepTimeInSecTotal) {
		if (stepTimeInSecTotal > 0){
		  this.stepTimeInSecTotal = stepTimeInSecTotal;
		}
	}

	public ProjectPool getPool() {
		return pool;
	}

	public void setPool(ProjectPool pool) {
		this.pool = pool;
	}

	public ProjectDetails getCurrentProject() {
		return currentProject;
	}

	public void setCurrentProject(ProjectDetails currentProject) {
		this.currentProject = currentProject;
	}

	public Calendar getPredictedFinalDate() {
		Calendar predictedFinalDate = currentDate;
		int delta = (100 - currentPersantage)*stepTimeInSecTotal;
		if (delta > 0){
   		  predictedFinalDate.add(Calendar.SECOND, delta );
		}
		return predictedFinalDate;
	}

	public String getComputerName(){
		try{
			return InetAddress.getLocalHost().getHostName();
		}
		catch(Exception e){
			return "unknown";
		}
	}


	public String getState() {

		String res = "UNKNOWN";

		switch (state){
		case S_WORKING:
		   res = "WORKING";
		   break;

		case S_FINISHED :
		   res = "FINISHED";
		   break;

		case  S_SHUTDOWN :
		   res = "SHUTDOWN";
		   break;

		case   S_COMPLETED:
		  res = "COMPLETED";
		  break;

		}
		return res;
	}

	public void setState(int state) {
		this.state = state;
	}


	//---------------------------------------------------------------------------------------
}
