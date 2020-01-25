package com.alex.FamilyMap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.R;
import com.alex.FamilyMap.Fragments.MapFragment;

import Model.EventM;
import Model.PersonM;

public class EventActivity extends AppCompatActivity {
    DataCache dataCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        String eventID = getIntent().getStringExtra("EVENT_ID");
        dataCache = DataCache.getInstance();
        EventM event = dataCache.getEvent(eventID);
        PersonM person = dataCache.getPerson(event.getPersonID());
        String str = person.getFirstName() + " " + person.getLastName() +  "\n" + event.getEventType().toUpperCase()
                + ": " + event.getCity() + ", " + event.getCountry() + " (" + event.getYear() + ")";
        Bundle bundle = new Bundle();
        bundle.putString("DESCRIPTION", str);
        bundle.putBoolean("UP_MENU", true);
        bundle.putString("EVENT_ID", eventID);
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new MapFragment();
        fragment.setArguments(bundle);
        fm.beginTransaction().add(R.id.fragment_container_event, fragment).commit();
    }
}
