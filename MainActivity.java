package com.example.weater3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.et);
        textView = findViewById(R.id.tv);

    }

    public void get(View view){
        String api_key = "718198a42149daae22c033622a23ca00";
        String city = editText.getText().toString();
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+ city + "&appid=718198a42149daae22c033622a23ca00";
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object =  response.getJSONObject("main");
                    String temperature = object.getString("temp");
                    textView.setText(temperature);
                    Double temp= Double.parseDouble(temperature)-273.15;
                    textView.setText("temperature : " + temp.toString().substring(0,5)+ "°C") ;
                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "error, try again and check city name", Toast.LENGTH_LONG).show();

            }
        });
        queue.add(request);


    }
}