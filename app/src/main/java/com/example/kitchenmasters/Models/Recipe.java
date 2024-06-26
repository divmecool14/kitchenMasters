package com.example.kitchenmasters.Models;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
   public boolean vegetarian;
   public boolean vegan;
   public boolean glutenFree;
   public boolean dairyFree;
   public boolean veryHealthy;
   public boolean cheap;
   public boolean veryPopular;
   public boolean sustainable;
   public boolean lowFodmap;
   public int weightWatcherSmartPoints;
   public String gaps;
   public int preparationMinutes;
   public int cookingMinutes;
   public int aggregateLikes;
   public int healthScore;
   public String creditsText;
   public String sourceName;
   public double pricePerServing;

   public int id;
   public String title;
   public int readyInMinutes;
   public int servings;
   public String sourceUrl;
   public String image;
   public String imageType;
   public String summary;
   public ArrayList<String> cuisines;
   public ArrayList<String> dishTypes;
   public ArrayList<String> diets;
   public ArrayList<String> occasions;
   public String instructions;

   public Object originalId;
   public double spoonacularScore;
   public String spoonacularSourceUrl;
   public String license;

   private String imageUrl;

   private List<Step> steps; // Additional field to store detailed steps


}
