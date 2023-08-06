package com.example.pruebas_json;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    EditText name_troop;
    TextView stats;
    Button btn_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name_troop = findViewById(R.id.name_troop);
        stats = findViewById(R.id.stats);
        btn_search = findViewById(R.id.btn_Search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "aquí estoy");
                if (!name_troop.getText().toString().isEmpty()) {
                    String name = name_troop.getText().toString().replace(" ", "").toLowerCase();
                    if (name.contains(".")) {
                        name = name.replace(".", "_");
                    }
                    barbarian(name);
                } else {
                    stats.setText("No se encontró la tropa");
                }

            }
        });
    }

    private void barbarian(String name) {
        String name_json = name;
        try {
            int resourceId = getResources().getIdentifier(name_json, "raw", getPackageName());
            InputStream inputStream = getResources().openRawResource(resourceId);

            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String json = new String(buffer, "UTF-8");

            // Analizar el JSON

            JSONObject jsonObject = new JSONObject(json);
            Log.d("MainActivity", String.valueOf(jsonObject));
            Iterator<String> keys = jsonObject.keys();


            while (keys.hasNext()) {
                String key = keys.next();
                Object value = jsonObject.get(key);

                Log.d("keys", key);
                Log.d("class of object", value.getClass().toString());
                Log.d("value", (String) value);


            }


            //stats.setText(String.valueOf(jsonObject));

        } catch (IOException | JSONException e) {
            Log.d("MainActivity", "Error:  " + e);
            e.printStackTrace();
        }
    }

}