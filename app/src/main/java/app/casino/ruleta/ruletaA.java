package app.casino.ruleta;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import java.util.Random;
import app.casino.ruleta.model.AdManager;
import app.casino.ruleta.model.actualizarMonedas;

public class ruletaA extends AppCompatActivity {

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

    private RewardedAd rewardedAd;

    private float valor = (float) 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruleta);

        Nmoneda = findViewById(R.id.Nmonedas);

        girar = findViewById(R.id.girar);
        ruleta = findViewById(R.id.ruleta);
        tvp = findViewById(R.id.TVp);
        mensa = findViewById(R.id.mensa);
        Lmensa = findViewById(R.id.Lmensa);
        bien = findViewById(R.id.bien);

        r = new Random();

        actualizarMonedas.readUser(this,Nmoneda);

        girar.setOnClickListener(v -> {

            AdManager.getInstance();
            rewardedAd =  AdManager.getAd();

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
                        AdManager.createAd(ruletaA.this);
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
                        AdManager.createAd(ruletaA.this);
                    }
                };
                rewardedAd.show(activityContext, adCallback);
            } else {
                Log.d("TAG", "The rewarded ad wasn't loaded yet.");
            }
        });

    }

    private void fantasma() {

        float finalValor = valor;
        float finalValor2 = Float.parseFloat((String) Nmoneda.getText());

        Log.i("valores", String.valueOf(finalValor));

        float Resultado = finalValor + finalValor2;
        Nmoneda.setText(String.valueOf(Resultado));
        actualizarMonedas.updateUser(getApplicationContext(),String.valueOf(Resultado));

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
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(ruletaA.this, MainActivity.class);

            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }



}