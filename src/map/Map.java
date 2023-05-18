package map;

import object.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static map.MapGenerator.*;


class Map extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
MapGenerator mapGenerator;
int monsterPressed = 0;
    public Map(MapGenerator mapGenerator) {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        this.mapGenerator = mapGenerator;

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int x = 0; x < cells; x++) {
            for(int y = 0; y < cells; y++) {
                switch(map[x][y].getType()) {
                    case 0:
                        g.setColor(Color.GREEN);
                        break;
                    case 1:
                        g.setColor(Color.RED);
                        break;
                    case 2:
                        g.setColor(Color.BLACK);
                        break;
                    case 3:
                    case 4:
                        g.setColor(Color.WHITE);
                        break;

                }
                //채우기.
                g.fillRect(x* CSIZE,y* CSIZE, CSIZE, CSIZE);
                //테두리 그리기.
                g.setColor(Color.BLACK);
                g.drawRect(x* CSIZE,y* CSIZE, CSIZE, CSIZE);

            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
            try {
                int x = e.getX()/ CSIZE;
                int y = e.getY()/CSIZE;
                Node current = map[x][y];
                if((tool == 2 || tool == 3) && (current.getType() != 0 && current.getType() != 1))
                    current.setType(tool);
                Update();
            } catch(Exception z) {}
    }

    public void Update() {   //UPDATE ELEMENTS OF THE GUI
        density = (cells*cells)*dense;
        CSIZE = MSIZE/cells;
        mapGenerator.canvas.repaint();
        mapGenerator.cellsL.setText(cells+"x"+cells);
        mapGenerator.msL.setText(delay+"ms");
        mapGenerator.densityL.setText(mapGenerator.obstacles.getValue()+"%");
        mapGenerator.monsterNumL.setText(mapGenerator.monsterSlide.getValue()+"마리");
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
            try {
                mapGenerator.resetMap();   //RESET THE MAP WHENEVER CLICKED
                int x = e.getX()/CSIZE;   //GET THE X AND Y OF THE MOUSE CLICK IN RELATION TO THE SIZE OF THE GRID
                int y = e.getY()/CSIZE;
                Node current = map[x][y];
                switch(tool) {
                    case 0: {   //캐릭터 위치
//                        System.out.println(monsterPressed + " " +monsterNum +  " " + monsterPressed%monsterNum);
//                        for(int i = 0; i < mapGenerator.cells; i++){
//                            for(int j = 0; j < mapGenerator.cells; i++){
//                                System.out.printf("%2d ",map[i][j].getType());
//                            }
//                            System.out.println();
//                        }
                        int index = 0;
                        if(current.getType()!=2) {   //IF NOT WALL
                            if(monsters[index].getX() > -1 && monsters[index].getY() > -1) {   //IF START EXISTS SET IT TO EMPTY
                                map[monsters[index].getX()][monsters[index].getY()].setType(3);
                                map[monsters[index].getX()][monsters[index].getY()].setHops(-1);
                            }
                            current.setHops(0);
                            monsters[index].setX(x);   //SET THE START X AND Y
                            monsters[index].setY(y);
                            current.setType(0);   //SET THE NODE CLICKED TO BE START
                        }

                        break;
                    }
                    case 1: {//몬스터 위치
                        if(current.getType()!=2) {   //IF NOT WALL
                            if(character.getX() > -1 && character.getY() > -1)   //IF FINISH EXISTS SET IT TO EMPTY
                                map[character.getX()][character.getY()].setType(3);
                            character.setX(x);   //SET THE FINISH X AND Y
                            character.setY(y);
                            current.setType(1);   //SET THE NODE CLICKED TO BE FINISH
                        }
                        break;
                    }
                    default:
                        if(current.getType() != 0 && current.getType() != 1)
                            current.setType(tool);
                        break;
                }
                this.requestFocus();
                Update();
            } catch(Exception z) {}   //EXCEPTION HANDLER
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                moveStart(0, -1); // 위로 이동
                break;
            case KeyEvent.VK_S:
                moveStart(0, 1); // 아래로 이동
                break;
            case KeyEvent.VK_A:
                moveStart(-1, 0); // 왼쪽으로 이동
                break;
            case KeyEvent.VK_D:
                moveStart(1, 0); // 오른쪽으로 이동
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private boolean isValidMove(int x, int y) {
        // 이동 가능한 위치인지 확인 (벽이 아니고, 유효한 좌표인지)
//            return x >= 0 && x < cells && y >= 0 && y < cells && map[x][y].getType() != 2;
        return false;
    }

    public void moveStart(int dx, int dy) {
//            // 시작지점의 새로운 위치 계산
//            int newFinishX = finishx + dx;
//            int newFinishY = finishy + dy;
//            if(finishx!=-1 && finishy!=-1){
//                // 새로운 위치가 유효한지 확인
//                if (isValidMove(newFinishX, newFinishY)) {
//                    // 이동 가능한 경우, 기존 위치를 빈 공간으로 설정
//                    map[finishx][finishy].setType(3);
//
//                    // 새로운 위치에 도착지점(플레이어) 설정
//                    map[newFinishX][newFinishY].setType(1);
//
//                    // 도착지점 위치 업데이트
//                    finishx = newFinishX;
//                    finishy = newFinishY;
//
//                    // GUI 업데이트
//                    Update();
//                }
//            }
    }
}