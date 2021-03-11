package com.human.edu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                EditText editTextID, editTextPassword;
                editTextID = findViewById(R.id.editTextID);
                editTextPassword = findViewById(R.id.editTextPassword);
                Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
                MainIntent.putExtra("editTextID", editTextID.getText().toString());
                MainIntent.putExtra("editTextPassword", editTextPassword.getText().toString());
            }
        });
    }

    //login.wml에서
    public void goToMain(View view) { //여기서 View는 클릭이벤트가 발생한 버튼을 가르킴.
        EditText editTextID, editTextPassword;
        
        editTextID = findViewById(R.id.editTextID);
        editTextPassword = findViewById(R.id.editTextPassword);
        //Toast.makeText(LoginActivity.this, "디버그 :" + editTextID.getText(), Toast.LENGTH_LONG).show();

        //로그인버튼을 onClick했을 때
        Intent MainIntent = new Intent(LoginActivity.this, MainActivity.class);
        //데이터를 입력해서 메인 액티비티화면 열기
        MainIntent.putExtra("editTextID", editTextID.getText().toString()); //아이디
        MainIntent.putExtra("editTextPassword", editTextPassword.getText().toString()); //암호
        startActivity(MainIntent); //액티비티 실행(=편지봉투인 Intent를 개봉, =화면 불러오기 실행)
        finish(); //LoginActivity화면을 종료(프로그램 종료X)
    }
}