package com.bitspice.pocketporo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    // Launch PoroActivity with selectedPoroSkin value, and then terminate StartActivity
    private void launchPoroActivity(int value){
        Intent intent = new Intent(this, PoroActivity.class);
        intent.putExtra("skin", value);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        ImageView startView = (ImageView) findViewById(R.id.startView);
        ImageView basicSkin = (ImageView) findViewById(R.id.skin1);
        ImageView braumSkin = (ImageView) findViewById(R.id.skin2);
        final Context context = this;

        int selectedPoroSkin = TimeUtils.getPoroSkin(context);
        // Nothing selected
        if (selectedPoroSkin == 0){

            // If ImageView basicSkin is selected from StartActivity on first launch, defaultPoroSkin will be skin1 in PoroActivity
            basicSkin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    TimeUtils.setPoroSkin(context, 1);
                    launchPoroActivity(1);
                }
            });

            // If ImageView braumSkin is selected from StartActivity on first launch, defaultPoroSkin will be skin1 in PoroActivity
            braumSkin.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    TimeUtils.setPoroSkin(context, 2);
                    launchPoroActivity(2);
                }
            });

        }
        else if (selectedPoroSkin == 1) {
            //  Basic skin animation
            launchPoroActivity(1);
        } else if (selectedPoroSkin == 2) {
            // Braum(mustache) skin animation
            launchPoroActivity(2);
        }
    }
}
