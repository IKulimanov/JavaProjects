import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.slf4j.LoggerFactory.getLogger;

public class AnnotationDemo {
    private static Logger log = getLogger(AnnotationDemo.class);

    private Long ID;
    @Logged(value = LogType.ERROR,
            loggedClass = AnnotationDemo.class)
    public Long getID() {

        log.info("Some message");
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

}
