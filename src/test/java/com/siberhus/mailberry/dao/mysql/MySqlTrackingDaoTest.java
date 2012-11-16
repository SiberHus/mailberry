package com.siberhus.mailberry.dao.mysql;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Ignore;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.siberhus.mailberry.dao.AbstractTrackingDaoTest;
import com.siberhus.mailberry.dao.TrackingDao;
import com.siberhus.mailberry.dao.impl.mysql.TrackingDaoImpl;

@Ignore
public class MySqlTrackingDaoTest extends AbstractTrackingDaoTest{
	
	@Override
	public TrackingDao getTrackingDao() {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(
				"mailberry_mysql", new Properties());
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/mailberry_stats");
		dataSource.setUsername("admin");
		dataSource.setPassword("password");
		NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
		TrackingDaoImpl trackingDao = new TrackingDaoImpl();
		trackingDao.setNamedParameterJdbcTemplate(jdbcTemplate);
		trackingDao.setEntityManager(emf.createEntityManager());
		return trackingDao;
	}
	
}
