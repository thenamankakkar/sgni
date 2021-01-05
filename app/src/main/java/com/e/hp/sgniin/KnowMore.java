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

    ProgressDialog loading;

    // Array of strings...
    ArrayAdapter<String> adapter;
    private ArrayList<String> __courseIdInItem = new ArrayList<>();


    /*variables for json response*/
    String fees, finalfees, markup, course_id, course_id2,coursename;

    String __instituteId, __courseId,__institutename,__instituteaddress;
    ArrayList<itemModel> arrayList;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more);
        TextView instituteName =(TextView)findViewById(R.id.instituteName);
        TextView institueAddress=(TextView)findViewById(R.id.instituteAddress);
        listView = (ListView) findViewById(R.id.courseListView);
        arrayList = new ArrayList<>();


        //progress dialog
        loading = new ProgressDialog(this);
        loading.setTitle("Please Wait..");
        loading.setMessage("Data is Loading .....");
        loading.setMax(5);
        loading.setCancelable(false);
        loading.show();

        /*getting the particular course/ institute ID's */
        Intent intent = getIntent();
        __instituteId = intent.getStringExtra("__instituteId");
        __courseId = intent.getStringExtra("__courseId");
        __institutename = intent.getStringExtra("__institutename");
        __instituteaddress = intent.getStringExtra("__instituteaddress");
        instituteName.setText(__institutename);
        institueAddress.setText(__instituteaddress);



        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());
        //Toast.makeText(this, "" + __instituteId, Toast.LENGTH_SHORT).show();


        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                .addBodyParameter("call", "CoursefeesbyIntId")
                .addBodyParameter("param", __instituteId)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loading.dismiss();

                        if (response != null && response.length() > 0) {

                            Log.d("response_knowmore", "" + response);
                            try {
                                ArrayList<String> res_locid_value = new ArrayList<String>();
                                ArrayList<itemModel> arrayList = new ArrayList<>();
                                ArrayList<String> course_name = new ArrayList<String>();
                                JSONArray contacts = response.getJSONArray("data");
                                for (int i = 0; i < contacts.length(); i++) {


                                    JSONObject c = contacts.getJSONObject(i);
                                    fees = c.getString("fees");
                                    finalfees = c.getString("final_fees");
                                    markup = c.getString("markup");
                                    course_id = c.getString("cid");
                                    coursename = c.getString("coursename");



                                    itemModel model = new itemModel();

                                    model.setCourseName(coursename);
                                    model.setFees(fees);
                                    model.setFees2("\u20B9"+finalfees);
                                    arrayList.add(model);



                                 /*   __courseIdInItem.add(fees);
                                    res_locid_value.add(course_id);
                                    course_name.add(course_id);
                                    Log.d("res_slug", "" + res_slug);
*/
                                }
                                ListViewCoursesAdapter adapter = new ListViewCoursesAdapter(KnowMore.this, arrayList);
                                listView.setAdapter(adapter);



/*                                adapter = new ArrayAdapter<String>(KnowMore.this, R.layout.course_info, res_locid_value);
                                android.widget.ListView list = (android.widget.ListView) findViewById(R.id.courseListView);
                                list.setAdapter(adapter);*/
                                listView.setTextFilterEnabled(true);
                                listView.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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