package com.example.manuel.serviciostsj.activity.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuel.serviciostsj.R;
import com.example.manuel.serviciostsj.activity.AltasActivity;
import com.example.manuel.serviciostsj.dto.gastos_medicos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GastosMdcFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class GastosMdcFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public GastosMdcFragment() {
        // Required empty public constructor
    }

    int ID_TRABAJADOR;
    String respuesta="";
    String IP= "http://servicios-tsj.gob.mx";
    String GET_GM= IP+"/wsobtener_gastmedicos.php";
    ObtenerWebService hiloconexion=null;
    gastos_medicos GastosMedicos=null;
    ArrayList<gastos_medicos> ArrayGastosMedicos= new ArrayList<gastos_medicos>();
    ArrayList<String> listGastosMedicos= new ArrayList<String>();
    ArrayAdapter<String> adapterGastosMedicos=null;
    TextView txtBienvenido;
    ListView ListGastosMedicos;
    Bundle b;

    int[] arre_id_datos=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hiloconexion= new ObtenerWebService();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_gastos_mdc, container, false);
        txtBienvenido = (TextView) view.findViewById(R.id.Bienvenido);
        ListGastosMedicos = (ListView) view.findViewById(R.id.ListGastosMedicos);
        try {
            respuesta = hiloconexion.execute(GET_GM).get();
            b= getArguments();
            if(b!=null){
                txtBienvenido.setText("Bienvenido : "+b.getString("NOMBRE")+" "+b.getString("APATERNO")+" "+b.getString("AMATERNO"));
                ID_TRABAJADOR= Integer.valueOf(b.getString("ID"));
                if(respuesta.equals("si hay gastos medicos")){
                    inicializarLista(ID_TRABAJADOR);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ListGastosMedicos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                muestraDialogo(position,b);
                return false;
            }
        });
        if (!verificaConexion(getContext())) {
            Snackbar.make(view,"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
        }
        return view;
    }

    private void inicializarLista(int IDT) {
        GastosMedicos= new gastos_medicos();
        int posicion=0,contador=0;
        for (gastos_medicos b : ArrayGastosMedicos) {
            if (!ArrayGastosMedicos.isEmpty()) {
                System.out.println("El id del trabajador es :" +b.getID_TRABAJADOR());
                if(b.getID_TRABAJADOR()==IDT) {
                    listGastosMedicos.add("\n" + "CANTIDAD ACUM TITULAR : " +b.getCANTIDAD_ACUMULADA_TITULAR()+ "\n" + "\n" + "CANTIDAD ACUM BENEFICIARIO : " + b.getCANTIDAD_ACUMULADA_BENEFICIARIO() + "\n" + "\n" + "CANTIDAD PAGAR : " + b.getCANTIDAD_PAGAR() + "\n" + "\n" +
                            "CONCEPTO : " + b.getCONCEPTO() + "\n" + "\n" + "COMPROBANTE : " + b.getCOMPROBANTE() + "\n" + "\n" + "ESTATUS : " + b.getESTATUS() + "\n" + "\n" +
                            "FECHA : " + b.getFECHA() + "\n" + "\n" +"NUMERO COMPROBANTE : " + b.getNUMERO_COMPROBANTE() + "\n" + "\n" + "OBSERVACIONES : " + b.getOBSERVACIONES() + "\n");
                    contador++;
                }
            }
        }
        arre_id_datos= new int[contador];
        for (gastos_medicos gm : ArrayGastosMedicos) {
            if (!ArrayGastosMedicos.isEmpty()) {
                if (gm.getID_TRABAJADOR() == IDT) {
                    arre_id_datos[posicion]=gm.getID();
                    posicion++;
                }
            }
        }
        adapterGastosMedicos =
                new ArrayAdapter<String>(getContext(), R.layout.list_black_text, R.id.list_content, listGastosMedicos);
        adapterGastosMedicos.notifyDataSetChanged();

        ListGastosMedicos.setAdapter(adapterGastosMedicos);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ObtenerWebService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String direccion = params[0];
            URL url = null; // url de donde queremos obtener la informacion.
            String devuelve = "";
                try {
                    url = new URL(direccion);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");
                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {

                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.
                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }
                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados
                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON.trim().equals("1")) {      // hay gastos medicos a mostrar
                            JSONArray usersJSON = respuestaJSON.getJSONArray("gastmedicos");   // estado es el nombre del campo en el JSON
                            for (int i = 0; i < usersJSON.length(); i++) {
                                GastosMedicos= new gastos_medicos(usersJSON.getJSONObject(i).getInt("ID")
                                        , usersJSON.getJSONObject(i).getInt("ID_BENEFICIARIO")
                                        , usersJSON.getJSONObject(i).getInt("ID_TRABAJADOR")
                                        , usersJSON.getJSONObject(i).getDouble("CANTIDAD_ACUMULADA_BENEFICIARIO")
                                        , usersJSON.getJSONObject(i).getDouble("CANTIDAD_ACUMULADA_TITULAR")
                                        , usersJSON.getJSONObject(i).getDouble("CANTIDAD_PAGAR")
                                        , usersJSON.getJSONObject(i).getString("CONCEPTO")
                                        , usersJSON.getJSONObject(i).getString("COMPROBANTE")
                                        , usersJSON.getJSONObject(i).getString("ESTATUS")
                                        , usersJSON.getJSONObject(i).getString("FECHA")
                                        , usersJSON.getJSONObject(i).getString("NUMERO_COMPROBANTE")
                                        , usersJSON.getJSONObject(i).getString("OBSERVACIONES"));
                                ArrayGastosMedicos.add(GastosMedicos);
                                devuelve = "si hay gastos medicos";
                            }
                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "no hay gastos medicos";
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return devuelve;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            System.out.println(s);
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

    }

    private void muestraDialogo(final int pos, final Bundle datos){
        final String[] arreglo_datos= new String[10];
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("¿Deseas modificar los datos seleccionados?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Aquí lo que se desea realizar
                        if (!verificaConexion(getContext())) {
                            Snackbar.make(getView(),"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
                        }
                            Toast.makeText(getContext(), "El id de datos de los Gastos Medicos es: " + arre_id_datos[pos], Toast.LENGTH_SHORT).show();
                            for (gastos_medicos gm : ArrayGastosMedicos) {
                                if (arre_id_datos[pos] == gm.getID()) {
                                    arreglo_datos[0] = String.valueOf(gm.getID());
                                    arreglo_datos[1] = String.valueOf(gm.getCANTIDAD_ACUMULADA_TITULAR());
                                    arreglo_datos[2] = String.valueOf(gm.getCANTIDAD_ACUMULADA_BENEFICIARIO());
                                    arreglo_datos[3] = String.valueOf(gm.getCANTIDAD_PAGAR());
                                    arreglo_datos[4] = gm.getCONCEPTO();
                                    arreglo_datos[5] = gm.getCOMPROBANTE();
                                    arreglo_datos[6] = gm.getESTATUS();
                                    arreglo_datos[7] = gm.getFECHA();
                                    arreglo_datos[8] = gm.getNUMERO_COMPROBANTE();
                                    arreglo_datos[9] = gm.getOBSERVACIONES();
                                }
                            }
                            for (int i = 0; i < arreglo_datos.length; i++) {
                                if (arreglo_datos[i].equals("") || arreglo_datos[i].equals(null)) {
                                    arreglo_datos[i] = "N/A";
                                }
                            }
                            Intent modificaDatos = new Intent(getContext(), AltasActivity.class);
                            modificaDatos.putExtra("datos", datos);
                            modificaDatos.putExtra("operacion", 2);
                            modificaDatos.putExtra("accion", "modificar");
                            modificaDatos.putExtra("editables", arreglo_datos);
                            startActivity(modificaDatos);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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