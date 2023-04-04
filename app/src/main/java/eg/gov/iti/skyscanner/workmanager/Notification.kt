package eg.gov.iti.skyscanner.workmanager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.mainactivity.view.MainActivity
import eg.gov.iti.skyscanner.models.MyIcons.aPIIconInt
import eg.gov.iti.skyscanner.models.MyIcons.replaceAPIIcon

class Notification (
    var context: Context,
    var description: String,
    var icon: String,
    var title: String
    ) : ContextWrapper(context) {

        val CHANNEL_ID = "Channel ID"
        val CHANNEL_NAME = "Channel Name"
        val CHANNEL_DESCRIPTION = "Channel Name"

        private var mManager: NotificationManager? = null
        var uniqueInt = (System.currentTimeMillis() and 0xfffffff).toInt()

        init {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel()
            }
        }

       // @RequiresApi(Build.VERSION_CODES.O)
        private fun createChannel() {
            val channel =
                NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)

            channel.enableVibration(true)
            channel.description = CHANNEL_DESCRIPTION
            getManager()?.createNotificationChannel(channel)
        }

        fun getManager(): NotificationManager? {
            if (mManager == null) {
                mManager = getSystemService(NotificationManager::class.java)
            }
            return mManager
        }

        fun getChannelNotification(): NotificationCompat.Builder {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK )

//        intent.putExtra(LOCATION_LATITUDE, weatherModel.lat)
//        intent.putExtra(LOCATION_LONGITUDE, weatherModel.lon)

            val bitmap = BitmapFactory.decodeResource(applicationContext.resources, aPIIconInt(icon))
            val flags = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_ONE_SHOT
           // @SuppressLint("UnspecifiedImmutableFlag")
            val pendingIntent = PendingIntent.getActivity(
                context,
               0,
                intent,
                flags
            )
            return NotificationCompat.Builder(
                applicationContext,
                CHANNEL_ID
            )
                .setContentTitle(title)
                .setContentText(description) // getText(R.string.open_dialogue)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // you can delete this line
               .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(aPIIconInt(icon))
               // .setSmallIcon(R.drawable.app_icon)
                .setLargeIcon(bitmap)
        }
    }