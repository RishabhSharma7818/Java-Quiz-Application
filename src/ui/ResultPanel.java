package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ResultPanel extends JPanel {

    public interface ResultListener {
        void onPlayAgain();
        void onExit();
    }

    public ResultPanel(int score, int total, ResultListener listener) {
        setLayout(new BorderLayout());
        setBackground(new Color(15, 23, 42));

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(15, 23, 42));
        center.add(buildCard(score, total, listener));
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildCard(int score, int total, ResultListener listener) {

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(new Color(30, 41, 59));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1),
                new EmptyBorder(40, 60, 40, 60)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 16, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        // ── Title ──
        JLabel title = new JLabel("Quiz Completed!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title.setForeground(Color.WHITE);
        card.add(title, gbc);

        // ── Score circle ──
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 24, 0);
        ScoreCirclePanel scoreCircle = new ScoreCirclePanel(score, total);
        scoreCircle.setPreferredSize(new Dimension(170, 170));
        card.add(scoreCircle, gbc);

        // ── Stats row ──
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 20, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel statsRow = new JPanel(new GridLayout(1, 3, 16, 0));
        statsRow.setBackground(new Color(30, 41, 59));
        statsRow.setPreferredSize(new Dimension(460, 90));

        statsRow.add(buildStat("Correct",  String.valueOf(score),               new Color(34, 197, 94)));
        statsRow.add(buildStat("Wrong",    String.valueOf(total - score),        new Color(239, 68, 68)));
        statsRow.add(buildStat("Accuracy", String.format("%.0f%%", score * 100.0 / total), new Color(99, 102, 241)));

        card.add(statsRow, gbc);

        // ── Message ──
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        gbc.fill = GridBagConstraints.NONE;

        JLabel message = new JLabel(getMessage(score, total), SwingConstants.CENTER);
        message.setFont(new Font("Segoe UI", Font.ITALIC, 15));
        message.setForeground(new Color(148, 163, 184));
        card.add(message, gbc);

        // ── Buttons ──
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 0, 0);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonRow.setBackground(new Color(30, 41, 59));

        JButton playAgain = createButton("Play Again", new Color(99, 102, 241), Color.WHITE);
        JButton exit      = createButton("Exit",       new Color(51, 65, 85),   new Color(203, 213, 225));

        playAgain.addActionListener(e -> listener.onPlayAgain());
        exit.addActionListener(e -> listener.onExit());

        buttonRow.add(playAgain);
        buttonRow.add(exit);
        card.add(buttonRow, gbc);

        return card;
    }

    private JPanel buildStat(String label, String value, Color valueColor) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(15, 23, 42));
        p.setBorder(new EmptyBorder(14, 10, 14, 10));

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("Segoe UI", Font.BOLD, 28));
        val.setForeground(valueColor);
        val.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(148, 163, 184));
        lbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        p.add(val);
        p.add(Box.createVerticalStrut(4));
        p.add(lbl);
        return p;
    }

    private JButton createButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(160, 46));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private String getMessage(int score, int total) {
        double pct = score * 100.0 / total;
        if (pct == 100) return "Perfect score! You are a Java master!";
        if (pct >= 80)  return "Excellent work! Almost perfect!";
        if (pct >= 60)  return "Good job! Keep practising!";
        if (pct >= 40)  return "Not bad — review the topics and try again!";
        return "Keep studying — you will get there!";
    }

    // ── Animated score circle ────────────────────────────────────────────────
    static class ScoreCirclePanel extends JPanel {

        private final int score;
        private final int total;
        private float animArc = 0f;

        ScoreCirclePanel(int score, int total) {
            this.score = score;
            this.total = total;
            setOpaque(false);

            float target = 360f * score / total;
            Timer t = new Timer(12, null);
            t.addActionListener(e -> {
                if (animArc < target) {
                    animArc = Math.min(animArc + 5f, target);
                    repaint();
                } else {
                    ((Timer) e.getSource()).stop();
                }
            });
            t.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size   = Math.min(getWidth(), getHeight()) - 16;
            int x      = (getWidth()  - size) / 2;
            int y      = (getHeight() - size) / 2;
            int stroke = 13;

            // Background ring
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(51, 65, 85));
            g2.drawOval(x, y, size, size);

            // Filled arc
            g2.setColor(getArcColor());
            g2.drawArc(x, y, size, size, 90, -(int) animArc);

            // Percentage text
            double pct = score * 100.0 / total;
            String pctText = String.format("%.0f%%", pct);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 30));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(Color.WHITE);
            g2.drawString(pctText,
                    getWidth() / 2 - fm.stringWidth(pctText) / 2,
                    getHeight() / 2 + 6);

            // score / total sub-text
            String sub = score + " / " + total;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fm = g2.getFontMetrics();
            g2.setColor(new Color(148, 163, 184));
            g2.drawString(sub,
                    getWidth() / 2 - fm.stringWidth(sub) / 2,
                    getHeight() / 2 + 26);

            g2.dispose();
        }

        private Color getArcColor() {
            double pct = score * 100.0 / total;
            if (pct >= 80) return new Color(34, 197, 94);
            if (pct >= 60) return new Color(234, 179, 8);
            return new Color(239, 68, 68);
        }
    }
}