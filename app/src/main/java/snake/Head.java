package snake;

public class Head {
    private int headX;
    private int headY;

    public Head(int headX, int headY) {
        // Start positions for snakes head
        this.headX = headX;
        this.headY = headY;
    }

    public int[] move(Direction direction, int boardRows, int boardCols) {
        if (direction == Direction.UP) {
            if (headY <= 0) { 
                return null;
            }
            headY -= 1;
        } else if (direction == Direction.RIGHT) {
            if (headX >= boardRows) {
                return null;
            }
            headX += 1;
        } else if (direction == Direction.DOWN) {
            if (headY >= boardRows) {
                return null;
            }
            headY += 1;
        } else {
            if (headX <= boardCols) {
                return null;
            }
            headX -= 1;
        }

        int[] headPos = { headX, headY };
        return headPos;
    }
}
