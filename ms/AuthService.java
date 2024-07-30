import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class AuthService extends UnicastRemoteObject implements AuthServiceAI {

    // Set up the JDBC driver name and database URL
    static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";
    static final String DB_URL = Configuration.getJDBCConnection();

    // Set up the orderinfo database credentials
    static final String USER = "root";
    static final String PASS = Configuration.MYSQL_PASSWORD;

    protected AuthService() throws RemoteException {
    }

    public static void main(String[] args) {
        try {
            AuthService authService = new AuthService();

            Registry registry = Configuration.createRegistry();
            registry.bind("AuthService", authService);

            String[] boundNames = registry.list();
            System.out.println("Registered services:");
            for (String name : boundNames) {
                System.out.println("\t" + name);
            }

        } catch (Exception e) {
            System.out.println("AuthService binding error: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public boolean authenticate(String username, String password) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean authenticated = false;
        try {

            System.out.println(JDBC_CONNECTOR+", "+DB_URL+", "+USER+", "+PASS);

            Class.forName(JDBC_CONNECTOR);

            //Open the connection to the orderinfo database

            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
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

