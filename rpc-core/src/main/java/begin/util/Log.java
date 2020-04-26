package begin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    private final static Logger logger = LoggerFactory.getLogger(Log.class);

    public static Logger getLogger() {
        return logger;
    }

}
