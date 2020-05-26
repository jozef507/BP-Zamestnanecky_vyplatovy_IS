package com.example.bpandroid3.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bpandroid3.SettingsActivity;
import com.example.bpandroid3.data.LoggedInUser;
import com.example.bpandroid3.mainactivity.MainActivity;
import com.example.bpandroid3.R;
import com.example.bpandroid3.httpcomunication.HttpClient;
import com.example.bpandroid3.httpcomunication.JsonArrayClass;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_READ_CONTACTS = 0;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressBar loadingProgressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = (EditText) findViewById(R.id.username);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.login);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading);

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String UserName = usernameEditText.getText().toString();
                String Pwd = passwordEditText.getText().toString();
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext() /* Activity context */);
                HttpClient.server_url = sharedPreferences.getString("server_address", "");

                new MyDownloadTask().execute(UserName, getMd5(Pwd));
            }
        });
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            loadSettingsActivity();
        }
        return super.onOptionsItemSelected(item);
    }


    private String getMd5(String input)
    { //geeksforgeeks
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadMainActivity()
    {
        Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(MainIntent);
    }

    private void loadSettingsActivity()
    {
        Intent MainIntent = new Intent(LoginActivity.this, SettingsActivity.class);
        startActivity(MainIntent);
    }



    class MyDownloadTask extends AsyncTask<String, Void, HttpClient>
    {
        private String email;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loadingProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected HttpClient doInBackground(String... params) {

            try {
                HttpClient httpClient = new HttpClient();
                httpClient.addPostParam("email", params[0]);
                email = params[0];
                httpClient.addPostParam("password", params[1]);
                httpClient.sendLoginPost();
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
                Toast.makeText(LoginActivity.this,"Problém s pripojením k serveru. Skontrolujte internetové pripojenie!", Toast.LENGTH_LONG).show();
                loadingProgressBar.setVisibility(View.GONE);
            }
            else if (a.getResponseCode()!=200)
            {
                JsonArrayClass json = new JsonArrayClass(a.getResponse());
                String message = json.getElement(0, "message") ;
                Toast.makeText(LoginActivity.this,message, Toast.LENGTH_LONG).show();
                loadingProgressBar.setVisibility(View.GONE);
            }
            else
            {
                JsonArrayClass json = new JsonArrayClass(a.getResponse());

                String id = json.getElement(0, "id") ;
                String email = this.email;
                String token = json.getElement(0, "token") ;
                String role = json.getElement(0, "role") ;

                LoggedInUser.setId(id);
                LoggedInUser.setToken(token);
                LoggedInUser.setEmail(email);
                LoggedInUser.setRole(role);

                Toast.makeText(LoginActivity.this,"Uspešne ste sa prihlásili", Toast.LENGTH_LONG).show();
                loadMainActivity();
                loadingProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
