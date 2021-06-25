package com.example.pwsi.ui.ui.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.pwsi.R;

import org.jetbrains.annotations.NotNull;

public class ResultFragment extends Fragment {

    private TextView resultTextView;

    @Override
    public void onViewCreated(@NotNull View view, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        assert bundle != null;

        resultTextView = (TextView) view.findViewById(R.id.resultTextView);

        String dataString = "New case added:\n\n" + bundle.getString("response", "");
        resultTextView.setText(dataString);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}