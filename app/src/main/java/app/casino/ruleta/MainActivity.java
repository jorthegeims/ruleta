package app.casino.ruleta;

import android.annotation.SuppressLint;
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
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import app.casino.ruleta.model.actualizarMonedas;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    SharedPreferences sharedPreferences;

    SharedPreferences preferencesU;
    boolean nom = false;

    private GoogleApiClient googleApiClient;

    @SuppressLint("MissingPermission")
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

        String SHARED_PREF_NAME = "mypref";
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String usuario = "usuario";
        preferencesU = getSharedPreferences(usuario, Context.MODE_PRIVATE);

        Log.i("valores", "main id: "+ preferencesU.getString(usuario, "gabriel"));

        if (preferencesU.getString(usuario, "gabriel").equals("gabriel")) {

            Intent intent = new Intent(MainActivity.this, registro.class);

            startActivity(intent);

        }else {

            TextView nmonedas = findViewById(R.id.Nmonedas);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();

            AdView adView = new AdView(this);

            adView.setAdSize(AdSize.BANNER);

            adView.setAdUnitId("ca-app-pub-5014261020839028/6105180409");

            AdView mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();

            mAdView.loadAd(adRequest);

            actualizarMonedas.readUser(this, nmonedas);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {

                opr.setResultCallback(this::handleSignInResult);
            }
        } catch (Exception e) {

            e.printStackTrace();

            Intent intent = new Intent(MainActivity.this, registro.class);

            startActivity(intent);

        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (!result.isSuccess()){
            goLogInScreen();
        }
    }

    private void goLogInScreen() {

        Intent intent = new Intent(this, registro.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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
            aux = aux + "https://play.google.com/store/apps/details?id=app.casino.ruleta" + getBaseContext().getPackageName();
            i.putExtra(Intent.EXTRA_TEXT, aux);
            startActivity(i);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }





    }

    public void Puntuar (View view) {

        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void Rasca (View view) {

        Intent intent = new Intent(MainActivity.this, rasca.class);


        startActivity(intent);

    }

    public void Cerrar (View view) {

        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> {
            if (status.isSuccess()) {
                goLogInScreen();
            }else {
                Toast.makeText(getApplicationContext(),"no se pudo cerrar sesion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }
}