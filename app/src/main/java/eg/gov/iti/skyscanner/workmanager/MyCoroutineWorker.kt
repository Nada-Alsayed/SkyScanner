package eg.gov.iti.skyscanner.workmanager

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.PixelFormat
import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import eg.gov.iti.skyscanner.DataBase.ConcreteLocalSource
import eg.gov.iti.skyscanner.DialogActivity
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.databinding.AlarmWindowBinding
import eg.gov.iti.skyscanner.models.MyIcons
import eg.gov.iti.skyscanner.models.MyIcons.ALERT
import eg.gov.iti.skyscanner.models.MyIcons.DESCRIPTION
import eg.gov.iti.skyscanner.models.MyIcons.FROM_TIME_IN_MILLIS
import eg.gov.iti.skyscanner.models.MyIcons.ICON
import eg.gov.iti.skyscanner.models.MyIcons.convertToUserAlert
import eg.gov.iti.skyscanner.models.MyIcons.openNotification
import eg.gov.iti.skyscanner.models.Repository
import eg.gov.iti.skyscanner.models.RepositoryInterface
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.network.APIClient
import eg.gov.iti.skyscanner.settings.view.Notification
import eg.gov.iti.skyscanner.workmanager.WorkRequestManager.removeWork
import kotlinx.coroutines.*
import java.time.LocalTime
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
                    openNotification(applicationContext, myAlert, description!!, icon!!, context.getString(R.string.app_name))
                    Log.e("TAG", "doWork: *************333" )
               }

                if (myAlert.alertOption == "") {
                    if (Settings.canDrawOverlays(context)) {
                        Log.e("TAG", "doWork: *************4444" )
                       GlobalScope.launch(Dispatchers.Main){
                           SetupAlarm(applicationContext, description!! ,icon!!).onCreate()
                       }

                        /*withContext(Dispatchers.Main) {
                            val intent = Intent(context, DialogActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_INCLUDE_STOPPED_PACKAGES )
                            intent.putExtra(DESCRIPTION, description)
                            intent.putExtra(ICON, icon)
                            Log.e("TAG", "doWork: *************4444"+icon+description )
                            context.startActivity(intent)
                        }*/
                    }
                }
                notificationRepository.deleteAlert(myAlert)
                removeWork("${myAlert.startLongDate}", applicationContext)

                WorkRequestManager.createWorkRequest(
                    myAlert,
                    description.toString(),
                    icon.toString(),
                    context,
                    (fromTimeInMillis + 86400000)
                )
            }
            else {
                notificationRepository.deleteAlert(myAlert)
                removeWork("${myAlert.startLongDate}", applicationContext)
            }
        }

        return Result.success()
    }

    private fun checkTime(alert: UserAlerts): Boolean {
        val calendar = Calendar.getInstance()
        val currentTime2 = calendar.time.time
        val currentDateInMillis =  Date().time
        val res=(currentDateInMillis >= alert.startLongDate) &&(currentDateInMillis <= alert.endLongDate+300000)&&(currentTime2>=alert.timeAlert)
        Log.e("TAG", "checktime: __________ "+res +alert.startLongDate+"  "+alert.endLongDate+"  "+alert.timeAlert)
        Log.e("TAG", "checktime: __________ "+res +currentDateInMillis+"  "+currentDateInMillis+"  "+currentTime2)
        return res
    }

}

class SetupAlarm(
    private val context: Context,
    private val description: String,
    private val icon: String
) {
    lateinit var binding: AlarmWindowBinding
    private lateinit var customDialog: View
    private var mediaPlayer: MediaPlayer = MediaPlayer.create(context, R.raw.sound)


    fun onCreate() {
        mediaPlayer.start()
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        customDialog = inflater.inflate(R.layout.alarm_window, null)
        binding = AlarmWindowBinding.bind(customDialog)
        initView()
        val LAYOUT_FLAG: Int = Flag()
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val layoutParams: WindowManager.LayoutParams = Params(LAYOUT_FLAG)
        windowManager.addView(customDialog, layoutParams)

    }

    private fun Flag(): Int {
        val LAYOUT_FLAG: Int
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        return LAYOUT_FLAG
    }

    private fun Params(LAYOUT_FLAG: Int): WindowManager.LayoutParams {
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
         //val params=
        return WindowManager.LayoutParams(
            width,
            WindowManager.LayoutParams.WRAP_CONTENT,
            LAYOUT_FLAG,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE,
            PixelFormat.TRANSLUCENT
        )
       /*     params .gravity=Gravity.TOP
        params.x=0
        params.y=0
        return params*/
    }

    private fun initView() {
        binding.image.setImageResource(MyIcons.aPIIconInt(icon))
        binding.textDescription.text = description
        binding.btnDismiss.setOnClickListener {
            //mediaPlayer!!.stop()
            close()
        }
    }
    private fun close() {
        try {
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(
                customDialog
            )
            customDialog.invalidate()
            (customDialog.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
        mediaPlayer.release()
    }
}