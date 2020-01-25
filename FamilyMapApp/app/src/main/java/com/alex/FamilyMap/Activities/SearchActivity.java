package com.alex.FamilyMap.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.R;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.List;


import Model.EventM;
import Model.PersonM;

public class SearchActivity extends AppCompatActivity {
    private RecyclerView mPersonRecyclerView;
    private RecyclerView mEventRecyclerView;
    private PersonAdapter personAdapter;
    private EventAdapter eventAdapter;
    private List<PersonM> personList;
    private List<EventM> eventList;
    EditText searchBar;
    DataCache dataCache = DataCache.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setUpSearchBar();
        setUpList();
        setUpRecyclerView();

        Iconify.with(new FontAwesomeModule());
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

    private void setUpSearchBar() {
        searchBar = findViewById(R.id.search_bar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPersonRecyclerView.setVisibility(View.VISIBLE);
                mEventRecyclerView.setVisibility(View.VISIBLE);
                personAdapter.getFilter().filter(s.toString());
                eventAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void setUpList() {
        personList = new ArrayList<>(dataCache.getPersonMap().values());
        eventList = new ArrayList<>(dataCache.getEventList());
    }

    private void setUpRecyclerView() {
        mPersonRecyclerView = findViewById(R.id.recycler_view_search_person);
        mPersonRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPersonRecyclerView.setNestedScrollingEnabled(false);
        personAdapter = new PersonAdapter(personList);
        mPersonRecyclerView.setAdapter(personAdapter);
        mPersonRecyclerView.setVisibility(View.GONE);

        mEventRecyclerView = findViewById(R.id.recycler_view_search_event);
        mEventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mEventRecyclerView.setNestedScrollingEnabled(false);
        eventAdapter = new EventAdapter(eventList);
        mEventRecyclerView.setAdapter(eventAdapter);
        mEventRecyclerView.setVisibility(View.GONE);
    }

    private class PersonHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PersonM mPerson;
        private final ImageView icon;
        private final TextView name;
        PersonHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            icon = itemView.findViewById(R.id.image_view);
            name = itemView.findViewById(R.id.text_view);
        }

        void bind(PersonM person) {
            mPerson = person;
            if (person.getGender().equals("m")) {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).colorRes(R.color.colorBlue);
                icon.setImageDrawable(genderIcon);
            }
            else {
                Drawable genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).colorRes(R.color.colorPink);
                icon.setImageDrawable(genderIcon);
            }
            String fullName = person.getFirstName() + " " + person.getLastName();
            name.setText(fullName);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), PersonActivity.class);
            intent.putExtra("PERSON_ID", mPerson.getPersonID());
            startActivity(intent);
        }
    }

    private class PersonAdapter extends RecyclerView.Adapter<PersonHolder> {
        private final List<PersonM> personList;
        private final List<PersonM> personListFull;
        PersonAdapter(List<PersonM> personList) {
            this.personList = personList;
            personListFull = new ArrayList<>(personList);
        }

        @Override
        public PersonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.search_item, parent, false);
            return new PersonHolder(view);
        }

        @Override
        public void onBindViewHolder(PersonHolder personHolder, int position) {
            PersonM person = personList.get(position);
            personHolder.bind(person);
        }

        @Override
        public int getItemCount() {
            return personList.size();
        }

        public Filter getFilter() {
            return filter;
        }

        private Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence s) {
                List<PersonM> filteredList = new ArrayList<>();
                if (s == null || s.length() == 0) {
                    filteredList.addAll(personListFull);
                } else {
                    String filterPattern = s.toString().toLowerCase().trim();
                    for (PersonM person : personListFull) {
                        if (person.getFirstName().toLowerCase().contains(filterPattern) ||
                                person.getLastName().toLowerCase().contains(filterPattern) ||
                                (person.getFirstName().toLowerCase() + " " + person.getLastName().toLowerCase()).contains(filterPattern)) {
                            filteredList.add(person);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                personList.clear();
                personList.addAll((List)results.values);
                notifyDataSetChanged();
            }
        };
    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private EventM mEvent;
        private final ImageView icon;
        private final TextView description;
        private final TextView name;
        EventHolder(View view) {
            super(view);
            itemView.setOnClickListener(this);
            icon = itemView.findViewById(R.id.image_view);
            description = itemView.findViewById(R.id.text_view);
            name = itemView.findViewById(R.id.text_view2);
        }

        void bind(EventM event) {
            int hash = event.getEventType().hashCode();
            int r = (hash & 0xFF0000) >> 16;
            int g = (hash & 0x00FF00) >> 8;
            int b = hash & 0x0000FF;
            int rgb = r;
            rgb = (rgb << 8) + g;
            rgb = (rgb << 8) + b;
            Drawable markerIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_map_marker).color(rgb);
            icon.setImageDrawable(markerIcon);
            mEvent = event;
            PersonM person = dataCache.getPerson(event.getPersonID());
            String descriptionStr = event.getEventType().toUpperCase() + ": " + event.getCity() + ", " +
                    event.getCountry() + " (" + event.getYear() + ")";
            description.setText(descriptionStr);
            String fullName = person.getFirstName() + " " + person.getLastName();
            name.setText(fullName);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), EventActivity.class);
            intent.putExtra("EVENT_ID", mEvent.getEventID());
            startActivity(intent);
        }
    }

    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private final List<EventM> eventList;
        private final List<EventM> eventListFull;
        EventAdapter(List<EventM> eventList) {
            this.eventList = eventList;
            eventListFull = new ArrayList<>(eventList);
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.search_item, parent, false);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(EventHolder eventHolder, int position) {
            EventM event = eventList.get(position);
            eventHolder.bind(event);
        }

        @Override
        public int getItemCount() {
            return eventList.size();
        }

        public Filter getFilter() {
            return filter;
        }

        private Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence s) {
                List<EventM> filteredList = new ArrayList<>();
                if (s == null || s.length() == 0) {
                    filteredList.addAll(eventListFull);
                } else {
                    String filterPattern = s.toString().toLowerCase().trim();
                    for (EventM event : eventListFull) {
                        PersonM person = dataCache.getPerson(event.getPersonID());
                        if (person.getFirstName().toLowerCase().contains(filterPattern) ||
                            person.getLastName().toLowerCase().contains(filterPattern) ||
                            (person.getFirstName() + " " + person.getLastName()).toLowerCase().contains(filterPattern)) {
                            filteredList.add(event);
                        }
                        if (event.getEventType().toLowerCase().contains(filterPattern) ||
                            event.getCity().toLowerCase().contains(filterPattern) ||
                            event.getCountry().toLowerCase().contains(filterPattern) ||
                            Integer.toString(event.getYear()).contains(filterPattern)) {
                            filteredList.add(event);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                eventList.clear();
                eventList.addAll((List)results.values);
                notifyDataSetChanged();
            }
        };
    }
}
