package com.example.testtask2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.testtask2.model.MainViewModule
import com.example.testtask2.databinding.ActivityMainBinding
import com.example.testtask2.model.BinDataaaaaa
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

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