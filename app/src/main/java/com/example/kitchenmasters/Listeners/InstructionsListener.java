package com.example.kitchenmasters.Listeners;

import com.example.kitchenmasters.Models.RecipeInstructionResponse;

public interface InstructionsListener {
    void didFetch(RecipeInstructionResponse response, String message);
    void didError(String message);
}
