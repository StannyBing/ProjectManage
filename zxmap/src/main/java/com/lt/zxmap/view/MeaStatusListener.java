package com.lt.zxmap.view;

import com.zxmap.zxmapsdk.style.layers.Layer;

/**
 * Created by Xiangb on 2019/9/27.
 * 功能：
 */
public interface MeaStatusListener {
    void onMapReAdd();

    void onPointChange();

    void onLayerAdd(String id, Layer layer);

    void onLayerRemove(String id);
}
