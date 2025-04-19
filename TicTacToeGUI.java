import java.awt.*;
import javax.swing.*;

public class TicTacToeGUI extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private char currentPlayer = 'X';
    private JLabel statusLabel;

    public TicTacToeGUI() {
        setTitle("Tic Tac Toe");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        statusLabel = new JLabel("Player X's turn");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(statusLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        initializeBoard(boardPanel);
        add(boardPanel, BorderLayout.CENTER);

        setVisible(true);
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
                    if (btn.getText().equals("")) {
                        btn.setText(String.valueOf(currentPlayer));
                        if (checkWin(row, col)) {
                            statusLabel.setText("Player " + currentPlayer + " wins!");
                            disableBoard();
                            askForRestart();
                        } else if (isBoardFull()) {
                            statusLabel.setText("It's a draw!");
                            askForRestart();
                        } else {
                            switchPlayer();
                            statusLabel.setText("Player " + currentPlayer + "'s turn");
                        }
                    }
                });

                boardPanel.add(btn);
            }
        }
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
        statusLabel.setText("Player X's turn");
    }

    private void askForRestart() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            resetBoard();
        } else {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToeGUI());
    }
}

