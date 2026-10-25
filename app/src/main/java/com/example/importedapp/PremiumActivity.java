package com.example.premiumdownloader.premium;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Billing UI placeholder. Before release, implement Google Play Billing purchase flow,
 * server-side purchase verification, acknowledge purchases, restore purchases, and entitlement checks.
 */
public class PremiumActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView text = new TextView(this);
        text.setPadding(24, 24, 24, 24);
        text.setText("Premium plan\\n\\nMonthly / Yearly subscription\\n\\nBilling setup required before taking payments.");
        text.setTextSize(18);
        setContentView(text);
    }
}
