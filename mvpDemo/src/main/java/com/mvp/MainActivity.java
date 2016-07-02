package com.mvp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mvp.presenter.UserLoginPresenter;
import com.mvp.view.IUserLoginView;

public class MainActivity extends AppCompatActivity implements IUserLoginView{


    private EditText mUsername,mPassword;
    private Button mLogin,mClear;
    private ProgressBar mProgressBar;
    private UserLoginPresenter mUserLoginPresenter = new UserLoginPresenter(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
    private void initView(){
        mUsername = (EditText) findViewById(R.id.username);
        mPassword = (EditText) findViewById(R.id.password);
        mClear = (Button) findViewById(R.id.clear);
        mLogin = (Button) findViewById(R.id.login);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserLoginPresenter.login();
            }
        });
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserLoginPresenter.clear();
            }
        });



    }

    @Override
    public String getUserName() {
        return mUsername.getText().toString();
    }

    @Override
    public String getPassWord() {
        return mPassword.getText().toString();
    }

    @Override
    public void clearUserName() {
        mUsername.setText("");
    }

    @Override
    public void clearPassWrod() {
        mPassword.setText("");
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void toMainActivity() {
        Toast.makeText(MainActivity.this, "I want go to the MainActivity", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void showFailedError() {
        Toast.makeText(MainActivity.this, "UserName or PassWord is wrong", Toast.LENGTH_SHORT).show();
    }
}
