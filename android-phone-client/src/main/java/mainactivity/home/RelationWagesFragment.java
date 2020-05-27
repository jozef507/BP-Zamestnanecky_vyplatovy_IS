package com.example.bpandroid3.mainactivity.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bpandroid3.R;
import com.example.bpandroid3.data.LoggedInUser;
import com.example.bpandroid3.data.WageD;
import com.example.bpandroid3.httpcomunication.HttpClient;
import com.example.bpandroid3.httpcomunication.JsonArrayClass;

import java.util.ArrayList;

public class RelationWagesFragment extends Fragment {

    private String conditionsID;
    private ArrayList<WageD> wageDS;

    private LinearLayout linearLayout;
    private TextView title;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_relation_wages, container, false);


        Bundle arguments = getArguments();
        conditionsID = arguments.getString("conditionsID");

        title = root.findViewById(R.id.title);
        linearLayout = root.findViewById(R.id.linearlayout);

        new MyDownloadTask().execute();



        return root;
    }

    private void setWagesModels(JsonArrayClass json)
    {
        wageDS = new ArrayList<>();

        for (int i = 0; i<json.getSize(); i++)
        {
            WageD wageD = new WageD();
            wageD.setId(json.getElement(i, "zm_id"));
            wageD.setLabel(json.getElement(i, "popis"));
            wageD.setEmployeeEnter(json.getElement(i, "vykon_eviduje_zamestnanec"));
            wageD.setTimeImportant(json.getElement(i, "nutne_evidovanie_casu"));
            wageD.setEmergencyImportant(json.getElement(i, "mozne_evidovanie_pohotovosti"));
            wageD.setTarif(json.getElement(i, "tarifa_za_jednotku_mzdy"));
            wageD.setPayWay(json.getElement(i, "sposob_vyplacania"));
            wageD.setPayDate(json.getElement(i, "datum_vyplatenia"));
            wageD.setWageFormID(json.getElement(i, "fm_id"));
            wageD.setWageFormName(json.getElement(i, "nazov"));
            wageD.setWageFormUnit(json.getElement(i, "jednotka_vykonu"));
            wageD.setWageFormUnitShort(json.getElement(i, "skratka_jednotky"));
            wageDS.add(wageD);
        }
    }

    private void createButtons()
    {
        int i = 0;

        for(WageD w : wageDS)
        {
            if(w.getEmployeeEnter().equals("áno"))
            {
                final Button button = (Button) getLayoutInflater().inflate(R.layout.my_template, null);
                button.setId(i);
                button.setText(( w.getWageFormName() + "\n" + w.getLabel() + "\n"  + w.getTarif()+"/"+w.getWageFormUnitShort()));
                final int finalI = i;
                final WageD finalW = w;
                button.setOnClickListener((new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadOtherFragment(finalW);
                    }
                }));

                linearLayout.addView(button);
                i++;
            }
        }
    }

    private void loadOtherFragment(WageD wageD)
    {
        WageHoursAddFragment fragment = new WageHoursAddFragment();
        Bundle arguments = new Bundle();
        arguments.putString( "conditionsID" , conditionsID);
        arguments.putString( "zm_id" , wageD.getId());
        arguments.putString( "nutne_evidovanie_casu" , wageD.getTimeImportant().equals("áno") ? "1":"0");
        arguments.putString( "mozne_evidovanie_pohotovosti" ,wageD.getEmergencyImportant().equals("áno") ? "1":"0");
        arguments.putString( "nazov" , wageD.getWageFormName());
        fragment.setArguments(arguments);
        final FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment).addToBackStack("wages").commit();
    }



    private class MyDownloadTask extends AsyncTask<String, Void, HttpClient>
    {
        private String email;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected HttpClient doInBackground(String... params) {

            try {
                HttpClient httpClient = new HttpClient();
                httpClient.sendGet("relation/wgs_of_rltn/"+ conditionsID, LoggedInUser.getToken(), LoggedInUser.getId());
                return httpClient;
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
                JsonArrayClass json = new JsonArrayClass(a.getResponse());
                setWagesModels(json);
                createButtons();
            }
        }
    }
}
