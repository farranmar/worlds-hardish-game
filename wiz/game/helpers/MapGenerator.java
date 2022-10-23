package wiz.game.helpers;

import engine.support.Vec2i;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MapGenerator {

    private Vec2i interiorDims; // only interior, doesn't count the perimeter rows/columns
    private Vec2i totalDims;
    private int depth;
    private int minHeight;
    private int minWidth;

    public MapGenerator(Vec2i totalDims, int depth, int minHeight, int minWidth){
        this.interiorDims = new Vec2i(totalDims.x-2, totalDims.y-2);
        this.totalDims = totalDims;
        this.depth = depth;
        this.minHeight = minHeight;
        this.minWidth = minWidth;
    }

    private static class Box {
        Vec2i topEdge;
        Vec2i leftEdge;

        public Box(Vec2i topEdge, Vec2i leftEdge){
            this.topEdge = topEdge;
            this.leftEdge = leftEdge;
        }

        public Vec2i vertOverlap(Box box){
            if(this.leftEdge.x >= box.leftEdge.x && this.leftEdge.y <= box.leftEdge.y){
                return this.leftEdge;
            } else if(this.leftEdge.x <= box.leftEdge.x && this.leftEdge.y >= box.leftEdge.y){
                return box.leftEdge;
            } else if(box.leftEdge.x >= this.leftEdge.x && this.leftEdge.y >= box.leftEdge.x){
                return new Vec2i(box.leftEdge.x, this.leftEdge.y);
            } else if(this.leftEdge.x >= box.leftEdge.x && this.leftEdge.x < box.leftEdge.y){
                return new Vec2i(this.leftEdge.x, box.leftEdge.y);
            } else {
                return null;
            }
        }

        public Vec2i horOverlap(Box box){
            if(this.topEdge.x >= box.topEdge.x && this.topEdge.y <= box.topEdge.y){
                return this.topEdge;
            } else if(this.topEdge.x <= box.topEdge.x && this.topEdge.y >= box.topEdge.y){
                return box.topEdge;
            } else if(this.topEdge.x <= box.topEdge.x && this.topEdge.y >= box.topEdge.x){
                return new Vec2i(box.topEdge.x, this.topEdge.y);
            } else if(this.topEdge.x < box.topEdge.y && box.topEdge.y < this.topEdge.y){
                return new Vec2i(this.topEdge.x, box.topEdge.y);
            } else {
                return null;
            }
        }
    }

    private static class Node {
        Box value;
        Node left;
        Node right;
        Node parent;
        int level;
        boolean connected;

        public Node(Box value, Node parent){
            this.value = value;
            this.parent = parent;
            this.connected = false;
            if(parent == null){ this.level = 0; }
            else {
                this.level = parent.level + 1;
                parent.addChild(this);
            }
        }

        public void addChild(Node child){
            if(this.left == null){
                this.left = child;
            } else {
                this.right = child;
            }
        }

        public Node getSister(){
            if(this.parent == null){ return null; }
            if(this.parent.left.equals(this)){ return this.parent.right; }
            else { return this.parent.left; }
        }

        public ArrayList<Node> getLevel(int level){
            ArrayList<Node> nodes = new ArrayList<>();
            if(this.level == level){ nodes.add(this); }
            if(this.left != null){ nodes.addAll(this.left.getLevel(level)); }
            if(this.right != null){ nodes.addAll(this.right.getLevel(level)); }
            return nodes;
        }

        public int maxLevel(){
            int max = this.level;
            if(this.left != null){ max = Math.max(max, this.left.maxLevel()); }
            if(this.right != null){ max = Math.max(max, this.right.maxLevel()); }
            return max;
        }

        public ArrayList<Node> getLeaves(){
            ArrayList<Node> leaves = new ArrayList<>();
            if(this.left == null && this.right == null){ leaves.add(this); }
            if(this.left != null){ leaves.addAll(this.left.getLeaves()); }
            if(this.right != null){ leaves.addAll(this.right.getLeaves()); }
            return leaves;
        }
    }

    public TileType[][] generate(long seed){
        Box wholeMap = new Box(new Vec2i(0, this.interiorDims.x-1), new Vec2i(0, this.interiorDims.y-1));
        Node root = new Node(wholeMap, null);
        Random random = new Random(seed);
        for(int i = 0; i < this.depth; i++){
            for(Node oldBoxNode : root.getLevel(i)){
                Box oldBox = oldBoxNode.value;
                boolean vertical = (i % 2) == 0;
                int dim = vertical ? oldBox.topEdge.y - oldBox.topEdge.x + 1 : oldBox.leftEdge.y - oldBox.leftEdge.x + 1;
                if((vertical && Math.floor((dim-1)/2.0) < minWidth) || (!vertical && Math.floor((dim-1)/2.0) < minHeight)){
                    continue;
                }
                int divIndex = -1;
                while(divIndex < 0){
                    int nextDivision = random.nextInt(dim);
                    if(nextDivision > 2 && nextDivision < (dim - 2)){
                        divIndex = vertical ? oldBox.topEdge.x + nextDivision : oldBox.leftEdge.x + nextDivision;
                    }
                }
                Box newBoxOne, newBoxTwo;
                if(vertical){
                    newBoxOne = new Box(new Vec2i(oldBox.topEdge.x, divIndex - 1), oldBox.leftEdge);
                    newBoxTwo = new Box(new Vec2i(divIndex+1, oldBox.topEdge.y), oldBox.leftEdge);
                } else {
                    newBoxOne = new Box(oldBox.topEdge, new Vec2i(oldBox.leftEdge.x, divIndex-1));
                    newBoxTwo = new Box(oldBox.topEdge, new Vec2i(divIndex+1, oldBox.leftEdge.y));
                }
                Node newNodeOne = new Node(newBoxOne, oldBoxNode);
                Node newNodeTwo = new Node(newBoxTwo, oldBoxNode);
            }
        }
        return boxesToMap(root, random);
    }

    private TileType[][] boxesToMap(Node root, Random random){
        TileType[][] map = allImpassable(totalDims);
        ArrayList<Node> nodes = root.getLeaves();
        ArrayList<Box> boxes = new ArrayList<>();
        for(Node node : nodes){
            boxes.add(node.value);
        }
        for(Box box : boxes){
            int width = -1;
            int height = -1;
            while(width < 0){
                System.out.println("while loop 1");
                int newWidth = random.nextInt(box.topEdge.y - box.topEdge.x + 1);
                if(newWidth >= (box.topEdge.y - box.topEdge.x)/2.0){
                    width = newWidth;
                }
            }
            while(height < 0){
                System.out.println("while loop 2");
                int newHeight = random.nextInt(box.leftEdge.y - box.leftEdge.x + 1);
                if(newHeight >= (box.leftEdge.y - box.leftEdge.x)/2.0){
                    height = newHeight;
                }
            }
            int maxXGen = (box.topEdge.y - box.topEdge.x + 1) - width;
            int maxYGen = (box.leftEdge.y - box.leftEdge.x + 1) - height;
            int posX = box.topEdge.x + random.nextInt(maxXGen);
            int posY = box.leftEdge.x + random.nextInt(maxYGen);
            box.topEdge = new Vec2i(posX, posX + width);
            box.leftEdge = new Vec2i(posY, posY + height);

            for(int i = box.leftEdge.x; i <= box.leftEdge.y; i++){
                for(int j = box.topEdge.x; j <= box.topEdge.y; j++){
                    map[i+1][j+1] = TileType.PASSABLE;
                    this.print(map);
                }
            }
        }

        for(int i = root.maxLevel(); i > 0; i--){
            ArrayList<Node> nodeList = root.getLevel(i);
            for(Node node : nodeList){
                Box box1 = node.value;
                if(node.getSister() == null){ continue; }
                Box box2 = node.getSister().value;
                Vec2i vertOverlap = box1.vertOverlap(box2);
                print(map);
                if(vertOverlap != null && !node.connected && !node.getSister().connected){
                    vertOverlap = vertOverlap.plus(new Vec2i(1));
                    int max = vertOverlap.y - vertOverlap.x;
                    int index = -1;
                    while(index < 0){
                        System.out.println("while loop 3");
                        int test = vertOverlap.x + random.nextInt(max+1);
                        boolean inBoxOne = false;
                        this.print(map);
                        for(int j = box1.topEdge.x+1; j <= box1.topEdge.y+1; j++){
                            if(map[test][j] != TileType.IMPASSABLE){
                                inBoxOne = true;
                                break;
                            }
                        }
                        if(!inBoxOne) { continue; }
                        for(int j = box2.topEdge.x+1; j <= box2.topEdge.y+1; j++){
                            if(map[test][j] != TileType.IMPASSABLE){
                                index = test;
                                break;
                            }
                        }
                    }
                    boolean enteredFirstRoom = false;
                    boolean exitedFirstRoom = false;
                    int starter = -1;
                    for(int k = Math.min(box1.topEdge.y+1, box2.topEdge.y+1); k > -1; k--){
                        if(map[index][k] != TileType.IMPASSABLE){
                            starter = k;
                            break;
                        }
                    }
                    for(int k = starter; k < map[index+1].length; k++){
                        TileType type = map[index][k];
                        if(!enteredFirstRoom && type == TileType.IMPASSABLE){
                            continue;
                        } else if(!enteredFirstRoom) {
                            enteredFirstRoom = true;
                        } else if(enteredFirstRoom && type == TileType.IMPASSABLE){
                            exitedFirstRoom = true;
                            map[index][k] = TileType.PASSABLE;
                        } else if(exitedFirstRoom && type == TileType.PASSABLE){
                            node.connected = true;
                            node.getSister().connected = true;
                            break;
                        }
                    }
                }
                Vec2i horOverlap = box1.horOverlap(box2);
                if(horOverlap != null && !node.connected && !node.getSister().connected){
                    horOverlap = horOverlap.plus(new Vec2i(1));
                    int max = horOverlap.y - horOverlap.x;
                    int index = -1;
                    while(index < 0){
                        System.out.println("while loop 4");
                        int test = horOverlap.x + random.nextInt(max);
                        boolean inBoxOne = false;
                        for(int j = box1.leftEdge.x+1; j <= box1.leftEdge.y+1; j++){
                            if(map[j][test] != TileType.IMPASSABLE){
                                inBoxOne = true;
                                break;
                            }
                        }
                        if(!inBoxOne) { continue; }
                        for(int j = box2.leftEdge.x+1; j <= box2.leftEdge.y+1; j++){
                            if(map[j][test] != TileType.IMPASSABLE){
                                index = test;
                                break;
                            }
                        }
                    }
                    boolean enteredFirstRoom = false;
                    boolean exitedFirstRoom = false;
                    int starter = -1;
                    for(int k = Math.min(box1.leftEdge.y+1, box2.leftEdge.y+1); k > -1; k--){
                        if(map[k][index] != TileType.IMPASSABLE){
                            starter = k;
                            break;
                        }
                    }
                    for(int k = starter; k < map.length; k++){
                        TileType type = map[k][index];
                        if(!enteredFirstRoom && type == TileType.IMPASSABLE){
                            continue;
                        } else if(!enteredFirstRoom) {
                            enteredFirstRoom = true;
                        } else if(enteredFirstRoom && type == TileType.IMPASSABLE){
                            exitedFirstRoom = true;
                            map[k][index] = TileType.PASSABLE;
                        } else if(exitedFirstRoom && type == TileType.PASSABLE){
                            node.connected = true;
                            node.getSister().connected = true;
                            break;
                        }
                    }
                }
            }
        }
        return map;
    }

    private void print(TileType[][] tiles){
        for(TileType[] row : tiles){
            for(TileType type : row){
                if(type == TileType.IMPASSABLE){ System.out.print("X "); }
                else if(type == TileType.PASSABLE){ System.out.print("O "); }
                else { System.out.print("* "); }

            }
            System.out.println();
        }
        System.out.println();
        System.out.println("--------------------------------");
        System.out.println();
    }

    private TileType[][] allImpassable(Vec2i dims){
        TileType[][] ret = new TileType[dims.x][dims.y];
        for(int i = 0; i < dims.y; i++){
            for(int j = 0; j < dims.x; j++){
                ret[i][j] = TileType.IMPASSABLE;
            }
        }
        return ret;
    }

}
