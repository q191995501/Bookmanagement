package liuyuan.bookmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.litepal.LitePal;

import java.util.List;

import liuyuan.bookmanagement.R;
import liuyuan.bookmanagement.adapter.BookAdapter;
import liuyuan.bookmanagement.mapping.Book;

public class SearchBookActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,View.OnClickListener{

    private ListView lvBook;

    private List<Book> books;

    private BookAdapter bookAdapter;

    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_search_list);
        Intent intent = this.getIntent();
        lvBook = (ListView) findViewById(R.id.lv_book_list);

        name= getIntent().getStringExtra("name");
        books = LitePal.where("bookname like ? ","%"+name+"%").find(Book.class);
        bookAdapter = new BookAdapter(SearchBookActivity.this,books);
        lvBook.setAdapter(bookAdapter);
        lvBook.setOnItemClickListener(this);
        View searchBack = findViewById(R.id.search_back);
        searchBack.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        books = LitePal.where("bookname like ? ","%"+name+"%").find(Book.class);
        bookAdapter = new BookAdapter(SearchBookActivity.this,books);
        lvBook.setAdapter(bookAdapter);
    }


    // 详情
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bookId", books.get(position).getId());
        bundle.putSerializable("bookName", books.get(position).getBookname());
        bundle.putSerializable("type", books.get(position).getType());
        bundle.putSerializable("author", books.get(position).getAuthor());
        bundle.putSerializable("price", books.get(position).getPrice());
        bundle.putSerializable("borrower", books.get(position).getBorrower());
        bundle.putSerializable("publicationDate", books.get(position).getPublicationDate());
        bundle.putSerializable("isbn", books.get(position).getIsbn());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
