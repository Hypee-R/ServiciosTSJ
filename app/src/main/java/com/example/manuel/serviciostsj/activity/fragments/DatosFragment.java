package com.example.manuel.serviciostsj.activity.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.manuel.serviciostsj.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DatosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DatosFragment extends Fragment {
    TextView txtBienvenido;
    ListView listDtPers;
    Bundle b;
    ArrayAdapter<String> adaptadorDtPer=null;
    private ArrayList<String> Arraylistdtper= new ArrayList<String>();
    private OnFragmentInteractionListener mListener;

    public DatosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_datos, container, false);

        txtBienvenido = (TextView) view.findViewById(R.id.Bienvenido);
        listDtPers = (ListView) view.findViewById(R.id.list_datos_per);
         b= getArguments();

        //txtBienvenido.setText("Hola Luis");
       if(b!=null) {
           String n_completo= b.getString("NOMBRE") + " " + b.getString("APATERNO") + " " + b.getString("AMATERNO");
           txtBienvenido.setText("Bienvenido : " + n_completo);
           Arraylistdtper.add("\n"+"RFC : "+b.getString("RFC")+"\n"+"\n"+"CURP : "+b.getString("CURP")+"\n"+"\n"+"NOMBRE : "+n_completo+"\n"+"\n"+
           "DOMICILIO : "+b.getString("DOMICILIO")+"\n"+"\n"+"ESTADO : "+b.getString("ESTADO")+"\n"+"\n"+"PAIS : "+b.getString("PAIS")+"\n"+"\n"+
           "NUMERO CEDULA : "+b.getString("NUMERO_CEDULA")+"\n"+"\n"+"CORREO : "+b.getString("CORREO")+"\n"+"\n"+"TELEFONO FIJO : "+b.getString("TELEFONO_FIJO")+"\n"+"\n"+
           "TELEFONO MOVIL : "+b.getString("TELEFONO_MOVIL")+"\n"+"\n"+"GRADO ESTUDIOS : "+b.getString("GRADO_ESTUDIOS")+"\n"+"\n"+
           "ENTIDAD ADSCRITO : "+b.getString("ENTIDAD")+"\n"+"\n"+"PUESTO : "+b.getString("PUESTO")+"\n"+"\n");
           adaptadorDtPer =
                   new ArrayAdapter<String>(getContext(), R.layout.list_black_text, R.id.list_content, Arraylistdtper);
           adaptadorDtPer.notifyDataSetChanged();

           listDtPers.setAdapter(adaptadorDtPer);
       }
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
