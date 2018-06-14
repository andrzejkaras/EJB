package ejb.db;

import ejb.Notification;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@ManagedBean
public class NotificationRepository {

    private static final String INSERT_QUERY = "INSERT INTO NOTIFICATION VALUES (null, ?, ?, ?)";
    private static final String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS PUBLIC.NOTIFICATION\n" +
            "(id INT auto_increment,\n" +
            "creation_date DATE,\n" +
            "thread_name VARCHAR(255),\n" +
            "random_content VARCHAR(255),\n" +
            "primary key(id));";

    @Inject
    private DbConnector dbConnection;

    @PostConstruct
    public void init() {
        Connection connection = dbConnection.getConnection();
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CREATE_QUERY);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAll(List<Notification> notifications) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY)) {
            for (Notification notification : notifications) {
                preparedStatement.setDate(1, new java.sql.Date(notification.getDate().getTime()));
                preparedStatement.setString(2, notification.getThreadName());
                preparedStatement.setString(3, notification.getRandomContent());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
