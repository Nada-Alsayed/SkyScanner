package eg.gov.iti.skyscanner.alert.view

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eg.gov.iti.skyscanner.databinding.RvRowAlertBinding
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.network.RetrofitHelper
class AdapterAlert (private val context: Context,
                    private val onButtonClickListener:OnClickAlertInterface,
                    private var userAlerts:List<UserAlerts>,
                    private val lang:String): RecyclerView.Adapter<AdapterAlert.ViewHolder>()
{

    private lateinit var binding: RvRowAlertBinding
    inner class ViewHolder(var binding: RvRowAlertBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setFavList(list: List<UserAlerts>) {
        userAlerts= list
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RvRowAlertBinding.inflate(inflater, parent, false)
        Log.i(ContentValues.TAG, "onCreateViewHolder: ")
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return userAlerts.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (userAlerts != null) {
            var myAlert: UserAlerts = userAlerts[position]
            holder.binding.txtStartDay.text = RetrofitHelper.convertTimeInMillisIntoString(myAlert.startLongDate,"dd/MM/yyyy",lang)
            holder.binding.timeStartDate.text = RetrofitHelper.convertTimeInMillisIntoString(myAlert.startLongDate,"HH:mm a",lang)
            holder.binding.txtEndDay.text = RetrofitHelper.convertTimeInMillisIntoString(myAlert.endLongDate,"dd/MM/yyyy",lang)
            holder.binding.timeEndDate.text = RetrofitHelper.convertTimeInMillisIntoString(myAlert.endLongDate,"HH:mm a",lang)
            holder.binding.iconDel.setOnClickListener {
                onButtonClickListener.deleteOnClick(myAlert)
            }
        }
    }
}