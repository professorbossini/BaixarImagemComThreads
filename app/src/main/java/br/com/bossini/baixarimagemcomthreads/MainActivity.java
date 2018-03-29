package br.com.bossini.baixarimagemcomthreads;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {

    private EditText cidadeEditText;
    private TextView descricaoTextView;
    private TextView minTextView;
    private TextView maxTextView;
    private TextView humidityTextView;
    private SimpleDateFormat sdf =
            new SimpleDateFormat("EEEE");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cidadeEditText = (EditText)
                findViewById(R.id.cidadeEditText);
        descricaoTextView = (TextView)
                findViewById(R.id.descricaoTextView);
        minTextView = (TextView)
                findViewById(R.id.minTextView);
        maxTextView = (TextView)
                findViewById(R.id.maxTextView);
        humidityTextView = (TextView)
                findViewById(R.id.humidityTextView);
    }

    public void buscar (View view){
        String cidade = cidadeEditText.
                            getEditableText().toString();

        StringBuilder sb = new StringBuilder ("");
        sb.append (getString(R.string.url_ws));
        sb.append(cidade);
        sb.append("&appid=").
                append(getString(R.string.chave)).
                append("&lang=pt").
                append ("&units=metric");
        new BuscaJSON().execute(sb.toString());

        /*final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().
                url(sb.toString()).
                build();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.
                            newCall(request).execute();
                    String corpo = response.body().string();
                    runOnUiThread(()->{
                            Toast.makeText(MainActivity.this,
                                    corpo,
                                    Toast.LENGTH_SHORT).show();
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/


    }

    private class BuscaJSON extends AsyncTask <String, Void, String>{

        @Override
        protected String doInBackground(String... url) {
            OkHttpClient client = new OkHttpClient();
            final Request request = new Request.Builder().
                    url(url[0]).
                    build();
            try {
                return client.newCall(request).execute().body().string();
            } catch (IOException e) {
                e.printStackTrace();
                String erro =
                        String.format ("{%s : %s}", "erro", e.getMessage());
                return erro;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            Toast.makeText(MainActivity.this, json,
                    Toast.LENGTH_SHORT).show();
            try {
                JSONObject previsao = new JSONObject(json);
                JSONArray list = previsao.getJSONArray("list");
                JSONObject dia = list.getJSONObject(0);
                long dt = dia.getLong("dt");
                double min = dia.getJSONObject("temp").getDouble("min");
                double max = dia.getJSONObject("temp").getDouble("max");
                int humidade = dia.getInt("humidity");
                String descricao =
                        dia.getJSONArray("weather").
                                getJSONObject(0).getString ("description");
                Date date = new Date();
                date.setTime(dt * 1000);
                descricaoTextView.setText(
                        String.format("%s : %s",
                                sdf.format(date), descricao));
                minTextView.setText("Min: " + Double.toString(min));
                maxTextView.setText("Max: " + Double.toString(max));
                humidityTextView.setText("Hum: " + Double.toString(humidade));


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
