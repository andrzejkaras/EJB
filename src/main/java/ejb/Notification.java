package ejb;

import java.io.Serializable;
import java.util.Date;

public class Notification implements Serializable {

    private Date date;
    private String threadName;
    private String randomContent;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getRandomContent() {
        return randomContent;
    }

    public void setRandomContent(String randomContent) {
        this.randomContent = randomContent;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "date=" + date +
                ", threadName='" + threadName + '\'' +
                ", randomContent='" + randomContent + '\'' +
                '}';
    }
}
