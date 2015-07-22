/**
 * Created by bborchard on 7/21/2015.
 */
public class Grid {

    private Cell[][] cells;

    private int[][] puzzleStatus;

    private int filled;



    public Grid(int s, int[][] puzzle){
        cells = new Cell[s][s];
        for (int i=0;i<s;i++)
            for(int j=0;j<s;j++)
                cells[i][j] = new Cell(i,j,s);

        this.filled = 0;

        this.puzzleStatus = puzzle;
    }

    public int setCell(int row, int col, int val){

        cells[row][col].setVal(val);
        filled++;

        for(int i=0;i<cells.length;i++)
            cells[row][i].not(val, this);
        Cell[] yarray = new Cell[cells.length];
        for(int i=0;i<cells.length;i++) {
            cells[i][col].not(val, this);
            yarray[i] = cells[i][col];
        }

        if (checkCellArray(cells[row], puzzleStatus[3][row], puzzleStatus[1][row]) == -1)
            return -1;
        if (checkCellArray(yarray, puzzleStatus[0][col], puzzleStatus[2][col]) == -1)
            return -1;

        if (filled == cells.length*cells.length){
            return solved() ? 1 : -1;
        }
        return 0;
    }

    public void checkRowCol(int row, int col, int val){
        // Col
        int cWithVal = -1;
        boolean onePossibility = true;
        for (int i=0;i<cells.length;i++){
            if (cells[i][col].possibilities[val-1] != 0){
                if (cWithVal != -1) {
                    onePossibility = false;
                    break;
                } else {
                    cWithVal = i;
                }
            }
        }
        if (onePossibility && cWithVal != -1) {
            setCell(cWithVal, col, val);
        }


        // Row
        cWithVal = -1;
        onePossibility = true;
        for (int i=0;i<cells.length;i++){
            if (cells[row][i].possibilities[val-1] != 0){
                if (cWithVal != -1) {
                    onePossibility = false;
                    break;
                } else {
                    cWithVal = i;
                }
            }
        }
        if (onePossibility && cWithVal != -1) {
            setCell(row, cWithVal, val);
        }
    }

    public void init(){

        // Top
        for (int i=0;i<cells.length;i++){
            if(puzzleStatus[0][i] == 1)
                setCell(0, i, 6);
            for(int j=puzzleStatus[0][i]-2;j>=0;j--){
                for(int k=cells.length;k>=cells.length-(puzzleStatus[0][i]-2-j);k--) {
                    cells[j][i].not(k, this);
                }
            }
        }

        // Right
        for (int i=0;i<cells.length;i++){
            if (puzzleStatus[1][i] == 1)
                setCell(i, cells.length-1, 6);
            for(int j=cells.length-(puzzleStatus[1][i]-1);j<cells.length;j++){
                for(int k=cells.length;k>=cells.length-(j-(cells.length-(puzzleStatus[1][i]-1)));k--) {
                    cells[i][j].not(k, this);
                }
            }
        }


        // Bottom
        for (int i=0;i<cells.length;i++){
            if (puzzleStatus[2][i] == 1)
                setCell(cells.length-1, i, 6);
            for(int j=cells.length-(puzzleStatus[2][i]-1);j<cells.length;j++){
                for(int k=cells.length;k>=cells.length-(j-(cells.length-(puzzleStatus[2][i]-1)));k--) {
                    cells[j][i].not(k, this);
                }
            }
        }



        // Left
        for (int i=0;i<cells.length;i++){
            if (puzzleStatus[3][i] == 1)
                setCell(i, 0, 6);
            for(int j=puzzleStatus[3][i]-2;j>=0;j--){
                for(int k=cells.length;k>=cells.length-(puzzleStatus[3][i]-2-j);k--) {
                    cells[i][j].not(k, this);
                }
            }
        }
    }

    private int checkCellArray(Cell[] group, int ltSkyscrapers, int rbSkyscrapers){
        int[] dupArray = new int[group.length];
        int ssFromLeftOrTop = 0;
        int ssFromRightOrBottom = 0;
        boolean filled = true;

        for (int i=0;i<cells.length;i++){

            if (group[i].getVal() != 0) {
                dupArray[group[i].getVal() - 1]++;

                if (i != 0 && group[i].getVal() > group[i - 1].getVal()) {
                    ssFromLeftOrTop++;
                }

                int s = group.length;
                if (i != 0 && group[s-i-1].getVal() > group[s-i].getVal()) {
                    ssFromRightOrBottom++;
                }
            } else
                filled = false;
        }

        // Check for duplicate numbers
        for (int n : dupArray)
            if (n > 1)
                return 0;

        if ((ssFromLeftOrTop > ltSkyscrapers && ltSkyscrapers != 0) || (ssFromRightOrBottom > rbSkyscrapers && rbSkyscrapers != 0))
            return 0; // Bad
        else if (filled && (ssFromLeftOrTop == ltSkyscrapers || ltSkyscrapers == 0) && (ssFromRightOrBottom == rbSkyscrapers || rbSkyscrapers == 0))
            return 1; // Good
        else
            return -1; // Unsure
    }

    private boolean solved(){

        for (int i=0;i<cells.length;i++)
            if (checkCellArray(cells[i], puzzleStatus[3][i], puzzleStatus[1][i]) == -1)
                return false;

        for (int i=0;i<cells.length;i++) {
            Cell[] yarray = new Cell[cells.length];
            for (int j = 0; j < cells.length; j++)
                yarray[j] = cells[j][i];
            if (checkCellArray(yarray, puzzleStatus[0][i], puzzleStatus[2][i]) == -1)
                return false;
        }

        return true;
    }

    public String toString(){
        String str = "";
        for(Cell[] cs : cells) {
            for (Cell c : cs)
                str += c + " ";
            str += "\n";
        }
        return str;
    }

    public String pToString(){
        String str = "";
        for(Cell[] cs : cells) {
            for (Cell c : cs)
                str += c.pToString();
            str += "\n";
        }
        return str;
    }
}
