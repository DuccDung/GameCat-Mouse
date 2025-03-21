package com.example.gamecatandmouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.graphics.Rect;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MatrixAdapter matrixAdapter;
    private Button upButton, downButton, leftButton, rightButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rcvMatrixGame);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 10));  // 10 cột trong ma trận

        matrixAdapter = new MatrixAdapter(this, 20, 10);  // Ma trận 20x10
        recyclerView.setAdapter(matrixAdapter);

        // Bỏ margin giữa các item trong RecyclerView
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                // Không thêm margin nữa, set margin = 0
                outRect.set(0, 0, 0, 0);  // Bỏ margin ở tất cả các cạnh
            }
        });

        upButton = findViewById(R.id.up_button);
        downButton = findViewById(R.id.down_button);
        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);

        upButton.setOnClickListener(v -> matrixAdapter.moveItem(0));  // Di chuyển lên
        downButton.setOnClickListener(v -> matrixAdapter.moveItem(1));  // Di chuyển xuống
        leftButton.setOnClickListener(v -> matrixAdapter.moveItem(2));  // Di chuyển trái
        rightButton.setOnClickListener(v -> matrixAdapter.moveItem(3));  // Di chuyển phải
    }
}
