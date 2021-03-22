package app.casino.ruleta;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import app.casino.ruleta.model.Persona;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private AdView mAdView;
    private TextView Nmonedas;
    SharedPreferences sharedPreferences;
    private String monedas = "monedas";
    private String SHARED_PREF_NAME = "mypref";
    SharedPreferences preferences;
    SharedPreferences preferencesU;
    private String usuario = "usuario";
    boolean nom = false;
    private TextView Usuario;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;


    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nom = getIntent().getBooleanExtra("nom",false);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView adView = new AdView(this);

        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        preferences = getSharedPreferences(monedas, Context.MODE_PRIVATE);
        preferencesU = getSharedPreferences(usuario, Context.MODE_PRIVATE);

        if (preferencesU.getString(usuario, "gabriel").equals("gabriel")) {

            Log.i("douuuu","inicio");



            Intent intent = new Intent(MainActivity.this, registro.class);

            startActivity(intent);

        }else {


            Nmonedas = findViewById(R.id.Nmonedas);
            Usuario = findViewById(R.id.usuario);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                    .build();


            //Nmonedas.setText(String.valueOf( preferences.getFloat(monedas, 0)));

            inicializarFirebase();
            listarDato();



        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else{

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }


    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()){

            GoogleSignInAccount account = result.getSignInAccount();

            Persona p = new Persona();



        } else
        {
            goLogInScreen();
        }
    }

    private void goLogInScreen() {

        Intent intent = new Intent(this, registro.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void inicializarFirebase() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void listarDato() {
        //Log.i("douuuu", String.valueOf(databaseReference.child("Persona").child(preferencesU.getString(usuario, "")).child("nombre").get));

        //Task<DataSnapshot> ti = databaseReference.child("Persona").child(preferencesU.getString(usuario, "")).get();
        //ti.
        Log.i("douuuu",preferencesU.getString(usuario, "gabriel"));
        databaseReference.child("Persona").child(preferencesU.getString(usuario, "gabriel"))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Persona pipol = snapshot.getValue(Persona.class);

                try {

                    Nmonedas.setText(String.valueOf(pipol.getMonedas()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void Score (View view){

        String dou = view.toString();
        Log.i("douuuu", dou);
        Intent intent = new Intent(MainActivity.this, score.class);
        startActivity(intent);
    }

    public void Ruleta (View view) {

        Intent intent = new Intent(MainActivity.this, ruletaA.class);

        startActivity(intent);


    }
    public void Recompensa (View view) {

        Intent intent = new Intent(MainActivity.this, popuprecompensa.class);

        startActivity(intent);


    }

    public void Compartir (View view) {

        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
            String aux = "Descarga la app\n";
            aux = aux + "https://play.google.com/store/apps/details?id=" + getBaseContext().getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, aux);
            startActivity(i);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }

    }

    public void Puntuar (View view) {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + "com.android.chrome")));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }

    }

    public void Rasca (View view) {

        Intent intent = new Intent(MainActivity.this, rasca.class);

        startActivity(intent);


    }

    public void Cerrar (View view) {

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                }else {
                    Toast.makeText(getApplicationContext(),"no se pudo cerrar sesion", Toast.LENGTH_SHORT);
                }
            }
        });

        //Intent intent = new Intent(MainActivity.this, registro.class);
        //startActivity(intent);


    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}

// prueba de app