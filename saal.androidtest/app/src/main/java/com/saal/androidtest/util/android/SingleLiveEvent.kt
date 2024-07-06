package com.saal.androidtest.util.android

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private var pending = false

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner) { t ->
            if (pending) {
                pending = false
                observer.onChanged(t)
            }
        }
    }

    override fun setValue(value: T) {
        pending = true
        super.setValue(value)
    }

    /**
     * Use this method to post a task for a single event.
     */
    fun postTask(task: T) {
        super.postValue(task)
    }
}