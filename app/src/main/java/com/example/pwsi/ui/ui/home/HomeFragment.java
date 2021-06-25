package com.example.pwsi.ui.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pwsi.BuildConfig;
import com.example.pwsi.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;


import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private double latitude;
    private double longitude;
    private String location_name;
    AutocompleteSupportFragment placeAutoComplete;
    Spinner spinnerCategories;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Get a handle to the fragment and register the callback.
        Places.initialize(requireContext(), BuildConfig.MAPS_API_KEY);
        Places.createClient(requireContext());
        placeAutoComplete = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.editTextAddIncidentLocalization);
        assert placeAutoComplete != null;
        placeAutoComplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                if (place.getLatLng() != null) {
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    location_name = place.getName();
                    // Creating a marker
                    final MarkerOptions markerOptions = new MarkerOptions();

                    markerOptions.position(place.getLatLng());

                    // Setting the title for the marker.
                    // This will be displayed on taping the marker
                    markerOptions.title(location_name);

                    // Clears the previously touched position
                    map.clear();
                    // Animating to the touched position
                    map.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 13.3f));

                    // Placing a marker on the touched position
                    map.addMarker(markerOptions);

                }
            }

            @Override
            public void onError(@NotNull Status status) {
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        List<String> categories = new ArrayList<>();
        categories.add("kategoria1");
        categories.add("Infrastruktura");
        categories.add("kategoria3");

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        spinnerCategories = view.findViewById(R.id.spinner);
        spinnerCategories.setAdapter(categoriesAdapter);

        Button sendButton = view.findViewById(R.id.sendButton);
        sendButton.setOnClickListener((e) -> {
            Bundle bundle = getEventData(view);
            sendAddCaseRequest(bundle);
        });
    }

    public Bundle getEventData(View view) {
        String nameString = ((TextView) view.findViewById(R.id.editTextAddIncidentName)).getText().toString();
        String categoryString = spinnerCategories.getSelectedItem().toString();
        String descriptionString = ((TextView) view.findViewById(R.id.editTextAddIncidentDescription)).getText().toString();

        Bundle bundle = new Bundle();
        bundle.putInt("userID", 1);
        bundle.putString("name", nameString);
//        bundle.putString("category", categoryString);
        bundle.putString("description", descriptionString);
        bundle.putString("date", new Date().toString());
//        bundle.putInt("resolved", 0);
        bundle.putDouble("longitude", longitude);
        bundle.putDouble("latitude", latitude);
        bundle.putString("location_name", location_name);
        return bundle;
    }

    public void sendAddCaseRequest(Bundle bundle) {
        int userID = bundle.getInt("userID", 1);
        String name = bundle.getString("name", "");
//        String category = bundle.getString("category", "");
        String description = bundle.getString("description", "");
        String date = bundle.getString("date", "");
        double longitude = bundle.getDouble("longitude", 0);
        double latitude = bundle.getDouble("latitude", 0);


        JSONObject caseJSON = new JSONObject();
        try {
            caseJSON.put("name", name);
//            caseJSON.put("category", category);
            caseJSON.put("description", description);
            caseJSON.put("date", date);
            caseJSON.put("longitude", longitude);
            caseJSON.put("latitude", latitude);
            caseJSON.put("location_name", location_name);
            caseJSON.put("userID", userID);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(requireContext());

        String url = BuildConfig.API_URL + "/case/new";

        // create JSON request object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, caseJSON,
                response -> {
                    Bundle bundler = new Bundle();
                    bundler.putString("response", caseJSON.toString());
                    redirectResult(bundler);
                },
                error -> {
                    Bundle bundler = new Bundle();
                    bundler.putString("response", error.toString());
                    redirectResult(bundler);
                });

        // Add the request to the RequestQueue.
        queue.add(request);

    }

    public void redirectResult(Bundle bundle) {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_home);
        navController.navigate(R.id.action_nav_home_to_resultFragment, bundle);
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onMapReady(@NotNull GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }
}