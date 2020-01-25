package com.alex.FamilyMap.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import Model.EventM;
import Model.PersonM;

public class PersonActivity extends AppCompatActivity {
    PersonM currPerson;
    ExpandableListView expandableListView;
    List<EventM> eventList;
    List<PersonM> personList;
    DataCache dataCache = DataCache.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        expandableListView = findViewById(R.id.expandable_list_view);
        eventList = new ArrayList<>();
        personList = new ArrayList<>();
        initData();
        TextView firstName = findViewById(R.id.actual_first_name);
        firstName.setText(currPerson.getFirstName());
        TextView lastName = findViewById(R.id.actual_last_name);
        lastName.setText(currPerson.getLastName());
        TextView gender = findViewById(R.id.actual_gender);
        if (currPerson.getGender().equals("m")) {
            gender.setText(R.string.male);
        }
        else {
            gender.setText(R.string.female);
        }
        expandableListView.setAdapter(new ExpandableListAdapter(eventList, personList));

    }

    private void initData() {
        String personID = getIntent().getStringExtra("PERSON_ID");
        currPerson = dataCache.getPerson(personID);
        eventList = dataCache.getAllEventsOfPerson(currPerson);
        personList = dataCache.getAllPersonOfPerson(currPerson);

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

    private class ExpandableListAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PERSON_GROUP_POSITION = 1;

        private final List<EventM> eventList;
        private final List<PersonM> personList;

        ExpandableListAdapter(List<EventM> eventList, List<PersonM> personList) {
            this.eventList = eventList;
            this.personList = personList;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return eventList.size();
                case PERSON_GROUP_POSITION:
                    return personList.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return getString(R.string.life_events);
                case PERSON_GROUP_POSITION:
                    return getString(R.string.family);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return eventList.get(childPosition);
                case PERSON_GROUP_POSITION:
                    return personList.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.life_events);
                    break;
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.family);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.search_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.search_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializeEventView(View view, final int childPosition) {
            ImageView icon = view.findViewById(R.id.image_view);
            int hash = eventList.get(childPosition).getEventType().hashCode();
            int r = (hash & 0xFF0000) >> 16;
            int g = (hash & 0x00FF00) >> 8;
            int b = hash & 0x0000FF;
            int rgb = r;
            rgb = (rgb << 8) + g;
            rgb = (rgb << 8) + b;
            Drawable markerIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_map_marker).color(rgb);
            icon.setImageDrawable(markerIcon);
            TextView description = view.findViewById(R.id.text_view);
            String descriptionStr = eventList.get(childPosition).getEventType().toUpperCase() + ": " + eventList.get(childPosition).getCity()
                    + ", " + eventList.get(childPosition).getCountry() + " (" + eventList.get(childPosition).getYear() + ")";
            description.setText(descriptionStr);
            TextView name = view.findViewById(R.id.text_view2);
            String nameStr = currPerson.getFirstName()+ " " + currPerson.getLastName();
            name.setText(nameStr);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                    intent.putExtra("EVENT_ID", eventList.get(childPosition).getEventID());
                    startActivity(intent);
                }
            });
        }

        private void initializePersonView(View view, final int childPosition) {
            if (personList.get(childPosition) != null) {
                ImageView icon = view.findViewById(R.id.image_view);
                if (personList.get(childPosition).getGender().equals("m")) {
                    Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.colorBlue);
                    icon.setImageDrawable(genderIcon);
                }
                else {
                    Drawable genderIcon = new IconDrawable(PersonActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.colorPink);
                    icon.setImageDrawable(genderIcon);
                }
                TextView name = view.findViewById(R.id.text_view);
                String nameStr = personList.get(childPosition).getFirstName() + " " + personList.get(childPosition).getLastName();
                name.setText(nameStr);
                TextView familyType = view.findViewById(R.id.text_view2);
                if (personList.get(childPosition).getPersonID().equals(currPerson.getFatherID())) {
                    familyType.setText(R.string.father);
                }
                else if (personList.get(childPosition).getPersonID().equals(currPerson.getMotherID())) {
                    familyType.setText(R.string.mother);
                }
                else if (personList.get(childPosition).getPersonID().equals(currPerson.getSpouseID())) {
                    familyType.setText(R.string.spouse);
                }
                else {
                    familyType.setText(R.string.child);
                }
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                    intent.putExtra("PERSON_ID", personList.get(childPosition).getPersonID());
                    startActivity(intent);
                }
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}
