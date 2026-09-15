package br.com.project.quackbase.model

data class DeviceInfo(
    val manufacturer: String,
    val model: String,
    val androidVersion: String,
    val sdkVersion: Int,
    val totalMemory: Long,
    val availableMemory: Long
)