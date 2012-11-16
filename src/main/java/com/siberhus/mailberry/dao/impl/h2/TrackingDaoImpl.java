package com.siberhus.mailberry.dao.impl.h2;

import org.springframework.stereotype.Repository;

import com.siberhus.mailberry.dao.impl.AbstractTrackingDaoImpl;

@Repository
public class TrackingDaoImpl extends AbstractTrackingDaoImpl {
	
	@Override
	public void createTable(Long campaignId) {
		String sql = "CREATE TABLE IF NOT EXISTS tracking_"+campaignId+" (" +
		"id BIGINT IDENTITY," +
		"subscriber_id BIGINT NOT NULL," +
		"email_domain VARCHAR(128) NOT NULL,"+
		"security_token VARCHAR(8) NOT NULL,"+
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
		sql += "ALTER TABLE tracking_"+campaignId+" ADD CONSTRAINT IF NOT EXISTS sub_id_unique_"
			+campaignId+" UNIQUE(subscriber_id);";
		getNamedParameterJdbcTemplate().getJdbcOperations().execute(sql);
	}
	
}
