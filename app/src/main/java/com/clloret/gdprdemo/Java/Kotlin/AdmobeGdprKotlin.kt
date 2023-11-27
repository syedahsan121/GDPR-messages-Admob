package com.clloret.gdprdemo.Java.Kotlin

import android.app.Activity
import android.util.Log
import com.google.android.gms.ads.MobileAds
import com.google.android.ump.*
import java.util.concurrent.atomic.AtomicBoolean

object AdmobeGdprKotlin {


    private lateinit var consentInformation: ConsentInformation
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)


    fun requestconsentfome(activity: Activity) {
//        ///The below code is for testing purpose only.Remove it in production build
//        val debugSettings = ConsentDebugSettings.Builder(activity)
//            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
//            .addTestDeviceHashedId("CF8C2FFDD37D9F39641BE2137B6E1070")
//            .build()
        val params = ConsentRequestParameters

            .Builder()
//            .setConsentDebugSettings(debugSettings) // Remove for production build
            .setTagForUnderAgeOfConsent(false)
            .build()

        consentInformation = UserMessagingPlatform.getConsentInformation(activity)
//        consentInformation.reset() // Remove for Production build
        consentInformation.requestConsentInfoUpdate( activity,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    activity ,
                    ConsentForm.OnConsentFormDismissedListener {
                            loadAndShowError ->
                        // Consent gathering failed.
                        if (loadAndShowError != null) {
                            Log.w("CheckGDPR", String.format("%s: %s",
                                loadAndShowError.errorCode,
                                loadAndShowError.message
                            ))
                        }

                        // Consent has been gathered.
                        if (consentInformation.canRequestAds()) {
                            initializeMobileAdsSdk(activity)
                        }
                    }
                )
            },
            {
                    requestConsentError ->
                // Consent gathering failed.
                Log.w("CheckGDPR", String.format("%s: %s",
                    requestConsentError.errorCode,
                    requestConsentError.message
                ))
            })
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk(activity)
        }

    }
    private fun initializeMobileAdsSdk(activity: Activity) {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        MobileAds.initialize(activity)
    }
}