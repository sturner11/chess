package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final int BOARD_SIZE_IN_SQUARES = 10;
    private static final int SQUARE_SIZE_IN_CHARS = 1;
    private static final int LINE_WIDTH_IN_CHARS = 1;
    private static final String EMPTY = "   ";

    private static final ArrayList<String[]> chessBoard = new ArrayList<>();

    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        args = new String[]{"a,b,c,d,e,f,g,h;a,b,c,d,e,f,g,h; , , , , , , , ; , , , , , , , ; , , , , , , , ; , , , , , , , ;a,b,c,d,e,f,g,h;a,b,c,d,e,f,g,h;"};
        String[] rows = args[0].split(";");
        for (String row: rows){
            String[] rowList = row.split(",");
            chessBoard.add(rowList);
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

        String[]headers  = {" ", "a", "b", "c", "d", "e", "f", "g", "h", " "};

        for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol){
            drawHeader(out, headers[boardCol]);

            if (boardCol < BOARD_SIZE_IN_SQUARES -1){
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
        }
        setBlack(out);
        out.println();
        setGray(out);
    }

    private static void drawHeader(PrintStream out, String header) {
        int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;

        out.print(EMPTY.repeat(prefixLength));
        printText(out, header);
        out.print(EMPTY.repeat(suffixLength));
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
            setBlack(out);
            out.println();
            setGray(out);
        }
    }

    private static void drawSide(PrintStream out, int boardRow) {
        out.print(boardRow);
        out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
    }

    private static void drawRow(PrintStream out, int boardRow){
        for (int col = 0; col < chessBoard.size(); ++col){
                setBlack(out);
                int prefixLength = SQUARE_SIZE_IN_CHARS / 2;
                int suffixLength = SQUARE_SIZE_IN_CHARS - prefixLength - 1;
                out.print(EMPTY.repeat(prefixLength));
                printText(out, chessBoard.get(boardRow)[col]);
                out.print(EMPTY.repeat(suffixLength));
            if (col < chessBoard.size() -1){
                out.print(EMPTY.repeat(LINE_WIDTH_IN_CHARS));
            }
        }
        setGray(out);
    }

    private static void setGray(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_BLACK);
    }
    private static void setBlack(PrintStream out) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }
}
