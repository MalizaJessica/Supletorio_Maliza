package com.example.supletorio;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Ventas_MJ extends ActivityInfo {
    private List<Tarea_MJ> listaTareas = new ArrayList<Tarea_MJ>();
    private RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();
    private Button eliminarimagen;
    View root;


    public Ventas_MJ() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_personal, container, false);
        // Inflate the layout for this fragment
        mAuth = FirebaseAuth.getInstance();
        recuperarDatosFB();
        eliminarimagen = root.findViewById(R.id.imageView_delete2);
        eliminarimagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminarimaginario();
            }
        });
        return root;
    }
    private void recuperarDatosFB() {
        FirebaseUser user = mAuth.getCurrentUser();
        databaseReference.child(user.getUid()).child("Tareas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaTareas.clear();
                for (DataSnapshot objeto: snapshot.getChildren()) {
                    Tarea_MJ tarea = objeto.getValue(Tarea_MJ.class);
                    if(tarea.getTipo().equals("b") && tarea.getCompletado()==false){
                        listaTareas.add(tarea);
                    }
                }
                //└Toast.makeText(getActivity(), String.valueOf(listaTareas.size()), Toast.LENGTH_SHORT).show();
                AdapterRecyclerView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void AdapterRecyclerView() {
        mRecyclerView = root.findViewById(R.id.recyclerviewTarea);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new AdapterTarea(listaTareas, R.layout.detallepersonal, new AdapterTarea.OnItemClickListener() {
            @Override
            public void onItemClick(Tarea tarea, int position) {
                Toast.makeText(getActivity(),tarea.getUid(), Toast.LENGTH_SHORT).show();
            }
        }, new AdapterTarea.OnViewClickListener() {
            @Override
            public void onViewClick(Tarea tarea, int positio, boolean estado) {

            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void eliminarimaginario() {
        FirebaseUser user = mAuth.getCurrentUser();
        for (Tarea_MJ tarea: listaTareas) {
            if (tarea.getCompletado()){
                databaseReference.child(user.getUid()).child("Tareas").child(tarea.getUid()).setValue(tarea);
            }
        }
    }
}
