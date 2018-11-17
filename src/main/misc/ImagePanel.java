package main.misc;

import java.awt.Graphics;

import javax.swing.JPanel;

public class ImagePanel extends JPanel {

	private static final long serialVersionUID = -8501520100572588099L;

	public void paintComponent(Graphics g) {
		g.drawImage(main.Main.world.img, 0, 0, this);
	}

}
