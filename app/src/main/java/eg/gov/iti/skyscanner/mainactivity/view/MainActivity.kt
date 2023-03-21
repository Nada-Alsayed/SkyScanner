package eg.gov.iti.skyscanner.mainactivity.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import eg.gov.iti.skyscanner.R
import eg.gov.iti.skyscanner.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toolbar: Toolbar
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolbar = findViewById(R.id.toolBar)
        setSupportActionBar(toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            binding.navDrawerLayout,
            toolbar,
            R.string.home,
            R.string.settings
        )
        binding.navDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        /* val actionBar = supportActionBar
         if (actionBar != null) {
             Toast.makeText(this,"jjjj",Toast.LENGTH_SHORT).show()
             actionBar.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
             actionBar.setDisplayShowHomeEnabled(true)
             actionBar.setDisplayHomeAsUpEnabled(true)
         }
 */
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(binding.navView, navController)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (binding.navDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.navDrawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.navDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}