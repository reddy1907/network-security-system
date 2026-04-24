package projectNS;

import java.util.*;

public class IntrusionDetector {

    private static Map<String, List<Long>> requestMap = new HashMap<>();

    private static final int MAX_REQUESTS = 2;   // 🔥 easier trigger
    private static final long TIME_WINDOW = 3000; // 3 sec

    public static boolean isAttack(String ip) {

        long currentTime = System.currentTimeMillis();

        requestMap.putIfAbsent(ip, new ArrayList<>());
        List<Long> requests = requestMap.get(ip);

        requests.add(currentTime);

        // remove old
        requests.removeIf(time -> (currentTime - time) > TIME_WINDOW);

        System.out.println("Requests for " + ip + ": " + requests.size());

        return requests.size() > MAX_REQUESTS;
    }
}