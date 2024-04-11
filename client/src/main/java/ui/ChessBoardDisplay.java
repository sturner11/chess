package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import chess.ChessMove;

import static java.lang.System.out;
import static ui.EscapeSequences.*;

public class ChessBoardDisplay {

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private  final int LINE_WIDTH_IN_CHARS = 1;
    private static String[]headers;

    private static String[]rowVals;

    private static List<String[]> chessBoard = new ArrayList<>();
    private static String boardColor;

    private static String blackBG;

    private static String blackT;
    private static String whiteBG;
    private static String whiteT;

    public static void draw(String board, String color, Collection<ChessMove> validMoves) {
        boardColor = color;
        chessBoard = new ArrayList<>();
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
//        ChessBoard board = new ChessBoard();
//        board.resetBoard();
//        board = board.toString();
//        args = new String[]{"R,N,B,Q,K,B,N,R;P,P,P,P,P,P,P,P; , , , , , , , ; , , , , , , , ; , , , , , , , ; , , , , , , , ;p,p,p,p,p,p,p,p;r,n,b,q,k,b,n,r;"};
        String[] rows = board.split(";");
        for (String row : rows) {
            String[] rowList = row.split(",");
            if (Objects.equals(color, "BLACK")) {
                List<String> list = Arrays.asList(rowList);
                Collections.reverse(list);
                list.toArray(rowList);
                chessBoard.add(rowList);
            } else {

            chessBoard.add(rowList);
            }
        }
        headers  = new String[]{" ", "a", "b", "c", "d", "e", "f", "g", "h", " "};
        rowVals = new String[]{"8", "7","6","5", "4", "3", "2", "1"};

        if (Objects.equals(color, "BLACK")) {
            whiteBG = SET_BG_COLOR_WHITE;
            whiteT = SET_TEXT_COLOR_BLACK;
            blackBG = SET_BG_COLOR_BLACK;
            blackT = SET_TEXT_COLOR_WHITE;
                List<String> list = Arrays.asList(headers);
                Collections.reverse(list);
                headers = list.toArray(new String[list.size()]);
                list = Arrays.asList(rowVals);
                Collections.reverse(list);
                rowVals = list.toArray(new String[list.size()]);
                List<String[]> oldBoard = chessBoard;
                chessBoard = chessBoard.reversed();
            } else {
            whiteBG = SET_BG_COLOR_WHITE;
            whiteT = SET_TEXT_COLOR_BLACK;
            blackBG = SET_BG_COLOR_BLACK;
            blackT = SET_TEXT_COLOR_WHITE;
        }

            out.print(ERASE_SCREEN);
            drawHeaders(out);
            drawChessBoard(out);

            drawHeaders(out);

            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_WHITE);
        }

    private static void drawHeaders(PrintStream out){
        setGray(out);



        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol){
            drawHeader(out, headers[boardCol]);
        }
        setOuter();
        out.println();
        setGray(out);
    }

    private static void drawHeader(PrintStream out, String header) {
        int prefixLength = SQUARE_SIZE_IN_CHARS;
        int suffixLength = SQUARE_SIZE_IN_CHARS;

        out.print(PRE_SPACE);
        out.print(header);
        out.print(POST_SPACE);
    }


    private static void printText(PrintStream out, String text) {
        out.print(text);
    }

    private static void drawChessBoard(PrintStream out) {
        for (int boardRow = 0; boardRow < chessBoard.size(); ++boardRow){
            drawSide(out, boardRow);
            setBlack(out);
            drawRow(out, boardRow);

            if (boardRow < chessBoard.size()) {
                drawSide(out, boardRow);
                setGray(out);
            }
            setOuter();
            out.println();
            setGray(out);
        }
    }

    private static void setOuter() {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawSide(PrintStream out, int boardRow) {
        out.print(PRE_SPACE);
        out.print(rowVals[boardRow]);
        out.print(POST_SPACE);
    }

    private static void drawRow(PrintStream out, int boardRow){
        for (int col = 0; col < chessBoard.size(); ++col){
                if (boardRow % 2 == 1) {
                    if (col % 2 == 1) {
                        setWhite(out);
                    } else {
                        setBlack(out);
                    }
                } else {
                    if (col % 2 == 1) {
                        setBlack(out);
                    } else {
                        setWhite(out);
                    }
                }
            String chessPiece = getChessUnicode(chessBoard.get(boardRow)[col]);
            printText(out, chessPiece);
        }
        setGray(out);
    }

    private static String getChessUnicode(String piece) {


        switch (piece) {
            case "p":
            case "P":
                return piece.toLowerCase().equals(piece) ? BLACK_PAWN : WHITE_PAWN;
            case "r":
            case "R":
                return piece.toLowerCase().equals(piece) ? BLACK_ROOK : WHITE_ROOK;
            case "n":
            case "N":
                return piece.toLowerCase().equals(piece) ? BLACK_KNIGHT : WHITE_KNIGHT;
            case "b":
            case "B":
                return piece.toLowerCase().equals(piece) ? BLACK_BISHOP : WHITE_BISHOP;
            case "k":
            case "K":
                return piece.toLowerCase().equals(piece) ? BLACK_KING : WHITE_KING;
            case "q":
            case "Q":
                return piece.toLowerCase().equals(piece) ? BLACK_QUEEN : WHITE_QUEEN;
        }
        return EMPTY.repeat(1);
    }

    private static void setGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private static void setBlack(PrintStream out) {
        out.print(blackBG);
        out.print(blackT);
    }
    private static void setWhite(PrintStream out) {
        out.print(whiteBG);
        out.print(whiteT);
    }
}
