package com.example.kitchenmasters.Listeners;

import com.example.kitchenmasters.Models.RandomRecipeApiResponse;

//interface for the error handling

public interface RandomRecipeResponseListener {
   void didFetch(RandomRecipeApiResponse response, String message);
   void didError(String message);
  // void didFetch(RecipeInstructionResponse instruction, String message);
}
//
// this ones just used for the result of the api

