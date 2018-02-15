package com.bitspice.pocketporo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

public class PoroActivity extends AppCompatActivity {
    // Constants for imageArray
    private static final int AGE_BABY = 0;
    private static final int AGE_TEEN = 1;
    private static final int AGE_ADULT = 2;
    private static final int STAGE_DEFAULT = 0;
    private static final int STAGE_HAPPY = 1;
    private static final int STAGE_SAD = 2;
    private static final int STAGE_DEAD = 3;
    private static final int STAGE_EXPLODED = 4;
    private static final int SKIN = 1;
    private static final int SKIN_BRAUM = 2;
    int[][][] imageArray;
    private int skin;
    private int age;
    private ImageView mainView;
    private Handler handler = new Handler();

    // Setting animation to STAGE_DEFAULT through setPoroAnimation method
    private void setPoroAnimation(int poroImage) {
        Drawable newDrawable = getResources().getDrawable(poroImage);
        mainView.setImageDrawable(newDrawable);
        if (newDrawable instanceof AnimationDrawable) {
            AnimationDrawable frameAnimation = (AnimationDrawable) mainView.getDrawable();
            frameAnimation.setCallback(mainView);
            frameAnimation.setVisible(true, true);
            frameAnimation.start();
        }
    }

    // Setting customDialogBuilder which resets TimeUtils' SharedPreferences (resetPoro) and returns to StartActivity
    private void showCustomDialog() {
        AlertDialog.Builder customDialogBuilder = new AlertDialog.Builder(PoroActivity.this);
        View customDialogView = getLayoutInflater().inflate(R.layout.dialog_custom, null);
        Button dialogButton = (Button) customDialogView.findViewById(R.id.dialogButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeUtils.resetPoro(PoroActivity.this);
                Intent intent = new Intent(PoroActivity.this, StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        customDialogBuilder.setView(customDialogView);
        customDialogBuilder.setCancelable(false);
        final AlertDialog customDialog = customDialogBuilder.create();
        Window window = customDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        customDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poro);
        final Context context = this;
        getSupportActionBar().hide();

        mainView = (ImageView) findViewById(R.id.mainView);
        ImageView snax = (ImageView) findViewById(R.id.poroSnax);
        final ImageView heart = (ImageView) findViewById(R.id.heartView);

        // Skin choice from activity_start determines what will become STAGE_DEFAULT
        skin = getIntent().getExtras().getInt("skin");


        // Every time ImageView snax is clicked, Poro is fed and counter increments
        snax.setOnClickListener(new View.OnClickListener() {
            int counter = 0;

            // Every time counter increments by one, an animation of a happy Poro appears in mainView and current timeFed is updated in TimeUtils.fedPoro
            public void onClick(View view) {
                counter++;
                TimeUtils.fedPoro(context);

                //Easter Egg: if Poro is fed 10 snax before counter resets, Poro exploding animation will show in mainView then customDialog will execute
                if (counter > 9) {
                    heart.clearAnimation();
                    heart.setVisibility(View.GONE);
                    setPoroAnimation(getImageId(skin, age, STAGE_EXPLODED));
                    showCustomDialog();
                } else {
                    setPoroAnimation(getImageId(skin, age, STAGE_HAPPY));
                    heart.setVisibility(View.VISIBLE);
                    Animation heartAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                            R.anim.heart_animation);
                    heart.startAnimation(heartAnimation);

                    // Happy Poro animation appears in mainView for a 1.5 second delay with a heartAnimation in heartView, and then changes back to the STAGE_DEFAULT
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (counter <= 9) {
                                setPoroAnimation(getImageId(skin, age, STAGE_DEFAULT));
                            }
                        }
                    }, 1500);
                }
            }
        });
    }

    // Array of frames/xml animations for BABY, TEEN, ADULT, BRAUM BABY, BRAUM TEEN, and BRAUM ADULT
    private int getImageId(int skin, int age, int stage) {
         // stage = STAGE_SAD;  // TODO: for stage test purposes
        if (imageArray == null) {
            imageArray = new int[3][3][5];
            imageArray[SKIN][AGE_BABY][STAGE_DEFAULT] = R.drawable.baby_poro;
            imageArray[SKIN][AGE_BABY][STAGE_HAPPY] = R.drawable.baby_happy;
            imageArray[SKIN][AGE_BABY][STAGE_SAD] = R.drawable.baby_sad;
            imageArray[SKIN][AGE_BABY][STAGE_DEAD] = R.drawable.baby_dead;
            imageArray[SKIN][AGE_BABY][STAGE_EXPLODED] = R.drawable.exploded_baby_poro;

            imageArray[SKIN][AGE_TEEN][STAGE_DEFAULT] = R.drawable.teen_poro;
            imageArray[SKIN][AGE_TEEN][STAGE_HAPPY] = R.drawable.teen_happy;
            imageArray[SKIN][AGE_TEEN][STAGE_SAD] = R.drawable.teen_sad;
            imageArray[SKIN][AGE_TEEN][STAGE_DEAD] = R.drawable.teen_dead;
            imageArray[SKIN][AGE_TEEN][STAGE_EXPLODED] = R.drawable.exploded_teen_poro;

            imageArray[SKIN][AGE_ADULT][STAGE_DEFAULT] = R.drawable.adult_poro;
            imageArray[SKIN][AGE_ADULT][STAGE_HAPPY] = R.drawable.adult_happy;
            imageArray[SKIN][AGE_ADULT][STAGE_SAD] = R.drawable.adult_sad;
            imageArray[SKIN][AGE_ADULT][STAGE_DEAD] = R.drawable.adult_dead;
            imageArray[SKIN][AGE_ADULT][STAGE_EXPLODED] = R.drawable.exploded_adult_poro;

            imageArray[SKIN_BRAUM][AGE_BABY][STAGE_DEFAULT] = R.drawable.braum_baby_poro;
            imageArray[SKIN_BRAUM][AGE_BABY][STAGE_HAPPY] = R.drawable.braum_baby_happy;
            imageArray[SKIN_BRAUM][AGE_BABY][STAGE_SAD] = R.drawable.braum_baby_sad;
            imageArray[SKIN_BRAUM][AGE_BABY][STAGE_DEAD] = R.drawable.braum_baby_dead;
            imageArray[SKIN_BRAUM][AGE_BABY][STAGE_EXPLODED] = R.drawable.braum_exploded_baby_poro;

            imageArray[SKIN_BRAUM][AGE_TEEN][STAGE_DEFAULT] = R.drawable.braum_teen_poro;
            imageArray[SKIN_BRAUM][AGE_TEEN][STAGE_HAPPY] = R.drawable.braum_teen_happy;
            imageArray[SKIN_BRAUM][AGE_TEEN][STAGE_SAD] = R.drawable.braum_teen_sad;
            imageArray[SKIN_BRAUM][AGE_TEEN][STAGE_DEAD] = R.drawable.braum_teen_dead;
            imageArray[SKIN_BRAUM][AGE_TEEN][STAGE_EXPLODED] = R.drawable.braum_exploded_teen_poro;

            imageArray[SKIN_BRAUM][AGE_ADULT][STAGE_DEFAULT] = R.drawable.braum_adult_poro;
            imageArray[SKIN_BRAUM][AGE_ADULT][STAGE_HAPPY] = R.drawable.braum_adult_happy;
            imageArray[SKIN_BRAUM][AGE_ADULT][STAGE_SAD] = R.drawable.braum_adult_sad;
            imageArray[SKIN_BRAUM][AGE_ADULT][STAGE_DEAD] = R.drawable.braum_adult_dead;
            imageArray[SKIN_BRAUM][AGE_ADULT][STAGE_EXPLODED] = R.drawable.braum_exploded_adult_poro;
        }

        return imageArray[skin][age][stage];
    }

    @Override
    protected void onResume() {
        super.onResume();
        long timeUtilsAge = TimeUtils.timeSinceOpened(this);

        // Aging animations
        if (timeUtilsAge < 1000 * 60 * 60 * 24 * 2) {
            age = AGE_BABY;
            setPoroAnimation(getImageId(skin, age, STAGE_DEFAULT));
        } else if (timeUtilsAge < 1000 * 60 * 60 * 24 * 5) {
            age = AGE_TEEN;
            setPoroAnimation(getImageId(skin, age, STAGE_DEFAULT));
        } else if (timeUtilsAge < 1000 * 60 * 60 * 24 * 9) {
            age = AGE_ADULT;
            setPoroAnimation(getImageId(skin, age, STAGE_DEFAULT));
        } else {
            age = AGE_ADULT;
            setPoroAnimation(getImageId(skin, age, STAGE_DEAD));
            showCustomDialog();
        }

         // age = AGE_ADULT; // TODO: for aging test purposes

        final long lastFed = TimeUtils.timeSinceLastFed(this);

        // If Poro hasn't been fed for 2 days
        if (lastFed > 1000 * 60 * 60 * 24 * 2 && TimeUtils.timeSinceOpened(this) > 1000 * 60 * 60 * 24 * 2) {
            setPoroAnimation(getImageId(skin, age, STAGE_DEAD));
            showCustomDialog();
        }
        // If Poro hasn't been fed for 8+ hours, static image of sad Poro appears in mainView
        else if (lastFed > 1000 * 60 * 60 * 8 && TimeUtils.timeSinceOpened(this) > 1000 * 60 * 60 * 8 ) {
            setPoroAnimation(getImageId(skin, age, STAGE_SAD));
        }

    }
}

