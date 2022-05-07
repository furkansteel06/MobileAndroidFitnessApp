package com.example.wm_fitness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wm_fitness.DietSheets.*;

import org.w3c.dom.Text;


public class FragmentNoSporDiet extends Fragment {
    private Button button1, button2, button3, button4, button5, button6, button7, button8;
    private TextView vkiInfoTV;
    View view;
    Intent intent;
    String vki;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nospordiet,container,false);
        BtnView(view);
        SharedPreferences mySharedPreferences = this.getContext().getSharedPreferences("VKI", Context.MODE_PRIVATE);
        vki = mySharedPreferences.getString("vkiInfo", "");
        vkiInfoTV.setText(vki);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),NoErkekActivity1.class);
                getActivity().startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),NoErkekActivity2.class);
                getActivity().startActivity(intent);
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),NoErkekActivity3.class);
                getActivity().startActivity(intent);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),NoErkekActivity4.class);
                getActivity().startActivity(intent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),NoKadinActivity1.class);
                getActivity().startActivity(intent);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(),NoKadinActivity2.class);
                getActivity().startActivity(intent);
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), NoKadinActivity3.class);
                getActivity().startActivity(intent);
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), NoKadinActivity4.class);
                getActivity().startActivity(intent);
            }
        });
        return view;
    }

    private void BtnView(View view) {
        button1 = (Button) view.findViewById(R.id.erkek1);
        button2 = (Button) view.findViewById(R.id.erkek2);
        button3 = (Button) view.findViewById(R.id.erkek3);
        button4 = (Button) view.findViewById(R.id.erkek4);
        button5 = (Button) view.findViewById(R.id.kadin1);
        button6 = (Button) view.findViewById(R.id.kadin2);
        button7 = (Button) view.findViewById(R.id.kadin3);
        button8 = (Button) view.findViewById(R.id.kadin4);
        vkiInfoTV = (TextView) view.findViewById(R.id.vkiInfoTV);
    }
}
