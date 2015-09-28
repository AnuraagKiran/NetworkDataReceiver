package com.itass.ottclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }


    public void LoginClick(View view)
    {
        //Checking credentials directly

        Bundle bundle = new Bundle();

        EditText editText = (EditText) findViewById(R.id.password);
        String editTextData = editText.getText().toString();
        bundle.putString("@string/password", editTextData);

        editText = (EditText) findViewById(R.id.user_name);
        editTextData = editText.getText().toString();
        bundle.putString("@string/user_name", editTextData);

        if(checkCredentials(bundle) == true) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    //Returns true for all values
    //Need to rewrite later
    public boolean checkCredentials(Bundle bundle)
    {
        return true;
    }

}
