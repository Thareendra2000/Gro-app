package com.example.groapp.Models

import java.util.*

data class ProductModel (
    var production_id : String? = null,
    var garden_name : String? = null,
    var garden_id : String? = null,
    var category : String? = null,
    var description : String? = null,
    var best_before : Date? = null,
    var name : String? = null,
    var quantity : Double? = null,
    var unit : String? = null,
    var unit_price : Double? = null
)
