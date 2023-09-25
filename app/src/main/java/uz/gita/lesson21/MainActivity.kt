package uz.gita.lesson21

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import uz.gita.lesson21.databinding.ActivityMainBinding
import uz.gita.lesson21.fragments.InfoScreen
import uz.gita.lesson21.fragments.MainScreen
import uz.gita.lesson21.fragments.SavedScreen

class MainActivity : AppCompatActivity() {
    private lateinit var bindidng: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindidng = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindidng.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_holder, MainScreen())
            .commit()


        bindidng.bottomNavMenu.setOnItemSelectedListener {

            when (it.itemId) {
                R.id.btn_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, MainScreen())
                        .commitNow()
                }

                R.id.btn_saved ->  {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, SavedScreen())
                        .commitNow()
                }

                R.id.btn_info -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_holder, InfoScreen())
                        .commitNow()
                }
            }


            return@setOnItemSelectedListener true
        }
    }


}