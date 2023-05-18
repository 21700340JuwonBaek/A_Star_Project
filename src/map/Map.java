package map;

import object.Item;
import object.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static map.MapGenerator.*;


class Map extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    MapGenerator mapGenerator;
    private Image frontImage, monsterImage,hurdleImage,backImage,itemImage;
    int monsterPressed = 0;
    public Map(MapGenerator mapGenerator) {
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        this.mapGenerator = mapGenerator;

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //배경 이미지
        if(is_pink == true) {
            ImageIcon back_icon = new ImageIcon("C:\\Users\\FastPc\\Desktop\\Spring Project\\A_Star_Project\\src\\img\\background_pink.png");
            backImage = back_icon.getImage();
            //배경 이미지 넣기
            g.drawImage(backImage,0,0, getWidth(),getHeight(), null);
        }

        //아이템 이미지
        ImageIcon item_icon = new ImageIcon("C:\\Users\\FastPc\\Desktop\\Spring Project\\A_Star_Project\\src\\img\\item.png");
        itemImage = item_icon.getImage();
        //사용자 이미지
        ImageIcon f_icon = new ImageIcon("C:\\Users\\FastPc\\Desktop\\Spring Project\\A_Star_Project\\src\\img\\f.png");
        frontImage = f_icon.getImage();
        //괴물 이미지
        ImageIcon mon_icon = new ImageIcon("C:\\Users\\FastPc\\Desktop\\Spring Project\\A_Star_Project\\src\\img\\monster.png");
        monsterImage = mon_icon.getImage();
        //장애물 이미지
        ImageIcon hurdle_icon = new ImageIcon("C:\\Users\\FastPc\\Desktop\\Spring Project\\A_Star_Project\\src\\img\\cookie.png");
        hurdleImage = hurdle_icon.getImage();

        //배경 이미지 넣기

        for(int x = 0; x < cells; x++) {
            for(int y = 0; y < cells; y++) {
                switch(map[x][y].getType()) {
                    case 0:
                        g.drawImage(monsterImage, x*CSIZE, y*CSIZE, CSIZE, CSIZE, null);
                        g.setColor(new Color(255, 0, 0, 1));
                        break;
                    case 1:
                        g.drawImage(frontImage, x*CSIZE, y*CSIZE, CSIZE, CSIZE, null);
                        g.setColor(new Color(255, 0, 0, 1));
                        break;
                    case 2:
                        g.drawImage(hurdleImage,x*CSIZE, y*CSIZE,CSIZE, CSIZE, null);
                        g.setColor(new Color(255,0,1));
                        break;
                    case 3:
                        g.setColor(Color.WHITE);
                        break;

                    case 4:
                        g.setColor(Color.CYAN);
                        break;
                    case 5:
                        g.setColor(Color.YELLOW);
                        break;

                }
                if(is_pink == false) {
                    g.fillRect(x* CSIZE,y* CSIZE, CSIZE, CSIZE);
                    //테두리 그리기.
                    g.setColor(Color.BLACK);
                    g.drawRect(x* CSIZE,y* CSIZE, CSIZE, CSIZE);
                }

                // 아이템 그리기
                for (int i = 0; i < mapGenerator.getItems().size(); i++) {
                    Item item = mapGenerator.getItems().get(i);
                    if (x == item.getX() && y == item.getY() && !mapGenerator.getItemCollected()[i]) {
                        g.drawImage(itemImage,x*CSIZE, y*CSIZE,CSIZE, CSIZE, null);
                    }
                }
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // 장애물을 그릴 수 있다.
        try {
            int x = e.getX()/ CSIZE;
            int y = e.getY()/CSIZE;
            Node current = map[x][y];
            if((tool == 2 || tool == 3) && (current.getType() != 0 && current.getType() != 1))
                current.setType(tool);
            Update();
        } catch(Exception z) {}
    }

    public void Update() {
        //GUI를 업데이트하기 위해서 필요한 코드.
        density = (cells*cells)*dense;
        CSIZE = MSIZE/cells;
        mapGenerator.canvas.repaint();
        mapGenerator.cellsL.setText(cells+"x"+cells);
        mapGenerator.msL.setText(delay+"ms");
        mapGenerator.densityL.setText(mapGenerator.obstacles.getValue()+"%");
        mapGenerator.monsterNumL.setText(mapGenerator.monsterSlide.getValue()+"마리");
        mapGenerator.itemCountL.setText("item : " + itemCount);
        mapGenerator.meetMonster();
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
            mapGenerator.resetMap();   //클릭을 하게 되면, 무조건 reset을 하게 되어있다. 설정이 바뀌게 되니까.
            int x = e.getX()/CSIZE;   //마우스 클릭 지점을 CELL size로 나눠줌으로써 x, y 좌표를 구한다.
            int y = e.getY()/CSIZE;
            Node current = map[x][y];
            switch(tool) {
                case 0: {   //캐릭터 위치
                    int index = 0;
                    if(current.getType()!=2) {   //만약 벽이 아니라면.
                        if(monsters[index].getX() > -1 && monsters[index].getY() > -1) {
                            //이미 몬스터의 출발지점이 설정되어있는 경우, 기존의 지점을 default로 업데이트한다.
                            map[monsters[index].getX()][monsters[index].getY()].setType(3);
                            map[monsters[index].getX()][monsters[index].getY()].setHops(-1);
                        }
                        current.setHops(0);
                        monsters[index].setX(x);
                        monsters[index].setY(y);
                        current.setType(0);
                    }

                    break;
                }
                case 1: {//몬스터 위치
                    if(current.getType()!=2) {
                        if(character.getX() > -1 && character.getY() > -1)
                            map[character.getX()][character.getY()].setType(3);
                        character.setX(x);
                        character.setY(y);
                        current.setType(1);
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
        } catch(Exception z) {}
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
            case KeyEvent.VK_Q:
                moveStart(-1, -1); // 왼쪽/위 대각선
                break;
            case KeyEvent.VK_E:
                moveStart(1, -1); // 오른쪽/위 대각선
                break;
            case KeyEvent.VK_Z:
                moveStart(-1, 1); // 왼쪽/아래 대각선
                break;
            case KeyEvent.VK_C:
                moveStart(1, 1); // 오른쪽/아래 대각선
                break;
            case KeyEvent.VK_P:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private boolean isValidMove(int x, int y) {
        // 이동 가능한 위치인지 확인 (벽이 아니고, 유효한 좌표인지)
        return x >= 0 && x < cells && y >= 0 && y < cells && map[x][y].getType() != 2;
    }

    public void moveStart(int dx, int dy) {
        // 시작지점의 새로운 위치 계산
        int newFinishX = character.getX() + dx;
        int newFinishY = character.getY() + dy;
        if(character.getX()!=-1 && character.getY()!=-1){
            // 새로운 위치가 유효한지 확인
            if (isValidMove(newFinishX, newFinishY)) {
                // 이동 가능한 경우, 기존 위치를 빈 공간으로 설정
                map[character.getX()][character.getY()].setType(3);

                // 새로운 위치에 도착지점(플레이어) 설정
                map[newFinishX][newFinishY].setType(1);

                // 도착지점 위치 업데이트
                character.setX(newFinishX);
                character.setY(newFinishY);


                mapGenerator.eatItem(newFinishX, newFinishY);

                // GUI 업데이트
                Update();
            }
        }
    }
}