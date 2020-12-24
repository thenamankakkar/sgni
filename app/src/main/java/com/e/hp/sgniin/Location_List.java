package com.e.hp.sgniin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Location_List extends AppCompatActivity {

    ProgressDialog loading;

    String res_locations, res_parent, res_slug, get_res_parent;

    String res_loc_id;

    // Array of strings...
    ArrayAdapter<String> adapter;
    Integer loc;


    private ArrayList<String> cityLocationId = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__list);


        //progress dialog
        loading = new ProgressDialog(this);
        loading.setTitle("Please Wait..");
        loading.setMessage("Loading .....");
        loading.setMax(5);
        loading.setCancelable(false);
        loading.show();


        Intent intent = getIntent();
        String get_res_location = intent.getStringExtra("res_locations");
        get_res_parent = intent.getStringExtra("res_parent");

       // Toast.makeText(this, "" + get_res_parent, Toast.LENGTH_SHORT).show();


        EditText inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.setCursorVisible(true);


        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                Location_List.this.adapter.getFilter().filter(cs);
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
                .addBodyParameter("loc", get_res_parent)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        loading.dismiss();

                        Log.d("location_resonse", "" + response);

                        try {
                            ArrayList<String> res_locid_value = new ArrayList<String>();
                            ArrayList<String> asli_res_locid = new ArrayList<String>();
                            JSONArray contacts = response.getJSONArray("data");
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                res_slug = c.getString("location");
                                res_loc_id = c.getString("locid");
                                cityLocationId.add(res_loc_id);
                                res_locid_value.add(res_slug);

                                Log.d("res_slug", "" + res_slug);


                            }

                            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.location_info, R.id.userInfo, res_locid_value);
                            android.widget.ListView list = (android.widget.ListView) findViewById(R.id.simpleListView);
                            list.setAdapter(adapter);
                            list.setTextFilterEnabled(true);
                            list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);


                            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int position, long id) {

                                    /*Toast.makeText(Location_List.this, "Item Clicked", Toast.LENGTH_SHORT).show();*/
                                    Intent intent = new Intent(Location_List.this, Location_vise_bookcourse.class);
                                    intent.putExtra("response_locid",cityLocationId.get(position));
                                    startActivity(intent);
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