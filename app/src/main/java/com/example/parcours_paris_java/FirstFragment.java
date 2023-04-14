package com.example.parcours_paris_java;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.parcours_paris_java.databinding.FragmentFirstBinding;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.IconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstFragment extends Fragment {
    MapView map = null;
    TextView tv = null;

    Parcours parcours;
    private FragmentFirstBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Context ctx = getActivity();
        Map<String, String> hmap = new HashMap<String, String>();

        try {
            File_extracter file_extracter = new File_extracter(requireActivity().getAssets(), "test.txt");
            hmap = file_extracter.extract_name();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        List<Questions> questionsList = extractQuestions(hmap);

        GeoPoint location = new GeoPoint(Double.parseDouble(hmap.get("G1")),Double.parseDouble(hmap.get("G2")));
        parcours = new Parcours(hmap.get("No"), hmap.get("Ds"), location, Integer.parseInt(hmap.get("tp")) , questionsList );
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
        items.add(new OverlayItem(parcours.getName(), parcours.getDescription(), parcours.getLocation()));

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        map = binding.map;
        tv = binding.textViewId;
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
        new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                tv.setText(parcours.getQuestionsList().get(0).getQuestion());
                return true;
            }

            @Override
            public boolean onItemLongPress(final int index, final OverlayItem item) {
                //NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
                tv.setText(parcours.getQuestionsList().get(0).getAnswer());
                return false;
            }
        }, ctx);
        mOverlay.setFocusItemsOnTap(true);

        map.getOverlays().add(mOverlay);

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
                String segments[] = hmap.get(key).split("ANS :");
                Questions questions = new Questions(segments[0], segments[1]);
                questionsList.add(questions);
            }
        }

        return  questionsList;

    }

}