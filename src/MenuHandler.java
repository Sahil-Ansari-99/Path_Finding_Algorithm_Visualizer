import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MenuHandler {
    private Grid world;
    private JSlider speed;
    private JButton run, reset, resetGrid, increaseBoxes, decreaseBoxes;
    private JRadioButton bfsBtn, dfsBtn, dijkstraBtn, aStarBtn;
    private JLabel algorithmSelect, speedLabel, sizeLabel;
    private ButtonGroup algoBtnGroup;

    public MenuHandler(Grid world) {
        this.world = world;

        algorithmSelect = new JLabel("Select Algorithm");
        algorithmSelect.setVisible(true);

        speedLabel = new JLabel("Speed");
        speedLabel.setVisible(true);

        sizeLabel = new JLabel("Boxes");
        sizeLabel.setVisible(true);

        bfsBtn = new JRadioButton();
        bfsBtn.setText("BFS");

        dfsBtn = new JRadioButton();
        dfsBtn.setText("DFS");

        dijkstraBtn = new JRadioButton();
        dijkstraBtn.setText("Dijkstra");

        aStarBtn = new JRadioButton();
        aStarBtn.setText("A*-Search");

        algoBtnGroup = new ButtonGroup();
        algoBtnGroup.add(bfsBtn);
        algoBtnGroup.add(dfsBtn);
        algoBtnGroup.add(dijkstraBtn);
        algoBtnGroup.add(aStarBtn);

        run = new JButton("Run");
        run.addActionListener(world);

        reset = new JButton("Reset");
        reset.addActionListener(world);

        resetGrid = new JButton("Reset Grid");
        resetGrid.addActionListener(world);

        increaseBoxes = new JButton("+");
        increaseBoxes.addActionListener(world);

        decreaseBoxes = new JButton("-");
        decreaseBoxes.addActionListener(world);

        speed = new JSlider();
        speed.setVisible(true);
        speed.setName("speed");
//        speed.setOpaque(false);
//        speed.setFocusable(false);
        speed.setMajorTickSpacing(5);
        speed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                JSlider source = (JSlider)changeEvent.getSource();
                speed.setValue(source.getValue());
                world.setAnimationSpeed();
            }
        });
    }

    public void position() {
        algorithmSelect.setBounds(20, world.getHeight() - 150, algorithmSelect.getWidth(), algorithmSelect.getHeight());

//        int btnX = 10 + algorithmSelect.getWidth() + 10;
        int btnX = 20;
        bfsBtn.setBounds(btnX, algorithmSelect.getY() + 20, bfsBtn.getWidth(), bfsBtn.getHeight());
        dfsBtn.setBounds(btnX, bfsBtn.getY() + 20, dfsBtn.getWidth(), dfsBtn.getHeight());
        dijkstraBtn.setBounds(btnX, dfsBtn.getY() + 20, dijkstraBtn.getWidth(), dijkstraBtn.getHeight());
        aStarBtn.setBounds(btnX, dijkstraBtn.getY() + 20, aStarBtn.getWidth(), aStarBtn.getHeight());

//        int runBtnX = btnX + Math.max(bfsBtn.getWidth(), Math.max(dfsBtn.getWidth(), Math.max(dijkstraBtn.getWidth(), aStarBtn.getWidth()))) + 10;
        int runBtnX = 100;
        run.setBounds(runBtnX, world.getHeight() - 40, run.getWidth(), run.getHeight());

        int resetGridBtnX = runBtnX + run.getWidth() + 20;
        resetGrid.setBounds(resetGridBtnX, world.getHeight() - 40, resetGrid.getWidth(), resetGrid.getHeight());

        int resetBtnX = resetGridBtnX + resetGrid.getWidth() + 20;
        reset.setBounds(resetBtnX, world.getHeight() - 40, reset.getWidth(), reset.getHeight());

        int speedLabelX = resetGridBtnX + 20;
        speedLabel.setBounds(speedLabelX, world.getHeight() - 150, speedLabel.getWidth(), speedLabel.getHeight());
        speed.setBounds(speedLabelX+speedLabel.getWidth()+10, world.getHeight() - 150, speed.getWidth(), speed.getHeight());

        int sizeLabelX = resetGridBtnX + 20;
        sizeLabel.setBounds(sizeLabelX, world.getHeight() - 95, sizeLabel.getWidth(), sizeLabel.getHeight());

        increaseBoxes.setBounds(sizeLabelX + sizeLabel.getWidth() + 20, world.getHeight() - 100, increaseBoxes.getWidth(), increaseBoxes.getHeight());
        decreaseBoxes.setBounds(increaseBoxes.getX() + increaseBoxes.getWidth() + 20, world.getHeight() - 100, decreaseBoxes.getWidth(), decreaseBoxes.getHeight());
    }

    public void addToFrame() {
        world.add(algorithmSelect);
        world.add(bfsBtn);
        world.add(dfsBtn);
        world.add(dijkstraBtn);
        world.add(aStarBtn);
        world.add(run);
        world.add(resetGrid);
        world.add(reset);
        world.add(speedLabel);
        world.add(speed);
        world.add(sizeLabel);
        world.add(increaseBoxes);
        world.add(decreaseBoxes);
    }

    public JSlider getSpeed() {
        return speed;
    }

    public int getSelectedAlgorithm() {
        if (bfsBtn.isSelected()) return 0;
        else if (dfsBtn.isSelected()) return 1;
        else if (dijkstraBtn.isSelected()) return 2;
        else if (aStarBtn.isSelected()) return 3;
        else return -1;
    }
}
