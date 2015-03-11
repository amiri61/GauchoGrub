package com.g10.gauchogrub;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.g10.gauchogrub.menu.DiningCommon;
import com.g10.gauchogrub.menu.Menu;
import com.g10.gauchogrub.menu.MenuItem;
import com.g10.gauchogrub.utils.CacheUtils;
import com.g10.gauchogrub.utils.MenuParser;
import com.g10.gauchogrub.utils.WebUtils;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MainMenuFragment extends Fragment {

    public final static Logger logger = Logger.getLogger("MainMenuFragment");
    ArrayList<ArrayList<Double>> allRankings;
    WebUtils w = new WebUtils();
    MenuParser mp = new MenuParser();
    TableLayout ratingsTable;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_menu_fragment, container, false);
        allRankings = new ArrayList<>();
        ratingsTable = (TableLayout)rootView.findViewById(R.id.rankingsTable);

        getTodaysRankings();

        return rootView;
    }

    public void inflateRatingsTable(){
        int count = 1;
        ArrayList<SimpleEntry<Integer,Double>> maxRating = findHighestMealRatings();

        for(int i = 0; i <= 2; i++) {
            TableRow ratingRow = new TableRow(getActivity().getApplicationContext());
            ratingRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView ratingTextView = new TextView(getActivity().getApplicationContext());
            ratingTextView.setTextColor(Color.rgb(255,108,52));
            ratingTextView.setTextSize(16);
            if(maxRating != null)
                ratingTextView.setText(DiningCommon.READABLE_DINING_COMMONS[maxRating.get(i).getKey()] + " (" + String.format("%.2f",(maxRating.get(i).getValue())) + " avg. likes per food item)");
            else
                ratingTextView.setText("Please connect to the internet for rating info");
            ratingRow.addView(ratingTextView);
            ratingsTable.addView(ratingRow, count);
            count += 2;
        }

    }

    public void getTodaysRankings() {
        new AsyncTask<Void, Void, ArrayList<ArrayList<Double>>>() {
            @Override
            protected ArrayList<ArrayList<Double>> doInBackground(Void... v) {
                    for(int i = 0; i <=3; i++) {
                        String menuString;
                        try {
                            menuString = w.createMenuString(DiningCommon.DATA_USE_DINING_COMMONS[i], "03/1" + i + "/2015");
                        } catch (Exception e) { e.printStackTrace(); menuString = "";
                        logger.info("caught api exception");}

                        ArrayList<Menu> todaysMenus = mp.getDailyMenuList(menuString);
                        ArrayList<Double> todaysRankings = getRankings(todaysMenus);
                        allRankings.add(todaysRankings);
                    }
                    return allRankings;
            }
            @Override
            protected void onPostExecute(ArrayList<ArrayList<Double>> result) {
                logger.info("entered post execute");
                inflateRatingsTable();
            }
        }.execute();

    }

    public ArrayList<Double> getRankings(ArrayList<Menu> menus) {
        double totalRating = 0, itemCount = 0;
        ArrayList<Double> dayRatings = new ArrayList<>();
        if(menus == null){ return dayRatings; }

        for(Menu menu : menus){
            for(MenuItem item : menu.menuItems) {
                itemCount++;
                totalRating = totalRating + getItemRating(item);
            }
            dayRatings.add((totalRating/itemCount));
            itemCount = 0;
            totalRating = 0;
        }
        return dayRatings;
    }

    public int getItemRating(MenuItem item){
        int totalPositiveRatings = item.totalPositiveRatings;
        int totalRating = item.totalRatings;
        int negativeRatings = totalRating - totalPositiveRatings;
        return totalPositiveRatings - negativeRatings;
    }

    public ArrayList<SimpleEntry<Integer,Double>> findHighestMealRatings() {
        if(allRankings.size() > 0 && allRankings.get(0).size() > 0) {
            double maxBreakFastRating = allRankings.get(0).get(0);
            double maxLunchRating = allRankings.get(0).get(1);
            double maxDinnerRating = allRankings.get(0).get(2);
            int dcTrack = 0, dcTrack1 = 0, dcTrack2 = 0;
            ArrayList<SimpleEntry<Integer, Double>> maxRatingList = new ArrayList<>();
            ArrayList<Integer> nullCheck = new ArrayList<>();

            for (int j = 0; j <= 3; j++) {
                if (allRankings.get(j).size() == 0) {
                    nullCheck.add(j);
                    logger.info(DiningCommon.DATA_USE_DINING_COMMONS[j] + " is null");
                }
            }

            for (int i = 1; i <= 3; i++) {
                if (!nullCheck.contains((i))) {
                    //Compute Max BreakFast Rating
                    if (i != 1) {
                        double currentBreakFastRating = allRankings.get(i).get(0);
                        if (currentBreakFastRating > maxBreakFastRating) {
                            maxBreakFastRating = currentBreakFastRating;
                            dcTrack = i;
                        }
                    }
                    //Compute Max Lunch Rating
                    double currentLunchRating;
                    if (i == 1) {
                        currentLunchRating = allRankings.get(i).get(0);
                    } else {
                        currentLunchRating = allRankings.get(i).get(1);
                    }
                    if (currentLunchRating > maxLunchRating) {
                        maxLunchRating = currentLunchRating;
                        dcTrack1 = i;
                    }

                    //Compute Max Dinner Rating
                    double currentDinnerRating;
                    if (i == 1) {
                        currentDinnerRating = allRankings.get(i).get(1);
                    } else {
                        currentDinnerRating = allRankings.get(i).get(2);
                    }
                    if (currentDinnerRating > maxDinnerRating) {
                        maxDinnerRating = currentDinnerRating;
                        dcTrack2 = i;
                    }
                }
            }
            SimpleEntry<Integer, Double> maxBreakFast = new SimpleEntry<>(dcTrack, maxBreakFastRating);
            SimpleEntry<Integer, Double> maxLunch = new SimpleEntry<>(dcTrack1, maxLunchRating);
            SimpleEntry<Integer, Double> maxDinner = new SimpleEntry<>(dcTrack2, maxDinnerRating);
            maxRatingList.add(maxBreakFast);
            maxRatingList.add(maxLunch);
            maxRatingList.add(maxDinner);

            return maxRatingList;
        }
        else
            return null;
    }

}