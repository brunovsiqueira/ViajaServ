package com.example.brunovsiq.viajaserv.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.brunovsiq.viajaserv.R;

public class MainActivity extends AppCompatActivity {

    private Button button2017;
    private Button button2018;
    private Button buttonRanking2017;
    private Button buttonRanking2018;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button2017 = findViewById(R.id.viagens_2017);
        button2018 = findViewById(R.id.viagens_2018);
        buttonRanking2017 = findViewById(R.id.ranking_cidades_2017);
        buttonRanking2018 = findViewById(R.id.ranking_cidades_2018);

        button2017.setOnClickListener(button2017ClickListener);
        button2018.setOnClickListener(button2018ClickListener);
        buttonRanking2017.setOnClickListener(buttonRanking2017ClickListener);
        buttonRanking2018.setOnClickListener(buttonRanking2018ClickListener);
    }

    View.OnClickListener button2017ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("ano", "2017");
            startActivity(intent);
        }
    };

    View.OnClickListener button2018ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra("ano", "2018");
            startActivity(intent);
        }
    };

    View.OnClickListener buttonRanking2017ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RankingActivity.class);
            intent.putExtra("ano", "2017");
            startActivity(intent);
        }
    };

    View.OnClickListener buttonRanking2018ClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, RankingActivity.class);
            intent.putExtra("ano", "2018");
            startActivity(intent);
        }
    };

}
