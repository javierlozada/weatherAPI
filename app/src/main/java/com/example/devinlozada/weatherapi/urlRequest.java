package com.example.devinlozada.weatherapi;

/**
 * Created by devinlozada on 11/02/17.
 */

public class urlRequest {
    String urlIp;

    public String urlrequest(String ciudad){
        urlIp = "http://api.openweathermap.org/data/2.5/weather?q="+ciudad+"&APPID=dfac9d9f4a7e26870187e5836dad5556";
        return urlIp;
    }

}
