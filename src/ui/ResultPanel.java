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

        // ── Outer wrapper for vertical centering ──
        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(new Color(15, 23, 42));

        JPanel card = buildCard(score, total, listener);
        center.add(card);
        add(center, BorderLayout.CENTER);
    }

    private JPanel buildCard(int score, int total, ResultListener listener) {

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(30, 41, 59));
        card.setBorder(new EmptyBorder(50, 60, 50, 60));

        // ── Trophy emoji label ──
        JLabel trophy = new JLabel(getTrophy(score, total), SwingConstants.CENTER);
        trophy.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 72));
        trophy.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Title ──
        JLabel title = new JLabel("Quiz Completed!", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Score circle panel ──
        ScoreCirclePanel scoreCircle = new ScoreCirclePanel(score, total);
        scoreCircle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreCircle.setMaximumSize(new Dimension(180, 180));

        // ── Stats row ──
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 20, 0));
        statsRow.setBackground(new Color(30, 41, 59));
        statsRow.setMaximumSize(new Dimension(480, 90));
        statsRow.setAlignmentX(Component.CENTER_ALIGNMENT);

        statsRow.add(buildStat("✅ Correct", String.valueOf(score), new Color(34, 197, 94)));
        statsRow.add(buildStat("❌ Wrong", String.valueOf(total - score), new Color(239, 68, 68)));
        statsRow.add(buildStat("📊 Accuracy", String.format("%.0f%%", score * 100.0 / total), new Color(99, 102, 241)));

        // ── Performance message ──
        JLabel message = new JLabel(getMessage(score, total), SwingConstants.CENTER);
        message.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        message.setForeground(new Color(148, 163, 184));
        message.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ── Buttons ──
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonRow.setBackground(new Color(30, 41, 59));

        JButton playAgain = createButton("🔄  Play Again", new Color(99, 102, 241), Color.WHITE);
        JButton exit      = createButton("✖  Exit",       new Color(51, 65, 85),   new Color(203, 213, 225));

        playAgain.addActionListener(e -> listener.onPlayAgain());
        exit.addActionListener(e -> listener.onExit());

        buttonRow.add(playAgain);
        buttonRow.add(exit);

        // ── Assemble ──
        card.add(trophy);
        card.add(Box.createVerticalStrut(10));
        card.add(title);
        card.add(Box.createVerticalStrut(30));
        card.add(scoreCircle);
        card.add(Box.createVerticalStrut(30));
        card.add(statsRow);
        card.add(Box.createVerticalStrut(20));
        card.add(message);
        card.add(Box.createVerticalStrut(35));
        card.add(buttonRow);

        return card;
    }

    private JPanel buildStat(String label, String value, Color valueColor) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(new Color(15, 23, 42));
        p.setBorder(new EmptyBorder(12, 10, 12, 10));

        JLabel val = new JLabel(value, SwingConstants.CENTER);
        val.setFont(new Font("Segoe UI", Font.BOLD, 26));
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

    private String getTrophy(int score, int total) {
        double pct = score * 100.0 / total;
        if (pct == 100) return "🏆";
        if (pct >= 80)  return "🥇";
        if (pct >= 60)  return "🥈";
        if (pct >= 40)  return "🥉";
        return "📚";
    }

    private String getMessage(int score, int total) {
        double pct = score * 100.0 / total;
        if (pct == 100) return "Perfect score! You're a Java master!";
        if (pct >= 80)  return "Excellent work! Almost perfect!";
        if (pct >= 60)  return "Good job! Keep practising!";
        if (pct >= 40)  return "Not bad — review the topics and try again!";
        return "Keep studying — you'll get there!";
    }

    // ── Inner class: animated score circle ──────────────────────────────────
    static class ScoreCirclePanel extends JPanel {

        private final int score;
        private final int total;
        private float animArc = 0f;

        ScoreCirclePanel(int score, int total) {
            this.score = score;
            this.total = total;
            setOpaque(false);
            setPreferredSize(new Dimension(160, 160));

            // Animate the arc filling up
            Timer t = new Timer(10, null);
            float target = 360f * score / total;
            t.addActionListener(e -> {
                if (animArc < target) {
                    animArc = Math.min(animArc + 6f, target);
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

            int size = Math.min(getWidth(), getHeight()) - 20;
            int x = (getWidth()  - size) / 2;
            int y = (getHeight() - size) / 2;
            int stroke = 12;

            // Background ring
            g2.setStroke(new BasicStroke(stroke, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(new Color(51, 65, 85));
            g2.drawArc(x, y, size, size, 90, -360);

            // Filled arc
            Color arcColor = getArcColor();
            g2.setColor(arcColor);
            g2.drawArc(x, y, size, size, 90, -(int) animArc);

            // Score text
            double pct = score * 100.0 / total;
            String pctText = String.format("%.0f%%", pct);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 28));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(Color.WHITE);
            g2.drawString(pctText,
                    getWidth() / 2 - fm.stringWidth(pctText) / 2,
                    getHeight() / 2 + fm.getAscent() / 2 - 6);

            String sub = score + " / " + total;
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fm = g2.getFontMetrics();
            g2.setColor(new Color(148, 163, 184));
            g2.drawString(sub,
                    getWidth() / 2 - fm.stringWidth(sub) / 2,
                    getHeight() / 2 + fm.getAscent() / 2 + 16);

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