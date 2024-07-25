package com.smatiukaite.bhcc_ims

import android.widget.Toast

class User {
    var id: Int = 0
    var firstName: String = ""
    var lastName: String = ""
    var email: String = ""
    var finalPassword: String = ""

    constructor(firstName:String,
                lastName: String,
                email: String,
                password: String,
                repeatPassword: String){
        this.firstName = firstName
        this.lastName = lastName
        this.email = email

        if(password == repeatPassword){
            this.finalPassword = password
        }
        else{
            throw IllegalArgumentException("Passwords do not match.")
        }
    }

    fun retrieveFinalPassword(): String {
        return finalPassword
    }

}
