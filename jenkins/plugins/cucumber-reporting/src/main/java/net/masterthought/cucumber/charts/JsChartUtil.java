package net.masterthought.cucumber.charts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class JsChartUtil {

    private static Logger logger = Logger.getLogger("net.masterthought.cucumber.charts.jschartutil");

    public List<String> orderStepsByValue(int numberTotalPassed, int numberTotalFailed, int numberTotalSkipped,
                                          int numberTotalPending, int numberTotalUndefined) {
        Map<String, Integer> map = new HashMap<String, Integer>();

        map.put("#B4D684", numberTotalPassed);
        map.put("#D68181", numberTotalFailed);
        map.put("#30ECEE", numberTotalSkipped);
        map.put("#F2C968", numberTotalPending);
        map.put("#D3D3D3", numberTotalUndefined);

        return getKeysSortedByValue(map);
    }

    public List<String> orderScenariosByValue(int numberTotalPassed, int numberTotalFailed) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("#88DD11", numberTotalPassed);
        map.put("#CC0000", numberTotalFailed);

        return getKeysSortedByValue(map);
    }

    private List<String> getKeysSortedByValue(Map<String, Integer> map) {
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });


        List<String> keys = new ArrayList<String>();
        for (Map.Entry<String, Integer> entry : list) {
            keys.add(entry.getKey());
        }
        return keys;
    }
}
