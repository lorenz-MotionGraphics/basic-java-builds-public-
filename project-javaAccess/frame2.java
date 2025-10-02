import javax.swing.*; 
import javax.swing.border.*; 
import javax.swing.text.*; 
import javax.swing.table.*;
import javax.swing.ImageIcon.*; 
import javax.swing.JOptionPane;
import java.awt.*; 
import java.awt.event.*;

class frame2 {
    private static JFrame f;
    private static JPanel mainPanel;

    public static void main(String[] args) {
        f = new JFrame("LMGResearch");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(500, 600);

        mainPanel = new JPanel();
        mainPanel.setLayout(null); //only one setLayout for one frame GUI
        mainPanel.setPreferredSize(new Dimension(480, 900));

        JOptionPane.showMessageDialog(null, "hello world");
        setDatadisplay();
        setTable();
        set3DShape(); // <-- Add 3D drawing here
        setRadioBtn();
        setCheckBox();
        setProgressBar();
        setListDisplay();
        setMenuDisplay();

        JScrollPane scrollPane = new JScrollPane(mainPanel); scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        f.add(scrollPane);
        f.setVisible(true);
    }

    private static void setDatadisplay() {
        JButton btn = new JButton("<html><font color='red'>pretest_!</font></html>");
        btn.setBounds(50, 50, 120, 50);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        btn.setContentAreaFilled(false);

        JLabel txt = new JLabel("<html><b><font color='blue'>helloworld</font></b></html>");
        txt.setBounds(50, 100, 150, 50);

        mainPanel.add(btn);
        mainPanel.add(txt);
    }

    private static void setTable() {
        String[][] data = {
            {"1", "esmael", "20"},
            {"2", "james carlo", "25"},
            {"3", "jamesaldrin", "30"}
        };

        String[][] dataTwo = {
            {"1", "harvey", "40"},
            {"2", "konami", "60"},
            {"3", "Lawrence", "20"}
        };

        String[] columns = {"ID", "Name", "Age"};
        String[] columnsTwo = {"identification", "name", "age"};

        JTable table = new JTable(data, columns);
        JTable tableTwo = new JTable(dataTwo, columnsTwo);

        // Table styling
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        table.setGridColor(Color.GRAY);
        table.setSelectionBackground(new Color(184, 207, 229));
        table.setSelectionForeground(Color.BLACK);
        table.setShowVerticalLines(false);
        table.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(200, 200, 200));
        header.setForeground(Color.BLACK);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

        tableTwo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableTwo.setRowHeight(25);
        tableTwo.setGridColor(Color.GRAY);
        tableTwo.setSelectionBackground(new Color(229, 204, 255));
        tableTwo.setSelectionForeground(Color.BLACK);
        tableTwo.setShowVerticalLines(false);

        JTableHeader header2 = tableTwo.getTableHeader();
        header2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header2.setBackground(new Color(200, 200, 255));
        header2.setForeground(Color.BLACK);
        ((DefaultTableCellRenderer) header2.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        DefaultTableCellRenderer centerRenderer2 = new DefaultTableCellRenderer();
        centerRenderer2.setHorizontalAlignment(JLabel.CENTER);
        tableTwo.getColumnModel().getColumn(0).setCellRenderer(centerRenderer2);
        tableTwo.getColumnModel().getColumn(2).setCellRenderer(centerRenderer2);

        JScrollPane scrollPane = new JScrollPane(table);
        JScrollPane scrollPane2 = new JScrollPane(tableTwo);

        scrollPane.setBounds(50, 150, 380, 100);
        scrollPane2.setBounds(50, 270, 380, 100);

        table.setFillsViewportHeight(true);
        tableTwo.setFillsViewportHeight(true);

        mainPanel.add(scrollPane);
        mainPanel.add(scrollPane2);
    }

    // New method for 3D drawing
    private static void set3DShape() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                // Enable anti-aliasing
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw built-in 3D rectangle
                g2.setColor(Color.BLUE);
                g2.fill3DRect(20, 20, 100, 50, true);

                // Simulate a sphere with gradient
                GradientPaint gradient = new GradientPaint(
                        150, 40, Color.WHITE,  // light side
                        200, 100, Color.RED   // dark side
                );
                g2.setPaint(gradient);
                g2.fillOval(150, 20, 80, 80);
            }
        };

        panel.setBounds(50, 400, 300, 150);
        panel.setBackground(Color.WHITE);
        mainPanel.add(panel);
    }

    public static void setCheckBox() {  

        JCheckBox checkBox = new JCheckBox("I agree to the terms");
        checkBox.setBounds(10, 10, 200, 30);  

        checkBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("Checked");
                } else {
                    System.out.println("Unchecked");
                }
            }
        });

        mainPanel.add(checkBox); 
    }  

    public static void setRadioBtn() {

        JRadioButton option1 = new JRadioButton("Option 1");
        option1.setBounds(50, 550, 100, 30);

        JRadioButton option2 = new JRadioButton("Option 2");
        option2.setBounds(50, 580, 100, 30);

        ButtonGroup group = new ButtonGroup();
        group.add(option1);
        group.add(option2);

        ActionListener listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Selected: " + e.getActionCommand());
            }
        };

        option1.addActionListener(listener);
        option2.addActionListener(listener);

        mainPanel.add(option1);
        mainPanel.add(option2);
    }

    public static void setProgressBar() {
        JProgressBar progressBar = new JProgressBar(0, 100); // min=0, max=100
        progressBar.setValue(0); // start at 0%
        progressBar.setStringPainted(true); // show percentage
        progressBar.setBounds(50, 650, 300, 30);

        mainPanel.add(progressBar);

        // Simulate loading
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                final int value = i;
                try { Thread.sleep(50); } catch (InterruptedException ignored) {}
                SwingUtilities.invokeLater(() -> progressBar.setValue(value));
            }
        }).start();
    }

    public static void setListDisplay() {
        String[] friends = { 
            "harvey", "konami", "jamesaldrin", "jeraldine", "esmael", "lander", 
            "jhomerl", "anthony", "edelyn", "micheal ong", "irwan pastano",
            "eonvher" 
        };

        JList<String> list = new JList<>(friends);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // only one at a time
        list.setVisibleRowCount(12);
        list.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Wrap in scroll pane
        JScrollPane scrollPaneList = new JScrollPane(list);
        scrollPaneList.setBounds(50, 700, 380, 100);

        // Add to frame
        mainPanel.add(scrollPaneList);

        // Handle selection
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                System.out.println("Selected: " + list.getSelectedValue());
            }
        });
    }

    private static void setMenuDisplay() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open");
        JMenuItem exitItem = new JMenuItem("Exit");

        // Add items to File menu
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.addSeparator(); // adds a line
        fileMenu.add(exitItem);

        // Create "Help" menu
        JMenu helpMenu = new JMenu("Help"); JMenuItem aboutItem = new JMenuItem("About");

        JMenu lorenzMenu = new JMenu("lorenz");

        helpMenu.add(aboutItem);

        // Add menus to menu bar
        menuBar.add(lorenzMenu);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        // Add menu bar to frame
        f.setJMenuBar(menuBar);

        // Action listeners

        exitItem.addActionListener(e -> System.exit(0));
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(mainPanel, "About this program..."));

    }
}
