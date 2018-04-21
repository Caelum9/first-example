package com.example.myfirstapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

//add dependencies to your class
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import android.os.AsyncTask;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    public String message, out;
    //@Override
    //protected void onCreate(Bundle savedInstanceState) {
    //    super.onCreate(savedInstanceState);
    //    setContentView(R.layout.activity_main);
    //}

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        message = editText.getText().toString();
        try {
            out = new CallbackTask().execute(dictionaryEntries()).get();
        } catch (Exception e) {
            out = "not find";
            return;
        }
        int a = out.indexOf("definitions");
        int b = out.indexOf("[", a);
        int c = out.indexOf("\"", b);
        int d = out.indexOf("]", b);
        out = out.substring(c,d);
        intent.putExtra(EXTRA_MESSAGE, out);
        startActivity(intent);
    }

    //public class MainActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

        }

        private String dictionaryEntries() {
            final String language = "en";
            final String word = message;
            final String word_id = word.toLowerCase(); //word id is case sensitive and lowercase is required
            return "https://od-api.oxforddictionaries.com:443/api/v1/entries/" + language + "/" + word_id;
        }


        //in android calling network requests on the main thread forbidden by default
        //create class to do async job
        private class CallbackTask extends AsyncTask<String, Integer, String> {

            @Override
            protected String doInBackground(String... params) {

                //TODO: replace with your own app id and app key
                final String app_id = "4520a60b";
                final String app_key = "2eb1c3127f854566ffb01b23764e5437";
                try {
                    URL url = new URL(params[0]);
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Accept","application/json");
                    urlConnection.setRequestProperty("app_id",app_id);
                    urlConnection.setRequestProperty("app_key",app_key);

                    // read the output from the server
                    BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line + "\n");
                    }

                    return stringBuilder.toString();

                }
                catch (Exception e) {
                    e.printStackTrace();
                    return e.toString();
                }
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                System.out.println(result);
            }
        }
    //}
}
