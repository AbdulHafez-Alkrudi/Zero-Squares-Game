import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ViewGUI implements View {
    private JFrame frame;
    private JPanel gridPanel;
    private int gridSize;
    private Consumer<String> moveListener;
    public ViewGUI() {
        frame = new JFrame("Zero Squares Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(new BorderLayout());

        gridPanel = new JPanel();
        frame.add(gridPanel, BorderLayout.CENTER);

        frame.setVisible(true);
        // Add key listener to capture arrow key inputs
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                String move = switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> "up";
                    case KeyEvent.VK_DOWN -> "down";
                    case KeyEvent.VK_LEFT -> "left";
                    case KeyEvent.VK_RIGHT -> "right";
                    case KeyEvent.VK_ESCAPE -> "restart";
                    default -> null;
                };
                if (move != null && moveListener != null) {
                    moveListener.accept(move);
                }
            }
        });
    }
    public void setMoveListener(Consumer<String> listener) {
        this.moveListener = listener;
    }
    public void displayMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
    public void close()
    {
        frame.dispose();
    }
    @Override
    public void display(Cell[][] grid, int size) {
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.gridSize = size;
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize));

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                gridPanel.add(createCellPanel(grid[row][col]));
            }
        }

        gridPanel.revalidate();
        gridPanel.repaint();
    }

    private JPanel createCellPanel(Cell cell) {
        JPanel cellPanel = new JPanel();
        cellPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Default border

        switch (cell.getType()) {
            case BLOCKED:
                cellPanel.setBackground(Color.BLACK);
                break;
            case EMPTY:
                cellPanel.setBackground(Color.WHITE);
                break;
            case COLORED:
                cellPanel.setBackground(getColor(cell.getCellColor()));
                break;
            case DESTINATION:
                cellPanel.setBackground(Color.WHITE);
                cellPanel.setBorder(BorderFactory.createLineBorder(getColor(cell.getDestinationColor()), 4));
                break;
            case COLORED_AND_DESTINATION:
                cellPanel.setBackground(getColor(cell.getCellColor()));
                cellPanel.setBorder(BorderFactory.createLineBorder(getColor(cell.getDestinationColor()), 4));
                break;
        }

        return cellPanel;
    }

    private Color getColor(String colorCode) {
        return switch (colorCode.toLowerCase()) {
            case "r" -> Color.RED;
            case "b" -> Color.BLUE;
            case "g" -> Color.GREEN;
            case "y" -> Color.YELLOW;
            case "c" -> Color.CYAN;
            case "p" -> Color.PINK;
            case "o" -> Color.ORANGE;
            case "m" -> Color.MAGENTA;
            case "k" -> Color.BLACK;
            case "d" -> Color.DARK_GRAY;
            case "l" -> Color.LIGHT_GRAY;
            default -> Color.GRAY;
        };
    }
}


