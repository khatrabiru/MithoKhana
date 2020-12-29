package com.khatribiru.mithokhana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esewa.android.sdk.payment.ESewaConfiguration;
import com.esewa.android.sdk.payment.ESewaPayment;
import com.esewa.android.sdk.payment.ESewaPaymentActivity;
import com.khatribiru.mithokhana.Database.Database;

public class EsewaPayment extends AppCompatActivity {

    private static final String CONFIG_ENVIRONMENT = ESewaConfiguration.ENVIRONMENT_TEST;
    private static final int REQUEST_CODE_PAYMENT = 1;
    private ESewaConfiguration eSewaConfiguration;

    private static final String MERCHANT_ID = "JB0BBQ4aD0UqIThFJwAKBgAXEUkEGQUBBAwdOgABHD4DChwUAB0R";
    private static final String MERCHANT_SECRET_KEY = "BhwIWQQADhIYSxILExMcAgFXFhcOBwAKBgAXEQ==";
    String amount = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eSewaConfiguration = new ESewaConfiguration()
                .clientId(MERCHANT_ID)
                .secretKey(MERCHANT_SECRET_KEY)
                .environment(CONFIG_ENVIRONMENT);

        if( getIntent() != null ) {
            amount = getIntent().getStringExtra("amount");
        }

        if( !amount.isEmpty() && amount != null ) {
            makePayment(amount);
        }
    }

    private void makePayment(String amount) {
        ESewaPayment eSewaPayment = new ESewaPayment(amount, "someProductName", "someUniqueId_" + System.nanoTime(), "https://somecallbackurl.com");
        Intent intent = new Intent(EsewaPayment.this, ESewaPaymentActivity.class);
        intent.putExtra(ESewaConfiguration.ESEWA_CONFIGURATION, eSewaConfiguration);
        intent.putExtra(ESewaPayment.ESEWA_PAYMENT, eSewaPayment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {

                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Log.i("Proof of Payment", s);
                Toast.makeText(this, "SUCCESSFUL PAYMENT", Toast.LENGTH_SHORT).show();

                // Delete cart
                new Database(getBaseContext()).cleanCart();
                Intent intent = new Intent(EsewaPayment.this, Home.class);
                startActivity(intent);

            } else if (resultCode == RESULT_CANCELED) {

                Toast.makeText(this, "Canceled By User", Toast.LENGTH_SHORT).show();

            } else if (resultCode == ESewaPayment.RESULT_EXTRAS_INVALID) {

                String s = data.getStringExtra(ESewaPayment.EXTRA_RESULT_MESSAGE);
                Log.i("Proof of Payment", s);

            }
        }
    }

}