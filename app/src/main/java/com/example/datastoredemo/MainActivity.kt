package com.example.datastoredemo

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.lifecycleScope
import com.example.datastoredemo.databinding.ActivityMainBinding
import com.example.datastoredemo.serializer.SettingsProtoSerializer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException


class MainActivity : AppCompatActivity() {
    // with Preferences DataStore
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    val Context.dataStore: DataStore<SettingsProto> by dataStore(
        fileName = "settings",
        serializer = SettingsProtoSerializer
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                save(binding.etValue.text.toString())
            }
        }

        binding.btnRead.setOnClickListener {
            lifecycleScope.launch {
                 read().collect {
                     binding.tvValue.text = it
                 }

            }
        }

    }

    private suspend fun save(value: String) {
        dataStore.updateData {store ->
            store.toBuilder()
                .setMessage(value)
                .build()
        }
    }

    private suspend fun read(): Flow<String> {
       return dataStore.data
            .catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                if (exception is IOException) {
                    emit(SettingsProto.getDefaultInstance())
                } else {
                    throw exception
                }
            }.map { protoBuilder ->
                protoBuilder.message
            }
    }
}