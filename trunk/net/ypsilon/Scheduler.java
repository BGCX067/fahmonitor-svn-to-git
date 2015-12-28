package net.ypsilon;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class Scheduler {

  public static void main(String[] args) {
       ApplicationContext ctx = new FileSystemXmlApplicationContext(
		        "applicationContext.xml");
       if (ctx == null); // just to avoid of 'not used' warning ;)
	}
}
