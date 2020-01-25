package com.alex.FamilyMap.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alex.FamilyMap.Data.DataCache;
import com.alex.FamilyMap.R;
import com.alex.FamilyMap.Proxy.ServerProxy;

import Model.Login;
import Response.AllEventResponse;
import Response.AllPersonResponse;
import Response.LoginRegisterResponse;

public class LoginFragment extends Fragment {
    private  static  final String LOG_TAG = "LoginFragment";
    private EditText mServerHost;
    private EditText mServerPort;
    private EditText mUsername;
    private EditText mPassword;
    private EditText mEmail;
    private EditText mFirstName;
    private EditText mLastName;
    private RadioGroup mGender;
    private RadioButton mMale;
    private RadioButton mFemale;
    private Button mLogInButton;
    private Button mRegisterButton;
    private Login mLoginModel;
    private DataCache dataCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "in onCreate(Bundle)");
        super.onCreate(savedInstanceState);
        dataCache = DataCache.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(LOG_TAG, "in onCreateView(...)");
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mLoginModel = new Login();
        mServerHost = view.findViewById(R.id.server_host);
        mServerPort = view.findViewById(R.id.server_port);
        mUsername = view.findViewById(R.id.username);
        mPassword = view.findViewById(R.id.password);
        mEmail = view.findViewById(R.id.email);
        mFirstName = view.findViewById(R.id.firstName);
        mLastName = view.findViewById(R.id.lastName);
        mGender = view.findViewById(R.id.radioGender) ;
        mMale = view.findViewById(R.id.radioMale);
        mFemale = view.findViewById(R.id.radioFemale);
        mLogInButton = view.findViewById(R.id.signIn);
        mLogInButton.setEnabled(false);
        mRegisterButton = view.findViewById(R.id.register);
        mRegisterButton.setEnabled(false);

        mServerHost.addTextChangedListener(editTextWatcher);
        mServerPort.addTextChangedListener(editTextWatcher);
        mUsername.addTextChangedListener(editTextWatcher);
        mPassword.addTextChangedListener(editTextWatcher);
        mEmail.addTextChangedListener(editTextWatcher);
        mFirstName.addTextChangedListener(editTextWatcher);
        mLastName.addTextChangedListener(editTextWatcher);
        mGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((RadioButton) v).isChecked();
                switch (v.getId()) {
                    case R.id.radioMale:
                        if (checked) {
                            mLoginModel.setGender("m");
                            break;
                        }
                    case R.id.radioFemale:
                        if (checked) {
                            mLoginModel.setGender("f");
                            break;
                        }

                }
            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterTask registerTask = new RegisterTask();
                registerTask.execute(mLoginModel);

            }
        });
        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginTask loginTask = new LoginTask();
                loginTask.execute(mLoginModel);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    private class RegisterTask extends AsyncTask<Login, Void, LoginRegisterResponse> {
        @Override
        protected LoginRegisterResponse doInBackground(Login... logins) {
            ServerProxy serverProxy = new ServerProxy();
            LoginRegisterResponse registerResponse = serverProxy.register(logins[0]);
            return registerResponse;
        }
        @Override
        protected void onPostExecute(LoginRegisterResponse registerResponse) {
            onLogin(registerResponse);
        }
    }

    private class LoginTask extends AsyncTask<Login, Void, LoginRegisterResponse> {
        @Override
        protected LoginRegisterResponse doInBackground(Login... logins) {
            ServerProxy serverProxy = new ServerProxy();
            LoginRegisterResponse loginResponse = serverProxy.login(logins[0]);
            return loginResponse;
        }
        @Override
        protected  void onPostExecute(LoginRegisterResponse loginResponse) {
            onLogin(loginResponse);
        }
    }

    private class retAllPersonTask extends AsyncTask<String, Void, AllPersonResponse> {
        @Override
        protected AllPersonResponse doInBackground(String... str) {
            ServerProxy serverProxy = new ServerProxy();
            AllPersonResponse allPersonResponse = serverProxy.retAllPerson(str[0], mLoginModel);
            return allPersonResponse;
        }
        @Override
        protected  void onPostExecute(AllPersonResponse allPersonResponse) {
            dataCache.addToDataCache(allPersonResponse);
        }
    }

    private class retAllEventTask extends AsyncTask<String, Void, AllEventResponse> {
        @Override
        protected AllEventResponse doInBackground(String... str) {
            ServerProxy serverProxy = new ServerProxy();
            AllEventResponse allEventResponse = serverProxy.retAllEvent(str[0], mLoginModel);
            return allEventResponse;
        }
        @Override
        protected  void onPostExecute(AllEventResponse alleventResponse) {
            dataCache.addToDataCache(alleventResponse);
        }
    }

    private TextWatcher editTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mLoginModel.setServerHost(mServerHost.getText().toString().trim());
            mLoginModel.setServerPort(mServerPort.getText().toString().trim());
            mLoginModel.setUserName(mUsername.getText().toString().trim());
            mLoginModel.setPassword(mPassword.getText().toString().trim());
            mLoginModel.setEmail(mEmail.getText().toString().trim());
            mLoginModel.setFirstName(mFirstName.getText().toString().trim());
            mLoginModel.setLastName(mLastName.getText().toString().trim());
            if (mMale.isChecked()) {
                mLoginModel.setGender("m");
            }
            else {
                mLoginModel.setGender("f");
            }

            if (!TextUtils.isEmpty(mServerHost.getText()) &&
                    !TextUtils.isEmpty(mServerPort.getText()) &&
                    !TextUtils.isEmpty(mUsername.getText()) &&
                    !TextUtils.isEmpty(mPassword.getText())) {
                mLogInButton.setEnabled(true);
            }
            else {
                mLogInButton.setEnabled(false);
            }
            if (!TextUtils.isEmpty(mServerHost.getText()) &&
                    !TextUtils.isEmpty(mServerPort.getText()) &&
                    !TextUtils.isEmpty(mUsername.getText()) &&
                    !TextUtils.isEmpty(mPassword.getText()) &&
                    !TextUtils.isEmpty(mEmail.getText()) &&
                    !TextUtils.isEmpty(mFirstName.getText()) &&
                    !TextUtils.isEmpty(mLastName.getText())) {
                mRegisterButton.setEnabled(true);
            }
            else {
                mRegisterButton.setEnabled(false);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void onLogin(LoginRegisterResponse loginRegisterResponse) {
        if (loginRegisterResponse.getMessage() == null) {
            dataCache.setLoggedInPersonID(loginRegisterResponse.getPersonID());
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Fragment fragment = new MapFragment();
            fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
        else {
            Toast.makeText(getActivity(), loginRegisterResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }
        new retAllPersonTask().execute(loginRegisterResponse.getAuthToken());
        new retAllEventTask().execute(loginRegisterResponse.getAuthToken());
    }
}
