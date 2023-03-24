package eg.gov.iti.skyscanner.models

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import com.bumptech.glide.Glide

object DataConverter {
    @InverseMethod("convertIntToString")
    fun convertStringToInt(value: String): Int {
        return try {
            value.toInt()
        } catch (e: NumberFormatException) {
            -1
        }
    }
    fun convertIntToString(value: Int): String? {
        return try {
            value.toString()
        } catch (e: NumberFormatException) {
            e.printStackTrace().toString()
        }
    }
    @InverseMethod("convertDoubleToFloat")
    fun convertFloatToDouble(value:Float):Double?{
        return value.toString().toDouble()

    }
    fun convertDoubleToFloat(value: Double):Float? {
        return value.toString().toFloat()

    }

}
@BindingAdapter("imgUrl")
fun loadImage(view: ImageView, url: String) {
    Glide.with(view.context).load(url).into(view)
}