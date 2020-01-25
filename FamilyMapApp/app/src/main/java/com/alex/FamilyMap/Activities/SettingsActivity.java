package com.alex.FamilyMap.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.Data.Filter;
import com.alex.FamilyMap.R;

import java.util.ArrayList;

import Model.EventM;

public class SettingsActivity extends AppCompatActivity {
    DataCache dataCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dataCache = DataCache.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Switch spouseLines = findViewById(R.id.spouse_toggle);
        spouseLines.setChecked(dataCache.isSpouse_lines());
        spouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setSpouse_lines(true);
                }
                else {
                    dataCache.setSpouse_lines(false);
                }
            }
        });
        Switch lifeStoryLines = findViewById(R.id.life_story_toggle);
        lifeStoryLines.setChecked(dataCache.isLife_story_toggle());
        lifeStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setLife_story_toggle(true);
                }
                else {
                    dataCache.setLife_story_toggle(false);
                }
            }
        });
        Switch familyTreeLines = findViewById(R.id.family_tree_toggle);
        familyTreeLines.setChecked(dataCache.isFamily_tree_lines());
        familyTreeLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dataCache.setFamily_tree_lines(true);
                }
                else {
                    dataCache.setFamily_tree_lines(false);
                }
            }
        });
        Switch fatherSwitch = findViewById(R.id.fathers_side_toggle);
        fatherSwitch.setChecked(dataCache.isFather_events());
        fatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Filter filter = new Filter();
                ArrayList<EventM> fatherEventList = filter.getFatherSide();
                if (isChecked) {
                    dataCache.setFather_events(true);
                    dataCache.addEventList(fatherEventList);
                }
                else {
                    dataCache.setFather_events(false);
                    dataCache.removeFromEventList(fatherEventList);
                }
            }
        });
        Switch motherSwitch = findViewById(R.id.mothers_side_toggle);
        motherSwitch.setChecked(dataCache.isMother_events());
        motherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Filter filter = new Filter();
                ArrayList<EventM> motherEventList = filter.getMotherSide();
                if (isChecked) {
                    dataCache.setMother_events(true);
                    dataCache.addEventList(motherEventList);
                }
                else {
                    dataCache.setMother_events(false);
                    dataCache.removeFromEventList(motherEventList);
                }
            }
        });
        Switch maleSwitch = findViewById(R.id.male_events_toggle);
        maleSwitch.setChecked(dataCache.isMale_events());
        maleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Filter filter = new Filter();
                ArrayList<EventM> maleEventList = filter.getMaleEvents();
                if (isChecked) {
                    dataCache.setMale_events(true);
                    dataCache.addEventList(maleEventList);
                }
                else {
                    dataCache.setMale_events(false);
                    dataCache.removeFromEventList(maleEventList);
                }
            }
        });
        Switch femaleSwitch = findViewById((R.id.female_events_toggle));
        femaleSwitch.setChecked(dataCache.isFemale_events());
        femaleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Filter filter = new Filter();
                ArrayList<EventM> femaleEventList = filter.getFemaleEvents();
                if(isChecked) {
                    dataCache.setFemale_events(true);
                    dataCache.addEventList(femaleEventList);
                }
                else {
                    dataCache.setFemale_events(false);
                    dataCache.removeFromEventList(femaleEventList);
                }
            }
        });
        View view = findViewById(R.id.logout_button);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCache.clearDatabase();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_up_button, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.up_button) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return true;
    }
}
