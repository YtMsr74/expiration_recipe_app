package com.example.tfs2.model.recipe

class ExtendedIngredient {
    var id: Int = 0
    lateinit var aisle: String
    lateinit var image: String
    lateinit var consistency: String
    lateinit var name: String
    lateinit var nameClean: String
    lateinit var original: String
    lateinit var originalString: String
    lateinit var originalName: String
    var amount: Double = 0.0
    lateinit var unit: String
    lateinit var meta: List<String>
    lateinit var metaInformation: List<String>
    lateinit var measures: Measures
}