package com.example.groapp

import junit.framework.TestCase
import org.junit.Test


class ProductProfitabilityTest {
    @Test
    fun testMostProfitableProduct() {
        // Mock data
        val products = arrayOf("Carrots", "Tomatoes", "Broccoli")
        val amounts = arrayOf(10, 15, 20)
        val prices = arrayOf(2.5, 3.0, 2.0)

        // Calculate profits for each product
        val profits = mutableListOf<Double>()
        for (i in products.indices) {
            profits.add(amounts[i] * prices[i])
        }

        // Find the most profitable product
        val maxProfit = profits.maxOrNull()
        val mostProfitableProduct = products[profits.indexOf(maxProfit)]

        // Verify the most profitable product
        TestCase.assertEquals("Tomatoes", mostProfitableProduct)
    }
}

