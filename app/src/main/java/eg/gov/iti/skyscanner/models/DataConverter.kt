package eg.gov.iti.skyscanner.models

import androidx.databinding.InverseMethod

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