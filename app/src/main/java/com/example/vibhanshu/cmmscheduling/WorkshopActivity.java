package com.example.vibhanshu.cmmscheduling;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class WorkshopActivity extends AppCompatActivity {

    TextView coachNumberT,divisionT, pohT, pDateT, rDateT, iohDateT, shopT, dateTimeT;
    Spinner reasonS;
    ImageView calImage;
    TextInputEditText remarkE;
    Button submit;

    String coachNumber, division, poh, pDate, rDate, iohDate, shop, reason="", remark="";
    int mYear=0, mMonth=0, mDay=0, mHour=0, mMin=0;

    //For Reason Spinner
    String[] reasonsString = {"Book for MID Life", "Rehabilitation", "Due for IOH", "Due for Special Lift", "For C Schedule", "For Special Repair"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workshop);
        coachNumberT = findViewById(R.id.coach_number);

        //Finding ids
        divisionT = findViewById(R.id.maintenance_depot);
        pohT = findViewById(R.id.poh);
        pDateT = findViewById(R.id.p_date);
        rDateT = findViewById(R.id.r_date);
        iohDateT = findViewById(R.id.ioh_date);
        shopT = findViewById(R.id.shop);
        reasonS = findViewById(R.id.reason_list);
        calImage = findViewById(R.id.cal_image);
        dateTimeT = findViewById(R.id.cal_textview);
        remarkE = findViewById(R.id.scheduling_remark);
        submit = findViewById(R.id.submit);

        //Taking result from back activity
        Intent result = getIntent();
        coachNumber = result.getStringExtra("coachnumber");
        division = result.getStringExtra("division");
        poh = result.getStringExtra("poh");
        pDate = result.getStringExtra("pdate");
        rDate = result.getStringExtra("rdate");
        iohDate = result.getStringExtra("iohdate");
        shop = result.getStringExtra("shop");

        //SetTexts to blank fields
        coachNumberT.setText(coachNumber);
        divisionT.setText(division);
        pohT.setText(poh);
        pDateT.setText(pDate);
        rDateT.setText(rDate);
        iohDateT.setText(iohDate);
        shopT.setText(shop);

        //Making String for Reason Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,reasonsString);
        reasonS.setAdapter(adapter);
        reasonS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                reason = adapterView.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                reason = adapterView.getItemAtPosition(0).toString();
            }
        });

        //Working on date & time picker
        calImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                final int hour = cal.get(Calendar.HOUR_OF_DAY);
                final int min = cal.get(Calendar.MINUTE);
                dateTimeT.setText(mDay+"/"+mMonth+"/"+mYear+"   "+mHour+":"+mMin);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        WorkshopActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int yr, int moy, int dom) {
                                mYear = yr;
                                mMonth = moy+1;
                                mDay = dom;
                                dateTimeT.setText(mDay+"/"+mMonth+"/"+mYear+"   "+mHour+":"+mMin);

                                TimePickerDialog timePickerDialog = new TimePickerDialog(
                                        WorkshopActivity.this,
                                        new TimePickerDialog.OnTimeSetListener() {
                                            @Override
                                            public void onTimeSet(TimePicker timePicker, int hr, int mi) {
                                                mHour = hr;
                                                mMin = mi;
                                                dateTimeT.setText(mDay+"/"+mMonth+"/"+mYear+"   "+mHour+":"+mMin);
                                            }
                                        },
                                        hour,
                                        min,
                                        true
                                );
                                timePickerDialog.show();
                            }
                        },
                        year,
                        month,
                        day
                );
                datePickerDialog.show();
            }
        });

        //Getting Texts from Remark (if any)
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                remark = remarkE.getText().toString().trim();
                if(mMin==0 && mHour == 0 && mDay == 0 && mMonth == 0 && mYear == 0)
                    Toast.makeText(WorkshopActivity.this,"Please Select a date",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(WorkshopActivity.this,"Reason:"+reason+"\nbooking:"+dateTimeT.getText().toString()+"\nRemark:"+remark,Toast.LENGTH_LONG).show();
            }
        });

    }
}
