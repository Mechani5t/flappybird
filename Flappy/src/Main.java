import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main {
	
	public static void main(String[] args) {
		MyFrame f = new MyFrame();
	}
}

class MyFrame extends JFrame {
	int upTime = 0;
	int buildTime = 0;
	Timer t = new Timer(20, new TimerListener());
	MyPanel p;
	
	public MyFrame(){
		setSize(800,700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		p = new MyPanel();
		p.l.setText("Click To Start");
		add(p);
		setTitle("please help me");
		addMouseListener(new ClickListener());
		setVisible(true);
	}
	
	class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			if(p.gameOver){
				t.stop();
			}
			if(upTime == 0){
				
			}else{
				upTime--;
				p.speed+= 1;
			}
			
			if(buildTime == 0){
				buildTime = 60;
				p.addPipe();
			}else{
				buildTime--;
			}
			if(!p.gameOver){
				p.repaint();
			}
		}
	}
	
	class ClickListener extends MouseAdapter {

		public void mousePressed(MouseEvent arg0) {
			if(!t.isRunning() && !p.gameOver){
				t.start();
			}
			upTime = 29;
			p.speed = -12;
		}
		
	}
}

class MyPanel extends JPanel {
	int speed = 0;
	ArrayList<Pipe> pipes = new ArrayList<Pipe>();
	int ballPos = 310;
	boolean gameOver = false;
	int score = 0;
	JLabel l;
	BufferedImage bird;
	BufferedImage birdFlap;
	BufferedImage pipe;
	BufferedImage pipe1;
	
	public MyPanel(){
		l = new JLabel();
		try {
//			bird = ImageIO.read(new File("src/sprites/flappybird0.png"));
//			birdFlap = ImageIO.read(new File("src/sprites/flappybird1.png"));
//			pipe = ImageIO.read(new File("src/sprites/pipe.png"));
//			pipe1 = ImageIO.read(new File("src/sprites/pipe1.png"));
			bird = ImageIO.read(getClass().getResource("/sprites/flappybird0.png"));
			birdFlap = ImageIO.read(getClass().getResource("/sprites/flappybird1.png"));
			pipe = ImageIO.read(getClass().getResource("/sprites/pipe.png"));
			pipe1 = ImageIO.read(getClass().getResource("/sprites/pipe1.png"));
		} catch (IOException e) {
			e.printStackTrace();
		} 
		l.setSize(40,40);
		l.setFont(new Font("SansSerif", Font.PLAIN, 50));
		l.setForeground(Color.WHITE);
		add(l);
	}
	
	public void paintComponent(Graphics g){
		l.setText(score+"");
		g.setColor(Color.CYAN);
		g.fillRect(0, 0, 800, 800);
		g.setColor(Color.GREEN);
		for(int i = 0; i < pipes.size(); i++){
			int n = i;
			if(pipes.get(i).p < -80){
				pipes.remove(i);
				n = -1;
			} else {
				g.drawImage(pipe, pipes.get(i).p, pipes.get(i).y+200, this);
				g.drawImage(pipe1, pipes.get(i).p, pipes.get(i).y-385, this);
				pipes.get(i).update();
			}
			if((pipes.get(i).p<240)&&(pipes.get(i).p>120)&&((pipes.get(i).y>ballPos)||(pipes.get(i).y+160<ballPos))){
				gameOver = true;
			}
			if(pipes.get(i).p == 120){
				score++;
			}
			i = n;
		}
		if((ballPos<0) || (ballPos>610)){
			gameOver = true;
		}
		if(speed<-5){
			g.drawImage(birdFlap, 190, ballPos, 62, 43, this);
		} else {
			g.drawImage(bird, 190, ballPos, 62, 43, this);
		}
		ballPos = ballPos + speed;
		if(pipes.size() == 0){
			l.setText("click to start");
		}
	}
	
	public void addPipe(){
		pipes.add(new Pipe((int) (Math.random()*300) + 100));
	}
}

