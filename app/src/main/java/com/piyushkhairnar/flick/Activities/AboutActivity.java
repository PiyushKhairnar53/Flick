package com.piyushkhairnar.flick.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.piyushkhairnar.flick.R;

import static com.piyushkhairnar.flick.R.drawable.explore_background;

public class AboutActivity extends AppCompatActivity {

    private ImageButton shareImageButton;
    private ImageButton rateUsImageButton;
    private ImageButton feedbackImageButton;

    private TextView versionNumberTextView;
    private TextView hitPro;

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        btnBack = (ImageView)findViewById(R.id.about_acti_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        shareImageButton = (ImageButton) findViewById(R.id.image_button_share_about);
        rateUsImageButton = (ImageButton) findViewById(R.id.image_button_rate_us_about);
        feedbackImageButton = (ImageButton) findViewById(R.id.image_button_feedback_about);

        versionNumberTextView = (TextView) findViewById(R.id.text_view_version_number);
        hitPro = (TextView)findViewById(R.id.hitanshuPro);

        loadActivity();

    }

    private void loadActivity() {

        shareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                String packageName = getApplicationContext().getPackageName();
                Intent appShareIntent = new Intent(Intent.ACTION_SEND);
                appShareIntent.setType("text/plain");
                String extraText = "Hey! Check out this amazing app.\n";
                extraText += "  https://play.google.com/store/apps/details?id=" + packageName;
                appShareIntent.putExtra(Intent.EXTRA_TEXT, extraText);
                startActivity(appShareIntent);
            }
        });

        rateUsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                String packageName = getApplicationContext().getPackageName();
                String playStoreLink = "https://play.google.com/store/apps/details?id=" + packageName;
                Intent appRateUsIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreLink));
                startActivity(appRateUsIntent);
            }
        });

        feedbackImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                Intent feedbackIntent = new Intent(Intent.ACTION_SENDTO);
                feedbackIntent.setData(Uri.parse("mailto:"));
                feedbackIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"piyushkhairnar53@gmail.com"});
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback: " + getResources().getString(R.string.app_name));
                startActivity(feedbackIntent);
            }
        });

        hitPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                String githubLink = "https://medium.com/@hitanshudhawan";
                Intent githubIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(githubLink));
                startActivity(githubIntent);
            }
        });

        try {
            versionNumberTextView.setText((getPackageManager().getPackageInfo(getPackageName(), 0)).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
