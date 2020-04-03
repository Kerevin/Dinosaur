/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dinosaur;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Personal Computer
 */
public class Sprite extends Rectangle {


	/*
	 There are 5 types, which are:
	 1: a single small cactus
	 2: a single big cactus
	 3: three small cacti
	 4: three big cacti
	 5: a crow
	 */
	public int type;
	boolean dead = false;
	public ArrayList<ImagePattern> animation = new ArrayList();

	public void chooseType() {
		switch (type) {
			// a small cactus
			case 0:
				setHeight(65);
				setWidth(30);
				setTranslateY(380 - getHeight());
				setFill(new ImagePattern(new Image(Sprite.class.getResource("img/smallcactus.png").toString())));
				break;

			// a big cactus
			case 1:
				setHeight(90);
				setWidth(40);
				setTranslateY(380 - getHeight());
				setFill(new ImagePattern(new Image(Sprite.class.getResource("img/bigcactus.png").toString())));
				break;

			// three small cacti
			case 2:
				setHeight(65);
				setWidth(90);
				setTranslateY(380 - getHeight());
				setFill(new ImagePattern(new Image(Sprite.class.getResource("img/threesmallcacti.png").toString())));
				break;

			// three big cacti
			case 3:
				setHeight(90);
				setWidth(120);
				setTranslateY(380 - getHeight());
				setFill(new ImagePattern(new Image(Sprite.class.getResource("img/threebigcacti.png").toString())));
				break;

			// a crow
			case 4:
				setHeight(40);
				setWidth(50);
				int random = (int) (Math.random() * 10 + 1) % 2;
				animation.add(new ImagePattern(new Image(Sprite.class.getResource("img/crow1.png").toString())));
				animation.add(new ImagePattern(new Image(Sprite.class.getResource("img/crow2.png").toString())));
				setFill(animation.get(0));
				if (random == 1) {
					setTranslateY(275);
				}
				else {
					setTranslateY(300);
				}
				break;
		}
	}

	Sprite(int type) {
		this.type = type;
		chooseType();
		setTranslateX(Dinosaur.width);

	}

	Sprite(int x, int y, int h, int w, int type) {
		super(w, h);
		this.type = type;
		setTranslateX(x);
		setTranslateY(y);
	}

	public void moveLeft() {
		setTranslateX(getTranslateX() - 6.5);
	}

}
