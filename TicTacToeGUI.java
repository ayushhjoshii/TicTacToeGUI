import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class TicTacToeGUI extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private JLabel statusLabel;

    private boolean isVsComputer = false;
    private String playerXName = "Player X";
    private String playerOName = "Player O";
    private String difficulty = "Easy";
    private Random random = new Random();

    public TicTacToeGUI() {
        // Get game mode and player names
        chooseGameMode();

        setTitle("Tic Tac Toe");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusLabel = new JLabel(playerXName + "'s turn (X)");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        initializeBoard(boardPanel);
        add(boardPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void chooseGameMode() {
        String[] options = {"Two Player", "Play vs Computer"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose Game Mode:",
                "Tic Tac Toe",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == 1) {
            isVsComputer = true;
            playerXName = JOptionPane.showInputDialog(this, "Enter your name (Player X):");
            if (playerXName == null || playerXName.trim().isEmpty()) playerXName = "Player X";
            playerOName = "Computer";
            chooseDifficulty();
        } else {
            isVsComputer = false;
            playerXName = JOptionPane.showInputDialog(this, "Enter name for Player X:");
            if (playerXName == null || playerXName.trim().isEmpty()) playerXName = "Player X";
            playerOName = JOptionPane.showInputDialog(this, "Enter name for Player O:");
            if (playerOName == null || playerOName.trim().isEmpty()) playerOName = "Player O";
        }
    }

    private void chooseDifficulty() {
        String[] levels = {"Easy", "Medium", "Hard"};
        difficulty = (String) JOptionPane.showInputDialog(
                this,
                "Choose Difficulty Level:",
                "Difficulty",
                JOptionPane.PLAIN_MESSAGE,
                null,
                levels,
                levels[0]);
        if (difficulty == null) difficulty = "Easy";
    }

    private void initializeBoard(JPanel boardPanel) {
        Font font = new Font("Arial", Font.BOLD, 60);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton("");
                btn.setFont(font);
                buttons[i][j] = btn;
                final int row = i;
                final int col = j;

                btn.addActionListener(e -> {
                    if (btn.getText().equals("") && (!isVsComputer || currentPlayer == 'X')) {
                        btn.setText(String.valueOf(currentPlayer));
                        if (checkWin(row, col)) {
                            statusLabel.setText(getCurrentPlayerName() + " wins!");
                            disableBoard();
                            askForRestart();
                        } else if (isBoardFull()) {
                            statusLabel.setText("It's a draw!");
                            askForRestart();
                        } else {
                            switchPlayer();
                            statusLabel.setText(getCurrentPlayerName() + "'s turn (" + currentPlayer + ")");
                            if (isVsComputer && currentPlayer == 'O') {
                                computerMove();
                            }
                        }
                    }
                });

                boardPanel.add(btn);
            }
        }
    }

    private void computerMove() {
        Timer timer = new Timer(500, e -> {
            int[] move = getComputerMove();
            buttons[move[0]][move[1]].setText("O");

            if (checkWin(move[0], move[1])) {
                statusLabel.setText(playerOName + " wins!");
                disableBoard();
                askForRestart();
            } else if (isBoardFull()) {
                statusLabel.setText("It's a draw!");
                askForRestart();
            } else {
                switchPlayer();
                statusLabel.setText(getCurrentPlayerName() + "'s turn (" + currentPlayer + ")");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private int[] getComputerMove() {
        // For now, all levels behave the same (random move).
        // You can upgrade logic here based on difficulty
        for (int i = 0; i < 100; i++) {
            int r = random.nextInt(3);
            int c = random.nextInt(3);
            if (buttons[r][c].getText().equals("")) {
                return new int[]{r, c};
            }
        }
        return new int[]{0, 0}; // fallback
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
    }

    private boolean checkWin(int row, int col) {
        // Horizontal
        if (buttons[row][0].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[row][1].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[row][2].getText().equals(String.valueOf(currentPlayer))) {
            return true;
        }
        // Vertical
        if (buttons[0][col].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[1][col].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[2][col].getText().equals(String.valueOf(currentPlayer))) {
            return true;
        }
        // Diagonals
        if (row == col &&
            buttons[0][0].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[1][1].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[2][2].getText().equals(String.valueOf(currentPlayer))) {
            return true;
        }
        if (row + col == 2 &&
            buttons[0][2].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[1][1].getText().equals(String.valueOf(currentPlayer)) &&
            buttons[2][0].getText().equals(String.valueOf(currentPlayer))) {
            return true;
        }
        return false;
    }

    private boolean isBoardFull() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                if (btn.getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void disableBoard() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                btn.setEnabled(false);
            }
        }
    }

    private void resetBoard() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                btn.setText("");
                btn.setEnabled(true);
            }
        }
        currentPlayer = 'X';
        statusLabel.setText(getCurrentPlayerName() + "'s turn (" + currentPlayer + ")");
    }

    private void askForRestart() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetBoard();
        } else {
            System.exit(0);
        }
    }

    private String getCurrentPlayerName() {
        return currentPlayer == 'X' ? playerXName : playerOName;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToeGUI());
    }
}
