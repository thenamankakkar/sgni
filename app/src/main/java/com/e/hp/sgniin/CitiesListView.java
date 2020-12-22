package com.e.hp.sgniin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CitiesListView extends AppCompatActivity {

    String res_locations,res_parent;

    ProgressDialog loading;

    // Array of strings...
    ArrayAdapter<String> adapter;
    android.widget.ListView simpleList;
    private ArrayList<String> songLink = new ArrayList<>();
    private ArrayList<String> songTitle = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list_view);

        //progress dialog
        loading = new ProgressDialog(this);
        loading.setTitle("Please Wait..");
        loading.setMessage("Loading .....");
        loading.setMax(5);
        loading.setCancelable(false);
        loading.show();

        EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.setCursorVisible(true);


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                CitiesListView.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());


        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                .addBodyParameter("call", "locationcity")
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loading.dismiss();
                        Log.d("registe_resonse", "" + response);

                        try {
                            ArrayList<String> name = new ArrayList<String>();
                            ArrayList<String> parent_location = new ArrayList<String>();
                            JSONArray contacts = response.getJSONArray("data");

                            ArrayList<Object> MARKET_CAP_ARRAY_LIST = new ArrayList<>();


                            for (int i = 1; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                res_locations = c.getString("location");
                                res_parent = c.getString("locid");

                                /*    name.add(i+")"+" "+res_locations);*/
                                parent_location.add(res_parent);
                                name.add(res_locations);
                                Log.d("response_location", "" + res_locations);
                            }

                            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.music_info, R.id.userInfo, name);
                            android.widget.ListView list = (android.widget.ListView) findViewById(R.id.simpleListView);
                            list.setAdapter(adapter);
                            list.setTextFilterEnabled(true);


                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {
                                    Intent intent = new Intent(CitiesListView.this, Location_List.class);
                                    intent.putExtra("res_locations",name.get(position));
                                    intent.putExtra("res_parent",parent_location.get(position));
                                    startActivity(intent);

                                    Toast.makeText(CitiesListView.this, name.get(position), Toast.LENGTH_SHORT).show();


                                }
                            });









                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.d("error", "ha ha" + error);
                    }
                });


    }

}