package com.example.campuscourier.supplier;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.campuscourier.R;
import com.example.campuscourier.requestor.Home;
import com.example.campuscourier.shared.CheckCompleted;
import com.example.campuscourier.shared.FirebaseHelper;
import com.example.campuscourier.shared.Report;
import com.example.campuscourier.shared.Requests;
import com.example.campuscourier.shared.Users;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Objects;

public class RequestDetailsForSupplierCancel extends AppCompatActivity {
    Requests r;
    TextView requestorTelegram, itemName, itemDescription, expiryDate, expiryTime, location, urgency;
    ImageView image;
    Button buttonBackToHome, buttonCancelRequest, buttonReport, buttonCompleted;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    static String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
    CheckCompleted checkCompleted = new CheckCompleted();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details_for_supplier_cancel);
        requestorTelegram = findViewById(R.id.requestorTelegram);
        itemName = findViewById(R.id.itemName);
        itemDescription = findViewById(R.id.itemDescription);
        image = findViewById(R.id.postImage);
        expiryDate = findViewById(R.id.expiryDate);
        expiryTime = findViewById(R.id.expiryTime);
        location = findViewById(R.id.location);
        urgency = findViewById(R.id.urgency);
        buttonBackToHome = findViewById(R.id.buttonBackToHome);
        buttonCancelRequest = findViewById(R.id.buttonCancelRequest);
        buttonCompleted = findViewById(R.id.buttonCompleted);
        buttonReport = findViewById(R.id.buttonReport);

        if (getIntent().hasExtra(HomeSupplier.NEXT_SCREEN)) {
            // get the Serializable data model class with the details in it
            r = (Requests) getIntent().getSerializableExtra(HomeSupplier.NEXT_SCREEN);
        }
        if (r != null) {

            db.collection("users").document(r.getUserId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Users u = documentSnapshot.toObject(Users.class);
                    requestorTelegram.setText(u.getTelegram());
                }
            });

            itemName.setText(r.getItem());
            itemDescription.setText(r.getDescription());
            expiryDate.setText(r.getDate());
            expiryTime.setText(r.getTime());
            location.setText(r.getLocation());
            urgency.setText(r.getUrgency());
            if(!Objects.equals(r.getImageStorageUri(), "")){
                StorageReference ref = storage.getReferenceFromUrl(r.getImageStorageUri());
                final long MAX_BYTE = 2100 * 1600;
                ref.getBytes(MAX_BYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        image.setImageBitmap(bitmap);
                        Log.d("IMAGE", "image shown");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Log.d("IMAGE", "image not shown");
                    }});}
        }

        buttonCancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                r.setStatus("Not Accepted");
//                r.setSupplierId("");

                db.collection("users").document(r.getUserId()).collection("posts").document(r.getDocId()).update("status", "Not Accepted");
                db.collection("posts").document(r.getDocId()).update("status", "Not Accepted");

                db.collection("users").document(r.getUserId()).collection("posts").document(r.getDocId()).update("supplierId", "");
                db.collection("posts").document(r.getDocId()).update("supplierId", "");

                FirebaseHelper.removePostFromTodoCollection(r);
                Intent intent = new Intent(getApplicationContext(), HomeSupplier.class);
                startActivity(intent);
                finish();
            }
        });

        buttonBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeSupplier.class);
                startActivity(intent);
                finish();
            }
        });

        buttonReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Report.class);
                startActivity(intent);
                finish();
            }
        });

        buttonCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("Delivering".equals(r.getStatus())) {
                    new AlertDialog.Builder(RequestDetailsForSupplierCancel.this)
                            .setTitle("Confirmation")
                            .setMessage("You have marked the request as delivered. Please wait for the requestor to confirm the delivery.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    r.setSupplierDone();
                                    db.collection("users").document(r.getUserId()).collection("posts").document(r.getDocId()).update("SupplierDone", true);
                                    db.collection("users").document(r.getSupplierId()).collection("todo").document(r.getDocId()).update("SupplierDone", true);
                                    db.collection("posts").document(r.getDocId()).update("SupplierDone", true);
                                    checkCompleted.checkAndUpdatePostStatus(r);
                                    Log.d("CheckCompleted", "isSupplierDone: " + String.valueOf(r.isSupplierDone()));
                                    Intent intent = new Intent(getApplicationContext(), HomeSupplier.class);
                                    startActivity(intent);
                                    finish();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setCancelable(false)
                            .show();
                } else {
                    Toast.makeText(RequestDetailsForSupplierCancel.this, "Please deliver item first", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}