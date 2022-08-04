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
import com.example.manuel.serviciostsj.dto.tipos_parentesco;

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
 * {@link AltaBenefiFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AltaBenefiFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public AltaBenefiFragment() {
        // Required empty public constructor
    }

    Bundle b;
    EditText txtNombre,txtApaterno,txtAmaterno,txtCurp,txtDomicilio,txtMunicipio,txtFechaNacimiento,
            txtCorreo,txtTelefonoMovil,txtTelefonoFijo,txtGradoEstudios,txtEspecialidad,txtObservaciones;
    Spinner cmbEstatus,cmbParentesco;
    Button btnAgregar;
    estatus estatuses=null;
    tipos_parentesco t_parentesco;

    //Arreglo de lista para insertar en la clase
    ArrayList<estatus> Arraylistaestatus= new ArrayList<estatus>();
    ArrayList<tipos_parentesco> Arraylistatparentesco= new ArrayList<tipos_parentesco>();

    String nombre="",apaterno="",amaterno="",curp="",domicilio="",municipio="",fechaNacimiento="",
            correo="",telefonomovil="",telefonofijo="",gradoestudios="",especialidad="",observaciones="";
    int estatus=0,parentesco=0,ID_TRABAJADOR=0;

    //Direcion (URL) del servicio al que me voy a conectar
    String IP= "http://servicios-tsj.gob.mx";
    String GET_PAREN= IP+"/wsobtener_tiposparentesco.php";
    String GET_ESTATUS= IP+"/wsobtener_estatus.php";
    String INSERT_BENE= IP+"/wsinsert_beneficiario.php";
    String UPDATE_BENE= IP+"/wsupdate_beneficiario.php";

    //Lista de String para los items del spinner
    ArrayList<String> listaestatus= new ArrayList<String>();
    ArrayList<String> listatparentescos= new ArrayList<String>();
    //Adaptador de lista de String de items para el spinner
    ArrayAdapter<String> spinnerEstatusAdapter=null;
    ArrayAdapter<String> spinnerParentescoAdapter=null;
    // Hilos de conexion de los web service
    InsertaBeneficiario hiloconexion=null;
    obtenerEstatus hiloestatus=null;
    obtenerParentesco hiloparetesco=null;

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
        hiloconexion = new InsertaBeneficiario();
        hiloestatus = new obtenerEstatus();
        hiloparetesco = new obtenerParentesco();
        try {
            respuesta2= hiloestatus.execute(GET_ESTATUS).get();
            respuesta3= hiloparetesco.execute(GET_PAREN).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_altas_benefi, container, false);
        txtNombre = (EditText) view.findViewById(R.id.txtNombre);
        txtApaterno = (EditText) view.findViewById(R.id.txtApaterno);
        txtAmaterno = (EditText) view.findViewById(R.id.txtAmaterno);
        txtCurp = (EditText) view.findViewById(R.id.txtCurp);
        txtDomicilio = (EditText) view.findViewById(R.id.txtDomicilio);
        txtMunicipio = (EditText) view.findViewById(R.id.txtMunicipio);
        txtFechaNacimiento = (EditText) view.findViewById(R.id.txtFechaNacimiento);
        txtFechaNacimiento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fecha = year + "-" + (month + 1) + "-" + dayOfMonth;
                        txtFechaNacimiento.setText(fecha);
                    }
                },dia,mes,ano);
                datePickerDialog.show();
            }
        });
        txtCorreo = (EditText) view.findViewById(R.id.txtCorreo);
        txtTelefonoMovil = (EditText) view.findViewById(R.id.txtTelefonoMovil);
        txtTelefonoFijo = (EditText) view.findViewById(R.id.txtTelefonoFijo);
        txtGradoEstudios = (EditText) view.findViewById(R.id.txtGradoEstudios);
        txtEspecialidad = (EditText) view.findViewById(R.id.txtEspecialidad);
        txtObservaciones = (EditText) view.findViewById(R.id.txtObservaciones);
        cmbEstatus = (Spinner) view.findViewById(R.id.cmbEstatus);
        cmbEstatus.setOnItemSelectedListener(new MyOnItemSelectedListener());
        cmbParentesco = (Spinner) view.findViewById(R.id.cmbParentesco);
        cmbParentesco.setOnItemSelectedListener(new MyOnItemSelectedListener());
        btnAgregar = (Button) view.findViewById(R.id.btnAgregar);

        if(respuesta2.equals("Hay estatus")){
            inicializaComboEstatus();
        }
        if(respuesta3.equals("Hay tipos parentesco")){
            inicializaComboParentesco();
        }

        b = getArguments();
        if(b!=null){
            //ID del trabajador de quien da de alta nuevo registro
            // es el actualmente loggeado en la aplicacion
            ID_TRABAJADOR= Integer.valueOf(b.getString("ID"));
            action = b.getString("accion").equals("modificar");
            if(action){
                btnAgregar.setText("Actualizar");
                arreglo_datos = new String[b.getStringArray("editables").length];
                arreglo_datos = b.getStringArray("editables");
                txtNombre.setText(arreglo_datos[1]);
                txtNombre.setEnabled(false);
                txtApaterno.setText(arreglo_datos[2]);
                txtApaterno.setEnabled(false);
                txtAmaterno.setText(arreglo_datos[3]);
                txtAmaterno.setEnabled(false);
                txtCurp.setText(arreglo_datos[4]);
                txtCurp.setEnabled(false);
                txtDomicilio.setText(arreglo_datos[5]);
                txtMunicipio.setText(arreglo_datos[6]);
                txtFechaNacimiento.setText(arreglo_datos[7]);
                txtFechaNacimiento.setEnabled(false);
                txtCorreo.setText(arreglo_datos[8]);
                txtTelefonoMovil.setText(arreglo_datos[9]);
                txtTelefonoFijo.setText(arreglo_datos[10]);
                txtGradoEstudios.setText(arreglo_datos[11]);
                txtEspecialidad.setText(arreglo_datos[14]);
                txtObservaciones.setText(arreglo_datos[15]);
            }
        }

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nombre=txtNombre.getText().toString();
                apaterno=txtApaterno.getText().toString();
                amaterno=txtAmaterno.getText().toString();
                curp=txtCurp.getText().toString();
                domicilio=txtDomicilio.getText().toString();
                municipio=txtMunicipio.getText().toString();
                fechaNacimiento=txtFechaNacimiento.getText().toString();
                correo=txtCorreo.getText().toString();
                telefonomovil=txtTelefonoMovil.getText().toString();
                telefonofijo=txtTelefonoFijo.getText().toString();
                gradoestudios=txtGradoEstudios.getText().toString();
                especialidad=txtEspecialidad.getText().toString();
                observaciones=txtObservaciones.getText().toString();

                if(!nombre.isEmpty()&&!apaterno.isEmpty()&&!amaterno.isEmpty()&&!curp.isEmpty()&&!domicilio.isEmpty()
                        &&!municipio.isEmpty()&&!fechaNacimiento.isEmpty()&&!correo.isEmpty()&&estatus>0&&parentesco>0){
                    if(nombre.length()>2) {
                        if(apaterno.length()>3) {
                            if(amaterno.length()>3) {
                                if(curp.length()==18||curp.toLowerCase().equals("n/a")) {
                                    if(domicilio.length()>5||domicilio.toLowerCase().equals("n/a")) {
                                        if(municipio.length()>5||municipio.toLowerCase().equals("n/a")) {
                                            if(correo.contains("@")&&correo.contains(".com")&&correo.length()>10||correo.toLowerCase().equals("n/a")) {
                                                if(!telefonomovil.isEmpty()&&telefonomovil.length()>9&&telefonomovil.length()<14||telefonomovil.toLowerCase().equals("n/a")) {
                                                    if(!telefonofijo.isEmpty()&&telefonofijo.length()>6&&telefonofijo.length()<14||telefonofijo.toLowerCase().equals("n/a")) {
                                                        try {
                                                            if (!verificaConexion(getContext())) {
                                                                Snackbar.make(getView(),"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
                                                            }else {
                                                                if (action) {
                                                                    respuesta1 = hiloconexion.execute(UPDATE_BENE, "modificar", arreglo_datos[0], nombre, apaterno, amaterno, curp
                                                                            , domicilio, municipio, fechaNacimiento, correo, telefonomovil, telefonofijo, gradoestudios
                                                                            , String.valueOf(estatus), String.valueOf(parentesco), especialidad, observaciones).get();
                                                                } else {
                                                                    respuesta1 = hiloconexion.execute(INSERT_BENE, "insertar", String.valueOf(ID_TRABAJADOR)
                                                                            , String.valueOf(parentesco), curp, String.valueOf(estatus), nombre
                                                                            , apaterno, amaterno, observaciones, correo, fechaNacimiento, especialidad
                                                                            , gradoestudios, telefonomovil, telefonofijo, municipio, domicilio).get();
                                                                }
                                                                if (respuesta1.equals("Beneficiario insertado correctamente") || respuesta1.equals("Beneficiario actualizado correctamente")) {
                                                                    if(respuesta1.equals("Beneficiario insertado correctamente")){
                                                                        Notificacion("Beneficiario Insertado","El beneficiario \n se ha insertado correctamente","Alta Exitosa");
                                                                    }else{
                                                                        Notificacion("Beneficiario Modificado","El beneficiario \n se ha modificado correctamente","Modificacion Exitosa");
                                                                    }
                                                                    txtNombre.setText("");
                                                                    txtApaterno.setText("");
                                                                    txtAmaterno.setText("");
                                                                    txtObservaciones.setText("");
                                                                    txtEspecialidad.setText("");
                                                                    txtCorreo.setText("");
                                                                    txtTelefonoFijo.setText("");
                                                                    txtTelefonoMovil.setText("");
                                                                    txtCurp.setText("");
                                                                    txtDomicilio.setText("");
                                                                    txtGradoEstudios.setText("");
                                                                    txtFechaNacimiento.setText("");
                                                                    txtMunicipio.setText("");
                                                                    cmbParentesco.setSelection(0);
                                                                    cmbEstatus.setSelection(0);
                                                                    Toast.makeText(getContext(), "Se han guardado los datos correctamente", Toast.LENGTH_SHORT).show();
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
                                                                } else {
                                                                    Toast.makeText(getContext(), "Oh no... Lo sentimos, algo ha salido mal... :( ", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        } catch (ExecutionException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }else{
                                                        txtTelefonoFijo.setText("");
                                                        Toast.makeText(getContext(),"El telefono fijo ingresado es invalido",Toast.LENGTH_SHORT).show();
                                                        View focusView = null;
                                                        focusView = txtTelefonoFijo;
                                                    }
                                                }else{
                                                    txtTelefonoMovil.setText("");
                                                    Toast.makeText(getContext(),"El telefono movil ingresado es invalido",Toast.LENGTH_SHORT).show();
                                                    View focusView = null;
                                                    focusView = txtTelefonoMovil;
                                                }
                                            }else{
                                                txtCorreo.setText("");
                                                Toast.makeText(getContext(),"El correo ingresado es invalido, debe llevar @ y/o .com",Toast.LENGTH_SHORT).show();
                                                View focusView = null;
                                                focusView = txtCorreo;
                                            }
                                        }else{
                                            txtMunicipio.setText("");
                                            Toast.makeText(getContext(),"El municipio ingresado es invalido",Toast.LENGTH_SHORT).show();
                                            View focusView = null;
                                            focusView = txtMunicipio;
                                        }
                                    }else{
                                        txtDomicilio.setText("");
                                        Toast.makeText(getContext(),"El domicilio ingresado es invalido",Toast.LENGTH_SHORT).show();
                                        View focusView = null;
                                        focusView = txtDomicilio;
                                    }
                                }else{
                                    txtCurp.setText("");
                                    Toast.makeText(getContext(),"El curp es invalido, debe tener 18 caracteres",Toast.LENGTH_SHORT).show();
                                    View focusView = null;
                                    focusView = txtCurp;
                                }
                            }else{
                                txtAmaterno.setText("");
                                Toast.makeText(getContext(),"El apellido materno ingresado es invalido",Toast.LENGTH_SHORT).show();
                                View focusView = null;
                                focusView = txtAmaterno;
                            }
                        }else{
                            txtApaterno.setText("");
                            Toast.makeText(getContext(),"El apellido paterno ingresado es invalido",Toast.LENGTH_SHORT).show();
                            View focusView = null;
                            focusView = txtApaterno;
                        }
                    }
                    else{
                        txtNombre.setText("");
                        Toast.makeText(getContext(),"El nombre ingresado es invalido",Toast.LENGTH_SHORT).show();
                        View focusView = null;
                        focusView = txtNombre;
                    }
                }else{
                    txtNombre.setText("");
                    Toast.makeText(getContext(),"Es necesario llenar los campos",Toast.LENGTH_SHORT).show();
                    View focusView = null;
                    focusView = txtNombre;
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

    public void inicializaComboParentesco(){
        t_parentesco= new tipos_parentesco();

        for (tipos_parentesco tp : Arraylistatparentesco) {
            if (!Arraylistatparentesco.isEmpty()) {
                listatparentescos.add(tp.getDESCRIPCION());
            }
        }
        spinnerParentescoAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, listatparentescos);
        spinnerParentescoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cmbParentesco.setAdapter(spinnerParentescoAdapter);
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

    public class InsertaBeneficiario extends AsyncTask<String, Void, String> {

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
                    jsonParam.put("CURSO","N/A");
                    jsonParam.put("RFC_TRABAJADOR", "");
                    jsonParam.put("RFC_BENEFICIARIO", "");
                    jsonParam.put("PARENTESCO", Integer.valueOf(params[3]));
                    jsonParam.put("CURP", params[4]);
                    jsonParam.put("ESTATUS", Integer.valueOf(params[5]));
                    jsonParam.put("NOMBRE", params[6]);
                    jsonParam.put("APELLIDO_PATERNO", params[7]);
                    jsonParam.put("APELLIDO_MATERNO", params[8]);
                    jsonParam.put("OBSERVACIONES", params[9]);
                    jsonParam.put("CONSTANCIA", "N/A");
                    jsonParam.put("CORREO", params[10]);
                    jsonParam.put("FECHA_ACT", "N/A");
                    jsonParam.put("FECHA_NACIMIENTO", Date.valueOf(params[11]));
                    jsonParam.put("NUMERO_CEDULA", "N/A");
                    jsonParam.put("ESPECIALIDAD", params[12]);
                    jsonParam.put("GRADO_ESTUDIOS", params[13]);
                    jsonParam.put("TELEFONO_MOVIL", params[14]);
                    jsonParam.put("TELEFONO_FIJO", params[15]);
                    jsonParam.put("PAIS", "México");
                    jsonParam.put("ESTADO", 29);
                    jsonParam.put("MUNICIPIO", params[16]);
                    jsonParam.put("DOMICILIO", params[17]);
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
                        if (resultJSON.trim().equals("1")) {      // Se ha insertado correctamente el Beneficiario
                            devuelve = "Beneficiario insertado correctamente";
                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "Beneficiario no pudo insertarse";
                        }
                    }
                } catch (MalformedURLException e) {
                    System.out.println("Error en InsertarWebService :" + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Error en InsertarWebService :" + e.getMessage());
                } catch (JSONException e) {
                    System.out.println("Error en InsertarWebService :" + e.getMessage());
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
                    jsonParam.put("NOMBRE", params[3]);
                    jsonParam.put("APELLIDO_PATERNO", params[4]);
                    jsonParam.put("APELLIDO_MATERNO", params[5]);
                    jsonParam.put("CURP", params[6]);
                    jsonParam.put("DOMICILIO", params[7]);
                    jsonParam.put("MUNICIPIO", params[8]);
                    jsonParam.put("FECHA_NACIMIENTO", Date.valueOf(params[9]));
                    jsonParam.put("CORREO", params[10]);
                    jsonParam.put("TELEFONO_MOVIL", params[11]);
                    jsonParam.put("TELEFONO_FIJO", params[12]);
                    jsonParam.put("GRADO_ESTUDIOS", params[13]);
                    jsonParam.put("ESTATUS", Integer.valueOf(params[14]));
                    jsonParam.put("PARENTESCO", Integer.valueOf(params[15]));
                    jsonParam.put("ESPECIALIDAD", params[16]);
                    jsonParam.put("OBSERVACIONES", params[17]);
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
                            devuelve = "Beneficiario actualizado correctamente";
                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "El Beneficiario no pudo actualizarse";
                        }
                    }
                } catch (MalformedURLException e) {
                    System.out.println("Error en UpdateWebService :" + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Error en UpdateWebService :" + e.getMessage());
                } catch (JSONException e) {
                    System.out.println("Error en UpdateWebService :" + e.getMessage());
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
                            estatus itemSeleccionar= new estatus(0,"Selecciona el Estatus");
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

    public class obtenerParentesco extends AsyncTask<String, Void, String> {
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
                        if (resultJSON.trim().equals("1")) {      // hay tipos parentesco a mostrar
                            JSONArray usersJSON = respuestaJSON.getJSONArray("tiposparentesco");   // tiposparentesco es el nombre del campo en el JSON
                            tipos_parentesco itemSeleccionar= new tipos_parentesco(0,"Selecciona el Parentesco");
                            Arraylistatparentesco.add(itemSeleccionar);
                            for (int i = 0; i < usersJSON.length(); i++) {
                                t_parentesco = new tipos_parentesco(usersJSON.getJSONObject(i).getInt("ID")
                                        , usersJSON.getJSONObject(i).getString("DESCRIPCION"));
                                Arraylistatparentesco.add(t_parentesco);
                                devuelve="Hay tipos parentesco";
                            }
                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "No hay tipos parentesco";
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
                case R.id.cmbEstatus: Toast.makeText(getContext(),"Se ha seleccionado el estatus item :"+pos,Toast.LENGTH_SHORT).show();
                    estatus=pos;
                    break;
                case R.id.cmbParentesco: Toast.makeText(getContext(),"Se ha seleccionado el parentesco item :"+pos,Toast.LENGTH_SHORT).show();
                    parentesco=pos;
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
