package com.example.tfs2.model

import android.content.Context
import com.example.tfs2.R
import com.example.tfs2.view.listener.InstructionListener
import com.example.tfs2.view.listener.RandomRecipeResponseListener
import com.example.tfs2.view.listener.RecipeDetailsListener
import com.example.tfs2.model.recipe.InstructionResponse
import com.example.tfs2.model.recipe.RandomRecipeApiResponse
import com.example.tfs2.model.recipe.RecipeDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class RecipeRequestManager(private var context: Context) {
    private var retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRandomRecipes(listener: RandomRecipeResponseListener, tags: List<String>) {
        val callRandomRecipes: CallRandomRecipes = retrofit.create(CallRandomRecipes::class.java)
        val call: Call<RandomRecipeApiResponse> = callRandomRecipes.callRandomRecipe(context.getString(
            R.string.api_key
        ), "10", tags)
        call.enqueue(object : Callback<RandomRecipeApiResponse> {
            override fun onResponse(call: Call<RandomRecipeApiResponse>, response: Response<RandomRecipeApiResponse>){
                if (!response.isSuccessful){
                    listener.didError(response.message())
                    return
                }
                listener.didFetch(response.body(), response.message())
            }

            override fun onFailure(call: Call<RandomRecipeApiResponse>, t: Throwable){
                t.message?.let { listener.didError(it) }
            }
        })
    }

    fun getRecipeDetails(listener: RecipeDetailsListener, id: Int) {
        val callRecipeDetails: CallRecipeDetails = retrofit.create(CallRecipeDetails::class.java)
        val call: Call<RecipeDetailsResponse> = callRecipeDetails.callRecipeDetails(id, context.getString(
            R.string.api_key
        ))
        call.enqueue(object: Callback<RecipeDetailsResponse> {
            override fun onResponse(
                call: Call<RecipeDetailsResponse>,
                response: Response<RecipeDetailsResponse>
            ) {
                if (!response.isSuccessful){
                    listener.didError(response.message())
                    return
                }
                response.body()?.let { listener.didFetch(it, response.message()) }
            }

            override fun onFailure(call: Call<RecipeDetailsResponse>, t: Throwable) {
                t.message?.let { listener.didError(it) }
            }
        })
    }

    fun getInstructions(listener: InstructionListener, id: Int) {
        val callInstructions: CallInstructions = retrofit.create(CallInstructions::class.java)
        val call: Call<List<InstructionResponse>> = callInstructions.callInstructions(id, context.getString(
            R.string.api_key
        ))
        call.enqueue(object: Callback<List<InstructionResponse>> {
            override fun onResponse(
                call: Call<List<InstructionResponse>>,
                response: Response<List<InstructionResponse>>
            ) {
                if(!response.isSuccessful){
                    listener.didError(response.message())
                    return
                }
                response.body()?.let { listener.didFetch(it,response.message()) }
            }

            override fun onFailure(call: Call<List<InstructionResponse>>, t: Throwable) {
                t.message?.let { listener.didError(it) }
            }
        })
    }

    interface CallRandomRecipes {
        @GET("recipes/random")
        fun callRandomRecipe(
            @Query("apiKey") apiKey: String,
            @Query("number") number: String,
            @Query("tags") tags: List<String>
        ): Call<RandomRecipeApiResponse>
    }

    private interface CallRecipeDetails {
        @GET("recipes/{id}/information")
        fun callRecipeDetails(
            @Path("id") id: Int,
            @Query("apiKey") apiKey: String
        ): Call<RecipeDetailsResponse>
    }

    private interface CallInstructions {
        @GET("recipes/{id}/analyzedInstructions")
        fun callInstructions(
            @Path("id") id: Int,
            @Query("apiKey") apiKey: String
        ): Call<List<InstructionResponse>>
    }
}