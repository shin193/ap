package scraper.EX8;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class ObjectCounter<E> {
    private Map<E, Integer> counterMap = new HashMap<>();

    public void add(E item) {
        counterMap.put(item, counterMap.getOrDefault(item, 0) + 1);
    }

    public List<Map.Entry<E, Integer>> getTop(int k) {
        return counterMap.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(k)
                .collect(Collectors.toList());
    }
}
