package com.spanishdictionary.app;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


public class MainActivity extends ActionBarActivity  {

    DataBaseHelper myDbHelper;
    ListView listView;
    ArrayList<DictionaryData> dictList;
    MyArrayAdapter myArrayAdapter;
    EditText searchTextBox;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDbHelper = new DataBaseHelper(this);

        initializeAdNetwork();

        //Get ListView from activity_main.xml
        listView = (ListView) findViewById(R.id.listView);
        dictList = new ArrayList<DictionaryData>();
        dictList.add(new DictionaryData(-1,"Search"));
        myArrayAdapter = new MyArrayAdapter(this,dictList, "", interstitial);

        //setListAdapter
        listView.setAdapter(myArrayAdapter);



        try {

            myDbHelper.createDataBase();

        } catch (IOException ioe) {

            throw new Error("Unable to create database");

        }

        try {

            myDbHelper.openDataBase();

        }catch(SQLException sqle){

            throw sqle;
        }

        searchTextBox = (EditText) findViewById(R.id.searchTextBox);
        searchTextBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                new searchDictAsyncTask().execute("");
            }
        });
        searchTextBox.requestFocus();
    }

    private void initializeAdNetwork()
    {
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Prepare the Interstitial Ad
        interstitial = new InterstitialAd(this);
        // Insert the Ad Unit ID
        interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));

        interstitial.loadAd(adRequest);

//// Prepare an Interstitial Ad Listener
//        interstitial.setAdListener(new AdListener() {
//            public void onAdLoaded() {
//                // Call displayInterstitial() function
//                displayInterstitial();
//            }
//        });
    }


    private class searchDictAsyncTask extends AsyncTask<String, Integer, String>
    {
        ArrayList<DictionaryData> dictData;

        //call to look up dictionary data
        //this method can't touch the UI
        @Override
        protected String doInBackground(String... s)
        {
            dictData = searchDictionary();
            final String searchText = searchTextBox.getText().toString();

            Collections.sort(dictList, new Comparator<DictionaryData>(){
                public int compare(DictionaryData s1, DictionaryData s2) {
                    return s1.getSpanishChars(searchText).length() - s2.getSpanishChars(searchText).length();
                }
            });

            return "";
        }

        //calling this method to modify UI
        @Override
        protected void onPostExecute(String s) {
            // textview for results;
            TextView textViewResults = (TextView) findViewById(R.id.textViewResults);
            if (dictList.get(0)._id != -1) {
                textViewResults.setText(dictList.size() + " results");
            }
            else
            {
                textViewResults.setText("0 results");
            }

            myArrayAdapter = new MyArrayAdapter(MainActivity.this,dictList, searchTextBox.getText().toString(), interstitial);
            listView.setAdapter(myArrayAdapter);
            myArrayAdapter.notifyDataSetChanged();
            listView.invalidateViews();
        }
    }

    public ArrayList<DictionaryData> searchDictionary()
    {

        String searchText = searchTextBox.getText().toString();


        //dictList.clear();
        dictList = myDbHelper.getDictionaryData(searchText);
        return dictList;



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            aboutMenuItem();
        }

        return super.onOptionsItemSelected(item);
    }

    private void aboutMenuItem() {



        startActivity(new Intent(this,about_me.class));

    }

    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
