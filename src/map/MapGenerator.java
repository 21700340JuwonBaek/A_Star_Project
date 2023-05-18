package map;

import algorithm.Algorithm;
import object.Character;
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
import java.util.List;
import java.util.Random;

public class MapGenerator {

    //FRAME
    JFrame frame;
    //GENERAL VARIABLES
    public static int monsterNum = 1;
    public static int cells = 20;
    public static int delay = 300;
    public static double dense = .5;
    public static double density = (cells*cells)*.5;
    public static int tool = 0;
    private int curAlg = 0;
    public static int WIDTH = 850;
    public static final int HEIGHT = 650;
    public static final int MSIZE = 600;
    public static int CSIZE = MSIZE/cells;
    //UTIL ARRAYS
    private String[] tools = {"Monster1","Me","Wall", "Eraser"};
    //BOOLEANS
    private boolean solving = false;
    //UTIL
    public static Node[][] map = new Node[cells][cells];

    public static Monster[] monsters = new Monster[3];

    public static Character character = new Character();

    Random r = new Random();
    //SLIDERS
    JSlider size = new JSlider(1,5,2);
    JSlider speed = new JSlider(300,600,delay);
    JSlider obstacles = new JSlider(1,100,50);
    JSlider monsterSlide  = new JSlider(1,3,1);
    //LABELS
    JLabel toolL = new JLabel("Toolbox");
    JLabel sizeL = new JLabel("Size:");
    JLabel cellsL = new JLabel(cells+"x"+cells);
    JLabel delayL = new JLabel("Delay:");
    JLabel msL = new JLabel(delay+"ms");
    JLabel obstacleL = new JLabel("Dens:");
    JLabel densityL = new JLabel(obstacles.getValue()+"%");
    JLabel monsterNumTextL = new JLabel("Mons:");
    JLabel monsterNumL = new JLabel(monsterSlide.getValue()+"마리");
    //BUTTONS
    JButton searchB = new JButton("Start Search");
    JButton resetB = new JButton("Reset");
    JButton genMapB = new JButton("Generate Map");
    JButton clearMapB = new JButton("Clear Map");
    //DROP DOWN
    JComboBox toolBx = new JComboBox(tools);
    //PANELS
    JPanel toolP = new JPanel();
    //CANVAS
    Map canvas;
    //BORDER
    Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);

    public MapGenerator() {   //CONSTRUCTOR
        for(int i = 0; i < monsters.length; i++){
            monsters[i] = new Monster();
        }
        clearMap();
        initialize();
    }

    public void generateMap() {   //GENERATE MAP
        clearMap();   //CREATE CLEAR MAP TO START
        for(int i = 0; i < density; i++) {
            Node current;
            do {
                int x = r.nextInt(cells);
                int y = r.nextInt(cells);
                current = map[x][y];   //FIND A RANDOM NODE IN THE GRID
            } while(current.getType()==2);   //IF IT IS ALREADY A WALL, FIND A NEW ONE
            current.setType(2);   //SET NODE TO BE A WALL
        }
    }

    public void clearMap() {   //CLEAR MAP
        /*TODO*/
        character.setX(-1);    //RESET THE START AND FINISH
        character.setY(-1);
        monsters[0].setX(-1);
        monsters[0].setY(-1);
        map = new Node[cells][cells];   //CREATE NEW MAP OF NODES
        for(int x = 0; x < cells; x++) {
            for(int y = 0; y < cells; y++) {
                map[x][y] = new Node(3,x,y);   //SET ALL NODES TO EMPTY
            }
        }
        reset();   //RESET SOME VARIABLES
    }

    public void resetMap() {   //RESET MAP
        int index = 0;
        for(int x = 0; x < cells; x++) {
            for(int y = 0; y < cells; y++) {
                Node current = map[x][y];
                if(current.getType()!=2) {    //CHECK TO SEE IF CURRENT NODE IS EITHER CHECKED OR FINAL PATH
                    map[x][y] = new Node(3, x, y);    //RESET IT TO AN EMPTY NOD
                }
            }
        }


        if(monsters[index].getX() > -1 && monsters[index].getY() > -1) {   //RESET THE START AND FINISH
            map[monsters[index].getX()][monsters[index].getY()] = new Node(0,monsters[index].getX(),monsters[index].getY());
            map[monsters[index].getX()][monsters[index].getY()].setHops(0);
        }
        if(character.getX() > -1 && character.getY() > -1)
            map[character.getX()][character.getY()] = new Node(1,character.getX(),character.getY());
        reset();   //RESET SOME VARIABLES
    }

    private void initialize() {   //INITIALIZE THE GUI ELEMENTS

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


        frame.getContentPane().add(toolP);

        canvas = new Map(this);
        canvas.setBounds(230, 10, MSIZE+1, MSIZE+1);
        frame.getContentPane().add(canvas);

        searchB.addActionListener(new ActionListener() {      //ACTION LISTENERS
            @Override
            public void actionPerformed(ActionEvent e) {
                reset();
                if((monsters[0].getX() > -1 && monsters[0].getX() > -1) && (character.getX() > -1 && character.getY() > -1))
                    solving = true;
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


        startSearch();   //START STATE
    }

    public void startSearch() {   //START STATE
        if(solving) {
            System.out.println("Solving Start");
            while (character.getX() != monsters[0].getX() || character.getY() != monsters[0].getY()) {
                long start = System.nanoTime();
                List<Node> pathNodeSet = Algorithm.AStar(monsters[0].getX(), monsters[0].getY());

                long end = System.nanoTime();

                long spentTime = (end-start)/ 1000000;
                System.out.println(pathNodeSet.isEmpty());
                for(Node node: pathNodeSet){
                    System.out.println(node.getX() + " " + node.getY());
                }
                if (pathNodeSet.isEmpty()) break;
                else {
                    if(delay - spentTime > 0) {
                        try {
                            Thread.sleep(500 - spentTime);
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
        pause();   //PAUSE STATE
    }

    public void pause() {   //PAUSE STATE
        int i = 0;
        while(!solving) {
            i++;
            if(i > 500)
                i = 0;
            try {
                Thread.sleep(1);
            } catch(Exception e) {}
        }
        startSearch();   //START STATE
    }

    public void reset() {   //RESET METHOD
        solving = false;
    }

    public void delay() {   //DELAY METHOD
        try {
            Thread.sleep(delay);
        } catch(Exception e) {}
    }
}