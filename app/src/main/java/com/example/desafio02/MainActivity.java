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
    String stringData, stringResultado, moedaStringDe, moedaStringPara, error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        btnConverter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {

                if(moedaStringDe.equals(moedaStringPara)){
                    tvResultado.setText("Moedas iguais!");
                    tvData.setText("Moedas iguais!");
                    return;
                }

                try {
                    getConverter();
                    formatStringDate();
                    saveOnSharedPreferences();

                } catch (Exception e) {
                    tvResultado.setText("Erro na conversão!");
                    tvData.setText("Erro na conversão!");
                }
            }
        });

    }

    private void getConverter() throws ExecutionException, InterruptedException, JSONException {
        String urlStr = "https://economia.awesomeapi.com.br/last/" + moedaStringDe + "-"
                + moedaStringPara;
        HttpAsyncTask task = new HttpAsyncTask();
        task.execute(urlStr);
        String resposta = task.get();
        JSONObject obj = new JSONObject(resposta);
        JSONObject jsonObject = obj.getJSONObject(moedaStringDe + moedaStringPara);
        stringResultado = moedaStringDe + "//" + moedaStringPara + ":" +
                jsonObject.getString("bid");
        stringData = jsonObject.getString("create_date");
    }

    private void saveOnSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("desafio02", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("chaveResultado", stringResultado);
        editor.putString("chaveData", stringData);
        editor.commit();
        tvResultado.setText(stringResultado);
        tvData.setText(stringData);
    }

    private void formatStringDate() {
        String[] split = stringData.split("-");
        String[] splitTime = split[2].split(" ");
        stringData = splitTime[0] + "/" + split[1] + "/" + split[0] + " "
                + splitTime[1];
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