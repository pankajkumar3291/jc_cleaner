package android.com.provider.activities;
import android.com.provider15_nov_2018.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import am.appwise.components.ni.NoInternetDialog;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivityTermsConditions extends AppCompatActivity{

    TextView tvDescription,tvCheckOne,tvCheckTwo,tvCheckThree,tvCheckFour;
    CheckBox checkBoxOne, checkBoxTwo,checkBoxThree,checkBoxFour;
    ImageView backarr;
    private NoInternetDialog noInternetDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_conditions);
        noInternetDialog = new NoInternetDialog.Builder(this).build();
        getWindow().setStatusBarColor(ContextCompat.getColor(ActivityTermsConditions.this, R.color.statusBarColor));
        findingIdsHere();
        termsAndConditionsCases();
        dataCombinationPolicyCases();
        phoneNumberVerificationCases();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
    @Override
    protected void attachBaseContext(Context newBase){
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private void phoneNumberVerificationCases() {
        String text = "Phone number verification, this will always result in data";
        // initialize a new ClickableSpan
        ClickableSpan clickableSpan = new ClickableSpan(){
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityTermsConditions.this, "Phone number verification,", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void updateDrawState(TextPaint ds){
                super.updateDrawState(ds);
                int color = ContextCompat.getColor(ActivityTermsConditions.this, R.color.relativeBottomColor);
                ds.setColor(color);
                ds.setUnderlineText(false);
            }
        };
        // initialize a new SpannableStringBuilder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);


        // apply the clickable text to the span
        ssBuilder.setSpan(
                clickableSpan, // span to add
                text.indexOf("Phone number verification,"), // start of the span (inclusive)
                text.indexOf("Phone number verification,") + String.valueOf("Phone number verification,").length(), // end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // do not extend the span when text add later
        );

        // bold
        ssBuilder.setSpan(
                new StyleSpan(Typeface.BOLD),
                text.indexOf("Phone number verification,"),
                text.indexOf("Phone number verification,") + String.valueOf("Phone number verification,").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        // underline
        ssBuilder.setSpan(
                new UnderlineSpan(),
                text.indexOf("Phone number verification,"),
                text.indexOf("Phone number verification,") + String.valueOf("Phone number verification,").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        tvCheckFour.setText(ssBuilder);
// this step is mandated for the url and clickable styles
        tvCheckFour.setMovementMethod(LinkMovementMethod.getInstance());
        tvCheckFour.setHighlightColor(Color.TRANSPARENT);
    }

    private void dataCombinationPolicyCases() {
        String text = "Data combination policy, i wish to receive all customized services.";

        // initialize a new ClickableSpan
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityTermsConditions.this, "Data combination policy,", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                int color = ContextCompat.getColor(ActivityTermsConditions.this, R.color.relativeBottomColor);
                ds.setColor(color);
                ds.setUnderlineText(false);
            }
        };
        // initialize a new SpannableStringBuilder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);
        // apply the clickable text to the span
        ssBuilder.setSpan(
                clickableSpan, // span to add
                text.indexOf("Data combination policy,"), // start of the span (inclusive)
                text.indexOf("Data combination policy,") + String.valueOf("Data combination policy,").length(), // end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // do not extend the span when text add later
        );
        // bold
        ssBuilder.setSpan(
                new StyleSpan(Typeface.BOLD),
                text.indexOf("Data combination policy,"),
                text.indexOf("Data combination policy,") + String.valueOf("Data combination policy,").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        // underline
        ssBuilder.setSpan(
                new UnderlineSpan(),
                text.indexOf("Data combination policy,"),
                text.indexOf("Data combination policy,") + String.valueOf("Data combination policy,").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        tvCheckThree.setText(ssBuilder);
// this step is mandated for the url and clickable styles
        tvCheckThree.setMovementMethod(LinkMovementMethod.getInstance());
        tvCheckThree.setHighlightColor(Color.TRANSPARENT);
    }

    private void termsAndConditionsCases() {
        String text = "Terms and Conditions and Special terms";
        // initialize a new ClickableSpan
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityTermsConditions.this, "Terms and Conditions", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                int color = ContextCompat.getColor(ActivityTermsConditions.this, R.color.relativeBottomColor);
                ds.setColor(color);
                ds.setUnderlineText(false);
            }
        };
        // initialize a new ClickableSpan
        ClickableSpan clickableSpan1 = new ClickableSpan(){
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityTermsConditions.this, "Special terms", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds){
                super.updateDrawState(ds);
                int color = ContextCompat.getColor(ActivityTermsConditions.this, R.color.relativeBottomColor);
                ds.setColor(color);
                ds.setUnderlineText(false);
            }
        };


        // initialize a new SpannableStringBuilder instance
        SpannableStringBuilder ssBuilder = new SpannableStringBuilder(text);


        // apply the clickable text to the span
        ssBuilder.setSpan(
                clickableSpan, // span to add
                text.indexOf("Terms and Conditions"), // start of the span (inclusive)
                text.indexOf("Terms and Conditions") + String.valueOf("Terms and Conditions").length(), // end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // do not extend the span when text add later
        );


        ssBuilder.setSpan(
                clickableSpan1, // span to add
                text.indexOf("Special terms"), // start of the span (inclusive)
                text.indexOf("Special terms") + String.valueOf("Special terms").length(), // end of the span (exclusive)
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE // do not extend the span when text add later
        );


        // underline
        ssBuilder.setSpan(
                new UnderlineSpan(),
                text.indexOf("Terms and Conditions"),
                text.indexOf("Terms and Conditions") + String.valueOf("Terms and Conditions").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        // underline
        ssBuilder.setSpan(
                new UnderlineSpan(),
                text.indexOf("Special terms"),
                text.indexOf("Special terms") + String.valueOf("Special terms").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        // bold
        ssBuilder.setSpan(
                new StyleSpan(Typeface.BOLD),
                text.indexOf("Terms and Conditions"),
                text.indexOf("Terms and Conditions") + String.valueOf("Terms and Conditions").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        // bold
        ssBuilder.setSpan(
                new StyleSpan(Typeface.BOLD),
                text.indexOf("Special terms"),
                text.indexOf("Special terms") + String.valueOf("Special terms").length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );


        tvCheckOne.setText(ssBuilder);
// this step is mandated for the url and clickable styles
        tvCheckOne.setMovementMethod(LinkMovementMethod.getInstance());
        tvCheckOne.setHighlightColor(Color.TRANSPARENT);

    }

    private void findingIdsHere() {


        backarr = findViewById(R.id.backarr);

        backarr.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        tvDescription = findViewById(R.id.tvDescription);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());


        tvCheckOne = findViewById(R.id.tvCheckOne);
        tvCheckTwo = findViewById(R.id.tvCheckTwo);
        tvCheckThree = findViewById(R.id.tvCheckThree);
        tvCheckFour = findViewById(R.id.tvCheckFour);


        checkBoxOne = findViewById(R.id.checkBoxOne);
        checkBoxTwo = findViewById(R.id.checkBoxTwo);

        checkBoxTwo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    checkBoxOne.setChecked(true);
                    checkBoxThree.setChecked(true);
                    checkBoxFour.setChecked(true);

                }
                else
                {

                    checkBoxOne.setChecked(false);
                    checkBoxThree.setChecked(false);
                    checkBoxFour.setChecked(false);
                }
            }
        });

        checkBoxThree = findViewById(R.id.checkBoxThree);
        checkBoxFour = findViewById(R.id.checkBoxFour);


//        // 1
//        checkBoxOne.setText("");
//        tvCheckOne.setText(Html.fromHtml("I have read and agree to the " +
//                "<a href='id.web.freelancer.example.TCActivity://Kode'>TERMS AND CONDITIONS</a>"));
//        tvCheckOne.setClickable(true);
//        tvCheckOne.setMovementMethod(LinkMovementMethod.getInstance());
//
//
//        //2
//
//        checkBoxTwo.setText("");
//        tvCheckTwo.setText(Html.fromHtml("I have read and agree to the " +
//                "<a href='id.web.freelancer.example.TCActivity://Kode'>TERMS AND CONDITIONS</a>"));
//        tvCheckTwo.setClickable(true);
//        tvCheckTwo.setMovementMethod(LinkMovementMethod.getInstance());
//
//
//        //3
//        checkBoxThree.setText("");
//        tvCheckThree.setText(Html.fromHtml("I have read and agree to the " +
//                "<a href='id.web.freelancer.example.TCActivity://Kode'>TERMS AND CONDITIONS</a>"));
//        tvCheckThree.setClickable(true);
//        tvCheckThree.setMovementMethod(LinkMovementMethod.getInstance());
//
//
//        //4
//
//        checkBoxFour.setText("");
//        tvCheckFour.setText(Html.fromHtml("I have read and agree to the " +
//                "<a href='id.web.freelancer.example.TCActivity://Kode'>TERMS AND CONDITIONS</a>"));
//        tvCheckFour.setClickable(true);
//        tvCheckFour.setMovementMethod(LinkMovementMethod.getInstance());


//        tvCheckOne.setPaintFlags(tvCheckOne.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        SpannableString content = new SpannableString("Terms and Conditions And Special Terms");
//        content.setSpan(new UnderlineSpan(), 0, 20, 0);
//        tvCheckOne.setText(content);


//        String text = "@Terms @and @Conditions And @Special @Terms";
//
//        SpannableString spanString = new SpannableString(text);
//        Matcher matcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
//
//        while (matcher.find()) {
//            spanString.setSpan(new ForegroundColorSpan(Color.parseColor("#0000FF")), matcher.start(), matcher.end(), 0); //to highlight word havgin '@'
//            final String tag = matcher.group(0);
//            ClickableSpan clickableSpan = new ClickableSpan() {
//                @Override
//                public void onClick(View textView) {
//                    Log.e("click", "click " + tag);
//                    String searchText = tag.replaceAll("\\\\\\W+", ""); //replace '@' with blank character to search on google.
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.in/search?q=" + searchText));
//                    startActivity(browserIntent);
//                }
//
//                @Override
//                public void updateDrawState(TextPaint ds) {
//                    super.updateDrawState(ds);
//
//                }
//            };
//            spanString.setSpan(clickableSpan, matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//        tvCheckOne.setText(spanString);
//        tvCheckOne.setMovementMethod(LinkMovementMethod.getInstance());


    }


}
