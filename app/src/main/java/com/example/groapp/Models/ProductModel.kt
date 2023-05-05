package com.example.groapp.Models

import java.util.*

data class ProductModel(
    var production_id: String? = null,
    var garden_name: String? = null,
    var garden_id: String? = null,
    var category: String? = null,
    var description: String? = null,
    var best_before: Date? = null,
    var name: String? = null,
    var quantity: String? = null,
    var unit: String? = null,
    var unit_price: String? = null,
    var img_url: String? = "https://cdn.shopify.com/s/files/1/0572/7889/0150/products/Quince-Flowers-Toronto-Florist-Rosey-Colour-Combo-2_1445x.jpg?v=1639030157"

)
