package com.example.tfs2.model

data class ItemCodeResponse(
    val code: String,
    val status: Int,
    val statusVerbose: String,
    val product: Product?
)

data class Product(
    val product_name: String?,
    val product_nameEn: String?,
    val brands: String?,
    val nutritionGrade: String?,
    val nutriScore: NutriScoreData?,
    val nutriments: Nutriments?,
    val ingredientsText: String?,
    val imageUrl: String?,
    val categories: String?,
    val allergens: String?
)

data class NutriScoreData(
    val score: Int?,
    val energyPoints: Int?,
    val sugarsPoints: Int?
)

data class Nutriments(
    val energy100g: Double?,
    val sugars100g: Double?,
    val fat100g: Double?,
    val proteins100g: Double?
)