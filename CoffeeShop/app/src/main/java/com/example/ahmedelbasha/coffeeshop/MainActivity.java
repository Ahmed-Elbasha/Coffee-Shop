package com.example.ahmedelbasha.coffeeshop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int quantity = 1;

    Toast toastMessage;
    Context context = MainActivity.this;
    int toastDuration = Toast.LENGTH_SHORT;
    CharSequence message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayQuantity(quantity);

        Button submitOrderButton = findViewById(R.id.order_button);

        submitOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox addWhippedCreamCheckBox = findViewById(R.id.add_whipped_cream_check_box);
                boolean addWhippedCream = addWhippedCreamCheckBox.isChecked();

                CheckBox addChocolateCheckBox = findViewById(R.id.add_chocolate_check_box);
                boolean addChocolate = addChocolateCheckBox.isChecked();

                EditText customerNameEditText = findViewById(R.id.customer_name_user_input);
                String customerName = customerNameEditText.getText().toString();

                int price = calculatePrice(addWhippedCream, addChocolate);

                String[] emailAddresses = {"ahmed.elbasha@outlook.com"};

                String subject = "JustJava order for " + customerName;

                sendOrderSummaryViaEmailIntent(emailAddresses, subject, createOrderSummary(price, addWhippedCream, addChocolate, customerName));
            }
        });

        Button incrementButton = findViewById(R.id.increment_button);

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity < 100) {
                    quantity = quantity + 1;
                    displayQuantity(quantity);
                } else {
                    message = "You cannot have more than 100 cups of coffee";
                    toastMessage = Toast.makeText(context, message, toastDuration);
                    toastMessage.show();
                    return;
                }
            }
        });

        Button decrementButton = findViewById(R.id.decrement_button);

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity = quantity - 1;
                    displayQuantity(quantity);
                } else {
                    message = "You cannot have less than 1 cup of coffee";
                    toastMessage = Toast.makeText(context, message, toastDuration);
                    toastMessage.show();
                    return;
                }
            }
        });
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCups) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCups);
    }

    /**
     * This method displays the given text on the screen.
     */

    /**
     * Calculates the price of the order.
     *
     */
    private int calculatePrice(boolean hasWhippedCream, boolean hasChocolate) {
        int pricePerCup = 5;
        int totalPrice = 0;

        if (hasWhippedCream && hasChocolate) {
            pricePerCup = 5 + 1 + 2;
            totalPrice = pricePerCup * quantity;
            return totalPrice;
        } else if (hasWhippedCream) {
            pricePerCup = 5 + 1;
            totalPrice = pricePerCup * quantity;
            return  totalPrice;
        } else if (hasChocolate) {
            pricePerCup = 5 + 2;
            totalPrice = pricePerCup * quantity;
            return  totalPrice;
        } else {
            totalPrice = pricePerCup * quantity;
            return  totalPrice;
        }
    }

    /**
     * Create summary of the order.
     *
     * @param addWhippedCream is whether or not the user wants whipped cream topping
     * @param addChocolate is whether or not the user wants chocolate topping
     * @param totalPrice of the order
     * @return text summary
     */
    private String createOrderSummary(int totalPrice, Boolean addWhippedCream, Boolean addChocolate, String customerName) {
        String orderSummary = "Name: " + customerName + "\n";
        orderSummary += "Add whipped cream? " + addWhippedCream + "\n";
        orderSummary += "Add chocolate? " + addChocolate + "\n";
        orderSummary += "Quantity: " + quantity + "\n";
        orderSummary += "Total: $" + totalPrice + "\n";
        orderSummary += "Thank you!";
        return orderSummary;
    }

    private void sendOrderSummaryViaEmailIntent(String[] emailAddress, String subject, String orderSummary) {
        Intent sendEmailIntent = new Intent(Intent.ACTION_SENDTO);
        sendEmailIntent.setData(Uri.parse("mailto:"));

        sendEmailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
        sendEmailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        sendEmailIntent.putExtra(Intent.EXTRA_TEXT, orderSummary);

        if (sendEmailIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(sendEmailIntent);
        } else {
            message = "Order submission process failed";
            toastMessage = Toast.makeText(context, message, toastDuration);
            toastMessage.show();
            return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        message = "Order submission process completed";
        toastMessage = Toast.makeText(context, message, toastDuration);
        toastMessage.show();
    }
}
