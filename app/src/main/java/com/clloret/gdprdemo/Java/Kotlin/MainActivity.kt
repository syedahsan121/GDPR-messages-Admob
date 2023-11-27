package com.clloret.gdprdemo.Java.Kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.clloret.gdprdemo.Java.AdmobGDPR.AdmobeGdprJava.requestConsentForm
import com.clloret.gdprdemo.Java.Kotlin.InAppUpdateManager.checkForUpdates
import com.clloret.gdprdemo.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


         ///for check app update
        InAppUpdateManager.init(this)
        checkForUpdates(this,100)

        ///for inappreview
        InAppReviewHelper.launchInAppReview(this) { isSuccess ->
            if (isSuccess) {
                // The in-app review process completed successfully
                // You can perform additional actions here if needed
                Log.d("InAppReview", "Review completed successfully")
            } else {
                // The in-app review process failed or was cancelled
                // You can handle the failure case here
                Log.d("InAppReview", "Review failed or cancelled")
            }
        }

        requestConsentForm(this)
    }



}
