package net.ypsilon;

//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTP;
//import org.apache.commons.net.ftp.FTPConnectionClosedException;

import org.finj.FTPClient;

import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.SocketException;

public class FTPUpload {

    static Logger logger = Logger.getLogger("net.ypsilon.FAHCheckJob");

	private String host;
	private String user;
	private String password;

	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

    boolean UploadFile(String content, String ftpFileName) throws SocketException, IOException{

    	FTPClient client = new FTPClient();
    	client.isVerbose(false);
    	client.open(host);
        client.login(user, password.toCharArray());
   	    logger.info("Connected to FTP server.");
        byte strBytes[] = content.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(strBytes);
	    client.putFile( byteArrayInputStream, ftpFileName, true );
        logger.info("File is uploaded.");
        logger.info("Disconnected from FTP.");
        client.close();

/*
    	FTPClient ftp = new FTPClient();
    	ftp.connect(host);
    	if (ftp.login(user, password)){
       	   logger.info("Connected to FTP server.");
           ftp.enterLocalPassiveMode();
           byte strBytes[] = content.getBytes();
	       ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(strBytes);
	       ftp.setFileType(FTP.BINARY_FILE_TYPE);
	       ftp.storeFile(ftpFileName, byteArrayInputStream);
           logger.info("File is uploaded.");
           try{
             ftp.logout();
    	}
        catch (FTPConnectionClosedException e)
        {
            logger.error("Server closed connection.");
        }

        }
        else
        {
          logger.error("Can not login on server.");
        }

        logger.info("Disconnected from FTP.");

        if (ftp.isConnected())
        {
           ftp.disconnect();
        }
*/
        return true;
    }
}
