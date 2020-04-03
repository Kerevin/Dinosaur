/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dinosaur;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 *
 * @author Dang Khoa
 */
public class Dinosaur extends Application {

	Rex rex = new Rex(50, 300, 94, 80, 5);
	Sprite bot = new Sprite((int) (Math.random() * 10 + 1) % 5);
	static Pane root = new Pane();
	static int frame = 0;
	static final int width = 800, height = 410;
	static float time = 0;
	static boolean running;
	static Rectangle gOverImg;
	static Sprite ground;
	static float score = 0;
	static Text scoreText;
	private Image restartImage = new Image(Sprite.class.getResource("img/restart.png").toString());
	private Image groundImg = new Image(Sprite.class.getResource("img/ground.png").toString());
	ImageView restartGame = new ImageView(restartImage);
	ImageView groundView1 = new ImageView(groundImg);
	ImageView groundView2 = new ImageView(groundImg);

	public void resetGame() {
		time = 0;
		score = 0;
		scoreText.setText("0");
		running = true;

		root.getChildren().removeAll(rex, restartGame);

		// remove all 
		getSprites().forEach(e -> {
			if (e.type < 5) {
				root.getChildren().remove(e);
			}
		});
		bot = new Sprite((int) (Math.random() * 10 + 1) % 5);
		root.getChildren().add(bot);

		rex = new Rex(50, 300, 94, 80, 5);
		root.getChildren().add(rex);

	}

	@Override
	public void start(Stage primaryStage) {

		running = true;

		groundView1.setTranslateY(height - 50);
		groundView1.setTranslateX(0);
		groundView2.setTranslateX(groundImg.getWidth());
		groundView2.setTranslateY(height - 50);

		restartGame.setTranslateX(width / 2 - restartImage.getWidth() / 2);
		restartGame.setTranslateY(height / 2);

		scoreText = new Text("0");
		scoreText.setStyle("-fx-font: 20 arials;");
		scoreText.setX(width / 2);
		scoreText.setY(50);

		root.setPrefSize(width, height);
		root.getChildren().add(rex);
		root.getChildren().addAll(groundView1, groundView2, scoreText);

		gOverImg = new Rectangle(380, 20, new ImagePattern(new Image(Sprite.class.getResource("img/gameover.png").toString())));

		createBot();

		Scene scene = new Scene(root);
		scene.setOnKeyPressed(e -> {
			switch (e.getCode()) {
				case SPACE:
					if (running) {
						if (!rex.ducking) {
							rex.jump();
						}
					}
					else {
						resetGame();
					}
					break;
				case DOWN:
					if (rex.getTranslateY() == 300 && running) {
						rex.ducking = true;
						rex.duck();
					}

					break;
			}

		});
		scene.setOnKeyReleased(e -> {
			if (e.getCode().equals(KeyCode.DOWN) && rex.ducking && running) {

				rex.running();
			}

		});
		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				if (running) {
					update();
				}

			}
		};
		timer.start();
		primaryStage.setTitle("Stupid T-Rex");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public void createBot() {
		Sprite newBot = new Sprite((int) (Math.random() * 10 + 1) % 5);
		root.getChildren().add(newBot);
	}

	public List<Sprite> getSprites() {
		return root.getChildren().stream().filter(n -> n instanceof Sprite).map(n -> (Sprite) n).collect(Collectors.toList());
	}

	public void update() {
		time += 0.01;
		score += 0.2;
		scoreText.setText("" + (int) score);
		rex.fall();

		// Animation for T-Rex
		frame = (frame + 1) % 15;

		if (bot.animation.size() > 0) {
			bot.setFill(bot.animation.get(frame / 10));
		}
		if (!rex.ducking && !rex.jumping) {
			rex.setFill(rex.runningAnimation.get(frame / 5));
		}
		else if (rex.ducking) {
			rex.setFill(rex.duckingAnimation.get(frame / 10));
		}

		// Create a corrected hitbox for T-Rex
		Ellipse hitbox = new Ellipse(rex.getTranslateX() + rex.getWidth() / 2, rex.getTranslateY() + rex.getHeight() / 2 - 5, rex.getWidth() - 70, rex.getHeight() - 62);
		hitbox.setScaleX(rex.getScaleX());
		hitbox.setScaleY(rex.getScaleY());
		//root.getChildren().add(hitbox);

		// Controll all bots.
		getSprites().forEach(s -> {
			if (s.type < 5) {
				s.moveLeft();
				if (s.getTranslateX() < -s.getWidth()) {
					s.dead = true;
				}
				if (s.animation.size() > 0) {
					s.setFill(s.animation.get(frame / 10));
				}
				// Check if T-Rex collidates with a bot
				if (s.getBoundsInParent().intersects(hitbox.getBoundsInParent())) {
					rex.die();
					running = false;
					gOverImg.setTranslateX(width / 2 - gOverImg.getWidth() / 2);
					gOverImg.setTranslateY(height / 2 - 100);
					root.getChildren().add(gOverImg);
					root.getChildren().add(restartGame);
				}

			}
		});

		if (time > 0.8) {
			createBot();
			time = 0;
		}

		// remove dead bots
		root.getChildren().removeIf(s -> {
			if (s instanceof Sprite) {
				Sprite n = (Sprite) s;
				if (n.type < 5 && n.dead == true) {

					return true;
				}
				return false;
			}
			else {
				return false;
			}
		});

		// Make ground moving constantly
		groundView1.setTranslateX(groundView1.getTranslateX() - 6.5);
		groundView2.setTranslateX(groundView2.getTranslateX() - 6.5);
		if (groundView1.getTranslateX() < -groundImg.getWidth()) {
			groundView1.setTranslateX(groundView2.getTranslateX() + groundImg.getWidth());
		}
		if (groundView2.getTranslateX() < -groundImg.getWidth()) {
			groundView2.setTranslateX(groundView1.getTranslateX() + groundImg.getWidth());
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	// class T-Rex 
	public static class Rex extends Sprite {

		boolean ducking = false;
		boolean jumping = false;
		Scale scale = new Scale();
		ArrayList<ImagePattern> runningAnimation;
		ArrayList<ImagePattern> duckingAnimation;
		ImagePattern dieImage = new ImagePattern(new Image(Dinosaur.class.getResource("img/die.png").toString()));

		public Rex(int x, int y, int h, int w, int type) {
			super(x, y, h, w, type);
			scale.setPivotX(x);
			scale.setPivotY(y);

			runningAnimation = new ArrayList();
			duckingAnimation = new ArrayList();

			for (int i = 1; i <= 3; i++) {
				String url = "img/running" + String.valueOf(i) + ".png";
				runningAnimation.add(new ImagePattern(new Image(Dinosaur.class.getResource(url).toString())));
			}
			for (int i = 1; i <= 2; i++) {
				String url = "img/ducking" + String.valueOf(i) + ".png";
				duckingAnimation.add(new ImagePattern(new Image(Dinosaur.class.getResource(url).toString())));
			}
			getTransforms().addAll(scale);

		}

		public void jump() {

			if (getTranslateY() == 300) {
				jumping = true;
				setFill(runningAnimation.get(0));
			}

		}

		public void fall() {
			// Falling down
			if (getTranslateY() != 300 && !jumping) {
				setTranslateY(getTranslateY() + 8);
			}
			// Jumping up
			if (jumping && getTranslateY() >= 80) {
				setTranslateY(getTranslateY() - 8);
			}
			else {
				jumping = false;
			}
		}

		public void die() {
			this.ducking = false;
			this.scale.setX(1);
			this.scale.setY(1);
			this.setScaleY(1);
			this.setFill(dieImage);

		}

		public void running() {
			this.ducking = false;
			this.scale.setX(1);
			this.scale.setY(1);
			this.setScaleY(1);
		}

		public void duck() {
			scale.setX(1.5);
			scale.setY(0.9);
			setScaleY(0.7);
		}
	}
}
