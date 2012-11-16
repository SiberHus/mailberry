package com.siberhus.mailberry.dao.h2;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.siberhus.mailberry.dao.AbstractTrackingDaoTest;
import com.siberhus.mailberry.dao.TrackingDao;
import com.siberhus.mailberry.dao.impl.h2.TrackingDaoImpl;

public class H2TrackingDaoTest extends AbstractTrackingDaoTest{
	
	@Override
	public TrackingDao getTrackingDao() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(
				"mailberry_h2", new Properties());
//		DriverManagerDataSource dataSource = new DriverManagerDataSource(){
		SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
		dataSource.setDriverClassName("org.h2.Driver");
		dataSource.setUrl("jdbc:h2:mem:test");
		dataSource.setUsername("sa");
		dataSource.setPassword("sa");
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		TrackingDaoImpl trackingDao = new TrackingDaoImpl();
		trackingDao.setNamedParameterJdbcTemplate(jdbcTemplate);
		trackingDao.setEntityManager(emf.createEntityManager());
//		jdbcTemplate.getJdbcOperations().execute("CREATE TABLE IF NOT EXISTS clickstreams (" +
//			"id BIGINT IDENTITY, campaign_id BIGINT NOT NULL,click_count INT)");
		return trackingDao;
	}
	
}
