package liuyuan.bookmanagement.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.permission.AndPermission;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;

import org.litepal.LitePal;

import java.util.List;

import liuyuan.bookmanagement.R;
import liuyuan.bookmanagement.fragment.BorrowingFrgment;
import liuyuan.bookmanagement.fragment.FirstFragment;
import liuyuan.bookmanagement.fragment.InputFragment;
import liuyuan.bookmanagement.fragment.SearchFragment;
import liuyuan.bookmanagement.mapping.Book;
import liuyuan.bookmanagement.model.BookModel;
import liuyuan.bookmanagement.model.HistoryModel;
import liuyuan.bookmanagement.view.PopMenu;
import liuyuan.bookmanagement.view.PopMenuItem;
import liuyuan.bookmanagement.view.PopMenuItemListener;

public class MainActivity extends AppCompatActivity {

    private int REQUEST_CODE_SCAN = 111;
    private static final String TAG = "MainActivity";
    private PopMenu popMenu ;
    private BookModel bookModel;
    private Integer fragmentTarger;


    private FirstFragment firstFragment;
    private FirstFragment onLoanFragment;
    private FirstFragment historyFragment;

    private InputFragment inputFragment;
    private SearchFragment searchFragment;
    private   FragmentManager fragmentManager;
    private BorrowingFrgment borrowingFrgment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPopMenu();
        bookModel=new BookModel();

        firstFragment=new FirstFragment(bookModel.getAll());
        onLoanFragment=new FirstFragment(bookModel.getloanBooks());
        historyFragment=new FirstFragment(R.layout.fragment_history_search_list_item,new HistoryModel());


        inputFragment=new InputFragment();
        searchFragment=new SearchFragment();

        fragmentManager = getSupportFragmentManager();

        borrowingFrgment=new BorrowingFrgment();
        // 开启一个事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentContainer, firstFragment);
        // 提交事务
        fragmentTransaction.commit();

    }





    private void initPopMenu(){
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        popMenu = new PopMenu.Builder().attachToActivity(MainActivity.this)
                .addMenuItem(new PopMenuItem("添加", getResources().getDrawable(R.drawable.tabbar_compose_idea)))
                .addMenuItem(new PopMenuItem("查找", getResources().getDrawable(R.drawable.tabbar_compose_select)))
                .addMenuItem(new PopMenuItem("全部图书", getResources().getDrawable(R.drawable.tabbar_compose_data)))
                .addMenuItem(new PopMenuItem("已借出", getResources().getDrawable(R.drawable.tabbar_compose_borrow)))
                .addMenuItem(new PopMenuItem("历史借阅", getResources().getDrawable(R.drawable.tabbar_compose_history)))
                .addMenuItem(new PopMenuItem("借书", getResources().getDrawable(R.drawable.tabbar_compose_book)))
                .addMenuItem(new PopMenuItem("还书", getResources().getDrawable(R.drawable.tabbar_compose_book1)))
                .setOnItemClickListener(new PopMenuItemListener() {
                    @Override
                    public void onItemClick(PopMenu popMenu, int position) {
                        switch (position){
                            case 0:
                                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,inputFragment).commit();
                                break;
                            case 1:
                                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,searchFragment).commit();
                                break;
                            case 2:
                                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,firstFragment).commit();
                                break;
                            case 3:
                                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,onLoanFragment).commit();
                                break;
                            case 4:
                                fragmentManager.beginTransaction().replace(R.id.fragmentContainer,historyFragment).commit();
                                break;
                            case 5:
                                fragmentTarger=1;
                                openQR();
                                break;
                            case 6:
                                fragmentTarger=2;
                                openQR();
                                break;


                        }

                    }
                }).build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        findViewById(R.id.rl_bottom_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!popMenu.isShowing()) {
                    popMenu.show();
                }
            }
        });

    }


    /**
     * 扫描二维码
     */
    private void openQR(){
        AndPermission.with(MainActivity.this)
                .runtime()
                .permission(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)
                .onGranted(permissions -> {
                    // Storage permission are allowed.
                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    ZxingConfig config = new ZxingConfig();
                    config.setShowbottomLayout(true);//底部布局（包括闪光灯和相册）
                    config.setPlayBeep(true);//是否播放提示音
                    config.setShake(true);//是否震动
                    config.setShowAlbum(true);//是否显示相册
                    config.setShowFlashLight(true);//是否显示闪光灯
                    intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                })
                .onDenied(permissions -> {
                    // Storage permission are not allowed.
                }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                switch (fragmentTarger){
                    case 1:
                        Bundle bundle = new Bundle();
                        bundle.putString("isbn", content);
                        borrowingFrgment.setArguments(bundle);
                        fragmentManager.beginTransaction().replace(R.id.fragmentContainer,borrowingFrgment).commit();
                        break;
                    case 2:
                        try {
                            Book book = bookModel.findByIsbn(content);
                            bookModel.setStillBook(book.getId());
                            fragmentManager.beginTransaction().replace(R.id.fragmentContainer,firstFragment).commit();
                            Toast.makeText(this, "还书成功！！", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(this, "该书不存在！！", Toast.LENGTH_SHORT).show();

                        }

                }


                Log.d(TAG, "获取到的数据: "+content);
            }
        }
    }

}
