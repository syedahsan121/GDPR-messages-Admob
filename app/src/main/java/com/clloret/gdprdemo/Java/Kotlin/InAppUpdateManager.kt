package com.clloret.gdprdemo.Java.Kotlin

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

object InAppUpdateManager {

    private lateinit var appUpdateManager: AppUpdateManager

    fun init(context: Context) {
        appUpdateManager = AppUpdateManagerFactory.create(context)
    }

    fun checkForUpdates(activity: Activity, requestCode: Int) {
        appUpdateManager = AppUpdateManagerFactory.create(activity)
        val appUpdateInfoTask: Task<AppUpdateInfo> =
            appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            Log.d("TAG", "checkForUpdates: success " + appUpdateInfo.updateAvailability())
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    activity,
                    requestCode
                )
            }
        }
        appUpdateInfoTask.addOnFailureListener {
            Log.d("TAG", "checkForUpdates: failed $it")
        }
    }
}
