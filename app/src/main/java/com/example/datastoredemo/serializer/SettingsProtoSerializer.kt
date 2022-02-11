package com.example.datastoredemo.serializer



import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer

import com.example.datastoredemo.SettingsProto
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

/**
 * Serializer to read/write data of type SettingProto
 */
object SettingsProtoSerializer : Serializer<SettingsProto> {
    override val defaultValue: SettingsProto
        get() = SettingsProto.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): SettingsProto {
        return try {
            SettingsProto.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            exception.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: SettingsProto, output: OutputStream) {
        t.writeTo(output)
    }
}

