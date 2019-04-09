package liuyuan.bookmanagement.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.litepal.LitePal;

import java.util.List;

import liuyuan.bookmanagement.R;
import liuyuan.bookmanagement.activity.DetailsActivity;
import liuyuan.bookmanagement.adapter.BookAdapter;
import liuyuan.bookmanagement.mapping.Book;
import liuyuan.bookmanagement.model.IDataBookItem;

@SuppressLint("ValidFragment")
public class FirstFragment extends Fragment implements AdapterView.OnItemClickListener{

    private int REQUEST_CODE_SCAN=111;
    private Context mConetext;

    private IDataBookItem dataItem;
    private ListView list_books;
    private SwipeRefreshLayout refreshLayout;
    private List<Book> data;
    private BookAdapter bookAdapter;
    private Integer layout;

    public FirstFragment(IDataBookItem dataItem) {
        this.mConetext=getContext();
        this.dataItem=dataItem;
        this.layout=R.layout.fragment_search_list_item;
    }


    public FirstFragment(int layout,IDataBookItem dataItem) {
        this.mConetext=getContext();
        this.dataItem=dataItem;
        this.layout=layout;
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        data =  LitePal.findAll(Book.class);
        data =  dataItem.getData();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);

//        Book book = new Book();
//        book.setBookname("啦啦");
//        book.setAuthor("asda");
//        book.setBorrower("asdad");
//        book.setPrice(1212.12);
//        book.setPublicationDate("120-1230as");
//        book.setType("sd");
//        book.save();


        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout);
        list_books=view.findViewById(R.id.list_books);
        FragmentActivity activity = getActivity();
        bookAdapter = new BookAdapter(layout,activity, data);
        list_books.setAdapter(bookAdapter);
        list_books.setOnItemClickListener(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                data.clear();
                data.addAll(LitePal.findAll(Book.class));
                refreshLayout.setRefreshing(false);
                bookAdapter.notifyDataSetChanged();
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        data.clear();

        List<Book> list =dataItem.getData();
        if (list != null && list.size() > 0) {
            data.addAll(list);
        }
        bookAdapter = new BookAdapter(layout,getActivity(), data);
        list_books.setAdapter(bookAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Long bookId = data.get(position).getId();
        String bookname = data.get(position).getBookname();
        String type = data.get(position).getType();
        String author = data.get(position).getAuthor();
        Double price =  data.get(position).getPrice();
        String borrower = data.get(position).getBorrower();
        String publicationDate = data.get(position).getPublicationDate();
        String isbn = data.get(position).getIsbn();


        // 传参
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("bookId", bookId);
        bundle.putSerializable("bookName", bookname);
        bundle.putSerializable("type", type);
        bundle.putSerializable("author", author);
        bundle.putSerializable("price", price);
        bundle.putSerializable("borrower", borrower);
        bundle.putSerializable("publicationDate", publicationDate);
        bundle.putSerializable("isbn", isbn);
        intent.putExtras(bundle);
        intent.setClass(getActivity().getApplicationContext(),DetailsActivity.class);
        startActivity(intent);
    }
}
