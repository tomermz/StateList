package com.example.ep.myapplication.Activitys.Services;

import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;

import com.example.ep.myapplication.Activitys.Model.State;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by EP on 18/07/2017.
 */

public   class DataService {

private  ArrayList<State> arrState = new ArrayList<>();


    public  State getNstateFromBstate(String stateCode) {

        ArrayList<String> arrS = new ArrayList<>();
        String sURL = "https://restcountries.com/v3.1/all" + stateCode+"?fields=name;nativeName"; // gets a state by code
        String name = null;
        State s = null;
        // Connect to the URL using java's native library
        URL url = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            url = new URL(sURL);


        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection request = (HttpURLConnection) url.openConnection();

            request.connect();


            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element

            JsonObject rootobj = root.getAsJsonObject();

            JsonElement entriesname = rootobj.get("name");
            JsonElement entriesnative = rootobj.get("nativeName");

            String nameR = entriesname.toString().replace("\"",""); // convert to pure string
            String nativen = entriesnative.toString().replace("\"","");

             s = new State(nameR,nativen);

            } catch (IOException e1) {
            e1.printStackTrace();
        }


        return s;

    }

    public ArrayList<State> getArrState()
    {

        String sURL = "https://restcountries.com/v3.1/all?fields=name,nativeName,borders,flag"; // get all states

        // Connect to the URL using java's native library
        URL url = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            url = new URL(sURL);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection request = (HttpURLConnection) url.openConnection();

            request.connect();

            // Convert to a JSON object to print data
            JsonParser jp = new JsonParser(); //from gson
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element

            JsonArray rootobj = root.getAsJsonArray(); //May be an array, may be an object.

            for (JsonElement je : rootobj) {
                JsonObject obj = je.getAsJsonObject(); //since you know it's a JsonObject
                JsonElement entriesname = obj.get("name");//will return members of your object
                //JsonElement entriesnative = obj.get("nativeName");
                JsonElement entriesborders = obj.get("borders");
                JsonElement entriesflag = obj.get("flag");

                String name = entriesname.getAsJsonObject().get("common").toString();//.replace("\"","");
                //String nativen = entriesname.getAsJsonObject().get("nativeName").getAsJsonObject().get("cnr").getAsJsonObject().get("common").toString();//.replace("\"","");
                String flag = entriesflag.toString();//.replace("\"","");

                ArrayList<String> arrBorders = new ArrayList<String>();
                JsonArray entriesbordersArray = entriesborders.getAsJsonArray();

                for(JsonElement j : entriesbordersArray){

                    String s = j.toString();//.replace("\"","");
                    arrBorders.add(s);
                }

                arrState.add(new State(name, arrBorders,name+" - native",flag));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NetworkOnMainThreadException e)
        {
            e.printStackTrace();
        }

        return arrState;
    }
}



