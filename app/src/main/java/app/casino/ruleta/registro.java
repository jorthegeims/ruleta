package app.casino.ruleta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import app.casino.ruleta.model.Persona;

public class registro extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText passwordP;
    private Button btnregistrar;
    private SignInButton signInButton;
    private LottieAnimationView loginA;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    SharedPreferences sharedPreferences;
    private String usuario = "usuario";
    private String SHARED_PREF_NAME = "mypref";
    SharedPreferences preferences;

    private boolean dou = false;

    /*
    FirebaseAuth mfirebaseAutH;

    FirebaseAuth.AuthStateListener mAuthListener;

    public static final int REQUEST_CODE = 11111;

    List<AuthUI.IdpConfig> provider = Arrays.asList(

            new AuthUI.IdpConfig.GoogleBuilder().build()

            );*/

    private GoogleApiClient googleApiClient;

    public static final int SIGN_IN_CODE = 777;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        loginA = findViewById(R.id.login);
        loginA.setAnimation(R.raw.login);
        loginA.playAnimation();

/*
        mfirebaseAutH = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    Toast.makeText(getApplicationContext(),"iniciado",Toast.LENGTH_LONG).show();
                }else {

                    startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                            .setAvailableProviders(provider)
                            .setIsSmartLockEnabled(false)
                            .build(),REQUEST_CODE
                    );
                }

            }
        };*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        signInButton = findViewById(R.id.iniciar);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        preferences = getSharedPreferences(usuario, Context.MODE_PRIVATE);

        inicializarFirebase();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    // 192.168.1.6 o 127.0.0.1


    private void inicializarFirebase() {

        FirebaseApp.initializeApp(this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion", Toast.LENGTH_LONG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

    if (result.isSuccess()){



        GoogleSignInAccount account = result.getSignInAccount();

        String id = account.getId();

        databaseReference.child("Persona").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Persona intento = snapshot.getValue(Persona.class);
                if (dou == false) {


                    try {
                        Log.i("douuuu", "inicioinicioinicioinicio");
                        Log.i("douuuu", id);
                        if (intento.getUid().equals(id)) {
                            Log.i("douuuu", "inicioinicio");
                            //Toast.makeText(getApplicationContext(), "Ese usuario ya existe", Toast.LENGTH_LONG).show();


                        }
                        Log.i("douuuu", intento.getNombre());
                    } catch (Exception e) {


                        SharedPreferences.Editor editor = preferences.edit();


                        Persona p = new Persona();


                        p.setUid(id);
                        p.setNombre(account.getDisplayName());
                        p.setEmail(account.getEmail());
                        p.setMonedas(0);
                        databaseReference.child("Persona").child(id).setValue(p);

                        editor.putString(usuario, id);
                        //editor.putFloat(monedas, (float) (preferences.getFloat(monedas, (float) 0.0)+2.5));
                        editor.apply();

                        //Toast.makeText(getApplicationContext(), "registrado", Toast.LENGTH_LONG).show();


                        dou = true;


                    }

                    goMainScreen();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }else {
        Toast.makeText(this, "no se pudo iniciar sesion", Toast.LENGTH_LONG).show();
    }


    }

    private void goMainScreen() {



    Intent intent = new Intent(this, MainActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);

    }
}

/*private void ejecutarServicio(String URL){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "registro exitoso", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros= new HashMap<String, String>();
                parametros.put("usuario",nombre.getText().toString());
                parametros.put("contraseña",contraseña.getText().toString());
                parametros.put("monedas", "0");
                return parametros;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(5*DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 0, 0));
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, 0, 0));
        RequestQueue requestQueue= Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);



    }*/