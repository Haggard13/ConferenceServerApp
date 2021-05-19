package com.example.conference.json

data class DMessageEntity(
    var id: Int,
    var text: String,
    var date_time: Long,
    var sender_id: Int,
    var dialogue_id: Int,
    var sender_name: String,
    var sender_surname: String,
    var sender_enum: Int,
    var type: Int
)
