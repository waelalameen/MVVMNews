package com.example.apple.myapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.apple.myapplication.newsFragments.NewsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // testApi();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(R.id.container, new NewsFragment())
                .disallowAddToBackStack()
                .commit();
    }

    private void testApi() {
        JSONObject param = new JSONObject();
        try {
            param.put("department_num", "3515513008");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://api.aqsati.net/api/v1/mobile/customer-sub", param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("res", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("err", error.getMessage());
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> header = new HashMap<>();
                header.put("key", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIyIiwibmFtZSI6Ik1vYmlsZSIsImFkbWluIjp0cnVlfQ.BzH-8Y1pIJWWUxszfOMg636wCXuiZLuGvY2oNjKq_qs");
                return header;
            }

            @Override
            public int getMethod() {
                return super.getMethod();
            }

            @Override
            protected String getParamsEncoding() {
                return super.getParamsEncoding();
            }

            @Override
            public Cache.Entry getCacheEntry() {
                return super.getCacheEntry();
            }

            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }

            @Override
            public int getTrafficStatsTag() {
                return super.getTrafficStatsTag();
            }

            @Override
            public byte[] getBody() {
                return super.getBody();
            }

            @Override
            public String getUrl() {
                return super.getUrl();
            }

            @Override
            public String getCacheKey() {
                return super.getCacheKey();
            }

            @Override
            public Object getTag() {
                return super.getTag();
            }

            @Override
            public RetryPolicy getRetryPolicy() {
                return super.getRetryPolicy();
            }

            @Override
            public String getBodyContentType() {
                return super.getBodyContentType();
            }
        };

        Singleton.Companion.getInstance().addToRequestQueue(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
