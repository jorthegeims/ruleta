package app.casino.ruleta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.casino.ruleta.model.Persona;

public class paypalA extends AppCompatActivity {

    private EditText correoE;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;

    private String SHARED_PREF_NAME = "mypref";

    SharedPreferences preferencesU;
    private String usuario = "usuario";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int ancho = displayMetrics.widthPixels;
        int alto = displayMetrics.heightPixels;

        getWindow().setLayout((int) (ancho*0.85), (int) (alto*0.5));




        Button addImage = (Button) findViewById(R.id.button2);
        correoE = findViewById(R.id.editTextC);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        preferencesU = getSharedPreferences(usuario, Context.MODE_PRIVATE);

        float monedas2 = getIntent().getFloatExtra("monedas", 0);
        int dinero = getIntent().getIntExtra("dinero",0);
        int resta = getIntent().getIntExtra("resta",0);

        inicializarFirebase();

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

                    String[] toArr = {"cristianhuaytan089@gmail.com"};
                    m.setTo(toArr);


                /*m.setFrom("wooo@wooo.com");
                m.setSubject("This is an email sent using my Mail JavaMail wrapper from an Android device.");
               */
                    m.setBody(correoE.getText().toString(),String.valueOf(dinero));

                    try {
                        m.addAttachment(String.valueOf(getFilesDir()));

                        if (m.send()) {




                            final float[] finalValor2 = new float[1];
                            final boolean[] dou = {false};

                            databaseReference.child("Persona").child(preferencesU.getString(usuario, "gabriel"))
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            if (dou[0] == false) {

                                                dou[0] = true;

                                                //Log.i("douuuu", String.valueOf(finalValor));
                                                Persona pipol1 = snapshot.getValue(Persona.class);

                                                finalValor2[0] = pipol1.getMonedas();

                                                float Resultado = finalValor2[0] - resta;
                                                databaseReference.child("Persona").child(preferencesU.getString(usuario, "gabriel")).child("monedas").setValue(Resultado);


                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });




                            Toast.makeText(paypalA.this, "Dentro unas semanas recivira el dinero", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(paypalA.this, "a ocurrido un error", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
                        Log.e("MailApp", "Could not send email", e);
                    }
                }else {

                    correoE.setError("pon tu paypal");

                }
            }
        });
    }
    private void inicializarFirebase() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }



}