package itaas.com.retrievenetworkdata;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

/**
 * This class extends BroadcastReceiver so implement some specific functionality
 * in onReceive method
 */
public class WifiBroadcastReceiver extends BroadcastReceiver {
    private static Context cxt;
    private static Intent intent;
    private boolean mainActivityIsRunning = false;
    MainActivity mainAct = null;
    private boolean firstTimeRun;

    public WifiBroadcastReceiver(){

    }

    public WifiBroadcastReceiver(Context cxt,Intent intent){
        this.cxt = cxt;
        this.intent = intent;
        mainActivityIsRunning = true;//once this class is constructed in MainActivity, this statement
        // tells this class that main activity is running so that it won't restart MainActivity
        //unnecessarily while its running
    }

    public void onReceive(final Context context, final Intent itnt) {
        cxt = context;
        intent = itnt;
        NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);

        //if network changed or wifi is changed then this if statement gets executed
        if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)
                || intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {

            if(networkInfo.isConnected()) {
                //If MainActivity is not running ( when the app is closed or not yet started)
                //then this if statement gets executed.
                if(!mainActivityIsRunning){
                    // Wifi is connected
                    Toast.makeText(context, "Wifi is Connected", Toast.LENGTH_SHORT).show();
                    //Log.w("BroadcastActions", "Activity not running..Starting Activity");
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                    String ssid = wifiInfo.getSSID();
                    //Log.w("BroadcastActions", "" + ssid);
                    if(ssid.equals("\"NETGEAR\"")){
                        Toast.makeText(context, "Required SSID found", Toast.LENGTH_SHORT).show();
                        //Log.w("BroadcastActions", "Required Wifi found");

                        //Creating the intent to start the MainActivity
                        Intent intent = cxt.getPackageManager().getLaunchIntentForPackage("itaas.com.retrievenetworkdata");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        cxt.startActivity(intent);
                    }
                    else{//Same as parent if.
                        Toast.makeText(context, "Required SSID not found... Found: "+ssid+"\n"+
                                "Started App Anyways...", Toast.LENGTH_LONG).show();
                        //Log.w("BroadcastActions", "Required Wifi not found. Found: "+ssid);

                        //Basically if particular SSID is not found then the
                        //MainActivity shouldn't start.
                        //We can implement to set the admin SSID in settings options
                        //menu and pass it to this class to check for it.

                        //Here we start the MainActivity anyways w/o checking for particular SSID.
                        Intent intent = cxt.getPackageManager().getLaunchIntentForPackage("itaas.com.retrievenetworkdata");
                        cxt.startActivity(intent);
                        mainActivityIsRunning = true;
                    }
                }
                //Log.d("Inetify", "Wifi is connected: ");
                //Log.w("Inetify", "Wifi is connected");
            }

            /*int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,-1);
            if(state == WifiManager.WIFI_STATE_DISABLED){
                Toast.makeText(context, "Wifi is Disconnected", Toast.LENGTH_SHORT).show();
                Log.w("Inetify", "Wifi is Disconnected");
                Log.d("Inetify", "Wifi is disconnected: ");
            }  */

        } /*else if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI &&
                    ! networkInfo.isConnected()) {
                // Wifi is disconnected
                Toast.makeText(context, "Wifi is Disconnected", Toast.LENGTH_SHORT).show();
                Log.w("Inetify", "Wifi is disconnected: " + String.valueOf(networkInfo));
            }
        }  */


    }


    /*public void onReceive(Context context,Intent itnt){
        cxt = context;
        intent = itnt;
        int state;
        String action = intent.getAction();
        //Log.w("Broadcast receiver", action);
        switch (action){

            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,-1);
                //Log.w("BroadcastActions", "Inside case 1");


                if(state == WifiManager.WIFI_STATE_ENABLED) {
                    Toast.makeText(context, "Wifi is Enabled", Toast.LENGTH_SHORT).show();
                    // Log.w("BroadcastActions", "Wifi is on");
                }

                if(state == WifiManager.WIFI_STATE_DISABLED){
                    Toast.makeText(context, "Wifi is Disabled", Toast.LENGTH_SHORT).show();
                    //Log.w("BroadcastActions", "Wifi is off");
                }
                break;

            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
               // Log.w("BroadcastActions", "Inside case 2");
                //if main activity is not running,
                //then only we need to start the app
                if(!mainActivityIsRunning){
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if(info != null){
                        if(info.isConnected()){
                            // Log.w("BroadcastActions", "Network is connected");
                            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                            String ssid = wifiInfo.getSSID();
                            // Log.w("BroadcastActions", "" + ssid);
                            if(ssid.equals("\"NETGEAR\"")){
                                Toast.makeText(context, "Required SSID found", Toast.LENGTH_SHORT).show();
                                // Log.w("BroadcastActions", "Required Wifi found");
                                Intent intent = cxt.getPackageManager().getLaunchIntentForPackage("itaas.com.retrievenetworkdata");
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                cxt.startActivity(intent);
                            }
                            else{
                                Toast.makeText(context, "Required SSID not found. Found: "+ssid+"\n"+
                                        "Starting App Anyways...", Toast.LENGTH_SHORT).show();
                                //Log.w("BroadcastActions", "Required Wifi not found. Found: "+ssid);
                                Intent intent = cxt.getPackageManager().getLaunchIntentForPackage("itaas.com.retrievenetworkdata");
                                cxt.startActivity(intent);
                                mainActivityIsRunning = true;
                            }
                        }
                    }
                    else{
                        Toast.makeText(context, "Network info=null", Toast.LENGTH_SHORT).show();
                        //Log.w("BroadcastActions", "Network info is null");
                    }
                }
                mainActivityIsRunning = false;
                break;
            }
    }*/
}
