package com.e.hp.sgniin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Wallet extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String Studentid = "sid";
    public static final String mypreference = "mypref";
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        sharedPreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        String student_id = sharedPreferences.getString(Studentid, "");

        //networking libray intialization
        AndroidNetworking.initialize(getApplicationContext());

        //progress dialog
        loading = new ProgressDialog(this);
        loading.setTitle("Please Wait..");
        loading.setMessage("Wallet Balance Loading .....");
        loading.setMax(5);
        loading.setCancelable(false);
        loading.show();

        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.post("https://sgni.in/api/run_new.php")

                .addBodyParameter("call", "wallb")
                .addBodyParameter("s", student_id)
                .setTag("test")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        loading.dismiss();

                        if (response != null && response.length() > 0) {

                            Log.d("response_wallet", "" + response);
                            try {

                                JSONArray contacts = response.getJSONArray("data");

                                TextView wallb =(TextView)findViewById(R.id.wallb);
                                wallb.setText(contacts.getString(0)+" \u20A8");


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