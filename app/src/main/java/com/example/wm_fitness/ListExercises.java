package com.example.wm_fitness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wm_fitness.Adapter.RecyclerViewAdapter;
import com.example.wm_fitness.Models.ExerciseModel;

import java.util.ArrayList;
import java.util.List;

public class ListExercises extends AppCompatActivity {

    List<ExerciseModel> exerciseList = new ArrayList<>();
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    String vkiInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_exercises);

        Intent intent = getIntent();
        vkiInfo = intent.getExtras().getString("vki");

        initData();
        bindViews();

        adapter = new RecyclerViewAdapter(exerciseList,getBaseContext());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        SharedPreferences mySharedPreferences = this.getSharedPreferences("VKI", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        editor.putString("vkiInfo",vkiInfo);
        editor.apply();

    }

    public void bindViews(){
        recyclerView = (RecyclerView)findViewById(R.id.list_ex);
    }

    private void initData() {

        exerciseList.add(new ExerciseModel(R.drawable.one,"Yarım Mekik"));
        exerciseList.add(new ExerciseModel(R.drawable.two,"Düz Mekik"));
        exerciseList.add(new ExerciseModel(R.drawable.three,"Hareketli Mekik"));
        exerciseList.add(new ExerciseModel(R.drawable.four,"Top Hareketi"));
        exerciseList.add(new ExerciseModel(R.drawable.five,"Bel ve Bacak Kaldırma"));
        exerciseList.add(new ExerciseModel(R.drawable.six,"Bisiklet Crounch Hareketi"));
        exerciseList.add(new ExerciseModel(R.drawable.seven,"Düz Crounch Hareketi"));
        exerciseList.add(new ExerciseModel(R.drawable.eight,"Yukarı Top Kaldırma"));
        exerciseList.add(new ExerciseModel(R.drawable.nine,"Ayakla İp Çekma"));
        exerciseList.add(new ExerciseModel(R.drawable.ten,"Tam Mekik"));
        exerciseList.add(new ExerciseModel(R.drawable.eleven,"Tam Mekik Alternatif"));

    }



}