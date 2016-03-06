

/**
 * 
 * @author Vincent
 *
 *         This class is the super for both pacman and the hydrae, and so it
 *         contains basic common elements such a base mover method and x y
 *         coordinates.
 */

public class Unit {
	Board b;
	private int x = 12;
	private int y = 1;

	/**
	 * this constructor gets the board from the game, so the Hydrae and Pacman
	 * can use it to determine legal moves, etc.
	 * 
	 * @param board
	 */

	public Unit(Board board) {
		b = board;
	}

	/**
	 * these getters and setters allow other classes to access and change the
	 * coordinates of Pacman and the Hydrae
	 * 
	 * @param i
	 */

	public void setXLocation(int i) {
		x = i;
	}

	public void setYLocation(int i) {
		y = i;
	}

	public int getXLocation() {
		return x;
	}

	public int getYLocation() {
		return y;
	}

	/**
	 * this method takes an integer and treats it as a direction. I decided to
	 * use a boolean to make it easier to tell whether the move was actually
	 * made. Both pacman and the hydrae have more elaborate move method which
	 * use this method as well as adding unique code of each type.
	 * 
	 * @param i
	 * @return
	 */
	boolean mover(int i) {
		switch (i) {
		case 0:
			switch (b.board[x - 1][y]) {
			case '.':
			case ' ':
			case 'o':
			case 'v':
			case '^':
			case '<':
			case '>':
			case 'M':
			case 'W':
				setXLocation(getXLocation() - 1);
				return true;
			default:
				return false;
			}
		case 1:
			switch (b.board[x + 1][y]) {
			case '.':
			case ' ':
			case 'o':
			case 'v':
			case '^':
			case '<':
			case '>':
			case 'M':
			case 'W':
				setXLocation(getXLocation() + 1);
				return true;
			default:
				return false;
			}
		case 2:
			if (getYLocation() + 1 >= b.board[0].length) {
				setYLocation(0);
				return true;
			} else {
				switch (b.board[x][y + 1]) {
				case '.':
				case ' ':
				case 'o':
				case 'v':
				case '<':
				case '>':
				case 'M':
				case 'W':
					setYLocation(getYLocation() + 1);
					return true;
				default:
					return false;
				}
			}

		case 3:
			if (getYLocation() - 1 < 0) {
				setYLocation(b.board[0].length - 1);
				return true;
			} else {
				switch (b.board[x][y - 1]) {
				case '.':
				case ' ':
				case 'o':
				case 'v':
				case '^':
				case '<':
				case '>':
				case 'M':
				case 'W':
					setYLocation(getYLocation() - 1);
					return true;
				default:
					return false;
				}
			}
		default:
			return false;
		}

	}
}
