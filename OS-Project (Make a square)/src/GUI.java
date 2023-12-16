import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GUI extends JFrame {
    private JTextField lField, tField, zField, zdashField;
    private JPanel gridPanel;
    private Map<Integer, Color> colorMap = new HashMap<>();

    public GUI() {
        colorMap = new HashMap<>();
        colorMap.put(-1, Color.WHITE); // Empty cell
        colorMap.put(0, Color.RED); // Color for value 0
        colorMap.put(1, Color.GREEN); // Color for value 1
        colorMap.put(2, Color.BLUE); // Color for value 2
        colorMap.put(3, Color.YELLOW); // Color for value 3
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(50, 50)); // Added space between panels
        mainPanel.setBorder(new EmptyBorder(40, 10, 10, 40)); // Added space between borders

        mainPanel.add(createInputPanel(), BorderLayout.WEST);
        mainPanel.add(createGridPanel(), BorderLayout.CENTER);
        mainPanel.add(createSolveButton(), BorderLayout.SOUTH);

        setTitle("Make a square");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500); // Smaller window size
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
    }

    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 0, 5)); // Reduced space between components

        String[] labels = { "L", "T", "Z", "Zdash" };
        JTextField[] fields = { lField = new JTextField(), tField = new JTextField(),
                zField = new JTextField(), zdashField = new JTextField()};
        for (JTextField field : fields) {
            field.setText("0");
        }
        for (int i = 0; i < labels.length; i++) {
            JPanel labelFieldPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 3, 0)); // Align label to the right
            labelFieldPanel.add(new JLabel(labels[i]));
            fields[i].setPreferredSize(new Dimension(40, 30)); // Fixed input box size
            labelFieldPanel.add(fields[i]);
            inputPanel.add(labelFieldPanel);
        }

        return inputPanel;
    }

    private JPanel createGridPanel() {
        gridPanel = new JPanel(new GridLayout(4, 4, 0, 0)); // Added space between cells

        for (int i = 0; i < 16; i++) {
            JButton cell = new JButton();
            cell.setPreferredSize(new Dimension(20, 20)); // Fixed cell size
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            cell.setFocusPainted(false);
            gridPanel.add(cell);
        }

        return gridPanel;
    }

    private JButton createSolveButton() {
        JButton solveButton = new JButton("Solve");
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // here what should happen after clicking in the solve button.
                // Retrieve values from input fields

                // get value from text input , then convert this value
                // to integer instead of string.
                int lValue = Integer.parseInt(lField.getText());
                int tValue = Integer.parseInt(tField.getText());
                int zValue = Integer.parseInt(zField.getText());
                int zdashValue = Integer.parseInt(zdashField.getText());
                if(lValue+tValue+zValue+zdashValue!=4){
                    JOptionPane.showMessageDialog(null, "Choose exactly 4 shapes");
                    return;
                }

                // create array of the exiting values
                int[] arr = { 0, 0, 0, 0};
                int p=0;
                for(int i=0;i<lValue;i++) arr[p++]=0;
                for(int i=0;i<tValue;i++) arr[p++]=1;
                for(int i=0;i<zValue;i++) arr[p++]=2;
                for(int i=0;i<zdashValue;i++) arr[p++]=3;

                // send our array to multithreading
                try {
                    int n;
                    if (arr[0] <= 1)
                        n = 4;
                    else
                        n = 2;
                    outer.Multithreading[] thread = new outer.Multithreading[n];
                    Thread[] proc = new Thread[n];
                    for (int i = 0; i < n; i++) {
                        Shapes shapes = new Shapes();
                        shapes.pre();
                        thread[i] = new outer.Multithreading(0, i, shapes.grid, arr, shapes.arr, i);
                        proc[i] = new Thread(thread[i]);
                        proc[i].start();
                        updateGrid(shapes.grid);
                    }
                    for (int i = 0; i < n; i++) {
                        proc[i].join();
                    }
                    if (!outer.check){
                        JOptionPane.showMessageDialog(null, "No Result");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "Congratulations!");
                        outer.check=false;
                    }
                } catch (InterruptedException ex) {

                }
            }

        });
        solveButton.setPreferredSize(new Dimension(30, 30)); // Fixed Solve button size
        return solveButton;
    }

    public void updateGrid(int[][] grid) {
        SwingUtilities.invokeLater(() -> {
            Component[] components = gridPanel.getComponents();
            int index = 0;
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    JButton cell = (JButton) components[index++];
                    int value = grid[i][j];
                    cell.setBackground(colorMap.get(value));
                    cell.setText(String.valueOf(value));
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI GUI = new GUI();
            GUI.setVisible(true);
        });
    }

}
