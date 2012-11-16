package com.siberhus.mailberry.dao.impl.postgresql;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.impl.AbstractTrackingDaoImpl;

@Repository
public class TrackingDaoImpl extends AbstractTrackingDaoImpl {
	
	@Override
	public void createTable(Long campaignId) {
		
		String sql = "select count(*) from pg_tables where tablename = 'tracking_"+campaignId+"'";
		
		int i = getNamedParameterJdbcTemplate().getJdbcOperations().queryForInt(sql);
		if(i!=0){
			log.info("Table tracking_{} 's already been created.",campaignId);
			return;
		}
		// CREATE TABLE IF NOT EXISTS is implemented in PostgreSQL 9.1
		sql = "CREATE TABLE tracking_"+campaignId+" (" +
			"id BIGSERIAL PRIMARY KEY," +
			"subscriber_id BIGINT NOT NULL UNIQUE," +
			"email_domain VARCHAR(128) NOT NULL," +
			"security_token VARCHAR(8) NOT NULL," +
			"success BOOLEAN," +
			"sent_time TIMESTAMP," +
			"hard_bounce BOOLEAN," +
			"soft_bounce BOOLEAN," +
			"error_message VARCHAR(256)," +
			"open_time TIMESTAMP," +
			"forwarded BOOLEAN," +
			"opt_out BOOLEAN," +
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
	
}
