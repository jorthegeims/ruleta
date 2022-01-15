package app.casino.ruleta;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class registro extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    RequestQueue requestQueue;

    private String idU;
    private String nameU;
    private String monedasU;

    private static final String URL1 = "http://192.168.1.8/android/save.php";

    SharedPreferences sharedPreferences;
    private final String usuario = "usuario";
    SharedPreferences preferencesU;

    private GoogleApiClient googleApiClient;

    public static final int SIGN_IN_CODE = 777;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        LottieAnimationView loginA = findViewById(R.id.login);
        loginA.setAnimation(R.raw.login);
        loginA.playAnimation();

        requestQueue = Volley.newRequestQueue(registro.this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        SignInButton signInButton = findViewById(R.id.iniciar);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent,SIGN_IN_CODE);
            }
        });
        String SHARED_PREF_NAME = "mypref";
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        preferencesU = getSharedPreferences(usuario, Context.MODE_PRIVATE);



    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Error de conexion", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE){
            assert data != null;
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            assert result != null;
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

    if (result.isSuccess()){

        GoogleSignInAccount account = result.getSignInAccount();

        assert account != null;

        idU = account.getEmail();
        nameU = account.getDisplayName();
        monedasU = "0";

        readUser();

    }else {

        Toast.makeText(this, "no se pudo iniciar sesion", Toast.LENGTH_LONG).show();
        result.getStatus();

    }
    }

    private void createUser(final String idU,final String nameU,final String monedasU) {


        StringRequest stringRequest1 = new StringRequest(
                Request.Method.POST,
                URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(registro.this, "correct", Toast.LENGTH_SHORT).show();

                        SharedPreferences.Editor editor2 = preferencesU.edit();
                        editor2.putString(usuario, idU);

                        Log.i("valores", idU);

                        editor2.apply();

                        goMainScreen();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof NoConnectionError) {

                            Toast.makeText(registro.this, "revisa tu conexion a internet", Toast.LENGTH_LONG).show();
                            Log.i("errores", error.getMessage());
                        }else {

                        Toast.makeText(registro.this, "error", Toast.LENGTH_LONG).show();

                            if (!(error.getMessage()==null)){

                                    Log.i("Errores", error.getMessage());
                                }
                        }

                    }
                }
        ){
            @NonNull
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("id", idU);//idU
                params.put("usuario", nameU);//nameU
                params.put("monedas", monedasU );//monedasU

                return params;
            }
        };

        requestQueue.add(stringRequest1);



    }

    private void readUser(){
        String URL = "http://192.168.1.8/android/fetch.php?id=" + idU;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(

                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String name, email, monedas;
                        try {
                            name = response.getString("usuario");
                            email = response.getString("id");
                            monedas = response.getString("monedas");

                            Log.i("valores", name + email + monedas);

                            SharedPreferences.Editor editor2 = preferencesU.edit();
                            editor2.putString(usuario, idU);
                            editor2.apply();

                            goMainScreen();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError) {

                            Toast.makeText(registro.this, "revisa tu conexion a internet", Toast.LENGTH_LONG).show();

                        }else if (error instanceof ServerError){
                            createUser(idU,nameU,monedasU);
                        }else if (error instanceof NetworkError){
                            Toast.makeText(registro.this, "error de servidor", Toast.LENGTH_LONG).show();
                        }



                        else if (!(error.getMessage()==null)){

                            if (error.getMessage().equals("org.json.JSONException: Value not of type java.lang.String cannot be converted to JSONObject")){

                            createUser(idU,nameU,monedasU);

                            }
                        }

                        else  {

                            Toast.makeText(registro.this, "error", Toast.LENGTH_LONG).show();

                        }

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void goMainScreen() {

    Intent intent = new Intent(this, MainActivity.class);
    intent.putExtra("nom", true);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intent);

    }
}