package com.example.manuel.serviciostsj.activity.fragments;

import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.manuel.serviciostsj.R;
import com.example.manuel.serviciostsj.dto.datos_laborales;

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
 * {@link DatosLaFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DatosLaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public DatosLaFragment() {
        // Required empty public constructor
    }
    String respuesta="";
    ObtenerWebService hiloconexion= new ObtenerWebService();
    datos_laborales dtlaborales= null;
    private ArrayList<datos_laborales> listdtlab= new ArrayList<datos_laborales>();
    private ArrayList<String> listdlab= new ArrayList<String>();
    String IP= "http://servicios-tsj.gob.mx";
    String GET_DATOS_LAB= IP+"/wsobtener_antlab.php";
    TextView txtBienvenido;
    ListView ListDatos;
    ArrayAdapter<String> adaptadorUsuarios=null;
    View view;
    int ID_TRABAJADOR;
    Bundle b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            respuesta = hiloconexion.execute(GET_DATOS_LAB).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        view=  inflater.inflate(R.layout.fragment_datos_la, container, false);
        ListDatos = (ListView) view.findViewById(R.id.LisDLaborales);
        txtBienvenido = (TextView) view.findViewById(R.id.Bienvenido);
        b= getArguments();
        if(b!=null) {
            txtBienvenido.setText("Bienvenido : "+b.getString("NOMBRE")+" "+b.getString("APATERNO")+" "+b.getString("AMATERNO"));
            ID_TRABAJADOR = Integer.valueOf(b.getString("ID"));
            if (respuesta.equals("si hay datos laborales")) {
                inicializarLista(ID_TRABAJADOR);
            }
        }
        if (!verificaConexion(getContext())) {
            Snackbar.make(view,"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
        }
        return view;
    }

    private void inicializarLista(int IDT) {
        dtlaborales= new datos_laborales();

        for (datos_laborales b : listdtlab) {
            if (!listdtlab.isEmpty()) {
                if(b.getID_TRABAJADOR()==IDT) {
                    listdlab.add("\n" + "NOMBRE : " + b.getNOMBRE() + "\n" + "\n" + "APELLIDO PATERNO : " + b.getAPELLIDO_PATERNO() + "\n" + "\n" + "APELLIDO MATERNO : " + b.getAPELLIDO_MATERNO() + "\n" + "\n" +
                            "NOMBRAMIENTO : " + b.getNOMBRAMIENTO() + "\n" + "\n" + "FECHA INGRESO : " + b.getFECHA_INGRESO() + "\n" + "\n" +
                            "CAUSA INASISTENCIA : " + b.getCAUSA_INASISTENCIA() + "\n" + "\n" + "FECHA FIN DEL PERMISO : " + b.getFECHA_FIN_LICENCIA() + "\n" + "\n" +
                            "FECHA REINGRESO : " + b.getFECHA_REINGRESO() + "\n" + "\n" + "LICENCIA MEDICA : " + b.getLICENCIA_MEDICA() + "\n" + "\n" +
                            "LUGAR ADSCRIPCION : " + b.getLUGAR_ADSCRIPCION() + "\n" + "\n" + "TIPO DE CONTRATACION : " + b.getTIPO_CONTRATACION() + "\n" + "\n" +
                            "OBSERVACIONES : " + b.getOBSERVACIONES() + "\n" + "\n");
                }
            }
        }
        adaptadorUsuarios =
                new ArrayAdapter<String>(getContext(), R.layout.list_black_text, R.id.list_content, listdlab);
        adaptadorUsuarios.notifyDataSetChanged();

        ListDatos.setAdapter(adaptadorUsuarios);
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
                            JSONArray usersJSON = respuestaJSON.getJSONArray("antlab");   // estado es el nombre del campo en el JSON
                            for (int i = 0; i < usersJSON.length(); i++) {
                                dtlaborales = new datos_laborales(usersJSON.getJSONObject(i).getInt("ID")
                                        , usersJSON.getJSONObject(i).getInt("ID_TRABAJADOR")
                                        , usersJSON.getJSONObject(i).getString("NOMBRAMIENTO")
                                        , usersJSON.getJSONObject(i).getString("TIPO_CONTRATACION")
                                        , usersJSON.getJSONObject(i).getString("LUGAR_ADSCRIPCION")
                                        , usersJSON.getJSONObject(i).getString("FECHA_INGRESO")
                                        , usersJSON.getJSONObject(i).getString("FECHA_REINGRESO")
                                        , usersJSON.getJSONObject(i).getString("CAUSA_INASISTENCIA")
                                        , usersJSON.getJSONObject(i).getString("OBSERVACIONES")
                                        , usersJSON.getJSONObject(i).get("FECHA_FIN_LICENCIA").toString()
                                        , usersJSON.getJSONObject(i).get("FECHA_INICIO_LICENCIA").toString()
                                        , usersJSON.getJSONObject(i).getString("FECHA_FIN_LICENCIA")
                                        , usersJSON.getJSONObject(i).getString("NOMBRE")
                                        , usersJSON.getJSONObject(i).getString("APELLIDO_PATERNO")
                                        , usersJSON.getJSONObject(i).getString("APELLIDO_MATERNO"));
                                listdtlab.add(dtlaborales);
                                devuelve = "si hay datos laborales";
                            }

                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "No hay datos laborales";
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
