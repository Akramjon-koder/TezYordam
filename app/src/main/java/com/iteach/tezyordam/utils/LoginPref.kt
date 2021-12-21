package com.iteach.tezyordam.utils

import io.paperdb.Paper

object LoginPref {
    private const val  login ="fdjnbds"
     fun SaveLogin(model: Int){
         Paper.book().write(login,model)
     }
    fun ReadLogin() : Int{
        return Paper.book().read(login, 0)
    }

}