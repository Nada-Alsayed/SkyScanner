package eg.gov.iti.skyscanner.workmanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eg.gov.iti.skyscanner.DataBase.ConcreteLocalSource
import eg.gov.iti.skyscanner.DialogActivity
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.alert.view.AlertConst
import eg.gov.iti.skyscanner.models.MyIcons.ALERT
import eg.gov.iti.skyscanner.models.MyIcons.DESCRIPTION
import eg.gov.iti.skyscanner.models.MyIcons.FROM_TIME_IN_MILLIS
import eg.gov.iti.skyscanner.models.MyIcons.ICON
import eg.gov.iti.skyscanner.models.MyIcons.convertToUserAlert
import eg.gov.iti.skyscanner.models.MyIcons.convertUserAlertToString
import eg.gov.iti.skyscanner.models.MyIcons.openNotification
import eg.gov.iti.skyscanner.models.Repository
import eg.gov.iti.skyscanner.models.RepositoryInterface
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.network.APIClient
import eg.gov.iti.skyscanner.network.RemoteSource
import eg.gov.iti.skyscanner.settings.view.Language
import eg.gov.iti.skyscanner.settings.view.Notification
import eg.gov.iti.skyscanner.workmanager.WorkRequestManager.removeWork
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MyCoroutineWorker (private val context: Context, parameters: WorkerParameters): CoroutineWorker(context, parameters) {
   // lateinit var remoteSource: RemoteSource
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    private lateinit var notificationRepository: RepositoryInterface
    override suspend fun doWork(): Result {
        sharedPreference = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
        val notification = sharedPreference.getString(Notification, "enable")
       // Toast.makeText(context,""+notification,Toast.LENGTH_SHORT).show()
        Log.e("TAG", "doWork: *************+++++++"+notification )
        CoroutineScope(Dispatchers.IO).launch {
            Log.e("TAG", "doWork: *************" )
            val myAlert = convertToUserAlert(inputData.getString(ALERT)!!)
            val description = inputData.getString(DESCRIPTION)
            val icon = inputData.getString(ICON)
            val fromTimeInMillis = inputData.getLong(FROM_TIME_IN_MILLIS, 0L)

            notificationRepository =
                Repository.getInstance(APIClient.getInstance(), ConcreteLocalSource(applicationContext))

            if (checkTime(myAlert)) {
                Log.e("TAG", "doWork: *************222 " +description+icon+fromTimeInMillis)
                if (notification.equals("enable")){
                    openNotification(context, myAlert, description!!, icon!!, context.getString(R.string.app_name))
                    Log.e("TAG", "doWork: *************333" )
               }

                if (myAlert.alertOption == "null") {
                    if (Settings.canDrawOverlays(context)) {
                        Log.e("TAG", "doWork: *************4444" )
                        withContext(Dispatchers.Main) {
                            val intent = Intent(context, DialogActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_INCLUDE_STOPPED_PACKAGES )
                            intent.putExtra(DESCRIPTION, description)
                            intent.putExtra(ICON, icon)
                            Log.e("TAG", "doWork: *************4444"+icon+description )
                            context.startActivity(intent)
                        }
                    }
                }

                WorkRequestManager.createWorkRequest(
                    myAlert,
                    description.toString(),
                    icon.toString(),
                    context,
                    (fromTimeInMillis + 86400000)
                )
            } else {
                notificationRepository.deleteAlert(myAlert)
                removeWork("${myAlert.startLongDate}", context)
            }
        }

        return Result.success()
    }

    private fun checkTime(alert: UserAlerts): Boolean {
        val currentTimeInMillis = Calendar.getInstance().timeInMillis
        val res=((currentTimeInMillis >= alert.startLongDate)&& (currentTimeInMillis <= alert.endLongDate))
        Log.e("TAG", "checktime: __________"+res)
        return res
    }
}