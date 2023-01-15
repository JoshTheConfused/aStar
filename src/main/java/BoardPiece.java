import java.awt.Color;

public class BoardPiece {
    private int x;
    private int y;
    private Color color;
    private int gCost; //distance from starting point
    private int hCost; //distance from and point
    private int fCost; //gCost + hCost
    private boolean closed;
    private boolean wall; //can this space be crossed
    private boolean start; //is this the start node
    private boolean end; //is this the end node
    private BoardPiece parent; //refers to previous segment in path, essentially making final path a linked list

    public BoardPiece(int x, int y) { //No board class to hold a double array of these - a little unsafe in larger contexts but not a huge deal
        this.x = x;
        this.y = y;
        color = Color.WHITE;
        gCost = 0;
        hCost = 0;
        fCost = 0;
        closed = false;
        wall = false;
        start = false;
        end = false;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getGCost() {
        return gCost;
    }

    public void setGCost(int gCost) {
        this.gCost = gCost;
    }

    public int getHCost() {
        return hCost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public int getFCost() {
        return fCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public boolean isWall() {
        return wall;
    }

    public void setWall(boolean wall) {
        this.wall = wall;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public BoardPiece getParent() {
        return parent;
    }

    public void setParent(BoardPiece parent) {
        this.parent = parent;
    }

    public int distance(BoardPiece target){
        int xDiff = Math.abs(x - target.getX());
        int yDiff = Math.abs(y - target.getY());
        return (Math.abs(xDiff - yDiff) * 10) + (Math.min(xDiff, yDiff) * 14);
    }

    public String toString() {
        String out = "";
        out += "(" + x + ", " + y + "):\n";
        out += "Color: " + color + "\n";
        out += "G Cost: " + gCost + "\n";
        out += "H Cost: " + hCost + "\n";
        out += "F Cost: " + fCost + "\n";
        out += "Wall: " + wall + "\n";
        out += "Closed: " + closed + "\n";
        out += "Start: " + start + "\n";
        out += "End: " + end + "\n";
        return out;
    }
}