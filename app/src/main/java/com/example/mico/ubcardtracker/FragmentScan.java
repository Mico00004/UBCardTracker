package com.example.mico.ubcardtracker;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class FragmentScan extends Fragment {
    private int STORAGE_PERMISSION_CODE = 1;
    public static EditText ScanCardNumber;
    private EditText ScanClientAddress;
    private Spinner ScanCardType,ScanArea;
    private Button Save,Cancel;
    private LinearLayout ScanLayout,ScanLoading;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_scan, container, false);
        initViews(view);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Scan Card");

        ScanCardNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getContext(), ScanCardActivity.class));
                }
                else{
                    requestCameraPermission();
                }
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHome fragmentHome = new FragmentHome();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_frame, fragmentHome);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ScanCardNumber.getText().length() < 1)
                {
                    Toast.makeText(getActivity(), "Please Scan the Card Number", Toast.LENGTH_SHORT).show();
                    ScanCardNumber.requestFocus();
                }
                else if(ScanCardType.getSelectedItem().equals("Select Card Type"))
                {
                    Toast.makeText(getActivity(), "Please select Card Type", Toast.LENGTH_SHORT).show();
                    ScanCardType.requestFocus();
                }
                else if(ScanArea.getSelectedItem().equals("Select Area"))
                {
                    Toast.makeText(getActivity(), "Please select Area", Toast.LENGTH_SHORT).show();
                    ScanArea.requestFocus();
                }
                else if(ScanClientAddress.getText().length() < 1)
                {
                    ScanClientAddress.setError("Please fill up Client Address");
                    ScanArea.requestFocus();
                }
                else
                {
                    showLoading();
                    addScannedCard();
                }
            }
        });
        String[] arraySpinner = new String[] {
                "Select Area", "ARMM", "CAR", "NCR", "Region 1",
                "Region 2", "Region 3", "Region 4A", "Region 4B",
                "Region 5", "Region 6", "Region 7", "Region 8",
                "Region 9", "Region 10", "Region 11", "Region 12",
                "Region 13"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ScanArea.setAdapter(adapter);

        String[] arraySpinner1 = new String[] {
                "Select Card Type","Platinum", "Gold", "Classic"
        };
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, arraySpinner1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ScanCardType.setAdapter(adapter1);

        return view;
    }
    private void initViews(View view) {
        ScanCardNumber = view.findViewById(R.id.scan_card_number);
        ScanCardType = view.findViewById(R.id.scan_card_type);
        ScanClientAddress = view.findViewById(R.id.scan_client_address);
        ScanArea = view.findViewById(R.id.scan_area);
        Save = view.findViewById(R.id.save_scan);
        Cancel = view.findViewById(R.id.cancel_scan);
        ScanLayout = view.findViewById(R.id.scan_card_layout);
        ScanLoading = view.findViewById(R.id.scan_card_loading);
    }
    private void requestCameraPermission()
    {
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.CAMERA))
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed to open the Camera")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startActivity(new Intent(getContext(), ScanCardActivity.class));
            }
        }
    }
    private void addScannedCard()
    {
        final String card_number = ScanCardNumber.getText().toString();
        final String card_type = ScanCardType.getSelectedItem().toString();
        final String client_address = ScanClientAddress.getText().toString();
        final String area = ScanArea.getSelectedItem().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SCAN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equalsIgnoreCase("Scanned Successfully")){
                            Toast.makeText(getActivity(), "Scanned Successfully", Toast.LENGTH_SHORT).show();
                            FragmentHome fragmentHome = new FragmentHome();
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fragment_frame, fragmentHome);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();

                        }
                        else if(response.equalsIgnoreCase("Card Number Already Exist")){
                            Toast.makeText(getActivity(), "Card Number Already Exist", Toast.LENGTH_SHORT).show();
                            showLayout();
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Failed to Scan", Toast.LENGTH_SHORT).show();
                            showLayout();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                        Log.wtf("Error",error);
                        showLayout();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("card_number",card_number);
                params.put("type", card_type);
                params.put("client_address", client_address);
                params.put("area", area);

                return params;
            }
        };
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }
    private void showLoading()
    {
        ScanLoading.setVisibility(View.VISIBLE);
        ScanLayout.setVisibility(View.GONE);
    }
    private void showLayout()
    {
        ScanLoading.setVisibility(View.GONE);
        ScanLayout.setVisibility(View.VISIBLE);
    }
}
