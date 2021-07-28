package com.example.weather2

import android.icu.text.SimpleDateFormat
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.UTF_8
import java.util.*

class MainActivity : AppCompatActivity() {
    //String for api
    val CITY: String = "dublin,ie"
    val API: String = "c0ead9243bf724091892d569000466f6"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weathertask().execute()
    }

    inner class weathertask() : AsyncTask<String, Void,String>(){

        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        override fun doInBackground(vararg p0: String?): String? {
            var response:String?
            try {
                response = URL("api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                        .readText(StandardCharsets.UTF_8).toString()
                        //.readText(Charsets.UTF_8)
            }
            catch (e:Exception){
                response = null
            }
            return response
        }

        @RequiresApi(Build.VERSION_CODES.N)
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated At: "+ SimpleDateFormat("dd/MM/yyy hh:mm a", Locale.ENGLISH ).format(Date(updatedAt*1000))
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: "+ main.getString("min_temp")+ "°C"
                val tempMax = "Max Temp: "+ main.getString("temp_max")+ "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windspeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name")+ ", " + sys.getString("country")

                //populate textviews with Data
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.min_temp).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.wind).text = windspeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                //disable the progreess bar and enable the mai container
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            }catch (e: Exception)
            {
                //if error disable progress bar and enable the errot text textview
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }


}


