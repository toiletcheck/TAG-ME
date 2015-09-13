package com.tcc.gian_luca.test;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.cast.Cast;
import com.squareup.okhttp.ResponseBody;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class RatingDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String mName;
    private Integer mStar;
    private Integer placeid;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static RatingDialog newInstance(String param1, Integer param2, Integer param3) {
        RatingDialog fragment = new RatingDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    public RatingDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mName = getArguments().getString(ARG_PARAM1);
            mStar = getArguments().getInt(ARG_PARAM2);
            placeid = getArguments().getInt(ARG_PARAM3);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View vi = inflater.inflate(R.layout.rating, container, false);
        Button butt = (Button)vi.findViewById(R.id.button);
        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Log.d("Test", "Test");

                sendRating(vi.findViewById(R.id.ratingBar));
            }
        });


        return vi;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void sendRating(View v) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.100.88")
                .build();

        

        RatingBar ratingbar = (RatingBar) v;

        float stars = ratingbar.getRating();
        int rated = Math.round(stars);

        TCC service = retrofit.create(TCC.class);



        Call<ResponseBody> result = service.sendRating(rated, placeid);
        result.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Response<ResponseBody> response) {
                if (response.isSuccess()) {
                Toast.makeText(getActivity(),"Sucsess", Toast.LENGTH_LONG);}
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
