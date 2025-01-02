package com.example.kelompok10_tickdone.models

import java.sql.Time
import java.util.Date

data class Task(
    val id : String? = null,
    val name : String? = null,
    val description : String? = null,
    val date : String? = null,
    val time : String? = null,
    val user : String? = null,
    val imageUri : String? = null
)
