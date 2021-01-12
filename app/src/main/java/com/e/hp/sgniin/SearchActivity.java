package com.e.hp.sgniin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private CourseAdapter mAdapter;

    String value;

    ProgressDialog institute_loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        value = intent.getStringExtra("searchedText");

        institute_loading = new ProgressDialog(SearchActivity.this);
        institute_loading.setTitle("Please Wait..");
        institute_loading.setMessage("Search Results Loading .....");
        institute_loading.setMax(5);
        institute_loading.setCancelable(false);
        institute_loading.show();
        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());

        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                .addBodyParameter("call", "find")
                .addBodyParameter("a", value)
                .addBodyParameter("param", "")

                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        institute_loading.dismiss();

                        if (response != null && response.length() > 0) {

                            List<CourseData> data = new ArrayList<>();


                            Log.d("response_search", "" + response);
                            try {

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

                                }


                                /*recycler view code*/
                                recyclerView = findViewById(R.id.recyclerView);
                                mAdapter = new CourseAdapter(SearchActivity.this, data);

                                //CourseViewAdapter courseViewAdapter = new CourseViewAdapter(data);
                                recyclerView.setAdapter(mAdapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));


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