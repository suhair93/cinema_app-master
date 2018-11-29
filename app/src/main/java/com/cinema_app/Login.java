package com.cinema_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cinema_app.admin.MainActivityAdmin;
import com.cinema_app.customer.MainActivity;
import com.cinema_app.models.Keys;
import com.cinema_app.models.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    LinearLayout signup ;
    Button login;
    EditText email,password;
    String name = "", pass = "";
    ProgressDialog dialog;
    FirebaseDatabase database;
    DatabaseReference ref;
    List<user> userlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signup = findViewById(R.id.signup);
        email = findViewById(R.id.email);
        password =findViewById(R.id.password);
        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("waiting ....");
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);



        database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Signup.class);

                startActivity(i);
            }
        });

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (TextUtils.isEmpty(email.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                    dialog.show();
                final SharedPreferences.Editor editor = getSharedPreferences(Keys.KEY_ID, MODE_PRIVATE).edit();

                if(email.getText().toString().equals("admin")&&password.getText().toString().equals("123")){
                    editor.putString(Keys.KEY_CUSTOMER,"");
                    editor.apply();
                    Intent i = new Intent(Login.this,MainActivityAdmin.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    finish();
                    startActivity(i);
                   dialog.dismiss();
                }else{
                    Query fireQuery = ref.child("user").orderByChild("email").equalTo(email.getText().toString());
                    fireQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // ازا الحساب غير موجوديظهر مسج
                            if (dataSnapshot.getValue() == null) {
                                Toast.makeText(Login.this, "Not found", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                // ازا الحساب موجوديقوم بتخزين الحساب المدخل
                            } else {
                                List<user> searchList = new ArrayList<user>();
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    user user = snapshot.getValue(user.class);
                                    searchList.add(user);

                                }
                                // لوب ليقوم بالبحث عن الحساب
                                for(int i=0;i<searchList.size();i++){
                                    // ازا الايميل والباسورد صحيحة
                                    if(searchList.get(i).getEmail().equals(email.getText().toString()) && searchList.get(i).getPassword().equals(password.getText().toString())) {

                                        //الاوبجكت هذا خاص بنقل البيانات من كلاس لكلاس اخر
                                     editor.putString(Keys.KEY_CUSTOMER,email.getText().toString());
                                     editor.putString(Keys.KEY_NAME,searchList.get(i).getName());

                                           editor.apply();
                                            dialog.dismiss();
                                            // الانتقال لواجهة الرئيسية اللادمن
                                        Intent i1 = new Intent(Login.this,MainActivity.class);
                                        i1.putExtra(Keys.KEY_CUSTOMER,email.getText().toString());
                                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            finish();
                                        startActivity(i1);

                                        // غير ذلك ازا كان خطأ بكلمة المرور او اسم المستخدم
                                    }else{
                                        dialog.dismiss();
                                        Toast.makeText(Login.this, "invalid user name or password", Toast.LENGTH_SHORT).show();}

                                }


                                dialog.dismiss();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            dialog.dismiss();
                            Toast.makeText(Login.this, "no connected internet", Toast.LENGTH_SHORT).show();}


                    });





                }
            }
        });

    }
}
