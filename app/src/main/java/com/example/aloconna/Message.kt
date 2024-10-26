package com.example.aloconna

interface Message{

    val msgId :String
    val sanderId : String
    val receiver : String

}



data class TextMessage(
    val text : String? = null,
    override var msgId: String="",
    override val sanderId: String="",
    override val receiver: String=""
):Message

data class MessagewithImage(

    val imageMsg : String? = null,
    override val msgId: String,
    override val sanderId: String,
    override val receiver: String
):Message

