package com.example.datastoredemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope

import com.example.datastoredemo.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    // with Preferences DataStore
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(binding.etKey.text.toString(), binding.etValue.text.toString())
            }
        }

        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                val value = read(binding.etKey2.text.toString())
                binding.tvValue.text = value ?: "No Value Found"
            }
        }

    }

    private suspend fun save(key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        dataStore.edit { settings ->
            settings[dataStoreKey] = value
        }
    }

    private suspend fun read(key: String): String? {
        val dataStoreKey = stringPreferencesKey(key)
        val preferences = dataStore.data.first()
        return preferences[dataStoreKey]
    }
}