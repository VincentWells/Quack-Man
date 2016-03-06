
import java.util.ListIterator;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
/*basically, this a Pac-man game in javafx I created in my spare time based off a school project
*When they octopi die, 2 more respawn.  Additionally, eating the RPG squares give the duck an rpg
*which he can fire using q, instead of eating the Octopi when he eats a big dot as in the original
*game.  Still a few bugs I haven't fixed, and the animations could use some improvement
*/
public class Runner extends Application {
	double gt2 = 0;
	double gt3 = 0;
	String move = "w";
	char prev = 'D';
	Canvas canvas;
	Group root;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		final Board b = new Board();
		b.initBoard();
		final Pacman p = new Pacman(b);
		final HydraeOperator h = new HydraeOperator(p, b);
		final RocketOperator r = new RocketOperator();
		p.initPac();
		h.initHydrae();
		root = new Group();
		final Scene theScene = new Scene(root);
		primaryStage.setScene(theScene);
		primaryStage.setTitle("Quack-Man");
		canvas = new Canvas(1800, 900);
		root.getChildren().add(canvas);
		final Image[] QuackMan = {new Image("QMu.png"), new Image("QMd.png"), new Image("QMr.png"),new Image("QMl.png")};
		final Image[] QuackManR = {new Image("QMRu.png"), new Image("QMRd.png"), new Image("QMRr.png"),new Image("QMRl.png")};
		final Image Background = new Image("Background.png");
		final GraphicsContext gc = canvas.getGraphicsContext2D();
		final long startNanoTime = System.nanoTime();
		new AnimationTimer() {
			public void handle(long currentNanoTime) {
				// double t = (currentNanoTime - startNanoTime) / 1000000000.0;
				double gt = (System.nanoTime() - startNanoTime) / 100000000.0;
				// background image clears canvas
				char c = operation(theScene);
				if (gt - gt2 > 2) {
					if (h.hydrae.size() < 4) {
						h.spawn();
					}
					h.checkCollision();
					h.makeMove();
					h.chucker();
					h.checkCollision();
					if (p.getLives() < 1) {
						stop();
					}
					gt2 = gt;
					r.shootAll();
					drawHydra(gc, h);
				} else if (gt - gt3 > 1.5) {
					if (p.makeMove(c)) {
						prev = c;
					} else {
						p.makeMove(prev);
					}
					if (c == 'Q') {
//					System.out.println(prev);
					if(p.loaded){
						r.r.add(new Rocket(b, p, p.dir, h, p.getXLocation(), p.getYLocation()));
						move = "";
					}
					}
					r.shootAll();
					drawHydra(gc, h);
					gt3 = gt;
					if (b.dotCount() < 1) {
						
						b.setMap(b.getMap() + 1);
						b.initBoard();
						p.initPac();
						h.initHydrae();
						canvas = new Canvas(b.y * 50, b.x * 50);
						root.getChildren().add(canvas);
					}
				}
				gc.drawImage(Background, 0, 0);
				drawMap(gc, b);
				if(p.loaded){
					gc.drawImage(QuackManR[p.getDir()], p.getYLocation() * 50, p.getXLocation() * 50);
				}
				else{
				gc.drawImage(QuackMan[p.getDir()], p.getYLocation() * 50, p.getXLocation() * 50);
				}
				drawHydra(gc, h);
				drawRockets(gc, r);
				gc.setFill(Color.YELLOW);
				gc.setStroke(Color.BLACK);
				gc.setLineWidth(1);
				Font theFont = Font.font("Times New Roman", FontWeight.BOLD, 28);
				gc.setFont(theFont);
				gc.fillText("Lives: " + p.getLives(), 25, 25);
				gc.strokeText("Lives: " + p.getLives(), 25, 25);
				gc.fillText("Crumbs left: " + b.dotCount(), 25, b.x * 50 - 25);
				gc.strokeText("Crumbs left: " + b.dotCount(), 25, b.x * 50 - 25);
			}
		}.start();

		primaryStage.show();
	}

	void drawMap(GraphicsContext gc, Board b) {
		Image wall = new Image("wall.png");
		Image crumb = new Image("crumb.png");
		Image chain = new Image("chain.png");
		Image rocketIcon = new Image("rpg.png");
		for (int j = 0; j < b.y; j++) {
			for (int i = 0; i < b.x; i++) {
				char t = b.board[i][j];
				switch (t) {
				case ('='): {
					gc.drawImage(wall, (double) j * 50, (double) i * 50);
					break;
				}
				case ('.'): {
					gc.drawImage(crumb, (double) j * 50, (double) i * 50);
					break;
				}
				case ('_'): {
					gc.drawImage(chain, (double) j * 50, (double) i * 50);
					break;
				}
				case ('o'): {
					gc.drawImage(rocketIcon, (double) j * 50, (double) i * 50);
					break;
				}
				}
			}
		}
	}

	void drawHydra(GraphicsContext gc, HydraeOperator h) {
		Image hydra = new Image("hydra.png");
		ListIterator<Hydra> it = h.hydrae.listIterator();
		Hydra hy = it.next();
		while (it.hasNext()) {
			gc.drawImage(hydra, hy.getYLocation() * 50, hy.getXLocation() * 50);
			hy = it.next();
		}
	}
	
	Image explosion[] = {new Image("1.png"),new Image("2.png"),new Image("3.png"),new Image("4.png"),new Image("5.png"),new Image("6.png"),new Image("7.png"),new Image("8.png"),new Image("9.png"),new Image("10.png")};
	
	void drawRockets(GraphicsContext gc, RocketOperator r){
//		System.out.println("rock");
		Image rocket[] = {new Image("rpgu.png"),new Image("rpgd.png"),new Image("rpgr.png"),new Image("rpgl.png")};
		for(Rocket rk: r.r){
			int x = rk.getXLocation();
			int y = rk.getYLocation();
//			System.out.println(rk.dir);
			gc.drawImage(rocket[rk.dir], y*50, x*50);
		}
		int rem[] = new int[r.b.size()];
		int move = 0;
		for(boom b: r.b){
			if(b.state  < 10){
				System.out.println("bam " + b.x + " " + b.y + " " + b.state + " " + r.b.size());
				gc.drawImage(explosion[b.state], b.y*50, b.x*50);
				b.increment();
				System.out.println(b.state);
			}
			else{
				rem[move] = r.b.indexOf(b);
				System.out.println("terminate");
				move++;
			}
		}
		while(move > 0){
			r.b.remove(move - 1);
			move--;
		}
	}

	char operation(Scene theScene) {
		theScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			public void handle(KeyEvent e) {
				move = e.getCode().toString();
			}
		});
		if(move.equals("")){
			return ' ';
		}
		else{
		return move.charAt(0);
		}
	}
	
	void animateExplosion(int x, int y, GraphicsContext gc){
		
	}
}
