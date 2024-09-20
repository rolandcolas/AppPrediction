package com.example.machine;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText cgpa, iq, profile_score;
    Button predict;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cgpa = findViewById(R.id.cgpa);
        iq = findViewById(R.id.iq);
        profile_score = findViewById(R.id.profile_score);
        predict = findViewById(R.id.predict);
        result = findViewById(R.id.result);

        // Corrected URL string
        String url = "http://192.168.10.79:5000/predict";

        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Hit the API
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String data = jsonObject.getString("placement");

                                    if (data.equals("1")) {
                                        result.setText("Placement Accepted");
                                    } else {
                                        result.setText("Placement Not Accepted");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                                    Log.d("ResponseError", "JSONException: " + e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("cgpa", cgpa.getText().toString());
                        params.put("iq", iq.getText().toString());
                        params.put("profile_score", profile_score.getText().toString());
                        return params;
                    }
                };

                // Adding request to the queue
                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                queue.add(stringRequest);
            }
        });
    }
}
