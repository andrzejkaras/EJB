package ejb.middle;

import com.thoughtworks.xstream.XStream;
import ejb.Configuration;
import ejb.Notification;
import org.apache.log4j.Logger;
import org.omg.CORBA.OBJECT_NOT_EXIST;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.text.SimpleDateFormat;

@Singleton
public class MiddleBean {

    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat(Configuration.DATE_FORMAT);
    private static final XStream xstream = new XStream();
    private static Logger logger = Logger.getLogger(MiddleBean.class);

    @PostConstruct
    public void init() {
        File directory = new File(Configuration.FILES_DIRECTORY);
        if (!directory.exists()) {
            if(directory.mkdir()) {
                logger.info("Created directory: " + directory.getName());
            } else {
                try {
                    throw new FileSystemException("Cannot create directory");
                } catch (FileSystemException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void writeToFileSystem(Notification notification) {
        String fileName = "ZGL_" + SIMPLE_DATE_FORMAT.format(notification.getDate()) + "_" + notification.hashCode() + ".xml";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Configuration.FILES_DIRECTORY + "/" + fileName))) {
            writer.write(xstream.toXML(notification));
            logger.info("Created file " + fileName + " in file system");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
