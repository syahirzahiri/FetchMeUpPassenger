package com.urbantechies.fetch_me_up_passenger.drivers;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.urbantechies.fetch_me_up_passenger.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountSafetyFragment extends Fragment {


    public AccountSafetyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account_safety, container, false);
    }

}
