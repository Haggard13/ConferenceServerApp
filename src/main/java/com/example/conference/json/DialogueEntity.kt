package com.example.conference.json

data class DialogueEntity(
    var id: Int,
    var second_user_id: Int,
    var second_user_email: String,
    var second_user_name: String,
    var second_user_surname: String,
    var last_message: String,
    var last_message_time: Long
)
