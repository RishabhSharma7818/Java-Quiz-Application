package ui;

import data.QuizData;
import model.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class QuizFrame extends JFrame {

    // ── Palette ─────────────────────────────────────────────────────────────
    private static final Color BG_DARK       = new Color(15,  23,  42);
    private static final Color BG_CARD       = new Color(30,  41,  59);
    private static final Color BG_OPTION     = new Color(51,  65,  85);
    private static final Color BG_HOVER      = new Color(71,  85, 105);
    private static final Color ACCENT        = new Color(99, 102, 241);
    private static final Color SUCCESS       = new Color(34, 197,  94);
    private static final Color DANGER        = new Color(239, 68,  68);
    private static final Color TEXT_PRIMARY  = new Color(248, 250, 252);
    private static final Color TEXT_MUTED    = new Color(148, 163, 184);
    private static final Color TIMER_WARN    = new Color(234, 179,   8);
    private static final Color TIMER_DANGER  = new Color(239,  68,  68);

    private static final int TIME_PER_QUESTION = 15;

    // ── State ────────────────────────────────────────────────────────────────
    private List<Question> questions;
    private int currentIndex = 0;
    private int score        = 0;
    private int timeLeft     = TIME_PER_QUESTION;
    private boolean answered = false;

    // ── Swing components ────────────────────────────────────────────────────
    private JLabel  questionNumberLabel;
    private JLabel  questionLabel;
    private JLabel  timerLabel;
    private JPanel  timerCirclePanel;
    private JPanel  progressBarFill;
    private JButton[] optionButtons;
    private JButton nextButton;
    private JPanel  mainPanel;
    private Timer   countdownTimer;

    // ────────────────────────────────────────────────────────────────────────
    public QuizFrame() {
        questions = QuizData.getQuestions();
        initFrame();
        buildUI();
        loadQuestion();
        setVisible(true);
    }

    // ── Frame setup ──────────────────────────────────────────────────────────
    private void initFrame() {
        setTitle("Java Quiz App");
        setSize(860, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BG_DARK);
    }

    // ── Build full UI ────────────────────────────────────────────────────────
    private void buildUI() {
        mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BG_DARK);
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        mainPanel.add(buildTopBar(),     BorderLayout.NORTH);
        mainPanel.add(buildCenter(),     BorderLayout.CENTER);
        mainPanel.add(buildBottomBar(),  BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    // ── TOP BAR  (progress + timer) ──────────────────────────────────────────
    private JPanel buildTopBar() {

        JPanel top = new JPanel(new BorderLayout(20, 0));
        top.setBackground(BG_DARK);
        top.setBorder(new EmptyBorder(0, 0, 24, 0));

        // Left: question label + progress bar
        JPanel leftSide = new JPanel();
        leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
        leftSide.setBackground(BG_DARK);

        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        questionNumberLabel.setForeground(ACCENT);

        // Progress track
        JPanel track = new JPanel(new BorderLayout());
        track.setBackground(new Color(51, 65, 85));
        track.setPreferredSize(new Dimension(0, 6));
        track.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));

        progressBarFill = new JPanel();
        progressBarFill.setBackground(ACCENT);
        track.add(progressBarFill, BorderLayout.WEST);

        leftSide.add(questionNumberLabel);
        leftSide.add(Box.createVerticalStrut(8));
        leftSide.add(track);

        // Right: circular timer
        timerCirclePanel = new TimerCircle();
        timerCirclePanel.setPreferredSize(new Dimension(72, 72));

        top.add(leftSide,         BorderLayout.CENTER);
        top.add(timerCirclePanel, BorderLayout.EAST);

        return top;
    }

    // ── CENTER  (question card + options) ───────────────────────────────────
    private JPanel buildCenter() {

        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setBackground(BG_DARK);

        // Question card
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
                new EmptyBorder(24, 28, 24, 28)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        questionLabel = new JLabel("", SwingConstants.LEFT);
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        questionLabel.setForeground(TEXT_PRIMARY);
        questionLabel.setBorder(new EmptyBorder(0, 0, 0, 0));
        card.add(questionLabel, BorderLayout.CENTER);

        // Options grid
        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 0, 12));
        optionsPanel.setBackground(BG_DARK);
        optionsPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        optionButtons = new JButton[4];
        String[] prefixes = {"A", "B", "C", "D"};
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            optionButtons[i] = createOptionButton(prefixes[i]);
            optionButtons[i].addActionListener(e -> handleOptionClick(idx));
            optionsPanel.add(optionButtons[i]);
        }

        center.add(card);
        center.add(optionsPanel);
        return center;
    }

    // ── BOTTOM BAR  (next button) ────────────────────────────────────────────
    private JPanel buildBottomBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bar.setBackground(BG_DARK);
        bar.setBorder(new EmptyBorder(24, 0, 0, 0));

        nextButton = new JButton("Next  →");
        nextButton.setFont(new Font("Segoe UI", Font.BOLD, 15));
        nextButton.setBackground(ACCENT);
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setBorderPainted(false);
        nextButton.setPreferredSize(new Dimension(150, 46));
        nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(e -> handleNext());

        bar.add(nextButton);
        return bar;
    }

    // ── Option button factory ────────────────────────────────────────────────
    private JButton createOptionButton(String prefix) {
        JButton btn = new JButton();
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setBackground(BG_OPTION);
        btn.setForeground(TEXT_PRIMARY);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(14, 20, 14, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                if (!answered) btn.setBackground(BG_HOVER);
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                if (!answered) btn.setBackground(BG_OPTION);
            }
        });

        return btn;
    }

    // ── Load a question ──────────────────────────────────────────────────────
    private void loadQuestion() {
        answered = false;
        timeLeft = TIME_PER_QUESTION;

        Question q = questions.get(currentIndex);

        // Progress
        int total = questions.size();
        questionNumberLabel.setText("Question  " + (currentIndex + 1) + "  /  " + total);
        int fillWidth = (int) ((currentIndex * 1.0 / total) * 700);
        progressBarFill.setPreferredSize(new Dimension(fillWidth, 6));
        progressBarFill.setBackground(ACCENT);
        mainPanel.revalidate();

        // Question text
        questionLabel.setText("<html><body style='width:640px'>" + q.getQuestionText() + "</body></html>");

        // Options
        String[] opts = q.getOptions();
        String[] prefixes = {"A.  ", "B.  ", "C.  ", "D.  "};
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(prefixes[i] + opts[i]);
            optionButtons[i].setBackground(BG_OPTION);
            optionButtons[i].setForeground(TEXT_PRIMARY);
            optionButtons[i].setEnabled(true);
        }

        // Button label
        nextButton.setText(currentIndex == questions.size() - 1 ? "Finish  ✓" : "Next  →");
        nextButton.setBackground(ACCENT);

        // Timer
        startTimer();
        timerCirclePanel.repaint();
    }

    // ── Option clicked ───────────────────────────────────────────────────────
    private void handleOptionClick(int selectedIdx) {
        if (answered) return;
        answered = true;
        stopTimer();

        int correct = questions.get(currentIndex).getCorrectAnswer();
        if (selectedIdx == correct) score++;

        // Highlight all buttons
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setEnabled(false);
            if (i == correct) {
                optionButtons[i].setBackground(SUCCESS);
                optionButtons[i].setForeground(Color.WHITE);
            } else if (i == selectedIdx) {
                optionButtons[i].setBackground(DANGER);
                optionButtons[i].setForeground(Color.WHITE);
            } else {
                optionButtons[i].setBackground(BG_OPTION);
                optionButtons[i].setForeground(TEXT_MUTED);
            }
        }
    }

    // ── Next / Finish ────────────────────────────────────────────────────────
    private void handleNext() {
        if (!answered) {
            // Shake-style warning on the next button
            JOptionPane.showMessageDialog(this,
                    "Please select an answer before continuing.",
                    "No Answer Selected",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        currentIndex++;
        if (currentIndex < questions.size()) {
            loadQuestion();
        } else {
            showResult();
        }
    }

    // ── Timer ────────────────────────────────────────────────────────────────
    private void startTimer() {
        stopTimer();
        countdownTimer = new Timer(1000, e -> {
            timeLeft--;
            timerCirclePanel.repaint();
            if (timeLeft <= 0) {
                stopTimer();
                // Auto-mark as wrong (no selection)
                answered = true;
                int correct = questions.get(currentIndex).getCorrectAnswer();
                for (int i = 0; i < 4; i++) {
                    optionButtons[i].setEnabled(false);
                    if (i == correct) {
                        optionButtons[i].setBackground(SUCCESS);
                        optionButtons[i].setForeground(Color.WHITE);
                    } else {
                        optionButtons[i].setBackground(BG_OPTION);
                        optionButtons[i].setForeground(TEXT_MUTED);
                    }
                }
            }
        });
        countdownTimer.start();
    }

    private void stopTimer() {
        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }
    }

    // ── Result screen ────────────────────────────────────────────────────────
    private void showResult() {
        stopTimer();
        ResultPanel result = new ResultPanel(score, questions.size(), new ResultPanel.ResultListener() {
            @Override public void onPlayAgain() {
                // Reset and restart
                currentIndex = 0;
                score        = 0;
                setContentPane(mainPanel);
                revalidate();
                repaint();
                loadQuestion();
            }
            @Override public void onExit() {
                dispose();
            }
        });
        setContentPane(result);
        revalidate();
        repaint();
    }

    // ── Inner class: circular countdown timer ────────────────────────────────
    private class TimerCircle extends JPanel {

        TimerCircle() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size   = Math.min(getWidth(), getHeight()) - 8;
            int x      = (getWidth()  - size) / 2;
            int y      = (getHeight() - size) / 2;
            int stroke = 5;

            // Background circle
            g2.setStroke(new BasicStroke(stroke));
            g2.setColor(new Color(51, 65, 85));
            g2.drawOval(x, y, size, size);

            // Arc
            Color arcCol = timeLeft > 10 ? ACCENT
                         : timeLeft > 5  ? TIMER_WARN
                         :                 TIMER_DANGER;
            g2.setColor(arcCol);
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int arc = (int) (360.0 * timeLeft / TIME_PER_QUESTION);
            g2.drawArc(x, y, size, size, 90, arc);

            // Number
            g2.setFont(new Font("Segoe UI", Font.BOLD, 20));
            FontMetrics fm = g2.getFontMetrics();
            String text = String.valueOf(timeLeft);
            g2.setColor(timeLeft <= 5 ? TIMER_DANGER : TEXT_PRIMARY);
            g2.drawString(text,
                    getWidth() / 2 - fm.stringWidth(text) / 2,
                    getHeight() / 2 + fm.getAscent() / 2 - 2);

            g2.dispose();
        }
    }
}