package com.example.messagecounter

import java.util.*

data class SmsData(
    var address: String,
    var msg: String,
    var time: String,
)

data class Conversation(
    val number: String,
    val message: List<Message>
)

data class Message(
    val number: String,
    val body: String,
    val date: Date
)