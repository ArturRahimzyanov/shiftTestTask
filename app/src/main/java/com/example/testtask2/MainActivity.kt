package com.example.testtask2

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.testtask2.model.MainViewModule
import com.example.testtask2.databinding.ActivityMainBinding
import com.example.testtask2.model.BinDataaaaaa
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {

    //45717360

   private lateinit var binding: ActivityMainBinding
   private lateinit var viewModule: MainViewModule
   private var binList = BinDataaaaaa()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModule = ViewModelProvider(this)[MainViewModule::class.java]
        viewModule.getBinList().observe(this) {
            insertBinlist(it)
        }

        binding.btGetData.setOnClickListener {
            binList = viewModule.sendRequest(binding.inputEdit.text.toString()).value!!
            runOnUiThread {
                insertBinlist(binList)
            }
        }
        initButtonsToOthersApp()
    }

    private fun initButtonsToOthersApp() {              //работет автомат запрас открывет
       binding.bankUrl.setOnClickListener {
           if(binding.bankUrl.text != "нет данных"){
               var urlBank = binding.bankUrl.text.toString()
               if (!urlBank.startsWith("https://") && !urlBank.startsWith("http://")){
                   urlBank = "http://$urlBank";
               }
               startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlBank)))
           }
       }

        binding.bankPhone.setOnClickListener {          //работает автомат звонилку открывает
            if(binding.bankPhone.text != "нет данных"){
                val intentLaunch2 = Intent(Intent.ACTION_DIAL)
                intentLaunch2.data = Uri.parse("tel:${binding.bankPhone.text}")
                startActivity(intentLaunch2)
            }
        }

        binding.latitudeTextView.setOnClickListener {
            if(binding.latitudeTextView.text != "нет данных"){
                toMaps()
            }
        }

        binding.longtitudeTextView.setOnClickListener {
            if(binding.longtitudeTextView.text != "нет данных") {
                toMaps()
            }
        }
    }

    private fun toMaps() {   //работает автомат звонилку открывает
        val geoUriString = "geo:${binding.latitudeTextView.text},${binding.longtitudeTextView.text}?z=15"
        val geoUri: Uri = Uri.parse(geoUriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        }else{
            Toast.makeText(this, "нет возможности открыть", Toast.LENGTH_SHORT).show()
        }
    }

    private fun insertBinlist(binList: BinDataaaaaa) {
        with(binding){
                binList.number.length?.let { binding.lengthTextView.text = binList.number.length.toString() } ?:
                run { binding.lengthTextView.text = "нет данных" }
                binList.number.luhn?.let { binding.lumTextView.text = binList.number.luhn.toString() } ?:
                 run { binding.lumTextView.text = "нет данных" }
                scemeTextView.text = binList.scheme ?: "нет данных"
                typeTextView.text = binList.type ?: "нет данных"
                brandTextView.text = binList.brand ?: "нет данных"
                 binList.prepaid?.let { binding.prepaidTextView.text = binList.prepaid.toString() } ?:
                 run { binding.prepaidTextView.text = "нет данных" }
                numericTextView.text = binList.country.numeric ?: "нет данных"
                alpha2TextView.text = binList.country.alpha2 ?: "нет данных"
                nameTextView.text = binList.country.name ?: "нет данных"
                emojiTextView.text = binList.country.emoji ?: "нет данных"
                currencyTextView.text = binList.country.currency ?: "нет данных"
                 binList.country.latitude?.let { binding.latitudeTextView.text = binList.country.latitude.toString() } ?:
                 run { binding.latitudeTextView.text = "нет данных" }
                  binList.country.longitude?.let { binding.longtitudeTextView.text = binList.country.longitude.toString() } ?:
                 run { binding.longtitudeTextView.text = "нет данных" }
                bankName.text = binList.bank.name ?: "нет данных"
                bankUrl.text = binList.bank.url ?: "нет данных"
                bankPhone.text = binList.bank.phone ?: "нет данных"
                bankCity.text = binList.bank.city ?: "нет данных"
        }
    }

    companion object{
        const val TAG = "logs"
    }
}