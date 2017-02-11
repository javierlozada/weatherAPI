package com.example.devinlozada.weatherapi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class getWebRequest {
    int reqresponseCode;


   public String getServiceAPI(String urladdress){
       URL url;
       int myTimeOut = 25001;
       String line,response = "";


       try {
           //obtentgo url
           url = new URL(urladdress);
           HttpURLConnection conn = (HttpURLConnection) url.openConnection();
           conn.setReadTimeout(myTimeOut);
           conn.setConnectTimeout(myTimeOut);
           conn.setRequestMethod("GET");

           reqresponseCode = conn.getResponseCode();

           System.out.println("statusCode: " + reqresponseCode);

            /*si la respuesta es igual a "200" p "HTTP_Ok" devuelveme la respuesta
             de lo contrario  devuelveme el error */
           if (reqresponseCode == HttpsURLConnection.HTTP_OK) {

               BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));


               while ((line = br.readLine()) != null) {
                   response += line;

               }
           } else {
               BufferedReader brError = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
               while ((line = brError.readLine()) != null) {
                   response += line;
               }
           }
       }catch (MalformedURLException e) {
               e.printStackTrace();
       }catch (Exception e){
           e.printStackTrace();

       }

       return response;
   }


    public int getStatusCode() throws IOException {
        return reqresponseCode;
    }



}
