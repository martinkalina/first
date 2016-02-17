package cz.jpower8.scheduler;

import java.io.FileInputStream;
import java.util.Properties;

import org.flywaydb.core.Flyway;

public class DbHelper {
	

	private Flyway flyway;

	public DbHelper()  {
		try {
			flyway = new Flyway();
			Properties properties = new Properties();
			FileInputStream is = new FileInputStream(("flyway.properties"));
			properties.load(is);
			flyway.configure(properties);
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void createDb(){
		flyway.migrate();
	}

	public void dropDb(){
		flyway.clean();
	}
	
}
