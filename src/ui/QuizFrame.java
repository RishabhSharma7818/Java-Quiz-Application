package ui;

import javax.swing.*;
import java.awt.*;

public class QuizFrame extends JFrame {

    private JLabel questionLabel;

    private JRadioButton option1;
    private JRadioButton option2;
    private JRadioButton option3;
    private JRadioButton option4;

    private JButton nextButton;

    public QuizFrame() {

        initializeFrame();
        initializeComponents();

        setVisible(true);
    }

    private void initializeFrame() {

        setTitle("Quiz Application");
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initializeComponents() {

        setLayout(new BorderLayout());

        questionLabel = new JLabel("What is JVM?");
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel();

        optionsPanel.setLayout(new GridLayout(4, 1));

        option1 = new JRadioButton("Java Virtual Machine");
        option2 = new JRadioButton("Java Variable Method");
        option3 = new JRadioButton("Java Very Machine");
        option4 = new JRadioButton("None");

        ButtonGroup group = new ButtonGroup();

        group.add(option1);
        group.add(option2);
        group.add(option3);
        group.add(option4);

        optionsPanel.add(option1);
        optionsPanel.add(option2);
        optionsPanel.add(option3);
        optionsPanel.add(option4);

        add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");

        add(nextButton, BorderLayout.SOUTH);
    }
}