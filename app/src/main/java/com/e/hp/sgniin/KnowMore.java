package com.e.hp.sgniin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class KnowMore extends AppCompatActivity {

    String res_locations, res_parent, res_slug, get_res_parent;

    String res_loc_id;

    // Array of strings...
    ArrayAdapter<String> adapter;
    private ArrayList<String> __courseIdInItem = new ArrayList<>();


    /*variables for json response*/
    String fees, finalfees, markup, course_id, course_id2;

    String __instituteId, __courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more);




        /*getting the particular course/ institute ID's */
        Intent intent = getIntent();
        __instituteId = intent.getStringExtra("__instituteId");
        __courseId = intent.getStringExtra("__courseId");


        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());
        Toast.makeText(this, "" + __instituteId, Toast.LENGTH_SHORT).show();


        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                .addBodyParameter("call", "CoursefeesbyIntId")
                .addBodyParameter("param", __instituteId)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        if (response != null && response.length() > 0) {

                            Log.d("response_knowmore", "" + response);
                            try {
                                ArrayList<String> res_locid_value = new ArrayList<String>();
                                ArrayList<String> asli_res_locid = new ArrayList<String>();
                                JSONArray contacts = response.getJSONArray("data");
                                for (int i = 0; i < contacts.length(); i++) {


                                    JSONObject c = contacts.getJSONObject(i);
                                    fees = c.getString("fees");
                                    finalfees = c.getString("final_fees");
                                    markup = c.getString("markup");
                                    course_id = c.getString("cid");

                                    /*second api call start*/
                                    AndroidNetworking.post("https://sgni.in/api/run_new.php")

                                            .addBodyParameter("call", "Get_Course_Name")
                                            .addBodyParameter("param", course_id)
                                            .setTag("test")
                                            .setPriority(Priority.MEDIUM)
                                            .build()
                                            .getAsJSONObject(new JSONObjectRequestListener() {
                                                @Override
                                                public void onResponse(JSONObject response) {

                                                    if (response != null && response.length() > 0) {

                                                        List<CourseData> data = new ArrayList<>();


                                                        Log.d("response_getcoursename", "" + response);
                   /*                                     try {

                                                            JSONArray contacts = response.getJSONArray("data");
                                                            for (int i = 0; i < contacts.length(); i++) {

                                                                JSONObject c = contacts.getJSONObject(i);
                                                                course_id2 = c.getString("cid");

                                                            }
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }*/
                                                    }
                                                }

                                                @Override
                                                public void onError(ANError error) {
                                                    // handle error
                                                    Log.d("error", "ha ha" + error);
                                                }
                                            });
                                    /*second api call end*/




                                    __courseIdInItem.add(fees);
                                    res_locid_value.add(course_id);
                                    Log.d("res_slug", "" + res_slug);













                                }

                                adapter = new ArrayAdapter<String>(KnowMore.this, R.layout.course_info, R.id.userInfo, res_locid_value);
                                android.widget.ListView list = (android.widget.ListView) findViewById(R.id.courseListView);
                                list.setAdapter(adapter);
                                list.setTextFilterEnabled(true);
                                list.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
                                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view,
                                                            int position, long id) {

                                        Toast.makeText(KnowMore.this, "Item Clicked", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }








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