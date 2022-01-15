package app.casino.ruleta;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.casino.ruleta.model.actualizarMonedas;

public class paypalA extends AppCompatActivity {

    private EditText correoE;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int ancho = displayMetrics.widthPixels;
        int alto = displayMetrics.heightPixels;

        getWindow().setLayout((int) (ancho*0.85), (int) (alto*0.5));

        Button addImage = findViewById(R.id.button2);
        correoE = findViewById(R.id.editTextC);
        progressBar = findViewById(R.id.progressBar2);

        float monedas2 = getIntent().getFloatExtra("monedas", 0);
        int dinero = getIntent().getIntExtra("dinero",0);
        int resta = getIntent().getIntExtra("resta",0);

        if (monedas2 == 0 || resta == 0 || dinero == 0 || monedas2 < resta){

            Toast.makeText(getApplicationContext(),"ocurrio un error",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }

        Log.i("casi", String.valueOf(getFilesDir()));
        addImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                if (!correoE.getText().toString().isEmpty()) {

                    Mail m = new Mail("appdeprueba.47@gmail.com", "prueba de app");

                    String[] toArr = {"gabrieldiazjor@gmail.com"};
                    m.setTo(toArr);

                    m.setBody(correoE.getText().toString(),String.valueOf(dinero));

                    try {
                        m.addAttachment(String.valueOf(getFilesDir()));

                        if (m.send()) {

                            float Resultado = monedas2 - resta;

                            actualizarMonedas.updateUser(getApplicationContext(),String.valueOf(Resultado));

                            Toast.makeText(paypalA.this, "Dentro unas semanas recivira el dinero", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(paypalA.this, "a ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e("MailApp", "Could not send email", e);
                    }
                }else {

                    correoE.setError("pon tu paypal");

                }

                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}