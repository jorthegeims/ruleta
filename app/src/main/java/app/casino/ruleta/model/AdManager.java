package app.casino.ruleta.model;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdManager {

    private static AdManager singleton;
    private static RewardedAd interstitialAd;

    public AdManager() {
    }

    /***
     * returns an instance of this class. if singleton is null create an instance
     * else return  the current instance
     */
    public static void getInstance() {
        if (singleton == null) {
            singleton = new AdManager();
        }

    }

    /***
     * Create an interstitial ad
     * @param context
     */
    public static void createAd(Context context) {
        interstitialAd = new RewardedAd(context,
                "ca-app-pub-5014261020839028/1489267336");
        //ca-app-pub-3940256099942544/5224354917
        //ca-app-pub-5014261020839028/1489267336


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
        interstitialAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);

    }

    /***
     * get an interstitial Ad
     * @return
     */
    public static RewardedAd getAd() {
        return interstitialAd;
    }
}