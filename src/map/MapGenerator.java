package map;

import algorithm.Algorithm;
import object.Character;
import object.Item;
import object.Monster;
import object.Node;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {

    //JFRAME
    JFrame frame;

    //전역변수들.
    //파일을 분리했지만, 여러 파일에서 사용되기 때문에 static으로 변수들을 선정.

    public static boolean is_pink = false;
    public static int monsterNum = 1;
    public static int cells = 20;
    public static int delay = 300;
    public static double dense = .5;
    public static double density = (cells*cells)*.5;
    public static int tool = 0;
    public static int itemCount = 0;
    private int curAlg = 0;
    public static int WIDTH = 850;
    public static final int HEIGHT = 650;
    public static final int MSIZE = 600;
    public static int CSIZE = MSIZE/cells;
    //UTIL ARRAYS
    private String[] tools = {"Monster","Player","Wall", "Eraser"};

    //통제를 위한 변수. startSearch 변수를 통제하기 위한 변수.
    private boolean solving = false;
    //UTIL
    public static Node[][] map = new Node[cells][cells];

    //원래는 몬스터 3마리까지 최대 구성하려고 했었음.
    public static Monster[] monsters = new Monster[3];

    //현재 내 위치.
    public static Character character = new Character();

    Random r = new Random();

    //슬라이더.
    JSlider size = new JSlider(1,5,2);
    JSlider speed = new JSlider(300,600,delay);
    JSlider obstacles = new JSlider(1,100,50);
    JSlider monsterSlide  = new JSlider(1,3,1);

    //레이블(Frame에 나타나는 Text)
    JLabel toolL = new JLabel("Toolbox");
    JLabel sizeL = new JLabel("Size:");
    JLabel cellsL = new JLabel(cells+"x"+cells);
    JLabel delayL = new JLabel("Delay:");
    JLabel itemCountL = new JLabel("item : " + itemCount);
    JLabel msL = new JLabel(delay+"ms");
    JLabel obstacleL = new JLabel("Dens:");
    JLabel densityL = new JLabel(obstacles.getValue()+"%");
    JLabel monsterNumTextL = new JLabel("Mons:");
    JLabel monsterNumL = new JLabel(monsterSlide.getValue()+"마리");

    //체크박스
    JCheckBox check = new JCheckBox("확인용 코드");

    //버튼들.
    JButton searchB = new JButton("Game Start");
    JButton resetB = new JButton("Reset");
    JButton genMapB = new JButton("Generate Map");
    JButton clearMapB = new JButton("Clear Map");

    //Drop down.
    JComboBox toolBx = new JComboBox(tools);

    //패널.
    JPanel toolP = new JPanel();

    //이벤트를 핸들해주는 Map형 객체.
    Map canvas;
    //BORDER
    Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
    // 아이템 관련 변수
    private List<Item> items;
    private boolean[] itemCollected;

    public MapGenerator() {   //호출시, GUI가 나타난다.
        for(int i = 0; i < monsters.length; i++){
            monsters[i] = new Monster();
        }
        clearMap();
        initialize();
    }

    public void generateMap() {
        //랜덤으로 장애물이 생성된다.
        clearMap();
        for(int i = 0; i < density; i++) {
            Node current;
            do {
                int x = r.nextInt(cells);
                int y = r.nextInt(cells);
                current = map[x][y];
            } while(current.getType()==2);
            current.setType(2);
        }
        generateItems();
    }

    private void generateItems() {// 아이템 생성 및 위치 설정 메서드
        items = new ArrayList<>();
        itemCollected = new boolean[3];

        for (int i = 0; i < 3; i++) {
            int itemX;
            int itemY;
            do {
                itemX = r.nextInt(cells);
                itemY = r.nextInt(cells);
            } while ((itemX == monsters[0].getX() && itemY == monsters[0].getY())
                    || (itemX == character.getX() && itemY == character.getY())
                    || (map[itemX][itemY].getType()==2));

            items.add(new Item(itemX, itemY));
            itemCollected[i] = false;
        }

    }
    public void eatItem(int x, int y) {
        // 주어진 좌표에 아이템이 있는지 확인하고, 있으면 아이템을 제거
        Item eatenItem = null;
        for (Item item : items) {
            if (item.getX() == x && item.getY() == y) {
                eatenItem = item;
                break;
            }
        }
        if (eatenItem != null) {
            items.remove(eatenItem);
            itemCount++;

            if(itemCount == 3){
                itemCount = 0;
                // 게임 끝내기
                clearMap();
                // 알림 창 표시
                JOptionPane.showMessageDialog(frame, "★ Game Clear! ★", "Chase Game", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    public void meetMonster() {
        // 주변에 몬스터가 존재한다면 게임을 끝낸다.
        if(isWithinEightDirections(monsters[0].getX(), monsters[0].getY(), character.getX(), character.getY()) && monsters[0].getX()!=-1 && character.getX()!=-1){
            itemCount = 0;
            // 게임 끝내기
            clearMap();
            // 알림 창 표시
            JOptionPane.showMessageDialog(frame, "˃̣̣̥᷄⌓˂̣̣̥᷅  Game Over! ˃̣̣̥᷄⌓˂̣̣̥᷅ ★", "Chase Game", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private boolean isWithinEightDirections(int x1, int y1, int x2, int y2) {
        //몬스터가 사용자와 접촉했을까? 를 말해주는 코드.
        int dx = Math.abs(x1 - x2);
        int dy = Math.abs(y1 - y2);

        return (dx <= 1 && dy <= 1);
    }

    public void clearMap() {
        //맵상의 모든 공간을 clear시켜주는 함수.
        character.setX(-1);
        character.setY(-1);
        monsters[0].setX(-1);
        monsters[0].setY(-1);
        map = new Node[cells][cells];
        for(int x = 0; x < cells; x++) {
            for(int y = 0; y < cells; y++) {
                map[x][y] = new Node(3,x,y);
            }
        }
        reset();
    }

    public void resetMap() {
        //벽을 제외하고 맵을 비운다.
        int index = 0;
        for(int x = 0; x < cells; x++) {
            for(int y = 0; y < cells; y++) {
                Node current = map[x][y];
                if(current.getType()!=2) {
                    map[x][y] = new Node(3, x, y);
                }
            }
        }

        //몬스터와 캐릭터의 위치를 업데이트 시켜준다. 맵의 type을 업데이트 시킴으로써.
        if(monsters[index].getX() > -1 && monsters[index].getY() > -1) {
            map[monsters[index].getX()][monsters[index].getY()] = new Node(0,monsters[index].getX(),monsters[index].getY());
            map[monsters[index].getX()][monsters[index].getY()].setHops(0);
        }
        if(character.getX() > -1 && character.getY() > -1)
            map[character.getX()][character.getY()] = new Node(1,character.getX(),character.getY());
        reset();
    }

    private void initialize() {
        // Constructor 호출시 수행되는 코드. UI가 결정되고, component의 이벤트를 설정.

        for(int x = 0; x < cells; x++){
            for(int y = 0; y < cells; y++){
                map[x][y] = new Node(3, x, y);
            }
        }
        frame = new JFrame();
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(WIDTH,HEIGHT);
        frame.setTitle("제목");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        toolP.setBorder(BorderFactory.createTitledBorder(loweredetched,"Controls"));
        int space = 25;
        int buff = 45;

        toolP.setLayout(null);
        toolP.setBounds(10,10,210,600);

        searchB.setBounds(40,space, 120, 25);
        toolP.add(searchB);
        space+=buff;

        resetB.setBounds(40,space,120,25);
        toolP.add(resetB);
        space+=buff;

        genMapB.setBounds(40,space, 120, 25);
        toolP.add(genMapB);
        space+=buff;

        clearMapB.setBounds(40,space, 120, 25);
        toolP.add(clearMapB);
        space+=40;



        toolL.setBounds(40,space,120,25);
        toolP.add(toolL);
        space+=25;

        toolBx.setBounds(40,space,120,25);
        toolP.add(toolBx);
        space+=buff;

        check.setBounds(40,space,120,25);
        toolP.add(check);
        space+=buff;

        monsterNumTextL.setBounds(15,space,40,25);
        toolP.add(monsterNumTextL);
        monsterSlide.setMajorTickSpacing(10);
        monsterSlide.setBounds(50,space,100,25);
        toolP.add(monsterSlide);
        monsterNumL.setBounds(160,space,40,25);
        toolP.add(monsterNumL);
        space+=buff;

        sizeL.setBounds(15,space,40,25);
        toolP.add(sizeL);
        size.setMajorTickSpacing(10);
        size.setBounds(50,space,100,25);
        toolP.add(size);
        cellsL.setBounds(160,space,40,25);
        toolP.add(cellsL);
        space+=buff;



        delayL.setBounds(15,space,50,25);
        toolP.add(delayL);
        speed.setMajorTickSpacing(5);
        speed.setBounds(50,space,100,25);
        toolP.add(speed);
        msL.setBounds(160,space,40,25);
        toolP.add(msL);
        space+=buff;

        obstacleL.setBounds(15,space,100,25);
        toolP.add(obstacleL);
        obstacles.setMajorTickSpacing(5);
        obstacles.setBounds(50,space,100,25);
        toolP.add(obstacles);
        densityL.setBounds(160,space,100,25);
        toolP.add(densityL);
        space+=buff;

        itemCountL.setBounds(15,space,100,25);
        toolP.add(itemCountL);
        space+=buff;

        frame.getContentPane().add(toolP);

        // 아이템 호출
        generateItems();

        canvas = new Map(this);
        canvas.setBounds(230, 10, MSIZE+1, MSIZE+1);
        frame.getContentPane().add(canvas);

        searchB.addActionListener(new ActionListener() {      //ACTION LISTENERS
            @Override
            public void actionPerformed(ActionEvent e) {
                canvas.requestFocusInWindow(); // toolP 패널에 포커스 설정
                reset();
                if((monsters[0].getX() > -1 && monsters[0].getX() > -1) && (character.getX() > -1 && character.getY() > -1))
                    solving = true;


                // 아이템 호출
                generateItems();
            }
        });
        resetB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetMap();
            }
        });
        genMapB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateMap();
                canvas.Update();
            }
        });
        clearMapB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearMap();
                canvas.Update();
            }
        });

        toolBx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                tool = toolBx.getSelectedIndex();
            }
        });

        check.addItemListener(new ItemListener()
        {

            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED)
                    is_pink=true;
                else
                    is_pink = false;

                canvas.Update();

            }

        });

        monsterSlide.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                monsterNum = monsterSlide.getValue();
                canvas.Update();
            }
        });

        size.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                cells = size.getValue()*10;
                clearMap();
                reset();
                canvas.Update();
            }
        });
        speed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                delay = speed.getValue();
                canvas.Update();
            }
        });
        obstacles.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                dense = (double)obstacles.getValue()/100;
                canvas.Update();
            }
        });


        startSearch();   //스타트가 항상 수행된다.
    }

    public void startSearch() {
        // Solving이 true라고 하면, if 조건에 들어간다. 그게 아니라면 pause함수로.
        if(solving) {
            while (character.getX() != monsters[0].getX() || character.getY() != monsters[0].getY()) {
                long start = System.nanoTime();
                List<Node> pathNodeSet = Algorithm.AStar(monsters[0].getX(), monsters[0].getY());

                long end = System.nanoTime();

                long spentTime = (end-start)/ 1000000;
                if (pathNodeSet.isEmpty()) break;
                else {
                    //몬스터가 움직이는 시간을 일정하게 하기 위해서.
                    if(delay - spentTime > 0) {
                        try {
                            Thread.sleep(delay - spentTime);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    Node nextNode = pathNodeSet.get(pathNodeSet.size() - 1);
                    monsters[0].setX(nextNode.getX());
                    monsters[0].setY(nextNode.getY());
                    resetMap();
                    canvas.Update();
                }
            }
            solving=false;
        }
        pause();
    }

    public void pause() {
        //loop를 돌면서 solving이 true인지를 감지.
        //true이면 startSearch를 수행한다.
        int i = 0;
        while(!solving) {
            i++;
            if(i > 500)
                i = 0;
            try {
                Thread.sleep(1);
            } catch(Exception e) {}
        }
        startSearch();
    }

    public void reset() {   //RESET METHOD
        solving = false;
    }

    public boolean[] getItemCollected() {
        return itemCollected;
    }

    public List<Item> getItems() {
        return items;
    }
}