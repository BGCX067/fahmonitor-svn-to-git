package net.ypsilon;

public class ProjectDetails {

	/*
       http://fah-web.stanford.edu/psummary.html
        
	   Project Number	Server IP       Work Unit Name	    Number of Atoms	     Preferred (days)	Final deadline (days) 	Credit	Frames	Code	Description	Contact
	   1164	            171.65.103.156 	p1164_RIBO_H2O_froz	91787	             7.00	            80.00	                574.00	100	    GROMACS	Description	dlucent
	*/	
	
	private int    projectID;
	private String serverIP;
	private String workUnitName;
	private int    numberOfAtoms;
	private int    preferredDays;
	private int    deadlineDays;
	private int    credit;	
	private int    frames;
	private String code;
	
	
	ProjectDetails(int projectID, int credit){
		this.projectID = projectID;
		this.credit    = credit;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public int getDeadlineDays() {
		return deadlineDays;
	}
	public void setDeadlineDays(int deadlineDays) {
		this.deadlineDays = deadlineDays;
	}
	public int getFrames() {
		return frames;
	}
	public void setFrames(int frames) {
		this.frames = frames;
	}
	public int getNumberOfAtoms() {
		return numberOfAtoms;
	}
	public void setNumberOfAtoms(int numberOfAtoms) {
		this.numberOfAtoms = numberOfAtoms;
	}
	public int getPreferredDays() {
		return preferredDays;
	}
	public void setPreferredDays(int preferredDays) {
		this.preferredDays = preferredDays;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public String getServerIP() {
		return serverIP;
	}
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}
	public String getWorkUnitName() {
		return workUnitName;
	}
	public void setWorkUnitName(String workUnitName) {
		this.workUnitName = workUnitName;
	}
	
}



