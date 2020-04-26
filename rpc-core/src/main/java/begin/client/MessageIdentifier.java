package begin.client;

import java.util.concurrent.atomic.AtomicInteger;

public class MessageIdentifier {

    private final static AtomicInteger autoIncrementId = new AtomicInteger(100000);

    public static int create() {
        return autoIncrementId.incrementAndGet();
    }
}
