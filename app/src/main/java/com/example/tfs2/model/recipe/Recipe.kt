package com.example.tfs2.model.recipe

class Recipe {
    var vegetarian: Boolean = false
    var vegan: Boolean = false
    var glutenFree: Boolean = false
    var dairyFree: Boolean = false
    var veryHealthy: Boolean = false
    var cheap: Boolean = false
    var veryPopular: Boolean = false
    var sustainable: Boolean = false
    var weightWatcherSmartPoints: Int = 0
    lateinit var gaps: String
    var lowFodmap: Boolean = false
    var aggregateLikes: Int = 0
    var spoonacularScore: Double = 0.0
    var healthScore: Double = 0.0
    lateinit var creditsText: String
    lateinit var license: String
    lateinit var sourceName: String
    var pricePerServing: Double = 0.0
    lateinit var extendedIngredients: List<ExtendedIngredient>
    var id: Int = 0
    lateinit var title: String
    var readyInMinutes: Int = 0
    var servings: Int = 0
    lateinit var sourceUrl: String
    lateinit var image: String
    lateinit var imageType: String
    lateinit var summary: String
    lateinit var cuisines: List<Any>
    lateinit var dishTypes: List<String>
    lateinit var diets: List<String>
    lateinit var occasions: List<String>
    lateinit var instructions: String
    lateinit var analyzedInstructions: List<AnalyzedInstruction>
    lateinit var originalId: Any
    lateinit var spoonacularSourceUrl: String
}