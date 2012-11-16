package com.siberhus.mailberry.dao.postgresql;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.siberhus.mailberry.dao.AbstractTrackingDaoTest;
import com.siberhus.mailberry.dao.TrackingDao;
import com.siberhus.mailberry.dao.impl.postgresql.TrackingDaoImpl;

public class PostgreSqlTrackingDaoTest extends AbstractTrackingDaoTest{
	
	@Override
	public TrackingDao getTrackingDao() {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(
				"mailberry_postgresql", new Properties());
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/mailberry_stats");
		dataSource.setUsername("admin");
		dataSource.setPassword("password");
		
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		TrackingDaoImpl trackingDao = new TrackingDaoImpl();
		trackingDao.setNamedParameterJdbcTemplate(jdbcTemplate);
		trackingDao.setEntityManager(emf.createEntityManager());
		
		return trackingDao;
	}
	
}
