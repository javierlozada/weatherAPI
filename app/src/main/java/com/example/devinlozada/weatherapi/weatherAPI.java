package com.example.devinlozada.weatherapi;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class weatherAPI extends AppCompatActivity {


    //clases externas
    private getWebRequest getMethod = new getWebRequest();
    private urlRequest urlRequestAPi   = new urlRequest();

    //componentes
    private EditText buscador;
    private TextView city,longitud,latitud,temperature,pressure,humidity,temp_min,temp_max,sealevel,grndlevel,main;
    private ImageView imageweather;

    private String textBuscador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_api);

        //Initializo componentes
        buscador    = (EditText) findViewById(R.id.buscadorCiudad);
        city        = (TextView) findViewById(R.id.ciudad);
        longitud    = (TextView) findViewById(R.id.longitud);
        latitud     = (TextView) findViewById(R.id.latitud);
        temperature = (TextView) findViewById(R.id.temp);
        pressure    = (TextView) findViewById(R.id.pressure);
        humidity    = (TextView) findViewById(R.id.humidity);
        temp_max    = (TextView) findViewById(R.id.temp_max);
        temp_min    = (TextView) findViewById(R.id.temp_min);
        sealevel    = (TextView) findViewById(R.id.sealevel);
        grndlevel   = (TextView) findViewById(R.id.groundlevel);
        main        = (TextView) findViewById(R.id.main);

        imageweather= (ImageView) findViewById(R.id.weatherImage);


        /******************* EVENTOS ******************/

        //en el teclado "fisico" del android aparece lupa para busca agregar evento
        buscador.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    textBuscador = buscador.getText().toString();

                    new getWeather().execute();
                    return true;
                }
                return false;
            }
        });


        //agrego un listener al icono del lado derecho que esta ubicado en el edittext
        buscador.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (buscador.getRight() - buscador.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width()))
                    {
                        textBuscador = buscador.getText().toString();

                        new getWeather().execute();

                        return true;
                    }
                }
                return false;




            }
        });



        /******************* END EVENTOS ******************/

    }//end Oncreate

    private class getWeather extends AsyncTask<Void,Void,Void>{
        ProgressDialog progressDialog;
        String getResponse = null;
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(weatherAPI.this);
        int statusCode,idObject;
        JSONObject jsonObject;


        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = new ProgressDialog(weatherAPI.this);
            progressDialog.setMessage("Cargando...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }//END doInBackground method


        @Override
        protected Void doInBackground(Void... params) {

            try {
                String urlRequest = urlRequestAPi.urlrequest(textBuscador);

                //enviando la url con lo que se obtuvo en el campo del buscador
                getResponse = getMethod.getServiceAPI(urlRequest);

                statusCode  = getMethod.getStatusCode();


            } catch (IOException e) {
                e.printStackTrace();
            }


            System.out.println(getResponse);


            return null;

        }//end doInBackground


        @Override
        protected void onPostExecute(Void result) {

        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }//end progressDialog

            //si el json es diferente de null y viene un status "200"
            if(getResponse != null && statusCode == 200) {
                try {
                    jsonObject                      = new JSONObject(getResponse);

                    JSONObject jsonObjectcoord      = jsonObject.getJSONObject("coord");
                    String lon                      = jsonObjectcoord.getString("lon");
                    String lat                      = jsonObjectcoord.getString("lat");

                    JSONArray jsonweatherArray      = jsonObject.getJSONArray("weather");
                    JSONObject  jsonObjectWeather   = jsonweatherArray.getJSONObject(0);
                    Object id                       = jsonObjectWeather.get("id");
                    String mainJson                 = jsonObjectWeather.getString("main");
                    String description              = jsonObjectWeather.getString("icon");

                    JSONObject jsonObjectmain       = jsonObject.getJSONObject("main");
                    Object temp                     = jsonObjectmain.get("temp");
                    Object pressureInt              = jsonObjectmain.get("pressure");
                    Object humedad                  = jsonObjectmain.get("humidity");
                    Object temp_minimo              = jsonObjectmain.get("temp_min");
                    Object temp_maximo              = jsonObjectmain.get("temp_max");
                    Object sealvl                   = jsonObjectmain.isNull("sea_level")? "" : jsonObjectmain.getString("sea_level");
                    Object grndlvl                  = jsonObjectmain.isNull("grnd_level")? "" : jsonObjectmain.getString("grnd_level");


                    JSONObject jsonObjectwind       = jsonObject.getJSONObject("wind");
                    Object speed                    = jsonObjectwind.get("speed");
                    Object deg                      = jsonObjectwind.get("deg");


                    JSONObject jsonObjectClouds     = jsonObject.getJSONObject("clouds");
                    Object all                      = jsonObjectClouds.get("all");

                    JSONObject jsonObjectsys        = jsonObject.getJSONObject("sys");
                    Object message                  = jsonObjectsys.get("message");
                    String country                  = jsonObjectsys.getString("country");
                    Object sunrise                  = jsonObjectsys.get("sunrise");
                    Object sunset                   = jsonObjectsys.get("sunset");


                    String base                     = jsonObject.getString("base");
                    Object dt                       = jsonObject.get("dt");
                    Object idJSON                   = jsonObject.get("id");
                    String name                     = jsonObject.getString("name");
                    Object cod                      = jsonObject.get("cod");


                    //seteo datos parseados del json
                    city.setText("ciudad: " +name);
                    longitud.setText("Longitud: " + lon);
                    latitud.setText("latitutd: " +lat);
                    temperature.setText("Temperatura: " +temp.toString());
                    pressure.setText("Presion: " +pressureInt.toString());
                    humidity.setText("Humedad: " +humedad.toString());
                    temp_min.setText("temperatura min: " + temp_minimo.toString());
                    temp_max.setText("temperatura max: " + temp_maximo.toString());
                    sealevel.setText("nivel del mar: " + sealvl.toString());
                    grndlevel.setText("nivel del suelo: " + grndlvl.toString());
                    main.setText(mainJson.toString());
                    idObject = (int) id;


                    //cambio de imagenes segun el codigo que se vino en el json(son muchos casos entonces no estan correctos las imagenes)
                    switch (idObject){
                        case 200:
                        case 201:
                        case 202:
                        case 210:
                        case 211:
                        case 212:
                        case 221:
                        case 230:
                        case 231:
                        case 232:
                            imageweather.setBackgroundResource(R.drawable.storm);
                            break;
                        case 300:
                        case 301:
                        case 302:
                        case 310:
                        case 311:
                        case 312:
                        case 313:
                        case 314:
                        case 321:
                        case 500:
                        case 501:
                        case 502:
                        case 503:
                        case 504:
                        case 511:
                        case 520:
                        case 521:
                        case 522:
                        case 531:
                            imageweather.setBackgroundResource(R.drawable.rain);
                            break;
                        case 600:
                        case 601:
                        case 602:
                        case 611:
                        case 612:
                        case 615:
                        case 616:
                        case 620:
                        case 621:
                        case 622:
                            imageweather.setBackgroundResource(R.drawable.snow);
                           break;

                        case 701:
                        case 711:
                        case 721:
                        case 731:
                        case 741:
                        case 751:
                        case 761:
                        case 762:
                        case 771:
                        case 781:
                            imageweather.setBackgroundResource(R.drawable.sunny);
                            break;

                        case 800:
                            imageweather.setBackgroundResource(R.drawable.suncloud);
                            break;

                        case 801:
                        case 802:
                        case 803:
                        case 804:
                            imageweather.setBackgroundResource(R.drawable.cloudy);
                            break;
                        case 900:
                        case 901:
                        case 902:
                        case 903:
                        case 904:
                        case 905:
                        case 906:
                            imageweather.setBackgroundResource(R.drawable.storm);
                            break;
                        case 951:
                        case 952:
                        case 953:
                        case 954:
                        case 955:
                        case 956:
                        case 957:
                        case 958:
                        case 959:
                        case 960:
                        case 961:
                        case 962:
                            imageweather.setBackgroundResource(R.drawable.rain);
                            break;
                    }


                }catch (Exception e){
                    e.printStackTrace();

                }//end try/catch
            }else{
                try {
                    jsonObject          = new JSONObject(getResponse);

                    String mensajeError = jsonObject.getString("message");
                    builder.setMessage(mensajeError);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                        });

                        builder.show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }// onPostExecute
    }//end AsyncTask

}//end weatherAPI


