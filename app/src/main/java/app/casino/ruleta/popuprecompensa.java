package app.casino.ruleta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import app.casino.ruleta.model.actualizarMonedas;

public class popuprecompensa extends AppCompatActivity {

    private float monedas;
    TextView nmonedas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popuprecompensa);

        nmonedas = findViewById(R.id.TM);

        actualizarMonedas.readUser(this, nmonedas);

        Log.i("valores", (String) nmonedas.getText());

    }

    public void Circulop1 (View view) {
        monedas = Float.parseFloat((String) nmonedas.getText());
        if (monedas > 2700) {

            Intent intent1 = new Intent(popuprecompensa.this, paypalA.class);
            intent1.putExtra("monedas", monedas);
            intent1.putExtra("dinero", 5);
            intent1.putExtra("resta", 2700);
            startActivity(intent1);

        }
    }

        public void Circulop2 (View view){
            monedas = Float.parseFloat((String) nmonedas.getText());
            if (monedas> 5200) {


                Intent intent1 = new Intent(popuprecompensa.this, paypalA.class);
                intent1.putExtra("monedas", monedas);
                intent1.putExtra("dinero",10);
                intent1.putExtra("resta",5200);
                startActivity(intent1);

            }
    }

    public void Circulop3 (View view){
        monedas = Float.parseFloat((String) nmonedas.getText());
        if (monedas> 7700) {


            Intent intent1 = new Intent(popuprecompensa.this, paypalA.class);
            intent1.putExtra("monedas", monedas);
            intent1.putExtra("dinero",15);
            intent1.putExtra("resta",7700);
            startActivity(intent1);

        }
    }

    public void Circulop4 (View view){

        if (monedas> 10200) {

            Intent intent1 = new Intent(popuprecompensa.this, paypalA.class);
            intent1.putExtra("monedas", monedas);
            intent1.putExtra("dinero",20);
            intent1.putExtra("resta",10200);
            startActivity(intent1);

        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(popuprecompensa.this, MainActivity.class);

            startActivity(intent);
        }

        return super.onKeyDown(keyCode, event);
    }

    public void atras (View view){

        Intent intent = new Intent(popuprecompensa.this, MainActivity.class);

        startActivity(intent);

    }
}