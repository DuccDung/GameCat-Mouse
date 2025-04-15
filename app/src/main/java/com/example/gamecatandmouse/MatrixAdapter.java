package com.example.gamecatandmouse;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.Queue;

public class MatrixAdapter extends RecyclerView.Adapter<MatrixAdapter.MatrixViewHolder> {
    private Context context;
    private int[][] matrix;  // Ma trận lưu trữ các ô
    private int goalX = -1, goalY = -1;
    private int selectedX = 1, selectedY = 1;  // Vị trí phần tử đỏ
    private int selectedX2 = 18, selectedY2 = 18; // Vị trí phần tử thứ hai (màu khác)

    public void setPointMouse(int x , int y){

    }
    public  interface notification{
        void checkNotify(boolean check); // true -> win , false -> lost
    }
    notification notification;
    public MatrixAdapter(Context context, int _matrix[][]) {
        this.context = context;
        this.notification = (MatrixAdapter.notification) context;
        matrix = _matrix;
        matrix[selectedX][selectedY] = 1;
        matrix[selectedX2][selectedY2] = 3;

        // Tìm vị trí goal
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == 3) {
                    goalX = i;
                    goalY = j;
                }
            }
        }
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

        if (row == selectedX && col == selectedY && row == goalX && col == goalY) {
            // Mèo đang đứng trên goal
            holder.itemView.setBackgroundColor(Color.MAGENTA);
        } else if (matrix[row][col] == 1) {
            holder.imgMember.setImageResource(R.drawable.cat);
        }else if (matrix[row][col] == 6) {
            holder.imgMember.setImageResource(R.drawable.gate);
        }
        else if (matrix[row][col] == -1) {
            holder.imgMember.setImageResource(R.drawable.member_wall);

        } else if (matrix[row][col] == 3) {
            holder.imgMember.setImageResource(R.drawable.mouse);
        }
        else if (matrix[row][col] == 5) {
            holder.imgMember.setImageResource(R.drawable.srrounded_wall);
        }
        else {
            holder.imgMember.setImageResource(R.drawable.wall);
        }
    }

    @Override
    public int getItemCount() {
        return matrix.length * matrix[0].length;  // Tổng số phần tử trong ma trận
    }
    public boolean isAtGoal() {
        return selectedX == goalX && selectedY == goalY;
    }
    public boolean isMouseAtGoal() {
        return selectedX2 == 3 && selectedY2 == 1;
    }
    public int getNextMoveBFS() {
        // Nếu đang đứng trên goal, return -2
        if (matrix[selectedX][selectedY] == 3) {
            return -2;
        }
       
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        int rows = matrix.length;
        int cols = matrix[0].length;

        boolean[][] visited = new boolean[rows][cols];
        Pair<Integer, Integer>[][] parent = new Pair[rows][cols];

        Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
        queue.add(new Pair<>(selectedX, selectedY));
        visited[selectedX][selectedY] = true;

        boolean found = false;
        int goalX = -1, goalY = -1;

        while (!queue.isEmpty()) {
            Pair<Integer, Integer> current = queue.poll();
            int x = current.first;
            int y = current.second;

            if (matrix[x][y] == 3) {
                goalX = x;
                goalY = y;
                found = true;
                break;
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                if (nx >= 0 && ny >= 0 && nx < rows && ny < cols && !visited[nx][ny] && matrix[nx][ny] != -1&& matrix[nx][ny] != 5 &&matrix[nx][ny] != 6) {
                    visited[nx][ny] = true;
                    parent[nx][ny] = new Pair<>(x, y);
                    queue.add(new Pair<>(nx, ny));
                }
            }
        }

        if (!found) return -1; // Không tìm thấy đường

        // Truy ngược từ goal về mèo để tìm bước đầu tiên
        int cx = goalX, cy = goalY;
        Pair<Integer, Integer> nextMove = null;

        while (parent[cx][cy] != null) {
            if (parent[cx][cy].first == selectedX && parent[cx][cy].second == selectedY) {
                nextMove = new Pair<>(cx, cy);
                break;
            }
            Pair<Integer, Integer> p = parent[cx][cy];
            cx = p.first;
            cy = p.second;
        }

        if (nextMove == null) return -1;

        int nx = nextMove.first, ny = nextMove.second;
        if (nx < selectedX) return 0;  // lên
        if (nx > selectedX) return 1;  // xuống
        if (ny < selectedY) return 2;  // trái
        if (ny > selectedY) return 3;  // phải

        return -1;
    }
    public void moveItem(int direction, int part) {
        if (part == 1) {  // Di chuyển phần tử đỏ
            matrix[selectedX][selectedY] = 0;  // Xóa vị trí cũ
            switch (direction) {
                case 0:  // Lên
                    if (selectedX > 1 &&  matrix[selectedX][selectedY] != -1) selectedX--;
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
        } else if (part == 2) {  // Di chuyển Chuột
            int newX = selectedX2;
            int newY = selectedY2;
            if ( newX == 3 && newY ==1) { // xử lý chuột thắng

                notification.checkNotify(true);
                return;
            }
            switch (direction) {
                case 0:  // Lên
                    if (selectedX2 > 0) newX--;
                    break;
                case 1:  // Xuống
                    if (selectedX2 < matrix.length - 1) newX++;
                    break;
                case 2:  // Trái
                    if (selectedY2 > 0) newY--;
                    break;
                case 3:  // Phải
                    if (selectedY2 < matrix[0].length - 1) newY++;
                    break;
            }

            // Chỉ di chuyển nếu ô mới không phải là vật cản (-1) hoặc tường đặc biệt (5)
            if (matrix[newX][newY] != -1 && matrix[newX][newY] != 5 ) {
                matrix[selectedX2][selectedY2] = 0;  // Xóa vị trí cũ
                selectedX2 = newX;
                selectedY2 = newY;
                matrix[selectedX2][selectedY2] = 3;  // Đặt phần tử chuột ở vị trí mới
            }
        }

        notifyDataSetChanged();  // Cập nhật RecyclerView
    }

    public static class MatrixViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgMember;
        public MatrixViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMember = itemView.findViewById(R.id.itemMemberGame);
        }
    }
}


