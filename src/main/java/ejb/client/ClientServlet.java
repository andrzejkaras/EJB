package ejb.client;

import ejb.Configuration;
import ejb.Notification;
import ejb.front.FrontBean;
import org.apache.log4j.Logger;

import javax.annotation.ManagedBean;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.stream.IntStream;

@ManagedBean("client")
@WebServlet("/notify")
public class ClientServlet extends HttpServlet {

    private Client client;

    @Resource
    private ManagedExecutorService executorService;

    @EJB
    private FrontBean frontBean;

    ClientServlet() {
        client = new Client();
        client.setFrontBeanRemote(frontBean);
        client.setManagedExecutorService(executorService);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        client.makeNotifications();
    }

     private class Client {

        private ManagedExecutorService managedExecutorService;

        private FrontBean frontBeanRemote;

        private Logger logger = Logger.getLogger(this.getClass());

        void makeNotifications() {
            IntStream.range(0, Configuration.AMOUNT_OF_NOTIFICATIONS)
                    .forEach(i -> executorService.execute(() -> frontBean.notify(createNotification())));
        }

        private Notification createNotification() {
            Notification notification = new Notification();

            notification.setDate(new Date());
            notification.setThreadName(Thread.currentThread().getName());
            notification.setRandomContent(UUID.randomUUID().toString());

            logger.info("Created: " + notification);
            return notification;
        }

         void setManagedExecutorService(ManagedExecutorService managedExecutorService) {
             this.managedExecutorService = managedExecutorService;
         }

         void setFrontBeanRemote(FrontBean frontBeanRemote) {
             this.frontBeanRemote = frontBeanRemote;
         }
    }
}
