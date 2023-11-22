package com.example.DvsQFood.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.DvsQFood.R;

public class Screen extends AppCompatActivity {
    Button btn_SignIn, btn_SignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);

        btn_SignIn = findViewById(R.id.btn_SignIn);
        btn_SignUp = findViewById(R.id.btn_SignUp);

        btn_SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Screen.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });

        btn_SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Screen.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }
}