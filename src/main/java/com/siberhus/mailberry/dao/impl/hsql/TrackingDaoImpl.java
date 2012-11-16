package com.siberhus.mailberry.dao.impl.hsql;

import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.impl.AbstractTrackingDaoImpl;
import com.siberhus.mailberry.dao.pojo.CountingStats;

@Repository
public class TrackingDaoImpl extends AbstractTrackingDaoImpl {
	
	@Override
	public void createTable(Long campaignId) {
		String sql = "CREATE TABLE IF NOT EXISTS tracking_"+campaignId+" (" +
			"id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY," +
			"subscriber_id BIGINT NOT NULL UNIQUE," +
			"email_domain VARCHAR(128) NOT NULL,"+
			"security_token VARCHAR(8) NOT NULL,"+
			"success TINYINT," +
			"sent_time TIMESTAMP," +
			"hard_bounce TINYINT," +
			"soft_bounce TINYINT," +
			"error_message VARCHAR(256)," +
			"open_time TIMESTAMP," +
			"forwarded TINYINT," +
			"opt_out TINYINT," +
			"rsvp_status VARCHAR(3)," +
			"rsvp_time TIMESTAMP," +
			"ua_raw VARCHAR(256)," +
			"ua_type VARCHAR(64)," +
			"ua_name VARCHAR(64)," +
			"ua_version VARCHAR(8)," +
			"ua_icon VARCHAR(64)," +
			"os_name VARCHAR(64)," +
			"os_family VARCHAR(64)," +
			"os_icon VARCHAR(64)," +
			"ip_address VARCHAR(32)" +
			");";
		getNamedParameterJdbcTemplate().getJdbcOperations().execute(sql);
	}
	
	@Override
	public void dropTable(Long campaignId) {
		getNamedParameterJdbcTemplate().getJdbcOperations()
			.execute("DROP TABLE tracking_"+campaignId+" IF EXISTS");
	}
	
	/**
	 * TODO: For HSQLDB, I don't know how to combine theses SQL statements into a single one.
	 */
	@Override
	public CountingStats getCoutingStats(Long campaignId) {
		
		CountingStats countingStat = new CountingStats();
		JdbcOperations jdbcOperation = getNamedParameterJdbcTemplate().getJdbcOperations();
		String sql = null;
		
		sql = "select count(*) from tracking_"+campaignId;
		countingStat.setEmails(jdbcOperation.queryForInt(sql));
		
		sql = "select count(*) from tracking_"+campaignId+" where success = true";
		countingStat.setSuccesses(jdbcOperation.queryForInt(sql));
		
		sql = "select count(*) from tracking_"+campaignId+" where hard_bounce = true";
		countingStat.setHardBounces(jdbcOperation.queryForInt(sql));
		
		sql = "select count(*) from tracking_"+campaignId+" where soft_bounce = true";
		countingStat.setSoftBounces(jdbcOperation.queryForInt(sql));
		
		sql = "select count(*) from tracking_"+campaignId+" where open_time is not null";
		countingStat.setOpens(jdbcOperation.queryForInt(sql));
		
		sql = "select count(*) from tracking_"+campaignId+" where forwarded = true";
		countingStat.setForwards(jdbcOperation.queryForInt(sql));
		
		sql = "select count(*) from tracking_"+campaignId+" where opt_out = true";
		countingStat.setOptOuts(jdbcOperation.queryForInt(sql));
		
		sql = "select count(*) from tracking_"+campaignId+" where rsvp_status is not null";
		countingStat.setRsvps(jdbcOperation.queryForInt(sql));
		
		return countingStat;
	}
}