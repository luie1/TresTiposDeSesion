package com.example.loginprueba;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.signin.SignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CODE = 1000;
    List<AuthUI.IdpConfig> prov;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=findViewById(R.id.signout);

        prov= Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn.setEnabled(false);
                                opciones();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(),""+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        opciones();
    }

    private void opciones() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder().
                        setAvailableProviders(prov).
                        setTheme(R.style.mio).
                        build(),CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CODE){
            IdpResponse response=IdpResponse.fromResultIntent(data);

            if(resultCode==RESULT_OK){
                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(getApplicationContext(),""+user.getEmail(),Toast.LENGTH_LONG).show();

                btn.setEnabled(true);
            }else{
                Toast.makeText(getApplicationContext(),""+response.getError().getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }
}
