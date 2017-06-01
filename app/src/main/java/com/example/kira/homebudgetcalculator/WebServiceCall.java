package com.example.kira.homebudgetcalculator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Created by pc on 18/3/2017.
 */

public class WebServiceCall {
    JSONObject jsonObj;
    String strUrl = "";

    public WebServiceCall()
    {
        jsonObj = null;
        strUrl = "http://192.168.0.174:8080/homeBudget/budget.php";
    }

    public String fnGetURL()
    {
        return strUrl;
    }

    public String makeHttpRequest(String url, String method, List<NameValuePair> params) {
        InputStream is = null;
        String json = "";
        //JSONObject jObj = null;
        //Making HTTP request

        try {
            //check for request method
            if (method == "POST") {
                //request method is POST
                //defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            } else if (method == "GET") {
                //request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();

            //jObj = new JSONObject(json);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //return JSON String
        return json;

    }
}
