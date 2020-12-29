package com.lt.zxmap.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lt.zxmap.R;
import com.zxmap.zxmapsdk.geometry.LatLng;
import com.zxmap.zxmapsdk.maps.MapView;
import com.zxmap.zxmapsdk.maps.Projection;
import com.zxmap.zxmapsdk.maps.ZXMap;


/**
 * create by 96212 on 2020/6/18.
 * Email 962123525@qq.com
 * desc
 */
public class LocationFuncView extends RelativeLayout {
    private ImageView cursorIv;
    private LatLng location;
    private CoordinateListener coordinateListener;

    public LocationFuncView(Context context) {
        this(context, null);
    }

    public LocationFuncView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LocationFuncView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_map_location, this, true);
        cursorIv = view.findViewById(R.id.cursorLocationIv);
    }

    @SuppressLint("DefaultLocale")
    public LocationFuncView bindMapView(final MapView mapView) {
        mapView.addView(this);
        final Projection projection = mapView.getZXMap().getProjection();
        cursorIv.post(new Runnable() {
            @Override
            public void run() {
                location = projection.fromScreenLocation(new PointF(cursorIv.getX() + cursorIv.getWidth() / 2, cursorIv.getY() + cursorIv.getHeight() / 2));
                coordinateResult();
            }
        });
//        location = String.format("(%.6f,%.6f)", latLng.getLatitude(), latLng.getLongitude());
        mapView.getZXMap().setOnCameraMoveListener(new ZXMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                location = projection.fromScreenLocation(new PointF(cursorIv.getX() + cursorIv.getWidth() / 2, cursorIv.getY() + cursorIv.getHeight() / 2));
//                location = String.format("(%.6f,%.6f)", latLng.getLatitude(), latLng.getLongitude());
                coordinateResult();
            }
        });
        return this;
    }

    private void coordinateResult() {
        if (coordinateListener != null) {
            coordinateListener.coordinate(location);
        }
    }

    public LocationFuncView setCoordinateListener(CoordinateListener coordinateListener) {
        this.coordinateListener = coordinateListener;
        return this;
    }

    public void removeView() {
        if (getParent() != null) {
            ((MapView) getParent()).removeView(this);
            removListener();
        }
    }

    public void removListener() {
        this.coordinateListener = null;
    }

    public interface CoordinateListener {
        void coordinate(LatLng location);
    }
}
