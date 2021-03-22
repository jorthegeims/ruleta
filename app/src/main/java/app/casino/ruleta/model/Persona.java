package app.casino.ruleta.model;

public class Persona {

    private String uid;
    private String nombre;
    private String contraseña;
    private float monedas;

    public float getMonedas() {
        return monedas;
    }

    public void setMonedas(float monedas) {
        this.monedas = monedas;
    }

    public Persona() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return contraseña;
    }

    public void setEmail(String contraseña) {
        this.contraseña = contraseña;
    }


    @Override
    public String toString() {
        return (" " + nombre + " " + monedas );
    }
}
