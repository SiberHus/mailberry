package com.siberhus.mailberry.ui.grid.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.siberhus.mailberry.ui.grid.GridDataQuery;


public abstract class JdbcGridDataHandler extends AbstractSqlGridDataHandler<Map<String, Object>> {
	
	private DataSource dataSource;
	
	private Database database;
	
	public static enum Database{
		H2, HSQL, MYSQL, POSTGRESQL, ORACLE, SQLSERVER 
	}
	public static class SqlRuntimeException extends RuntimeException{
		private static final long serialVersionUID = 1L;

		public SqlRuntimeException() {
			super();
		}
		public SqlRuntimeException(String message, Throwable cause) {
			super(message, cause);
		}
		public SqlRuntimeException(String message) {
			super(message);
		}
		public SqlRuntimeException(Throwable cause) {
			super(cause);
		}
	}
	
	public JdbcGridDataHandler(){}
	
	public JdbcGridDataHandler(DataSource dataSource, Database database){
		this.dataSource = dataSource;
		this.database  = database;
	}
	
	@Override
	public void addQueryOrder(GridDataQuery query, String sidx, String sord) {
		if(database!=Database.SQLSERVER){
			if(query.getAlias()!=null){
				query.appendQueryString(" ORDER BY "+query.getAlias()+"."+sidx+" "+sord);
			}else{
				query.appendQueryString(" ORDER BY "+sidx+" "+sord);
			}
		}else{
			query.addOrder(sidx+" "+sord);
		}
	}
	
	@Override
	public long countResult(GridDataQuery gridDataQuery) {
		Number count = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			ps = conn.prepareStatement("SELECT COUNT(*) FROM ("+gridDataQuery.getQueryString()+
					") mailberry;");
			for(int i=0;i<gridDataQuery.getParameters().size();i++){
				ps.setObject(i+1, gridDataQuery.getParameters().get(i));
			}
			rs = ps.executeQuery();
			if(rs.next())
				return rs.getLong(1);
		} catch (SQLException e) {
			throw new SqlRuntimeException(e);
		} finally{
			closeQuietly(conn, ps, rs);
		}
		return count.longValue();
	}
	
	@Override
	public List<Map<String, Object>> getResultList(GridDataQuery gridDataQuery, int offset, int limit) {
		
		List<Map<String, Object>> resultList = new LinkedList<Map<String,Object>>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			String sql = gridDataQuery.getQueryString();
			switch(database){
			case H2:
			case HSQL:
			case POSTGRESQL:
			case MYSQL:
				sql += " LIMIT "+limit+" OFFSET "+offset;
				break;
			case ORACLE:
				sql = "SELECT b.* FROM (SELECT rownum mynum , a.* FROM ( "+sql+
				") a WHERE rownum <= "+(offset+limit)+" ) b WHERE mynum >="+offset;
				break;
			case SQLSERVER:
				int i = sql.toUpperCase().indexOf(" FROM ");
				sql = new StringBuilder(sql).insert(i," , ROW_NUMBER() OVER (ORDER BY "
						+gridDataQuery.getOrdersString()+") AS RowNum").toString();
				sql = "SELECT * FROM ("+sql+") AS MyDerivedTable WHERE MyDerivedTable.RowNum BETWEEN "
					+offset+" AND "+(offset+limit);
				break;
			}
			ps = conn.prepareStatement(sql);
			for(int i=0;i<gridDataQuery.getParameters().size();i++){
				ps.setObject(i+1, gridDataQuery.getParameters().get(i));
			}
			rs = ps.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			while(rs.next()){
				Map<String, Object> result = new LinkedHashMap<String, Object>();
				for(int i=1;i<=colCount;i++){
					result.put(rsmd.getColumnName(i), rs.getObject(i));
				}
				resultList.add(result);
			}
		} catch (SQLException e) {
			throw new SqlRuntimeException(e);
		} finally{
			closeQuietly(conn, ps, rs);
		}
		return resultList;
	}
	
	private static void closeQuietly(Connection conn, Statement stmt, ResultSet rs){
		if(rs!=null){
			try{ rs.close(); }catch(Exception e){}
		}
		if(stmt!=null){
			try{ stmt.close(); }catch(Exception e){}
		}
		if(conn!=null){
			try{ conn.close(); }catch(Exception e){}
		}
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}
	
}
