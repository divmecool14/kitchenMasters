package com.example.kitchenmasters.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kitchenmasters.Models.Recipe;
import com.example.kitchenmasters.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RandomRecipeAdapter extends RecyclerView.Adapter<RandomRecipeViewHolder>{//uses the recyclerview holder
   Context context;
   List<Recipe> list;

   public RandomRecipeAdapter(Context context, ArrayList<Recipe> recipes) {
      this.context = context;
      this.list = recipes;
   }

   //these 3 overrides neccesary requiruired by the recycler view holder.
   @NonNull
   @Override // this one inflates the xml layout list_random_recipe.xml
   public RandomRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      return new RandomRecipeViewHolder(LayoutInflater.from(context).inflate(R.layout.list_random_recipe, parent, false));
   }

   @Override //retrives recipe object from the list at specified position and sets the data
   //using reference from the randomrecipeviewholder.
   public void onBindViewHolder(@NonNull RandomRecipeViewHolder holder, int position) {
      holder.textView_title.setText(list.get(position).title);
      holder.textView_title.setSelected(true);
      holder.textView_like.setText(list.get(position).aggregateLikes+"likes");
      holder.textView_time.setText(list.get(position).readyInMinutes+"Minutes");
      Picasso.get().load(list.get(position).image).into(holder.imageView_food); // loads image into the imageview using picasso library
      holder.textView_ingredients.setText(list.get(position).instructions);

   }

   @Override//RETURNS SIZE OF IT
   public int getItemCount() {
      return list.size();
   }
}

class RandomRecipeViewHolder extends RecyclerView.ViewHolder{
   CardView random_list_container; //card view good for box type things
   TextView textView_title, textView_time, textView_like, textView_steps, textView_ingredients ;
   ImageView imageView_food;


   public RandomRecipeViewHolder(@NonNull View itemView){
      super(itemView);
      random_list_container = itemView.findViewById(R.id.random_list_container);
      textView_title = itemView.findViewById(R.id.textView_title);
      textView_time = itemView.findViewById(R.id.textView_time);

      textView_like = itemView.findViewById(R.id.textView_like);
      imageView_food = itemView.findViewById(R.id.imageView_food);
      textView_steps = itemView.findViewById(R.id.textView_steps);
      textView_ingredients = itemView.findViewById(R.id.textView_ingredients);
   }
}
