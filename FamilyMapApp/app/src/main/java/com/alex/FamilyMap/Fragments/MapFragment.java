package com.alex.FamilyMap.Fragments;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.Activities.MainActivity;
import com.alex.FamilyMap.Activities.PersonActivity;
import com.alex.FamilyMap.Activities.SearchActivity;
import com.alex.FamilyMap.Activities.SettingsActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.alex.FamilyMap.R;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Model.EventM;
import Model.PersonM;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    private boolean hasUpMenu = false;
    private DataCache dataCache;
    private EventM currEvent;
    private Map<Marker, EventM> markerEventMap;
    private List<Polyline> polyLines;
    TextView description;
    ImageView icon;
    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        Iconify.with(new FontAwesomeModule());
        dataCache = DataCache.getInstance();
        markerEventMap = new HashMap<>();
        polyLines = new ArrayList<>();
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);
        icon = view.findViewById(R.id.image_view_map);
        description = view.findViewById(R.id.text_view_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setHasOptionsMenu(true);
        checkIfBundle();
        description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PersonActivity.class);
                intent.putExtra("PERSON_ID", currEvent.getPersonID());
                startActivity(intent);
            }
        });
        return view;
    }
    private void checkIfBundle(){
        Bundle bundle = this.getArguments();
        String str = null;
        PersonM selectedPerson = null;
        if (bundle != null) {
            str = bundle.getString("DESCRIPTION");
            hasUpMenu = bundle.getBoolean("UP_MENU");
            currEvent = dataCache.getEvent(bundle.getString("EVENT_ID"));
        }
        if(str != null) {
            if (currEvent != null) {
                selectedPerson = dataCache.getPerson(currEvent.getPersonID());
                if (selectedPerson.getGender().equals("m")) {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.colorBlue);
                    icon.setImageDrawable(genderIcon);
                }
                else {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.colorPink);
                    icon.setImageDrawable(genderIcon);
                }
                description.setText(str);
            }
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (hasUpMenu) {
            inflater.inflate(R.menu.menu_up_button, menu);
            MenuItem upButton = menu.findItem(R.id.up_button);
            upButton.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if(item.getItemId() == R.id.up_button) {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                    return true;
                }
            });
        }
        else {
            inflater.inflate(R.menu.menu_map, menu);
            MenuItem searchItem = menu.findItem(R.id.search_button);
            MenuItem settingItem = menu.findItem(R.id.settings_button);

            searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
            settingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLoadedCallback(this);
        addMarkerToMap();
        if (currEvent != null) {
            LatLng place = new LatLng(currEvent.getLatitude(), currEvent.getLongitude());
            map.animateCamera(CameraUpdateFactory.newLatLng(place));
        }
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final EventM selectedEvent = markerEventMap.get(marker);
                PersonM selectedPerson = dataCache.getPerson(selectedEvent.getPersonID());
                for(Polyline line : polyLines) {
                    line.remove();
                }
                addLinesToMap(selectedEvent, selectedPerson);
                if (selectedPerson.getGender().equals("m")) {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.colorBlue);
                    icon.setImageDrawable(genderIcon);
                }
                else {
                    Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.colorPink);
                    icon.setImageDrawable(genderIcon);
                }
                String str = selectedPerson.getFirstName() + " " + selectedPerson.getLastName() +  "\n"
                             + selectedEvent.getEventType().toUpperCase() + ": " + selectedEvent.getCity()
                             + ", " + selectedEvent.getCountry() + " (" + selectedEvent.getYear() + ")";
                description.setText(str);
                description.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), PersonActivity.class);
                        intent.putExtra("PERSON_ID", selectedEvent.getPersonID());
                        startActivity(intent);
                    }
                });
                map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                return true;
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(map != null){
            map.clear();
            onMapReady(map);
        }
    }

    private void addLinesToMap(EventM event, PersonM person) {
        PersonM spouse = dataCache.getSpouse(person);
        if(spouse != null) {
            List<EventM> spouseEvents = dataCache.getAllEventsOfPerson(spouse);
            if (dataCache.isSpouse_lines()) {
                if (!spouseEvents.isEmpty()) {
                    EventM spouseEvent = spouseEvents.get(0);
                    PolylineOptions polylineOptions = new PolylineOptions().add(new LatLng(event.getLatitude(), event.getLongitude()),
                            new LatLng(spouseEvent.getLatitude(), spouseEvent.getLongitude())).width(12).color(Color.RED);
                    polyLines.add(map.addPolyline(polylineOptions));
                }
            }
            if (dataCache.isFamily_tree_lines()) {
                drawFamilyLines(person, event, 20);
            }
            if (dataCache.isLife_story_toggle()) {
                List<EventM> otherEventList = dataCache.getAllEventsOfPerson(person);
                for (int i = 0; i < otherEventList.size() - 1; i++) {
                    PolylineOptions polylineOptions = new PolylineOptions().add(new LatLng(otherEventList.get(i).getLatitude(),
                            otherEventList.get(i).getLongitude()), new LatLng(otherEventList.get(i + 1).getLatitude(),
                            otherEventList.get(i + 1).getLongitude())).width(12).color(Color.BLUE);
                    polyLines.add(map.addPolyline(polylineOptions));
                }
            }
        }
    }

    private void drawFamilyLines(PersonM person, EventM event, int width) {
        width = Math.round((float) (width) * 3/4);
        if (dataCache.getFather(person) == null || dataCache.getMother(person) == null) {
            return;
        }
        PersonM father = dataCache.getFather(person);
        PersonM mother = dataCache.getMother(person);
        List<EventM> fatherList = dataCache.getAllEventsOfPerson(father);
        if (!fatherList.isEmpty()) {
            EventM fatherEvent = fatherList.get(0);
            PolylineOptions polylineOptions = new PolylineOptions().add(new LatLng(event.getLatitude(), event.getLongitude()),
                    new LatLng(fatherEvent.getLatitude(), fatherEvent.getLongitude())).width(width).color(Color.GRAY);
            polyLines.add(map.addPolyline(polylineOptions));
        }
        List<EventM> motherList = dataCache.getAllEventsOfPerson(mother);
        if (!motherList.isEmpty()) {
            EventM motherEvent = motherList.get(0);
            PolylineOptions polylineOptions = new PolylineOptions().add(new LatLng(event.getLatitude(), event.getLongitude()),
                    new LatLng(motherEvent.getLatitude(), motherEvent.getLongitude())).width(width).color(Color.GRAY);
            polyLines.add(map.addPolyline(polylineOptions));
        }
        if (!fatherList.isEmpty()) {
            drawFamilyLines(father, fatherList.get(0), width);
        }
        if (!motherList.isEmpty()) {
            drawFamilyLines(mother, motherList.get(0), width);
        }
    }

    private void addMarkerToMap() {
        for (EventM event : dataCache.getEventList()) {
            LatLng place = new LatLng(event.getLatitude(), event.getLongitude());
            String str = event.getEventType();
            int hue = getHueFromString(str);
            Marker marker = map.addMarker(new MarkerOptions().position(place).title(event.getCity()).icon(BitmapDescriptorFactory.defaultMarker(hue)));
            markerEventMap.put(marker, event);
        }
    }

    private int getHueFromString(String str) {
        int hash = str.toLowerCase().hashCode();
        int r = (hash & 0xFF0000) >> 16;
        int g = (hash & 0x00FF00) >> 8;
        int b = hash & 0x0000FF;
        return getHue(r, g, b);
    }

    public int getHue(int red, int green, int blue) {
        float min = Math.min(Math.min(red, green), blue);
        float max = Math.max(Math.max(red, green), blue);
        if (min == max) {
            return 0;
        }
        float hue;
        if (max == red) {
            hue = (green - blue) / (max - min);
        } else if (max == green) {
            hue = 2f + (blue - red) / (max - min);
        } else {
            hue = 4f + (red - green) / (max - min);
        }
        hue = hue * 60;
        if (hue < 0) hue = hue + 360;
        return Math.round(hue);
    }

    @Override
    public void onMapLoaded() {}
}