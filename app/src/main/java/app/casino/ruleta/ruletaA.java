package app.casino.ruleta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;
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


import java.text.BreakIterator;
import java.util.Random;
import java.util.TimeZone;

import app.casino.ruleta.model.Persona;


public class ruletaA extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;

    ImageView girar;
    ImageView ruleta;
    ImageView mensa;
    RelativeLayout Lmensa;
    private LottieAnimationView bien;
    private int intentos = 0;

    Random r;

    int degree = 0, degree_old = 0;

    private TextView tvp;

    private static final float FACTOR = 22.5f;
    private String text = "";


    TextView Nmoneda;
    private TextView Usuario;

    private RewardedAd rewardedAd;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;

    private String SHARED_PREF_NAME = "mypref";

    SharedPreferences preferencesU;
    private String usuario = "usuario";
    private float valor = (float) 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        preferencesU = getSharedPreferences(usuario, Context.MODE_PRIVATE);

        Nmoneda = findViewById(R.id.Nmonedas);
        //String monedasN = sharedPreferences.getString(monedas,"0");

        girar = (ImageView) findViewById(R.id.girar);
        ruleta = (ImageView) findViewById(R.id.ruleta);
        tvp = findViewById(R.id.TVp);
        mensa = findViewById(R.id.mensa);
        Lmensa = findViewById(R.id.Lmensa);
        bien = findViewById(R.id.bien);
        Usuario = findViewById(R.id.usuario);




        r = new Random();




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



        /*MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

        mInterstitialAd.loadAd(new AdRequest.Builder().build());*/
        inicializarFirebase();
        listarDato();

        //Log.i("TAG",String.valueOf( preferences.getFloat(monedas, 0)));



        girar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Lmensa.setVisibility(View.GONE);

                if (rewardedAd.isLoaded()) {
                    Activity activityContext = ruletaA.this;
                    RewardedAdCallback adCallback = new RewardedAdCallback() {
                        @Override
                        public void onRewardedAdOpened() {
                            // Ad opened.
                        }

                        @Override
                        public void onRewardedAdClosed() {
                            rewardedAd = createAndLoadRewardedAd();
                            // Ad closed.
                        }

                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem reward) {

                            degree_old = degree % 360;
                            degree = r.nextInt(3600) + 720;
                            RotateAnimation rotate = new RotateAnimation(degree_old, degree,
                                    RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                            rotate.setDuration(3600);
                            rotate.setFillAfter(true);
                            rotate.setInterpolator(new DecelerateInterpolator());
                            rotate.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {


                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {


                                    tvp.setText(currentNumber(360 - (degree % 360)));



                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });

                            ruleta.startAnimation(rotate);


                            // User earned reward.
                        }

                        @Override
                        public void onRewardedAdFailedToShow(AdError adError) {
                            // Ad failed to display.
                            rewardedAd = createAndLoadRewardedAd();
                        }
                    };
                    rewardedAd.show(activityContext, adCallback);
                } else {
                    Log.d("TAG", "The rewarded ad wasn't loaded yet.");
                }


            }

        });

    }

    private void fantasma() {

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

                            float Resultado = finalValor + finalValor2[0];
                            Nmoneda.setText(String.valueOf(Resultado));
                            databaseReference.child("Persona").child(preferencesU.getString(usuario, "gabriel")).child("monedas").setValue(Resultado);


                        }



                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    public RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(this,
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
        return rewardedAd;
    }

    private String currentNumber(int degrees){



        if (intentos > 0){

            text += ",";

        }



        if (degrees >= (FACTOR * 0) && degrees < (FACTOR * 2)) {

            text += "2.5";
            mensa.setImageResource(R.drawable.moneda25);
            Lmensa.setVisibility(View.VISIBLE);
            valor = (float) 2.5;

        }

        if (degrees >= (FACTOR * 2) && degrees < (FACTOR * 4)) {

            text += "1.5";
            mensa.setImageResource(R.drawable.moneda15);
            Lmensa.setVisibility(View.VISIBLE);
            valor = (float) 1.5;
        }
        if (degrees >= (FACTOR * 4) && degrees < (FACTOR * 6)) {
            text += "4.5";
            mensa.setImageResource(R.drawable.moneda45);
            Lmensa.setVisibility(View.VISIBLE);
            valor = (float) 4.5;
        }
        if (degrees >= (FACTOR * 6) && degrees < (FACTOR * 8)) {
            text += "2";
            mensa.setImageResource(R.drawable.moneda2);
            Lmensa.setVisibility(View.VISIBLE);
            valor = (float) 2.0;
        }
        if (degrees >= (FACTOR * 8) && degrees < (FACTOR * 10)) {
            text += "0.5";
            mensa.setImageResource(R.drawable.moneda05);
            Lmensa.setVisibility(View.VISIBLE);
            valor = (float) 0.5;
        }
        if (degrees >= (FACTOR * 10) && degrees < (FACTOR * 12)) {
            text += "3";
            mensa.setImageResource(R.drawable.moneda3);
            Lmensa.setVisibility(View.VISIBLE);
            valor = (float) 3.0;
        }
        if (degrees >= (FACTOR * 12) && degrees < (FACTOR * 14)) {
            text += "7";
            mensa.setImageResource(R.drawable.moneda7);
            Lmensa.setVisibility(View.VISIBLE);
            valor = (float) 7.0;
        }
        if (degrees >= (FACTOR * 14) && degrees < (FACTOR * 16)) {
            text += "1";
            mensa.setImageResource(R.drawable.moneda1);
            Lmensa.setVisibility(View.VISIBLE);
            valor = (float) 1.0;
        }

        fantasma();

        bien.playAnimation();

        intentos++;


        return text;

    }

    public void VI (View view) {

        Lmensa.setVisibility(View.GONE);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {

            Intent intent = new Intent(ruletaA.this, MainActivity.class);

            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
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
                        Nmoneda.setText(String.valueOf(pipol.getMonedas()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

}