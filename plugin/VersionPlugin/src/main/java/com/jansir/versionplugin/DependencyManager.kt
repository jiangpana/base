package com.jansir.versionplugin

/**
 * 包名:com.jansir.versionplugin
 */

object Versions {

    //androidx
    val appcompat = "1.2.0"
    val core_ktx = "1.3.2"
    val constraintlayout = "2.0.4"
    val cardview = "1.0.0"
    val recyclerview = "1.1.0"
    val paging = "3.0.0-alpha01"
    val work = "2.2.0"
    val room = "2.3.0-alpha01"
    val startup = "1.0.0"
    val swiperefreshlayout = "1.1.0"
    val fragment = "1.2.1"


    //test
    val junit = "4.12"
    val junitExt = "1.1.1"
    val espressoCore = "3.2.0"


    //kotlin
    val kotlin_version ="1.3.72"

}

object AndroidX {
    val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    val core_ktx = "androidx.core:core-ktx:${Versions.core_ktx}"
    val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    val pagingRuntime = "androidx.paging:paging-runtime:${Versions.paging}"
    val workRuntime = "androidx.work:work-runtime:${Versions.work}"
    val workTesting = "androidx.work:work-testing:${Versions.work}"
    val cardview = "androidx.cardview:cardview:${Versions.cardview}"
    val startup = "androidx.startup:startup-runtime:1.0.0"
    val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recyclerview}"
    val fragment_runtime = "androidx.fragment:fragment:${Versions.fragment}"
    val fragment_runtimeKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    val fragment_testing = "androidx.fragment:fragment-testing:${Versions.fragment}"
    val room_runtime = "androidx.room:room-runtime:${Versions.room}"
    val room_compiler = "androidx.room:room-compiler:${Versions.room}"
    val room_ktx = "androidx.room:room-ktx:${Versions.room}"
    val room_rxjava2 = "androidx.room:room-rxjava2:${Versions.room}"
    val room_testing = "androidx.room:room-testing:${Versions.room}"
    val lifecycle_livedata_ktx = "androidx.lifecycle:lifecycle-livedata-ktx:2.2.0-alpha02"
    val lifecycle_viewmodel_ktx = "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0-alpha03"
    val lifecycle_extensions = "androidx.lifecycle:lifecycle-extensions:2.2.0-alpha03"
}


object Kotlin {
    val coroutines_android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.1"
    val coroutines_core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.1"
    val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin_version}"
}

object Test {
    val junit = "junit:junit:${Versions.junit}"
    val androidTestJunit = "androidx.test.ext:junit:${Versions.junitExt}"
    val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"


}

object Data {
    val mmkv = "com.tencent:mmkv:1.2.4"
}


object UI {
    val XUI = "com.github.xuexiangjys:XUI:1.1.5"
    val xpopup = "com.lxj:xpopup:2.1.17"
    val PickerView = "com.contrarywind:Android-PickerView:3.2.5"
    val PictureSelector = "com.github.LuckSiege.PictureSelector:picture_library:v2.2.3"
    val lottie = "com.airbnb.android:lottie:2.5.4"
    val BaseRecyclerViewAdapterHelper = "com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4"
    val easyadapter = "com.lxj:easyadapter:1.2.2"
    val jDatabinding = "com.hi-dhl:jdatabinding:1.0.1"

    val me_fragmentationx = "me.yokeyword:fragmentationx:1.0.1"
    val me_fragmentationx_swipeback = "me.yokeyword:fragmentationx-swipeback:1.0.1"
    val me_eventbus_activity_scope = "me.yokeyword:eventbus-activity-scope:1.1.0"
    val viewbinding_ktx = "com.dylanc:viewbinding-ktx:1.0.0"

}

object Glide {
    val glide = "com.github.bumptech.glide:glide:4.6.1"
}

object Bus {
    val live_event_bus_x = "com.jeremyliao:live-event-bus-x:1.5.7"
}

object Http {
    val gson = "com.google.code.gson:gson:2.8.5"
    val okhttp = "com.squareup.okhttp3:okhttp:3.12.1"
    val progressmanager = "me.jessyan:progressmanager:1.5.0"
    val retrofit_runtime = "com.squareup.retrofit2:retrofit:2.9.0"
    val retrofit_gson = "com.squareup.retrofit2:converter-gson:2.9.0"
    val retrofit_mock = "com.squareup.retrofit2:retrofit-mock:2.9.0"
}

object Tool {
    val timber = "com.jakewharton.timber:timber:4.7.1"
    val logger = "com.orhanobut:logger:2.2.0"
    val Cockroach = "com.github.SJJ-dot:Cockroach:2.0.1"
    val utilcode = "com.blankj:utilcode:1.25.5"
    val xxpermissions = "com.hjq:xxpermissions:9.6"
}