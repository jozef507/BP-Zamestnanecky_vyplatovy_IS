package com.example.bpandroid3.mainactivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.bpandroid3.R;
import com.example.bpandroid3.data.LoggedInUser;
import com.example.bpandroid3.httpcomunication.HttpClient;
import com.example.bpandroid3.httpcomunication.JsonArrayClass;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    public void onStop()
    {
        new MyUploadTask().execute();
        super.onStop();
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

            HttpClient ht = new HttpClient();
            try {
                ht.sendPost("auth/logout", LoggedInUser.getToken(), LoggedInUser.getId());
                return ht;
            }catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(HttpClient a) {
            if(a==null)
            {
                Toast.makeText( getApplicationContext(),"Problem v jadre aplikácie. Konntaktujte vyvojarov!", Toast.LENGTH_LONG).show();
            }
            else if (a.getResponseCode()!=200)
            {
                JsonArrayClass json = new JsonArrayClass(a.getResponse());
                String message = json.getElement(0, "message") ;
                Toast.makeText( getApplicationContext(),message, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText( getApplicationContext(),"Boli ste odhlásený!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

}
