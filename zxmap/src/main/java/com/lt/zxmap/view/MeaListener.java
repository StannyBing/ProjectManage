package com.lt.zxmap.view;

import com.zxmap.zxmapsdk.geometry.LatLng;
import com.zxmap.zxmapsdk.style.layers.Layer;

import java.util.List;

/**
 * Created by Xiangb on 2019/9/27.
 * 功能：
 */
public interface MeaListener {

    void updateSource(boolean updateTotal);

    void onPointChange();

    void onMapReAdd();

    int getGeoType();

    List<LatLng> getLatlngs();

    List<List<LatLng>> getTotalList();

    int getEditLatlngIndex();

    void startTranslate();

    void stopTranslate();

    boolean isTranslate();

    MeaOptManager getOptManager();

    MeaLayerManager getLayerManager();

    void onLayerAdd(String name, Layer layer);

    void onLayerRemove(String name);
}
