package app.casino.ruleta;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import java.text.DecimalFormat;
import app.casino.ruleta.model.AdManager;
import app.casino.ruleta.model.actualizarMonedas;
import in.myinnos.androidscratchcard.ScratchCard;

public class rasca extends AppCompatActivity {

    private ScratchCard mScratchCard;
    private TextView gana;
    private TextView nmoneda;

    private LottieAnimationView video;
    private TextView PLAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rasca);

        gana = findViewById(R.id.Gana);
        video = findViewById(R.id.video);
        video.setAnimation(R.raw.play);
        video.playAnimation();

        mScratchCard = (ScratchCard) findViewById(R.id.scratchCard);

        nmoneda = findViewById(R.id.Nmonedas);

        actualizarMonedas.readUser(this,nmoneda);

        rasco();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(rasca.this, MainActivity.class);

            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }

    public void rasco(){

        float valor = (float) (Math.random() * (5 - 0.1) + 0.1);

        DecimalFormat formato1 = new DecimalFormat("#.0");

        float valor1 = Float.parseFloat(formato1.format(valor));
        gana.setText(String.valueOf(valor));

        mScratchCard.setOnScratchListener(new ScratchCard.OnScratchListener() {

            @Override
            public void onScratch(ScratchCard scratchCard, float visiblePercent) {

                if (visiblePercent > 0.3) {

                    mScratchCard.setVisibility(View.GONE);

                    float finalValor2 = Float.parseFloat((String) nmoneda.getText());

                    Log.i("valores", String.valueOf(valor1));

                    float Resultado = valor1 + finalValor2;
                    nmoneda.setText(String.valueOf(Resultado));
                    actualizarMonedas.updateUser(getApplicationContext(),String.valueOf(Resultado));

                }
            }
        });
    }

    public void ruleta(View view){

        Intent intent = new Intent(rasca.this, rasca.class);

        startActivity(intent);

    }

    public void playvideo (View view) {

        AdManager.getInstance();
        RewardedAd rewardedAd = AdManager.getAd();

        if (rewardedAd.isLoaded()) {
            Activity activityContext = rasca.this;
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {

                    AdManager.createAd(rasca.this);

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
                    AdManager.createAd(rasca.this);
                }
            };
            rewardedAd.show(activityContext, adCallback);
        } else {
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
        }
    }
}