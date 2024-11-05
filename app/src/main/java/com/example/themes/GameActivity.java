package com.example.themes;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Intent.getIntent;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private boolean isBot;
    private boolean isPlayerXTurn = true; // Чей сейчас ход, true = X, false = O
    private String[][] board = new String[3][3]; // Игровое поле
    private Button[][] buttons = new Button[3][3]; // Кнопки на экране
    private TextView statusTextView;

    private int playerWins = 0, botWins = 0, draws = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadTheme();
        setContentView(R.layout.activity_game);
        statusTextView = findViewById(R.id.statusTextView);

        isBot = getIntent().getBooleanExtra("isBot", false);

        buttons[0][0] = findViewById(R.id.button00);
        buttons[0][1] = findViewById(R.id.button01);
        buttons[0][2] = findViewById(R.id.button02);
        buttons[1][0] = findViewById(R.id.button03);
        buttons[1][1] = findViewById(R.id.button04);
        buttons[1][2] = findViewById(R.id.button05);
        buttons[2][0] = findViewById(R.id.button06);
        buttons[2][1] = findViewById(R.id.button07);
        buttons[2][2] = findViewById(R.id.button08);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int finalI = i;
                final int finalJ = j;
                buttons[i][j].setOnClickListener(view -> onPlayerMove(finalI, finalJ));
            }
        }

        loadStatistics();
        resetBoard();
    }

    private void onPlayerMove(int row, int col) {
        if (board[row][col] != null) {
            return;
        }

        if (isPlayerXTurn) {
            board[row][col] = "X";
            buttons[row][col].setText("X");
        } else {
            board[row][col] = "O";
            buttons[row][col].setText("O");
        }

        if (checkWin()) {
            updateStatus(isPlayerXTurn ? "X победил!" : "O победил!");
            updateStatistics(isPlayerXTurn);
            return;
        }

        if (isBoardFull()) {
            updateStatus("Ничья!");
            updateStatistics(false);
            return;
        }

        isPlayerXTurn = !isPlayerXTurn;

        // Если включен режим с ботом, бот должен ходить после игрока
        if (isBot && !isPlayerXTurn) {
            botMove();
        }
    }

    private void botMove() {
        Random random = new Random();
        int row, col;

        do {
            row = random.nextInt(3);
            col = random.nextInt(3);
        } while (board[row][col] != null);

        board[row][col] = "O";
        buttons[row][col].setText("O");

        if (checkWin()) {
            updateStatus("O (бот) победил!");
            updateStatistics(false);
            return;
        }

        if (isBoardFull()) {
            updateStatus("Ничья!");
            updateStatistics(false);
            return;
        }

        isPlayerXTurn = !isPlayerXTurn;
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != null && board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2])) {
                return true;
            }
            if (board[0][i] != null && board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i])) {
                return true;
            }
        }
        if (board[0][0] != null && board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2])) {
            return true;
        }
        if (board[0][2] != null && board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0])) {
            return true;
        }

        return false;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void updateStatus(String status) {
        statusTextView.setText(status);
    }

    private void updateStatistics(boolean playerWon) {
        SharedPreferences preferences = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        if (playerWon) {
            playerWins++;
            editor.putInt("playerWins", playerWins);
        } else if (isBot && !isPlayerXTurn) {
            botWins++;
            editor.putInt("botWins", botWins);
        } else {
            draws++;
            editor.putInt("draws", draws);
        }

        editor.apply();
        resetBoard();
    }

    private void loadStatistics() {
        SharedPreferences preferences = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        playerWins = preferences.getInt("playerWins", 0);
        botWins = preferences.getInt("botWins", 0);
        draws = preferences.getInt("draws", 0);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = null;
                buttons[i][j].setText("");
            }
        }
        isPlayerXTurn = true;
    }

    private void loadTheme() {
        SharedPreferences preferences = getSharedPreferences("TicTacToePrefs", MODE_PRIVATE);
        boolean darkTheme = preferences.getBoolean("darkTheme", false);
        if (darkTheme) {
            setTheme(R.style.Base_Theme_Themes);
        } else {
            setTheme(R.style.Theme_Themes);
        }
    }
}
