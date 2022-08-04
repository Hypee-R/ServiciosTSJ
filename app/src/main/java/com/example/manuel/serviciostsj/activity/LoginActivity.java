package com.example.manuel.serviciostsj.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.manuel.serviciostsj.R;
import com.example.manuel.serviciostsj.dto.usuarios;

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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

@RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
public class LoginActivity extends AppCompatActivity {

    static usuarios users=null;
    static ArrayList<usuarios> listusuarios= new ArrayList<usuarios>();
    // Url del servicio web al que me voy a conectar
    static String IP= "http://servicios-tsj.gob.mx";
    //Rutas de los web services
    static String GET = IP + "/wsobtener_users.php";
    static String GET_BY_ID = IP + "/wsobtener_users_id.php";
    static String UPDATE = IP + "/wsupdate_users.php";
    static String DELETE = IP + "/wsdelete_users.php";
    static String INSERT = IP + "/wsinsert_users.php";
    static String usuario="", contraseña="";
    ObtenerWebService hiloconexion;
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUsuarioView;
    private EditText mPasswordView;
    private ProgressBar mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsuarioView = (AutoCompleteTextView) findViewById(R.id.txtusuario);
        //populateAutoComplete();
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = (ProgressBar) findViewById(R.id.login_progress);
        mProgressView.getIndeterminateDrawable()
                .setColorFilter(Color.rgb(126,0,23), PorterDuff.Mode.SRC_IN);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button SignInButton = (Button) findViewById(R.id.sign_in_button);
        SignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario= mUsuarioView.getText().toString();
                contraseña= mPasswordView.getText().toString();
                if (!verificaConexion(getApplicationContext())) {
                    Snackbar.make(findViewById(android.R.id.content),"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
                }else {
                    if (!usuario.equals("") && !contraseña.equals("")) {
                        if (isEmailValid(usuario) && isPasswordValid(contraseña)) {
                            hiloconexion = new ObtenerWebService();
                            try {
                                showProgress(true);
                                mAuthTask = new UserLoginTask(usuario, contraseña);
                                mAuthTask.execute((Void) null);
                                String respuesta = hiloconexion.execute(GET, "1", usuario, contraseña).get(); //Parametros que recive doInBackground
                                if (respuesta.equals("no hay usuarios con ese nombre")) {
                                    Toast.makeText(getApplicationContext(), "El usuario oh contraseña son incorrectos", Toast.LENGTH_SHORT).show();
                                    View focusView = null;
                                    focusView = mUsuarioView;
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            mUsuarioView.setText("");
                            mPasswordView.setText("");
                        } else {
                            attemptLogin();
                        }
                    } else {
                        if (usuario.equals("") && contraseña.equals("")) {
                            attemptLogin();
                            Toast.makeText(getApplicationContext(), "Son necesarios los campos llenos", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        if (!verificaConexion(this)) {
            Snackbar.make(findViewById(android.R.id.content),"Comprueba tu conexión a Internet.... ", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsuarioView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String usuario = mUsuarioView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(usuario)) {
            mUsuarioView.setError(getString(R.string.error_field_required));
            focusView = mUsuarioView;
            cancel = true;
        } else if (!isEmailValid(usuario)) {
            mUsuarioView.setError(getString(R.string.error_invalid_email));
            focusView = mUsuarioView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(usuario, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String user) {
        //TODO: Replace this with your own logic
        return user.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUser;
        private final String mPassword;

        UserLoginTask(String user, String password) {
            mUser = user;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                //finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class ObtenerWebService extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String direccion = params[0];
            URL url = null; // url de donde queremos obtener la informacion.
            String devuelve = "";
            String us = params[2];
            String pass = params[3];
            if (params[1] == "1") { //Consulta de todos los usuarios

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
                            JSONArray usersJSON = respuestaJSON.getJSONArray("users");   // estado es el nombre del campo en el JSON
                            for (int i = 0; i < usersJSON.length(); i++) {
                                users = new usuarios(usersJSON.getJSONObject(i).getInt("ID"), usersJSON.getJSONObject(i).getString("NOMBRE")
                                        , usersJSON.getJSONObject(i).getString("APELLIDO_PATERNO")
                                        , usersJSON.getJSONObject(i).getString("APELLIDO_MATERNO")
                                        , usersJSON.getJSONObject(i).getString("USUARIO")
                                        , usersJSON.getJSONObject(i).getString("CONTRASENA")
                                        , usersJSON.getJSONObject(i).getString("PERFIL")
                                        , usersJSON.getJSONObject(i).getInt("HABILITADO")
                                        , usersJSON.getJSONObject(i).getString("RFC")
                                        , usersJSON.getJSONObject(i).getString("CURP")
                                        , usersJSON.getJSONObject(i).getString("DOMICILIO")
                                        , usersJSON.getJSONObject(i).getString("ESTADO")
                                        , usersJSON.getJSONObject(i).getString("PAIS")
                                        , usersJSON.getJSONObject(i).getString("NUMERO_CEDULA")
                                        , usersJSON.getJSONObject(i).getString("CORREO")
                                        , usersJSON.getJSONObject(i).getString("TELEFONO_FIJO")
                                        , usersJSON.getJSONObject(i).getString("TELEFONO_MOVIL")
                                        , usersJSON.getJSONObject(i).getString("GRADO_ESTUDIOS")
                                        , usersJSON.getJSONObject(i).getString("ENTIDAD")
                                        , usersJSON.getJSONObject(i).getString("PUESTO"));
                                listusuarios.add(users);
                            }

                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "No hay usuarios";
                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (usuarios b : listusuarios) {
                    if(!listusuarios.isEmpty()) {
                        if (us.equals(b.getUSUARIO()) && md5(pass).equals(b.getCONTRASEÑA())) {
                            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                            intent.putExtra("ID", String.valueOf(b.getID()));
                            intent.putExtra("NOMBRE", b.getNOMBRE());
                            intent.putExtra("APATERNO", b.getAPATERNO());
                            intent.putExtra("AMATERNO", b.getAMATERNO());
                            intent.putExtra("USUARIO", b.getUSUARIO());
                            intent.putExtra("CONTRASEÑA", b.getCONTRASEÑA());
                            intent.putExtra("PERFIL", b.getPERFIL());
                            intent.putExtra("HABILITADO", String.valueOf(b.getHABILITADO()));
                            intent.putExtra("RFC", b.getRFC());
                            intent.putExtra("CURP", b.getCURP());
                            intent.putExtra("DOMICILIO", b.getDOMICILIO());
                            intent.putExtra("ESTADO", b.getESTADO());
                            intent.putExtra("PAIS", b.getPAIS());
                            intent.putExtra("NUMERO_CEDULA", b.getN_CEDULA());
                            intent.putExtra("CORREO", b.getCORREO());
                            intent.putExtra("TELEFONO_FIJO", b.getT_FIJO());
                            intent.putExtra("TELEFONO_MOVIL", b.getT_MOVIL());
                            intent.putExtra("GRADO_ESTUDIOS", b.getG_ESTUDIOS());
                            intent.putExtra("ENTIDAD", b.getENTIDAD());
                            intent.putExtra("PUESTO", b.getPUESTO());
                            startActivity(intent);
                            devuelve= "si hay usuarios con esos datos";
                            break;
                        } else {
                            if (!us.equals(b.getUSUARIO()) && !pass.equals(b.getCONTRASEÑA())) {
                                devuelve= "no hay usuarios con ese nombre";
                            }
                        }
                    }
                }

                return devuelve;
            } else if (params[1].trim().equals("2")) {    // consulta por id

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
                        // que transformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON

                        if (resultJSON.trim().equals("1")) {      // hay un usuario que mostrar
                            devuelve = devuelve + respuestaJSON.getJSONObject("alumno").getString("idAlumno") + " " +
                                    respuestaJSON.getJSONObject("alumno").getString("nombre") + " " +
                                    respuestaJSON.getJSONObject("alumno").getString("direccion");

                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "No hay usuario";
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

            } else if (params[1].trim().equals("3")) {    // insert

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
                    jsonParam.put("NomUsuario", params[2]);
                    jsonParam.put("apellido_paterno", params[3]);
                    jsonParam.put("apellido_materno", params[4]);
                    jsonParam.put("telefono", params[5]);
                    jsonParam.put("correo", params[6]);
                    jsonParam.put("password", params[7]);
                    jsonParam.put("permisos", params[8]);
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

                        if (resultJSON.trim().equals("1")) {      // hay un usuario  que mostrar
                            devuelve = "Usuario insertado correctamente";

                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "El usuario no pudo insertarse";
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

            } else if (params[1].trim().equals("4")) {    // update

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
                    jsonParam.put("idalumno", params[2]);
                    jsonParam.put("nombre", params[3]);
                    jsonParam.put("direccion", params[4]);
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
                            devuelve = "Usuario actualizado correctamente";

                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "El usuario no pudo actualizarse";
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

            } else if (params[1].trim().equals("5")) {    // delete

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
                    jsonParam.put("idUser", params[2]);
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
                            devuelve = "Usuario borrado correctamente";

                        } else if (resultJSON.trim().equals("2")) {
                            devuelve = "No hay usuarios";
                        }

                    }

                } catch (MalformedURLException e) {
                    System.out.println("Error en BorrarWebService :" + e.getMessage());
                } catch (IOException e) {
                    System.out.println("Error en BorrarWebService :" + e.getMessage());
                } catch (JSONException e) {
                    System.out.println("Error en BorrarWebService :" + e.getMessage());
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

    private static final String md5(final String password) {
        try {
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
