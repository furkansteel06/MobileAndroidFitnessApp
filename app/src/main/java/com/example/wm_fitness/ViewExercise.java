package com.example.wm_fitness;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wm_fitness.Utils.Common;

public class ViewExercise extends AppCompatActivity {

    int image_id;
    String name;
    String vki;

    TextView timer, title;
    ImageView detail_image;
    CountDownTimer yourCountDownTimer;
    Button btnStart;

    boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        SharedPreferences mySharedPreferences = this.getSharedPreferences("VKI", Context.MODE_PRIVATE);
        vki = mySharedPreferences.getString("vkiInfo", "");

        final MediaPlayer mp = MediaPlayer.create(ViewExercise.this, R.raw.a1);
        final MediaPlayer mp2 = MediaPlayer.create(ViewExercise.this, R.raw.ticktok);



        timer = (TextView)findViewById(R.id.timer);
        title = (TextView)findViewById(R.id.title);
        detail_image = (ImageView)findViewById(R.id.detail_image);
        btnStart =(Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isRunning){
                    btnStart.setText("BİTİR");

                    int timeLimit = 0;

                    if (Float.parseFloat(vki) < 20) {
                        timeLimit = Common.TIME_LIMIT_EASY;
                    } else if (Float.parseFloat(vki) < 25) {
                        timeLimit = Common.TIME_LIMIT_MEDIUM;
                    } else if (Float.parseFloat(vki) < 30) {
                        timeLimit = Common.TIME_LIMIT_HARD;
                    } else {
                        timeLimit = Common.TIME_LIMIT_SOHARD;
                    }

                    yourCountDownTimer = new CountDownTimer(timeLimit,1000)
                    {

                        @Override
                        public void onTick(long l) {
                            timer.setText("" + l/1000);
                            if (l < 6000)
                                mp2.start();
                        }



                        @Override
                        public void onFinish() {
                            //Ads
                            Toast.makeText(com.example.wm_fitness.ViewExercise.this, "Tamamlandı!!!", Toast.LENGTH_SHORT).show();
                            mp.start();
                            finish();
                        }
                    }.start();
                }else {
                    //Ads
                    yourCountDownTimer.cancel();
                    Toast.makeText(com.example.wm_fitness.ViewExercise.this, "Egzersiz Sonlandırıldı!!!", Toast.LENGTH_SHORT).show();
                    finish();
                }

                isRunning = !isRunning;
            }
        });


        timer.setText("");

        if(getIntent() != null){
            image_id = getIntent().getIntExtra("image_id",-1);
            name = getIntent().getStringExtra("name");

            detail_image.setImageResource(image_id);
            title.setText(name);
        }

    }
}