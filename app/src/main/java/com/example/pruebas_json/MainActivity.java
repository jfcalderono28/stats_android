package com.example.pruebas_json;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
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
                    try {
                        barbarian(name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    stats.setText("No se encontró la tropa");
                }

            }
        });
    }

    private void barbarian(String name) throws JSONException {
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

                Class value2 = value.getClass();

                if (value2.equals(String.class)) {
                    Log.d("keys", key);
                    Log.d("value", (String) value);
                } else if (value2.equals(JSONObject.class)) {

                    JSONObject nestedJsonObject = (JSONObject) value;
                    // Ahora puedes trabajar con el objeto JSON nestedJsonObject
                    // Por ejemplo, puedes obtener sus claves y valores
                    Iterator<String> nestedKeys = nestedJsonObject.keys();
                    while (nestedKeys.hasNext()) {
                        String nestedKey = nestedKeys.next();
                        Object nestedValue = nestedJsonObject.get(nestedKey);
                        Log.d("nestedKey", nestedKey);
                        Log.d("nestedValue", nestedValue.toString());

                        JSONObject statsJsonObject = (JSONObject) nestedValue;
                        Iterator<String> statsKeys = statsJsonObject.keys();

                        while (statsKeys.hasNext()){
                            String statKey = statsKeys.next();
                            Object statValue = statsJsonObject.get(statKey);
                            Log.d("statKey", statKey);
                            Log.d("statValue", statValue.toString());
                        }

                    }
                } else if (value.equals(null)) {
                    stats.setText("Sin estadisticas");
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        //stats.setText(String.valueOf(jsonObject));
    }
}
