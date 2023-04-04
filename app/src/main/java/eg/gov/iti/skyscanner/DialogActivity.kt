package eg.gov.iti.skyscanner

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import eg.gov.iti.skyscanner.databinding.ActivityDialogBinding
import eg.gov.iti.skyscanner.models.MyIcons.DESCRIPTION
import eg.gov.iti.skyscanner.models.MyIcons.ICON
import eg.gov.iti.skyscanner.models.MyIcons.aPIIconInt


class DialogActivity : AppCompatActivity() {
    private lateinit var description: String
    private lateinit var icon: String
    var mMediaPlayer: MediaPlayer? = null
    lateinit var binding: ActivityDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        description = intent.getStringExtra(DESCRIPTION).toString()
        Log.e("TAG", "doWork: *************55" + description)
        icon = intent.getStringExtra(ICON).toString()
        Log.e("TAG", "doWork: *************55" + icon)
        //                              width                               height
         window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setFinishOnTouchOutside(false)
        setUiAndSound()
    }

    private fun startSound() {
        if (mMediaPlayer == null) { //mMediaPkayer is your variable
            mMediaPlayer = MediaPlayer.create(
                this,
                R.raw.sound
            ) //raw is the folder where you have the audio files or sounds, water is the audio file (is a example right)
            mMediaPlayer!!.isLooping = true //to repeat again n again
            mMediaPlayer!!.start() //to start the sound
        }
    }

    private fun setUiAndSound() {
        startSound()
        binding.image.setImageResource(aPIIconInt(icon))
        binding.textDescription.text = description
        binding.btnDismiss.setOnClickListener {
            mMediaPlayer!!.stop()
            this.finish()
        }
    }
}