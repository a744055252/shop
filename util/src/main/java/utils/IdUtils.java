package utils;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class IdUtils {
    private static final ConcurrentHashMap<String, AtomicInteger> date2flow = new ConcurrentHashMap<>();

    public IdUtils() {
    }

    public static long genId() {
        String dateStr = DateUtils.format(new Date(), "yyyyMMddHHmmss");
        AtomicInteger ai = date2flow.get(dateStr);
        int autoId;
        if (ai == null) {
            synchronized(date2flow) {
                ai = date2flow.get(dateStr);
                if (ai == null) {
                    date2flow.clear();
                    ai = new AtomicInteger(0);
                    date2flow.put(dateStr, ai);
                    autoId = ai.incrementAndGet();
                } else {
                    autoId = ai.incrementAndGet();
                }
            }
        } else {
            autoId = ai.incrementAndGet();
        }

        if (autoId > 99999) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException var5) {
                var5.printStackTrace();
            }

            return genId();
        } else {
            return Long.parseLong(dateStr + autoId);
        }
    }


}
