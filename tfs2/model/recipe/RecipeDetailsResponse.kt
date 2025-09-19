package com.example.tfs2.model.recipe

class RecipeDetailsResponse {
    var id: Int = 0
    lateinit var title: String
    lateinit var image: String
    lateinit var imageType: String
    var servings: Int = 0
    var readyInMinutes: Int = 0
    lateinit var license: String
    lateinit var sourceName: String
    lateinit var sourceUrl: String
    lateinit var spoonacularSourceUrl: String
    var aggregateLikes: Int = 0
    var HealthScore: Double = 0.0
    var spoonacularScore: Double = 0.0
    var pricePerServing: Double = 0.0
    lateinit var analyzedInstructions: List<AnalyzedInstruction>
    var cheap: Boolean = false
    lateinit var creditsText: String
    lateinit var cuisines: List<Any>
    var dairyFree: Boolean = false
    lateinit var diets: List<String>
    lateinit var gaps: String
    var glutenFree: Boolean = false
    lateinit var instructions: String
    var ketogenic: Boolean = false
    var lowFodmap: Boolean = false
    lateinit var occasions: List<String>
    var sustainable: Boolean = false
    var vegan: Boolean = false
    var vegetarian: Boolean = false
    var veryHealthy: Boolean = false
    var veryPopular: Boolean = false
    var whole30: Boolean = false
    var weightWatcherSmartPoints: Int = 0
    lateinit var dishTypes: List<String>
    lateinit var extendedIngredients: List<ExtendedIngredient>
    lateinit var summary: String
    lateinit var winePairing: WinePairing
}