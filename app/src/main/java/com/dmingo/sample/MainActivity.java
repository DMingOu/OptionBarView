package com.dmingo.sample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.dmingo.R;
import com.dmingo.optionbarview.OptionBarView;


public class MainActivity extends AppCompatActivity {

    OptionBarView opv1;
    OptionBarView opv2;

    OptionBarView opvSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        opv1 =findViewById(R.id.opv_1);
        opv1.setSplitMode(true);
        opv1.setOnOptionItemClickListener(new OptionBarView.OnOptionItemClickListener() {
            @Override
            public void leftOnClick() {
                Toast.makeText(MainActivity.this,"Left Click",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void centerOnClick() {
                Toast.makeText(MainActivity.this,"Center Click",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void rightOnClick() {
                Toast.makeText(MainActivity.this,"Right Click",Toast.LENGTH_SHORT).show();
            }
        });

        opv2 = findViewById(R.id.opv_2);
        opv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"OptionBarView Click",Toast.LENGTH_LONG).show();
            }
        });
        opv2.setRightText("hadhadasdas");

        opvSwitch = findViewById(R.id.opv_switch);
        opvSwitch.setSplitMode(true);
        opvSwitch.setOnSwitchCheckedChangeListener(new OptionBarView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(OptionBarView view, boolean isChecked) {
                Toast.makeText(MainActivity.this,"Switch是否被打开："+isChecked,Toast.LENGTH_SHORT).show();
            }
        });
    }
}