package com.clinicsanddoctors.ui.main.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinicsanddoctors.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;

import java.util.Set;

/**
 * Created by Daro on 27/07/2017.
 */

public class ShopRenderer extends DefaultClusterRenderer<ClinicCluster> {

    private IconGenerator mClusterIconGenerator;
    private TextView mClusterCant;

    public ShopRenderer(Context context, GoogleMap map, ClusterManager<ClinicCluster> clusterManager) {
        super(context, map, clusterManager);
        mClusterIconGenerator = new IconGenerator(context);

        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.item_cluster, null);
        mClusterCant = (TextView) view.findViewById(R.id.mCant);
        int with = (int) context.getResources().getDimension(R.dimen.custom_cluster_size);
        int height = (int) context.getResources().getDimension(R.dimen.custom_cluster_size);
        view.setLayoutParams(new ViewGroup.LayoutParams(with, height));
        mClusterIconGenerator.setContentView(view);
        mClusterIconGenerator.setBackground(null);
    }

    @Override
    protected void onBeforeClusterItemRendered(ClinicCluster item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(item.getmIcon()));
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<ClinicCluster> cluster, MarkerOptions markerOptions) {
        mClusterCant.setText(String.valueOf(cluster.getSize()));
        Bitmap icon = mClusterIconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 1;
    }

    @Override
    protected void onClusterItemRendered(ClinicCluster clusterItem, Marker marker) {
        super.onClusterItemRendered(clusterItem, marker);
        marker.setTag(clusterItem.getClinic());
    }

    @Override
    public void onClustersChanged(Set<? extends Cluster<ClinicCluster>> clusters) {
        super.onClustersChanged(clusters);
    }
}
