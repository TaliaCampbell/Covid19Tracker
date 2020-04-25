package com.example.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    TextView confirmedTextView;
    TextView totalDeathsView;
    TextView totalRecoveredView;
    EditText searchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmedTextView = findViewById(R.id.confirmed);
        totalDeathsView = findViewById(R.id.deaths);
        totalRecoveredView = findViewById(R.id.recovered);
        searchBar = findViewById(R.id.search_bar);
    }



    public void fetchData(View view) {


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.covid19api.com/summary";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject responseObject = new JSONObject(response);
                            JSONArray countriesArray = responseObject.getJSONArray("Countries");
                            for(int i = 0; i<countriesArray.length();i++){
                                JSONObject currentCountry = countriesArray.getJSONObject(i);
                                String currentCountryString = currentCountry.getString("Country").toLowerCase();
                                String countrySearched = searchBar.getText().toString().toLowerCase();
                                if(currentCountryString.equals(countrySearched)){
                                    confirmedTextView.setText(currentCountry.getString("TotalConfirmed"));
                                    totalDeathsView.setText(currentCountry.getString("TotalDeaths"));
                                    totalRecoveredView.setText(currentCountry.getString("TotalRecovered"));
                                }
                            }
                        }catch(Exception e){
                            confirmedTextView.setText("Country Not Found");
                            totalDeathsView.setText("Country Not Found");
                            totalRecoveredView.setText("Country Not Found");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                confirmedTextView.setText("That didn't work!");
                totalDeathsView.setText("That didn't work!");
                totalRecoveredView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}