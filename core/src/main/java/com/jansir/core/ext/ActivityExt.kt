package com.jansir.core.ext

import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MotionEvent

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.viewbinding.ViewBinding
import com.blankj.utilcode.util.SPUtils
import com.bumptech.glide.Glide
import com.jansir.core.base.XStarter
import com.jansir.core.base.activity.BaseActivity
import com.jansir.core.base.fragment.BaseFragment
import com.jansir.core.base.livedata.LifecycleHandler
import com.lxj.xpopup.interfaces.XPopupImageLoader
import java.io.File
import java.util.*

/**
 * Description: Activity相关
 * Create by lxj, at 2018/12/7
 */
inline fun <reified VB : ViewBinding> BaseActivity<*>.inflateLazyVB(): Lazy<VB> = lazy {
    inflateBinding<VB>(layoutInflater)
}


inline fun <reified T> Fragment.startActivity(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null) {
    activity?.startActivity<T>(flag, bundle)
}

inline fun <reified T> Fragment.startActivityForResult(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null, requestCode: Int = -1) {
    activity?.startActivityForResult<T>(flag, bundle, requestCode)
}

inline fun <reified T> Context.startActivity(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null, requestCode: Int = -1) {
    val intent = Intent(this, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        } else if (this !is Activity) {
            this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        if (bundle != null) bundle.toBundle()?.let { putExtras(it) }
    }
    startActivity(intent)
}

inline fun <reified T> View.startActivity(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null) {
    context.startActivity<T>(flag, bundle)
}

inline fun <reified T> View.startActivityForResult(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null, requestCode: Int = -1) {
    (context as Activity).startActivityForResult<T>(flag, bundle, requestCode)
}

inline fun <reified T> Activity.startActivityForResult(flag: Int = -1, bundle: Array<out Pair<String, Any?>>? = null, requestCode: Int = -1) {
    val intent = Intent(this, T::class.java).apply {
        if (flag != -1) {
            this.addFlags(flag)
        }
        if (bundle != null) bundle.toBundle()?.let { putExtras(it) }
    }
    startActivityForResult(intent, requestCode)
}

fun FragmentActivity.finishDelay(delay: Long = 1) {
    LifecycleHandler(this).postDelayed({ finish() }, delay)
}

//post, postDelay
fun FragmentActivity.post(action: ()->Unit){
    LifecycleHandler(this).post { action() }
}

fun FragmentActivity.postDelay(delay:Long = 0, action: ()->Unit){
    LifecycleHandler(this).postDelayed({ action() }, delay)
}

//view model
fun <T: ViewModel> FragmentActivity.getVM(clazz: Class<T>) = ViewModelProviders.of(this).get(clazz)

//******************* 隐藏底部导航栏 *******************//
fun Activity.hideBottomNav() {
    val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or View.SYSTEM_UI_FLAG_FULLSCREEN)
    window.decorView.systemUiVisibility = uiOptions
}


//根据传入控件的坐标和用户的焦点坐标，判断是否隐藏键盘，如果点击的位置在控件内，则不隐藏键盘
fun Activity.hideKeyboard(event: MotionEvent, view: View?) {
    if (view == null) return
    try {
        if (view is EditText) {
            val location = intArrayOf(0, 0)
            view.getLocationInWindow(location)
            val left = location[0]
            val top = location[1]
            val right = left + view.getWidth()
            val bootom = top + view.getHeight()
            // 判断焦点位置坐标是否在空间内，如果位置在控件外，则隐藏键盘
            if (event.rawX < left || event.rawX > right
                || event.y < top || event.rawY > bootom
            ) {
                // 隐藏键盘
                val token = view.getWindowToken()
                val inputMethodManager = this
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(
                    token,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

}


fun  Activity.getExtraString(key: String): String? {
    return intent.extras?.getString(key)
}

inline fun <reified  T : Parcelable> Activity.getParcelable(key: String): T? {
    return intent.extras?.getParcelable<T>(key)
}

fun  Activity.getExtraStringList(key: String): ArrayList<String>?{
    return intent.extras?.getStringArrayList(key)
}

fun  Activity.getExtraInt(key: String, default: Int = 0): Int {
    return intent.extras?.getInt(key)?:default
}
//去应用市场更新
fun BaseActivity<*>.openMarket(id : String) {
    try {
        val uri = Uri.parse("market://details?id=" + id)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    } catch (e: Exception) {
    }
}

inline fun <reified A : Activity> BaseActivity<*>.open(vararg params: Pair<String, Any>) {
    XStarter.startActivity<A>(this, *params)
}

inline fun <reified A : Activity> BaseFragment<*>.open(vararg params: Pair<String, Any>) {
    XStarter.startActivity<A>(this, *params)
}

inline fun <reified A : Activity> Context.open(vararg params: Pair<String, Any>) {
    XStarter.startActivity<A>(this, *params)
}

inline fun <reified A : Activity> BaseActivity<*>.openForResult(vararg params: Pair<String, Any>, crossinline okCall: (intent: Intent) -> Unit) {
    XStarter.startActivityForResult<A>(this, *params) {
        if (it == null) {
            //未成功处理，即（ResultCode != RESULT_OK）
        } else {
            //处理成功，这里可以操作返回的intent
            okCall(it)
        }
    }
}



class ImageLoader : XPopupImageLoader {
    override fun loadImage(position: Int, url: Any, imageView: ImageView) {
        //必须指定Target.SIZE_ORIGINAL，否则无法拿到原图，就无法享用天衣无缝的动画
        Glide.with(imageView)
            .load(url)
            .into(imageView)
    }

    override fun getImageFile(context: Context, uri: Any): File? {
        try {
            return Glide.with(context).downloadOnly().load(uri).submit().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}


fun Context.imageUri(resId: Int): String {
    val r: Resources =
        getResources()
    val uri = Uri.parse(
        ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + r.getResourcePackageName(resId) + "/"
                + r.getResourceTypeName(resId) + "/"
                + r.getResourceEntryName(resId)
    )

    return uri.toString()
}