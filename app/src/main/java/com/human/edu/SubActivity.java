package com.human.edu;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.AsyncResponse;
import core.JsonConverter;
import core.PostResponseAsyncTask;

/**
 * 이 액티비티에서는 리사이클러뷰에 RestAPI Json 데이터를 바인딩 시키는 기능
 * List객체(json데이터바인딩) - 어댑터클래스(데이터와 뷰객체 준간) - 리사이클러뷰
 */
public class SubActivity extends AppCompatActivity {
    //리사이클러 뷰를 사용할 멤버변수(=필드변수) 생성
    private RecyclerAdapter mRecyclerAdapter;
    private List mItemList = new ArrayList<MemberVO>();

    //어댑터에서 선택한 값 확인 변수(선택한 회원을 삭제하기 위해서)
    private String currentCursorId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        //객체 생성
        mRecyclerAdapter = new RecyclerAdapter(mItemList);

        //리사이클러뷰xml과 어댑터 클래스를 바인딩
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러 뷰의 높이를 고정함
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mRecyclerAdapter); //데이터없는 빈 어댑터를 뷰화면에 바인딩 시킴
        getAllData();
        mRecyclerAdapter.setOnItemClickListner(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemVClick(View v, int position) {
                MemberVO memberVO = (MemberVO) mItemList.get(position);
                currentCursorId = memberVO.getUser_id();
                //Toast.makeText(getApplicationContext(),"현재 선택한 회원ID는 "+currentCursorId, Toast.LENGTH_LONG).show();
                deleteUserData(position, currentCursorId);
            }
        });
    }

    //RestAPI 서버로 UserID를 전송해서 스프링앱의 사용자를 삭제하는 메서드
    private void deleteUserData(int position, String currentCursorId) {
        //삭제 대화상자에 보여줄 메세지를 만듦
        String message = "해당 회원을 삭제 하시겠습니까?<br />" +
                "position : " + position + "<br />" +
                "회원ID : " + currentCursorId + "<br />";
        DialogInterface.OnClickListener deleteListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //RestAPI 삭제 호출 call 비동기 통신 시작
                String requestUrl = "http://192.168.1.15:8080/android/delete/"+currentCursorId;
                PostResponseAsyncTask deleteTask = new PostResponseAsyncTask(SubActivity.this, new AsyncResponse() {
                    @Override
                    public void processFinish(String output) {
                        if(output.equals("sucess")) {
                            Toast.makeText(SubActivity.this, "삭제 성공", Toast.LENGTH_LONG).show();
                            getAllData();
                        }else {
                            Toast.makeText(SubActivity.this, "삭제 실패", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                deleteTask.execute(requestUrl);
            }
        };
        //삭제를 물어보는 다이얼로그를 생성
        new AlertDialog.Builder(this).setTitle("선택된 회원을 삭제")
                .setMessage(Html.fromHtml(message))
                .setPositiveButton("삭제", deleteListener)
                .setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss(); //취소버튼을 눌렀을 때 화면에서 치우기
                            }
                        }).show();
    }


    //RestAPI서버에서 전송받은 데이터를 리사이클러뷰 어댑터에 자인딩 시킴
    private void getAllData() {
        //RestAPI서버와 비동기 통신 시작
        String requestUrl = "http://192.168.1.15:8080/andriod/list";
        HashMap postDataParams = new HashMap();
        postDataParams.put("mobile","android");
        List resultList = new ArrayList<>(); //RestAPI에서 보내온 json데이터가 저장공간 생성
        PostResponseAsyncTask readTask = new PostResponseAsyncTask(SubActivity.this, postDataParams, new AsyncResponse() {
            @Override
            public void processFinish(String output) {
                Log.i("REstAPI텍스트: ", output);
                ArrayList<MemberVO> memberList = new JsonConverter<MemberVO>().toArrayList(output, MemberVO.class);
                //위 컨버트한 멤버리스트 변수를 어댑터에 바인딩 시킴
                for(MemberVO value: memberList) {
                    //resultList객체에 1개 레코드씩 저장 => 어댑터에 데이터 바인딩 예정
                    String p_id = value.getUser_id();
                    String p_name = value.getUser_name();
                    String p_email = value.getEmail();

                    resultList.add(new MemberVO(p_id,p_name,p_email));
                }
                //화면 출력
                mItemList.clear();
                mItemList.addAll(resultList);
                mRecyclerAdapter.notifyDataSetChanged(); //어댑터 객체가 리프레시 됨
            }
        });
        readTask.execute(requestUrl); //비동기 통신 시작명령
    }
}