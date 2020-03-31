import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import javax.swing.Timer;

class Grid extends JPanel implements MouseListener, MouseMotionListener, KeyListener, ActionListener {
    private int width, height, rows, cols, size, boxNumber;
    private int[] start, end;
    private char currentKey;
    private PathFinder pathFinder;
    private Timer timer;
    private Stack<int[]> path;
    private List<int[]> pathList;
    private List<int[]> visitedNodeList;
    private boolean exploredNodesPrinted, isAnimationRunning, isRunning;
    private int count = 0;
    private int speed;
    private Random random;
    private int red, green, blue;
    private Color brightBlueLight, brightGreenLight, brightRedLight, brightYellowLight;
    private MenuHandler handler;

    public Grid(int w, int h, int r, int c) {
        setSize(width = w, height = h);
        rows = r;
        cols = c;
        size = 10;
        boxNumber = 500 / size;
        pathFinder = new PathFinder(this, boxNumber);
        start = new int[]{-1, -1};
        end = new int[]{-1, -1};
        currentKey = 'a';
        path = new Stack<>();
        pathList = new ArrayList<>();
        visitedNodeList = new ArrayList<>();
        exploredNodesPrinted = false;
        isAnimationRunning = false;
        isRunning = false;
        random = new Random();
        brightBlueLight = new Color(30, 50, 255);
        brightGreenLight = new Color(153, 255, 51);
        brightRedLight = new Color(255, 20, 20);
        brightYellowLight = new Color(255, 255, 0);
        this.setFocusable(true);
        this.requestFocus();
        addMouseListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        speed = 10;
        timer = new Timer(speed, this);
        handler = new MenuHandler(this);
    }

    public void paintComponent(Graphics g) {
        width = 500;
        height = 500;
        g.setColor(Color.WHITE);
        handler.position();
        handler.addToFrame();
        int min = 210;
        int max = 255;
        red = random.nextInt(max - min) + min;
        green = random.nextInt(max - min) + min;
        blue = random.nextInt(max - min) + min;
        Color flicker = new Color(red, green, blue);

        int currRow = 0, currCol = 0;
        int[][] grid = pathFinder.getGrid();
        for (int i = 0; i < width; i += size) {
            currCol = 0;
            for (int j = 0; j < height; j += size) {
                g.drawRect(i, j, size, size);
                if (grid[currRow][currCol] == 0) {
                    if (pathFinder.isComplete()) g.setColor(flicker);
                    else if (exploredNodesPrinted) g.setColor(Color.WHITE);
                    else g.setColor(Color.WHITE);
                }
                else if (grid[currRow][currCol] == 1) g.setColor(Color.BLACK);
                else if (grid[currRow][currCol] == 2) g.setColor(brightBlueLight);
                else g.setColor(brightYellowLight);
                g.fillRect(i, j, size, size);
                currCol++;
            }
            currRow++;
        }

        path = pathFinder.getPath();
        if (pathFinder.isComplete()) {
            timer.setDelay(speed);
            timer.start();
            isAnimationRunning = true;
            if (count < pathFinder.getExploredNodes().size()) {
                List<int[]> currNodeList = pathFinder.getExploredNodes().get(count++);
                g.setColor(brightBlueLight);
                for (int[] node : currNodeList) {
                    g.fillRect(node[0]*size, node[1]*size, size, size);
                }
                for (int i = 0; i < currNodeList.size(); i++) {
                    visitedNodeList.add(currNodeList.get(i));
                }
                g.setColor(brightGreenLight);
                for (int[] node :  visitedNodeList) {
                    g.fillRect(node[0]*size, node[1]*size, size, size);
                }
            } else {
                exploredNodesPrinted = true;
            }
//            for (int i = 0; i < pathList.size(); i++) {
//                curr = pathList.get(i);
//                g.fillRect(curr[0] * size, curr[1] * size, size, size);
//            }
//            System.out.println("Stopping");
//            pathFinder.resetPathFound();
//            timer.stop();
        }
        if (exploredNodesPrinted) {
            g.setColor(brightGreenLight);
            for (int[] node :  visitedNodeList) {
                g.fillRect(node[0]*size, node[1]*size, size, size);
            }
            if (pathFinder.isPathFound()) {
                g.setColor(brightRedLight);
                int[] curr = path.pop();
                pathList.add(curr);
                for (int[] node : pathList) {
                    g.fillRect(node[0] * size, node[1] * size, size, size);
                }
            }
        }

        if (path.size() == 1) {
            isAnimationRunning = false;
            isRunning = false;
            timer.stop();
        }

        if (pathFinder.getEnd()[0] != -1) {
            g.setColor(brightYellowLight);
            g.fillRect(pathFinder.getEnd()[0]*size, pathFinder.getEnd()[1]*size, size, size);
        }

        g.setColor(Color.BLACK);
        for (int i = 0; i < width; i += size) {
            g.drawLine(i, 0, i, height);
        }
        for (int i = 0; i <= height; i += size) {
            g.drawLine(0, i, width, i);
        }
    }

    private void performMouseClick(MouseEvent mouseEvent) {
        int xRem = mouseEvent.getPoint().x % size;
        int yRem = mouseEvent.getPoint().y % size;
        int x = mouseEvent.getPoint().x - xRem;
        int y = mouseEvent.getPoint().y - yRem;
        if (x >= 0 && x < 500 && y >= 0 && y < 500) {
            if (SwingUtilities.isLeftMouseButton(mouseEvent)) {
                if (currentKey == 's') {
                    pathFinder.setStart(x / size, y / size);
                } else if (currentKey == 'e') {
                    pathFinder.setEnd(x / size, y / size);
                } else {
                    pathFinder.setObstacle(x / size, y / size);
                }
            } else if (SwingUtilities.isRightMouseButton(mouseEvent)) {
                pathFinder.removeCell(x / size, y / size);
            }
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        performMouseClick(mouseEvent);
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        performMouseClick(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        currentKey = keyEvent.getKeyChar();
        if (keyEvent.getKeyCode() == KeyEvent.VK_SPACE) {
            int res = pathFinder.bfs();
            System.out.println(res);
        }
        if (currentKey == 'r') {
            resetGridVariables();
            pathFinder.reset();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        currentKey = 'a';
    }


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getActionCommand() != null) {
            if (actionEvent.getActionCommand().equals("Run")) {
                if (!isAnimationRunning) {
                    int selectedAlgo = handler.getSelectedAlgorithm();
                    int res = 0;
                    isRunning = true;
                    switch (selectedAlgo) {
                        case 0:
                            res = pathFinder.bfs();
                            break;
                        case 1:
                            res = pathFinder.dfs();
                            break;
                        case 2:
                            res = pathFinder.dijkstra();
                            break;
                        case 3:
                            res = pathFinder.aStarSearch();
                            break;
                        default:
                            isRunning = false;
                            System.out.println("Please select algorthm");
                    }
                    System.out.println(pathFinder.getPath().size());
                }
            }
            if (actionEvent.getActionCommand().equals("Reset Grid")) {
                resetGridVariables();
                pathFinder.resetGrid();
            }
            if (actionEvent.getActionCommand().equals("Reset")) {
                resetGridVariables();
                pathFinder.reset();
            }
            if (actionEvent.getActionCommand().equals("-")) {
                if (size == 100) {
                    System.out.println("Min Size");
                } else {
                    for (int i = size+1; i <100 ; i++) {
                        if (500 % i == 0) {
                            size = i;
                            break;
                        }
                    }
                    if (!isRunning) {
                        boxNumber = 500 / size;
                        pathFinder = new PathFinder(this, boxNumber);
                        repaint();
                    }
                }
            }
            if (actionEvent.getActionCommand().equals("+")) {
                if (size == 4) {
                    System.out.println("Max Boxes");
                } else {
                    for (int i = size-1; i > 0; i--) {
                        if (500 % i == 0) {
                            size = i;
                            break;
                        }
                    }
                    if (!isRunning) {
                        boxNumber = 500 / size;
                        pathFinder = new PathFinder(this, boxNumber);
                        repaint();
                    }
                }
            }
        } else {
            repaint();
        }
    }

    public void setAnimationSpeed() {
        double change = ((50 - handler.getSpeed().getValue()) / 50.0) * 10;
        speed = 10 + (int)change;
        if (isAnimationRunning) repaint();
        System.out.println(speed);
    }

    private void resetGridVariables() {
        count = 0;
        path = new Stack<>();
        pathList = new ArrayList<>();
        visitedNodeList = new ArrayList<>();
        exploredNodesPrinted = false;
        isAnimationRunning = false;
        isRunning = false;
    }
}

class Main extends JFrame{

    public Main() {
        Grid grid = new Grid(500, 700, 20, 20);
        this.setTitle("Path Finding Algo Visualizer");
        this.setSize(new Dimension(500, 700));
        this.setMinimumSize(new Dimension(500, 700));
        this.setResizable(false);
        add(grid);
        pack();
    }

    public static void main(String[] args) {
        new Main().setVisible(true);
    }
}