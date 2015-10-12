package itaas.com.retrievenetworkdata;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ListFragment extends Fragment {
    private static ArrayAdapter<String> listAdapter;
    List<String> displayedList;
    public ListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            ///do something to load data
            new FetchDeviceDataTask().execute();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String[] data = {"nil","nil","nil","nil","nil","nil","nil"};
        displayedList = new ArrayList<>(Arrays.asList(data));

        listAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_device,
                R.id.list_item_text_info,
                displayedList);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView listView = (ListView)rootView.findViewById(R.id.listview_devices);
        listView.setAdapter(listAdapter);

        return rootView;
    }


    public class FetchDeviceDataTask extends AsyncTask<Void, Void, String> {
        private final String LOG_TAG = FetchDeviceDataTask.class.getSimpleName();

        //method to parse xml of inputStream
        private void parseXml(InputStream buffer){
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            try {
                dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(buffer);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("item");
                displayedList.clear();
                for (int i = 0; i < nList.getLength(); i++) {
                    displayedList.add(nList.item(i).getTextContent());
                    Log.w(LOG_TAG, "got data in loop: "+i);
                }
            }
            catch (ParserConfigurationException | SAXException | IOException e) {
                Log.w(LOG_TAG, e.getMessage());
            }

        }


        @Override
        protected String doInBackground(Void... params) {


            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {

                // Construct the URL for the xml query
                URL url = new URL("http://192.168.33.56:8080/");


                // Create the request to 192.168.33.56:8080, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();


                parseXml(inputStream);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error: "+ e.getMessage());
                // If the code didn't successfully get the data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }


            return "";
        }

        //get some dummy result so that this method is executed
        //without conflicts.
        @Override
        protected void onPostExecute(String result){

            listAdapter.notifyDataSetChanged();//Notifying the adapter to refresh

        }
    }
}

