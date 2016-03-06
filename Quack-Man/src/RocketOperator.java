import java.util.ArrayList;

public class RocketOperator {
	ArrayList<Rocket> r = new ArrayList<Rocket>();
	ArrayList<boom> b = new ArrayList<boom>();

	void shootAll() {
		int i = 0;
		int[] removals = new int[r.size()];
//		System.out.println(r.size());
		if (r.size() > 0) {
			for (Rocket rk : r) {
				if (!rk.shoot()) {
					b.add(new boom(rk.getXLocation(), rk.getYLocation()));
					removals[i] = r.indexOf(rk);
					i++;
				}
			}
		}
		if (i > 0) {
			for (int j : removals) {
				r.remove(j);
			}
		}
	}
}
