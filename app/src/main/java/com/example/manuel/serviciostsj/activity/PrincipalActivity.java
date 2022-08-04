package com.example.manuel.serviciostsj.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.example.manuel.serviciostsj.R;
import com.example.manuel.serviciostsj.activity.fragments.BeneficiariosFragment;
import com.example.manuel.serviciostsj.activity.fragments.DatosFragment;
import com.example.manuel.serviciostsj.activity.fragments.DatosLaFragment;
import com.example.manuel.serviciostsj.activity.fragments.GastosMdcFragment;
import com.example.manuel.serviciostsj.activity.fragments.HistoriaCuFragment;
import com.example.manuel.serviciostsj.activity.fragments.ProcResponsFragment;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatosFragment.OnFragmentInteractionListener,
        DatosLaFragment.OnFragmentInteractionListener, HistoriaCuFragment.OnFragmentInteractionListener,
        BeneficiariosFragment.OnFragmentInteractionListener, GastosMdcFragment.OnFragmentInteractionListener,
        ProcResponsFragment.OnFragmentInteractionListener {

    Intent altas=null;
    Bundle datos;
    int touch=0;
    FloatingActionButton fab1,fab2,fab3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab1= (FloatingActionButton) findViewById(R.id.fab_1);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altas= new Intent(getApplicationContext(),AltasActivity.class);
                altas.putExtra("datos",datos);
                altas.putExtra("operacion",0);
                altas.putExtra("accion","insertar");
                startActivity(altas);
            }
        });
        fab2= (FloatingActionButton) findViewById(R.id.fab_2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altas= new Intent(getApplicationContext(),AltasActivity.class);
                altas.putExtra("datos",datos);
                altas.putExtra("operacion",1);
                altas.putExtra("accion","insertar");
                startActivity(altas);
            }
        });
        fab3= (FloatingActionButton) findViewById(R.id.fab_3);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                altas= new Intent(getApplicationContext(),AltasActivity.class);
                altas.putExtra("datos",datos);
                altas.putExtra("operacion",2);
                altas.putExtra("accion","insertar");
                startActivity(altas);
            }
        });
        //Animations
        final Animation show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        final Animation hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);

        final Animation show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        final Animation hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);

        final Animation show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
        final Animation hide_fab_3= AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(touch==0) {
                    mostrarFABS(show_fab_1,show_fab_2,show_fab_3);
                }
                touch++;
                if(touch>1) {
                    ocultarFABS(hide_fab_1,hide_fab_2,hide_fab_3);
                    touch=0;
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        datos= getIntent().getExtras();

        CharSequence title= "ServiciosTSJ";

        setFragment(0,title,datos);

        if (!verificaConexion(this)) {
            Snackbar.make(findViewById(android.R.id.content),"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            //case R.id.action_settings: updatePassword();
              //  break;
            case R.id.action_DatosPersonales: setFragment(0,"ServiciosTSJ",datos);
                                                item.setChecked(false);
                break;
            case R.id.action_info: VentanaEmergente();
                break;
            case R.id.cerrar_sesion: Intent logout= new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(logout);
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       switch (id){
           case R.id.action_DatosPersonales: item.setChecked(false);
               break;
           case R.id.datos_laborales: item.setChecked(true);
                    setFragment(1,item.getTitle(),datos);
               break;
           case R.id.historia_curricular: item.setChecked(true);
               setFragment(2,item.getTitle(),datos);
               break;
           case R.id.beneficiarios: item.setChecked(true);
               setFragment(3,item.getTitle(),datos);
               break;
           case R.id.gastos_medicos: item.setChecked(true);
               setFragment(4,item.getTitle(),datos);
               break;
           case R.id.proc_responsabilidad: item.setChecked(true);
               setFragment(5,item.getTitle(),datos);
               break;
           default:
               break;
       }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //Este metodo sirve para asignar los tipos de fragments
    //dependiendo de que ventana se mande a traer y se asigna
    public void setFragment(int menu, CharSequence title, Bundle datos){
        boolean FragmentTransaction= false;
        Fragment fragment= null;

        switch (menu){
            case 0: fragment = new DatosFragment();
                FragmentTransaction = true;
                break;
            case 1:fragment = new DatosLaFragment();
                FragmentTransaction = true;
                break;
            case 2: fragment = new HistoriaCuFragment();
                FragmentTransaction = true;
                break;
            case 3: fragment = new BeneficiariosFragment();
                FragmentTransaction = true;
                break;
            case 4: fragment = new GastosMdcFragment();
                FragmentTransaction = true;
                break;
            case 5: fragment = new ProcResponsFragment();
                FragmentTransaction = true;
            default:
                break;
        }

        if(FragmentTransaction){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_principal,fragment)
                    .commit();

            getSupportActionBar().setTitle(title);
        }

        fragment.setArguments(datos);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    //Este medoto sirve para abrir una ventana emergente sobre la informacion de la app
    public void VentanaEmergente(){
            AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
            builder.setTitle("Informacion sobre la aplicacion");
            builder.setMessage("Esta aplicacion fue elaborada con fines de consulta de informacion"+"\n"+
            " para el Tribunal Superior de justicia."+"\n"+
            " Los detalles de esta aplicacion estan sujetas a derechos de autor."+"\n"+
            " Desarrollado por: Luis Antonio Altamirano Sanchez.")
                    .setCancelable(false)
                    .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updatePassword(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PrincipalActivity.this);
        builder.setTitle("Actualizar Contraseña")
                .setView(R.layout.activity_updt_passw)
                .setCancelable(false)
                .setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //Metodo que muestra los fabs ocultos bajo el FAB principal
    public void mostrarFABS(Animation show_fab_1, Animation show_fab_2, Animation show_fab_3){
        FrameLayout.LayoutParams layoutParamsF1 = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParamsF1.rightMargin += (int) (fab1.getWidth() * 1.7);
        layoutParamsF1.bottomMargin += (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParamsF1);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);

        FrameLayout.LayoutParams layoutParamsF2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParamsF2.rightMargin += (int) (fab2.getWidth() * 1.5);
        layoutParamsF2.bottomMargin += (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParamsF2);
        fab2.startAnimation(show_fab_2);
        fab2.setClickable(true);

        FrameLayout.LayoutParams layoutParamsF3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParamsF3.rightMargin += (int) (fab3.getWidth() * 0.25);
        layoutParamsF3.bottomMargin += (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(layoutParamsF3);
        fab3.startAnimation(show_fab_3);
        fab3.setClickable(true);
    }

    //Metodo que oculta los fabs bajo el FAB principal
    public void ocultarFABS(Animation hide_fab_1,Animation hide_fab_2,Animation hide_fab_3){
        FrameLayout.LayoutParams LayoutParamsF1 = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        LayoutParamsF1.rightMargin -= (int) (fab1.getWidth() * 1.7);
        LayoutParamsF1.bottomMargin -= (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(LayoutParamsF1);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);

        FrameLayout.LayoutParams LayoutParamsF2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        LayoutParamsF2.rightMargin -= (int) (fab2.getWidth() * 1.5);
        LayoutParamsF2.bottomMargin -= (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(LayoutParamsF2);
        fab2.startAnimation(hide_fab_2);
        fab2.setClickable(false);

        FrameLayout.LayoutParams LayoutParamsF3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        LayoutParamsF3.rightMargin -= (int) (fab3.getWidth() * 0.25);
        LayoutParamsF3.bottomMargin -= (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(LayoutParamsF3);
        fab3.startAnimation(hide_fab_3);
        fab3.setClickable(false);
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
