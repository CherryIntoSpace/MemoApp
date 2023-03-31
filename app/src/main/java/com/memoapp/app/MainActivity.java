package com.memoapp.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    /*MainData 클래스를 저장하는 ArrayList 형태의 arrayList 선언*/
    private ArrayList<MainData> arrayList;
    /*arrayList와 recyclerView 사이의 데이터를 이어주는 역할을 하는 MainAdapter 클래스의 참조 변수
    * mainAdapter 선언*/
    private MainAdapter mainAdapter;
    /*앱의 화면 출력을 담당하는 RecyclerView의 참조변수 recyclerView 선언*/
    private RecyclerView recyclerView;
    /*LinearLayoutManager는 recyclerView가 화면에 표시될 때, 아이템 뷰들이 배치되는 형태를 관리함,
    * 여기서는 리니어(일렬)로 배치*/
    private LinearLayoutManager linearLayoutManager;
    /*문자열을 입력하는 위젯인 EditText의 et_text 참조 변수 선언*/
    private EditText et_text;
    /*et_text에 입력한 문자열을 저장할 변수 input 선언*/
    private String input;
    /*btn_add는 입력한 문자열을 arrayList에 저장할 때, 클릭을 통해 신호를 주는 역할을 함*/
    private Button btn_add;
    /*SharedPreferences로 데이터를 저장할 xml 파일의 이름인 file 변수 선언*/
    public static final String file = "save";

    /*앱이 실행될 때의 생명 주기 OnCreate()*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*recyclerView 설정*/
        recyclerView = findViewById(R.id.rv);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        /*getStringArrayPref를 통해 데이터가 저장된 xml파일을 불러오고, 데이터를 arrayList에 저장함*/
        arrayList = getStringArrayPref(file);

        /*arrayList를 mainAdapter에 연결시키고, recyclerView의 어댑터를 mainAdapter로 설정함*/
        mainAdapter = new MainAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);

        /*activity_main의 위젯을 변수로 선언*/
        et_text = findViewById(R.id.et_text);
        btn_add = findViewById(R.id.btn_add);

        /*btn_add가 클릭되었을 때의 생명주기 setOnClickListener()*/
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*et_text에 입력된 텍스트를 문자열로 변환하여 input에 저장*/
                input = et_text.getText().toString();
                /*다음 입력을 위해 et_text를 비워줌*/
                et_text.setText(null);
                /*arrayList에 저장하기 위해서 현재 시간을 구하는 getTime과 문자열 input을 인자로 하는
                * 클래스 mainData를 만들어줌*/
                MainData mainData = new MainData(getTime()
                        ,input);
                /*만들어진 mainData를 arrayList에 add*/
                arrayList.add(mainData);
                /*notifyDataSetChanged는 mainAdapter에
                arrayList의 크기와 아이템들이 바뀌었음을 알리고,
                결과적으로 recyclerView가 바뀌었음을 나타낸다.*/
                mainAdapter.notifyDataSetChanged();
                /*편의를 위해 arrayList에 데이터가 추가되면 키보드를 내려 recyclerView를 보기 쉽게 한다.*/
                hideKeyboard();
                /*추가한 값을 보기 쉽게 스크롤 또한 최하단으로 내려준다*/
                recyclerView.scrollToPosition(arrayList.size()-1);
            }
        });
    }
    /*뒤로 가기(앱 종료)를 했을 때의 생명 주기 onDestroy*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            /*setStringArrayPref를 통해 arrayList에 저장된 데이터들을 xml에 저장함*/
            setStringArrayPref(file, arrayList);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    /*현재 시간을 알아내기 위한 함수, 현재 시간을 문자열로 return*/
    private String getTime(){
        long mNow = System.currentTimeMillis();
        Date mReDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = mFormat.format(mReDate);
        return formatDate;
    }
    /*키보드를 숨기는 함수*/
    private void hideKeyboard(){
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
    /*file xml에 arrayList를 저장하는 함수*/
    private void setStringArrayPref(String key, ArrayList<MainData> arrayList) throws JSONException {
        /*xml 저장을 위한 SharedPreferences과 Editor 설정*/
        SharedPreferences sharedPreferences = getSharedPreferences(key, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        /*SharedPreferences는 문자열만 저장이 가능한데, arrayList는 배열이므로 변환해줘야 함*/
        /*변환은 arrayList -> jsonObject -> jsonArray -> 문자열로 이루어짐*/
        JSONArray jsonArray = new JSONArray();
        /*arrayList의 데이터들을 arrayList의 사이즈만큼 반복하여
        date와 text를 key로 한 문자열을 jsonObject에 추가하고,
        이 jsonObject를 jsonArray에 넣어줌으로써 jsonArray를 완성함*/
        for(int i=0; i<arrayList.size(); ++i){
            JSONObject json = new JSONObject();
            json.put("date" , arrayList.get(i).getTv_date());
            json.put("text", arrayList.get(i).getTv_content());
            jsonArray.put(json);
        }
        /*arrayList에 값이 있다면 jsonArray를 문자열로 변환하여 file xml에 저장함*/
        if(!arrayList.isEmpty()){
            editor.putString(key, jsonArray.toString());
        }
        /*반대로 값이 없다면 null 파일을 저장함*/
        else{
            editor.putString(key,null);
        }
        /*apply를 통해 비동기 처리 저장*/
        editor.apply();
    }
    /*file xml로부터 ArrayList를 만들어 return하는 getStringArrayPref함수*/
    private ArrayList<MainData> getStringArrayPref(String key){
        /*SharedPreferences 설정*/
        SharedPreferences preferences = getSharedPreferences(key,0);
        /*getString은 어떤 이름의 파일을 불러오고, 저장된 값이 없을 때 불러올 값을 인자로 한다.*/
        String json = preferences.getString(key,null);

        /*return할 ArrayList를 datas로 선언한다.*/
        ArrayList<MainData> datas = new ArrayList<>();
        /*불러온 json의 값이 null이 아니면 xml에 저장된 데이터가 있다는 것을 의미하므로 if문을 통해 분기한다*/
        if(json != null){
            try{
                /*setStringArrayPref과는 반대로 문자열 -> jsonObject -> jsonArray -> datas로 변환하여
                * 불러온다*/
                /*문자열 json을 JSONArray인 array에 모두 저장하여 선언한다*/
                JSONArray array = new JSONArray(json);
                /*반복문을 통해 array의 길이만큼 array로부터 JSONObject를 받아오고 jsonObject에 저장한다*/
                /*이 jsonObject에서 key값이 각각 date, text인 현재 시간과 문자열을 추출하여 MainData 클래스의
                * 인자로 넘겨준다. 이로써 ArrayList인 datas를 만들 수 있다.*/
                for(int i=0; i<array.length(); ++i){
                    JSONObject jsonObject = (JSONObject) array.opt(i);
                    datas.add(new MainData(jsonObject.optString("date"), jsonObject.optString("text")));
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            /*완성된 datas를 return 한다*/
            return datas;
        }
        /*반대로 json에 저장된 값이 없다면(이전에 setStringArrayPref에서 저장한 데이터가 없음을 의미)
        * 새로운 ArrayList를 return한다.*/
        else return new ArrayList<>();
    }
}