package com.getintouch.vptgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, email_id;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.edit_text_username);
        password = findViewById(R.id.edit_text_password);
        email_id = findViewById(R.id.edit_text_email);
        submit = findViewById(R.id.submit);

        final HttpConfig httpConfig = new HttpConfig(this, "http://10.0.2.2/vptg/Users/index.php", HttpConfig.POST, new ResponseListener() {
            @Override
            public void onErrorReceived() {

            }

            @Override
            public void onResponseReceived(String s) {
                if (s.equals("true")){
                    Toast.makeText(RegisterActivity.this, "Player Registered Successfully", Toast.LENGTH_SHORT).show();
                    username.setText("");
                    email_id.setText("");
                    password.setText("");
                }else if (s.equals("false")){
                    Toast.makeText(RegisterActivity.this, "Something went wrong! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }){
            @Override
            public Map<String, String> addParams() {
                Map<String, String> map = new HashMap();
                map.put("user_name",username.getText().toString());
                map.put("password",password.getText().toString());
                map.put("email",email_id.getText().toString());
                return map;
            }
        };

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                httpConfig.run();
            }
        });
    }
}
