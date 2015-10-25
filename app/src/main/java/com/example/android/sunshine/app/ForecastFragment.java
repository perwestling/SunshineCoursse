package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment doing a forecast in the UI.
 */
public class ForecastFragment extends Fragment {

    private ArrayAdapter<String> mForecastAdapter;

    private List<String> forecastData = new ArrayList<>();

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        mForecastAdapter = createAdaptor();
        listView.setAdapter(mForecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent showDetailsIntent = new Intent(getActivity(), DetailActivity.class);
                showDetailsIntent.setAction(Intent.ACTION_SEND);
                showDetailsIntent.putExtra(Intent.EXTRA_TEXT, forecastData.get(position));
                showDetailsIntent.setType("text/plain");

                startActivity(showDetailsIntent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateWeather() {
        FetchWeatherTask weaterTask = new FetchWeatherTask(getActivity(), mForecastAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key),
                getString(R.string.pref_location_default));
        weaterTask.execute(location);
    }

    private ArrayAdapter<String> createAdaptor() {
        return new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_forecast, R.id.list_item_forecast_textview, forecastData);
    }
}

