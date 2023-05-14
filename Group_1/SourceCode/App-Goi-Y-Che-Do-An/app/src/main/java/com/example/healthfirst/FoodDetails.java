package com.example.healthfirst;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FoodDetails extends AppCompatActivity {

    TextView title;
    TextView content;
    static public boolean active = false;
    static final String API_KEY = "96EGfrzgEBpUVcYoFT0kUVXhyNJscf11oEdhMbrG";
    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.food_details_layout);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        title = (TextView) findViewById(R.id.title);
        content = (TextView) findViewById(R.id.content);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String contentStr = "";
                    String foodName = getIntent().getStringExtra("key");
                    title.setText(getIntent().getStringExtra("foodvn"));
                    String urlStr = "https://api.nal.usda.gov/fdc/v1/foods/search?api_key=" + API_KEY + "&query=" + foodName;

                    URL url = new URL(urlStr);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        Gson gson = new Gson();
                        FoodSearchResponse foodSearchResponse = gson.fromJson(response.toString(), FoodSearchResponse.class);

                        if (foodSearchResponse != null && foodSearchResponse.getFoodItems() != null) {
                            int counter = 0;
                            FoodItem foodItem = foodSearchResponse.getFoodItems().get(0);
                                System.out.println("Nutrients:");
                                if (foodItem.getNutrients() != null) {
                                    for (Nutrient nutrient : foodItem.getNutrients()) {
                                        if(counter == 20) break;
                                        if(nutrient.getValue() > 0.0){
                                            contentStr += "- " + nutrient.getName() + ": " + nutrient.getValue() + " " + nutrient.getUnitName() + "\n";
                                            counter += 1;
                                        }
                                    }
                                }

                        }
                        if(contentStr != "") content.setText(contentStr);


                    } else {
                        System.out.println("Error: " + responseCode);
                    }

                    connection.disconnect();
                } catch(IOException e){
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

}
class FoodSearchResponse {
    @SerializedName("foods")
    private List<FoodItem> foodItems;

    public List<FoodItem> getFoodItems() {
        return foodItems;
    }
}

class FoodItem {
    @SerializedName("description")
    private String description;

    @SerializedName("foodNutrients")
    private List<Nutrient> nutrients;

    public String getDescription() {
        return description;
    }

    public List<Nutrient> getNutrients() {
        return nutrients;
    }
}

class Nutrient {
    @SerializedName("nutrientName")
    private String name;

    @SerializedName("value")
    private double value;

    @SerializedName("unitName")
    private String unitName;

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String getUnitName() {
        return unitName;
    }
}