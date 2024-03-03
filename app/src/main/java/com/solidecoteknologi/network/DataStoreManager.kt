package com.solidecoteknologi.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val setDataStore : DataStore<Preferences>){
    fun getToken(): Flow<String> = setDataStore.data.map { pref ->
        pref[TOKEN] ?: "undefined"
    }

    fun getStatus(): Flow<Boolean> = setDataStore.data.map { pref ->
        pref[ISLOGIN] ?: false
    }
    suspend fun saveDataStore(isToken : String, isName: String, isLogin: Boolean) {
        setDataStore.edit { preferences ->
            preferences[TOKEN] = isToken
            preferences[NAMEKEY] = isName
            preferences[ISLOGIN] = isLogin
        }
    }

    suspend fun delete(){
        setDataStore.edit { pref -> pref.clear() }
    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val NAMEKEY = stringPreferencesKey("name")
        private val ISLOGIN = booleanPreferencesKey("isLogin")
    }
}