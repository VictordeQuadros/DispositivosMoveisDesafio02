package com.example.desafio02;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    Button btnConverter;
    TextView tvData, tvResultado;
    Spinner spnDe, spnPara;
    String stringData, stringResultado, moedaStringDe, moedaStringPara;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        btnConverter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {


                String urlStr = "https://economia.awesomeapi.com.br/last/" + moedaStringDe + "-"
                        + moedaStringPara;
                HttpAsyncTask task = new HttpAsyncTask();
                task.execute(urlStr);

                JSONObject obj = null;
                String logr = "Passou";
                String resposta = "Erro";

//                try {
//                    resposta = task.get();
//                } catch (ExecutionException e) {
//                    logr = "Erro01";
//                } catch (InterruptedException e) {
//                    logr = "Erro01";
//                }
//
//                obj = new JSONObject(resposta);
                try {
                    resposta = task.get();
//                obj = new JSONObject(resposta);
//                urlStr =   moedaStringDe + moedaStringPara;
//                JSONObject jsonObject = obj.getJSONObject(moedaStringDe + moedaStringPara);

                } catch (Exception e) {
                    urlStr =   moedaStringDe + moedaStringPara;
                    logr = "Erro";
                }

//                try {
//                    resposta = task.get();
//                    obj = new JSONObject(resposta);
//                    JSONObject jsonObject = obj.getJSONObject(moedaStringDe + moedaStringPara);
//
//                    logr = jsonObject.getString(moedaStringDe + moedaStringPara);
//                } catch (Exception e) {
//                    logr = "Erro";
//                }


                SharedPreferences prefs = getSharedPreferences("desafio02", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("chaveData", moedaStringDe);
                editor.putString("chaveResultado", moedaStringPara);
                editor.commit();
                tvData.setText(moedaStringDe);
                tvResultado.setText(moedaStringPara);
            }
        });

    }

    private void init() {
        tvResultado = findViewById(R.id.resultado);
        tvData = findViewById(R.id.data);
        btnConverter = findViewById(R.id.button);
        spnDe = findViewById(R.id.moedaDe);
        spnPara = findViewById(R.id.moedaPara);

        spnDe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                moedaStringDe = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spnPara.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                moedaStringPara = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        SharedPreferences prefs = getSharedPreferences("desafio02", MODE_PRIVATE);
        stringResultado = prefs.getString("chaveResultado", "");
        tvResultado.setText(stringResultado);
        stringData = prefs.getString("chaveData", "");
        tvData.setText(stringData);
    }



}