package com.getintouch.vptgame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    TextView reg_screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.edit_text_username);
        password = findViewById(R.id.edit_text_password);
        login = findViewById(R.id.login);
        reg_screen = findViewById(R.id.reg_screen);

        reg_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        final HttpConfig httpConfig = new HttpConfig(this, "http://10.0.2.2/vptg/Users/login.php", HttpConfig.POST, new ResponseListener() {
            @Override
            public void onErrorReceived() {

            }

            @Override
            public void onResponseReceived(String s) {
                if (s.equals("true")){
                    Toast.makeText(LoginActivity.this, "Player Login Successfully", Toast.LENGTH_SHORT).show();
                    username.setText("");
                    password.setText("");
                }else if (s.equals("false")){
                    Toast.makeText(LoginActivity.this, "Something went wrong! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public Map<String, String> addParams() {
                Map<String, String> map = new HashMap();
                map.put("user_name",username.getText().toString());
                map.put("password",password.getText().toString());
                return map;
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpConfig.run();
            }
        });
    }
}
