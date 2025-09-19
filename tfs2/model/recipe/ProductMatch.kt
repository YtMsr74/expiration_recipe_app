package com.example.tfs2.model.recipe

class ProductMatch {
    var id: Int = 0
    lateinit var title: String
    lateinit var description: String
    lateinit var price: String
    lateinit var imageUrl: String
    var averageRating: Double = 0.0
    var ratingCount: Double = 0.0
    var score: Double = 0.0
    lateinit var link: String
}