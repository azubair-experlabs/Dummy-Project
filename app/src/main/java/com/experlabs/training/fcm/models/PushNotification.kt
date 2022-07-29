package com.experlabs.training.fcm.models

data class PushNotification(
    var data: NotificationData,
    var to : String
)
