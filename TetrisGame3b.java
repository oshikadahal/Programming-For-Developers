


/*
 * Tetris Game - Java Implementation
 * 
 * Game Description:
 * This is a classic Tetris game implementation using Java Swing for the GUI. The game consists of a grid where 
 * different Tetriminos (blocks) of varying shapes fall from the top of the screen. The player must manipulate 
 * these blocks to fit them into the grid and form complete rows, which will be cleared. The game ends when 
 * the blocks stack up to the top of the screen.
 * 
 * The game features the following:
 * - A grid of size 10x20 (width x height).
 * - Several block shapes (e.g., O, I, L) that can be rotated and moved left, right, or down.
 * - A score counter that increases as rows are cleared.
 * - The game speeds up as the player scores more points.
 * - An option to restart the game.
 * - A preview of the next block.
 * 
 * Controls:
 * - Left button to move the current block left.
 * - Right button to move the current block right.
 * - Rotate button to rotate the current block.
 * - Restart button to restart the game.
 * 
 * Game Flow:
 * - The game initializes with a blank board.
 * - A new random block appears and falls down.
 * - The player can move the block left, right, rotate, or let it fall down faster.
 * - When the block reaches the bottom or collides with another block, it "lands," and a new block appears.
 * - If a row is completely filled with blocks, it gets cleared, and the score is updated.
 * - The game continues until the blocks stack to the top of the screen.
 * - The game speed increases after every 500 points scored to make the game more challenging.
 * 
 * Algorithm Description:
 * 
 * 1. **Initialization**:
 * - The game board is initialized as a 2D array with a size of 20x10 (height x width).
 * - The initial score is set to 0.
 * - A queue of random blocks is prepared to provide the next block in line.
 * - The next block is generated at the beginning and placed on the board.
 * 
 * 2. **Block Movement**:
 * - The current block starts at the top center of the board.
 * - The player can move the block left or right and rotate it.
 * - The block moves down automatically over time. If it collides with another block or the bottom of the board, it lands.
 * 
 * 3. **Collision Detection**:
 * - A method checks for collisions when moving or rotating the block. A collision occurs if the block:
 *     - Moves out of bounds (off the edges of the board).
 *     - Overlaps with any already placed blocks in the grid.
 * 
 * 4. **Row Clearing**:
 * - When a row is completely filled with blocks, it gets cleared. The rows above it shift down.
 * - The score increases by 100 points for each cleared row.
 * 
 * 5. **Game Over Check**:
 * - The game ends when the top row of the board has any filled cells. This indicates that there is no room for the next block to fall.
 * 
 * 6. **Speed Adjustment**:
 * - The game speed (time between block moves) increases as the player's score increases. Every 500 points, the speed reduces by 50 milliseconds, making the blocks fall faster.
 * 
 * 7. **Restart**:
 * - The game can be restarted at any time by pressing the restart button. It resets the board, score, and block queue to their initial state.
 * 
 * 8. **Drawing**:
 * - The game uses the `paintComponent` method to draw the game board and current block.
 * - Each block is drawn as a filled rectangle of a specific color.
 * - The grid is drawn with lines to represent the board's cells.
 * - The next block is also drawn in a preview area on the side of the game board.
 * 
 * 9. **Controls**:
 * - The game listens to button presses (left, right, rotate, restart) to update the current block's position and rotation.
 * 
 * Classes and Methods:
 * 1. **Block Class**:
 *    - Represents a Tetrimino block with a shape and coordinates (x, y).
 *    - Includes methods for moving the block (left, right, down, up) and rotating it.
 * 
 * 2. **TetrisGame Class**:
 *    - Manages the game state, including the board, current block, next block, score, and game speed.
 *    - Handles game logic, user input, collision detection, row clearing, and drawing of the game elements.
 *    - Contains methods for generating new blocks, moving blocks, rotating blocks, clearing rows, and checking game over.
 * 
 * 3. **GUI**:
 *    - Uses Java Swing components for the user interface, including a game board panel, control buttons, and a score label.
 *    - The game loop runs using a `Timer` to update the game state and repaint the screen periodically.
 */


import java.awt.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

class TetrisGame extends JFrame {

    // Game constants
    private static final int BOARD_WIDTH = 10;
    private static final int BOARD_HEIGHT = 20;
    private static final int CELL_SIZE = 30;
    private static final int INITIAL_GAME_SPEED = 500; // Initial speed (milliseconds per tick)
    private int gameSpeed = INITIAL_GAME_SPEED;

    // Game state
    private int[][] board;
    private Block currentBlock;
    private Block nextBlock;
    private Queue<Block> blockQueue;
    private int score;

    // GUI components
    private JPanel gameBoardPanel;
    private JLabel scoreLabel;

    public TetrisGame() {
        // Initialize game state
        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        blockQueue = new LinkedList<>();
        score = 0;
        generateNewBlock();
        nextBlock = blockQueue.poll();

        // Set up the GUI
        setTitle("Tetris");
        setSize(BOARD_WIDTH * CELL_SIZE + 180, BOARD_HEIGHT * CELL_SIZE + 100); // Adjust size for better spacing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Game Board Panel
        gameBoardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
                drawBlock(g, currentBlock);
                drawNextBlock(g);
            }
        };
        gameBoardPanel.setPreferredSize(new Dimension(BOARD_WIDTH * CELL_SIZE, BOARD_HEIGHT * CELL_SIZE));
        gameBoardPanel.setBackground(new Color(28, 28, 28)); // Dark background for the board

        // Score Label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        scoreLabel.setForeground(Color.WHITE);

        // Control buttons with enhanced style
        JButton leftButton = new JButton("Left");
        JButton rightButton = new JButton("Right");
        JButton rotateButton = new JButton("Rotate");
        JButton restartButton = new JButton("Restart");

        // Button styling
        styleButton(leftButton);
        styleButton(rightButton);
        styleButton(rotateButton);
        styleButton(restartButton);

        leftButton.addActionListener(e -> moveLeft());
        rightButton.addActionListener(e -> moveRight());
        rotateButton.addActionListener(e -> rotate());
        restartButton.addActionListener(e -> restartGame());

        // Styled button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(45, 45, 45));
        buttonPanel.add(leftButton);
        buttonPanel.add(rightButton);
        buttonPanel.add(rotateButton);
        buttonPanel.add(restartButton);

        // Side panel for score and next block
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(new Color(45, 45, 45));
        sidePanel.add(scoreLabel);
        sidePanel.add(new JLabel("Next Block:"));
        sidePanel.add(Box.createVerticalStrut(20));

        // Main panel to hold game board and side panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gameBoardPanel, BorderLayout.CENTER);
        mainPanel.add(sidePanel, BorderLayout.EAST);

        // Add main panel and button panel to the frame
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Game loop with dynamic speed increase
        Timer timer = new Timer(gameSpeed, e -> {
            moveDown();
            if (isGameOver()) {
                ((Timer) e.getSource()).stop();
                JOptionPane.showMessageDialog(this, "Game Over! Final Score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            }
            gameBoardPanel.repaint();
            adjustSpeed(); // Increase speed over time
        });
        timer.start();
    }

    // Block class
    private static class Block {

        private int[][] shape;
        private int x, y;

        public Block(int[][] shape) {
            this.shape = shape;
            this.x = BOARD_WIDTH / 2 - shape[0].length / 2;
            this.y = 0;
        }

        public int[][] getShape() {
            return shape;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void moveDown() {
            y++;
        }

        public void moveLeft() {
            x--;
        }

        public void moveRight() {
            x++;
        }

        public void moveUp() {
            y--;
        }

        public void rotate() {
            int[][] rotatedShape = new int[shape[0].length][shape.length];
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[0].length; j++) {
                    rotatedShape[j][shape.length - 1 - i] = shape[i][j];
                }
            }
            shape = rotatedShape;
        }
    }

    // Game logic
    private void generateNewBlock() {
        int[][][] shapes = {
            {{1, 1}, {1, 1}}, // O shape
            {{1, 1, 1, 1}}, // I shape
            {{1, 0}, {1, 1}}, // L shape
        // Add more shapes as needed
        };
        int[][] shape = shapes[(int) (Math.random() * shapes.length)];
        Block block = new Block(shape);
        blockQueue.add(block);
        if (currentBlock == null) {
            currentBlock = blockQueue.poll();
        }
    }

    private void moveLeft() {
        currentBlock.moveLeft();
        if (isCollision(currentBlock)) {
            currentBlock.moveRight();
        }
    }

    private void moveRight() {
        currentBlock.moveRight();
        if (isCollision(currentBlock)) {
            currentBlock.moveLeft();
        }
    }

    private void rotate() {
        currentBlock.rotate();
        if (isCollision(currentBlock)) {
            for (int i = 0; i < 3; i++) {
                currentBlock.rotate(); // Rotate back
            }
        }
    }

    private void moveDown() {
        currentBlock.moveDown();
        if (isCollision(currentBlock)) {
            currentBlock.moveUp(); // Revert the move
            placeBlock(currentBlock); // Place the block
            int rowsCleared = clearRows(); // Clear full rows
            score += rowsCleared * 100; // Update score
            scoreLabel.setText("Score: " + score); // Update score display
            currentBlock = nextBlock; // Set next block as current
            nextBlock = blockQueue.poll(); // Generate new next block
            generateNewBlock(); // Add a new block to the queue
        }
    }

    private boolean isCollision(Block block) {
        int[][] shape = block.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] != 0 && (block.getY() + i >= BOARD_HEIGHT || block.getX() + j < 0 || block.getX() 
                + j >= BOARD_WIDTH || board[block.getY() + i][block.getX() + j] != 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void placeBlock(Block block) {
        int[][] shape = block.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] != 0) {
                    board[block.getY() + i][block.getX() + j] = 1; // 1 represents a filled cell
                }
            }
        }
    }

    private int clearRows() {
        int rowsCleared = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean rowFilled = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] == 0) {
                    rowFilled = false;
                    break;
                }
            }
            if (rowFilled) {
                rowsCleared++;
                for (int k = i; k > 0; k--) {
                    board[k] = board[k - 1].clone();
                }
                board[0] = new int[BOARD_WIDTH];
                i++;  // Skip the next row, since it's now a copy of the row below
            }
        }
        return rowsCleared;
    }

    private boolean isGameOver() {
        // Check if the first row is filled
        for (int j = 0; j < BOARD_WIDTH; j++) {
            if (board[0][j] != 0) {
                return true;
            }
        }
        return false;
    }

    private void adjustSpeed() {
        if (score % 500 == 0 && gameSpeed > 100) { // Increase speed every 500 points
            gameSpeed -= 50; // Increase speed by reducing delay
        }
    }

    // Restart the game
    private void restartGame() {
        board = new int[BOARD_HEIGHT][BOARD_WIDTH];
        blockQueue.clear();
        score = 0;
        generateNewBlock();
        nextBlock = blockQueue.poll();
        gameSpeed = INITIAL_GAME_SPEED; // Reset speed
        scoreLabel.setText("Score: 0");
        gameBoardPanel.repaint();
    }

    // Drawing methods
    private void drawBoard(Graphics g) {
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i][j] != 0) {
                    g.setColor(Color.CYAN);
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
                g.setColor(Color.GRAY);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    private void drawBlock(Graphics g, Block block) {
        int[][] shape = block.getShape();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] != 0) {
                    g.setColor(Color.ORANGE); // Vivid color for current block
                    g.fillRect((block.getX() + j) * CELL_SIZE, (block.getY() + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }
    }

    private void drawNextBlock(Graphics g) {
        if (nextBlock != null) {
            int[][] shape = nextBlock.getShape();
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[0].length; j++) {
                    if (shape[i][j] != 0) {
                        g.setColor(Color.GREEN);
                        g.fillRect((BOARD_WIDTH + 2 + j) * CELL_SIZE, (2 + i) * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    // Button Styling Method
    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 153, 255)); // Blue background
        button.setForeground(Color.WHITE); // White text
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        button.setPreferredSize(new Dimension(100, 40));
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TetrisGame game = new TetrisGame();
            game.setVisible(true);
        });
    }
}
