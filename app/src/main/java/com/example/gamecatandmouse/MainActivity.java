package com.example.gamecatandmouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.Button;
import android.graphics.Rect;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MatrixAdapter matrixAdapter;
    private Button upButton, downButton, leftButton, rightButton, upButton2, downButton2, leftButton2, rightButton2;
    private Handler handlerPart1 = new Handler();
    private Runnable runnablePart1 = null;
    private Handler handlerPart2 = new Handler();
    private Runnable runnablePart2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.rcvMatrixGame);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 20));  // 10 cột trong ma trận

        int[][] matrix = new int[20][20];
        matrix[2][2] = 3;
        matrixAdapter = new MatrixAdapter(this, matrix);
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
        upButton2.setOnClickListener(v -> autoMoveStepByStep());

    }

    private void autoMoveStepByStep() {
        int direction = matrixAdapter.getNextMoveBFS();

        if (direction == -2 || matrixAdapter.isAtGoal()) {
            Toast.makeText(this, "Tới đích!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (direction == -1) {
            Toast.makeText(this, "Không tìm thấy đường!", Toast.LENGTH_SHORT).show();
            return;
        }

        matrixAdapter.moveItem(direction, 1);

        new Handler().postDelayed(this::autoMoveStepByStep, 200);
    }




}

