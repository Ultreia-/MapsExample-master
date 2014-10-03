package com.marvin.mapsexample;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Tobias on 02-10-2014.
 */
public class restTestScreen extends RestServer {

    TextView etResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rest_test_screen);

        // get reference to the views
        etResponse = (EditText) findViewById(R.id.etResponse);

        /*this.requestPost("http://marvin.idyia.dk/user/postdata",
            new HashMap<String, String>(){{
                put("data", "data-2343657468");
                put("moredata", "moredata-34543476");
            }},
            new RestCallback());*/

        this.requestGet("http://marvin.idyia.dk/user/getdata/95", new RestCallback());
    }

    private class RestCallback implements RestCallbackInterface {

        public void onEndRequest(String result)
        {
            etResponse.setText(result);
        }
    }
}
