package app.casino.ruleta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.TimeZone;

import app.casino.ruleta.model.Persona;

public class popuprecompensa extends AppCompatActivity {



    private TextView Nmonedas;

    private Intent intent;
    int fecha2;
    SharedPreferences sharedPreferences;
    private String SHARED_PREF_NAME = "mypref";
    SharedPreferences preferences;
    private String fecha = "fecha";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    SharedPreferences preferencesU;
    private String usuario = "usuario";
    private boolean on = false;

    private float monedas;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popuprecompensa);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        preferences = getSharedPreferences(fecha, Context.MODE_PRIVATE);
        preferencesU = getSharedPreferences(usuario, Context.MODE_PRIVATE);
        Nmonedas = findViewById(R.id.TM);
        inicializarFirebase();
        databaseReference.child("Persona").child(preferencesU.getString(usuario, "gabriel"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                            //Log.i("douuuu", String.valueOf(finalValor));
                            Persona pipol1 = snapshot.getValue(Persona.class);

                            monedas = pipol1.getMonedas();

                            Nmonedas.setText(pipol1.getMonedas() + " coins");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        intent = new Intent(Intent.ACTION_VIEW);


    }

    public void Circulop1 (View view) {

        if (monedas > 2700) {


            Intent intent1 = new Intent(popuprecompensa.this, paypalA.class);
            intent1.putExtra("monedas", monedas);
            intent1.putExtra("dinero", 5);
            intent1.putExtra("resta", 2700);
            startActivity(intent1);


        }

    }

        public void Circulop2 (View view){

            if (monedas> 5200) {


                Intent intent1 = new Intent(popuprecompensa.this, paypalA.class);
                intent1.putExtra("monedas", monedas);
                intent1.putExtra("dinero",10);
                intent1.putExtra("resta",5200);
                startActivity(intent1);

            }
    }

    public void Circulop3 (View view){

        if (monedas> 7700) {


            Intent intent1 = new Intent(popuprecompensa.this, paypalA.class);
            intent1.putExtra("monedas", monedas);
            intent1.putExtra("dinero",15);
            intent1.putExtra("resta",7700);
            startActivity(intent1);

        }
    }

    public void Circulop4 (View view){

        if (monedas> 10200) {


            Intent intent1 = new Intent(popuprecompensa.this, paypalA.class);
            intent1.putExtra("monedas", monedas);
            intent1.putExtra("dinero",20);
            intent1.putExtra("resta",10200);
            startActivity(intent1);

        }
    }



    private void inicializarFirebase() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {

            Intent intent = new Intent(popuprecompensa.this, MainActivity.class);

            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }

    public void atras (View view){

        Intent intent = new Intent(popuprecompensa.this, MainActivity.class);

        startActivity(intent);

    }



}