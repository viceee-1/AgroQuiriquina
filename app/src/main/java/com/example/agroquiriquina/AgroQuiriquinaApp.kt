package com.example.agroquiriquina

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class AgroQuiriquinaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}