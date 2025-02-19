package ru.gr26x;

import ru.gr26x.db.HibernateUtil;
import ru.gr26x.math.StringFunction;
import ru.gr26x.painting.FunctionPainter;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainWindow extends JFrame {

    private final JPanel mainPanel;
    private final JPanel controlPanel;
    private final JLabel functionLabel;
    private final JTextField functionField;
    private final JButton plotButton;

    private final FunctionPainter functionPainter = new FunctionPainter(-3.0, 3.0, -3.0, 3.0);

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private StringFunction function = null;

    public MainWindow() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(450, 500));
        mainPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (function != null) {
                    functionPainter.setFunction(function);
                    functionPainter.paint(g);
                    functionPainter.paintFunction(g);
                }
            }
        };

        controlPanel = new JPanel();
        functionLabel = new JLabel();
        functionField = new JTextField();
        plotButton = new JButton();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                HibernateUtil.shutdown();
                super.windowClosed(e);
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                updatePainter();
                mainPanel.repaint();
            }
        });

        createUI();

        pack();
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(new Point((screenSize.width - getWidth()) / 2, (screenSize.height - getHeight()) / 2));
    }

    private void createUI() {
        mainPanel.setBackground(Color.WHITE);

        createControlPanel();

        var mainLayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(mainLayout);
        mainLayout.setHorizontalGroup(
                mainLayout.createSequentialGroup()
                        .addGap(8)
                        .addGroup(
                                mainLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(mainPanel, 300, MAX_SZ, MAX_SZ)
                                        .addComponent(controlPanel)
                        )
                        .addGap(8)
        );
        mainLayout.setVerticalGroup(
                mainLayout.createSequentialGroup()
                        .addGap(8)
                        .addGroup(
                                mainLayout.createSequentialGroup()
                                        .addComponent(mainPanel, 300, MAX_SZ, MAX_SZ)
                                        .addGap(8)
                                        .addComponent(controlPanel, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(8)
        );
    }

    private void createControlPanel() {
        controlPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), "Параметры графика:"));

        functionLabel.setText("Введите функцию для построения графика: ");

        functionField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                functionTextChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                functionTextChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                functionTextChanged();
            }
        });

        plotButton.setText("Построить график");
        plotButton.addActionListener(e -> {
            createPlot();
            mainPanel.repaint();
        });

        var gl = new GroupLayout(controlPanel);
        controlPanel.setLayout(gl);
        gl.setHorizontalGroup(
                gl.createSequentialGroup()
                        .addGap(8)
                        .addGroup(
                                gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(functionLabel)
                                        .addComponent(functionField)
                                        .addComponent(plotButton, GroupLayout.Alignment.CENTER)
                        )
                        .addGap(8)
        );
        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addGap(8)
                        .addGroup(
                                gl.createSequentialGroup()
                                        .addComponent(functionLabel)
                                        .addGap(8)
                                        .addComponent(functionField, MAX_SZ, MAX_SZ, MAX_SZ)
                                        .addGap(8)
                                        .addComponent(plotButton, MIN_SZ, MIN_SZ, MIN_SZ)
                        )
                        .addGap(8)
        );
    }

    private void functionTextChanged() {
        mainPanel.repaint();
    }

    private void updatePainter() {
        functionPainter.setWidth(mainPanel.getWidth());
        functionPainter.setHeight(mainPanel.getHeight());
    }

    private void createPlot() {
        try {
            this.function = new StringFunction(functionField.getText(), "x");
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Ошибка в функции: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static final int MIN_SZ = GroupLayout.PREFERRED_SIZE;
    private static final int MAX_SZ = GroupLayout.DEFAULT_SIZE;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            window.setVisible(true);
        });
    }
}