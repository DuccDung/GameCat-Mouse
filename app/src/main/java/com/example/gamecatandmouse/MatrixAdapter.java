package com.example.gamecatandmouse;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MatrixAdapter extends RecyclerView.Adapter<MatrixAdapter.MatrixViewHolder> {
    private Context context;
    private int[][] matrix;  // Ma trận lưu trữ các ô
    private int selectedX = 0, selectedY = 0;  // Vị trí phần tử đỏ
    private int selectedX2 = 0, selectedY2 = 1; // Vị trí phần tử thứ hai (màu khác)

    public MatrixAdapter(Context context, int rows, int cols) {
        this.context = context;
        matrix = new int[rows][cols];  // Tạo ma trận
        matrix[selectedX][selectedY] = 1;  // Phần tử đỏ tại vị trí đầu
        matrix[selectedX2][selectedY2] = 2; // Phần tử thứ hai (màu khác)
    }

    @NonNull
    @Override
    public MatrixViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itembase, parent, false);
        return new MatrixViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatrixViewHolder holder, int position) {
        int row = position / matrix[0].length;  // Tính chỉ số hàng
        int col = position % matrix[0].length;  // Tính chỉ số cột

        if (matrix[row][col] == 1) {
            holder.itemView.setBackgroundColor(Color.RED);  // Phần tử đỏ
        } else if (matrix[row][col] == 2) {
            holder.itemView.setBackgroundColor(Color.GREEN);  // Phần tử thứ hai (màu khác)
        } else {
            holder.itemView.setBackgroundColor(Color.GRAY);  // Các phần tử xám
        }
    }

    @Override
    public int getItemCount() {
        return matrix.length * matrix[0].length;  // Tổng số phần tử trong ma trận
    }

    public void moveItem(int direction, int part) {
        if (part == 1) {  // Di chuyển phần tử đỏ
            matrix[selectedX][selectedY] = 0;  // Xóa vị trí cũ
            switch (direction) {
                case 0:  // Lên
                    if (selectedX > 0) selectedX--;
                    break;
                case 1:  // Xuống
                    if (selectedX < matrix.length - 1) selectedX++;
                    break;
                case 2:  // Trái
                    if (selectedY > 0) selectedY--;
                    break;
                case 3:  // Phải
                    if (selectedY < matrix[0].length - 1) selectedY++;
                    break;
            }
            matrix[selectedX][selectedY] = 1;  // Đặt phần tử đỏ ở vị trí mới
        } else if (part == 2) {  // Di chuyển phần tử thứ hai
            matrix[selectedX2][selectedY2] = 0;  // Xóa vị trí cũ
            switch (direction) {
                case 0:  // Lên
                    if (selectedX2 > 0) selectedX2--;
                    break;
                case 1:  // Xuống
                    if (selectedX2 < matrix.length - 1) selectedX2++;
                    break;
                case 2:  // Trái
                    if (selectedY2 > 0) selectedY2--;
                    break;
                case 3:  // Phải
                    if (selectedY2 < matrix[0].length - 1) selectedY2++;
                    break;
            }
            matrix[selectedX2][selectedY2] = 2;  // Đặt phần tử thứ hai ở vị trí mới
        }
        notifyDataSetChanged();  // Cập nhật RecyclerView
    }

    public static class MatrixViewHolder extends RecyclerView.ViewHolder {
        public MatrixViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}


