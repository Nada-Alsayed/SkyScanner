package eg.gov.iti.skyscanner

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import eg.gov.iti.skyscanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        if (actionBar != null) {
            Toast.makeText(this,"jjjj",Toast.LENGTH_SHORT).show()
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.navView, navController)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId ==android.R.id.home){
            if(binding.navDrawerLayout.isDrawerOpen(GravityCompat.START)){
                binding.navDrawerLayout.closeDrawer(GravityCompat.START)
            }   else{
                binding.navDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
            return super.onOptionsItemSelected(item)
    }
}