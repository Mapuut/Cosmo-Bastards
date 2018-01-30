package udp.network.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * unique identifier for every client.
 */
public final class UniqueIdentifier {

    /**
     * list of IDs.
     */
    private static List<Integer> ids = new ArrayList<>();

    /**
     * size of ID pool.
     */
    private static final int RANGE = 10000;

    /**
     * current index.
     */
    private static int index = 0;

    private static HashMap<Integer, String> nameByIdMap = new HashMap<>();

    // initializition of static variables (similar to a constructor)
    static {
        for (int i = 0; i < RANGE; i++) {
            ids.add(i);
        }
        Collections.shuffle(ids);
    }

    /**
     * get a random ID.
     *
     * @return id
     */
    public static int getIdentifier() {
        // reset index if goes over pool sizz
        if (index > ids.size() - 1) index = 0;
        return ids.get(index++);
    }

    public static void setNameAndIdOfClient(int id, String name) {
        nameByIdMap.put(id, name);
    }

    public static String getNameById(int id) {
        return nameByIdMap.get(id);
    }
}
