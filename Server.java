import java.io.*;
import java.net.*;

public class Server {
    private static final int PORT = 8030;
    private static char[][] board = {
            {'1', '2', '3'},
            {'4', '5', '6'},
            {'7', '8', '9'}
    };
    private static char currentPlayer = 'X';

    public static void main(String[] args) {
        System.out.println("Сервер запущен. Ожидание двух игроков...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Socket player1 = serverSocket.accept();
            System.out.println("Игрок 1 подключён.");
            PrintWriter out1 = new PrintWriter(player1.getOutputStream(), true);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));

            Socket player2 = serverSocket.accept();
            System.out.println("Игрок 2 подключён.");
            PrintWriter out2 = new PrintWriter(player2.getOutputStream(), true);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));

            out1.println("Добро пожаловать! Вы играете за X.\n" +
                    "Чтобы сделать ход вам необходимо ввести номер соответствующей ячейки.");
            out2.println("Добро пожаловать! Вы играете за O.\n" +
                    "Чтобы сделать ход вам необходимо ввести номер соответствующей ячейки.");

            while (true) {
                sendBoard(out1);
                sendBoard(out2);

                if (currentPlayer == 'X') {
                    out1.println("Ваш ход (X): ");
                    String move = in1.readLine();
                    if (processMove(move, 'X')) {
                        if (checkWin('X')) {
                            sendBoard(out1);
                            sendBoard(out2);
                            out1.println("Вы победили!");
                            out2.println("Игрок X победил!");
                            break;
                        }
                        currentPlayer = 'O';
                    } else {
                        out1.println("Некорректный ход. Попробуйте снова.");
                    }
                } else {
                    out2.println("Ваш ход (O): ");
                    String move = in2.readLine();
                    if (processMove(move, 'O')) {
                        if (checkWin('O')) {
                            sendBoard(out1);
                            sendBoard(out2);
                            out2.println("Вы победили!");
                            out1.println("Игрок O победил!");
                            break;
                        }
                        currentPlayer = 'X';
                    } else {
                        out2.println("Некорректный ход. Попробуйте снова.");
                    }
                }

                if (isDraw()) {
                    sendBoard(out1);
                    sendBoard(out2);
                    out1.println("Ничья!");
                    out2.println("Ничья!");
                    break;
                }
            }

            player1.close();
            player2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendBoard(PrintWriter out) {
        out.println("\nТекущее игровое поле:");
        out.println("-------------");
        for (char[] row : board) {
            out.print("| ");
            for (char cell : row) {
                out.print(cell + " | ");
            }
            out.println();
            out.println("-------------");
        }
    }

    private static boolean processMove(String move, char player) {
        try {
            int position = Integer.parseInt(move) - 1;
            int row = position / 3;
            int col = position % 3;
            if (board[row][col] != 'X' && board[row][col] != 'O') {
                board[row][col] = player;
                return true;
            }
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return false;
    }

    private static boolean checkWin(char player) {
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == player && board[i][1] == player && board[i][2] == player) return true;
            if (board[0][i] == player && board[1][i] == player && board[2][i] == player) return true;
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private static boolean isDraw() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell != 'X' && cell != 'O') return false;
            }
        }
        return true;
    }
}
