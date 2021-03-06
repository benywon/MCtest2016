package publicMethods;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by benywon on 12/10 0010.
 */
public class SortUtils {
    /**
     * 按照第二个值来表明是升序还是降序
     * @param map
     * @param FromSmall2Large false表示从大到小 true表示从小到大
     * @return
     */
    public static Map sortByValue(Map map,final Boolean FromSmall2Large)
    {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                if (FromSmall2Large) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue())
                            .compareTo(((Map.Entry) (o2)).getValue());
                } else {
                    return ((Comparable) ((Map.Entry) (o2)).getValue())
                            .compareTo(((Map.Entry) (o1)).getValue());
                }

            }
        });
        Map result = new LinkedHashMap();

        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
    /**
     * 使用 Map按key进行排序
     * @param oriMap
     * @return
     */
    public static Map sortMapByKey(Map oriMap) {
        if (oriMap == null || oriMap.isEmpty()) {
            return null;
        }
        Map<Integer, Object> sortedMap = new TreeMap<Integer, Object>(new Comparator<Integer>() {
            public int compare(Integer key1, Integer key2) {
                return key1 - key2;
            }});
        sortedMap.putAll(oriMap);
        return sortedMap;
    }

    /**
     * @param map map
     * @param reversed true if from small to large
     * @return sorted map
     */
    public static List<String> sortByValues(Map<String,Float> map,boolean reversed)
    {
        List<String> sorted_keys=map.entrySet().stream()
                .sorted((e1, e2) -> reversed?e1.getValue().compareTo(e2.getValue()):e2.getValue().compareTo(e1.getValue())) // custom Comparator
                .map(e -> e.getKey())
                .collect(Collectors.toList());
        return sorted_keys;
    }


}
