package com.example.groapp.volunteering.models

data class VolunteeringModel(
    var volunteeringId: String? = null,
    var userId: String? = null,
    var userName: String? = null,
    var hours: String? = null,
    var gardenId: String? = null,
    var gardenName: String? = null,
    var date: String? = null,
    var status: String = "Pending"
)