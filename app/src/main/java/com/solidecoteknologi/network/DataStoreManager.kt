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
        pref[TOKEN] ?: ""
    }

    fun getIdAccount(): Flow<String> = setDataStore.data.map { pref ->
        pref[IDACC] ?: ""
    }

    fun getStatus(): Flow<Boolean> = setDataStore.data.map { pref ->
        pref[ISLOGIN] ?: false
    }
    suspend fun saveDataStore(isToken : String, isId: String, isLogin: Boolean) {
        setDataStore.edit { preferences ->
            preferences[TOKEN] = isToken
            preferences[IDACC] = isId
            preferences[ISLOGIN] = isLogin
        }
    }

    suspend fun saveStatus(isStatus : Boolean) {
        setDataStore.edit { preferences ->
            preferences[ISLOGIN] = isStatus
        }
    }

    fun getStoredValues(): Flow<DataAccountValues> = setDataStore.data.map { pref ->
        DataAccountValues(
            token = pref[TOKEN] ?: "",
            idAccount = pref[IDACC] ?: ""
        )
    }

    suspend fun delete(){
        setDataStore.edit {
            setDataStore.edit { pref ->
                pref.clear()
            }
        }
    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val IDACC = stringPreferencesKey("idAcc")
        private val ISLOGIN = booleanPreferencesKey("isLogin")
        private val ACCOUNT = stringPreferencesKey("account")
    }
}

data class DataAccountValues(
    val token: String,
    val idAccount: String
)