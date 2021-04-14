package com.human.edu;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

import core.AsyncResponse;
import core.PostResponseAsyncTask;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText editTextID, editTextPassword;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(LoginActivity.this, "onDestory상태6", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(LoginActivity.this, "onStop상태5", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(LoginActivity.this, "onPause상태4", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(LoginActivity.this, "onResume상태3", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(LoginActivity.this, "onStart상태2", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Toast.makeText(LoginActivity.this, "onCreate상태1", Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        editTextID = findViewById(R.id.editTextID);
        editTextPassword = findViewById(R.id.editTextPassword);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //스프링으로 보낼 데이터를 해시맵 타입을 저장
                HashMap postDataParams = new HashMap();
                postDataParams.put("txtUsername",editTextID.getText().toString());
                postDataParams.put("txtPassword",editTextPassword.getText().toString());
                //스프링앱 주소를 지정
                String requestUrl = "http://192.168.1.15:8080/android/login";
                //jsp의 Ajax과 같은 역할의 AsyncTask클래스 사용
                PostResponseAsyncTask readTask = new PostResponseAsyncTask(LoginActivity.this, postDataParams, new AsyncResponse() {

                    @Override
                    public void processFinish(String output) { //output는 스프링앱에서 전송받은 로그인 사용자 정보
                        Toast.makeText(LoginActivity.this, output+"디버그", Toast.LENGTH_SHORT).show();
                        String jsonString = output.substring(output.indexOf('{'),output.indexOf('}'));
                        if(!jsonString.equals("{}")) { //로그인 사용자 정보가 있으면
                            Log.i("디버그", jsonString);
                            //로그인 이후 액티비티를 여기서 띄우기
                            Intent intent = new Intent(LoginActivity.this, SubActivity.class);
                        }else{
                            Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                readTask.execute(requestUrl);
                /*
                //Intent는 안드로이드앱에서 액티비티간 데이터를 전송하는 클래스
                Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                mainIntent.putExtra("editTextID", editTextID.getText().toString());
                mainIntent.putExtra("editTextPassword", editTextPassword.getText().toString());
                startActivity(mainIntent);
                 */
            }
        });
    }
    //login.xml에서 onClick 속성을 gotoMain이라고 지정했을 경우 사용가능(아래)
    public void gotoMain(View view) { //여기서 view는 클릭이벤트가 발생한 버튼
        EditText editTextID, editTextPassword;
        editTextID = findViewById(R.id.editTextID);
        editTextPassword = findViewById(R.id.editTextPassword);
        //Toast.makeText(LoginActivity.this, "디버그: " + editTextID.getText(),Toast.LENGTH_LONG).show();
        //로그인 버튼을 onClick했을때
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        //데이터를 입력해서 메인액티비티 화면열기(아래)
        mainIntent.putExtra("editTextID", editTextID.getText().toString());//아이디
        mainIntent.putExtra("editTextPassword", editTextPassword.getText().toString());//암호
        startActivity(mainIntent);//편지봉투Intent를 개봉 = 화면불러오기실행
        //finish();//LoginActivity화면을 종료(프로그램종료X)
    }
}