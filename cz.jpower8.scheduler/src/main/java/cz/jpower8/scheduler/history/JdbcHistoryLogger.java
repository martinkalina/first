package cz.jpower8.scheduler.history;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.listeners.JobListenerSupport;
import org.quartz.spi.ClassLoadHelper;
import org.quartz.spi.SchedulerPlugin;
import org.quartz.utils.DBConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs executions into DB.
 * 
 * @author Martin Kalina
 *
 */
public class JdbcHistoryLogger extends JobListenerSupport implements SchedulerPlugin {

	private static final Logger log = LoggerFactory.getLogger(JdbcHistoryLogger.class);
	
	private String name;

	private String dsName;


	@Override
	public void initialize(final String name, Scheduler scheduler, ClassLoadHelper loadHelper) throws SchedulerException {
		this.name = name;
		if (dsName != null){
			scheduler.getListenerManager().addJobListener(this);
		} else {
			log.error("No datasource specified for plugin" + getName() + ", will not log history into db");
		}
	}

	@Override
	public void start() {
	}

	@Override
	public void shutdown() {
	}
	
	public void setDataSource(String dsName){
		this.dsName = dsName;
	}


	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		Connection conn = null;
		try {
			conn = DBConnectionManager.getInstance().getConnection(dsName);
			PreparedStatement ps = conn.prepareStatement("INSERT INTO sched_task_history"
					+ "(job_name, job_group, sched_name, "
					+ "schedule_time, fire_time,"
					+ "refire_count, job_run_time, failed) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
			ps.setString(1, context.getJobDetail().getKey().getName());
			ps.setString(2, context.getJobDetail().getKey().getGroup());
			ps.setString(3, context.getScheduler().getSchedulerName());
			
			ps.setTimestamp(4, new Timestamp(context.getScheduledFireTime().getTime()));
			ps.setTimestamp(5, new Timestamp(context.getFireTime().getTime()));
			
			ps.setInt(6, context.getRefireCount());
			ps.setLong(7, context.getJobRunTime());
			ps.setBoolean(8, jobException != null);
			ps.executeUpdate();
		} catch (SQLException e) {
			log.error("Cannot log Job history", e);
		} catch (SchedulerException e) {
			log.error("Cannot log Job history", e);
		} finally {
			if (conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					log.error("Cant close connection", e);
				}
			}
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
}
