package com.example.bpandroid3.mainactivity.account;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bpandroid3.R;
import com.example.bpandroid3.data.LoggedInUser;
import com.example.bpandroid3.httpcomunication.HttpClient;
import com.example.bpandroid3.httpcomunication.JsonArrayClass;

public class AccountFragment extends Fragment {

    private TextView email;
    private TextView type;

    private TextView nameLastname;
    private TextView phone;
    private TextView bornDate;
    private TextView bornNumber;
    private TextView address;
    private TextView insComp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        new MyDownloadTask().execute();
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        email = root.findViewById(R.id.text_email);
        type = root.findViewById(R.id.text_type);
        nameLastname = root.findViewById(R.id.text_namelastname);
        phone = root.findViewById(R.id.text_phone);
        bornDate = root.findViewById(R.id.text_borndate);
        bornNumber = root.findViewById(R.id.text_bornnumber);
        address = root.findViewById(R.id.text_adress);
        insComp = root.findViewById(R.id.text_inscomp);

        return root;
    }

    private void setViewModelTexts(JsonArrayClass json)
    {
        email.setText(LoggedInUser.getEmail());
        type.setText(LoggedInUser.getRole());

        nameLastname.setText((json.getElement(0, "priezvisko") + " "
            +json.getElement(0, "meno")));
        phone.setText(("tel: " + json.getElement(0, "telefon")));
        bornDate.setText(("nar: " + json.getElement(0, "datum_narodenia")));
        bornNumber.setText(("r.č: " + json.getElement(0, "rodne_cislo")));
        address.setText((json.getElement(0, "ulica") + " "
                +json.getElement(0, "cislo") + ", " + json.getElement(0, "mesto")));
        insComp.setText(json.getElement(0, "zdravotna_poistovna"));
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
                httpClient.sendGet("employee/nf/"+LoggedInUser.getId(), LoggedInUser.getToken(), LoggedInUser.getId());
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
                setViewModelTexts(json);
            }
        }
    }

}
