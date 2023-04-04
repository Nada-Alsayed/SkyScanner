package eg.gov.iti.skyscanner.workmanager

import android.content.Context
import android.util.Log
import androidx.work.*

import eg.gov.iti.skyscanner.models.MyIcons.ALERT
import eg.gov.iti.skyscanner.models.MyIcons.DESCRIPTION
import eg.gov.iti.skyscanner.models.MyIcons.FROM_TIME_IN_MILLIS
import eg.gov.iti.skyscanner.models.MyIcons.ICON
import eg.gov.iti.skyscanner.models.MyIcons.convertUserAlertToString
import eg.gov.iti.skyscanner.models.UserAlerts
import java.util.*
import java.util.concurrent.TimeUnit

object WorkRequestManager {
    fun createWorkRequest(alert: UserAlerts,
                          description: String,
                          icon: String,
                          context: Context,
                          fromTimeInMillis: Long) {

        Log.e("TAG", "work request: *************999 " +  description + icon+fromTimeInMillis)

        val data = Data.Builder()
            .putString(ALERT, convertUserAlertToString(alert))
            .putString(DESCRIPTION, description)
            .putString(ICON, icon)
            .putLong(FROM_TIME_IN_MILLIS, fromTimeInMillis)
            .build()

        val workRequest = PeriodicWorkRequestBuilder<MyCoroutineWorker>(1,TimeUnit.DAYS)
            .setInitialDelay(fromTimeInMillis - Calendar.getInstance().timeInMillis, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .setConstraints(Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
            .addTag(alert.startLongDate.toString())
            .build()

        WorkManager
            .getInstance(context)
           .enqueueUniquePeriodicWork("${alert.startLongDate}", ExistingPeriodicWorkPolicy.REPLACE, workRequest)
            //.enqueue(oneTimeWorkRequest)
    }

    fun removeWork(tag: String, context: Context) {
        val worker = WorkManager.getInstance(context)
        worker.cancelAllWorkByTag(tag)
    }
}