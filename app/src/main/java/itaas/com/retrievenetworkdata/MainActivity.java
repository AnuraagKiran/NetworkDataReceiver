package itaas.com.retrievenetworkdata;

import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private WifiBroadcastReceiver wifiBroadcastReceiver = null;
    private boolean receiverRegistered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_activity, new ListFragment())
                    .commit();
        }//Till here normal on create statements.

        //If BroadcastReceiver is not registered
        //this registers the receiver
        if(!receiverRegistered){
            wifiBroadcastReceiver = new WifiBroadcastReceiver(getApplicationContext(),getIntent());
            IntentFilter intFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            intFilter.addAction("android.intent.action.BOOT_COMPLETED");//to register the receiver on boot
            registerReceiver(wifiBroadcastReceiver, intFilter);
            receiverRegistered = true;//once its registered,its set a strue so that
            //it wont be re-registered on calling onCreate again
        }
        //Log.w("Main Activity", "in onCreate");
    }

    @Override
    public void onStop(){
        //Log.w("Main Activity", "in onStop");
        super.onStop();
    }


    @Override
    public void onDestroy(){
        //Log.w("Main Activity", "in onDestroy");
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

}


