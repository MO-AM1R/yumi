package com.example.yumi.presentation.home.view.fragments;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.example.yumi.R;
import com.example.yumi.databinding.FragmentProfileBinding;
import com.example.yumi.utils.LocaleHelper;


public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if (view == null){
            binding = FragmentProfileBinding.inflate(inflater);
            view = binding.getRoot();
        }else{
            binding = FragmentProfileBinding.bind(view);
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupLanguageSpinner();

        binding.spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedLang = binding.spinnerLanguage
                        .getItemAtPosition(position)
                        .toString();

                if (LocaleHelper.isSameLanguage(requireActivity(), selectedLang)) {
                    return;
                }

                selectedLang = selectedLang.substring(0, 2).toLowerCase();
                LocaleHelper.saveLanguage(requireContext(), selectedLang);
                LocaleHelper.setLocale(requireContext(), selectedLang);

                requireActivity().recreate();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupLanguageSpinner() {
        Spinner spinner = binding.spinnerLanguage;

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
    }
}