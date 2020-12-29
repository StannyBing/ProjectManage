package com.lt.zxmap.tool;

import android.util.Log;

import com.zxmap.zxmapsdk.maps.ZXMap;
import com.zxmap.zxmapsdk.style.layers.CircleLayer;
import com.zxmap.zxmapsdk.style.layers.FillExtrusionLayer;
import com.zxmap.zxmapsdk.style.layers.FillLayer;
import com.zxmap.zxmapsdk.style.layers.Filter;
import com.zxmap.zxmapsdk.style.layers.Layer;
import com.zxmap.zxmapsdk.style.layers.LineLayer;
import com.zxmap.zxmapsdk.style.layers.SymbolLayer;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Xiangb on 2018/3/6.
 * 功能：过滤器工具类
 */

public class FilterTool {


    private static FilterTool filterTool;
    private ZXMap zxMap;
    private HashMap<String, Filter.Statement> filterMap = new HashMap<>();//过滤器map
    private List<String> saveList = new ArrayList<>();//已保存过滤器的style列表

    private FilterTool() {

    }

    /**
     * 单例化
     * 禁止使用两个不同的对象进行保存
     *
     * @param zxMap
     * @return
     */
    public static FilterTool getInstance(ZXMap zxMap) {
        if (filterTool == null) {
            filterTool = new FilterTool();
        }
        filterTool.zxMap = zxMap;
        return filterTool;
    }

    /**
     * 保存默认的过滤语句
     *
     * @param styleId
     * @return
     */
    private void saveStyleFilter(String styleId) {
        try {
            Log.e("style--------------", zxMap.getLayerIdsById(styleId).size() + "");
            List<String> layers = zxMap.getLayerIdsById(styleId);
            for (int i = 0; i < layers.size(); i++) {
                String filter = zxMap.getLayer(layers.get(i)).getFilter();
                if (filter.startsWith("[") && filter.endsWith("]") && filter.length() > 2) {
                    JSONArray jsonArray = new JSONArray(filter);
                    if (jsonArray.get(0) != null && !"null".equals(jsonArray.getString(0).toString())) {
                        //将默认过滤语句进行存储
                        filterMap.put(styleId + "_" + layers.get(i), handleFilter(jsonArray.get(0).toString()));
                    } else {
                        //如果默认过滤器为空，建立一个替代的过滤器，因为过滤器不能设置为null
                        filterMap.put(styleId + "_" + layers.get(i), Filter.notHas("nullFilter"));
                    }
                }
            }
            if (layers.size() > 0) {
                saveList.add(styleId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 追加过滤语句
     *
     * @param styleId
     * @param appendFilter
     */
    public void appendStyleFilter(String styleId, Filter.Statement appendFilter) {
        //如果该style未保存过过滤器，先进行保存操作
        if (!saveList.contains(styleId)) {
            saveStyleFilter(styleId);
        }
        List<String> layers = zxMap.getLayerIdsById(styleId);
        if (layers != null) {
            for (int i = 0; i < layers.size(); i++) {
                Layer layer = zxMap.getLayer(layers.get(i));
                if (filterMap.containsKey(styleId + "_" + layers.get(i)) && layer != null) {//判断是否已保存当前layer的过滤语句
                    Filter.Statement defaultfilter = filterMap.get(styleId + "_" + layers.get(i));
                    try {
                        setFilter(layer, defaultfilter, appendFilter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
    }

    /**
     * 还原过滤语句
     *
     * @param styleId
     */
    public void recoveryStyleFilter(String styleId) {
        appendStyleFilter(styleId, null);
    }

    /**
     * 设置过滤器
     *
     * @param layer
     * @param defaultfilter
     * @param appendFilter
     */
    private void setFilter(Layer layer, Filter.Statement defaultfilter, Filter.Statement appendFilter) throws Exception {
        Filter.Statement filter;
        if (appendFilter == null) {
            filter = defaultfilter;
        } else {
            filter = Filter.all(defaultfilter, appendFilter);
        }

        if (layer instanceof LineLayer) {
            ((LineLayer) layer).setFilter(filter);
        } else if (layer instanceof FillLayer) {
            ((FillLayer) layer).setFilter(filter);
        } else if (layer instanceof FillExtrusionLayer) {
            ((FillExtrusionLayer) layer).setFilter(filter);
        } else if (layer instanceof CircleLayer) {
            ((CircleLayer) layer).setFilter(filter);
        } else if (layer instanceof SymbolLayer) {
            ((SymbolLayer) layer).setFilter(filter);
        }
    }

    /**
     * 处理过滤条件
     * ["any",["==","dm","S4"],["==","dm","S41"],["==","dm","S42"]]
     *
     * @param filterString
     * @return
     */
    private Filter.Statement handleFilter(String filterString) throws Exception {
        JSONArray jsonArray = new JSONArray(filterString);
        if (jsonArray.length() > 1) {
            if ("all".equals(jsonArray.get(0).toString())) {//并
                return Filter.all(handleCompoundFilter(jsonArray));
            } else if ("any".equals(jsonArray.get(0).toString())) {//或
                return Filter.any(handleCompoundFilter(jsonArray));
            } else if ("none".equals(jsonArray.get(0).toString())) {//否
                return Filter.none(handleCompoundFilter(jsonArray));
            } else {
                return handleSimpleFilter(jsonArray);
            }
        } else {
            return Filter.notHas("nullFilter");
        }
        //        return null;
    }

    /**
     * 处理简单filter
     * ["==","dm","S41"]
     *
     * @param jsonArray
     * @return
     */
    private Filter.Statement handleSimpleFilter(JSONArray jsonArray) throws Exception {
        if (jsonArray.length() > 1) {
            jsonArray.put(1,jsonArray.getString(1).replace("$", ""));
            if ("has".equals(jsonArray.getString(0))) {//包含
                return Filter.has(jsonArray.getString(1));
            } else if ("!has".equals(jsonArray.getString(0))) {//不包含
                return Filter.notHas(jsonArray.getString(1));
            } else if ("==".equals(jsonArray.getString(0))) {//等于
                return Filter.eq(jsonArray.getString(1), jsonArray.get(2));
            } else if ("!=".equals(jsonArray.getString(0))) {//不等于
                return Filter.neq(jsonArray.getString(1), jsonArray.get(2));
            } else if (">".equals(jsonArray.getString(0))) {//大于
                return Filter.gt(jsonArray.getString(1), jsonArray.get(2));
            } else if (">=".equals(jsonArray.getString(0))) {//大于等于
                return Filter.gte(jsonArray.getString(1), jsonArray.get(2));
            } else if ("<".equals(jsonArray.getString(0))) {//小于
                return Filter.lt(jsonArray.getString(1), jsonArray.get(2));
            } else if ("<=".equals(jsonArray.getString(0))) {//小于等于
                return Filter.lte(jsonArray.getString(1), jsonArray.get(2));
            } else if ("in".equals(jsonArray.getString(0))) {//存在于
                List<Object> objects = new ArrayList<>();
                for (int i = 2; i < jsonArray.length(); i++) {
                    objects.add(jsonArray.get(i));
                }
                return Filter.in(jsonArray.getString(1), objects.toArray());
            } else if ("!in".equals(jsonArray.getString(0))) {//不存在于
                List<Object> objects = new ArrayList<>();
                for (int i = 2; i < jsonArray.length(); i++) {
                    objects.add(jsonArray.get(i));
                }
                return Filter.notIn(jsonArray.getString(1), objects.toArray());
            }
        }
        return Filter.eq("1", "1");
    }


    /**
     * 处理复合Filter
     * ["any",[["any",["==","dm","S4"],["==","dm","S41"],["==","dm","S42"]]],["==","dm","S41"],["==","dm","S42"]]
     *
     * @param filterArray
     * @return
     */
    private Filter.Statement[] handleCompoundFilter(JSONArray filterArray) throws Exception {
        List<Filter.Statement> filterList = new ArrayList<>();
        for (int i = 1; i < filterArray.length(); i++) {
            try {
                if (countString(filterArray.getString(i), "[") > 1) {
                    filterList.add(handleFilter(filterArray.getString(i)));
                } else if ("all".equals(filterArray.getString(i))
                        || "any".equals(filterArray.getString(i))
                        || "none".equals(filterArray.getString(i))) {
                    filterList.add(handleFilter(filterArray.getString(i)));
                } else {
                    filterList.add(handleSimpleFilter(filterArray.getJSONArray(i)));
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
        if (filterList.size() == 1) {
            filterList.add(Filter.eq("1", "1"));
        }
        return filterList.toArray(new Filter.Statement[]{});
    }

    private int countString(String string, String a) {

        int counter = 0;
        for (int i = 0; i <= string.length() - a.length(); i++) {
            if (string.substring(i, i + a.length()).equals(a)) {
                counter++;
            }
        }
        return counter;
    }

}
