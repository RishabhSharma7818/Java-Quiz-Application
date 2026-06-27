package ui;

import data.QuizData;
import model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class QuizFrame extends JFrame {

    private List<Question> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;

    private JLabel questionNumberLabel;
    private JLabel questionLabel;

    private JRadioButton option1;
    private JRadioButton option2;
    private JRadioButton option3;
    private JRadioButton option4;

    private JButton nextButton;

    private ButtonGroup buttonGroup;

    public QuizFrame() {

        initializeFrame();

        questions = QuizData.getQuestions();

        initializeComponents();

        loadQuestion();

        setVisible(true);
    }

    private void initializeFrame() {

        setTitle("Java Quiz Application");
        setSize(800, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        getContentPane().setBackground(new Color(245, 247, 250));
        
    }

    private void initializeComponents() {

        setLayout(new BorderLayout(20, 20));

        // ================= TOP PANEL =================
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        questionNumberLabel.setForeground(new Color(37, 99, 235));

        questionLabel = new JLabel("", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        questionLabel.setForeground(new Color(40, 40, 40));

        topPanel.add(questionNumberLabel, BorderLayout.NORTH);
        topPanel.add(questionLabel, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // ================= OPTIONS PANEL =================
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(4, 1, 15, 15));
        optionsPanel.setBackground(new Color(245, 247, 250));
        optionsPanel.setBorder(new EmptyBorder(20, 40, 20, 40));

        option1 = createOptionButton();
        option2 = createOptionButton();
        option3 = createOptionButton();
        option4 = createOptionButton();

        buttonGroup = new ButtonGroup();

        buttonGroup.add(option1);
        buttonGroup.add(option2);
        buttonGroup.add(option3);
        buttonGroup.add(option4);

        optionsPanel.add(option1);
        optionsPanel.add(option2);
        optionsPanel.add(option3);
        optionsPanel.add(option4);

        add(optionsPanel, BorderLayout.CENTER);

        // ================= BOTTOM PANEL =================
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(245, 247, 250));

        nextButton = new JButton("Next");

        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 18));
        nextButton.setBackground(new Color(37, 99, 235));
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setPreferredSize(new Dimension(140, 45));

        bottomPanel.add(nextButton);

        add(bottomPanel, BorderLayout.SOUTH);

        nextButton.addActionListener(e -> nextQuestion());
    }

    private JRadioButton createOptionButton() {

        JRadioButton button = new JRadioButton();

        button.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        return button;
    }

    private void loadQuestion() {

        Question question = questions.get(currentQuestionIndex);

        questionNumberLabel.setText(
                "Question " + (currentQuestionIndex + 1) + " of " + questions.size());

        questionLabel.setText(question.getQuestionText());

        option1.setText(question.getOptions()[0]);
        option2.setText(question.getOptions()[1]);
        option3.setText(question.getOptions()[2]);
        option4.setText(question.getOptions()[3]);

        buttonGroup.clearSelection();

        if (currentQuestionIndex == questions.size() - 1) {
            nextButton.setText("Finish");
        } else {
            nextButton.setText("Next");
        }
    }

    private int getSelectedAnswer() {

        if (option1.isSelected()) return 0;
        if (option2.isSelected()) return 1;
        if (option3.isSelected()) return 2;
        if (option4.isSelected()) return 3;

        return -1;
    }

    private void nextQuestion() {

        int selectedAnswer = getSelectedAnswer();

        if (selectedAnswer == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select an answer first!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Question currentQuestion = questions.get(currentQuestionIndex);

        if (selectedAnswer == currentQuestion.getCorrectAnswer()) {
            score++;
        }

        currentQuestionIndex++;

        if (currentQuestionIndex < questions.size()) {

            loadQuestion();

        } else {

            showResult();

        }
    }

    private void showResult() {

        double percentage = (score * 100.0) / questions.size();

        JOptionPane.showMessageDialog(
                this,
                "Quiz Completed!\n\n"
                        + "Correct Answers : " + score + "\n"
                        + "Total Questions : " + questions.size() + "\n"
                        + "Percentage : " + String.format("%.2f", percentage) + "%",
                "Quiz Result",
                JOptionPane.INFORMATION_MESSAGE
        );

        dispose();
    }
}
