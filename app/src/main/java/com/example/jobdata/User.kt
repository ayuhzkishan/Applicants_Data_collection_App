package com.example.jobdata

data class User(
    val fullName: String,
    val address: String,
    val tenthPassingYear: String,
    val twelfthPassingYear: String?,
    val twelfthSpecialisation: String?,
    val diplomaSpecialisation: String?,
    val additionalSkills: String?,
    val tenthCertificateUrl: String?,
    val twelfthCertificateUrl: String?
)
