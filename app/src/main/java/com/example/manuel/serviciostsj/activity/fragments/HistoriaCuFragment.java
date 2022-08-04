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
import com.example.manuel.serviciostsj.dto.historia_curricular;

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
 * {@link HistoriaCuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class HistoriaCuFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public HistoriaCuFragment() {
        // Required empty public constructor
    }
    String respuesta="";
    String IP= "http://servicios-tsj.gob.mx";
    String GET_HIST_CU= IP+"/wsobtener_histcu.php";
    ObtenerWebService hiloconexion=null;
    historia_curricular dthistcu=null;
    ArrayList<historia_curricular> Arraylisthistcu= new ArrayList<historia_curricular>();
    ArrayList<String> listhis_cu= new ArrayList<String>();
    TextView txtBienvenido;
    ListView Listhistcu;
    Bundle b;
    int ID_TRABAJADOR;
    ArrayAdapter<String> adapterHistcu=null;

    int[] arreglo_id_datos=null;;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hiloconexion= new ObtenerWebService();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_historia_cu, container, false);
        txtBienvenido= (TextView) view.findViewById(R.id.Bienvenido);
        Listhistcu= (ListView) view.findViewById(R.id.LisHistcu);
        try {
            respuesta= hiloconexion.execute(GET_HIST_CU).get();
            b= getArguments();
            if(b!=null){
                txtBienvenido.setText("Bienvenido : "+b.getString("NOMBRE")+" "+b.getString("APATERNO")+" "+b.getString("AMATERNO"));
                ID_TRABAJADOR= Integer.valueOf(b.getString("ID"));
                if(respuesta.equals("si hay hist curricular")){
                    inicializarLista(ID_TRABAJADOR);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Listhistcu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
        dthistcu= new historia_curricular();
        int posicion=0,contador=0;
        for (historia_curricular b : Arraylisthistcu) {
            if (!Arraylisthistcu.isEmpty()) {
                if(b.getID_TRABAJADOR()==IDT) {
                    listhis_cu.add("\n" + "NOMBRE CURSO: " + b.getCURSO()+ "\n" + "\n" + "CALIFICACION : " + b.getCALIFICACION() + "\n" + "\n" + "INSTITUCION : " + b.getINSTITUCION_EXPIDE() + "\n" + "\n" +
                            "LUGAR DEL CURSO : " + b.getLUGAR_EXPEDICION() + "\n" + "\n" + "DURACION : " + b.getDURACION() + "\n" + "\n" +
                            "FECHA DE INICIO : " + b.getFECHA_INICIO() + "\n" + "\n" + "FECHA FIN : " + b.getFECHA_FIN() + "\n" + "\n" +
                            "QUIEN AUTORIZA : " + b.getQUIEN_AUTORIZA() + "\n" + "\n" + "OBSERVACIONES : " + b.getOBSERVACIONES() + "\n" + "\n");
                    contador++;
                }
            }
        }
        arreglo_id_datos= new int[contador];
        for (historia_curricular hc : Arraylisthistcu) {
            if (!Arraylisthistcu.isEmpty()) {
                if (hc.getID_TRABAJADOR() == IDT) {
                    arreglo_id_datos[posicion] = hc.getID();
                    posicion++;
                }
            }
        }
        adapterHistcu =
                new ArrayAdapter<String>(getContext(), R.layout.list_black_text, R.id.list_content, listhis_cu);
        adapterHistcu.notifyDataSetChanged();

        Listhistcu.setAdapter(adapterHistcu);
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
                        if (resultJSON.trim().equals("1")) {      // hay usuarios a mostrar
                            JSONArray usersJSON = respuestaJSON.getJSONArray("histcu");   // estado es el nombre del campo en el JSON
                            for (int i = 0; i < usersJSON.length(); i++) {
                                dthistcu = new historia_curricular(usersJSON.getJSONObject(i).getInt("ID")
                                        , usersJSON.getJSONObject(i).getInt("ID_TRABAJADOR")
                                        , usersJSON.getJSONObject(i).getString("CURSO")
                                        , usersJSON.getJSONObject(i).getString("FECHA_INICIO")
                                        , usersJSON.getJSONObject(i).getString("FECHA_FIN")
                                        , usersJSON.getJSONObject(i).getString("DURACION")
                                        , usersJSON.getJSONObject(i).getString("INSTITUCION_EXPIDE")
                                        , usersJSON.getJSONObject(i).getString("LUGAR_EXPEDICION")
                                        , usersJSON.getJSONObject(i).getString("QUIEN_AUTORIZA")
                                        , usersJSON.getJSONObject(i).getString("CALIFICACION")
                                        , usersJSON.getJSONObject(i).getString("OBSERVACIONES"));
                                Arraylisthistcu.add(dthistcu);
                                devuelve = "si hay hist curricular";
                            }
                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "no hay hist curricular";
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
                            Toast.makeText(getContext(), "El id de datos de la Historia Curricular es: " + arreglo_id_datos[pos], Toast.LENGTH_SHORT).show();
                            for (historia_curricular hc : Arraylisthistcu) {
                                if (arreglo_id_datos[pos] == hc.getID()) {
                                    arreglo_datos[0] = String.valueOf(hc.getID());
                                    arreglo_datos[1] = hc.getCURSO();
                                    arreglo_datos[2] = hc.getCALIFICACION();
                                    arreglo_datos[3] = hc.getINSTITUCION_EXPIDE();
                                    arreglo_datos[4] = hc.getLUGAR_EXPEDICION();
                                    arreglo_datos[5] = hc.getDURACION();
                                    arreglo_datos[6] = hc.getFECHA_INICIO();
                                    arreglo_datos[7] = hc.getFECHA_FIN();
                                    arreglo_datos[8] = hc.getQUIEN_AUTORIZA();
                                    arreglo_datos[9] = hc.getOBSERVACIONES();
                                }
                            }
                            for (int i = 0; i < arreglo_datos.length; i++) {
                                if (arreglo_datos[i].equals("") || arreglo_datos[i].equals(null)) {
                                    arreglo_datos[i] = "N/A";
                                }
                            }
                            Intent modificaDatos = new Intent(getContext(), AltasActivity.class);
                            modificaDatos.putExtra("datos", datos);
                            modificaDatos.putExtra("operacion", 0);
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