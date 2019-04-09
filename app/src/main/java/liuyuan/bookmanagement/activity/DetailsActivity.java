package liuyuan.bookmanagement.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.Calendar;

import liuyuan.bookmanagement.R;
import liuyuan.bookmanagement.mapping.Book;


public class DetailsActivity extends AppCompatActivity {
    private TextView dateEdit;
    private EditText bookDateEditText;
    private Calendar showDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        Intent intent = getIntent();
        // 拿书
        getBook(intent);
        showDate = Calendar.getInstance();
        bookDateEditText = (EditText)this.findViewById(R.id.bookDateEditText);
        bookDateEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    onClickPublicationTime();
                    return true;
                } else {
                    return false;
                }
            }
        });
        // 更新
        Button updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                // 校验
                if(!checkNotNull()){
                    Toast.makeText(DetailsActivity.this, "请检查数据，字段不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    updateBook(intent);
                    finish();
                }

                // 返回刷新上一页???

            }
        });

        // 删除
        Button removeButton = (Button) findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                removeBook();

                // 返回刷新上一页???

                finish();
            }
        });
        findViewById(R.id.iv_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 点击选择出版时间
     *
     */
    public void onClickPublicationTime() {
        String publicationDateStr = bookDateEditText.getText().toString();
        if (!TextUtils.isEmpty(publicationDateStr)&&publicationDateStr.contains("-")) {
            String[] split = publicationDateStr.split("-");
            showDate.set(Integer.parseInt(split[0]),
                    Integer.parseInt(split[1])-1, Integer.parseInt(split[2]));
        }
        else{
            showDate.setTimeInMillis(System.currentTimeMillis());
        }
        showDatePickerDialog();
    }

    /**
     * 显示时间捡取器
     */
    private void showDatePickerDialog() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                showDate.set(Calendar.YEAR, year);
                showDate.set(Calendar.MONTH, monthOfYear);
                showDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                bookDateEditText.setText(DateFormat.format("yyyy-MM-dd", showDate));
            }
        }, showDate.get(Calendar.YEAR), showDate.get(Calendar.MONTH), showDate.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void getBook(Intent intent) {
        Long bookId = intent.getLongExtra("bookId",0l);
        Double price = intent.getDoubleExtra("price", 0.00);
        TextView bookIdTextView = (TextView) findViewById(R.id.bookNumberEditText);
        bookIdTextView.setText(String.valueOf(bookId));bookIdTextView.setFocusable(false);
        TextView bookNameTextView = (TextView) findViewById(R.id.bookNameEditText);
        bookNameTextView.setText(intent.getStringExtra("bookName"));
        TextView typeTextView = (TextView) findViewById(R.id.bookTypeEditText);
        typeTextView.setText(intent.getStringExtra("type"));
        TextView authorTextView = (TextView) findViewById(R.id.bookAuthorEditText);
        authorTextView.setText(intent.getStringExtra("author"));
        TextView priceTextView = (TextView) findViewById(R.id.bookPriceEditText);
        priceTextView.setText(price.toString());
        TextView borrowerTextView = (TextView) findViewById(R.id.bookBorrowerEditText);
        borrowerTextView.setText(intent.getStringExtra("borrower"));
        TextView publicationDateTextView = (TextView) findViewById(R.id.bookDateEditText);
        publicationDateTextView.setText(intent.getStringExtra("publicationDate"));
        TextView bookIsbnEditText = (TextView) findViewById(R.id.bookIsbnEditText);
        bookIsbnEditText.setText(intent.getStringExtra("isbn"));
    }

    private void updateBook(Intent intent) {
        TextView bookIdTextView = (TextView) findViewById(R.id.bookNumberEditText);
        TextView bookNameTextView = (TextView) findViewById(R.id.bookNameEditText);
        TextView typeTextView = (TextView) findViewById(R.id.bookTypeEditText);
        TextView authorTextView = (TextView) findViewById(R.id.bookAuthorEditText);
        TextView priceTextView = (TextView) findViewById(R.id.bookPriceEditText);
        TextView borrowerTextView = (TextView) findViewById(R.id.bookBorrowerEditText);
        TextView publicationDateTextView = (TextView) findViewById(R.id.bookDateEditText);
        TextView bookIsbnEditText = (TextView) findViewById(R.id.bookIsbnEditText);

        Book book = new Book();
        book.setBookname(bookNameTextView.getText().toString());
        book.setIsbn(bookIsbnEditText.getText().toString());
        book.setType(typeTextView.getText().toString());
        book.setAuthor(authorTextView.getText().toString());
        book.setPrice(Double.valueOf(priceTextView.getText().toString()));
        book.setBorrower(borrowerTextView.getText().toString());
        book.setPublicationDate(publicationDateTextView.getText().toString());
        book.update(intent.getLongExtra("bookId",0l));
        Toast.makeText(DetailsActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
    }

    private void removeBook() {
        TextView bookIdTextView = (TextView) findViewById(R.id.bookNumberEditText);
        String bookId = bookIdTextView.getText().toString();
        LitePal.delete(Book.class, Long.valueOf(bookId));
        Toast.makeText(DetailsActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
    }

    private boolean checkNotNull(){
        boolean isNotNull = true;
        if(TextUtils.isEmpty(((TextView) findViewById(R.id.bookNumberEditText)).getText())){
            isNotNull = false;
        } else if(TextUtils.isEmpty(((TextView) findViewById(R.id.bookNameEditText)).getText())){
            isNotNull = false;
        } else if(TextUtils.isEmpty(((TextView) findViewById(R.id.bookTypeEditText)).getText())){
            isNotNull = false;
        } else if(TextUtils.isEmpty(((TextView) findViewById(R.id.bookAuthorEditText)).getText())){
            isNotNull = false;
        } else if(TextUtils.isEmpty(((TextView) findViewById(R.id.bookPriceEditText)).getText())){
            isNotNull = false;
        }
//        else if(TextUtils.isEmpty(((TextView) findViewById(R.id.bookBorrowerEditText)).getText())){
//            isNotNull = false;
//        }
        else if(TextUtils.isEmpty(((TextView) findViewById(R.id.bookDateEditText)).getText())){
            isNotNull = false;
        }
        return isNotNull;
    }
}
