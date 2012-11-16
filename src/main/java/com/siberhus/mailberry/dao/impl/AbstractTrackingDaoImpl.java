package com.siberhus.mailberry.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.siberhus.mailberry.dao.TrackingDao;
import com.siberhus.mailberry.dao.pojo.ClientInfoStats;
import com.siberhus.mailberry.dao.pojo.CountingStats;
import com.siberhus.mailberry.dao.pojo.OsInfo;
import com.siberhus.mailberry.dao.pojo.UaInfo;
import com.siberhus.mailberry.model.Tracking;

public abstract class AbstractTrackingDaoImpl implements TrackingDao {
	
	protected final Logger log = LoggerFactory.getLogger(getClass());
	
	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private NamedParameterJdbcTemplate statsJdbcTemplate;
	
	@Override
	public void dropTable(Long campaignId) {
		statsJdbcTemplate.getJdbcOperations()
			.execute("DROP TABLE IF EXISTS tracking_"+campaignId);
	}
	
	@Override
	public Tracking save(Long campaignId, Tracking tracking) {
		
		String sql = "SELECT COUNT(*) FROM tracking_"+campaignId+" WHERE subscriber_id=?";
		int count = statsJdbcTemplate.getJdbcOperations().queryForInt(sql, tracking.getSubscriberId());
		Map<String, Object> paramMap = getParamMap(tracking);
		if(count==0){
			sql = "INSERT INTO tracking_"+campaignId+
			"(subscriber_id, email_domain, security_token, success, sent_time," +
			"hard_bounce,soft_bounce,error_message,open_time,forwarded,opt_out,rsvp_status," +
			"rsvp_time,ua_raw,ua_type,ua_name,ua_version,ua_icon,os_name,os_family,os_icon,ip_address) " +
			"VALUES(:subscriberId,:emailDomain,:securityToken,:success,:sentTime," +
			":hardBounce,:softBounce,:errorMessage,:openTime,:forwarded,:optOut,:rsvpStatus," +
			":rsvpTime,:uaRaw,:uaType,:uaName,:uaVersion,:uaIcon,:osName,:osFamily,:osIcon,:ipAddress);";
			KeyHolder keyHolder = new GeneratedKeyHolder();
			statsJdbcTemplate.update(sql, new MapSqlParameterSource(paramMap), keyHolder);
			tracking.setId((Long)keyHolder.getKeys().get("id"));
		}else{
			sql = "UPDATE tracking_"+campaignId+
			" SET subscriber_id=:subscriberId,email_domain=:emailDomain,security_token=:securityToken," +
			"success=:success,sent_time=:sentTime,hard_bounce=:hardBounce,soft_bounce=:softBounce," +
			"error_message=:errorMessage,open_time=:openTime,forwarded=:forwarded,opt_out=:optOut,rsvp_status=:rsvpStatus," +
			"rsvp_time=:rsvpTime,ua_raw=:uaRaw,ua_type=:uaType,ua_name=:uaName,ua_version=:uaVersion,ua_icon=:uaIcon," +
			"os_name=:osName,os_family=:osFamily,os_icon=:osIcon,ip_address=:ipAddress WHERE id=:id";
			statsJdbcTemplate.update(sql, paramMap);
		}
		return tracking;
	}
	
	
	@Override
	public Tracking findBySubscriberId(Long campaignId, final Long subscriberId) {
		String sql = "SELECT * FROM tracking_"+campaignId+" WHERE subscriber_id = ?";
		Tracking tracking = null;
		try{
			tracking = statsJdbcTemplate.getJdbcOperations().queryForObject(sql, new Object[]{subscriberId}, new RowMapper<Tracking>(){
				Tracking tracking = new Tracking();
				@Override
				public Tracking mapRow(ResultSet rs, int rowNum)
						throws SQLException {
					tracking.setId(rs.getLong("id"));
					tracking.setSubscriberId(rs.getLong("subscriber_id"));
					tracking.setEmailDomain(rs.getString("email_domain"));
					tracking.setSecurityToken(rs.getString("security_token"));
					tracking.setSuccess(rs.getBoolean("success"));
					tracking.setSentTime(rs.getDate("sent_time"));
					tracking.setHardBounce(rs.getBoolean("hard_bounce"));
					tracking.setSoftBounce(rs.getBoolean("soft_bounce"));
					tracking.setErrorMessage(rs.getString("error_message"));
					tracking.setOpenTime(rs.getDate("open_time"));
					tracking.setForwarded(rs.getBoolean("forwarded"));
					tracking.setOptOut(rs.getBoolean("opt_out"));
					tracking.setRsvpStatus(rs.getString("rsvp_status"));
					tracking.setRsvpTime(rs.getDate("rsvp_time"));
					tracking.setUaRaw(rs.getString("ua_raw"));
					tracking.setUaType(rs.getString("ua_type"));
					tracking.setUaName(rs.getString("ua_name"));
					tracking.setUaVersion(rs.getString("ua_version"));
					tracking.setOsName(rs.getString("os_name"));
					tracking.setOsFamily(rs.getString("os_family"));
					tracking.setIpAddress(rs.getString("ip_address"));
					return tracking;
				}
			});
		}catch(EmptyResultDataAccessException e){
			log.warn("This error should happen in test environment only: {}", e.toString());
		}
		return tracking;
	}
	
	private Map<String, Object> getParamMap(Tracking tracking){
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("id", tracking.getId());
		params.put("subscriberId", tracking.getSubscriberId());
		params.put("emailDomain", tracking.getEmailDomain());
		params.put("securityToken", tracking.getSecurityToken());
		params.put("success", tracking.isSuccess());
		params.put("sentTime", tracking.getSentTime());
		params.put("hardBounce", tracking.isHardBounce());
		params.put("softBounce", tracking.isSoftBounce());
		params.put("errorMessage", tracking.getErrorMessage());
		params.put("openTime", tracking.getOpenTime());
		params.put("forwarded", tracking.isForwarded());
		params.put("optOut", tracking.isOptOut());
		params.put("rsvpStatus", tracking.getRsvpStatus());
		params.put("rsvpTime", tracking.getRsvpTime());
		params.put("uaRaw", tracking.getUaRaw());
		params.put("uaType", tracking.getUaType());
		params.put("uaName", tracking.getUaName());
		params.put("uaVersion", tracking.getUaVersion());
		params.put("uaIcon", tracking.getUaIcon());
		params.put("osName", tracking.getOsName());
		params.put("osFamily", tracking.getOsFamily());
		params.put("osIcon", tracking.getOsIcon());
		params.put("ipAddress", tracking.getIpAddress());
		return params;
	}

	@Override
	public void setSuccess(Long campaignId, Long subscriberId) {
		String sql = "UPDATE tracking_"+campaignId+
			" SET success=?, sent_time=? where subscriber_id=?";
		statsJdbcTemplate.getJdbcOperations().update(sql, 
				new Object[]{true, new Date(), subscriberId});
	}
	
	@Override
	public void setError(Long campaignId, Long subscriberId, String errorMessage) {
		String sql = "UPDATE tracking_"+campaignId+
			" SET success=?, hard_bounce=?, error_message=? " +
			"where subscriber_id=?";
		statsJdbcTemplate.getJdbcOperations().update(sql, 
				new Object[]{false, true, errorMessage, subscriberId});
	}
	
	@Override
	public String getSecurityToken(Long campaignId, Long subscriberId) {
		String sql = "SELECT security_token from tracking_"+campaignId
			+" WHERE subscriber_id=?";
		String token = null;
		try{
			token = statsJdbcTemplate.getJdbcOperations().queryForObject(sql, 
				new Object[]{subscriberId}, String.class);
		}catch(EmptyResultDataAccessException e){
			log.warn("This error should happen in test environment only: {}", e.toString());
		}
		return token;
	}
	
	@Override
	public void setRsvpStatus(Long campaignId, Long subscriberId,
			String rsvpStatus) {
		String sql = "UPDATE tracking_"+campaignId
			+" SET rsvp_status=?, rsvp_time=? where subscriber_id=?";
		statsJdbcTemplate.getJdbcOperations().update(sql, 
				rsvpStatus, new Date(), subscriberId);
	}
	
	
	@Override
	public void setOptOut(Long campaignId, Long subscriberId) {
		String sql = "UPDATE tracking_"+campaignId+" SET opt_out=? where subscriber_id=?";
		statsJdbcTemplate.getJdbcOperations().update(sql, true, subscriberId);
	}
	
	
	@Override
	public CountingStats getCoutingStats(Long campaignId) {
		String sql = "SELECT (SELECT count(*) FROM tracking_"+campaignId+") emails," +
				"(SELECT COUNT(*) FROM tracking_"+campaignId+" WHERE success = ?) successes," +
				"(SELECT COUNT(*) FROM tracking_"+campaignId+" WHERE hard_bounce = ?) hard_bounces," +
				"(SELECT COUNT(*) FROM tracking_"+campaignId+" WHERE soft_bounce = ?) soft_bounces," +
				"(SELECT COUNT(*) FROM tracking_"+campaignId+" WHERE open_time IS NOT NULL) opens," +
				"(SELECT COUNT(*) FROM tracking_"+campaignId+" WHERE forwarded = ?) forwards," +
				"(SELECT COUNT(*) FROM tracking_"+campaignId+" WHERE opt_out = ?) opt_outs," +
				"(SELECT COUNT(*) FROM tracking_"+campaignId+" WHERE rsvp_status IS NOT NULL) rsvps;";
		CountingStats countingStat = statsJdbcTemplate.getJdbcOperations()
			.queryForObject(sql, new RowMapper<CountingStats>(){
			@Override
			public CountingStats mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				CountingStats stat = new CountingStats();
				stat.setEmails(rs.getInt(1));
				stat.setSuccesses(rs.getInt(2));
				stat.setHardBounces(rs.getInt(3));
				stat.setSoftBounces(rs.getInt(4));
				stat.setOpens(rs.getInt(5));
				stat.setForwards(rs.getInt(6));
				stat.setOptOuts(rs.getInt(7));
				stat.setRsvps(rs.getInt(8));
				return stat;
			}
		}, new Object[]{true, true, true, true, true});
//		sql = "SELECT SUM(click_count) FROM clickstreams WHERE campaign_id=?";
//		int totalClicks = statsJdbcTemplate.getJdbcOperations().queryForInt(sql, campaignId);
		Number clickCount = ((Number)em.createQuery("select sum(clickCount) from Clickstream c where campaign.id=?")
				.setParameter(1, campaignId).getSingleResult());
		int totalClicks = 0;
		if(clickCount!=null){
			totalClicks = clickCount.intValue();
		}
		countingStat.setClicks(totalClicks);
		
		return countingStat;
	}
	
	@Override
	public ClientInfoStats getClientInfoStats(Long campaignId){
		ClientInfoStats infoStats = new ClientInfoStats();
		String sql = "SELECT ua_type, ua_name, ua_version, ua_icon, count(*) AS total FROM tracking_"+
			campaignId+" GROUP BY ua_type, ua_name, ua_version, ua_icon ORDER BY ua_type, total DESC";
		final List<UaInfo> uaInfos = new LinkedList<UaInfo>();
		statsJdbcTemplate.getJdbcOperations().query(sql, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				UaInfo uaInfo = new UaInfo();
				uaInfo.setType(rs.getString(1));
				uaInfo.setName(rs.getString(2));
				uaInfo.setVersion(rs.getString(3));
				uaInfo.setIcon(rs.getString(4));
				uaInfo.setCount(rs.getInt(5));
				uaInfos.add(uaInfo);
			}
		});
		infoStats.setUaInfos(uaInfos);
		
		sql = "SELECT os_family, os_name, os_icon, count(*) AS total FROM tracking_"+
			campaignId+" GROUP BY os_family, os_name, os_icon ORDER BY os_family, total DESC";
		final List<OsInfo> osInfos = new LinkedList<OsInfo>();
		statsJdbcTemplate.getJdbcOperations().query(sql, new RowCallbackHandler(){
			@Override
			public void processRow(ResultSet rs) throws SQLException {
				OsInfo osInfo = new OsInfo();
				osInfo.setFamily(rs.getString(1));
				osInfo.setName(rs.getString(2));
				osInfo.setIcon(rs.getString(3));
				osInfo.setCount(rs.getInt(4));
				osInfos.add(osInfo);
			}
		});
		infoStats.setOsInfos(osInfos);
		
		return infoStats;
	}
	
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
		return statsJdbcTemplate;
	}
	
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate jdbcTemplate){
		this.statsJdbcTemplate = jdbcTemplate;
	}
	
	public EntityManager getEntityManager(){
		return em;
	}
	
	public void setEntityManager(EntityManager em){
		this.em = em;
	}
}
