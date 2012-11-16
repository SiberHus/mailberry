package com.siberhus.mailberry.dao.impl.mysql;

import com.siberhus.mailberry.dao.impl.AbstractTrackingDaoImpl;

public class TrackingDaoImpl extends AbstractTrackingDaoImpl {
	
	@Override
	public void createTable(Long campaignId) {
		String sql = "CREATE TABLE IF NOT EXISTS tracking_"+campaignId+" (" +
			"id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
			"subscriber_id BIGINT NOT NULL," +
			"email_domain VARCHAR(128) NOT NULL,"+
			"security_token VARCHAR(8) NOT NULL,"+
			"success TINYINT," +
			"sent_time TIMESTAMP NULL DEFAULT NULL," +
			"hard_bounce TINYINT," +
			"soft_bounce TINYINT," +
			"error_message VARCHAR(256)," +
			"open_time TIMESTAMP NULL DEFAULT NULL," +
			"forwarded TINYINT," +
			"opt_out TINYINT," +
			"rsvp_status VARCHAR(3)," +
			"rsvp_time TIMESTAMP NULL DEFAULT NULL," +
			"ua_raw VARCHAR(256)," +
			"ua_type VARCHAR(64)," +
			"ua_name VARCHAR(64)," +
			"ua_version VARCHAR(8)," +
			"ua_icon VARCHAR(64)," +
			"os_name VARCHAR(64)," +
			"os_family VARCHAR(64)," +
			"os_icon VARCHAR(64)," +
			"ip_address VARCHAR(32)," +
			"UNIQUE KEY sub_id_unique_"+campaignId+"(subscriber_id)" +
			");";
		getNamedParameterJdbcTemplate().getJdbcOperations().execute(sql);
	}
	
}
