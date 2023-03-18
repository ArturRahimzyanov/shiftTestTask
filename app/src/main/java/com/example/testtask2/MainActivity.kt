package com.example.testtask2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Surface
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.vector.DefaultRotation
import androidx.lifecycle.ViewModelProvider
import com.example.testtask2.databinding.ActivityMainBinding
import com.example.testtask2.model.BinDataaaaaa
import com.example.testtask2.model.MainViewModule


class MainActivity : AppCompatActivity() {

    //45717360

   private lateinit var binding: ActivityMainBinding
   private lateinit var viewModule: MainViewModule
   private var bin = BinDataaaaaa()
   private val binList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModule = ViewModelProvider(this)[MainViewModule::class.java]
        viewModule.getBin().observe(this) {
            insertBinlist(it)
        }

        binding.inputEdit.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, binList))
        binding.inputEdit.threshold = 0
        binding.inputEdit.onFocusChangeListener =
         OnFocusChangeListener { v, hasFocus ->
             if(v.windowVisibility != View.INVISIBLE){
                 return@OnFocusChangeListener
             }
             if (hasFocus) binding.inputEdit.showDropDown()
             else binding.inputEdit.dismissDropDown()
         }

        binding.btGetData.setOnClickListener {
            val requestBin = binding.inputEdit.text.toString()
            if(isNumeric(requestBin)){
                bin = viewModule.sendRequest(requestBin).value!!
                if( requestBin !in binList ) {  binList.add(requestBin) }
                runOnUiThread {
                    insertBinlist(bin)
                }
                binding.inputEdit.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, binList))
            }else{
                Toast.makeText(this, "неверный формат данных", Toast.LENGTH_SHORT).show()
            }

        }
        initButtonsToOthersApp()
    }



    fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
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

    private fun toMaps() {   //работает автомат карту открывает
        val geoUriString = "geo:${binding.latitudeTextView.text},${binding.longtitudeTextView.text}?z=15"
        val geoUri: Uri = Uri.parse(geoUriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
        if (mapIntent.resolveActivity(packageManager) != null) {
            startActivity(mapIntent)
        } else{
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