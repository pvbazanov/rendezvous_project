package com.example.revuproject.view.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.revuproject.FragmentTypes;
import com.example.revuproject.R;
import com.example.revuproject.database.relations.PlaceCoordinates;
import com.example.revuproject.viewmodel.SharedViewModel;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.mapview.MapView;

public class MapFragment extends Fragment {
    Context context;
    Context applicationContext;
    SharedViewModel viewModel;

    private Point targetLocation = new Point(58.010207, 56.228059);
    private float zoom = 14.0f;
    private MapView mapView;
    private final static String TAG = "MapFragment";

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationContext = getActivity().getApplicationContext();
        viewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);
        viewModel.onCall(FragmentTypes.MAP);

        try{
            PlaceCoordinates c = new PlaceCoordinates(viewModel.getSelectedMeeting().getPlaceCoords());
            targetLocation = new Point(c.latitude, c.longitude);
            zoom = 18.0f;
        }catch (NullPointerException e){
            Log.w(TAG, e.toString());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mapFragment = inflater.inflate(R.layout.fragment_map, container, false);
        context = mapFragment.getContext();

        MapKitFactory.initialize(mapFragment.getContext());

        mapView = (MapView)  mapFragment.findViewById(R.id.mapview);
        mapView.getMap().move(
                new CameraPosition(targetLocation, zoom, 0.0f, 0.0f),
                new Animation(Animation.Type.SMOOTH, 1),
                null);

        mapView.getMap().getMapObjects().addPlacemark(targetLocation);

        return mapFragment;
    }

    @Override
    public void onStop() {
        // Вызов onStop нужно передавать инстансам MapView и MapKit.
        mapView.onStop();
        MapKitFactory.getInstance().onStop();

        super.onStop();
    }

    @Override
    public void onStart() {
        // Вызов onStart нужно передавать инстансам MapView и MapKit.
        super.onStart();
        MapKitFactory.getInstance().onStart();
        mapView.onStart();
    }
}