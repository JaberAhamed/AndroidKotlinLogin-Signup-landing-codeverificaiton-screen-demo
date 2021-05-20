package com.bidyava.solutions

import android.app.Application
import android.preference.PreferenceManager
import android.util.Log
import androidx.databinding.library.BuildConfig
import com.bidyava.solutions.di.AppModule
import com.bidyava.solutions.di.ApplicationGrap
import com.bidyava.solutions.di.DaggerApplicationGrap
import com.bidyava.solutions.ui.BaseActivity
import com.bidyava.solutions.utils.AppSignatureHelper
import com.bidyava.solutions.utils.RememberMeSharedPreference
import java.util.*
import javax.inject.Inject

class MyApplication : Application() {


    val appGraph: ApplicationGrap by lazy {
        DaggerApplicationGrap
            .builder()
            .appModule(AppModule(this))
            .build()
    }


    override fun onCreate() {
        super.onCreate()


        if (BuildConfig.DEBUG) {

            // Note: It just needed for automated sms retrieve api.
            val appSignatureHelper = AppSignatureHelper(this)
            Log.d("CODEE", "App Signatures: " + appSignatureHelper.appSignatures)
        }

         var rememberMeSharedPreference=RememberMeSharedPreference(this)

        var change = ""
        /*val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val language = sharedPreferences.getString("language", "bak")*/
        val language=rememberMeSharedPreference.getLanguage()
        if (language == "Bangla") {
            change="bn"
        } else if (language == "English" ) {
            change = "en"
        }else {
            change ="en"
        }
        Log.d("LANGUAGE",change+"  "+language)

        BaseActivity.dLocale = Locale(change)
    }
}
