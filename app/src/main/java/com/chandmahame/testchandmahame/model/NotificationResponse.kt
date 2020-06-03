package com.chandmahame.testchandmahame.model

data class NotificationResponse(
    val id: Int,
    val big_text: String,
    val big_content_title: String,
    val summary_text: String,
    val content_title: String,
    val path: String
)