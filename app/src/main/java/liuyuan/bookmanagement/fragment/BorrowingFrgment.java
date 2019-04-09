package liuyuan.bookmanagement.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import liuyuan.bookmanagement.R;
import liuyuan.bookmanagement.mapping.Book;
import liuyuan.bookmanagement.model.BookModel;

public class BorrowingFrgment extends Fragment implements View.OnClickListener{

    private TextView bookIsbnText;
    private TextView bookNameText;
    private BookModel bookModel;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.borrowing_book, container, false);
        initView(view);
        return view;
    }



    private void initView(View view){
        bookModel=new BookModel();
        bookIsbnText = view.findViewById(R.id.bookIsbnText);
        bookIsbnText.setText(getArguments().getString("isbn"));
        bookNameText = view.findViewById(R.id.bookNameText);
        view.findViewById(R.id.button).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        try {
            Book book = bookModel.findByIsbn(bookIsbnText.getText().toString());
            bookModel.setLoanBook(book.getId(),bookNameText.getText().toString());
            Toast.makeText(getActivity(), "借书成功！！！", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "借书失败", Toast.LENGTH_SHORT).show();
        }
    }
}
