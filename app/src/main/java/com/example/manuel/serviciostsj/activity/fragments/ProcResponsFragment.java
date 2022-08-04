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
import com.example.manuel.serviciostsj.dto.proc_responsabilidad;

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
 * {@link ProcResponsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProcResponsFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public ProcResponsFragment() {
        // Required empty public constructor
    }

    int ID_TRABAJADOR;
    String respuesta="";
    String IP= "http://servicios-tsj.gob.mx";
    String GET_PR= IP+"/wsobtener_procRspnd.php";
    ObtenerWebService hiloconexion=null;
    proc_responsabilidad ProcResponsabilidad=null;
    ArrayList<proc_responsabilidad> ArrayProcResponsabilidad= new ArrayList<proc_responsabilidad>();
    ArrayList<String> listProcResponsabilidad= new ArrayList<String>();
    ArrayAdapter<String> adapterProcResponsabilidad=null;
    TextView txtBienvenido;
    ListView ListProcResponsabilidad;
    Bundle b;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hiloconexion= new ObtenerWebService();
        try {
            respuesta= hiloconexion.execute(GET_PR).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_proc_respons, container, false);
        txtBienvenido = (TextView) view.findViewById(R.id.Bienvenido);
        ListProcResponsabilidad = (ListView) view.findViewById(R.id.ListProcRespons);
        b= getArguments();
        if(b!=null){
            txtBienvenido.setText("Bienvenido : "+b.getString("NOMBRE")+" "+b.getString("APATERNO")+" "+b.getString("AMATERNO"));
            ID_TRABAJADOR= Integer.valueOf(b.getString("ID"));
            if(respuesta.equals("si hay proc responsabilidad")){
                inicializarLista(ID_TRABAJADOR);
            }
        }
        if (!verificaConexion(getContext())) {
            Snackbar.make(view,"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
        }
        return view;
    }

    private void inicializarLista(int IDT) {
        ProcResponsabilidad= new proc_responsabilidad();

        for (proc_responsabilidad b : ArrayProcResponsabilidad) {
            if (!ArrayProcResponsabilidad.isEmpty()) {
                if(b.getID_TRABAJADOR()==IDT) {
                    listProcResponsabilidad.add("\n" + "NOMBRE PROMOVENTE : " +b.getNOMBRE_PROMOVENTE()+ "\n" + "\n" + "FECHA : " + b.getFECHA() + "\n" + "\n" + "NUMERO EXPEDIENTE : " + b.getNUMERO_EXPEDIENTE() + "\n" + "\n" +
                            "SENTIDO RESOLUCION : " + b.getSENTIDO_RESOLUCION()+ "\n" + "\n" + "SANCION : " + b.getSANCION() + "\n" + "\n" + "TERMINOS SANCION : " + b.getTERMINOS_SANCION() + "\n" + "\n" +
                            "OBSERVACIONES : " + b.getOBSERVACIONES() + "\n");
                }
            }
        }
        adapterProcResponsabilidad =
                new ArrayAdapter<String>(getContext(), R.layout.list_black_text, R.id.list_content, listProcResponsabilidad);
        adapterProcResponsabilidad.notifyDataSetChanged();

        ListProcResponsabilidad.setAdapter(adapterProcResponsabilidad);
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

                    if (resultJSON.trim().equals("1")) {      // hay proc responsabilidad a mostrar
                        JSONArray usersJSON = respuestaJSON.getJSONArray("procRspnd");   // estado es el nombre del campo en el JSON
                        for (int i = 0; i < usersJSON.length(); i++) {
                            ProcResponsabilidad= new proc_responsabilidad(usersJSON.getJSONObject(i).getInt("ID")
                                    , usersJSON.getJSONObject(i).getInt("ID_TRABAJADOR")
                                    , usersJSON.getJSONObject(i).getString("NOMBRE_PROMOVENTE")
                                    , usersJSON.getJSONObject(i).getString("FECHA")
                                    , usersJSON.getJSONObject(i).getString("NUMERO_EXPEDIENTE")
                                    , usersJSON.getJSONObject(i).getString("SENTIDO_RESOLUCION")
                                    , usersJSON.getJSONObject(i).getString("SANCION")
                                    , usersJSON.getJSONObject(i).getString("TERMINOS_SANCION")
                                    , usersJSON.getJSONObject(i).getString("OBSERVACIONES"));
                            ArrayProcResponsabilidad.add(ProcResponsabilidad);
                            devuelve = "si hay proc responsabilidad";
                        }

                    } else if (resultJSON.trim().equals("2")) {
                        devuelve = "no hay proc responsabilidad";
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
