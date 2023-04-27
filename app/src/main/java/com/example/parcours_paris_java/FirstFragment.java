package com.example.parcours_paris_java;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.parcours_paris_java.databinding.FragmentFirstBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstFragment extends Fragment {
    MapView map = null;
    TextView tv = null;

    Parcours parcours;
    private FragmentFirstBinding binding;

    private FirstFragmentListener listener;

    private LocationManager mMyLocationOverlay;
    private LocationManager locationManager;
    private LocationListener locationListener;
    protected boolean gps_enabled, network_enabled;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Context ctx = getActivity();
        Map<String, String> hmap = new HashMap<String, String>();
        AssetManager assetManager = ctx.getAssets();
        String[] files = new String[0];
        ArrayList<Parcours> parcoursArrayList = new ArrayList<Parcours>();
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        try {
            files = assetManager.list("parcours");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /* Set start parcour Icon */
        Drawable start = ResourcesCompat.getDrawable(getResources(), R.drawable.parcours_start_flag, null);
        Bitmap bitmap = ((BitmapDrawable)start).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
        Drawable start_flag = new BitmapDrawable(getResources(), resizedBitmap);

        /* Set end parcour Icon */
        Drawable end = ResourcesCompat.getDrawable(getResources(), R.drawable.parcours_end_flag, null);
        Bitmap bitmap_end = ((BitmapDrawable)end).getBitmap();
        Bitmap resizedBitmap_end = Bitmap.createScaledBitmap(bitmap_end, 80, 80, false);
        Drawable end_flag = new BitmapDrawable(getResources(), resizedBitmap_end);

        Drawable draw = ResourcesCompat.getDrawable(getResources(), R.drawable.blue_pos, null);
        Bitmap bitmap_pos = ((BitmapDrawable)draw).getBitmap();
        Bitmap resizedBitmap_pos = Bitmap.createScaledBitmap(bitmap_pos, 30, 30, false);
        Drawable rdraw = new BitmapDrawable(getResources(), resizedBitmap_pos);


        for (int i = 0; i < files.length; i++) {
            try {
                File_extracter file_extracter = new File_extracter(requireActivity().getAssets(), "parcours/" + files[i]);
                hmap = file_extracter.extract_name();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            List<Questions> questionsList = extractQuestions(hmap);

            GeoPoint location = new GeoPoint(Double.parseDouble(hmap.get("G1")), Double.parseDouble(hmap.get("G2")));
            GeoPoint arrival = new GeoPoint(Double.parseDouble(hmap.get("A1")), Double.parseDouble(hmap.get("A2")));
            parcours = new Parcours(hmap.get("No"), hmap.get("Ds"), location, Integer.parseInt(hmap.get("tp")), questionsList, arrival);
            parcoursArrayList.add(parcours);
            Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

            OverlayItem flag = new OverlayItem(parcours.getName(), parcours.getDescription(), parcours.getLocation());
            flag.setMarker(start_flag);

            items.add(flag);

        }


        binding = FragmentFirstBinding.inflate(inflater, container, false);
        tv = binding.textViewId;
        map = binding.map;
        Marker positionMarker = new Marker(map);
        positionMarker.setIcon(rdraw);
        Marker startMarker = new Marker(map);
        startMarker.setIcon(end_flag);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Log.d("ici", "else");
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    GeoPoint position = new GeoPoint(location.getLatitude(), location.getLongitude());
                    positionMarker.setPosition(position);
                    positionMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    map.getOverlays().add(positionMarker);


                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}

                @Override
                public void onProviderEnabled(String provider) {}

                @Override
                public void onProviderDisabled(String provider) {}
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }




        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
        new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {

                try {
                    map.getOverlays().remove(startMarker);
                } catch (Exception e) {

                }
                tv.setText(parcoursArrayList.get(index).getQuestionsList().get(0).getQuestion());


                startMarker.setPosition(parcoursArrayList.get(index).getArrival());
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                map.getOverlays().add(startMarker);
                tv.setText("L'arrivée du parcours vient d'apparaitre ! ");
                binding.go.setVisibility(getView().VISIBLE);
                binding.go.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MainActivity main = (MainActivity) getActivity();
                        main.changeFragment(parcoursArrayList.get(index));
                    }
                });

                return  true;
            }

            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {


                return false;
            }
        }, ctx);

        mOverlay.setFocusItemsOnTap(true);


        map.getOverlays().add(0,mOverlay);



        IMapController mapController = map.getController();

        map.setHorizontalMapRepetitionEnabled(true);
        map.setVerticalMapRepetitionEnabled(false);
        map.setScrollableAreaLimitLatitude(MapView.getTileSystem().getMaxLatitude(), MapView.getTileSystem().getMinLatitude(), 0);
        mapController.setZoom(13.7);
        GeoPoint startPoint = new GeoPoint(48.855370, 2.346897);
        mapController.setCenter(startPoint);
        map.setTileSource(TileSourceFactory.MAPNIK);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public List<Questions> extractQuestions(Map<String, String> hmap){
        List<Questions> questionsList = new ArrayList<>();
        for (String key : hmap.keySet()) {
            if (key.matches("Q.")){
                String segments[] = hmap.get(key).split("ANS : ");
                Questions questions = new Questions(segments[0], segments[1]);
                questionsList.add(questions);
            }
        }

        return  questionsList;

    }

    public interface FirstFragmentListener {
        void onObjectSelected(Parcours object);
    }

    public void onObjectSelected(Parcours object) {
        listener.onObjectSelected(object);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FirstFragmentListener) {
            listener = (FirstFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentAListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    @Override
    public void onPause() {
        super.onPause();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (locationManager != null && locationListener != null) {
                locationManager.removeUpdates(locationListener);
            }
        }
    }



}

