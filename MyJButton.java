import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;


public class MyJButton extends JButton implements MouseListener{
	private static final long serialVersionUID = 1L;
	Minesweeper game;
	int upperValue, lowerValue, row, col;
	public static int flagCount = 0;
	
	public MyJButton(int row, int col, Minesweeper game) {
		this.row = row; this.col = col; this.game = game;
		upperValue = lowerValue = 0;
		setMargin(new Insets(1, 1, 1, 1));
		addMouseListener(this);
		this.setEnabled(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getButton() == MouseEvent.BUTTON1) {
			game.openButton(this);
			game.checkIfWon();
		}
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (isEnabled()) {
			if (arg0.getButton() == MouseEvent.BUTTON3) {
				upperValue = (upperValue+1)%3;
				if (upperValue == 1) {
					MyJButton.flagCount += 1;
				}
				if (upperValue == 2) {
					MyJButton.flagCount -= 1;
				}
				setIcon(game.upperImages[upperValue]);
				game.paint();
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
