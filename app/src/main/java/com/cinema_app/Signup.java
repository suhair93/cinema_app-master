package com.cinema_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cinema_app.models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    ImageView back;
    ProgressDialog dialog;
    Button  signup;

    FirebaseDatabase database;
    DatabaseReference ref;
    EditText email,password,return_password,phone,name ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup.this,Login.class);
                startActivity(i);
            }
        });


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ...");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);


        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        password = findViewById(R.id.password);
        return_password = findViewById(R.id.re_password);
        phone = findViewById(R.id.phone);


        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

// شروط عندما لا يدخل المستخدم الايميل او الباسورد
                if (TextUtils.isEmpty(email.getText().toString())) {
                    // الرسالة التى تظهر للمستخدم
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }else

                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;

                } else if (!isEmailValid(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter correct email address!", Toast.LENGTH_SHORT).show();


                }else


                if(!password.getText().toString().equals(return_password.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Password is not identical", Toast.LENGTH_SHORT).show();

                }else
                if(TextUtils.isEmpty(phone.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Enter phone!", Toast.LENGTH_SHORT).show();

                }else
                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Enter name!", Toast.LENGTH_SHORT).show();

                }else

                    // ظهور علامة التحميل
                    dialog.show();
// هذه الدالة خاصه بالبحث في الفيربيس للتأكد من ان الايميل الذي تم ادخاله غير موجود بالداتا بيس
                //user اسم الجدول الذي يتم تخزين فيه كل حسابات المسخدمين
                //id هو العمود الي يتم تخزين فيه الايميل
                // email وهو متغير الستلانج اللي خزنت فيه ليتم مقارنته بالاوبجكت الموجود
                Query fireQuery = ref.child("user").orderByChild("email").equalTo(email.getText().toString());
                fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // ازا غير موجود قم بتخزينه
                        if (dataSnapshot.getValue() == null) {


                            // اوبجكت من نوع يوزر لتخزين بs account alيانات الادمن الجديد
                            user user = new user();
                            user.setEmail(email.getText().toString());
                            user.setPassword(password.getText().toString());
                            user.setName(name.getText().toString());
                            user.setPhone(phone.getText().toString());
                            // حفظه ك اوبجكت في جدول اليوزر بالفيربيس
                            ref.child("user").push().setValue(user);

                            // رسالة عند الانتهاء
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                            // الانتقال اللي تسجيل الدخول
                            startActivity(new Intent(Signup.this, Login.class));
                            finish();
                        } else {
                            // عند عدم تحقق الشرط ووجود المستخدم تظهر هذه الرسالة
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Thiready exists", Toast.LENGTH_SHORT).show();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dialog.dismiss();
                        // رساله خطأ
                        Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_LONG).show();

                    }
                });


            }
        });


    }


    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
