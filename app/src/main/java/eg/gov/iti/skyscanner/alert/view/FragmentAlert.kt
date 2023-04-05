package eg.gov.iti.skyscanner.alert.view

import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import eg.gov.iti.skyscanner.DataBase.ConcreteLocalSource
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.alert.viewmodel.AlertViewModel
import eg.gov.iti.skyscanner.alert.viewmodel.AlertViewModelFactory
import eg.gov.iti.skyscanner.databinding.DialogAlertBinding
import eg.gov.iti.skyscanner.databinding.FragmentAlertBinding
import eg.gov.iti.skyscanner.models.Repository
import eg.gov.iti.skyscanner.models.UserAlerts
import eg.gov.iti.skyscanner.models.WeatherDetail
import eg.gov.iti.skyscanner.network.APIClient
import eg.gov.iti.skyscanner.network.RequestState
import eg.gov.iti.skyscanner.settings.view.Language
import eg.gov.iti.skyscanner.workmanager.WorkRequestManager.createWorkRequest
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*

class FragmentAlert : Fragment(), OnClickAlertInterface {
    private lateinit var myView: View
    private lateinit var dialog: Dialog
    private lateinit var binding: FragmentAlertBinding
    lateinit var sharedPreference: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var adapterAlert: AdapterAlert
    lateinit var viewModel: AlertViewModel
    lateinit var viewModelFactory: AlertViewModelFactory
    lateinit var alertActBinding: DialogAlertBinding
    private lateinit var userAlerts: UserAlerts
    private var startLongDate: Long = 0
    private var endLongDate: Long = 0
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var time: String
    override fun onResume() {
        super.onResume()
        val activity: Activity? = activity
        if (activity != null) {
            activity.title = getString(eg.gov.iti.skyscanner.R.string.alerts)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myView = view
        init()
    }

    private fun init() {
        userAlerts= UserAlerts(0,1,1,"")
        viewModelFactory = AlertViewModelFactory(
            Repository.getInstance(
                APIClient.getInstance(), ConcreteLocalSource(requireContext())
            )
        )
        sharedPreference = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        editor = sharedPreference.edit()
        val lang = sharedPreference.getString(Language, "en")
        viewModel = ViewModelProvider(this, viewModelFactory)[AlertViewModel::class.java]

        adapterAlert = lang?.let { AdapterAlert(requireContext(), this, emptyList(), it) }!!

        dialog = Dialog(myView.context)
        alertActBinding = DialogAlertBinding.inflate(layoutInflater)
        dialog.setContentView(alertActBinding.root)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        addListeners()

        viewModel.getStoredAlert()
        lifecycleScope.launch() {
            viewModel.alertFromRoom.collect { db ->
                when (db) {
                    is RequestState.Success -> {

                        db.data?.let { adapterAlert.setAlertList(it) }
                        binding.RVAlert.adapter = adapterAlert
                    }
                    else -> {
                        binding.pBar.visibility = View.VISIBLE
                    }
                }
            }
        }


    }

    private fun setOneTimeWorkRequest(alert: UserAlerts, description: String, icon: String) {
        Log.e("TAG", "notify: *************8888 " + description + icon)

        createWorkRequest(alert, description, icon, myView.context, alert.timeAlert)
    }

    /*fun askForDrawOverlaysPermission(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(activity)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + activity.packageName))
            activity.startActivityForResult(intent, requestCode)
        }
    }*/
    private fun askForDrawOverlaysPermission() {
        if (!Settings.canDrawOverlays(requireView().context)) {
            if ("xiaomi" == Build.MANUFACTURER.lowercase(Locale.ROOT)) {
                val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
                intent.setClassName(
                    "com.miui.securitycenter",
                    "com.miui.permcenter.permissions.PermissionsEditorActivity"
                )
                intent.putExtra("extra_pkgname", requireView().context.packageName)
                AlertDialog.Builder(requireView().context)
                    .setTitle(R.string.draw_overlays)
                    .setMessage(R.string.draw_overlays_description)
                    .setPositiveButton(R.string.go_to_settings) { dialog, which ->
                        startActivity(intent)
                    }
                    .setIcon(R.drawable.baseline_warning_amber_24)
                    .show()
            } else {
                AlertDialog.Builder(requireView().context)
                    .setTitle(R.string.warning)
                    .setMessage(R.string.error_msg_permission_required)
                    .setPositiveButton(R.string.ok) { _, _ ->
                        val permissionIntent = Intent(
                            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + requireView().context.packageName)
                        )
                        runtimePermissionResultLauncher.launch(permissionIntent)
                    }
                    .setIcon(R.drawable.baseline_warning_amber_24)
                    .show()
            }
        }

    }

    private val runtimePermissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { }

    private fun addListeners() {
        binding.fabAlert.setOnClickListener {
            val pm = requireView().context.getSystemService(Context.POWER_SERVICE) as PowerManager
            if (!Settings.canDrawOverlays(requireView().context)) {
                askForDrawOverlaysPermission()
            } else if (!pm.isIgnoringBatteryOptimizations(myView.context.packageName)) {
                Log.e("TAG", "addListeners: *******")
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:" + requireView().context.packageName)
                startActivity(intent)
            } else {
                dialog.show()
            }

        }
        viewModel.getStoredWeather()

        alertActBinding.startDateId.setOnClickListener {
           // showDateTimePicker(AlertConst.START_EDIT_TEXT)
            setAlarmDateAndTime{(timeInMillis, dateInMillis) ->
                time=getTimeToAlert(timeInMillis,"en")
                startDate=getDateToAlert(dateInMillis,"en")
                alertActBinding.startDateId.setText("$startDate \n $time")
                userAlerts.startLongDate = dateInMillis
                userAlerts.timeAlert = timeInMillis
                Log.d("Alarm", "Time: $timeInMillis, Date: $dateInMillis")
            }
        }
        //endDateid = dialog.findViewById(R.id.endDateid)
        alertActBinding.endDateid.setOnClickListener {
           setAlarmEndDate {
               endDate=getDateToAlert(it,"en")
               userAlerts.endLongDate=it
               alertActBinding.endDateid.setText("$endDate \n $time")
           }
        }
        /*alertOptions.setOnCheckedChangeListener{_,checkedId->
            val radioButton: RadioButton = dialog.findViewById(checkedId)
            option = radioButton.getText().toString()
            if (option == getString(R.string.alarm)) {
                option = AlertsConstants.ALARM
            }
            else {
                option = AlertsConstants.NOTIFICATION
            }
        }*/

        alertActBinding.saveAlertsData.setOnClickListener {
            if (alertActBinding.startDateId.text.toString().isEmpty()) {
                alertActBinding.startDateId.error = "Field is required"
            } else if (alertActBinding.endDateid.text.toString().isEmpty()) {
                alertActBinding.endDateid.error = "Field is required"
            } else {
                storeInRoom()
                /*Log.e("TAG", "startDate :$startLongDate")
                Log.e("TAG", "endDate :$endLongDate")
                Log.e("TAG", "option :$option")
*/
                alertActBinding.startDateId.setText("")
                alertActBinding.endDateid.setText("")
                dialog.cancel()
            }
        }
    }

    private fun storeInRoom() {
        runBlocking {
            lifecycleScope.launch() {
              //  userAlerts = UserAlerts(startLongDate = startLongDate,endLongDate = endLongDate, alertOption = "null")
                viewModel.insertAlert(userAlerts)
            }.join()

            viewModel.getStoredWeather()
            lifecycleScope.launch() {
                viewModel.allWeatherFromRoom.collectLatest { alert ->
                    when (alert) {
                        is RequestState.Success -> {
                            val weather = alert.data?.get(0)?.let {
                                WeatherDetail(
                                    it.alerts,
                                    it.current,
                                    it.daily,
                                    it.hourly,
                                    it.lat,
                                    it.lon,
                                    it.timezone,
                                    it.timezone_offset,
                                    it.isFav
                                )
                            }
                            if (weather != null) {
                                if (weather.alerts.isNullOrEmpty()) {
                                    setOneTimeWorkRequest(
                                        userAlerts,
                                        getString(R.string.NoAlerts),
                                        weather.current.weather[0].icon
                                    )
                                } else {
                                    setOneTimeWorkRequest(
                                        userAlerts,
                                        weather.alerts!![0].tags[0],
                                        weather.current.weather[0].icon
                                    )
                                }
                            }
                        }
                        else -> {}
                    }
                }
            }
        }
    }

    /*fun showDateTimePicker(label: String) {
        val currentDate = Calendar.getInstance()
        var date: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { view, year, monthOfYear, dayOfMonth ->
                date.set(year, monthOfYear, dayOfMonth)
                TimePickerDialog(
                    context,
                    { view, hourOfDay, minute ->
                        date.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        date.set(Calendar.MINUTE, minute)
                        if (label == AlertConst.START_EDIT_TEXT) {
                            startDate = date
                            startLongDate = updateLabel(startDate, alertActBinding.startDateId)
                        } else {
                            endDate = date
                            endLongDate = updateLabel(endDate, alertActBinding.endDateid)
                        }
                        Log.v("TAG", "The choosen one " + date.getTime())
                    }, currentDate[Calendar.HOUR_OF_DAY], currentDate[Calendar.MINUTE], false
                ).show()
            }, currentDate[Calendar.YEAR], currentDate[Calendar.MONTH], currentDate[Calendar.DATE]
        )
        datePickerDialog.datePicker.minDate = currentDate.timeInMillis;
        datePickerDialog.show();
    }*/


    private fun setAlarmDateAndTime(callback: (Pair<Long, Long>) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    TimePickerDialog(
                        requireContext(),
                        0,
                        { _, hour, minute ->
                            this.set(Calendar.HOUR_OF_DAY, hour)
                            this.set(Calendar.MINUTE, minute)
                            callback(this.timeInMillis to this.timeInMillis.dateToLong())
                        },
                        this.get(Calendar.HOUR_OF_DAY),
                        this.get(Calendar.MINUTE),
                        false
                    ).show()
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }
    }

    // Extension function to convert a date to a long value
    fun Long.dateToLong(): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return calendar.apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    private fun updateLabel(calendar: Calendar, editText: EditText): Long {
        val myFormat = "HH:mm a\ndd/MM/yyyy"
        val dateFormat =
            SimpleDateFormat(myFormat, Locale(sharedPreference.getString(Language, "en")))
        val milliseconds: Long = calendar.timeInMillis
        editText.setText(dateFormat.format(calendar.time))
        return milliseconds
    }
    fun getDateToAlert(timestamp: Long, language: String): String{
        return SimpleDateFormat("M/d/yyyy",Locale(language)).format(timestamp)
    }

    fun getTimeToAlert(timestamp: Long, language: String): String{
        return SimpleDateFormat("h:mm a",Locale(language)).format(timestamp)
    }
    private fun setAlarmEndDate(callback: (Long) -> Unit) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                0,
                { _, year, month, day ->
                    this.set(Calendar.YEAR, year)
                    this.set(Calendar.MONTH, month)
                    this.set(Calendar.DAY_OF_MONTH, day)
                    callback(this.timeInMillis)
                },
                this.get(Calendar.YEAR),
                this.get(Calendar.MONTH),
                this.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = Calendar.getInstance().timeInMillis
            datePickerDialog.show()
        }
    }
    override fun deleteOnClick(userAlert: UserAlerts) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setCancelable(true)
        builder.setTitle(getString(R.string.delete_alert_title))
        builder.setMessage(getString(R.string.are_you_sure))
        builder.setPositiveButton(getString(R.string.confirm_yes)) { _, _ ->
            viewModel.deleteAlert(userAlert)
        }
        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }

        //  val dialog: AlertDialog = builder.create()
        //  dialog.setCanceledOnTouchOutside(false)
        builder.show()

    }
}
