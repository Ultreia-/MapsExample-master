package com.marvin.mapsexample;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.marvin.mapsexample.HelperPackage.RestCallbackInterface;
import com.marvin.mapsexample.HelperPackage.RestServer;

import org.json.JSONObject;

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

        public void onEndRequest(JSONObject result)
        {

        }
    }
}
