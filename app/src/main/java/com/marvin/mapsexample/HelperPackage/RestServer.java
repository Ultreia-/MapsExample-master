package com.marvin.mapsexample.HelperPackage;

/**
 * Created by Tobias on 02-10-2014.
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class RestServer extends Activity {

    public interface RestCallbackInterface {

        void onEndRequest(JSONObject result);
    }

    public void requestGet(String url, final RestCallbackInterface callback) {
            new HttpAsyncTask() {
                @Override
                protected String doInBackground(String... urls) {
                    return GET(urls[0]);
                }
                @Override
                protected void onPostExecute(String result) {
                    JSONObject json = null;
                    try {
                        json = new JSONObject(result);// convert String to JSONObject
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callback.onEndRequest(json);
                }
            }.execute(url);
    }

    public void requestPost(String url, final HashMap data, final RestCallbackInterface callback) {
        new HttpAsyncTask() {
            @Override
            protected String doInBackground(String... urls) {
                return POST(urls[0], data);
            }
            @Override
            protected void onPostExecute(String result) {
                JSONObject json = null;
                try {
                    json = new JSONObject(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onEndRequest(json);
            }
        }.execute(url);
    }

    public static String GET(String url){

        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            result = executeHttpRequest(httpResponse);

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public static String POST(String url, HashMap data){

        String result = "";
        HttpPost httppost = new HttpPost(url);

        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);

            Iterator iter = data.keySet().iterator();
            while(iter.hasNext())
            {
                String key = (String) iter.next();
                String val = (String) data.get(key);

                nameValuePairs.add(new BasicNameValuePair(key, val));
            }

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse httpResponse = httpclient.execute(httppost);

            result = executeHttpRequest(httpResponse);

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String executeHttpRequest(HttpResponse httpResponse) throws Exception{
        InputStream inputStream = null;
        String result = "";

        // receive response as inputStream
        inputStream = httpResponse.getEntity().getContent();

        // convert inputstream to string
        if(inputStream != null)
            result = convertInputStreamToString(inputStream);
        else
            result = "Did not work!";

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_SHORT).show();
        }
    }
}
