package c201.analyze;

import java.util.*;

public class Histogram {
    private Map<String, Integer> histogram;

    public Histogram() {
        histogram = new HashMap<>();
    }

    public void add(String key) {
        histogram.put(key, histogram.getOrDefault(key, 0) + 1);
    }

    public void remove(String key) {
        histogram.remove(key);
    }

    public HashMap<String, Integer> sortAscending() {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(histogram.entrySet());
        list.sort(Comparator.comparing(Map.Entry::getValue));

        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for(Map.Entry<String, Integer> entry : list) {
            temp.put(entry.getKey(), entry.getValue());
        }

        return temp;
    }
}
