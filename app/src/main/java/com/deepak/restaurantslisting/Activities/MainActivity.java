package com.deepak.restaurantslisting.Activities;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.deepak.restaurantslisting.Constants.AppConstants;
import com.deepak.restaurantslisting.Fragments.CategoryDialogFragment;
import com.deepak.restaurantslisting.Utils.GPSTracker;
import com.deepak.restaurantslisting.R;
import com.deepak.restaurantslisting.Adapters.RestaurantAdapter;
import com.deepak.restaurantslisting.Models.RestaurantData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements RestaurantAdapter.MyOnclickListener,  SearchView.OnQueryTextListener{
    private RecyclerView recyclerview;
    private static ArrayList<RestaurantData> restData = new ArrayList<>();
    private RestaurantAdapter mAdapter;
    ProgressDialog pDialog;
    double latitude,longitude;
    RelativeLayout locationlayout;
    TextView location_text;
    GPSTracker gps;
    Geocoder geocoder;
    List<Address> addresses=new ArrayList<Address>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        locationlayout = (RelativeLayout) findViewById(R.id.location_layout);
        location_text = (TextView) findViewById(R.id.location_text);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerview.setLayoutManager(layoutManager);
        mAdapter = new RestaurantAdapter(restData,MainActivity.this);
        recyclerview.setAdapter(mAdapter);

        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            try {
                geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if(addresses!=null&&addresses.size()!=0)
                {
                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    location_text.setText(address+", "+city);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                Log.d(AppConstants.CDLog,AppConstants.CDLog+e);
            }
        }else{
            gps.showSettingsAlert();
        }
        if(restData==null||restData.size()==0)  sendRequest();

        locationlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gps.canGetLocation()){
                    try {
                        geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                        addresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if(addresses!=null&&addresses.size()!=0)
                        {
                            String address = addresses.get(0).getAddressLine(0);
                            String city = addresses.get(0).getLocality();
                            location_text.setText(address+", "+city);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e){
                        Log.d(AppConstants.CDLog,AppConstants.CDLog+e);
                    }
                }
                else {
                    gps.showSettingsAlert();
                }
            }
        });
    }
    private void sendRequest(){
        pDialog = new ProgressDialog(MainActivity.this);
        if(restData==null||restData.size()==0) {
            pDialog.setMessage(AppConstants.LOADING);
            pDialog.show();
            pDialog.setCancelable(false);
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                AppConstants.url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        showJSON(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(AppConstants.CDLog, AppConstants.ERROR + error.getMessage());
                pDialog.hide();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(jsonObjReq);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //To prevent window leaks
        if(pDialog!=null)
            pDialog.cancel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void openCategory(int position){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.addToBackStack(null);
        CategoryDialogFragment dialog =  CategoryDialogFragment.newInstance(restData.get(position).getCategories());
        dialog.show(manager, AppConstants.Dialog);

    }
    private void showJSON(JSONObject json){
        if (json != null) {
            try {
                //Get JSON response by converting JSONArray into String
                JSONArray array = json.getJSONArray(AppConstants.DATA);
                RestaurantData restaurant;
                String name,locality,image;
                double lat,lang;
                int noOfCoupons;
                for(int i = 0; i < array.length(); i++) {
                    ArrayList<String> categories=new ArrayList<>();
                    JSONObject obj = array.getJSONObject(i);
                    name=obj.get(AppConstants.RestName).toString();
                    image=obj.get(AppConstants.RestImage).toString();
                    noOfCoupons=(int)obj.get(AppConstants.Coupons);
                    locality=obj.get(AppConstants.Neighbour).toString();
                    lat=Double.parseDouble(obj.get(AppConstants.Lat).toString());
                    lang=Double.parseDouble(obj.get(AppConstants.Long).toString());
                    float [] dist = new float[1];
                    Location.distanceBetween(lat, lang, latitude, longitude, dist);
                    JSONArray array2 = obj.getJSONArray(AppConstants.Categories);
                    for(int j = 0; j < array2.length(); j++) {
                        JSONObject obj3 = array2.getJSONObject(j);
                        categories.add(obj3.get(AppConstants.Name).toString());
                    }
                    restaurant = new RestaurantData(image,name,locality,false,noOfCoupons,lat,lang,dist[0],categories);
                    restData.add(restaurant);
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                Toast.makeText(MainActivity.this, AppConstants.ERROR_PARSING, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, AppConstants.SOMETHING_WENT_WRONG, Toast.LENGTH_LONG).show();
            }
            sortList();
            mAdapter.notifyDataSetChanged();
        }
        //When JSON is null
        else {
            Toast.makeText(MainActivity.this,AppConstants.UNEXPECTED_ERROR,Toast.LENGTH_SHORT).show();
        }
    }
    public void sortList(){
        Collections.sort(restData, new Comparator<RestaurantData>(){
            public int compare(RestaurantData emp1, RestaurantData emp2) {
                return Double.compare(emp1.getDistance(),emp2.getDistance());
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
                        mAdapter.setFilter(restData);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }
    @Override
    public boolean onQueryTextChange(String newText) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        final ArrayList<RestaurantData> filteredModelList = filter(restData, newText);
        mAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    private ArrayList<RestaurantData> filter(ArrayList<RestaurantData> models, String query) {
        query = query.toLowerCase();

        final ArrayList<RestaurantData> filteredModelList = new ArrayList<>();
        for (RestaurantData model : models) {
            final String text = model.getRestname().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}
