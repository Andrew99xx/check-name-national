package com.indra.checkmynationality;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtview=findViewById(R.id.textView2);
        percentage=findViewById(R.id.textView3);
        editTxt=findViewById(R.id.editText);
        warning=findViewById(R.id.warning);
        refreshButton=findViewById(R.id.refresh);
        cMap=new CountryMap();
        cMap.setCountryCodes();

    }
    Locale locale;
    private View loadingLayout;
    private TextView txtview;
    private TextView percentage;
    private EditText editTxt;
    private CountryMap cMap;
    private TextView warning;
    private ImageButton refreshButton;
    

    public void callAPI() {
        showLoadingLayout();


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String token=editTxt.getText().toString();
        cleanAll();
        if(token.isEmpty()){

            warning.setText("Please do not provide empty name");
            hideLoadingLayout();
            return;

        } else if (token.contains(" ")) {
            warning.setText(("Please do not provide space in name"));
            hideLoadingLayout();
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
                hideLoadingLayout();

                //setValues(country, probability);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                txtview.setText("We cannot predict nationality. Please enter correct name");
                hideLoadingLayout();

            }

        });
        requestQueue.add(request);


    }

    public void cleanAll(){
        percentage.setText("");
        txtview.setText("");
        warning.setText("");
        editTxt.setText("");

    }

    private void hideLoadingLayout() {
        ViewGroup rootView = findViewById(android.R.id.content);
        rootView.removeView(loadingLayout);
    }

    private void showLoadingLayout() {
        // Check if the loading layout is already inflated
        if (loadingLayout == null) {
            LayoutInflater inflater = LayoutInflater.from(this);
            loadingLayout = inflater.inflate(R.layout.layout_loading, null);
        }

        // Add the loading layout to the root view
        ViewGroup rootView = findViewById(android.R.id.content);
        rootView.addView(loadingLayout);
    }


    public void checkNational(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        callAPI();

    }

    public void onClickRefresh(View view) {
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        view.startAnimation(animation);
        cleanAll();
    }
}