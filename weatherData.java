package com.example.weatherappattempt;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    //Declare variables from Json data

    private String temperature, icon, city, weatherType;
    private int Condition;

    public static weatherData fromJson(JSONObject jsonObject){
        try{
            weatherData weatherD = new weatherData();
            weatherD.city= jsonObject.getString("name");
            weatherD.Condition =jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.weatherType = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.icon =  updateWeatherIcon(weatherD.Condition);
            double tempResult = jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue = (int)Math.rint(tempResult);
            weatherD.temperature = Integer.toString(roundedValue);
            return weatherD;

        }catch (JSONException e){
            e.printStackTrace();
            return null;

        }
    }

    private static String updateWeatherIcon(int condition){
        //thunderstorm
        if(condition >=0 && condition<=300){
            return"rain_thunder";
        }//lightrain
        else if(condition >=300 && condition<=500){
            return"light_rain";
        }
        //Shower
        else if(condition >=500 && condition<=600){
            return"heavy_rain";
        }//snow
        else if(condition >=600 && condition<=700) {
            return "ic_snowman";
        }//fog
        else if(condition >=700 && condition<=771){
            return"twister";
        }//Overcast
        else if(condition >=772 && condition<=800) {
            return "overcast";
        }//Sunny
        else if(condition == 800){
            return"ic_sunnny";
        }//Cloudy
        else if (condition >=801 && condition <= 804){
            return "ic-cloudy";
        }//Thunder
        else if (condition >=900&& condition <= 902){
            return "ic_thunder";
        }//little bit of snow
        if (condition == 903){
            return "snow2";
        }
        if (condition == 904){
            return "ic-sunny";
        }
        if (condition >= 905 && condition <=1000){
            return "thunder2";
        }

        return "dunno";
    }

    public String getTemperature() {
        return temperature + " °C";
    }

    public String getIcon() {
        return icon;
    }

    public String getCity() {
        return city;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public int getCondition() {
        return Condition;
    }
}
