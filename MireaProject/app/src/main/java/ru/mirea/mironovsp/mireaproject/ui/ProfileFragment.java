package ru.mirea.mironovsp.mireaproject.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ru.mirea.mironovsp.mireaproject.R;

public class ProfileFragment extends Fragment {
    private static final String PREFS_NAME = "ProfilePrefs";

    private EditText etFullName, etGroup, etAge;
    private RadioGroup rgGender;
    private Button btnSaveProfile;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        etFullName = root.findViewById(R.id.etFullName);
        etGroup = root.findViewById(R.id.etGroup);
        etAge = root.findViewById(R.id.etAge);
        rgGender = root.findViewById(R.id.rgGender);
        btnSaveProfile = root.findViewById(R.id.btnSaveProfile);

        loadProfileData();

        btnSaveProfile.setOnClickListener(v -> saveProfileData());

        return root;
    }

    private void saveProfileData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("fullName", etFullName.getText().toString());
        editor.putString("group", etGroup.getText().toString());
        editor.putInt("age", Integer.parseInt(etAge.getText().toString()));

        int selectedId = rgGender.getCheckedRadioButtonId();
        RadioButton radioButton = requireView().findViewById(selectedId);
        editor.putString("gender", radioButton.getText().toString());

        editor.apply();

        Toast.makeText(getContext(), "Профиль сохранен", Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        SharedPreferences prefs = requireActivity().getSharedPreferences(PREFS_NAME, 0);

        etFullName.setText(prefs.getString("fullName", ""));
        etGroup.setText(prefs.getString("group", ""));
        etAge.setText(String.valueOf(prefs.getInt("age", 0)));

        String gender = prefs.getString("gender", "");
        if (gender.equals("Мужской")) {
            rgGender.check(R.id.rbMale);
        } else if (gender.equals("Женский")) {
            rgGender.check(R.id.rbFemale);
        }
    }
}