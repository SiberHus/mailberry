import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Types;


public class Test {
	
	/**
	 * Oracle count(*) = NUMERIC
	 * Postgresql count(*) = BIGINT
	 * MySQL count(*) = BIGINT
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
//		Class.forName("org.postgresql.Driver");
//		String connStr = "jdbc:postgresql://localhost:5432/mailberry";
		String username = "homemart";
		String password = "homemart1w2q3r4e";
		Class.forName("oracle.jdbc.OracleDriver");
		String connStr = "jdbc:oracle:thin:@localhost:1521:orcl";
		Connection conn = DriverManager.getConnection(connStr, username, password);
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("select id from homemart.gsk_users");
		ResultSetMetaData rsmd = rs.getMetaData();
		rs.next();
		System.out.println(">"+rsmd.getColumnType(1));
		System.out.println(rs.getString(1));
	}
}
