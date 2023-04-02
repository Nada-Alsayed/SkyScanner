package eg.gov.iti.skyscanner

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.*

object LanguageManager {
    /*fun setLanguage(context: Activity, language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val resource=context.resources
        val config = resource.configuration
       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
            resource.updateConfiguration(config,resource.displayMetrics)
            //context.createConfigurationContext(config)
       } else {
                 config.locale = locale
                 context.resources.updateConfiguration(config, context.resources.displayMetrics)
             }

    }*/

    fun  setLanguage(context: Context, language:String) {

        val locale = Locale(language)
        val config = context.resources.configuration
        config.locale= Locale(language)
        Locale.setDefault(locale)
        //config.setLocale(locale)
        config.setLayoutDirection(Locale(language))
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
        context.createConfigurationContext(config)
        //config.onConfigurationChanged(config)
    }
}