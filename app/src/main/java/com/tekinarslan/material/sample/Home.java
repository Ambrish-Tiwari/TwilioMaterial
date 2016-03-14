package com.tekinarslan.material.sample;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.twilio.client.Connection;
import com.twilio.client.ConnectionListener;
import com.twilio.client.Device;
import com.twilio.client.DeviceListener;
import com.twilio.client.PresenceEvent;
import com.twilio.client.Twilio;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements Twilio.InitListener,DeviceListener,ConnectionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "Home";
    private Context context = null;
    private String phoneNumber = null;
    private Connection connection = null;
    private Device device = null;

    // TODO: Rename and change types of parameters
    private int mParam1;


    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
        context = this.getActivity().getApplicationContext();
        Twilio.initialize(context, this /* Twilio.InitListener */);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(int param1) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        EditText dailNumber = (EditText)view.findViewById(R.id.mobNumber);
        phoneNumber = dailNumber.toString();
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabButton);
        fab.setDrawableIcon(getResources().getDrawable(R.drawable.telephone));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Home.this.connect(phoneNumber);
            }
        });
        switch (mParam1) {
            case 0:
                fab.setBackgroundColor(getResources().getColor(R.color.material_deep_teal_500));
                break;
            case 1:
                fab.setBackgroundColor(getResources().getColor(R.color.red));
                break;
            case 2:
                fab.setBackgroundColor(getResources().getColor(R.color.blue));

                break;
            case 3:
                fab.setBackgroundColor(getResources().getColor(R.color.material_blue_grey_800));
                break;
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void connect(String phoneNumber )
    {
        Log.d(TAG + "Phone Number ", phoneNumber);
        Map<String, String> parameters = new HashMap<String, String>();
        //parameters.put("From", Constants.FROM_NUMBER);
        parameters.put("To", phoneNumber);
        //connection = device.connect(parameters, null /* ConnectionListener */);
        if (connection == null){
            Log.v(TAG,"Establishing connection....");
            //Log.w(TAG, "Failed to create new connection");
            connection = device.connect(parameters, null /* ConnectionListener */);
        }else{
            Log.w(TAG, "Already Connected.");
            Log.v(TAG, "Disconnecting...");
            connection.disconnect();
            connection = null;
            Log.v(TAG, "Connection Disconnected.");
        }
    }
    public void disconnect()
    {
        if (connection != null) {
            connection.disconnect();
            connection = null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onInitialized() {
        Log.d(TAG, "Twilio SDK is ready");
        new RetrieveCapabilityToken().execute();
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "Twilio SDK couldn't start: " + e.getLocalizedMessage());
    }

    @Override
    public void onConnecting(Connection connection) {

    }

    @Override
    public void onConnected(Connection connection) {

    }

    @Override
    public void onDisconnected(Connection connection) {

    }

    @Override
    public void onDisconnected(Connection connection, int i, String s) {

    }

    @Override
    public void onStartListening(Device device) {

    }

    @Override
    public void onStopListening(Device device) {

    }

    @Override
    public void onStopListening(Device device, int i, String s) {

    }

    @Override
    public boolean receivePresenceEvents(Device device) {
        return false;
    }

    @Override
    public void onPresenceChanged(Device device, PresenceEvent presenceEvent) {

    }

    protected void setCapabilityToken(String capabilityToken){
        Log.d(TAG,"Inside setCapabilityToken");
        device = Twilio.createDevice(capabilityToken, null /* DeviceListener */);

        Log.d(TAG,"Device Object Created.");
        Intent intent = new Intent(context, SampleActivity.class);
        Log.d(TAG,"Intent Created");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Log.d(TAG, "PendingIntent Created.");
        if(device!=null) {
            Log.d(TAG,"Initiate pendingIntent.");
            device.setIncomingIntent(pendingIntent);
        }else{
            Log.e(TAG,"Device Object is Null.");
        }
    }
    @Override
    protected void finalize()
    {
        if (device != null)
            device.release();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class  RetrieveCapabilityToken extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            try{
                String capabilityToken = HttpHelper.httpGet(Constants.CAPABILITY_TOKEN_URL);
                Log.d(TAG + " capabilityToken: ",capabilityToken);
                return capabilityToken;
            } catch( Exception e ){
                Log.e(TAG, "Failed to obtain capability token: " + e.getLocalizedMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String capabilityToken) {
            super.onPostExecute(capabilityToken);
            Home.this.setCapabilityToken(capabilityToken);
        }
    }
}
