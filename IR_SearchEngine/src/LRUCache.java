import java.util.HashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Iterator;

public class LRUCache {

    private static int capacity = 5;
    private static LinkedHashMap<String,List<String>> map = new LinkedHashMap<String,List<String>>();

    public LRUCache() {
     //   this.capacity = capacity;
       // this.map = new LinkedHashMap<>();
    }

    public static List<String> get(String key) {
    	List<String> value = map.get(key);
        if (value == null) {
            value = null;
        } else {
            set(key, value);
        }
        return value;
    }

    public static void set(String key, List<String> value) {
        if (map.containsKey(key)) {
               map.remove(key);
        } else if (map.size() == capacity) {
            Iterator<String> it = map.keySet().iterator();
            it.next();
            it.remove();
        }
        map.put(key, value);
    }
}