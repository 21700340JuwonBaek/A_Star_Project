package object;

public class Node {

    // 0 = 몬스터
    // 1 = 캐릭터
    // 2 = 벽
    // 3 = 빈공간
    // 4 = 확인 완료된 노드
    private int cellType = 0;
    private int hops;
    private int x;
    private int y;
    private int lastX;
    private int lastY;
    private double dToEnd = 0;

    public Node(int type, int x, int y) {
        cellType = type;
        this.x = x;
        this.y = y;
        hops = -1;
    }

    public double getEuclidDist(int finishx, int finishy) {
        //추정거리를 계산하는 함수.
        int xdif = Math.abs(x-finishx);
        int ydif = Math.abs(y-finishy);
        dToEnd = Math.sqrt((xdif*xdif)+(ydif*ydif));
        return dToEnd;
    }

    public int getX() {return x;}
    public int getY() {return y;}
    public int getLastX() {return lastX;}
    public int getLastY() {return lastY;}
    public int getType() {return cellType;}
    public int getHops() {return hops;}

    public void setType(int type) {cellType = type;}
    public void setLastNode(int x, int y) {lastX = x; lastY = y;}
    public void setHops(int hops) {this.hops = hops;}
}


