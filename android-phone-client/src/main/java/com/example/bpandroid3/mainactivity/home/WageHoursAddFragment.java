package com.example.bpandroid3.mainactivity.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bpandroid3.R;
import com.example.bpandroid3.data.HoursD;
import com.example.bpandroid3.data.LoggedInUser;
import com.example.bpandroid3.data.WageD;
import com.example.bpandroid3.httpcomunication.HttpClient;
import com.example.bpandroid3.httpcomunication.JsonArrayClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WageHoursAddFragment extends Fragment {

    String conditionsID;
    WageD wageD;
    HoursD hoursD;

    private LinearLayout dateBox;
    private LinearLayout timeBox;
    private LinearLayout emergencyBox;
    private LinearLayout overtimeBox;
    private LinearLayout performanceBox;
    private LinearLayout partBox;

    private TextView label;
    private TextView date;
    private EditText timeFrom;
    private EditText timeTo;
    private EditText overtime;
    private EditText performance;
    private EditText part;

    private Spinner emergencyType;

    private Button button;

    private DatePickerDialog.OnDateSetListener dateSetListener;
    private static final String TAG = "WageHoursAddActivity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_wage_hours_add, container, false);

        Bundle arguments = getArguments();

        wageD = new WageD();
        wageD.setId(arguments.getString("zm_id"));
        wageD.setTimeImportant(arguments.getString("nutne_evidovanie_casu"));
        wageD.setEmergencyImportant(arguments.getString("mozne_evidovanie_pohotovosti"));
        wageD.setWageFormName(arguments.getString("nazov"));
        conditionsID = arguments.getString("conditionsID");

        dateBox = root.findViewById(R.id.date_box);
        timeBox = root.findViewById(R.id.time_box);
        emergencyBox = root.findViewById(R.id.emergency_box);
        overtimeBox = root.findViewById(R.id.overtime_box);
        performanceBox = root.findViewById(R.id.performance_box);
        partBox = root.findViewById(R.id.part_box);

        label = root.findViewById(R.id.label);
        date = root.findViewById(R.id.date);
        timeFrom = root.findViewById(R.id.time_from);
        timeTo = root.findViewById(R.id.time_to);
        overtime = root.findViewById(R.id.overtime);
        performance = root.findViewById(R.id.performance);
        part = root.findViewById(R.id.part);

        emergencyType = root.findViewById(R.id.emergency_type);
        button = root.findViewById(R.id.button);

        setCalendar();
        setEnability();
        setButton();

        return root;
    }

    private void setCalendar()
    {
        date.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day){
                Log.d(TAG, "onDateSet: dd.mm.yyyy: " + day + "." + (++month) +"."+ year);

                String dat = day+"."+month+"."+year;
                date.setText(dat);
            }
        };
    }

    private void setEnability()
    {
        if(wageD.getTimeImportant().equals("nie"))
        {
            timeFrom.setVisibility(View.GONE);
            timeTo.setVisibility(View.GONE);
            emergencyType.setVisibility(View.GONE);
            overtime.setVisibility(View.GONE);
            timeBox.setVisibility(View.GONE);
        }

        if(wageD.getEmergencyImportant().equals("nie"))
            emergencyType.setVisibility(View.GONE);

        if(!wageD.getWageFormName().contains("podielová"))
        {
            partBox.setVisibility(View.GONE);
            part.setVisibility(View.GONE);
        }


        if(!wageD.getWageFormName().contains("výkonová"))
        {
            performanceBox.setVisibility(View.GONE);
            performance.setVisibility(View.GONE);
        }

    }

    private void setButton()
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                label.setVisibility(View.INVISIBLE);

                if(!checkFormular()) return;
                if(!checkFormularTypes()) return;


                setModelsFromInputs();
                new MyUploadTask().execute();
            }
        });
    }


    public boolean checkFormular()
    {

        boolean flag = true;
        if(date.getVisibility() != View.GONE && (date.getText() == null || date.getText().toString().trim().isEmpty()))
            flag=false;
        else if(timeFrom.getVisibility() != View.GONE && (timeFrom.getText() == null || timeFrom.getText().toString().trim().isEmpty()))
            flag=false;
        else if(timeTo.getVisibility() != View.GONE && (timeTo.getText() == null || timeTo.getText().toString().trim().isEmpty()))
            flag=false;
        else if(performance.getVisibility() != View.GONE && (performance.getText() == null || performance.getText().toString().trim().isEmpty()))
            flag=false;
        else if(part.getVisibility() != View.GONE && (part.getText() == null || part.getText().toString().trim().isEmpty()))
            flag=false;

        if(!flag)
        {
            System.out.println("Nevyplené údaje!");
            label.setText("Nevyplené údaje!");
            label.setVisibility(View.VISIBLE);
        }

        return flag;
    }

    public boolean checkFormularTypes()
    {
        boolean flag = true;
        boolean timeFlag = true;
        if(timeFrom.getVisibility() != View.GONE && !timeFrom.getText().toString().matches("^((0?[0-9])|(1[0-9])|(2[0-3])):(00|15|30|45)$"))
        {
            flag=false;
            timeFlag = false;
        }
        else if(timeTo.getVisibility() != View.GONE && !timeTo.getText().toString().matches("^((0?[0-9])|(1[0-9])|(2[0-3])):(00|15|30|45)$"))
        {
            flag=false;
            timeFlag = false;
        }
        else if(overtime.getVisibility() != View.GONE && !overtime.getText().toString().isEmpty() && !overtime.getText().toString().matches("^((0?[0-9])|(1[0-9])|(2[0-3])):(00|15|30|45)$"))
        {
            flag=false;
            timeFlag = false;
        }
        else if(performance.getVisibility() != View.GONE && !performance.getText().toString().matches("^\\+?-?\\d+(\\.\\d+)?$"))
        {
            flag=false;
        }
        else if(part.getVisibility() != View.GONE && !part.getText().toString().matches("^\\+?-?\\d+(\\.\\d+)?$"))
        {
            flag=false;
        }

        if(!flag)
        {
            if(!timeFlag)
            {
                System.out.println("Časové údaje zaokrúhlite na štvrťhodiny!");
                label.setText("Časové údaje zaokrúhlite na štvrťhodiny!");
            }
            else
            {
                System.out.println("Niektoré vyplnené údaje majú nesprávny formát.");
                label.setText("Niektoré vyplnené údaje majú nesprávny formát.");
            }

            label.setVisibility((View.VISIBLE));
        }

        return flag;
    }

    public void setModelsFromInputs()
    {
        hoursD = new HoursD();
        String dateS = date.getText().toString().trim();
        dateS = convertDateFormat(dateS);

        hoursD.setDate(dateS);

        if(timeFrom.getVisibility() == View.GONE)
            hoursD.setFrom("NULL");
        else
            hoursD.setFrom(timeFrom.getText().toString().trim());

        if(timeTo.getVisibility() == View.GONE)
            hoursD.setTo("NULL");
        else
            hoursD.setTo(timeTo.getText().toString().trim());

        if(emergencyType.getVisibility() == View.GONE)
        {
            hoursD.setEmergencyType("NULL");
        }
        else
        {
            if(emergencyType.getSelectedItem().toString().equals("Žiadná pohotovosť"))
                hoursD.setEmergencyType("NULL");
            else
                hoursD.setEmergencyType(emergencyType.getSelectedItem().toString());
        }

        if(performance.getVisibility() == View.GONE)
            hoursD.setUnitsDone("NULL");
        else
            hoursD.setUnitsDone(performance.getText().toString().trim());

        if(part.getVisibility() == View.GONE)
            hoursD.setPartBase("NULL");
        else
            hoursD.setPartBase(part.getText().toString().trim());

        if(overtime.getVisibility() != View.GONE && !overtime.getText().toString().trim().isEmpty())
        {
            String ot = overtime.getText().toString();
            String[] parts = ot.split(":");
            String part1 = parts[0];
            Double part2 = Double.parseDouble(parts[1]);
            part2 = part2/60.0;
            hoursD.setOverTime(part1+"."+part2);
        }
        else
        {
            hoursD.setOverTime("NULL");
        }
    }

    private String convertDateFormat(String date)
    {
        final String OLD_FORMAT = "d.M.yyyy";
        final String NEW_FORMAT = "yyyy-MM-dd";

        String newDateString;

        SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
        Date d = null;
        try {
            d = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            d = null;
        }
        sdf.applyPattern(NEW_FORMAT);
        newDateString = sdf.format(d);
        return newDateString;
    }

    private class MyUploadTask extends AsyncTask<String, Void, HttpClient>
    {
        private String email;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected HttpClient doInBackground(String... params) {

            try {
                HttpClient ht = new HttpClient();
                ht.addPostParam("consid", conditionsID);
                ht.addPostParam("wageid", wageD.getId());
                ht.addPostParam("date", hoursD.getDate());
                ht.addPostParam("from", hoursD.getFrom());
                ht.addPostParam("to", hoursD.getTo());
                ht.addPostParam("overtime", hoursD.getOverTime());
                ht.addPostParam("units", hoursD.getUnitsDone());
                ht.addPostParam("part", hoursD.getPartBase());
                ht.addPostParam("emergency", hoursD.getEmergencyType());
                ht.sendPost("hours/crt_hrs_by_emp", LoggedInUser.getToken(), LoggedInUser.getId());
                return ht;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(HttpClient a) {
            if(a==null)
            {
                Toast.makeText( getContext(),"Problem v jadre aplikácie. Konntaktujte vyvojarov!", Toast.LENGTH_LONG).show();
            }
            else if (a.getResponseCode()!=200)
            {
                JsonArrayClass json = new JsonArrayClass(a.getResponse());
                String message = json.getElement(0, "message") ;
                Toast.makeText( getContext(),message, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText( getContext(),"Pridanie odpracovaného výkonu prebehlo úspešne!", Toast.LENGTH_LONG).show();
                getParentFragmentManager().popBackStackImmediate();
            }
        }
    }
}
