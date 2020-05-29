package com.lloydsbyte.todoos.utilz

import android.content.Context
import android.content.SharedPreferences

class SharedPref_Controller(val context: Context) {

    private val PRIVATE_MODE = 0
    private val GLOBAL_KEY = "com.lloydsbyte.todoos.stored_prefs"
    //Individual Keys
    private val SERVER_ADDRESS = "server_address.todoos"
    private val DEVICE_ID = "device_id.todoos"
    private val LAST_DOWNLOADED = "last_downloaded.todoos"



    val sharedPref: SharedPreferences = context.getSharedPreferences(GLOBAL_KEY, PRIVATE_MODE)

    fun wipeAllData(){
        sharedPref.edit().clear().apply()
    }

    //Save ip and port
    fun saveServerAddress(address: String) {
        writeString(SERVER_ADDRESS, address)
    }

    fun getServerAddress():String {
        return readString(SERVER_ADDRESS)
    }

    fun rememberDevice(deviceId: String){
        writeString(DEVICE_ID, deviceId)
    }
    fun getDeviceId(): String {
        return readString(DEVICE_ID)
    }


    //Used for HomeFragment to check when was the last time it synced (updates if an hour outdated)
    fun checkIfConfigured(): Boolean {
        return readBool(LAST_DOWNLOADED)
    }
    fun updateConfigStatus(value: Boolean){
        writeBool(LAST_DOWNLOADED, value)
    }

    /** PRIVATE METHODS USED BY THE ABOVE FUNCTIONS **/
    private fun readString(key: String): String {
        return sharedPref.getString(key, null)?:""
    }

    private fun writeString(key: String, value: String){
        val editor = sharedPref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    private fun readBool(key: String): Boolean {
        return sharedPref.getBoolean(key, false)
    }

    private fun writeBool(key: String, value: Boolean){
        val editor = sharedPref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun readInt(key: String): Int {
        return sharedPref.getInt(key, 0)
    }

    private fun writeInt(key: String, value: Int) {
        val editor = sharedPref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    private fun readLong(key: String): Long {
        return sharedPref.getLong(key, 0L)
    }

    private fun writeLong(key: String, value: Long) {
        val editor = sharedPref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

}