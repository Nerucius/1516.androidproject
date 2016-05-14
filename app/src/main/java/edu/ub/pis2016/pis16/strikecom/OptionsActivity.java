package edu.ub.pis2016.pis16.strikecom;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import edu.ub.pis2016.pis16.strikecom.engine.framework.Screen;
import edu.ub.pis2016.pis16.strikecom.engine.opengl.GLGameFragment;

/**
 * Created by root on 13/05/16.
 */
public class OptionsActivity extends Activity {
    Activity Options;
    SeekBar seekBarMusic;
    SeekBar seekBarSound;
    TextView textViewMusic;
    TextView textViewSound;
    static int percentMusic=100;
    static int percentSound=100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Options= this;


        seekBarMusic = (SeekBar) findViewById(R.id.seekBarMusic);
        seekBarSound = (SeekBar) findViewById(R.id.seekBarSound);
        textViewMusic = (TextView) findViewById(R.id.percentMusic);
        textViewSound = (TextView) findViewById(R.id.percentSound);
        seekBarMusic.setProgress(percentMusic);
        seekBarSound.setProgress(percentSound);
        textViewMusic.setText(percentMusic + "%");
        textViewSound.setText(percentSound + "%");

        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int seekBarPercent = 0;

            public void onProgressChanged(SeekBar seekBarMusic, int percent, boolean fromUser) {
                seekBarPercent = percent;
            }

            public void onStartTrackingTouch(SeekBar seekBarMusic) {

            }

            public void onStopTrackingTouch(SeekBar seekBarMusic) {
                textViewMusic.setText(seekBarPercent + "%");
            }
        });
        seekBarSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int seekBarPercent=0;

            public void onProgressChanged(SeekBar seekBarMusic, int percent, boolean fromUser){
                seekBarPercent=percent;

            }

            public void onStartTrackingTouch(SeekBar seekBarMusic) {

            }

            public void onStopTrackingTouch(SeekBar seekBarMusic) {
                textViewSound.setText(seekBarPercent + "%");
            }
        });

        Button btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                percentMusic=seekBarMusic.getProgress();
                percentSound=seekBarSound.getProgress();/*
                Bundle bundle = new Bundle();
                bundle.putInt("percentMusic", percentMusic);
                StrikeComGLGame fragObj = new StrikeComGLGame();
                fragObj.newInstance(bundle);
                FragmentTransaction transaction= getFragmentManager().beginTransaction();
                transaction.replace(R.id.gameFragment, fragObj);
                transaction.commit();*/

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Options);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("percentMusic",percentMusic);
                editor.commit();

                finish();

                /*Intent changeToGame = new Intent(Options, MainMenuActivity.class);
                startActivity(changeToGame);*/
            }
        });

        Button btnExit = (Button) findViewById(R.id.btnExit2);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                /*Intent changeToGame = new Intent(Options, MainMenuActivity.class);
                startActivity(changeToGame);*/
            }
        });


    }

    public int getPercentMusic(){
        return percentMusic;
    }

    public int getPercentSound(){
        return percentSound;
    }
}
