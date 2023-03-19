package com.example.testtask2

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.testtask2.databinding.ActivityMainBinding
import com.example.testtask2.model.BinDataaaaaa
import com.example.testtask2.model.MainViewModule

class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding
   private lateinit var viewModule: MainViewModule
   private var bin = BinDataaaaaa()
   private var binList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSavedListFromSP()
        setupViewModel()
        setupAutoTextComplete()
        binding.btGetData.setOnClickListener { sendRequest() }
        initButtonsToOthersApp()
    }

    private fun getSavedListFromSP() {
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val mySet = sharedPreferences.getStringSet("list", emptySet())
        val myArrayList = arrayListOf<String>()
        myArrayList.addAll(mySet!!)
        binList = myArrayList
    }

    private fun saveList(arrayList: ArrayList<String>) {
        val mySet = arrayList.toSet()
        val sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putStringSet("list", mySet)
        editor.apply()
    }


    private fun sendRequest() {
        val requestBinInput = binding.inputEdit.text.toString()
        if(isNumeric(requestBinInput) && isOnline(this) ){        //проверка строки на валидность и на онлайн
            bin = viewModule.sendRequest(requestBinInput).value!!
            if( requestBinInput !in binList ) {  binList.add(requestBinInput) }
            runOnUiThread {
                insertBinlist(bin)
            }
            binding.inputEdit.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, binList))
        }else {
            Toast.makeText(this, "неверные данные или вы не подключены к интернету ", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if ((activeNetworkInfo != null) && activeNetworkInfo.isConnected) {
                return true
            }
        }
        return false
    }

    private fun setupViewModel() {
        viewModule = ViewModelProvider(this)[MainViewModule::class.java]
        viewModule.getBin().observe(this) { insertBinlist(it) }
    } //устанавливаем модель и наблюдателей

    private fun setupAutoTextComplete() {
        binding.inputEdit.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, binList))
        binding.inputEdit.threshold = 0
        binding.inputEdit.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus ->
                if(v.windowVisibility != View.INVISIBLE){  //чтобы при повороте не выходило исключение
                    return@OnFocusChangeListener
                }
                if (hasFocus) binding.inputEdit.showDropDown()
                else binding.inputEdit.dismissDropDown()
            }
    }

    private fun isNumeric(toCheck: String): Boolean {
        val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
        return toCheck.matches(regex)
    } //true - если все числа [0; 9]

    private fun initButtonsToOthersApp() {              //работет автомат запрос открывет в браузере
        binding.bankUrl.setOnClickListener {
           if(binding.bankUrl.text != "нет данных"){
               var urlBank = binding.bankUrl.text.toString()
               if (!urlBank.startsWith("https://") && !urlBank.startsWith("http://")){
                   urlBank = "http://$urlBank"
               }
               startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlBank)))
           }else{
               Toast.makeText(this, "нет данных для открытия", Toast.LENGTH_SHORT).show()
           }
       }

        binding.bankPhone.setOnClickListener {          //работает автомат звонилку открывает
            if(binding.bankPhone.text != "нет данных"){
                val intentLaunch2 = Intent(Intent.ACTION_DIAL)
                intentLaunch2.data = Uri.parse("tel:${binding.bankPhone.text}")
                startActivity(intentLaunch2)
            }else{
                Toast.makeText(this, "нет данных для открытия", Toast.LENGTH_SHORT).show()
            }
        }

        binding.latitudeTextView.setOnClickListener {
            if(binding.latitudeTextView.text != "нет данных"){
                toMaps()
            }else{
                Toast.makeText(this, "нет данных для открытия", Toast.LENGTH_SHORT).show()
            }
        }

        binding.longtitudeTextView.setOnClickListener {
            if(binding.longtitudeTextView.text != "нет данных") {
                toMaps()
            }else{
                Toast.makeText(this, "нет данных для открытия", Toast.LENGTH_SHORT).show()
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
                bankName.text = binList.bank?.name ?: "нет данных"
                bankUrl.text = binList.bank?.url ?: "нет данных"    //не убирать вопросы!!!!
                bankPhone.text = binList.bank?.phone ?: "нет данных"
                bankCity.text = binList.bank?.city ?: "нет данных"
        }
    }

    override fun onStop() {
        saveList(binList as ArrayList<String>) //в onDestroy не успевает сохранять, поэтому здесь
        super.onStop()
    }
}