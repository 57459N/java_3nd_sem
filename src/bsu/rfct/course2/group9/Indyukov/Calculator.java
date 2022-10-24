package bsu.rfct.course2.group9.Indyukov;


import javax.imageio.ImageIO;
import javax.lang.model.type.NullType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static java.lang.Math.*;


public class Calculator {

    private int activeMemoryCell = 0;

    private int activeFormula = 1;

    private double memoryCells[] = new double[3];

    private JFrame mainFrame = new JFrame();

    private JLabel resultLabel = new JLabel();

    private JLabel formulaImageLabel = new JLabel();

    private JLabel memoryTextLabel = new JLabel("MEM:");

    private JLabel formulaTextLabel = new JLabel("Formula:");

    Calculator() {


        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth() * 480 / 1920;
        int height = gd.getDisplayMode().getHeight() * 400 / 1080;


        mainFrame.setSize(width, height);


        JPanel memPanel = new JPanel(); // the panel is not visible in output


        JRadioButton rbMem1 = new JRadioButton("1");
        JRadioButton rbMem2 = new JRadioButton("2");
        JRadioButton rbMem3 = new JRadioButton("3");

        rbMem1.setSelected(true);

        rbMem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calculator.this.activeMemoryCell = 0;
            }
        });

        rbMem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calculator.this.activeMemoryCell = 1;
            }
        });

        rbMem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calculator.this.activeMemoryCell = 2;
            }
        });

        ButtonGroup memButtonGroup = new ButtonGroup();
        memButtonGroup.add(rbMem1);
        memButtonGroup.add(rbMem2);
        memButtonGroup.add(rbMem3);

        JButton buttonMemoryPlus = new JButton("M+");
        buttonMemoryPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int activeCell = Calculator.this.activeMemoryCell;
                memoryCells[activeCell] += Double.parseDouble(resultLabel.getText());
                updateResult(activeCell);
            }
        });

        JButton buttonMemoryMinus = new JButton("M-");
        buttonMemoryMinus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int activeCell = Calculator.this.activeMemoryCell;
                memoryCells[activeCell] -= Double.parseDouble(resultLabel.getText());
                updateResult(activeCell);
            }
        });

        JButton buttonMemoryClear = new JButton("MC");
        buttonMemoryClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int activeCell = Calculator.this.activeMemoryCell;
                memoryCells[activeCell] = 0;
                updateResult(activeCell);
            }
        });


        JRadioButton rbFormula1 = new JRadioButton("1");
        JRadioButton rbFormula2 = new JRadioButton("2");

        rbFormula1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calculator.this.activeFormula = 1;
                drawFormula("https://chart.googleapis.com/chart?cht=tx&chl=(\\ln(1%2Bx)^2%2B\\cos\\pi%20z^3)^{\\sin{y}}%2B(\\e{x^{2}}%2B\\cos%20(\\e{z})%2B\\sqrt{1/y})^{1/x}");
            }
        });

        rbFormula2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Calculator.this.activeFormula = 2;
                drawFormula("https://chart.googleapis.com/chart?cht=tx&chl=y.\\frac{x^2}{\\lg{z^y%2B\\cos^2{\\sqrt[3]{x}}}}");
            }
        });

        rbFormula1.setSelected(true);

        ButtonGroup formulaButtonGroup = new ButtonGroup();
        formulaButtonGroup.add(rbFormula1);
        formulaButtonGroup.add(rbFormula2);

        JTextField textVariableX = new JTextField("0", 6);
        JTextField textVariableY = new JTextField("0", 6);
        JTextField textVariableZ = new JTextField("0", 6);

        JButton buttonCalculate = new JButton("Calculate");

        buttonCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Double result = null;
                double x = Double.parseDouble(textVariableX.getText());
                double y = Double.parseDouble(textVariableY.getText());
                double z = Double.parseDouble(textVariableZ.getText());
                switch (Calculator.this.activeFormula) {
                    case (1) -> result = calculateFirstFormula(x, y, z);
                    case (2) -> result = calculateSecondFormula(x, y, z);
                }

                Calculator.this.resultLabel.setText(result.toString());
            }
        });


        Box hboxFormulaChoice = Box.createHorizontalBox();
        hboxFormulaChoice.add(Box.createHorizontalGlue());
        hboxFormulaChoice.add(formulaTextLabel);
        hboxFormulaChoice.add(rbFormula1);
        hboxFormulaChoice.add(rbFormula2);
        hboxFormulaChoice.add(Box.createHorizontalGlue());

        Box hboxFormulaImage = Box.createHorizontalBox();
        hboxFormulaImage.add(Box.createHorizontalGlue());
        hboxFormulaImage.add(Box.createVerticalStrut(80));
        hboxFormulaImage.add(formulaImageLabel);
        hboxFormulaImage.add(Box.createVerticalStrut(80));
        hboxFormulaImage.add(Box.createHorizontalGlue());

        Box hboxMemory = Box.createHorizontalBox();
        hboxMemory.add(Box.createHorizontalGlue());
        hboxMemory.add(memoryTextLabel);
        hboxMemory.add(rbMem1);
        hboxMemory.add(rbMem2);
        hboxMemory.add(rbMem3);
        hboxMemory.add(buttonMemoryPlus);
        hboxMemory.add(buttonMemoryMinus);
        hboxMemory.add(buttonMemoryClear);
        hboxMemory.add(Box.createHorizontalGlue());

        Box hboxVariables = Box.createHorizontalBox();
        hboxVariables.add(Box.createHorizontalStrut(width/5));
        Box hboxFunc = Box.createHorizontalBox();

        hboxFunc.add(new JLabel("F( "));
        hboxFunc.add(textVariableX);
        hboxFunc.add(new JLabel(" ; "));
        hboxFunc.add(textVariableY);
        hboxFunc.add(new JLabel(" ; "));
        hboxFunc.add(textVariableZ);
        hboxFunc.add(new JLabel(") = "));
        hboxFunc.setMaximumSize(new Dimension(30,20));

        hboxVariables.add(hboxFunc);
        hboxVariables.add(Box.createHorizontalStrut(3));
        hboxVariables.add(resultLabel);
        hboxVariables.setMaximumSize(new Dimension(1000,30));
        hboxVariables.add(Box.createHorizontalGlue());


        Box hboxCalculate = Box.createHorizontalBox();
        hboxCalculate.add(Box.createHorizontalGlue());
        hboxCalculate.add(buttonCalculate);
        hboxCalculate.add(Box.createHorizontalGlue());


        Box contentBox = Box.createVerticalBox();
        contentBox.add(hboxFormulaChoice);
        contentBox.add(hboxFormulaImage);
        contentBox.add(hboxMemory);
        contentBox.add(Box.createVerticalStrut(10));
        contentBox.add(hboxVariables);
        contentBox.add(Box.createVerticalStrut(10));
        contentBox.add(hboxCalculate);
        contentBox.add(Box.createVerticalGlue());

        mainFrame.getContentPane().add(contentBox);
    }

    private Double calculateFirstFormula(Double x, Double y, Double z) {
        if (x == 0 || y == 0) {
            return null;
        }
        return pow(log(pow(1 + x, 2)) + cos(PI * pow(z, 3)), sin(y)) +
                pow(pow(E, x * x) + cos(pow(E, z)) + sqrt(1 / y), 1 / x);
    }

    private Double calculateSecondFormula(Double x, Double y, Double z) {
        if (y == 0 || x == -1) {
            return null;
        }

        double divider = (log10(pow(z, y)) + pow(cos(pow(x, 1.f / 3.f)), 2));
        if (divider == 0) {
            return null;
        }

        return y * x * x / divider;
    }

    private void updateResult(int memoryCell) {
        Calculator.this.resultLabel.setText(Double.toString(Calculator.this.memoryCells[memoryCell]));
    }

    private void drawFormula(String strUrl) {
        URL url = null;
        try {
            url = new URL(strUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        formulaImageLabel.setIcon(new ImageIcon(image));
    }

    public void setVisible(boolean state) {
        mainFrame.setVisible(state);
    }
}
