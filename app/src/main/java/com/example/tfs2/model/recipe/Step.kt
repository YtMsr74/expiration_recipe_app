package com.example.tfs2.model.recipe

class Step {
    var number: Int = 0
    lateinit var step: String
    lateinit var ingredients: List<Ingredient>
    lateinit var equipments: List<Equipment>
    lateinit var length: Length
}