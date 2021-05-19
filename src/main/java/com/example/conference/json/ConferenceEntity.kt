package com.example.conference.json

data class ConferenceEntity(
    var id: Int,
    var name: String,
    var count: Int,
    var last_message: String,
    var last_message_time: Long
)