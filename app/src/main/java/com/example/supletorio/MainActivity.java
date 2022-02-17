package com.example.supletorio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity<FirebaseAuth> extends AppCompatActivity {

    ///////////
    EditText correo,contraseña;
    String textCorreo,textContrasenia;
    Button aceptar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        correo = findViewById(R.id.editText_correo);
        contraseña = findViewById(R.id.editText_contrasenia);
        aceptar = findViewById(R.id.button_iniciar_sesion);

        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textCorreo = correo.getText().toString();
                textContrasenia = contraseña.getText().toString();
                if(!Patterns.EMAIL_ADDRESS.matcher(textCorreo).matches()){
                    correo.setError("Verfifica que el correo esté bien escrito");
                    correo.requestFocus();
                }else if (textContrasenia.isEmpty()){
                    contraseña.setError("Intente ingresar su contraseña");
                    contraseña.requestFocus();
                }else{
                    IngresarFirebase(textCorreo,textContrasenia);
                }
            }


        });
    }

    private void IngresarFirebase(String correob,String contraseñab) {
        Toast.makeText(MainActivity.this, textCorreo+textContrasenia,
                Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(correob, contraseñab)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Authentication susces.",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        UsuarioLogeado();
    }
    private void UsuarioLogeado(){
        if(mAuth != null){
            //Toast.makeText(MenuPrincipal.this, "Usuario logeado", Toast.LENGTH_SHORT).show();
        }else{
            startActivity(new Intent(MainActivity.this,MainActivity2_MJ.class));
            finish();
        }
    }
}