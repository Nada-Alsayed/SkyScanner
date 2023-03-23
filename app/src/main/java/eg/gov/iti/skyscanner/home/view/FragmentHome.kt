package eg.gov.iti.skyscanner.home.view

import android.R
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment



class FragmentHome : Fragment() {
    override fun onResume() {
        super.onResume()
        val activity: Activity? = activity
        if (activity != null) {
            activity.title = getString(eg.gov.iti.skyscanner.R.string.home)

        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(eg.gov.iti.skyscanner.R.layout.fragment_home, container, false)
    }
}