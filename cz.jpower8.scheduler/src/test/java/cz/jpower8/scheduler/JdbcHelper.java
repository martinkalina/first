package cz.jpower8.scheduler;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.util.Properties;

import org.apache.log4j.lf5.util.StreamUtils;

public class JdbcHelper {
	
	private Connection connection;

	public JdbcHelper() throws Exception {
		Properties props = new Properties();
		props.load(new FileInputStream("quartz-jdbc-store.properties"));
		String driverClassname = props.getProperty("org.quartz.dataSource.myDS.driver");
		String url = props.getProperty("org.quartz.dataSource.myDS.URL");
		
		Driver driver = (Driver) Class.forName(driverClassname).newInstance();
		
		Properties info = new Properties();
		info.put("user", props.getProperty("org.quartz.dataSource.myDS.user"));
		info.put("password", props.getProperty("org.quartz.dataSource.myDS.password"));
		connection = driver.connect(url, info);
	}
	
	public void createDb(){
		execute(JdbcHelper.class.getResourceAsStream("create_db.sql"));
	}
	private void execute(InputStream is) {
		try {
			String sql = new String(StreamUtils.getBytes(is));
			connection.createStatement().execute(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void dropDb(){
		execute(JdbcHelper.class.getResourceAsStream("drop_db.sql"));
	}
	
}
