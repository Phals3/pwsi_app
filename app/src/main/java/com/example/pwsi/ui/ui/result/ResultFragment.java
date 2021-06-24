package com.example.pwsi.ui.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.pwsi.R;

import org.jetbrains.annotations.NotNull;

public class ResultFragment extends Fragment {

    private TextView resultTextView;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;

        String name = bundle.getString("name", "");
        String category = bundle.getString("category", "");
        String description = bundle.getString("description", "");
        String location = bundle.getString("location", "");
        String dataString = String.format("Nazwa zdarzenia: %s\nKategoria: %s\nOpis: %s\n Lokalizacja zdarzenia: %s", name, category, description, location);

        resultTextView = (TextView) view.findViewById(R.id.resultTextView);
        resultTextView.setText(dataString);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public static ResultFragment newInstance(Bundle bundle) {
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}