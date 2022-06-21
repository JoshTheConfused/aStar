import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

public class AStar {
    public static void findPath(BoardPiece[][] board) throws Exception {
        ArrayList<BoardPiece> open = new ArrayList<>();
        ArrayList<BoardPiece> closed = new ArrayList<>();
        BoardPiece current;
        BoardPiece start = null; //null assignment allows exception to be thrown below
        BoardPiece end = null; //start and end should be treated as final; they aren't declared as such because they are assigned in a loop

        for (int i = 0; i < 158; i++) {
            for (int j = 0; j < 83; j++) {
                if (board[i][j].isStart())
                    start = board[i][j];
                else if (board[i][j].isEnd())
                    end = board[i][j];
            }
        }
        if (start == null || end == null)
            throw new Exception();
        open.add(start);

        while (true) {
            fCostQuicksort(open);
            current = open.get(0);
            open.remove(0); //moves the current space to closed, as that is what it will be from now on
            closed.add(current);
            current.setClosed(true);

            if (current.isEnd()) //ends the loop if the end has been reached
                break;

            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int x = current.getX() + i;
                    int y = current.getY() + j;

                    if (x < 0 || x >= 158 || y < 0 || y >= 83)
                        continue; //skips the iteration if the space does not exist within the bounds of the board

                    if (board[x][y].isClosed() || board[x][y].isWall())
                        continue; //skips the iteration if the space has already or should not be evaluated (will also skip the current space)

                    int newGCost;
                    if (i == 0 || j == 0) //If the space is in line with the current space, it will be an extra 10 units of distance away
                        newGCost = board[x][y].getGCost() + 10;
                    else //otherwise, it's 14 units of distance, since it's a diagonal (10√2)
                        newGCost = board[x][y].getGCost() + 14;

                    int xDiff = Math.abs(end.getX() - x);
                    int yDiff = Math.abs(end.getY() - y); //finds the position of the space relative to the end space
                    int newHCost = (Math.abs(xDiff - yDiff) * 10) + (Math.min(xDiff, yDiff) * 14); //distance to end space (not perfect)

                    int newFCost = newGCost + newHCost;

                    if (newGCost < board[x][y].getGCost() || !open.contains(board[x][y])) {
                        board[x][y].setGCost(newGCost);
                        board[x][y].setHCost(newHCost);
                        board[x][y].setFCost(newFCost); //new costs are assigned, assuming the space needs to be updated

                        board[x][y].setParent(current); //creates a pseudo-linked list starting from the end space once the algorithm is complete

                        if (!open.contains(board[x][y]))
                            open.add(board[x][y]);
                    }
                }
            }
        }

        for (BoardPiece space : open)
            space.setColor(Color.GREEN);

        for (BoardPiece space : closed)
            space.setColor(Color.RED);

        BoardPiece currentSpace = end;
        while (!currentSpace.isStart()) {
            currentSpace.setColor(Color.BLUE);
            currentSpace = currentSpace.getParent();
        }
        start.setColor(Color.BLUE); //Assigns all colors, making a red blob with green edges and a blue path through it
    }

    private static void fCostQuicksort(ArrayList<BoardPiece> list) { //makes quicksort more concise to interact with
        fCostQuicksort(list, 0, list.size() - 1);
    }

    private static void fCostQuicksort(ArrayList<BoardPiece> list, int lowIndex, int highIndex) { //quicksort (youtube.com/watch?v=h8eyY7dIiN4)
        if (lowIndex >= highIndex) //prevents stack overflow once the list being sorted is one element in length
            return;

        int pivotIndex = new Random().nextInt(highIndex - lowIndex) + lowIndex; //assigns a random pivot and moves it to the high index for simplicity
        int pivot = list.get(pivotIndex).getFCost();
        swap(list, pivotIndex, highIndex);

        int leftPointer = lowIndex;
        int rightPointer = highIndex;

        while (leftPointer < rightPointer) { //moves two pointers towards each other, sorting along the way, until they reach each other and swap with the pivot
            while (list.get(leftPointer).getFCost() <= pivot && leftPointer < rightPointer) {
                leftPointer++;
            }
            while (list.get(rightPointer).getFCost() >= pivot && leftPointer < rightPointer) {
                rightPointer--;
            }

            swap(list, leftPointer, rightPointer);
        }
        swap(list, leftPointer, highIndex); //once the pointers meet, they swap with the pivot value. The list is now partitioned around the pivot

        fCostQuicksort(list, lowIndex, leftPointer - 1); //recursively sorts left of pointer
        fCostQuicksort(list, leftPointer + 1, highIndex); //recursively sorts right of pointer
    }

    private static void swap(ArrayList<BoardPiece> list, int index1, int index2) { //swaps two elements in an array list
        BoardPiece temp = list.get(index1);
        list.set(index1, list.get(index2));
        list.set(index2, temp);
    }
}
/*
        -Find start location and add it to open
        -Find end location and save as const

        -Loop {
            -sort OPEN by f-cost (write quicksort)
            -CURRENT = OPEN[0]
            -move CURRENT to CLOSED

            -if (CURRENT.isEND) {
                -path is found
            -}

            -Loop over neighboring spaces (remember to catch ArrayIndexOutOfBounds) {
                -if (NEIGHBOR.isClosed || NEIGHBOR.isWall) {
                    -Skip
                -}

                -Calculate path distance to neighbor (add 10 or 14 to gCost if straight or diagonal)
                -Calculate Δ
                -Calculate hCost (|Δy-Δx|*10 + 14*smaller(Δx, Δy)) (Δ being between CURRENT and end)
                -fCost = gCost + hCost

                -if (new path < NEIGHBOR.getGCost || !OPEN.contains(NEIGHBOR)){
                    -Assign NEIGHBOR newly calculated costs
                    -NEIGHBOR.setParent(CURRENT)
                    -if (!OPEN.contains(NEIGHBOR)
                        -OPEN.add(NEIGHBOR)
                }
            }
        }

        Foreach over CLOSED, OPEN. Reverse through pseudo-linked list starting from end {
            Closed = red, Open = dark green or green, Path = blue
        }*/