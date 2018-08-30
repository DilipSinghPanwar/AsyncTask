package com.androiddevs;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog mProgressDialog;
    private TextView mTvJsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTvJsonData = (TextView) findViewById(R.id.tvJsonData);

        getPOSTRequestJson("");
//        getGetRequestJson("https://jsonplaceholder.typicode.com/users/1/todos");
    }

    void getGetRequestJson(String api) {
        Log.e("GET", ">>");
        // AsyncTask Class for http request in background
        class GetJsonRequest extends AsyncTask<String, Void, String> {

            StringBuilder sb = new StringBuilder();
            private HttpURLConnection httpConn;

            //method for pre tasks
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //start progress dialog
                mProgressDialog = ProgressDialog.show(MainActivity.this, null, "Please wait...", true, true);
            }

            //method for background tasks
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                String json;
                try {

                    URL url = new URL(uri);
                    httpConn = (HttpURLConnection) url.openConnection();
                    httpConn.setUseCaches(false);
                    //       httpConn.setRequestMethod("GET");
                    httpConn.setDoInput(true); // true if we want to read server's response
                    httpConn.setDoOutput(false); // false indicates this is a GET request
                    httpConn.setConnectTimeout(15000);
                    httpConn.setReadTimeout(15000);
                    httpConn.connect();

                    InputStream inputStream = new BufferedInputStream(httpConn.getInputStream());

                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    //getting input-stream
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    //return input-stream
                    return sb.toString().trim();
                } catch (Exception e) {
                    return e.toString();
                } finally {
                    httpConn.disconnect();
                }
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                mProgressDialog.dismiss();
                Log.e("API >>> RESPONSE >>>>", response);
                mTvJsonData.setText(response);
            }
        }
        GetJsonRequest getReq = new GetJsonRequest();
        getReq.execute(api);
    }

    void getPOSTRequestJson(String api) {
        // AsyncTask Class for http request in background
        class PostJsonRequest extends AsyncTask<String, Void, String> {
            StringBuilder sb;

            //method for pre tasks
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //start progress dialog
                mProgressDialog = ProgressDialog.show(MainActivity.this, null, "Please wait...", true, true);
            }

            //method for background tasks
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                 /*   // METHOD ONE
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                    con.setDoInput(true);
                    con.setDoOutput(true);

                    OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                    writer.write("user_id=1&user_language=Hindi&device_id=353327069148898&is_guest=2");
                    writer.flush();
                    writer.close();
                    con.connect();*/

                    // METHOD TWO
                    Map<String, Object> paramss = new LinkedHashMap<>();
                    paramss.put("phone", "+65 974024444");
                    paramss.put("deviceToken", "353327069148898");
//                    paramss.put("device_id","353327069148898");
//                    paramss.put("is_guest","2");

                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String, Object> param : paramss.entrySet()) {
                        if (postData.length() != 0) postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    String urlParameters = postData.toString();

                    URL url = new URL(uri);
                    URLConnection con = url.openConnection();
//                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.setDoOutput(true);
                    OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());

                    writer.write(urlParameters);
                    writer.flush();
                    writer.close();
                    con.connect();

                    // METHOD THREE
//                    URL url = new URL(uri);
//                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
//                    con.setReadTimeout(15000);
//                    con.setConnectTimeout(15000);
//                    con.setRequestMethod("POST");
//                    con.setDoInput(true);
//                    con.setDoOutput(true);
//
//                    Uri.Builder builder = new Uri.Builder()
//                            .appendQueryParameter("postId", "20");
////                            .appendQueryParameter("password", "qwerty");
////                            .appendQueryParameter("device_id", "353327069148898")
////                            .appendQueryParameter("is_guest", "2");
//                    String query = builder.build().getEncodedQuery();
////                    String query = builder.build().toString();
//
//                    OutputStream os = con.getOutputStream();
//                    BufferedWriter writer = new BufferedWriter(
//                            new OutputStreamWriter(os, "UTF-8"));
//                    writer.write(query);
//                    writer.flush();
//                    writer.close();
//                    os.close();
//                    con.connect();
//


                    //builder object to store input-stream
                    InputStream stream = con.getInputStream();
                    sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    String json;
                    //getting input-stream
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    Log.e(TAG, "doInBackground: >>" + sb);
                    //return input-stream
                    return sb.toString().trim();
                } catch (Exception e) {
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String response) {
                super.onPostExecute(response);
                mProgressDialog.dismiss();
                Log.e("API >>> RESPONSE >>>>", response);
                mTvJsonData.setText(response);
            }
        }
        PostJsonRequest postReq = new PostJsonRequest();
        postReq.execute(api);
    }
}
