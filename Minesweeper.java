import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

public class Minesweeper extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	Object lostMsg = "You hit a Mine. Game Over. Better Luck Next Time!";
	Object wonMsg = "Congratulations, You Won the Game!\nTime: "; 
	Object restartMsg = "Are you sure you want to Restart Game with new settings?";
	String[] imagenames = {"", "flag.png", "question_mark.png", "cross.png", "mine.png", "mine1.png", "clock.png"};
	public Icon[] upperImages;
	Image mineImage;
	int numberOfRows, numberOfCols, numberOfMines, timeElapsed;
	boolean isGameOver;
	Dimension size;
	JPanel gamePanel = new JPanel(), scorePanel;
	JLabel timer, flag;
	TextField timerText, flagText;
	Timer clock;
	JMenu optionsmenu, thememenu;
	JMenuItem aboutitem;
	JMenuBar menubar;
	String[] optionsString = {"Beginner(10 mines, 9 x 9)", "Intermediate(40 mines, 16 x 16)", "Advanced(99 mines, 16 x 30)"};
	ButtonGroup optionsGroup = new ButtonGroup();
	JRadioButtonMenuItem[] options = new JRadioButtonMenuItem[3];
	MyJButton[][] button;
	Point[] mine;
	
	public static void main (String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new Minesweeper();
			}
		});
	}
	
	public Minesweeper() {
		super("Minesweeper");
		upperImages = new Icon[7];
		for (int i = 0; i < upperImages.length; i++) {
			upperImages[i] = new ImageIcon(getClass().getResource("/images/" + imagenames[i]));
		}
		mineImage = new ImageIcon(getClass().getResource("/images/" + imagenames[5])).getImage();
		this.setIconImage(mineImage);
		numberOfRows = numberOfCols = 9; numberOfMines = 10;
		size = new Dimension(numberOfRows*35, numberOfCols*45);
		setPreferredSize(size);
		menubar = new JMenuBar();
		optionsmenu = new JMenu("Options");
		options[0] = new JRadioButtonMenuItem(optionsString[0], true);options[0].setMnemonic(KeyEvent.VK_B);
		options[1] = new JRadioButtonMenuItem(optionsString[1], false);options[1].setMnemonic(KeyEvent.VK_I);
		options[2] = new JRadioButtonMenuItem(optionsString[2], false);options[2].setMnemonic(KeyEvent.VK_A);
            for (JRadioButtonMenuItem option : options) {
                optionsGroup.add(option);
                option.addActionListener(this);
                optionsmenu.add(option);
            }
		optionsmenu.setMnemonic(KeyEvent.VK_O);
		aboutitem = new JMenuItem("About", KeyEvent.VK_U);
		aboutitem.addActionListener(this);
		optionsmenu.add(aboutitem);
		menubar.add(optionsmenu);
		setJMenuBar(menubar);
		startGame();
		setVisible(true);
		requestFocusInWindow();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private void startGame() {
		// TODO Auto-generated method stub
		this.setPreferredSize(size);
		isGameOver = false; timeElapsed = 0; MyJButton.flagCount = 0;
		setLayout(new BorderLayout());
		gamePanel.setBorder(new EmptyBorder(20, 20, 0, 20));
		add(gamePanel, BorderLayout.CENTER);
		gamePanel.setLayout(new GridLayout(numberOfRows, numberOfCols));
		createButtons();
		scorePanel = new JPanel();
		timer = new JLabel(upperImages[6]);
		timerText = new TextField("0", 1);
		timerText.setEditable(false);
		scorePanel.add(timer);
		scorePanel.add(timerText);
		flag = new JLabel(upperImages[1]);
		flagText = new TextField(1);
		flagText.setEditable(false);
		scorePanel.add(flag);
		scorePanel.add(flagText);
		add(scorePanel, BorderLayout.SOUTH);
		clock = new Timer(1000, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				timerText.setText(Integer.toString(timeElapsed++));
			}
		});
		paint();
		pack();
	}
	
	public void paint() {
		flagText.setText(Integer.toString(numberOfMines - MyJButton.flagCount));
	}

	private void createButtons() {
		// TODO Auto-generated method stub
		button = new MyJButton[numberOfRows][numberOfCols];
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfCols; j++) {
				button[i][j] = new MyJButton(i, j, this);
				gamePanel.add(button[i][j]);
			}
		}
	}

	private void generateMines(Point firstButtonOpened) {
		// TODO Auto-generated method stub
		System.out.println(firstButtonOpened);
		mine = new Point[numberOfMines];
		Random r = new Random();
		for (int i = 0; i < numberOfMines; i++) {
			mine[i] = new Point(r.nextInt(numberOfRows), r.nextInt(numberOfCols));
			if (replaceMine(mine[i], firstButtonOpened)) {
				i--;
				continue;
			}
			for (int j = 0; j < i; j++) {
				if (mine[i].equals(mine[j])) {
					i--;
					break;
				}
			}
			System.out.println(mine[i]);
		}
	}

	private boolean replaceMine(Point mine, Point firstButtonOpened) {
		// TODO Auto-generated method stub
		int x = firstButtonOpened.x, y = firstButtonOpened.y;
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = y - 1; j <= y + 1; j++) {
				if (mine.equals(new Point(i, j))) {
					return true;
				}
			}
		}
		return false;
	}

	private void plantMines() {
		// TODO Auto-generated method stub
		for (int i = 0; i < numberOfMines; i++) {
			button[mine[i].x][mine[i].y].lowerValue = -1;
		}
	}
	
	private void surroundMines() {
		// TODO Auto-generated method stub
		for (int i = 0; i < numberOfMines; i++) {
			for (int j = mine[i].x - 1; j <= mine[i].x + 1; j++) {
				if (j >= 0 && j < numberOfRows) {
					for (int k = mine[i].y - 1; k <= mine[i].y + 1; k++) {
						if (k >= 0 && k < numberOfCols) {
								if (button[j][k].lowerValue >= 0) {
									button[j][k].lowerValue += 1;
								}
						}
					}
				}
			}
		}
	}

	public void openButton(MyJButton b) {
		// TODO Auto-generated method stub
		if (b.isEnabled() && b.upperValue == 0) {
			switch (b.lowerValue) {
			case -1:
				b.setIcon(upperImages[4]);
				if (!isGameOver) {
					isGameOver = true;
					gameLost();
				}
				break;
			case 0:
				b.setEnabled(false);
				b.setText("");
				if (timeElapsed == 0) {
					clock.start();
					timeElapsed++;
					generateMines(new Point(b.row, b.col));
					plantMines();
					surroundMines();
				}
				for (int i = b.row - 1; i <= b.row + 1; i++) {
					if (i >= 0 && i < numberOfRows) {
						for (int j = b.col - 1; j <= b.col + 1; j++) {
							if (j >= 0 && j < numberOfCols) {
								openButton(button[i][j]);
							}
						}
					}
				}
				break;
			default:
				b.setEnabled(false);
				b.setText(Integer.toString(b.lowerValue));
				break;
			}
		}
	}
	
	private void openAllButtons() {
		// TODO Auto-generated method stub
		for (int i = 0; i < numberOfRows; i++) {
			for (int j = 0; j < numberOfCols; j++) {
				if (button[i][j].lowerValue != -1 && button[i][j].upperValue >= 1) {
					button[i][j].setIcon(upperImages[3]);
				}
				openButton(button[i][j]);
			}
		}
	}

	public void checkIfWon() {
		// TODO Auto-generated method stub
		boolean gameWonFlag = true;
		for (int i = 0; i < numberOfRows && gameWonFlag; i++) {
			for (int j = 0; j < numberOfCols; j++) {
				if (button[i][j].lowerValue != -1 && button[i][j].isEnabled()) {
					gameWonFlag = false;
					break;
				}
			}
		}
		if (gameWonFlag) {
			isGameOver = true;
			gameWon();
		}
	}

	private void gameWon() {
		// TODO Auto-generated method stub
		clock.stop();
		openAllButtons();
		Object[] options = {"Play Again", "Exit"};
		int selection = JOptionPane.showOptionDialog(this, wonMsg + timerText.getText() + " seconds", "You Won", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (selection == JOptionPane.YES_OPTION) {
			size = getSize();
			remove(scorePanel);
			gamePanel.removeAll();
			startGame();
		} else {
			System.exit(0);
		}
	}

	public void gameLost() {
		clock.stop();
		openAllButtons();
		Object[] options = {"Play Again", "Exit"};
		int selection = JOptionPane.showOptionDialog(this, lostMsg, "You Lost", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (selection == JOptionPane.YES_OPTION) {
			size = getSize();
//			remove(gamePanel);
			remove(scorePanel);
			gamePanel.removeAll();
			startGame();
		} else {
			System.exit(0);
		}
	}

	private void restartGame(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int selection = JOptionPane.showConfirmDialog(this, restartMsg);
		if (selection == JOptionPane.YES_OPTION) {
			clock.stop();
			if (arg0.getSource() == options[0]) {
				numberOfRows = numberOfCols = 9;
				numberOfMines = 10;
				size = new Dimension(numberOfRows*35, numberOfCols*45);
			} else if (arg0.getSource() == options[1]) {
				numberOfRows = numberOfCols = 16;
				numberOfMines = 40;
				size = new Dimension(numberOfRows*35, numberOfCols*45);
			} else if (arg0.getSource() == options[2]) {
				numberOfRows = 16; numberOfCols = 30;
				numberOfMines = 99;
				size = new Dimension(850, 600);
			}
			remove(scorePanel);
			gamePanel.removeAll();
			startGame();
			setLocationRelativeTo(null);
		} else {
			switch (numberOfMines) {
			case 10:
				options[0].setSelected(true);
				break;
			case 40:
				options[1].setSelected(true);
				break;
			case 99:
				options[2].setSelected(true);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource() == aboutitem) {
			JOptionPane.showMessageDialog(this, "Created by Ruchit Modi.", getTitle(), JOptionPane.INFORMATION_MESSAGE);
		} else {
			restartGame(arg0);
		}
	}

}