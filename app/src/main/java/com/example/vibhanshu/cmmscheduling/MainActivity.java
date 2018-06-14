package com.example.vibhanshu.cmmscheduling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button workshop,findCoach;
    Spinner owningRailwaySpinner;
    String[] owningRailwayList = {"CR","ECOR","ECR","ER","NCR","NER","NFR","NR","NWR","SCR","SECR","SER","SR","SWR","WCR","WR"};
    LinearLayout takeInputLayout;
    TextInputEditText coachNumberEditText;

    //RequestQueue requestQueue;

    ProgressDialog pd = null;

    private static String SERVER_URL = "http://cmm.indianrail.gov.in/cmms/mobile.do?subAction=getCoachStatusCOIS";
    String selectedRailway="",coachNumber="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        workshop = findViewById(R.id.workshop_boking);
        findCoach = findViewById(R.id.find_coach);
        coachNumberEditText = findViewById(R.id.coach_number_edit_text);
        owningRailwaySpinner = findViewById(R.id.owning_railway_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,owningRailwayList);
        owningRailwaySpinner.setAdapter(adapter);
        takeInputLayout = findViewById(R.id.take_input_form_layout);
        workshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                workshop.setVisibility(View.GONE);
                takeInputLayout.setVisibility(View.VISIBLE);
            }
        });
        owningRailwaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedRailway = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedRailway = adapterView.getItemAtPosition(0).toString();
            }
        });
        findCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()) {
                    if (pd==null){
                        pd = new ProgressDialog(MainActivity.this);
                    }
                    pd.setTitle("Loading...");
                    pd.setMessage("Just a Sec");
                    pd.setCancelable(false);
                    pd.show();
                    //requestQueue = VolleySingleton.getInstance(MainActivity.this).getRequestQueue();
                    String fullURL = SERVER_URL+"&"+"coachnumber="+coachNumber+"&"+"owningrly="+selectedRailway;
                    //Toast.makeText(MainActivity.this, fullURL, Toast.LENGTH_LONG).show();
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, fullURL, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            if (response.toString().isEmpty()){
                                Toast.makeText(MainActivity.this,"No Coach Found",Toast.LENGTH_LONG).show();
                            }
                            else {
                                JSONArray jarray = null;
                                JSONObject jsonobject = null;
                                try{
                                    jarray = response.getJSONArray("Coach_Details");
                                    for (int i=0;i<jarray.length();i++){
                                        jsonobject = jarray.getJSONObject(i);
                                        //String coachnumber = jsonobject.getString("COACH_NUMBER");
                                        //String division=jsonobject.getString("COACH_CURRENT_DIVISION_CODE");;
                                        //TODO: Fill below details with appropriate data;
                                        String POH = "POH";
                                        String PDate = "DD/MM/YY";
                                        String RDate = "DD/MM/YY";
                                        String IOHDate = "DD/MM/YY";
                                        String Shop = "Place";

                                        //TODO: create intent to next activity by sending above data
                                        /*Intent workshopIntent = new Intent(MainActivity.this,WorkshopActivity.class);
                                        workshopIntent.putExtra("coachnumber","coachnumber");
                                        workshopIntent.putExtra("division","division");
                                        workshopIntent.putExtra("poh",POH);
                                        workshopIntent.putExtra("pdate",PDate);
                                        workshopIntent.putExtra("rdate",RDate);
                                        workshopIntent.putExtra("iohdate",IOHDate);
                                        workshopIntent.putExtra("shop",Shop);
                                        startActivity(workshopIntent);*/

                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            error.printStackTrace();

                            Log.i("Response Error", error.toString());

                            Toast.makeText(MainActivity.this,  "Error in retrieving given Coach details", Toast.LENGTH_LONG).show();

                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json, text/plain, */*");
                            return params;
                        }
                    };

                    //Adding JSONRequest to queue
                    //requestQueue.add(jsonObjectRequest);
                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);



                    //TODO: Remove the below code, this is for checking without volley connection
                    String POH = "POH";
                    String PDate = "DD/MM/YY";
                    String RDate = "DD/MM/YY";
                    String IOHDate = "DD/MM/YY";
                    String Shop = "Place";


                    final Intent workshopIntent = new Intent(MainActivity.this,WorkshopActivity.class);
                    workshopIntent.putExtra("coachnumber","coachnumber");
                    workshopIntent.putExtra("division",selectedRailway);
                    //workshopIntent.putExtra("division","division");
                    workshopIntent.putExtra("poh",POH);
                    workshopIntent.putExtra("pdate",PDate);
                    workshopIntent.putExtra("rdate",RDate);
                    workshopIntent.putExtra("iohdate",IOHDate);
                    workshopIntent.putExtra("shop",Shop);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pd.dismiss();
                            startActivity(workshopIntent);
                        }
                    },4000);

                }
            }
        });

    }

    private boolean validateFields(){
        if (TextUtils.isEmpty(coachNumberEditText.getText().toString().trim())) {
            coachNumberEditText.setError("Please Enter Coach Number");
            return false;
        }
        else if (coachNumberEditText.getText().toString().trim().length() != 5) {
            coachNumberEditText.setError("Please Enter Valid Coach Number");
            return false;
        }
        else{
            coachNumber = coachNumberEditText.getText().toString().trim();
            return true;
        }
    }

}
