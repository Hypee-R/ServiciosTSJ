package com.example.manuel.serviciostsj.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.example.manuel.serviciostsj.R;
import com.example.manuel.serviciostsj.activity.fragments.AltaBenefiFragment;
import com.example.manuel.serviciostsj.activity.fragments.AltaGastMedicFragment;
import com.example.manuel.serviciostsj.activity.fragments.AltaHistcuFragment;

public class AltasActivity extends AppCompatActivity implements AltaHistcuFragment.OnFragmentInteractionListener,
        AltaBenefiFragment.OnFragmentInteractionListener, AltaGastMedicFragment.OnFragmentInteractionListener{

    Bundle b;
    int operacion=0;
    String[] arreglo_datos=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altas);
        b= getIntent().getExtras().getBundle("datos");
        operacion= getIntent().getExtras().getInt("operacion");
        if(getIntent().getExtras().getString("accion").equals("modificar")){
            arreglo_datos= new String[getIntent().getExtras().getStringArray("editables").length];
            arreglo_datos= getIntent().getExtras().getStringArray("editables");
            setFragmentModifi(operacion,"Modificacion de Datos",b,arreglo_datos);
        }else if(getIntent().getExtras().getString("accion").equals("insertar")){
            setFragment(operacion,"Nuevo Registro",b);
        }
        if (!verificaConexion(this)) {
            Snackbar.make(getCurrentFocus(),"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
        }
    }

    //Este metodo sirve para asignar los tipos de fragments
    //dependiendo de que ventana se mande a traer y se asigna
    public void setFragment(int menu, CharSequence title, Bundle datos){
        boolean FragmentTransaction= false;
        Fragment fragment= null;

        switch (menu){
            case 0: fragment = new AltaHistcuFragment();
                FragmentTransaction = true;
                break;
            case 1: fragment = new AltaBenefiFragment();
                FragmentTransaction = true;
                break;
            case 2: fragment = new AltaGastMedicFragment();
                FragmentTransaction = true;
                break;
            default:
                break;
        }

        if(FragmentTransaction){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.altas_content,fragment)
                    .commit();

            getSupportActionBar().setTitle(title);
        }
        datos.putString("accion","insertar");
        fragment.setArguments(datos);

    }

    public void setFragmentModifi(int menu, CharSequence title, Bundle datos,String[] arreglo_datos){
        boolean FragmentTransaction= false;
        Fragment fragment= null;

        switch (menu){
            case 0: fragment = new AltaHistcuFragment();
                FragmentTransaction = true;
                break;
            case 1: fragment = new AltaBenefiFragment();
                FragmentTransaction = true;
                break;
            case 2: fragment = new AltaGastMedicFragment();
                FragmentTransaction = true;
                break;
            default:
                break;
        }

        if(FragmentTransaction){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.altas_content,fragment)
                    .commit();

            getSupportActionBar().setTitle(title);
        }
        datos.putStringArray("editables",arreglo_datos);
        datos.putString("accion","modificar");
        fragment.setArguments(datos);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public static boolean verificaConexion(Context ctx) {
        boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
    }
}
