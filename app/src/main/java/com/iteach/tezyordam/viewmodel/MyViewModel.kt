package com.iteach.taxi.viewmodel
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iteach.taxi.fragment.login.base.LoginModel
import com.iteach.taxi.fragment.login.base.Login_Password
import com.iteach.taxi.fragment.login.repository.LoginRepository

class MyViewModel :ViewModel() {
    val login_repository = LoginRepository()


    //val cancelOrderRepo = CancelledOrder()
    val cancelledOrder =MutableLiveData<Boolean>()
    val success =MutableLiveData<Boolean>()

    val user = MutableLiveData<LoginModel>()
    val error = MutableLiveData<String>()

    fun sendLogin(timeBegin:String, timeEnd:String,context: Context){
        login_repository.sendLogin(error,success,timeBegin,timeEnd,context)
    }

    fun cancelOrderFun(orderId: Int, token:String,status: Int,context: Context){
      //  cancelOrderRepo.cancelOrder(error,cancelledOrder,orderId, token, status,context)
    }

//
//    fun orderBegining(token:String,definitionModel: DefinitionModel){
//        sendOrderBegin.sendOrderStart(error,orderBegin,definitionModel,token)
//    }


}