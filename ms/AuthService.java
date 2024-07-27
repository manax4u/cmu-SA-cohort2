import java.sql.*;

public class AuthService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/ms_orderinfo";
    private static final String USER = "root";
    private static final String PASS = "test";
    private static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";


    public boolean authenticate(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean authenticated = false;

        try {
            Class.forName(JDBC_CONNECTOR);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "SELECT * FROM userCredential WHERE user_name=? AND password=?";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();

            if (rs.next()) {
                authenticated = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return authenticated;
    }

    public boolean register(String username, String password, String role) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean registered = false;

        try {

            Class.forName(JDBC_CONNECTOR);

            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            String query = "INSERT INTO userCredential (user_name, password, role) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            int rowsAffected = stmt.executeUpdate();

            registered = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return registered;
    }
}

