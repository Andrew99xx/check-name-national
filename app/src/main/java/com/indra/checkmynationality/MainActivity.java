package com.indra.checkmynationality;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    Locale locale;
    public TextView txtview;
    public TextView percentage;
    public EditText editTxt;
    public CountryMap cMap;
    public TextView warning;
    
    void init(){
        txtview=findViewById(R.id.textView2);
        percentage=findViewById(R.id.textView3);
        editTxt=findViewById(R.id.editText);
        warning=findViewById(R.id.warning);
        cMap=new CountryMap();
        cMap.setCountryCodes();
    }
    public void callAPI() {


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String token=editTxt.getText().toString();
        if(token.contains("")){
            warning.setText("Please do not provide empty name");
            return;

        } else if (token.contains(" ")) {
            warning.setText(("Please do not provide space in name"));
            return;

        }

        String url= "https://api.nationalize.io/?name="+token;
        JsonObjectRequest request= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String countryNo;
                System.out.println(response.toString());
                Gson gson= new Gson();
                Root root= gson.fromJson(response.toString(),Root.class);
                if(null!=root.getCountry().get(0).getCountry_id()) {
                    countryNo = root.getCountry().get(0).getCountry_id();
                    locale=new Locale("", countryNo);
                    int probability=(int)(root.getCountry().get(0).getProbability()*100);
                    System.out.println(locale.getDisplayCountry()+" "+probability);
                    txtview.setText("Origin Country is "+locale.getDisplayCountry());
                    percentage.setText(probability+"%");
                }
                else{
                    txtview.setText("We cannot predict nationality. Please enter correct name");
                }

                //setValues(country, probability);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtview.setText("We cannot predict nationality. Please enter correct name");
            }
        });
        requestQueue.add(request);

    }

    private void setValues(String country, int probability) {


    }

    public void checkNational(View view) {
        init();
        callAPI();

    }
}