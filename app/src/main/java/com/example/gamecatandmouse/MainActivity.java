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
    private Button upButton, downButton, leftButton, rightButton, upButton2, downButton2, leftButton2, rightButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rcvMatrixGame);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 20));  // 10 cột trong ma trận

        matrixAdapter = new MatrixAdapter(this, 20, 20);  // Ma trận 20x10
        recyclerView.setAdapter(matrixAdapter);

        // Các nút điều hướng cho phần tử đỏ
        upButton = findViewById(R.id.up_button);
        downButton = findViewById(R.id.down_button);
        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);

        // Các nút điều hướng cho phần tử thứ hai (màu khác)
        upButton2 = findViewById(R.id.up_button2);
        downButton2 = findViewById(R.id.down_button2);
        leftButton2 = findViewById(R.id.left_button2);
        rightButton2 = findViewById(R.id.right_button2);

        // Điều khiển phần tử đỏ
        upButton.setOnClickListener(v -> matrixAdapter.moveItem(0, 1));  // Di chuyển lên
        downButton.setOnClickListener(v -> matrixAdapter.moveItem(1, 1));  // Di chuyển xuống
        leftButton.setOnClickListener(v -> matrixAdapter.moveItem(2, 1));  // Di chuyển trái
        rightButton.setOnClickListener(v -> matrixAdapter.moveItem(3, 1));  // Di chuyển phải

        // Điều khiển phần tử thứ hai
        upButton2.setOnClickListener(v -> matrixAdapter.moveItem(0, 2));  // Di chuyển lên
        downButton2.setOnClickListener(v -> matrixAdapter.moveItem(1, 2));  // Di chuyển xuống
        leftButton2.setOnClickListener(v -> matrixAdapter.moveItem(2, 2));  // Di chuyển trái
        rightButton2.setOnClickListener(v -> matrixAdapter.moveItem(3, 2));  // Di chuyển phải
    }
}

