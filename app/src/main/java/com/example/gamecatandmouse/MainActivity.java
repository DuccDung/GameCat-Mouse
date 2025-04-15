package com.example.gamecatandmouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MatrixAdapter.notification {
    private RecyclerView recyclerView;
    private MatrixAdapter matrixAdapter;
    private ImageView upButton, downButton, leftButton, rightButton;
    private Button upButton2;
    Handler handler = new Handler();
    Runnable currentRunnable = null;
    private FrameLayout overlay;
    private FrameLayout overlayVictory;
    private FrameLayout overlayDefect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeStatusBarTransparent();
        applyTopPadding();
        overlay = findViewById(R.id.overlay);
        recyclerView = findViewById(R.id.rcvMatrixGame);
        overlayDefect = findViewById(R.id.overlayDefect);
        overlayVictory = findViewById(R.id.overlayWin);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 20));  // 10 cột trong ma trận

        int[][] matrix = new int[20][20];
        // Đặt tất cả là 0
        for (int row = 0; row < 20; row++) {
            for (int col = 0; col < 20; col++) {
                matrix[row][col] = 0;
            }
        }



        matrix[3][1] = -1;
        matrix[4][1] = -1;
        matrix[4][2] = -1;
        matrix[13][2] = -1;
        matrix[2][3] = -1;
        matrix[13][3] = -1;
        matrix[2][3] = -1;
        matrix[4][4] = -1;
        matrix[5][4] = -1;
        matrix[2][5] = -1;
        matrix[3][5] = -1;
        matrix[9][5] = -1;
        matrix[10][5] = -1;
        matrix[11][5] = -1;
        matrix[12][5] = -1;
        matrix[6][6] = -1;
        matrix[9][6] = -1;
        matrix[15][6] = -1;
        matrix[3][7] = -1;
        matrix[11][7] = -1;
        matrix[4][9] = -1;
        matrix[6][9] = -1;
        matrix[9][9] = -1;
        matrix[10][9] = -1;
        matrix[11][9] = -1;
        matrix[12][9] = -1;
        matrix[15][9] = -1;
        matrix[17][9] = -1;
        matrix[2][10] = -1;
        matrix[6][10] = -1;
        matrix[9][10] = -1;
        matrix[1][11] = -1;
        matrix[3][11] = -1;
        matrix[6][11] = -1;
        matrix[9][11] = -1;
        matrix[2][12] = -1;
        matrix[6][12] = -1;
        matrix[9][12] = -1;
        matrix[11][12] = -1;
        matrix[16][12] = -1;
        matrix[6][13] = -1;
        matrix[9][13] = -1;
        matrix[5][14] = -1;
        matrix[9][14] = -1;
        matrix[4][15] = -1;
        matrix[5][15] = -1;
        matrix[6][15] = -1;
        matrix[9][15] = -1;
        matrix[16][16] = -1;
        matrix[16][17] = -1;
        matrix[19][17] = -1;
        matrix[8][18] = -1;
        matrix[13][18] = -1;
        matrix[16][18] = -1;
        matrix[3][1] = 6;
// Tường viền
        for (int i = 0; i < 20; i++) {
            matrix[0][i] = 5;
            matrix[19][i] = 5;
            matrix[i][0] = 5;
            matrix[i][19] = 5;
        }
        matrixAdapter = new MatrixAdapter(this, matrix);
        recyclerView.setAdapter(matrixAdapter);


        // Các nút điều hướng cho phần tử đỏ
        upButton = findViewById(R.id.up_button);
        downButton = findViewById(R.id.down_button);
        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);

        // Các nút điều hướng cho phần tử thứ hai (màu khác)
        upButton2 = findViewById(R.id.up_button2);
        upButton2.setOnClickListener(v -> autoMoveStepByStep());
        overlayVictory.setOnClickListener(v->{
            Intent intent = new Intent(this ,Level.class);
            startActivity(intent);
            finish();
        });
        overlayDefect.setOnClickListener(v->{
            Intent intent = new Intent(this ,Level.class);
            startActivity(intent);
            finish();
        });
        Runnable runnableUp = new Runnable() {
            @Override
            public void run() {
                matrixAdapter.moveItem(0, 2);
                handler.postDelayed(this, 1000);
            }
        };

        Runnable runnableDown = new Runnable() {
            @Override
            public void run() {
                matrixAdapter.moveItem(1, 2);
                handler.postDelayed(this, 1000);
            }
        };

        Runnable runnableLeft = new Runnable() {
            @Override
            public void run() {
                matrixAdapter.moveItem(2, 2);
                handler.postDelayed(this, 1000);
            }
        };

        Runnable runnableRight = new Runnable() {
            @Override
            public void run() {
                matrixAdapter.moveItem(3, 2);
                handler.postDelayed(this, 1000);
            }
        };
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRunnable(runnableUp);
            }
        });

        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRunnable(runnableDown);
            }
        });

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRunnable(runnableLeft);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRunnable(runnableRight);
            }
        });

    }
    private void startRunnable(Runnable runnable) {
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable);
        }
        currentRunnable = runnable;
        handler.post(runnable);
    }
    private void autoMoveStepByStep() {
        overlay.setVisibility(View.GONE);
        int direction = matrixAdapter.getNextMoveBFS();
        if (matrixAdapter.isMouseAtGoal()) {
            overlayVictory.setVisibility(View.VISIBLE);
            handler.removeCallbacksAndMessages(null);
            return;
        }
        if (direction == -1) {
            overlayDefect.setVisibility(View.VISIBLE);
            handler.removeCallbacksAndMessages(null);
            return;
        }

        matrixAdapter.moveItem(direction, 1);

        new Handler().postDelayed(this::autoMoveStepByStep, 1500);
    }

    private void makeStatusBarTransparent() {
        Window window = getWindow();

        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );

        window.setStatusBarColor(Color.TRANSPARENT);
    }
    private void applyTopPadding() {
        View contentContainer = findViewById(R.id.fragment_container);

        if (contentContainer != null) {
            int statusBarHeight = getStatusBarHeight();
            contentContainer.setPadding(0, statusBarHeight, 0, 0);
        }
    }
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void checkNotify(boolean check) {
        if(check == true){
            overlayVictory.setVisibility(View.VISIBLE);
            handler.removeCallbacksAndMessages(null);
        }
        else {

        }
    }
}

