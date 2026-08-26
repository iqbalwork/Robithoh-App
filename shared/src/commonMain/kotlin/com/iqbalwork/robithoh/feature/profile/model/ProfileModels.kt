package com.iqbalwork.robithoh.feature.profile.model

import kotlinx.serialization.Serializable

@Serializable
data class InstitutionItem(
    val id: String,
    val name: String,
    val acronym: String,
    val description: String,
    val roleCategory: String,
    val logoDrawable: String? = null
)

@Serializable
data class PesantrenProfile(
    val name: String,
    val location: String,
    val tagline: String,
    val historyText: String,
    val mursyidName: String,
    val mursyidTitle: String,
    val mursyidBiography: String,
    val institutions: List<InstitutionItem>
)
