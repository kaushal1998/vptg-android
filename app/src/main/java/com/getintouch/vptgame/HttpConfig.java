package com.getintouch.vptgame;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dell on 09-03-2018.
 */

public class HttpConfig {
    private Map<String, String> body;
    public static final String POST = "POST";
    public static final String GET = "GET";
    private HttpURLConnection httpURLConnection = null;
    private BufferedWriter bufferedWriter = null;
    private BufferedReader bufferedReader = null;
    URL surl;
    int REQUEST_TIMEOUT = 0, RESPONSE_TIMEOUT = 0;
    String RequestType = null;
    ResponseListener responseListener;
    Context context;


    public HttpConfig(Context context, String url, String RequestType, ResponseListener responseListener) {
        try {
            surl = new URL(url);
            this.RequestType = RequestType;
            this.responseListener = responseListener;
            this.context = context;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public HttpConfig(Context context, String url, String RequestType, ResponseListener responseListener, int requestTimeountInSeconds, int responseTimeountInSeconds) {
        try {
            surl = new URL(url);
            this.RequestType = RequestType;
            this.responseListener = responseListener;
            this.REQUEST_TIMEOUT = requestTimeountInSeconds * 1000;
            this.RESPONSE_TIMEOUT = responseTimeountInSeconds * 1000;
            this.context = context;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String MapToString(){
        body = addParams();
        String postStatement = "";
        int i=0;
        if (body.size() != 0) {
            for (String key : body.keySet()) {
                try {
                    String sampleStatement = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(body.get(key), "UTF-8");
                    postStatement += sampleStatement;
                    if (i != body.size() - 1) {
                        postStatement += "&";
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                i++;
            }
        }
        return postStatement;
    }

    public Map<String, String> addParams() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void run(){
        HttpConfigTask httpConfigTask = new HttpConfigTask(MapToString());
        httpConfigTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public class HttpConfigTask extends AsyncTask<String, Void, String>{

        String post_data;

        public HttpConfigTask(String post_data) {
            this.post_data = post_data;
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (RequestType.equals(POST)) {
                    httpURLConnection = (HttpURLConnection) surl.openConnection();
                    httpURLConnection.setRequestMethod(RequestType);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setConnectTimeout(REQUEST_TIMEOUT);
                    httpURLConnection.setReadTimeout(RESPONSE_TIMEOUT);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    bufferedWriter.write(post_data);
                    bufferedWriter.close();
                    outputStream.flush();
                    outputStream.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    return result;
                }else if(RequestType.equals(GET)) {
                    httpURLConnection = (HttpURLConnection) surl.openConnection();
                    httpURLConnection.setRequestMethod(RequestType);
                    httpURLConnection.setConnectTimeout(REQUEST_TIMEOUT);
                    httpURLConnection.setReadTimeout(RESPONSE_TIMEOUT);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                    String result = "";
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    return result;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                responseListener.onResponseReceived(s);
            }catch(Exception e) {
                responseListener.onErrorReceived();
            }
        }
    }
}
