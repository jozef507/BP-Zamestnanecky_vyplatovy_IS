package com.example.bpandroid3.mainactivity.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.example.bpandroid3.R;
import com.example.bpandroid3.data.LoggedInUser;
import com.example.bpandroid3.data.RelationD;
import com.example.bpandroid3.httpcomunication.HttpClient;
import com.example.bpandroid3.httpcomunication.JsonArrayClass;
import com.example.bpandroid3.mainactivity.MainActivity;
import com.example.bpandroid3.relationwages.RelationWagesActivity;
import com.example.bpandroid3.ui.login.LoginActivity;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    ArrayList<RelationD> relationDS;
    LinearLayout linearLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        new MyDownloadTask().execute();

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        linearLayout = root.findViewById(R.id.linearlayout);

        return root;
    }

    private void setRelationModels(JsonArrayClass json)
    {
        relationDS = new ArrayList<>();

        for (int i = 0; i<json.getSize(); i++)
        {
            RelationD relationD = new RelationD();
            relationD.setId(json.getElement(i, "id"));
            relationD.setType(json.getElement(i, "typ"));
            relationD.setFrom(json.getElement(i, "nice_date1"));
            relationD.setTo(json.getElement(i, "nice_date2"));
            relationD.setConditionsID(json.getElement(i, "ppv_id"));
            relationD.setPositionName(json.getElement(i, "po_nazov"));
            relationD.setPlaceName(json.getElement(i, "pr_nazov"));
            relationDS.add(relationD);
        }
    }

    private void createButtons()
    {
        int i = 0;

        for(RelationD r : relationDS)
        {
            final Button button = (Button) getLayoutInflater().inflate(R.layout.my_template, null);
            button.setId(i);
            button.setText((r.getType() + "\n" + r.getPlaceName() + "\n"  + r.getPositionName()));
            final int finalI = i;
            final RelationD finalR = r;
            button.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), (finalR.getType() + "" +finalI), Toast.LENGTH_SHORT).show();
                    loadMainActivity(finalR);
                }
            }));

            linearLayout.addView(button);
            i++;
        }
    }


    private void loadMainActivity(RelationD relationD)
    {
        Intent MainIntent = new Intent(getContext(), RelationWagesActivity.class);
        MainIntent.putExtra("conditionsID", relationD.getConditionsID());
        startActivity(MainIntent);
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
                httpClient.sendGet("relation/emp_rel_cons_po_pl_of_emp/"+ LoggedInUser.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
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
                Toast.makeText( getActivity(),"Problem v jadre aplikácie. Konntaktujte vyvojarov!", Toast.LENGTH_LONG).show();
            }
            else if (a.getResponseCode()!=200)
            {
                JsonArrayClass json = new JsonArrayClass(a.getResponse());
                String message = json.getElement(0, "message") ;
                Toast.makeText( getActivity(),message, Toast.LENGTH_LONG).show();
            }
            else
            {
                JsonArrayClass json = new JsonArrayClass(a.getResponse());
                setRelationModels(json);
                createButtons();
            }
        }
    }
}
