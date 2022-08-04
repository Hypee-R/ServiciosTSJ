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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.manuel.serviciostsj.R;
import com.example.manuel.serviciostsj.activity.PrincipalActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AltaHistcuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AltaHistcuFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    public AltaHistcuFragment() {
        // Required empty public constructor
    }

    // Url del servicio web al que me voy a conectar
    static String IP= "http://servicios-tsj.gob.mx";
    //Rutas de los web services
    static String INSERT_HIST_CU = IP + "/wsinsert_histcu.php";
    static String UPDATE_HIST_CU = IP + "/wsupdate_histcu.php";
    InsertaHistCurricular hiloHisCu=null;
    Bundle b;
    int dia,mes,ano;
    String respuesta="";
    boolean accion=false;
    int ID_TRABAJADOR=0;
    EditText txtNcurso,txtCalif,txtNinstit,txtLugarCurso,txtDuracionCurso,txtFechaInicio,txtFechaFin,txtQuienAut,txtObserv;
    String ncurso,ncalif,ninst,nlugarcurso,ndurcurso,nfechainicio,nfechafin,nquienaut,nobser;
    Button btnAgregar;
    String[] arreglo_datos= null;
    private NotificationManager mNotificationManager;
    private int NOTIF_ALERTA_ID = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hiloHisCu = new InsertaHistCurricular();
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_alta_histcu, container, false);
        txtNcurso= (EditText) view.findViewById(R.id.txtCursoTomado);
        txtCalif= (EditText) view.findViewById(R.id.txtCalificacion);
        txtNinstit= (EditText) view.findViewById(R.id.txtIntitucion);
        txtLugarCurso= (EditText) view.findViewById(R.id.txtLugarCurso);
        txtDuracionCurso= (EditText) view.findViewById(R.id.txtDuracionCurso);
        txtFechaInicio= (EditText) view.findViewById(R.id.txtFechaInicio);
        txtFechaInicio.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                final Calendar c= Calendar.getInstance();
            dia= c.get(Calendar.DAY_OF_MONTH);
            mes= c.get(Calendar.MONTH);
            ano= c.get(Calendar.YEAR);

            DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String fecha = year + "-" + (month + 1) + "-" + dayOfMonth;
                    txtFechaInicio.setText(fecha);
                }
            },dia,mes,ano);
                datePickerDialog.show();
            }
        });

        txtFechaFin= (EditText) view.findViewById(R.id.txtFechaFin);

        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c= Calendar.getInstance();
                dia= c.get(Calendar.DAY_OF_MONTH);
                mes= c.get(Calendar.MONTH);
                ano= c.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog= new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String fecha = year + "-" + (month + 1) + "-" + dayOfMonth;
                        txtFechaFin.setText(fecha);
                    }
                },dia,mes,ano);
                datePickerDialog.show();
            }
        });
        txtQuienAut= (EditText) view.findViewById(R.id.txtQuienAutoriza);
        txtObserv= (EditText) view.findViewById(R.id.txtObservaciones);
        btnAgregar= (Button) view .findViewById(R.id.btnAgregar);

        b= getArguments();
        if(b!=null){
            //ID del trabajador de quien da de alta nuevo registro
            // es el actualmente loggeado en la aplicacion
            ID_TRABAJADOR= Integer.valueOf(b.getString("ID"));
            accion= b.getString("accion").equals("modificar");
            if(accion) {
                btnAgregar.setText("Actualizar");
                arreglo_datos = new String[b.getStringArray("editables").length];
                arreglo_datos = b.getStringArray("editables");
                txtNcurso.setText(arreglo_datos[1]);
                txtCalif.setText(arreglo_datos[2]);
                txtNinstit.setText(arreglo_datos[3]);
                txtLugarCurso.setText(arreglo_datos[4]);
                txtDuracionCurso.setText(arreglo_datos[5]);
                txtFechaInicio.setText(arreglo_datos[6]);
                txtFechaFin.setText(arreglo_datos[7]);
                txtQuienAut.setText(arreglo_datos[8]);
                txtObserv.setText(arreglo_datos[9]);
            }
        }
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ncurso= txtNcurso.getText().toString();
                ncalif= txtCalif.getText().toString();
                ninst= txtNinstit.getText().toString();
                nlugarcurso= txtLugarCurso.getText().toString();
                ndurcurso= txtDuracionCurso.getText().toString();
                nfechainicio= txtFechaInicio.getText().toString();
                nfechafin= txtFechaFin.getText().toString();
                nquienaut= txtQuienAut.getText().toString();
                nobser= txtObserv.getText().toString();
                if(!ncurso.equals("")&&!ncalif.equals("")&&!ninst.equals("")&&!nlugarcurso.equals("")&&!ndurcurso.equals("")
                        &&!nfechainicio.equals("")&&!nfechafin.equals("")&&!nquienaut.equals("")&&!nobser.equals("")){
                    if(ncurso.length()>3||ncurso.toLowerCase().equals("n/a")) {
                        if(ncalif.length()>0||ncalif.toLowerCase().equals("n/a")) {
                            if(ninst.length()>5||ninst.toLowerCase().equals("n/a")) {
                                if(nlugarcurso.length()>5||nlugarcurso.toLowerCase().equals("n/a")) {
                                    if(ndurcurso.length()>5||ndurcurso.toLowerCase().equals("n/a")) {
                                        if(nfechainicio.length()==10||nfechainicio.toLowerCase().equals("n/a")) {
                                            if(nfechafin.length()==10||nfechafin.toLowerCase().equals("n/a")) {
                                                if(nquienaut.length()>1||nquienaut.toLowerCase().equals("n/a")) {
                                                    if(nobser.length()>2||nobser.toLowerCase().equals("n/a")) {
                                                        try {
                                                            if (!verificaConexion(getContext())) {
                                                                Snackbar.make(getView(),"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
                                                            }else {
                                                                if (accion) {
                                                                    respuesta = hiloHisCu.execute(UPDATE_HIST_CU, "modificar", arreglo_datos[0], ncurso, nfechainicio, nfechafin, ndurcurso, ninst, nlugarcurso, nquienaut, ncalif, nobser).get();
                                                                } else {
                                                                    respuesta = hiloHisCu.execute(INSERT_HIST_CU, "insertar", String.valueOf(ID_TRABAJADOR), ncurso, nfechainicio, nfechafin, ndurcurso, ninst, nlugarcurso, nquienaut, ncalif, nobser).get();
                                                                }
                                                                if (respuesta.equals("Historia Curricular insertada correctamente") || respuesta.equals("HistCu actualizada correctamente")) {
                                                                    if(respuesta.equals("Historia Curricular insertada correctamente")){
                                                                        Notificacion("Historia Curricular Insertada","La historia curricular \n se ha insertado correctamente","Alta Exitosa");
                                                                    }else{
                                                                        Notificacion("Historia Curricular Modificada","La historia curricular \n se ha modificado correctamente","Modificacion Exitosa");
                                                                    }
                                                                    txtNcurso.setText("");
                                                                    txtObserv.setText("");
                                                                    txtQuienAut.setText("");
                                                                    txtFechaFin.setText("");
                                                                    txtFechaInicio.setText("");
                                                                    txtCalif.setText("");
                                                                    txtDuracionCurso.setText("");
                                                                    txtLugarCurso.setText("");
                                                                    txtNinstit.setText("");
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
                                                        txtObserv.setText("");
                                                        Toast.makeText(getContext(),"Las Observaciones ingresadas son invalidas",Toast.LENGTH_SHORT).show();
                                                        View focusView = null;
                                                        focusView = txtObserv;
                                                    }
                                                }else{
                                                    txtQuienAut.setText("");
                                                    Toast.makeText(getContext(),"El campo Quien Autoriza ingresado es invalido",Toast.LENGTH_SHORT).show();
                                                    View focusView = null;
                                                    focusView = txtCalif;
                                                }
                                            }else{
                                                txtFechaFin.setText("");
                                                Toast.makeText(getContext(),"La fecha fin ingresada es invalida",Toast.LENGTH_SHORT).show();
                                                View focusView = null;
                                                focusView = txtFechaFin;
                                            }
                                        }else{
                                            txtFechaInicio.setText("");
                                            Toast.makeText(getContext(),"La fecha  de inicio ingresada es invalida",Toast.LENGTH_SHORT).show();
                                            View focusView = null;
                                            focusView = txtFechaInicio;
                                        }
                                    }else{
                                        txtDuracionCurso.setText("");
                                        Toast.makeText(getContext(),"La duracion del curso ingresada es invalida",Toast.LENGTH_SHORT).show();
                                        View focusView = null;
                                        focusView = txtDuracionCurso;
                                    }
                                }else{
                                    txtLugarCurso.setText("");
                                    Toast.makeText(getContext(),"El lugar del curso ingresado es invalido",Toast.LENGTH_SHORT).show();
                                    View focusView = null;
                                    focusView = txtLugarCurso;
                                }
                            }else{
                                txtNinstit.setText("");
                                Toast.makeText(getContext(),"El nombre de la institucion ingresada es invalida",Toast.LENGTH_SHORT).show();
                                View focusView = null;
                                focusView = txtNinstit;
                            }
                        }else{
                            txtCalif.setText("");
                            Toast.makeText(getContext(),"La calificacion ingresada es invalida",Toast.LENGTH_SHORT).show();
                            View focusView = null;
                            focusView = txtCalif;
                        }
                    }else{
                        txtNcurso.setText("");
                        Toast.makeText(getContext(),"El nombre del curso es invalido",Toast.LENGTH_SHORT).show();
                        View focusView = null;
                        focusView = txtNcurso;
                    }
                }else{
                    txtNcurso.setText("");
                    Toast.makeText(getContext(),"Es necesario llenar todos los campos",Toast.LENGTH_SHORT).show();
                    View focusView = null;
                    focusView = txtNcurso;
                }
            }
        });
        if (!verificaConexion(getContext())) {
            Snackbar.make(view,"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
        }
        return view;
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

    public class InsertaHistCurricular extends AsyncTask<String, Void, String> {

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
                    jsonParam.put("ID_TRABAJADOR", Integer.valueOf(params[1].trim()));
                    jsonParam.put("CURSO", params[2]);
                    jsonParam.put("FECHA_INICIO", Date.valueOf(params[3]));
                    jsonParam.put("FECHA_FIN", Date.valueOf(params[4]));
                    jsonParam.put("DURACION", params[5]);
                    jsonParam.put("INSTITUCION_EXPIDE", params[6]);
                    jsonParam.put("LUGAR_EXPEDICION", params[7]);
                    jsonParam.put("QUIEN_AUTORIZA", params[8]);
                    jsonParam.put("CALIFICACION", params[9]);
                    jsonParam.put("OBSERVACIONES", params[10]);
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

                        if (resultJSON.trim().equals("1")) {      // Se ha insertado correctamente la Historia Curricular
                            devuelve = "Historia Curricular insertada correctamente";

                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "Historia Curricular no pudo insertarse";
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
                    jsonParam.put("ID", params[2]);
                    jsonParam.put("CURSO", params[3]);
                    jsonParam.put("FECHA_INICIO", params[4]);
                    jsonParam.put("FECHA_FIN", params[5]);
                    jsonParam.put("DURACION", params[6]);
                    jsonParam.put("INSTITUCION_EXPIDE", params[7]);
                    jsonParam.put("LUGAR_EXPEDICION", params[8]);
                    jsonParam.put("QUIEN_AUTORIZA", params[9]);
                    jsonParam.put("CALIFICACION", params[10]);
                    jsonParam.put("OBSERVACIONES", params[11]);
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

                        if (resultJSON.trim().equals("1")) {      // hay un usuario que mostrar
                            devuelve = "HistCu actualizada correctamente";

                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "La HistCu no pudo actualizarse";
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
