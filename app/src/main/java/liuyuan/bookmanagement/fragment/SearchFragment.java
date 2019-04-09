package liuyuan.bookmanagement.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import liuyuan.bookmanagement.R;
import liuyuan.bookmanagement.activity.SearchBookActivity;


public class SearchFragment extends Fragment {
    private EditText bookNameEditText;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        bookNameEditText = (EditText) view.findViewById(R.id.bookNameSearchText);
        View searchButton = view.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSearch();
            }
        });
    }





    /**
     * 搜索按钮点击事件
     */
    public void onClickSearch(){
        Intent intent = new Intent();


        //图书名称
        if(!TextUtils.isEmpty(bookNameEditText.getText().toString()))

            intent.putExtra("name", bookNameEditText.getText().toString());
        if (intent!=null && intent.getExtras() != null) {
            intent.setClass(getActivity().getApplicationContext(),SearchBookActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getActivity(), "请输入至少一个搜索信息", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


}
