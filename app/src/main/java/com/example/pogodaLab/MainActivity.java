package com.example.pogodaLab;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView cityName;
    Button search;
    TextView textView;
    TextView textView2;
    String url;


    class getWeather extends AsyncTask<String , Void , String> {

        @Override
        protected String doInBackground(String... urls) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();



                InputStream inputStream = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = "";

                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }
                return result.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return  null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                JSONObject jsonObject = new JSONObject(result);
                String weather = jsonObject.getString("main");

                        /*"temp":12.55,
                        "feels_like":11.64,
                        "temp_min":11.49,
                        "temp_max":13.62,
                        "pressure":1016,
                        "humidity":68*/

                        /* + "°C"*/

                weather = weather.replace("temp", "Temperatura");
                weather = weather.replace(",", "\n");
                weather = weather.replace(":", ": ");
                weather = weather.replace("\"", "");
                weather = weather.replace("{", "");
                weather = weather.replace("}", "");
                weather = weather.replace("feels_like", "Temperatura odczuwalna");
                weather = weather.replace("_min", " minimalna");
                weather = weather.replace("_max", " maksymalna");
                weather = weather.replace("pressure", "Ciśnienie");
                weather = weather.replace("humidity", "Wilgotność");

                textView.setText(weather);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        cityName = findViewById(R.id.cityName);
        search = findViewById(R.id.search);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        final String[] temp = {""};


        search.setOnClickListener(view -> {

            String city = cityName.getText().toString();

            try {
                    if (city != null) {
                        url = "https://api.openweathermap.org/data/2.5/weather?q="+city+"&units=metric&appid=821e1d5848d9b2d8f6b36ac3b2538216";
                    } else {
                        Toast.makeText(MainActivity.this, "Wprowadź nazwę miasta!", Toast.LENGTH_SHORT).show();
                    }

                    getWeather task = new getWeather();
                    temp[0] = task.execute(url).get();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            if(temp[0] == null) {
                    textView.setText("Nie znaleziono miasta!");
                }

        });


    }
}