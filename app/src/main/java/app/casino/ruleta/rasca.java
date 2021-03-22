package app.casino.ruleta;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Random;

import app.casino.ruleta.model.Persona;
import in.myinnos.androidscratchcard.ScratchCard;

public class rasca extends AppCompatActivity {

    private ScratchCard mScratchCard;
    private float valor;
    private TextView gana;
    private TextView nmoneda;

    Drawable sentido;
    private RewardedAd rewardedAd;
    private LottieAnimationView video;
    private TextView PLAY;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;

    private String SHARED_PREF_NAME = "mypref";

    SharedPreferences preferencesU;
    private String usuario = "usuario";

    private TextView Usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rasca);
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        preferencesU = getSharedPreferences(usuario, Context.MODE_PRIVATE);

        rewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(LoadAdError adError) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

        gana = findViewById(R.id.Gana);
        video = findViewById(R.id.video);
        video.setAnimation(R.raw.play);
        video.playAnimation();

        mScratchCard = (ScratchCard) findViewById(R.id.scratchCard);

        nmoneda = findViewById(R.id.Nmonedas);
        Usuario = findViewById(R.id.usuario);
        inicializarFirebase();
        listarDato();




        rasco();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {

            Intent intent = new Intent(rasca.this, MainActivity.class);

            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }

    public void rasco(){

        valor = (float) (Math.random()*(5-0.1)+0.1);

        DecimalFormat formato1 = new DecimalFormat("#.0");

        valor = Float.parseFloat(formato1.format(valor));
        gana.setText(String.valueOf(valor));





        mScratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {




            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {

                if (visiblePercent > 0.3) {

                    mScratchCard.setVisibility(View.GONE);
                    //Toast.makeText(rasca.this, "Content Visible", Toast.LENGTH_SHORT).show();
                    //Float.parseFloat(formato1.format(preferences.getFloat(monedas, (float) 0.0)+valor)));


                    float finalValor = valor;
                    final float[] finalValor2 = new float[1];
                    final boolean[] dou = {false};


                    databaseReference.child("Persona").child(preferencesU.getString(usuario, "gabriel"))
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    if (dou[0] == false) {

                                        dou[0] = true;

                                        Log.i("douuuu", String.valueOf(finalValor));
                                        Persona pipol1 = snapshot.getValue(Persona.class);

                                        finalValor2[0] = pipol1.getMonedas();

                                        String Resultado = String.valueOf(finalValor + finalValor2[0]);
                                        nmoneda.setText(Resultado);
                                        databaseReference.child("Persona").child(preferencesU.getString(usuario, "gabriel")).child("monedas").setValue(Resultado);


                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                }





            }
        });



    }

    public void ruleta(View view){

        Intent intent = new Intent(rasca.this, rasca.class);

        startActivity(intent);

    }

    public void playvideo (View view) {

        if (rewardedAd.isLoaded()) {
            Activity activityContext = rasca.this;
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {



                    // Ad closed.
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {

                    video.setVisibility(View.GONE);
                    PLAY = findViewById(R.id.playvideo);
                    PLAY.setVisibility(View.GONE);
                    // User earned reward.
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                    Intent intent = new Intent(rasca.this, rasca.class);

                    startActivity(intent);
                }
            };
            rewardedAd.show(activityContext, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
        }

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
        databaseReference.child("Persona").child(preferencesU.getString(usuario, "gabriel"))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Persona pipol = snapshot.getValue(Persona.class);

                        //Usuario.setText(pipol.getNombre());
                        nmoneda.setText(String.valueOf(pipol.getMonedas()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}