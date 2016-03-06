import java.util.ListIterator;

public class Rocket extends Unit {
	Pacman p;
	int dir;
	boolean live = true;
	HydraeOperator h;
	char prev = '.';
	int x;
	int y;

	public Rocket(Board b, Pacman p, int dir, HydraeOperator h, int x, int y) {
		super(b);
		this.p = p;
		this.dir = dir;
		this.h = h;
		this.x = x;
		this.y = y;
		super.setXLocation(x);
		super.setYLocation(y);
	}

	boolean shoot() {
		checkCollision(super.getXLocation(), super.getYLocation());
		if (live) {
			if (super.mover(dir)) {
//				System.out.println(x + " " + y);
				checkCollision(super.getXLocation(), super.getYLocation());
				return live;
			}
		}
		return false;
	}

	void checkCollision(int x, int y) {
		int add = 0;
		if (b.board[x][y] != '=') {
			ListIterator<Hydra> it = h.hydrae.listIterator();
			Hydra hy = it.next();
			while (it.hasNext()) {
				if (hy.getXLocation() == x && hy.getYLocation() == y) {
					live = false;
					hy.setXLocation(h.spawnX);
					hy.setYLocation(h.spawnY);
					hy.setPrev(' ');
//					System.out.println("hit");
					add++;
				}
				hy = it.next();
			}
		} else {
			live = false;
		}
		while (add > 0) {
			h.spawn();
			add--;
		}
	}
}
