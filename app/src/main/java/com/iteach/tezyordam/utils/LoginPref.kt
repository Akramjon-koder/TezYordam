package com.iteach.tezyordam.utils

import com.iteach.taxi.fragment.login.base.LoginModel
import io.paperdb.Paper

object LoginPref {
    private const val  login ="fdjnbds"
     fun SaveLogin(model: LoginModel){
         Paper.book().write(login,model)
     }
    fun ReadLogin() : LoginModel{
        return Paper.book().read(login, LoginModel())
    }

}