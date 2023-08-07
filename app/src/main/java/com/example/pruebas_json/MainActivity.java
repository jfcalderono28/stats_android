package com.example.pruebas_json;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText name_troop;
    Button btn_search;
    ListView lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name_troop = findViewById(R.id.name_troop);

        btn_search = findViewById(R.id.btn_Search);
        lv1 = findViewById(R.id.lv1);
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
                }
            }
        });
    }


    //el metodo se llama barbarian pero va funcionar para cualquier tropa
    private void barbarian(String name) throws JSONException {
        String name_json = name;
        List<String> llaves = new ArrayList<>();

        try {
            int resourceId = getResources().getIdentifier(name_json, "raw", getPackageName());
            if (resourceId != 0) {


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
                    //esta linea es para saber que tipo de dato es value
                    Class value2 = value.getClass();

                    if (key.equalsIgnoreCase("name") || key.equalsIgnoreCase("url")) {
                        llaves.add(key + ":    " + (String) value);
                    } else if (value2.equals(JSONObject.class)) {
                        llaves.add("");
                        //acá se llega a las estadisticas
                        JSONObject nestedJsonObject = (JSONObject) value;

                        Iterator<String> nestedKeys = nestedJsonObject.keys();
                        while (nestedKeys.hasNext()) {
                            //acá se entra a cada nivel de la tropa
                            String nestedKey = nestedKeys.next();
                            Object nestedValue = nestedJsonObject.get(nestedKey);


                            JSONObject statsJsonObject = (JSONObject) nestedValue;
                            Iterator<String> statsKeys = statsJsonObject.keys();
                            while (statsKeys.hasNext()) {
                                //Acá se recorre el json de las estadisticas
                                String statKey = statsKeys.next();
                                Object statValue = statsJsonObject.get(statKey);
                                llaves.add(statKey + "  -->  " + (String) statValue);

                                if (!statsKeys.hasNext()) {
                                    llaves.add("");
                                }
                            }
                        }
                    } else if (value.equals(null)) {
                        //en caso de que las estadisticas sea null
                        llaves.add("Sin estadisticas");
                    }
                }
            } else {
                llaves.add("No se encontró la tropa");
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.lv1, llaves);
        lv1.setAdapter(adapter);
    }
}
