package com.example.andriy.openeyes;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


@SuppressLint("ValidFragment")
public class TabInfrormationComfortablePlace extends Fragment {
    TextView describeText, adressText;
    String adress, describe;

    public TabInfrormationComfortablePlace(String adr, String desc){
        adress=adr;
        describe=desc;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_tab_infrormation_comfortable_place, container, false);
        describeText=(TextView) view.findViewById(R.id.describeTextComfortablePlace);
        describeText.setText(describe);
        adressText=(TextView) view.findViewById(R.id.adressTextComfortablePlace);
        adressText.setText(adress);
        return  view ;
    }


}
