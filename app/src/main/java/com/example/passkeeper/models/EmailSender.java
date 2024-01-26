package com.example.passkeeper.models;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class EmailSender {

    private static final String API_URL = "http://192.168.1.11/usable_security_APIs/email_ver.py";

    public static void sendEmail(String email, String subject, String body, Context context) {
        // Append parameters to the URL using Uri.Builder
        Uri.Builder builder = Uri.parse(API_URL).buildUpon();

        // String data = String.format("receiver_email=%s&subject=%s&message=%s", encodedEmail, encodedSubject, encodedBody);

        // Add parameters to the builder
        builder.appendQueryParameter("receiver_email", email);
        builder.appendQueryParameter("subject", subject);
        builder.appendQueryParameter("message", body);


        // Build the complete URL
        String finalUrl = builder.build().toString();

        Log.i("Uri res", finalUrl);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, finalUrl,
                response -> {
                    // Handle the response
                    if (response.contains("sent"))
                        Toast.makeText(context, "Email sent", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(context, "Email not sent, please try again", Toast.LENGTH_SHORT).show();
                },
                error -> Toast.makeText(context, "Email not sent, please try again", Toast.LENGTH_SHORT).show());

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
