package com.github.exact7.xtra

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import androidx.lifecycle.ProcessLifecycleOwner
import com.github.exact7.xtra.di.AppInjector
import com.github.exact7.xtra.util.AppLifecycleObserver
import com.github.exact7.xtra.util.LifecycleListener
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasBroadcastReceiverInjector
import dagger.android.HasServiceInjector
import javax.inject.Inject

class XtraApp : MultiDexApplication(), HasActivityInjector, HasServiceInjector, HasBroadcastReceiverInjector {

    @Inject lateinit var dispatchingActivityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var dispatchingServiceInjector: DispatchingAndroidInjector<Service>
    @Inject lateinit var dispatchingBroadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>
    private val appLifecycleObserver = AppLifecycleObserver()

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
    }

    override fun activityInjector(): AndroidInjector<Activity> = dispatchingActivityInjector
    override fun serviceInjector(): AndroidInjector<Service> = dispatchingServiceInjector
    override fun broadcastReceiverInjector(): AndroidInjector<BroadcastReceiver> = dispatchingBroadcastReceiverInjector

    fun setLifecycleListener(listener: LifecycleListener?) {
        appLifecycleObserver.setLifecycleListener(listener)
    }
}
