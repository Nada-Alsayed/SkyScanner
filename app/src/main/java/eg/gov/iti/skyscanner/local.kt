package eg.gov.iti.skyscanner

import android.content.Context
import android.content.res.Configuration
import java.util.*

class local {
   /*fun setLocale( c: Context) {
        setNewLocale(c, getLanguage(c));
    }*/

    fun  setNewLocale(c: Context, language:String ) {
       // persistLanguage(c, language);
        updateResources(c, language)
    }

    /*fun  getLanguage(c: Context) { ... }*/

/*
    fun  persistLanguage(c: Context, String language) { ... }
*/
fun  updateResources(context: Context, language:String) {

    val locale = Locale(language)
    Locale.setDefault(locale)
    val config = context.resources.configuration
    config.setLocale(locale)
    context.createConfigurationContext(config)
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
}
    /*fun  updateResources(c: Context, language:String) {
       val locale =  Locale(language);
        Locale.setDefault(locale);

        val res = c.getResources();
        val config = Configuration(res.getConfiguration());
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }*/
}