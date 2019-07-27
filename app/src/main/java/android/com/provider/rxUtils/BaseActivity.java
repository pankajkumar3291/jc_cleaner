package android.com.provider.rxUtils;

import android.app.AlertDialog;
import android.com.provider.activities.ActivitySignIn;
import android.com.provider.activities.ActivitySignUp;
import android.com.provider15_nov_2018.R;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity {


    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private TextView tvToastError, tvClose, tvToast;
    private TextView tvYes;


    public void showTheDialogMessageForError(String message) {

        LayoutInflater li = LayoutInflater.from(this);
        View dialogView = li.inflate(R.layout.dialog_show_for_message_error, null);


        findingLogoutDialodIdsHereForError(dialogView, message);


        alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder
                .setCancelable(false);
        alertDialog = alertDialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();


    }

    private void findingLogoutDialodIdsHereForError(View dialogView, String message) {


        tvToastError = dialogView.findViewById(R.id.tvToastError);
        tvClose = dialogView.findViewById(R.id.tvClose);

        tvToastError.setText(message);
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });

    }


//    public void showTheDialogMessageForOk(String message) {
//
//
//        LayoutInflater li = LayoutInflater.from(this);
//        View dialogView = li.inflate(R.layout.dialog_show_for_message_ok, null);
//
//
//        findingLogoutDialodIdsHere(dialogView, message);
//
//
//        alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setView(dialogView);
//        alertDialogBuilder
//                .setCancelable(false);
//        alertDialog = alertDialogBuilder.create();
//        Objects.requireNonNull(alertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.show();
//
//
//    }

    private void findingLogoutDialodIdsHere(View dialogView, String message) {

        tvToast = dialogView.findViewById(R.id.tvToast);
        tvYes = dialogView.findViewById(R.id.tvYes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                callTheDashboardActivity();
                alertDialog.dismiss();
            }
        });
        tvToast.setText(message);

    }

//    private void callTheDashboardActivity() {
//
//        Intent intent1 = new Intent(BaseActivity.this, ActivitySignIn.class);
////        intent1.putExtra("Uniqid", "ActivitySignUp");
//        startActivity(intent1);
//        finish();
//
//
//    }


}
