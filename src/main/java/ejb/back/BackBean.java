package ejb.back;

import com.thoughtworks.xstream.XStream;
import ejb.Configuration;
import ejb.Notification;
import ejb.db.NotificationRepository;
import org.apache.log4j.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Startup
@Singleton
public class BackBean {
    private static final XStream xstream = new XStream();
    private static Logger logger = Logger.getLogger(BackBean.class);

    @Inject
    private NotificationRepository notificationRepository;

    @Schedule(hour = "*", minute = "*", second = "*/10", persistent = false)
    public void handle() {
        List<File> files = loadFiles();
        save(files);
        removeFiles(files);
    }

    private List<File> loadFiles() {
        try {
            return Files.walk(Paths.get(Configuration.FILES_DIRECTORY))
                    .filter(Files::isRegularFile).map(Path::toFile)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private void save(List<File> files) {
        List<Notification> notifications = new ArrayList<>();

        for (File file : files) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                Notification notification = (Notification) xstream.fromXML(fileInputStream);
                notifications.add(notification);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!files.isEmpty()) {
            notificationRepository.saveAll(notifications);
            logger.info("Saved " + notifications.size() + " notifications to database");
        }
    }

    private void removeFiles(List<File> files) {
        files.forEach(File::delete);
        if (!files.isEmpty()) {
            logger.info("Deleted " + files.size() + " from file system");
        }
    }
}
