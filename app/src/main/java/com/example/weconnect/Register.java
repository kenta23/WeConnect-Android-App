package com.example.weconnect;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class Register extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText confirmPass;

    private RadioGroup radioGroupRegisterGender;

    private RadioButton radioButtonRegisteredGenderSelected;
    private Button signup;

    private FirebaseAuth auth;
    private CheckBox checkBox;
    private static final String TAG = "Register";
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    DatabaseReference reference;
    FirebaseDatabase db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initDatePicker();
        dateButton = findViewById(R.id.dateButton);
        dateButton.setText(getTodaysDate());

        // editText
        firstName = findViewById(R.id.editFirstName);
        lastName = findViewById(R.id.editLastName);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editPassword);
        confirmPass = findViewById(R.id.editConfirmPassword);

        // Button
        signup = findViewById(R.id.btnSignup);

        // RadioButton for Gender
        radioGroupRegisterGender = findViewById(R.id.radioGroup);
        radioGroupRegisterGender.clearCheck();

        // Checkbox
        checkBox = findViewById(R.id.chkAgreement);
        auth = FirebaseAuth.getInstance();

        signup.setOnClickListener(view -> {

            int selectedGenderId = radioGroupRegisterGender.getCheckedRadioButtonId();
            radioButtonRegisteredGenderSelected = findViewById(selectedGenderId);

            // Obtain the entered data
            String textFirstName = firstName.getText().toString();
            String textLastName = lastName.getText().toString();
            String textEmail = email.getText().toString();
            String textBirthDate = dateButton.getText().toString();
            String textPassword = password.getText().toString();
            String textConfirmPassword = confirmPass.getText().toString();
            String textGender;

            // If field data is empty
            if (TextUtils.isEmpty(textFirstName)) {
                Toast.makeText(Register.this, "Please enter your first name", Toast.LENGTH_LONG).show();
                firstName.setError("First name is required");
                firstName.requestFocus();
            } else if (TextUtils.isEmpty(textLastName)) {
                Toast.makeText(Register.this, "Please enter your last name", Toast.LENGTH_LONG).show();
                lastName.setError("Last name is required");
                lastName.requestFocus();
            } else if (TextUtils.isEmpty(textEmail)) {
                Toast.makeText(Register.this, "Please enter your email address", Toast.LENGTH_LONG).show();
                email.setError("Email is required");
                email.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                Toast.makeText(Register.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
                email.setError("Invalid email address");
                email.requestFocus();
            } else if (radioGroupRegisterGender.getCheckedRadioButtonId() == -1) {
                Toast.makeText(Register.this, "Please select your gender", Toast.LENGTH_LONG).show();
                radioButtonRegisteredGenderSelected.setError("Gender is required");
                radioButtonRegisteredGenderSelected.requestFocus();
            } else if (TextUtils.isEmpty(textPassword)) {
                Toast.makeText(Register.this, "Please enter your password", Toast.LENGTH_LONG).show();
                password.setError("Password is required");
                password.requestFocus();
            } else if (textPassword.length() < 8) {
                Toast.makeText(Register.this, "Password should be at least 8 characters long", Toast.LENGTH_LONG).show();
                password.setError("Password is too short");
                password.requestFocus();
            } else if (TextUtils.isEmpty(textConfirmPassword)) {
                Toast.makeText(Register.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                confirmPass.setError("Password confirmation is required");
                confirmPass.requestFocus();
            } else if (!textPassword.equals(textConfirmPassword)) {
                Toast.makeText(Register.this, "Password confirmation doesn't match", Toast.LENGTH_LONG).show();
                confirmPass.setError("Password confirmation is required");
                confirmPass.requestFocus();
                password.setText("");
                confirmPass.setText("");
            } else if (!checkBox.isChecked()) {
                Toast.makeText(Register.this, "Please check the box to proceed", Toast.LENGTH_LONG).show();
                checkBox.setError("Checkbox is required");
                checkBox.requestFocus();
            } else {
                textGender = radioButtonRegisteredGenderSelected.getText().toString();
                registerUser(textFirstName, textLastName, textEmail, textBirthDate, textGender, textPassword);
            }
        });
    }

    private void registerUser(String textFirstName, String textLastName, String textEmail, String textBirthDate, String textGender, String textPassword) {
        // Hash password
        String hashedPassword = hashPassword(textPassword);

        // Create user profile
        auth.createUserWithEmailAndPassword(textEmail, hashedPassword).addOnCompleteListener(Register.this, task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();

                // Enter data into Firebase
                Users users = new Users(textFirstName, textLastName, textEmail, textBirthDate, textGender, hashedPassword);

                // Extracting user for "Registered User" from database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference(auth.getUid());

                reference.child(firebaseUser.getUid()).setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            // Send verification email
                            // firebaseUser.sendEmailVerification();

                            // Open login page if successful
                            Intent intent = new Intent(Register.this, Profile.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(Register.this, "Registration failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    email.setError("Your email is invalid or already in use. Please use another email");
                    email.requestFocus();
                } catch (FirebaseAuthUserCollisionException e) {
                    email.setError("User is already registered with this email");
                    email.requestFocus();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
        return null;
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        return makeDateString(day, month, year);
    }

    // Date Button
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        switch (month) {
            case 1:
                return "JAN";
            case 2:
                return "FEB";
            case 3:
                return "MAR";
            case 4:
                return "APR";
            case 5:
                return "MAY";
            case 6:
                return "JUN";
            case 7:
                return "JUL";
            case 8:
                return "AUG";
            case 9:
                return "SEPT";
            case 10:
                return "OCT";
            case 11:
                return "NOV";
            case 12:
                return "DEC";
        }
        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}
