package com.clloret.gdprdemo.Java.AdmobGDPR;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.util.concurrent.atomic.AtomicBoolean;

public class AdmobeGdprJava {


    private static ConsentInformation consentInformation;
    private static final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);

    public static void requestConsentForm(Activity activity) {
        // The below code is for testing purposes only. Remove/Commit  it in the production build.
     ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
             ///replase your device id
             ///get your id from logcat to search debug setting
              .addTestDeviceHashedId("9DA99C54F195ACB9DABB5570BE537974")
              .build();

        ConsentRequestParameters params = new ConsentRequestParameters.Builder()
                .setConsentDebugSettings(debugSettings) // Remove for production build
                .setTagForUnderAgeOfConsent(false)
                .build();

        consentInformation = UserMessagingPlatform.getConsentInformation(activity);
       consentInformation.reset(); // Remove for Production build

        consentInformation.requestConsentInfoUpdate(activity, params,
                () -> UserMessagingPlatform.loadAndShowConsentFormIfRequired(activity,
                        (ConsentForm.OnConsentFormDismissedListener) loadAndShowError -> {
                            // Consent gathering failed.
                            if (loadAndShowError != null) {
                                Log.w("CheckGDPR", String.format("%s: %s",
                                        loadAndShowError.getErrorCode(),
                                        loadAndShowError.getMessage()
                                ));
                            }

                            // Consent has been gathered.
                            if (consentInformation.canRequestAds()) {
                                initializeMobileAdsSdk(activity);
                            }
                        }),
                requestConsentError -> {
                    // Consent gathering failed.
                    Log.w("CheckGDPR", String.format("%s: %s",
                            requestConsentError.getErrorCode(),
                            requestConsentError.getMessage()
                    ));
                });

        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk(activity);
        }
    }

    private static void initializeMobileAdsSdk(Activity activity) {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return;
        }

        MobileAds.initialize(activity);
    }
}
