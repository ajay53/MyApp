package com.goazzi.workoutmanager.helper

class Constant {
    companion object {
        const val LOW_IMPORTANCE_CHANNEL_ID: String = "low_importance_service_channel"
        const val DEFAULT_CHANNEL_ID: String = "foreground_service_channel"
        const val DEFAULT_NOTIFICATION_ID: Int = 101
        const val DAYS_FOR_FLEXIBLE_UPDATE: Int = 0
        const val UPDATE_REQUEST_CODE: Int = 201


        //Sync Status
        const val STATUS_CHANGED: String = "status_changed"
        const val STATUS_UNCHANGED: String = "status_unchanged"
        const val STATUS_DELETED: String = "status_deleted"

        //Data Store Shared Preferences
        const val DATA_STORE: String = "data_store_pref"
        const val KEY_USERNAME: String = "key_username"
        const val KEY_EMAIL_ID: String = "key_email_Id"
    }

    enum class Category {
        CARDIO, SHOULDER, CHEST, BACK, LEGS, ABS, ARMS, UPPER_BODY, FULL_BODY;
    }
}