package com.example.ads

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

object AdMobManager {

  // Official Google AdMob Sample Test Ad Unit IDs
  const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
  const val TEST_INTERSTITIAL_AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"
  const val TEST_REWARDED_AD_UNIT_ID = "ca-app-pub-3940256099942544/5224354917"

  private var isInitialized = false
  private var interstitialAd: InterstitialAd? = null
  private var rewardedAd: RewardedAd? = null

  /**
   * Initializes the Google Mobile Ads SDK once per app process.
   */
  fun initialize(context: Context, onComplete: () -> Unit = {}) {
    if (isInitialized) {
      onComplete()
      return
    }
    try {
      MobileAds.initialize(context) {
        isInitialized = true
        onComplete()
      }
    } catch (_: Throwable) {
      onComplete()
    }
  }

  /**
   * Loads a native Kotlin AdMob Interstitial ad.
   */
  fun loadInterstitial(
    context: Context,
    adUnitId: String = TEST_INTERSTITIAL_AD_UNIT_ID,
    onLoaded: () -> Unit = {},
    onFailed: (String) -> Unit = {}
  ) {
    val adRequest = AdRequest.Builder().build()
    InterstitialAd.load(
      context,
      adUnitId,
      adRequest,
      object : InterstitialAdLoadCallback() {
        override fun onAdLoaded(ad: InterstitialAd) {
          interstitialAd = ad
          onLoaded()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
          interstitialAd = null
          onFailed(loadAdError.message)
        }
      }
    )
  }

  /**
   * Displays the loaded Interstitial ad.
   */
  fun showInterstitial(
    activity: Activity,
    onDismissed: () -> Unit = {}
  ) {
    interstitialAd?.let { ad ->
      ad.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
          interstitialAd = null
          onDismissed()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
          interstitialAd = null
          onDismissed()
        }
      }
      ad.show(activity)
    } ?: run {
      onDismissed()
    }
  }

  /**
   * Loads a native Kotlin AdMob Rewarded ad.
   */
  fun loadRewarded(
    context: Context,
    adUnitId: String = TEST_REWARDED_AD_UNIT_ID,
    onLoaded: () -> Unit = {},
    onFailed: (String) -> Unit = {}
  ) {
    val adRequest = AdRequest.Builder().build()
    RewardedAd.load(
      context,
      adUnitId,
      adRequest,
      object : RewardedAdLoadCallback() {
        override fun onAdLoaded(ad: RewardedAd) {
          rewardedAd = ad
          onLoaded()
        }

        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
          rewardedAd = null
          onFailed(loadAdError.message)
        }
      }
    )
  }

  /**
   * Displays the loaded Rewarded ad.
   */
  fun showRewarded(
    activity: Activity,
    onRewardEarned: (Int) -> Unit,
    onDismissed: () -> Unit = {}
  ) {
    rewardedAd?.let { ad ->
      ad.fullScreenContentCallback = object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
          rewardedAd = null
          onDismissed()
        }

        override fun onAdFailedToShowFullScreenContent(adError: AdError) {
          rewardedAd = null
          onDismissed()
        }
      }
      ad.show(activity) { rewardItem ->
        onRewardEarned(rewardItem.amount)
      }
    } ?: run {
      onDismissed()
    }
  }

  /**
   * Loads an AdView for traditional XML layouts / Views.
   */
  fun setupXmlAdView(adView: AdView) {
    val adRequest = AdRequest.Builder().build()
    adView.loadAd(adRequest)
  }
}

/**
 * Native Jetpack Compose Banner Ad component.
 * Embeds Google AdMob's native Android AdView directly into Compose UI seamlessly.
 */
@Composable
fun AdBannerView(
  modifier: Modifier = Modifier,
  adUnitId: String = AdMobManager.TEST_BANNER_AD_UNIT_ID,
  adSize: AdSize = AdSize.BANNER
) {
  AndroidView(
    modifier = modifier
      .fillMaxWidth()
      .height(50.dp)
      .testTag("admob_banner_view"),
    factory = { context ->
      try {
        AdView(context).apply {
          setAdSize(adSize)
          setAdUnitId(adUnitId)
          loadAd(AdRequest.Builder().build())
        }
      } catch (_: Throwable) {
        android.view.View(context)
      }
    },
    update = { adView ->
      // Kept active across recompositions
    }
  )
}
