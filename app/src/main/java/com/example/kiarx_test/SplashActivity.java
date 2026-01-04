package com.example.kiarx_test;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fallback: Set a simple layout or view to ensure activity can render something
        // This avoids issues if the theme background is problematic
        setContentView(new View(this));
        
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            try {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 2000); 
    }
}
