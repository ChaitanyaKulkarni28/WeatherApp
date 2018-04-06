package com.example.chaitanyakulkarni.myweatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public static final String[] cities = {"Austin","Dallas","Pune","Mumbai"};
    static TextView place;
    static TextView temp;
    AutoCompleteTextView autoCompleteTextView;
    ImageView imageView;
    EditText editText;
    Button button;
    ArrayAdapter<String> arrayAdapter;
    static String cityName=" ";
    static String APIKey = "xxx";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        place = (TextView)findViewById(R.id.place);
        temp = (TextView)findViewById(R.id.temp);
        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        imageView = (ImageView) findViewById(R.id.image);
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,cities);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setThreshold(2);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCompleteTextView.showDropDown();
            }
        });



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText().toString().equals(""))
                {
                    cityName = autoCompleteTextView.getText().toString().trim();

                }
                else
                {
                    cityName = editText.getText().toString().trim();

                }
                Weather getData = new Weather();
                try
                {
                    String k = getData.execute("http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+APIKey).get();
                    JSONObject jsonObject = new JSONObject(k);
                    String main = jsonObject.getString("main");

                    JSONObject jsonObject1 = new JSONObject(main);
                    String t = jsonObject1.getString("temp");
                    double temp1 = Double.valueOf(t) - 273.15;
                    String weather = jsonObject.getString("weather");
                    JSONArray jsonArray = new JSONArray(weather);
                    String description = "";

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject arrayObject = jsonArray.getJSONObject(i);
                        description = arrayObject.getString("description");
                    }

                    place.setText(cityName);
                    temp.setText("Temperature:"+String.valueOf(temp1)+"\n Description:"+description);
                    editText.setText("");
                    Log.i("Weather details",k);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
     
    }
}
