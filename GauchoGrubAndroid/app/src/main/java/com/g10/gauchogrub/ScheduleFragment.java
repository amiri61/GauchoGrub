package com.g10.gauchogrub;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.AbstractMap.SimpleEntry;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ScheduleFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private TableLayout scheduleTable;

    public final static Logger logger = Logger.getLogger("ScheduleFragment");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.schedule_fragment, container, false);

        this.scheduleTable = (TableLayout) rootView.findViewById(R.id.schedule_table);

        // Initialize Spinner
        Spinner spinner = (Spinner)rootView.findViewById(R.id.schedule_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.dining_commons_array, R.layout.dining_cams_spinner_item);
        adapter.setDropDownViewResource(R.layout.dining_cams_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);

        return rootView;
    }

    private void inflateSchedule(View view) {
        ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> schedule = getDeLaGuerraSchedule();
        TableLayout scheduleTable = (TableLayout) view.findViewById(R.id.schedule_table);
        TableRow row = new TableRow(getActivity().getApplicationContext());
        TextView tv = new TextView(getActivity().getApplicationContext());
        tv.setText("test");
        row.addView(tv);
        scheduleTable.addView(row);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> schedule = null;
        scheduleTable.removeAllViews();

        if (pos == 0) {
            schedule = getCarilloSchedule();
        } else if (pos == 1) {
            schedule = getDeLaGuerraSchedule();
        } else if (pos == 2) {
            schedule = getOrtegaSchedule();
        } else if (pos == 3) {
            schedule = getPortollaSchedule();
        }

        for (SimpleEntry<String, ArrayList<SimpleEntry<String, String>>> headerEntry : schedule) {
            String header = headerEntry.getKey();

            TableRow headerRow = new TableRow(getActivity().getApplicationContext());
            headerRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            View headerView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.schedule_header, null);
            TextView headerTextView = (TextView) headerView.findViewById(R.id.header);
            headerTextView.setText(header);
            headerRow.addView(headerView);
            scheduleTable.addView(headerRow);

            for(SimpleEntry<String, String> mealEntry : headerEntry.getValue()) {
                String mealTitle = mealEntry.getKey();
                String mealTime = mealEntry.getValue();

                TableRow entryRow = new TableRow(getActivity().getApplicationContext());
                entryRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
                View entryView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.schedule_entry, null);

                TextView mealTypeView = (TextView) entryView.findViewById(R.id.meal_type);
                mealTypeView.setText(mealTitle);

                TextView mealTimeView = (TextView) entryView.findViewById(R.id.meal_time);
                mealTimeView.setText(mealTime);

                entryRow.addView(entryView);
                scheduleTable.addView(entryRow);
                logger.info("child count " + scheduleTable.getChildCount());
            }
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    private ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> getOrtegaSchedule() {
        ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> schedule = new ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>>();

        // Monday-Friday
        ArrayList<SimpleEntry<String, String>> monFri = new ArrayList<SimpleEntry<String, String>>();
        monFri.add(new SimpleEntry("Breakfast", "7:15am - 10:45am"));
        monFri.add(new SimpleEntry("Lunch", "11:45am - 2:30pm"));
        monFri.add(new SimpleEntry("Dinner", "5:00pm - 8:00pm"));

        // Sack Meals
        ArrayList<SimpleEntry<String, String>> sackMeals = new ArrayList<SimpleEntry<String, String>>();
        sackMeals.add(new SimpleEntry("Mon-Fri", "7:30am - 11:30am"));

        schedule.add(new SimpleEntry("Monday-Friday", monFri));
        schedule.add(new SimpleEntry("Sack Meals", sackMeals));

        return schedule;
    }

    private ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> getDeLaGuerraSchedule() {
        ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> schedule = new ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>>();

        // Monday-Friday
        ArrayList<SimpleEntry<String, String>> monFri = new ArrayList<SimpleEntry<String, String>>();
        monFri.add(new SimpleEntry("Lunch", "11:00am - 2:30pm"));
        monFri.add(new SimpleEntry("Dinner", "5:00pm - 8:00pm"));

        // Monday-Thursday
        ArrayList<SimpleEntry<String, String>> monThu = new ArrayList<SimpleEntry<String, String>>();
        monThu.add(new SimpleEntry("Late Night", "9:00pm - 12:30am"));

        // Saturday-Sunday
        ArrayList<SimpleEntry<String, String>> satSun = new ArrayList<SimpleEntry<String, String>>();
        satSun.add(new SimpleEntry("Brunch", "10:30am - 2:00pm"));
        satSun.add(new SimpleEntry("Dinner", "5:00pm - 8:00pm"));

        schedule.add(new SimpleEntry("Monday-Friday", monFri));
        schedule.add(new SimpleEntry("Saturday-Sunday", satSun));
        schedule.add(new SimpleEntry("Moday-Thursday", monThu));

        return schedule;
    }

    private ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> getCarilloSchedule() {
        ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> schedule = new ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>>();

        // Monday-Friday
        ArrayList<SimpleEntry<String, String>> monFri = new ArrayList<SimpleEntry<String, String>>();
        monFri.add(new SimpleEntry("Breakfast", "7:15am - 10:00am"));
        monFri.add(new SimpleEntry("Lunch", "11:00am - 2:30pm"));
        monFri.add(new SimpleEntry("Dinner", "5:00pm - 8:00pm"));

        // Saturday-Sunday
        ArrayList<SimpleEntry<String, String>> satSun = new ArrayList<SimpleEntry<String, String>>();
        satSun.add(new SimpleEntry("Brunch", "10:30am - 2:00pm"));
        satSun.add(new SimpleEntry("Dinner", "5:00pm - 8:00pm"));

        schedule.add(new SimpleEntry("Monday-Friday", monFri));
        schedule.add(new SimpleEntry("Saturday-Sunday", satSun));

        return schedule;
    }

    private ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> getPortollaSchedule() {
        ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>> schedule = new ArrayList<SimpleEntry<String, ArrayList<SimpleEntry<String, String>>>>();

        // Monday-Friday
        ArrayList<SimpleEntry<String, String>> monFri = new ArrayList<SimpleEntry<String, String>>();
        monFri.add(new SimpleEntry("Breakfast", "7:00am - 10:30am"));
        monFri.add(new SimpleEntry("Lunch", "12:00am - 2:30pm"));
        monFri.add(new SimpleEntry("Dinner", "5:00pm - 8:00pm"));

        // Saturday-Sunday
        ArrayList<SimpleEntry<String, String>> satSun = new ArrayList<SimpleEntry<String, String>>();
        satSun.add(new SimpleEntry("Brunch", "10:30am - 2:00pm"));
        satSun.add(new SimpleEntry("Dinner", "5:00pm - 8:00pm"));

        // Sack Meals
        ArrayList<SimpleEntry<String, String>> sackMeals = new ArrayList<SimpleEntry<String, String>>();
        sackMeals.add(new SimpleEntry("Mon-Fri", "7:30am - 11:30am"));

        schedule.add(new SimpleEntry("Monday-Friday", monFri));
        schedule.add(new SimpleEntry("Saturday-Sunday", satSun));
        schedule.add(new SimpleEntry("Sack Meals", sackMeals));

        return schedule;
    }
}