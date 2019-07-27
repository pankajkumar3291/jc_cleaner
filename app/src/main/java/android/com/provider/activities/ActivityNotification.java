package android.com.provider.activities;

import android.com.provider15_nov_2018.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityNotification extends AppCompatActivity {


    private TextView tvNoti, tvNoti2, tvNoti3, tvNoti4, tvNoti5, tvNoti6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        tvNoti = findViewById(R.id.tvNoti);
        tvNoti2 = findViewById(R.id.tvNoti2);
        tvNoti3 = findViewById(R.id.tvNoti3);
        tvNoti4 = findViewById(R.id.tvNoti4);
        tvNoti5 = findViewById(R.id.tvNoti5);
        tvNoti6 = findViewById(R.id.tvNoti6);


        if (getIntent().hasExtra("title") && getIntent().hasExtra("Service") && getIntent().hasExtra("Zipcode") && getIntent().hasExtra("Date") && getIntent().hasExtra("Time")) {


            String data2 = getIntent().getStringExtra("title");
            String data3 = getIntent().getStringExtra("Service");
            String data4 = getIntent().getStringExtra("Zipcode");
            String data5 = getIntent().getStringExtra("Date");
            String data6 = getIntent().getStringExtra("Time");


            tvNoti2.setText(data2);
            tvNoti3.setText(data3);
            tvNoti4.setText(data4);
            tvNoti5.setText(data5);
            tvNoti6.setText(data6);


        } else {

            Toast.makeText(ActivityNotification.this, "No data ", Toast.LENGTH_SHORT).show();
        }


    }
}
