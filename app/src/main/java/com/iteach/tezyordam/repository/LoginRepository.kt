package com.iteach.taxi.fragment.login.repository

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.iteach.taxi.api.NetworkManeger
import com.iteach.taxi.fragment.login.base.LoginModel
import com.iteach.taxi.fragment.login.base.Login_Password
import com.iteach.tezyordam.base.BaseResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class LoginRepository {
    val compositeDisposible = CompositeDisposable()
    fun sendLogin(
        error: MutableLiveData<String>,
        success: MutableLiveData<Boolean>,
        timeBegin:String,
        timeEnd:String,
        requireActivity: Context
    ){
        compositeDisposible.add(
            NetworkManeger.getApiService(requireActivity).getupdate(1,1,timeBegin,timeEnd).
            subscribeOn(Schedulers.io()).
            observeOn(AndroidSchedulers.mainThread()).
            subscribeWith(object : DisposableObserver<BaseResponse>(){
                override fun onNext(t: BaseResponse) {
                    if (t.status==200){
                        success.value =true
                    }
                }

                override fun onError(e: Throwable) {
                    error.value = e?.localizedMessage
                }
                override fun onComplete() {
                }

            })
        )
    }
}