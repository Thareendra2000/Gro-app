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
){
    fun toProduct(production: ProductModel, key: String, value: String): ProductModel {
        when (key.toLowerCase()) {
            "production_id" -> production.production_id = value
            "garden_name" -> production.garden_name = value
            "garden_id" -> production.garden_id = value
            "category" -> production.category = value
            "description" -> production.description = value
            "best_before" -> production.best_before = Date(value.toLong())
            "name" -> production.name = value
            "quantity" -> production.quantity = value
            "unit" -> production.unit = value
            "unit_price" -> production.unit_price = value
            "img_url" -> production.img_url = value
        }
        return production
    }
}
