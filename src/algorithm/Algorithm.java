package algorithm;

import object.Node;

import java.util.ArrayList;
import java.util.List;

import static map.MapGenerator.cells;
import static map.MapGenerator.character;
import static map.MapGenerator.map;


public class Algorithm {
    public static List<Node> AStar( int startx, int starty) {
        ArrayList<Node> priority = new ArrayList<Node>();
        priority.add(map[startx][starty]);
        List<Node> pathNodeSet = new ArrayList<>();
        while(true) {
            if(priority.isEmpty()) {
                break;
            }
            int hops = priority.get(0).getHops()+1;
            Node current = priority.get(0);

            //Priority가 가장 높은 Node 추출 후 탐색 수행.
            ArrayList<Node> explored = new ArrayList<Node>();

            //현재 노드의 인접 노드를 찾기 위해 반복문을 수행
            for(int a = -1; a <= 1; a++) {
                for(int b = -1; b <= 1; b++) {
                    int xbound = current.getX()+a;
                    int ybound = current.getY()+b;
                    if((xbound > -1 && xbound < cells) && (ybound > -1 && ybound < cells)) {   //경계 바깥으로 빠져나가지 않게.
                        Node neighbor = map[xbound][ybound]; //탐색 대상 노드
                        if((neighbor.getHops()==-1 || neighbor.getHops() > hops) && neighbor.getType()!=2) {   //한번도 수행되지 않은 공간+ 벽이 아이어야 한다.

                            if(neighbor.getType()!=0 && neighbor.getType() != 1)   //도착지점이나 시작지점이 아니라면 탐색했다고 체크
                                neighbor.setType(4);

                            neighbor.setLastNode(current.getX(), current.getY());   //KEEP TRACK OF THE NODE THAT THIS NODE IS EXPLORED FROM
                            neighbor.setHops(hops);   //SET THE HOPS FROM THE START

                            explored.add(neighbor);   //ADD THE NODE TO THE LIST
                        }
                    }
                }
            }
            //이웃의 탐색이 완료되었음.

            priority.remove(0); // 현재 노드의 탐색이 완료되었으므로 제거

            if(explored.size() > 0) { // 만약 탐색된 것이 있다면
                for(Node result: explored){ // 탐색 대상중, 끝점이 존재하는지 탐색.
                    if(result.getType() == 1){// 존재한다면 return을 하면 된다.
                        return backtrack(map, result.getLastX(), result.getLastY(), hops);
                    }
                }

                priority.addAll(explored); // 그게 아니라면 explored된 것을 큐에 추가함.

            }
            sortQue(priority);   //우선순위 큐이기 때문에 소팅을 해준다.
        }

        return pathNodeSet;
    }
    public static ArrayList<Node> sortQue(ArrayList<Node> sort) {
        //탐색된 대상 중에서 어떤 노드의 neighbor를 먼저 탐색할 것인가?
        int c = 0;

        while(c < sort.size()) {
            int sm = c;
            for(int i = c+1; i < sort.size(); i++) {
                //현재 노드까지의 hops(현재노드까지 오는데의 cost) 와, 도착지까지 가는데 필요한 예상 cost(Euclidean distance)
                if(sort.get(i).getEuclidDist(character.getX(), character.getY())+sort.get(i).getHops() <
                        sort.get(sm).getEuclidDist(character.getX(), character.getY())+sort.get(sm).getHops())
                    sm = i;
            }
            if(c != sm) {
                //Swap이 필요한 경우, swap.
                Node temp = sort.get(c);
                sort.set(c, sort.get(sm));
                sort.set(sm, temp);
            }
            c++;
        }
        return sort;
    }

    public static List<Node> backtrack(Node[][] map, int lx, int ly, int hops) {
        //백트레킹 작업. 도착지점으로부터 현재 지점까지의 경로를 탐색한다.
        List<Node> returnNode = new ArrayList<>();
        while(hops > 1) {  //hops는 시작지점부터 현재지점까지의 소모한 cost이므로 hops만큼 반복.
            Node current = map[lx][ly];
            current.setType(5);
            returnNode.add(current);
            lx = current.getLastX();
            ly = current.getLastY();
            hops--;
        }
        return returnNode;
    }}
