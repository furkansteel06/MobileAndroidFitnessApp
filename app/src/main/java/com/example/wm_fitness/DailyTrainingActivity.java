package com.example.wm_fitness;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wm_fitness.Models.ExerciseModel;
import com.example.wm_fitness.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class DailyTrainingActivity extends AppCompatActivity {

    Button btnStart;
    ImageView ex_image;
    TextView txtGetReady, txtCountdown, txtTimer, ex_name;
    ProgressBar progressBar;
    LinearLayout layoutGetReady;
    String vkiInfo;
    MediaPlayer mp, mp2;

    int ex_id = 0;

    List<ExerciseModel> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily__training);

        Intent intent = getIntent();
        vkiInfo = intent.getExtras().getString("vki");

        initData();
        bindViews();

        //set data
        progressBar.setMax(list.size());

        setExerciseInformation(ex_id);
        mp = MediaPlayer.create(DailyTrainingActivity.this, R.raw.a1);
        mp2 = MediaPlayer.create(DailyTrainingActivity.this, R.raw.ticktok);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnStart.getText().toString().toLowerCase().equals("başlat")){
                    showGetReady();
                    btnStart.setText("geç");
                }
                else if(btnStart.getText().toString().toLowerCase().equals("geç")){
                    if(Float.parseFloat(vkiInfo) < 20)
                        exercisesEasyModeCountDown.cancel();
                    else if(Float.parseFloat(vkiInfo) < 25)
                        exercisesMediumModeCountDown.cancel();
                    else if(Float.parseFloat(vkiInfo) < 30)
                        exercisesHardModeCountDown.cancel();
                    else
                        exercisesSoHardModeCountDown.cancel();

                    restTimeCountDown.cancel();

                    if(ex_id < list.size()){
                        showRestTime();
                        ex_id++;
                        progressBar.setProgress(ex_id);
                        txtTimer.setText("");
                    }
                    else
                        showFinished();

                }
                else{
                    if(Float.parseFloat(vkiInfo) < 20)
                        exercisesEasyModeCountDown.cancel();
                    else if(Float.parseFloat(vkiInfo) < 25)
                        exercisesMediumModeCountDown.cancel();
                    else if(Float.parseFloat(vkiInfo) < 30)
                        exercisesHardModeCountDown.cancel();
                    else
                        exercisesSoHardModeCountDown.cancel();

                    restTimeCountDown.cancel();

                    if(ex_id < list.size())
                        setExerciseInformation(ex_id);
                    else
                        showFinished();
                }

            }
        });
    }

    private void bindViews(){
        btnStart = (Button)findViewById(R.id.btnStart);
        ex_image = (ImageView)findViewById(R.id.detail_image);
        txtCountdown = (TextView)findViewById(R.id.txtCountdown);
        txtGetReady = (TextView)findViewById(R.id.txtGetReady);
        txtTimer = (TextView)findViewById(R.id.timer);
        ex_name = (TextView)findViewById(R.id.title);
        layoutGetReady = (LinearLayout)findViewById(R.id.layout_get_ready);
        progressBar = (MaterialProgressBar)findViewById(R.id.progressBar);
    }

    private void showRestTime() {
        ex_image.setVisibility(View.INVISIBLE);
        txtTimer.setVisibility(View.INVISIBLE);
        btnStart.setText("Atla");
        btnStart.setVisibility(View.VISIBLE);
        layoutGetReady.setVisibility(View.VISIBLE);

        restTimeCountDown.start();

        txtGetReady.setText("DİNLENME ZAMANI");
    }

    private void showGetReady() {
        ex_image.setVisibility(View.INVISIBLE);
        btnStart.setVisibility(View.INVISIBLE);
        txtTimer.setVisibility(View.VISIBLE);

        layoutGetReady.setVisibility(View.VISIBLE);

        txtGetReady.setText("HAZIRLAN");
        new CountDownTimer(7000,1000){

            @Override
            public void onTick(long l) {
                txtCountdown.setText(""+ (l-1000)/1000);
            }

            @Override
            public void onFinish() {
                showExercises();
            }
        }.start();

    }

    private void showExercises() {

        if(ex_id < list.size()) //Liste boyutu
        {
            ex_image.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
            layoutGetReady.setVisibility(View.INVISIBLE);

            if(Float.parseFloat(vkiInfo) < 20)
                exercisesEasyModeCountDown.start();
            else if(Float.parseFloat(vkiInfo) < 25)
                exercisesMediumModeCountDown.start();
            else if(Float.parseFloat(vkiInfo) < 30)
                exercisesHardModeCountDown.start();
            else
                exercisesSoHardModeCountDown.start();

            //Set data
            ex_image.setImageResource(list.get(ex_id).getImage_id());
            ex_name.setText(list.get(ex_id).getName());

        }
        else
            showFinished();
    }

    private void showFinished() {
        ex_image.setVisibility(View.INVISIBLE);
        btnStart.setVisibility(View.INVISIBLE);
        txtTimer.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        layoutGetReady.setVisibility(View.VISIBLE);

        txtGetReady.setText("Tamamlandı !!!");
        txtCountdown.setText("Tebrikler ! \nBugünkü egzersizlerini tamamladın");
        txtCountdown.setTextSize(20);


    }

    //Countdown
    CountDownTimer exercisesEasyModeCountDown = new CountDownTimer(Common.TIME_LIMIT_EASY,1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText(""+ (l/1000));
            if (l < 6000)
                mp2.start();
        }

        @Override
        public void onFinish() {
            if(ex_id < list.size()-1){
                ex_id++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");

                setExerciseInformation(ex_id);
                mp.start();
                btnStart.setText("Başlat");
            }
            else {
                showFinished();
            }
        }
    };
    CountDownTimer exercisesMediumModeCountDown = new CountDownTimer(Common.TIME_LIMIT_MEDIUM,1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText(""+ (l/1000));
            if (l < 6000)
                mp2.start();
        }

        @Override
        public void onFinish() {
            if(ex_id < list.size()-1){
                ex_id++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");

                setExerciseInformation(ex_id);
                mp.start();
                btnStart.setText("Başlat");
            }
            else {
                showFinished();
            }
        }
    };
    CountDownTimer exercisesHardModeCountDown = new CountDownTimer(Common.TIME_LIMIT_HARD,1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText(""+ (l/1000));
            if (l < 6000)
                mp2.start();
        }

        @Override
        public void onFinish() {
            if(ex_id < list.size()-1){
                ex_id++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");

                setExerciseInformation(ex_id);
                mp.start();
                btnStart.setText("Başlat");
            }
            else {
                showFinished();
            }
        }
    };
    CountDownTimer exercisesSoHardModeCountDown = new CountDownTimer(Common.TIME_LIMIT_SOHARD,1000) {
        @Override
        public void onTick(long l) {
            txtTimer.setText(""+ (l/1000));
            if (l < 6000)
                mp2.start();
        }

        @Override
        public void onFinish() {
            if(ex_id < list.size()-1){
                ex_id++;
                progressBar.setProgress(ex_id);
                txtTimer.setText("");

                setExerciseInformation(ex_id);
                mp.start();
                btnStart.setText("Başlat");
            }
            else {
                showFinished();
            }
        }
    };

    CountDownTimer restTimeCountDown = new CountDownTimer(11000,1000) {
        @Override
        public void onTick(long l) {
            txtCountdown.setText(""+(l/1000));
        }

        @Override
        public void onFinish() {
            setExerciseInformation(ex_id);
            showExercises();
        }
    };

    private void setExerciseInformation(int id) {
        ex_image.setImageResource(list.get(id).getImage_id());
        ex_name.setText(list.get(id).getName());
        btnStart.setText("Başlat");

        ex_image.setVisibility(View.VISIBLE);
        btnStart.setVisibility(View.VISIBLE);
        txtTimer.setVisibility(View.VISIBLE);

        layoutGetReady.setVisibility(View.INVISIBLE);

    }

    private void initData() {

        list.add(new ExerciseModel(R.drawable.one,"Yarım Mekik"));
        list.add(new ExerciseModel(R.drawable.two,"Düz Mekik"));
        list.add(new ExerciseModel(R.drawable.three,"Hareketli Mekik"));
        list.add(new ExerciseModel(R.drawable.four,"Top Hareketi"));
        list.add(new ExerciseModel(R.drawable.five,"Bel ve Bacak Kaldırma"));
        list.add(new ExerciseModel(R.drawable.six,"Bisiklet Crounch Hareketi"));
        list.add(new ExerciseModel(R.drawable.seven,"Düz Crounch Hareketi"));
        list.add(new ExerciseModel(R.drawable.eight,"Yukarı Top Kaldırma"));
        list.add(new ExerciseModel(R.drawable.nine,"Ayakla İp Çekma"));
        list.add(new ExerciseModel(R.drawable.ten,"Tam Mekik"));
        list.add(new ExerciseModel(R.drawable.eleven,"Tam Mekik Alternatif"));

    }
}