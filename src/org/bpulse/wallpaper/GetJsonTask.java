package org.bpulse.wallpaper;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ben on 26/03/2016.
 */
public class GetJsonTask extends AsyncTask<String, Void, JSONArray> {

    @Override
    protected JSONArray doInBackground(String... strings) {
        try {
            String url = strings[0];

            // Create a new HTTP Client
            DefaultHttpClient defaultClient = new DefaultHttpClient();
            // Setup the get request
            HttpGet httpGetRequest = new HttpGet(url);

            // Execute the request in the client
            HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
            // Grab the response
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "UTF-8"));

            StringBuffer stringBuffer = new StringBuffer();
            String line = null;

            while ((line = reader.readLine()) != null) {
                stringBuffer.append(line);//.append("\n");
            }

            String json = stringBuffer.toString();

            // Instantiate a JSON object from the request response
            return new JSONArray(json);
            //JSONObject jsonObject = new JSONObject(json);
            //JSONArray jsonArray = new JSONArray(json);
            //return jsonArray.getJSONObject(0);
            //return jsonObject;

        } catch (Exception e) {
            // In your production code handle any errors and catch the individual exceptions
            e.printStackTrace();
            return null;
        }
    }
}
