package com.e.hp.sgniin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Location_vise_bookcourse extends AppCompatActivity {
    RecyclerView recyclerView;
    String course_res_name, course_res_address, course_res_views;

    private CourseAdapter mAdapter;

    String __institute_slug;
    ProgressDialog institute_loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_vise_bookcourse);

        institute_loading = new ProgressDialog(Location_vise_bookcourse.this);
        institute_loading.setTitle("Please Wait..");
        institute_loading.setMessage("Institutes Loading .....");
        institute_loading.setMax(5);
        institute_loading.setCancelable(false);
        institute_loading.show();


        Intent intent = getIntent();
        String gotintentofLocationID = intent.getStringExtra("response_locid");
        /*Toast.makeText(this, gotintentofLocationID, Toast.LENGTH_SHORT).show();*/


        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                .addBodyParameter("call", "cview")
                .addBodyParameter("loc", gotintentofLocationID)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        institute_loading.dismiss();
                            List<CourseData> data = new ArrayList<>();
                            Log.d("response_course", "" + response);
                            try {
                                String empty_response = response.getString("data");
                                if (empty_response.equals("[]"))
                                {
                                    Toast.makeText(Location_vise_bookcourse.this, "No Data Found !", Toast.LENGTH_SHORT).show();
                                }
                                JSONArray contacts = response.getJSONArray("data");
                                for (int i = 0; i < contacts.length(); i++) {
                                    CourseData fishData = new CourseData();
                                    JSONObject c = contacts.getJSONObject(i);
                                    fishData.fishName = c.getString("name");
                                    fishData.fishaddress = c.getString("address");
                                    fishData.fishviews = c.getString("Views");
                                    fishData.inst_name = c.getString("name");
                                    fishData.institute_id = c.getString("inst_id");
                                    fishData.course_id = c.getString("inst_cid");
                                    fishData.__institute_slug= c.getString("inst_slug");
                                    data.add(fishData);


                           /*     CourseViewData model = new CourseViewData(course_res_name,course_res_address,R.drawable.batman);
                                data.add(model);*/

                                    Log.d("notgetting", "" + course_res_name);
                                    //Toast.makeText(Location_vise_bookcourse.this, course_res_name, Toast.LENGTH_SHORT).show();

                                }


                                /*recycler view code*/
                                recyclerView = findViewById(R.id.recyclerView);
                                mAdapter = new CourseAdapter(Location_vise_bookcourse.this, data);

                                //CourseViewAdapter courseViewAdapter = new CourseViewAdapter(data);
                                recyclerView.setAdapter(mAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(Location_vise_bookcourse.this));


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