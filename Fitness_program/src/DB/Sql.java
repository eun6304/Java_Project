package DB;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;


public class Sql {
	
	private final String DRV = "oracle.jdbc.driver.OracleDriver";
	private final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
	
	private java.sql.Connection conn = null;
	private PreparedStatement psmt = null;
    
    public boolean isConnected = false;
    public String query;
    
    public Sql() {
    	
    }
    
    public Sql(String user, String pw) {
    	this.connect(user, pw);
    }
    
    // 연결
    public boolean connect(String user, String pw) {
    	isConnected = false;
		try {
			Class.forName(DRV);
			
			try {
				conn = DriverManager.getConnection(URL, user, pw);
				isConnected = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return isConnected;
    }

    // 연결 해제
    public void disConnect() {
    	try {
    		if (conn != null)
    			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

    }
    
    // 
    public int executeUpdate(String sql) {
    	int result = 0; 
    	
    	if (!this.isConnected) 
    		return result;
    	
    	try {
    		psmt = conn.prepareStatement(sql);	
			result = psmt.executeUpdate();
			psmt.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    	return result;
    }
    
    public int executeUpdate() {
    	return this.executeUpdate(this.query);
    }
    
    // select문에서만 사용, x번째 컬럼 부터 y번째 컬럼 까지
    public String[][] executeQuery(String sql, int start, int end) {
    	String[][] result = new String[10000][++end - start];
    	try {
			psmt = conn.prepareStatement(sql);
			ResultSet rs = psmt.executeQuery();
			
			int row = 0;
			while (rs.next()) {
				for (int i = start, j = 0; i < end; i++, j++) {
					result[row][j] = rs.getString(i);
				}
				row++;
			}
			result = Arrays.copyOf(result, row);

			rs.close();
			psmt.close();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
 
    	return result;
    }
    
    public String[][] executeQuery(int start, int end) {
    	return this.executeQuery(this.query, start, end);
    }
}
