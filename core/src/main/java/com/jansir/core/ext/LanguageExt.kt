package com.jansir.core.ext

import android.content.Context
import android.content.res.Resources
import android.os.Build
import com.blankj.utilcode.util.SPUtils
import java.util.*

/**
 * 包名:com.jansir.core.ext
 */

/*
* 使用代码:
*  val  mLocaleLanguage = resources.getLocaleLanguage()
*  var mData: List<SelectLanguageEntity>
*
*   mData = resources.getStringArray(R.array.language).mapIndexed { index, language ->
            val array = language.split(",")
            val entity: SelectLanguageEntity
            if (mLocaleLanguage == array[1]) {
                entity = SelectLanguageEntity(array[0])
                entity.isSelect = true
            } else {
                entity = SelectLanguageEntity(array[0])
            }
            entity
        }
*
* 点击事件
* click {
*   mLocaleLanguage =
                       resources.getStringArray(R.array.language)[position].split(",")[1]
                    changeLanguage()
                    EventBus.getDefault().post(LanguageEvent())
                    finish()
   }
 private fun changeLanguage() {
        if (mLocaleLanguage == "zh") {
            saveLanguage(Locale.SIMPLIFIED_CHINESE)
        } else if (mLocaleLanguage == "tw") {
            saveLanguage(Locale.TRADITIONAL_CHINESE)
        }else{
            saveLanguage(Locale(mLocaleLanguage))
        }
*
*/

fun Context.saveLanguage(locale: Locale) {
    val localLanguage = locale.language
    val localCountry = locale.country
    if(localCountry.isNotEmpty()){
        SPUtils.getInstance("Utils").put("KEY_LOCALE",  localLanguage + "$" + localCountry)
    }else{
        SPUtils.getInstance("Utils").put("KEY_LOCALE",  localLanguage + "$" + "   ")
    }
    val resources = resources
    val config = resources.configuration
    val dm = resources.displayMetrics
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        config.setLocale(locale)
        createConfigurationContext(config)
    } else {
        config.locale = locale
    }
    resources.updateConfiguration(config, dm)
}

fun Resources.getLocaleLanguage(): String {
    var language = ""
    when (configuration.locale.language) {
        "zh" -> {
            language = if (configuration.locale.country ==Locale.SIMPLIFIED_CHINESE.country) {
                //简体
                "zh"
            } else {
                //繁体
                "tw"
            }
        }
        else-> language =configuration.locale.language
    }
    return language
}

data class SelectLanguageEntity (val language:String){
    var isSelect=false
}

