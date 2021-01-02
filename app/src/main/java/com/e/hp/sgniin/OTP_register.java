package com.e.hp.sgniin;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.navigation.NavigationView;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OTP_register extends AppCompatActivity
        implements InternetConnectivityListener {

    private InternetAvailabilityChecker mInternetAvailabilityChecker;

    ProgressDialog progressdialog;
    ProgressDialog progress_verifyotp;
    String msg = "NO Internet Connection";

    No_Internet customProgress;


    EditText ed_phone, ed_ref, ed_otp;
    String phone, ref, getPhone, getRef;
    Button register, register2;

    String getphone, studentid, scode, otp, vkey;

    String getOtp;


    /*varibles to add the user in local db*/
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String Phone = "nameKey";
    String registeredno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_register);

        sharedPreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                //TODO your background code

                if (sharedPreferences.contains(Phone)) {

                    registeredno = sharedPreferences.getString(Phone, "");

/*                    Toast.makeText(getApplicationContext(), registeredno, Toast.LENGTH_SHORT).show();*/

                    //networking libray intialization
                    AndroidNetworking.initialize(getApplicationContext());
                    /*auto-login*/
                    AndroidNetworking.post("https://sgni.in/api/run_new.php")

                            .addBodyParameter("call", "autologin")
                            .addBodyParameter("param", registeredno)

                            .setTag("test")
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Intent intent = new Intent(OTP_register.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);


                                    Log.d("sharepreference", "" + response);

                                    try {
                                        JSONArray contacts = response.getJSONArray("data");
                                        for (int i = 0; i < contacts.length(); i++) {
                                            JSONObject c = contacts.getJSONObject(i);

                                        }



                                        Log.d("register_phone", "" + contacts);
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
        });





        //custom progress instance
        customProgress = No_Internet.getInstance();

        //internet availability instance
        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);

        //progress dialog
        progressdialog = new ProgressDialog(this);
        progressdialog.setTitle("Please Wait..");
        progressdialog.setMessage("Sending OTP .....");
        progressdialog.setMax(5);
        progressdialog.setCancelable(false);


        //progress dialog verify otp
        progress_verifyotp = new ProgressDialog(this);
        progress_verifyotp.setTitle("Please Wait..");
        progress_verifyotp.setMessage("Verifying OTP .....");
        progress_verifyotp.setMax(5);
        progress_verifyotp.setCancelable(false);





        ed_phone = (EditText) findViewById(R.id.ed_phone);
        ed_ref = (EditText) findViewById(R.id.ed_ref);
        ed_otp = (EditText) findViewById(R.id.ed_otp);

        register = (Button) findViewById(R.id.btn_register);
        register2 = (Button) findViewById(R.id.btn_register2);

        ed_phone.addTextChangedListener(nameTextWatcher);
        ed_otp.addTextChangedListener(nameTextWatcher);


        //register button click
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhone = ed_phone.getText().toString();
                getRef = ed_ref.getText().toString();
                /* Toast.makeText(getApplicationContext(),"ff"+getphno,Toast.LENGTH_LONG).show();*/

                progressdialog.show();

                AndroidNetworking.post("https://sgni.in/api/run_new.php")

                        .addBodyParameter("call", "register")
                        .addBodyParameter("email", "")
                        .addBodyParameter("pass", "")
                        .addBodyParameter("name", "")
                        .addBodyParameter("phone", getPhone)
                        .addBodyParameter("ref", getRef)
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressdialog.dismiss();


                                AlertDialog.Builder builder = new AlertDialog.Builder(
                                        OTP_register.this);
                                builder.setTitle("Check Your Inbox");
                                builder.setMessage("OTP has been sent to your number");
                                builder.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                builder.show();


                                Log.d("registe_resonse", "" + response);

                                try {
                                    JSONArray contacts = response.getJSONArray("data");
                                    for (int i = 0; i < contacts.length(); i++) {
                                        JSONObject c = contacts.getJSONObject(i);
                                        getphone = c.getString("phone");
                                        studentid = c.getString("studentid");
                                        scode = c.getString("scode");
                                        otp = c.getString("otp");
                                        vkey = c.getString("vkey");
                                        Log.d("get_register_phone", "" + getphone);
                                        Log.d("studentid", "" + studentid);
                                        Log.d("scode", "" + scode);
                                        Log.d("response_otp", "" + otp);
                                        Log.d("vkey", "" + vkey);
                                    }
                                    ed_phone.setEnabled(false);
                                    ed_ref.setEnabled(false);
                                    register.setText("Verify OTP");

                                    if (!getphone.isEmpty()) {


                                        ed_otp.setVisibility(View.VISIBLE);

                                    }


                                    Log.d("register_phone", "" + contacts);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //disable button on response
                                register.setVisibility(View.GONE);
                                register2.setVisibility(View.VISIBLE);


                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.d("error", "ha ha" + error);
                            }
                        });
            }
        });



        /*verifying the otp*/
        register2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Toast.makeText(OTP_register.this, "Button clicked", Toast.LENGTH_SHORT).show();*/
                progress_verifyotp.show();
                getOtp = ed_otp.getText().toString();
                if (getOtp.equals(otp)) {
                    /*Pass the dashboard intent*/
                    progress_verifyotp.dismiss();




/*
                    if (sharedPreferences.contains(Name)) {

                    }*/

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Phone, getPhone);
                    editor.apply();
                    editor.commit();


                    Intent intent = new Intent(OTP_register.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("user_phone_no", getPhone);
                    intent.putExtra("student_id", studentid);
                    intent.putExtra("scode", scode);
                    intent.putExtra("student_otp", otp);
                    intent.putExtra("user_vkey", vkey);
                    startActivity(intent);
                } else if (!getOtp.equals(otp)) {
                    progress_verifyotp.dismiss();
                    ProgressDialog otperrorProgress = new ProgressDialog(OTP_register.this);
                    otperrorProgress.setTitle("Please Wait..");
                    otperrorProgress.setMessage(" OTP Not Matching");
                    otperrorProgress.setMax(5);
                    otperrorProgress.setCancelable(false);

                    otperrorProgress.setButton(DialogInterface.BUTTON_NEGATIVE, "Enter OTP AGAIN", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            otperrorProgress.dismiss();//dismiss dialog
                        }
                    });


                    otperrorProgress.show();


                }


            }
        });


    }

    private TextWatcher nameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            getOtp = ed_otp.getText().toString();
            if (getOtp.isEmpty()) {
                ed_otp.setError("Please Enter Received OTP");
            }


            phone = ed_phone.getText().toString().trim();
            ref = ed_ref.getText().toString().trim();


            if (phone.isEmpty()) {
                ed_phone.setError("Enter Phone Number");
            } else {
                /* register.setBackgroundColor(ContextCompat.getColor(AddNewScreen.this,R.color.colorPrimary));*/
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (ed_phone.getText().length() > 0) {
                ed_phone.setError(null);
            }

            if (ed_phone.getText().length() < 10) {
                ed_phone.setError("Please Enter 10 Digits Number");

            }
            if (ed_phone.getText().length() == 10) {
                register.setEnabled(true);

            }


            if (ed_otp.getText().length() > 0) {
                ed_otp.setError(null);
                register2.setEnabled(true);
            }

        }
    };


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(OTP_register.this);
        builder.setMessage("Do you want to close this application ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("SGNI.in Termination");
        alert.show();
    }


    //check-internet
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInternetAvailabilityChecker.removeInternetConnectivityChangeListener(this);
    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        if (isConnected) {

            customProgress.hideProgress();
        } else {
            customProgress.showProgress(OTP_register.this, msg, false);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}