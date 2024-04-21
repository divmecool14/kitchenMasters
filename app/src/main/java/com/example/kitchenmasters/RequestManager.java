package com.example.kitchenmasters;

import android.content.Context;

import com.example.kitchenmasters.Listeners.InstructionsListener;
import com.example.kitchenmasters.Listeners.RandomRecipeResponseListener;
import com.example.kitchenmasters.Models.RandomRecipeApiResponse;
import com.example.kitchenmasters.Models.RecipeInstructionResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RequestManager {
    Context context; // Declaration of Context variable
    //whats retrofit:

    Retrofit retrofit = new Retrofit.Builder() // Create Retrofit instance
            .baseUrl("https://api.spoonacular.com/") // Set base URL for API
            .addConverterFactory(GsonConverterFactory.create()) // Add Gson converter factory for JSON serialization/deserialization
            .build(); // Build Retrofit instance

    public RequestManager(Context context) {
        this.context = context; // Initialize context variable with provided context
    }


    // Method to fetch random recipes from API
    public void getRandomRecipes(RandomRecipeResponseListener listener) {
        String num = "1";
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class); // Create Retrofit service for API call
        Call<RandomRecipeApiResponse> call = callRandomRecipes.callRandomRecipe(num, "cff4fe5f5fd946dfa36b6f19334d5326"); // Make API call to fetch random recipes
        call.enqueue(new Callback<RandomRecipeApiResponse>() { // Asynchronous execution of API call
            @Override
            public void onResponse(Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()) { // Check if response is successful
                    listener.didError(response.message()); // Call listener method for error handling
                    return;
                }
                listener.didFetch(response.body(), response.message()); // Call listener method for successful response
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage()); // Call listener method for failure
            }
        });
    }


    //METHOD FOR THE SEARCH RECIPES
        public void getInstructions(String query ,InstructionsListener listener){

            CallInstructions callInstructions = retrofit.create(CallInstructions.class);
            Call<RecipeInstructionResponse> call = callInstructions.callInstructions(query ,"1","cff4fe5f5fd946dfa36b6f19334d5326");
            call.enqueue(new Callback<RecipeInstructionResponse>() {

                @Override
                public void onResponse(Call<RecipeInstructionResponse> call, Response<RecipeInstructionResponse> response) {
                    if(!response.isSuccessful()){ // Check if response is successful
                        listener.didError(response.message()); // Call listener method for error handling
                        return;
                    }
                    listener.didFetch(response.body(), response.message());
                }

                @Override
                public void onFailure(Call<RecipeInstructionResponse> caller, Throwable t) {
                    listener.didError(t.getMessage()); // Call listener method for failure
                }
            });
        }


    // Interface for Retrofit service
    private interface CallRandomRecipes{
        // Method to make API call to get random recipes
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("number") String number,
                @Query("apiKey") String apiKey // Query parameter: API key
        );
    }

    private interface CallInstructions{
        @GET("recipes/complexSearch")
        Call<RecipeInstructionResponse> callInstructions(
                @Query("query") String query,
                @Query("number") String number,
                @Query("apiKey") String apiKey // Query parameter: API key


        );
    }
}