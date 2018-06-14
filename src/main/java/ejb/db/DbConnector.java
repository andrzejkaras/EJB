package ejb.db;

import org.h2.tools.Server;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@ManagedBean
public class DbConnector {

    private static final String DB_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1";
    private Connection connection;

    @PostConstruct
    public void init() {
        try {
            connection = DriverManager.getConnection(DB_URL, "sa", "sa");
            runWebServer();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void runWebServer() throws SQLException {
        Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();
    }

    public Connection getConnection() {
        return connection;
    }
}
