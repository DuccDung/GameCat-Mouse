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
        matrixAdapter = new MatrixAdapter(this, matrix);

        matrixAdapter.setWall(3, 4); // Đặt tường tại vị trí (3,4)
        matrixAdapter.setWall(5, 5); // Tường tại (5,5)

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

        upButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tự tìm đường cho phần tử 2 (xanh) đến vị trí (5,5) chẳng hạn
                hillClimbMove(2, 3, 5);
            }
        });

    }
    private void hillClimbMove(int part, int goalX, int goalY) {
        final Handler handler = new Handler();
        final int[] currentX = new int[1];
        final int[] currentY = new int[1];

        if (part == 1) {
            currentX[0] = matrixAdapter.getSelectedX1();
            currentY[0] = matrixAdapter.getSelectedY1();
        } else {
            currentX[0] = matrixAdapter.getSelectedX2();
            currentY[0] = matrixAdapter.getSelectedY2();
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (currentX[0] == goalX && currentY[0] == goalY) {
                    return; // Đã đến đích
                }

                int up = (currentY[0] - 1 >= 0 && matrixAdapter.getMatrix()[currentX[0]][currentY[0] - 1] != -1)
                        ? distance(currentX[0], currentY[0] - 1, goalX, goalY)
                        : Integer.MAX_VALUE;

                int down = (currentY[0] + 1 < matrixAdapter.getRowCount() && matrixAdapter.getMatrix()[currentX[0]][currentY[0] + 1] != -1)
                        ? distance(currentX[0], currentY[0] + 1, goalX, goalY)
                        : Integer.MAX_VALUE;

                int left = (currentX[0] - 1 >= 0 && matrixAdapter.getMatrix()[currentX[0] - 1][currentY[0]] != -1)
                        ? distance(currentX[0] - 1, currentY[0], goalX, goalY)
                        : Integer.MAX_VALUE;

                int right = (currentX[0] + 1 < matrixAdapter.getColCount() && matrixAdapter.getMatrix()[currentX[0] + 1][currentY[0]] != -1)
                        ? distance(currentX[0] + 1, currentY[0], goalX, goalY)
                        : Integer.MAX_VALUE;

                int[] moves = {up, right, down, left};
                int minIndex = 0;
                int minValue = moves[0];
                for (int i = 1; i < 4; i++) {
                    if (moves[i] < minValue) {
                        minValue = moves[i];
                        minIndex = i;
                    }
                }

                switch (minIndex) {
                    case 0:
                        currentY[0]--;
                        matrixAdapter.moveItem(0, part);
                        break;
                    case 1:
                        currentX[0]++;
                        matrixAdapter.moveItem(3, part); // Lưu ý: moveItem(3, part) là phải
                        break;
                    case 2:
                        currentY[0]++;
                        matrixAdapter.moveItem(1, part);
                        break;
                    case 3:
                        currentX[0]--;
                        matrixAdapter.moveItem(2, part);
                        break;
                }

                if (minValue != Integer.MAX_VALUE && (currentX[0] != goalX || currentY[0] != goalY)) {
                    handler.postDelayed(this, 300); // Chạy tiếp sau 300ms
                }
            }
        };

        handler.post(runnable);
    }

    private int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

}

