package com.example.manuel.serviciostsj.activity.fragments;

import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.manuel.serviciostsj.R;
import com.example.manuel.serviciostsj.activity.PrincipalActivity;
import com.example.manuel.serviciostsj.dto.estatus;
import com.example.manuel.serviciostsj.dto.tipos_concepto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AltaGastMedicFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AltaGastMedicFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public AltaGastMedicFragment() {
        // Required empty public constructor
    }

    //Arreglo de lista para insertar en la clase
    ArrayList<estatus> Arraylistaestatus= new ArrayList<estatus>();
    ArrayList<tipos_concepto> Arraylistatconcepto= new ArrayList<tipos_concepto>();

    Bundle b;
    EditText txtCantidadTitular, txtCantidadBeneficiario,txtCantidadPagar,txtComprobante,txtFecha,txtObservaciones,txtNumeroComprobante;
    Spinner cmbConcepto, cmbEstatus;
    Button btnGuardar;
    estatus estatuses=null;
    tipos_concepto t_concepto;

    //Direcion (URL) del servicio al que me voy a conectar
    String IP= "http://servicios-tsj.gob.mx";
    String GET_CONCEPTO= IP+"/wsobtener_tiposconcepto.php";
    String GET_ESTATUS= IP+"/wsobtener_estatus.php";
    String INSERT_GASTMEDIC= IP+"/wsinsert_gastmedico.php";
    String UPDATE_GASTMEDIC= IP+"/wsupdate_gastmedico.php";

    String cantidadtitular="",cantidadbeneficiario="",cantidadpagar="",comprobante="",fecha="",observaciones="",numerocomprobante="";
    int concepto=0, estatus=0,ID_TRABAJADOR=0;

    //Lista de String para los items del spinner
    ArrayList<String> listaestatus= new ArrayList<String>();
    ArrayList<String> listaconcepto= new ArrayList<String>();
    //Adaptador de lista de String de items para el spinner
    ArrayAdapter<String> spinnerEstatusAdapter=null;
    ArrayAdapter<String> spinnerConceptoAdapter=null;
    // Hilos de conexion de los web service
    InsertaGastMedic hiloconexion=null;
    obtenerEstatus hiloestatus=null;
    obtenerConcepto hiloconcepto=null;

    //Variables para obtener las respuestas de los diferentes
    //web service
    String respuesta1="",respuesta2="",respuesta3="";

    int dia,mes,ano;
    String[] arreglo_datos= null;
    Boolean action=false;
    private NotificationManager mNotificationManager;
    private int NOTIF_ALERTA_ID = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        hiloconexion = new InsertaGastMedic();
        hiloestatus = new obtenerEstatus();
        hiloconcepto = new obtenerConcepto();
        try {
            respuesta2= hiloestatus.execute(GET_ESTATUS).get();
            respuesta3= hiloconcepto.execute(GET_CONCEPTO).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_alta_gast_medic, container, false);
        txtCantidadTitular = (EditText) view.findViewById(R.id.txtCantidadTitular);
        txtCantidadBeneficiario = (EditText) view.findViewById(R.id.txtCantidadBeneficiario);
        txtCantidadPagar = (EditText) view.findViewById(R.id.txtCantidadPagar);
        txtComprobante = (EditText) view.findViewById(R.id.txtComprobante);
        btnGuardar = (Button) view.findViewById(R.id.btnAgregar);
        txtFecha = (EditText) view.findViewById(R.id.txtFecha);
        txtFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fecha = year + "-" + (month + 1) + "-" + dayOfMonth;
                        txtFecha.setText(fecha);
                    }
                },dia,mes,ano);
                datePickerDialog.show();
            }
        });
        txtObservaciones = (EditText) view.findViewById(R.id.txtObservaciones);
        txtNumeroComprobante = (EditText) view.findViewById(R.id.txtNumeroComprobante);

        cmbConcepto = (Spinner) view.findViewById(R.id.cmbTiposConcepto);
        cmbConcepto.setOnItemSelectedListener(new MyOnItemSelectedListener());
        cmbEstatus = (Spinner) view.findViewById(R.id.cmbEstatus);
        cmbEstatus.setOnItemSelectedListener(new MyOnItemSelectedListener());

        if(respuesta2.equals("Hay estatus")){
            inicializaComboEstatus();
        }
        if(respuesta3.equals("Hay tipos concepto")){
            inicializaComboConcepto();
        }

        b = getArguments();
        if(b!=null){
            //ID del trabajador de quien da de alta nuevo registro
            // es el actualmente loggeado en la aplicacion
            ID_TRABAJADOR= Integer.valueOf(b.getString("ID"));
            action = b.getString("accion").equals("modificar");
            if(action){
                btnGuardar.setText("Actualizar");
                arreglo_datos = new String[b.getStringArray("editables").length];
                arreglo_datos = b.getStringArray("editables");
                txtCantidadTitular.setText(arreglo_datos[1]);
                txtCantidadBeneficiario.setText(arreglo_datos[2]);
                txtCantidadPagar.setText(arreglo_datos[3]);
                txtComprobante.setText(arreglo_datos[5]);
                txtFecha.setText(arreglo_datos[7]);
                txtNumeroComprobante.setText(arreglo_datos[8]);
                txtObservaciones.setText(arreglo_datos[9]);
            }
        }
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    cantidadtitular= txtCantidadTitular.getText().toString();
                    cantidadbeneficiario= txtCantidadBeneficiario.getText().toString();
                    cantidadpagar= txtCantidadPagar.getText().toString();
                    comprobante= txtComprobante.getText().toString();
                    fecha= txtFecha.getText().toString();
                    observaciones= txtObservaciones.getText().toString();
                    numerocomprobante= txtNumeroComprobante.getText().toString();

                    if(!cantidadtitular.isEmpty()&&!cantidadbeneficiario.isEmpty()&&!cantidadpagar.isEmpty()
                            &&!fecha.isEmpty()&&!observaciones.isEmpty()&&!numerocomprobante.isEmpty()&&concepto>0&&estatus>0){
                        if(cantidadtitular.length()>1){
                            if(cantidadbeneficiario.length()>1){
                                if(cantidadpagar.length()>1){
                                    if(fecha.length()==10){
                                        if(observaciones.length()>2||observaciones.toLowerCase().equals("n/a")){
                                            if(numerocomprobante.length()>2||numerocomprobante.toLowerCase().equals("n/a")){
                                                try {
                                                    if (!verificaConexion(getContext())) {
                                                        Snackbar.make(getView(), "Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
                                                    }
                                                        if (action) {
                                                            respuesta1 = hiloconexion.execute(UPDATE_GASTMEDIC, "modificar", arreglo_datos[0], cantidadtitular, cantidadbeneficiario
                                                                    , cantidadpagar, String.valueOf(concepto), comprobante, String.valueOf(estatus), fecha, observaciones, numerocomprobante).get();
                                                        } else {
                                                            respuesta1 = hiloconexion.execute(INSERT_GASTMEDIC, "insertar", String.valueOf(ID_TRABAJADOR)
                                                                    , fecha, numerocomprobante, cantidadpagar, observaciones
                                                                    , cantidadbeneficiario, cantidadtitular, String.valueOf(estatus), String.valueOf(concepto)).get();
                                                        }
                                                    if (respuesta1.equals("Gasto Medico insertado correctamente") || respuesta1.equals("Gasto Medico actualizado correctamente")) {
                                                        if(respuesta1.equals("Gasto Medico insertado correctamente")){
                                                            Notificacion("Gasto Medico Insertado","El gasto medico \n se ha insertado correctamente","Alta Exitosa");
                                                        }else{
                                                            Notificacion("Gasto Medico Modificado","El gasto medico \n se ha modificado correctamente","Modificacion Exitosa");
                                                        }
                                                        Toast.makeText(getContext(), "Se han guardado los datos correctamente", Toast.LENGTH_SHORT).show();
                                                        txtCantidadTitular.setText("");
                                                        txtCantidadBeneficiario.setText("");
                                                        txtObservaciones.setText("");
                                                        txtNumeroComprobante.setText("");
                                                        txtFecha.setText("");
                                                        txtCantidadPagar.setText("");
                                                        txtComprobante.setText("");
                                                        cmbEstatus.setSelection(0);
                                                        cmbConcepto.setSelection(0);
                                                        Intent intent = new Intent(getContext(), PrincipalActivity.class);
                                                        intent.putExtra("ID", b.getString("ID"));
                                                        intent.putExtra("NOMBRE", b.getString("NOMBRE"));
                                                        intent.putExtra("APATERNO", b.getString("APATERNO"));
                                                        intent.putExtra("AMATERNO", b.getString("AMATERNO"));
                                                        intent.putExtra("USUARIO", b.getString("USUARIO"));
                                                        intent.putExtra("CONTRASEÑA", b.getString("CONTRASEÑA"));
                                                        intent.putExtra("PERFIL", b.getString("PERFIL"));
                                                        intent.putExtra("HABILITADO", b.getString("HABILITADO"));
                                                        intent.putExtra("RFC", b.getString("RFC"));
                                                        intent.putExtra("CURP", b.getString("CURP"));
                                                        intent.putExtra("DOMICILIO", b.getString("DOMICILIO"));
                                                        intent.putExtra("ESTADO", b.getString("ESTADO"));
                                                        intent.putExtra("PAIS", b.getString("PAIS"));
                                                        intent.putExtra("NUMERO_CEDULA", b.getString("NUMERO_CEDULA"));
                                                        intent.putExtra("CORREO", b.getString("CORREO"));
                                                        intent.putExtra("TELEFONO_FIJO", b.getString("TELEFONO_FIJO"));
                                                        intent.putExtra("TELEFONO_MOVIL", b.getString("TELEFONO_MOVIL"));
                                                        intent.putExtra("GRADO_ESTUDIOS", b.getString("GRADO_ESTUDIOS"));
                                                        intent.putExtra("ENTIDAD", b.getString("ENTIDAD"));
                                                        intent.putExtra("PUESTO", b.getString("PUESTO"));
                                                        startActivity(intent);
                                                        getActivity().finish();

                                                    } else {
                                                        Toast.makeText(getContext(), "Oh no... Lo sentimos, algo ha salido mal... :( ", Toast.LENGTH_SHORT).show();
                                                        txtCantidadTitular.setText("");
                                                        txtCantidadBeneficiario.setText("");
                                                        txtObservaciones.setText("");
                                                        txtNumeroComprobante.setText("");
                                                        txtFecha.setText("");
                                                        txtCantidadPagar.setText("");
                                                        txtComprobante.setText("");
                                                        cmbEstatus.setSelection(0);
                                                        cmbConcepto.setSelection(0);
                                                        Intent intent = new Intent(getContext(), PrincipalActivity.class);
                                                        intent.putExtra("ID", b.getString("ID"));
                                                        intent.putExtra("NOMBRE", b.getString("NOMBRE"));
                                                        intent.putExtra("APATERNO", b.getString("APATERNO"));
                                                        intent.putExtra("AMATERNO", b.getString("AMATERNO"));
                                                        intent.putExtra("USUARIO", b.getString("USUARIO"));
                                                        intent.putExtra("CONTRASEÑA", b.getString("CONTRASEÑA"));
                                                        intent.putExtra("PERFIL", b.getString("PERFIL"));
                                                        intent.putExtra("HABILITADO", b.getString("HABILITADO"));
                                                        intent.putExtra("RFC", b.getString("RFC"));
                                                        intent.putExtra("CURP", b.getString("CURP"));
                                                        intent.putExtra("DOMICILIO", b.getString("DOMICILIO"));
                                                        intent.putExtra("ESTADO", b.getString("ESTADO"));
                                                        intent.putExtra("PAIS", b.getString("PAIS"));
                                                        intent.putExtra("NUMERO_CEDULA", b.getString("NUMERO_CEDULA"));
                                                        intent.putExtra("CORREO", b.getString("CORREO"));
                                                        intent.putExtra("TELEFONO_FIJO", b.getString("TELEFONO_FIJO"));
                                                        intent.putExtra("TELEFONO_MOVIL", b.getString("TELEFONO_MOVIL"));
                                                        intent.putExtra("GRADO_ESTUDIOS", b.getString("GRADO_ESTUDIOS"));
                                                        intent.putExtra("ENTIDAD", b.getString("ENTIDAD"));
                                                        intent.putExtra("PUESTO", b.getString("PUESTO"));
                                                        startActivity(intent);
                                                        getActivity().finish();
                                                    }

                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    } catch (ExecutionException e) {
                                                        e.printStackTrace();
                                                    } catch (Throwable throwable) {
                                                    throwable.printStackTrace();
                                                }
                                            }else{
                                                    txtNumeroComprobante.setText("");
                                                    Toast.makeText(getContext(),"El numero comprobante ingresado es invalido",Toast.LENGTH_SHORT).show();
                                                    View focusView = null;
                                                    focusView = txtNumeroComprobante;
                                                }
                                            }else{
                                                txtObservaciones.setText("");
                                                Toast.makeText(getContext(),"Las observacion ingresada es invalida",Toast.LENGTH_SHORT).show();
                                                View focusView = null;
                                                focusView = txtObservaciones;
                                            }
                                        }else{
                                            txtFecha.setText("");
                                            Toast.makeText(getContext(),"La fecha ingresada es invalida",Toast.LENGTH_SHORT).show();
                                            View focusView = null;
                                            focusView = txtFecha;
                                        }
                                    }else{
                                        txtCantidadPagar.setText("");
                                        Toast.makeText(getContext(),"La cantidad a pagar ingresada es invalida",Toast.LENGTH_SHORT).show();
                                        View focusView = null;
                                        focusView = txtCantidadPagar;
                                    }
                                }else{
                                    txtCantidadBeneficiario.setText("");
                                    Toast.makeText(getContext(),"La cantidad beneficiario ingresada es muy baja",Toast.LENGTH_SHORT).show();
                                    View focusView = null;
                                    focusView = txtCantidadBeneficiario;
                                }
                        }else{
                            txtCantidadTitular.setText("");
                            Toast.makeText(getContext(),"La cantidad titular ingresada es muy baja",Toast.LENGTH_SHORT).show();
                            View focusView = null;
                            focusView = txtCantidadTitular;
                        }
                    }else{
                        txtCantidadTitular.setText("");
                        Toast.makeText(getContext(),"Es necesario llenar todos los campos",Toast.LENGTH_SHORT).show();
                        View focusView = null;
                        focusView = txtCantidadTitular;
                    }
            }
        });
        if (!verificaConexion(getContext())) {
            Snackbar.make(view,"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
        }
        return view;
    }

    public void inicializaComboEstatus(){
        estatuses= new estatus();

        for (estatus e : Arraylistaestatus) {
            if (!Arraylistaestatus.isEmpty()) {
                listaestatus.add(e.getESTATUS());
            }
        }
        spinnerEstatusAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listaestatus);
        spinnerEstatusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbEstatus.setAdapter(spinnerEstatusAdapter);
    }

    public void inicializaComboConcepto(){
        t_concepto= new tipos_concepto();

        for (tipos_concepto tc : Arraylistatconcepto) {
            if (!Arraylistatconcepto.isEmpty()) {
                listaconcepto.add(tc.getCONCEPTO());
            }
        }
        spinnerConceptoAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listaconcepto);
        spinnerConceptoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbConcepto.setAdapter(spinnerConceptoAdapter);
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

    public class InsertaGastMedic extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String direccion = params[0];
            URL url = null; // url de donde queremos obtener la informacion.
            String devuelve = "";
            if(params[1].equals("insertar")) {
                try {
                    HttpURLConnection urlConn;
                    DataOutputStream printout;
                    DataInputStream input;
                    url = new URL(direccion);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.connect();
                    //Creo el Objeto JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("ID_TRABAJADOR", Integer.valueOf(params[2].trim()));
                    jsonParam.put("FECHA", Date.valueOf(params[3]));
                    jsonParam.put("NUMERO_COMPROBANTE", params[4]);
                    jsonParam.put("CANTIDAD_PAGAR", Double.valueOf(params[5]));
                    jsonParam.put("OBSERVACIONES", params[6]);
                    jsonParam.put("CANTIDAD_ACUMULADA_BENEFICIARIO", Double.valueOf(params[7]));
                    jsonParam.put("CANTIDAD_ACUMULADA_TITULAR", Double.valueOf(params[8]));
                    jsonParam.put("ESTATUS", Integer.valueOf(params[9]));
                    jsonParam.put("CONCEPTO", Integer.valueOf(params[10]));
                    // Envio los parámetros post.
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    int respuesta = urlConn.getResponseCode();
                    StringBuilder result = new StringBuilder();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            result.append(line);
                            //response+=line;
                        }
                        //Creo un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados
                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON
                        if (resultJSON.trim().equals("1")) {      // Se ha insertado correctamente el Gasto Medico
                            devuelve = "Gasto Medico insertado correctamente";
                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "Gasto Medico no pudo insertarse";
                        }
                    }
                } catch (MalformedURLException e) {
                    System.out.println("Error en InsertarWebService :" + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Error en InsertarWebService :" + e.getMessage());
                } catch (JSONException e) {
                    System.out.println("Error en InsertarWebService :" + e.getMessage()+"/n"+e.getCause());
                }
                return devuelve;
            }else if(params[1].equals("modificar")){
                try {
                    HttpURLConnection urlConn;
                    DataOutputStream printout;
                    DataInputStream input;
                    url = new URL(direccion);
                    urlConn = (HttpURLConnection) url.openConnection();
                    urlConn.setDoInput(true);
                    urlConn.setDoOutput(true);
                    urlConn.setUseCaches(false);
                    urlConn.setRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Accept", "application/json");
                    urlConn.connect();
                    //Creo el Objeto JSON
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("ID", Integer.valueOf(params[2].trim()));
                    jsonParam.put("CANTIDAD_ACUMULADA_TITULAR", Double.valueOf(params[3]));
                    jsonParam.put("CANTIDAD_ACUMULADA_BENEFICIARIO", Double.valueOf(params[4]));
                    jsonParam.put("CANTIDAD_PAGAR", Double.valueOf(params[5]));
                    jsonParam.put("CONCEPTO", Integer.valueOf(params[6]));
                    jsonParam.put("COMPROBANTE",null);
                    jsonParam.put("ESTATUS", Integer.valueOf(params[8]));
                    jsonParam.put("FECHA", Date.valueOf(params[9]));
                    jsonParam.put("OBSERVACIONES", params[10]);
                    jsonParam.put("NUMERO_COMPROBANTE", params[11]);
                    // Envio los parámetros post.
                    OutputStream os = urlConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(jsonParam.toString());
                    writer.flush();
                    writer.close();

                    int respuesta = urlConn.getResponseCode();

                    StringBuilder result = new StringBuilder();
                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        String line;
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                        while ((line = br.readLine()) != null) {
                            result.append(line);
                            //response+=line;
                        }
                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados
                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON
                        if (resultJSON.trim().equals("1")) {      // hay un beneficiario que mostrar
                            devuelve = "Gasto Medico actualizado correctamente";
                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "El Gasto Medico no pudo actualizarse";
                        }
                    }
                } catch (MalformedURLException e) {
                    System.out.println("Error en UpdateWebService :" + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Error en UpdateWebService :" + e.getMessage());
                } catch (JSONException e) {
                    System.out.println("Error en UpdateWebService :" + e.getMessage()+"/n"+e.getCause());
                }
                return devuelve;
            }
            return null;
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

    public class obtenerEstatus extends AsyncTask<String, Void, String> {

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

                    if (resultJSON.trim().equals("1")) {      // hay estatus a mostrar
                        JSONArray usersJSON = respuestaJSON.getJSONArray("estatus");   // estado es el nombre del campo en el JSON
                        com.example.manuel.serviciostsj.dto.estatus itemSeleccionar= new estatus(0,"Selecciona el Estatus");
                        Arraylistaestatus.add(itemSeleccionar);
                        for (int i = 0; i < usersJSON.length(); i++) {
                            estatuses = new estatus(usersJSON.getJSONObject(i).getInt("ID")
                                    , usersJSON.getJSONObject(i).getString("ESTATUS"));
                            Arraylistaestatus.add(estatuses);
                            devuelve="Hay estatus";
                        }
                    } else if (resultJSON.trim().equals("2")) {
                        devuelve = "No hay estatus";
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

    public class obtenerConcepto extends AsyncTask<String, Void, String> {
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

                    if (resultJSON.trim().equals("1")) {      // hay tipos concepto a mostrar
                        JSONArray usersJSON = respuestaJSON.getJSONArray("tiposconcepto");   // tiposconcepto es el nombre del campo en el JSON
                        tipos_concepto itemSeleccionar= new tipos_concepto(0,"Selecciona el Concepto");
                        Arraylistatconcepto.add(itemSeleccionar);
                        for (int i = 0; i < usersJSON.length(); i++) {
                            t_concepto = new tipos_concepto(usersJSON.getJSONObject(i).getInt("ID")
                                    , usersJSON.getJSONObject(i).getString("CONCEPTO"));
                            Arraylistatconcepto.add(t_concepto);
                            devuelve="Hay tipos concepto";
                        }

                    } else if (resultJSON.trim().equals("2")) {
                        devuelve = "No hay tipos concepto";
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

    public class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView parent, View view, int pos,long id) {
            switch (parent.getId()){
                case R.id.cmbEstatus: //Toast.makeText(getContext(),"Se ha seleccionado el estatus item :"+pos,Toast.LENGTH_SHORT).show();
                    estatus=pos;
                    break;
                case R.id.cmbTiposConcepto: //Toast.makeText(getContext(),"Se ha seleccionado el concepto item :"+pos,Toast.LENGTH_SHORT).show();
                    concepto=pos;
                    break;
                default:
                    break;
            }
            //Podemos hacer varios ifs o un switchs por si tenemos varios spinners.
        }
        public void onNothingSelected(AdapterView parent) {
            // Do nothing.
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

    public void Notificacion(String title, String text,String ticker){
        int count=1;
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.drawable.banner_principal)
                .setLargeIcon((((BitmapDrawable)getResources()
                        .getDrawable(R.drawable.ic_beenhere_black_24dp)).getBitmap()))
                .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(text)
                .setContentInfo(String.valueOf(count))
                .setTicker(ticker);
        count++;
        Intent notIntent =
                new Intent(getContext(), getClass());

        PendingIntent contIntent =
                PendingIntent.getActivity(
                        getContext(), 0, notIntent, 0);

        mBuilder.setContentIntent(contIntent);

        mNotificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIF_ALERTA_ID, mBuilder.build());
    }

}