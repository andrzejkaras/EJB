package ejb.front;

import ejb.Notification;
import ejb.middle.MiddleBean;
import org.apache.log4j.Logger;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

@Remote
@Stateless
@Asynchronous
public class FrontBean {

    @EJB
    private MiddleBean middleBean;

    private static Logger logger = Logger.getLogger(FrontBean.class);

    public void notify(Notification notification) {
        logger.info("Sending to MiddleBean: " + notification);
        middleBean.writeToFileSystem(notification);
    }
}
