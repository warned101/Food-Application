package com.example.eaters;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.eaters.Interface.ItemClickListener;
import com.example.eaters.ViewHolder.FoodViewHolder;
import com.example.eaters.ViewHolder.MenuViewHolder;
import com.example.eaters.model.Category;
import com.example.eaters.model.Food;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodlist;
    FirebaseRecyclerAdapter adapter;

    String categoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //Firebase
        database = FirebaseDatabase.getInstance();
        foodlist = database.getReference("Foods");

        //Loading the menu
        recyclerView = findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent here
        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {
            loadFoodList(categoryId);
        }
    }

    private void loadFoodList(String categoryId) {


        FirebaseRecyclerOptions<Food> options =

                new FirebaseRecyclerOptions.Builder<Food>()

                        .setQuery(foodlist.orderByChild("MenuId").equalTo(categoryId), Food.class).build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder viewHolder, int position, @NonNull Food model) {

                viewHolder.foodName.setText(model.getName());

                Picasso.get().load(model.getImage()).into(viewHolder.foodImage);

                final Food local = model;

                viewHolder.setItemClickListener(new ItemClickListener() {

                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Toast.makeText(FoodList.this, ""+local.getName(), Toast.LENGTH_SHORT).show();


                    }

                });

            }

        };

        recyclerView.setAdapter(adapter);

    }

    @Override

    public void onStart() {

        super.onStart();

        adapter.startListening();

    }


    @Override

    public void onStop() {

        super.onStop();

        adapter.stopListening();
    }
}
