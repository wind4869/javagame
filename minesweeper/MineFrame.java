import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MineFrame extends JFrame implements MouseListener, Runnable
{
	public static void main(String[] args)
	{
		MineFrame mf = new MineFrame();
	}

	MineFrame()
	{
		square = Integer.parseInt(JOptionPane.showInputDialog(this, "请输入雷区边长"));
		mineNumTotal = Integer.parseInt(JOptionPane.showInputDialog(this, "请输入地雷数目"));
		mineSweeped = 0;
		blanksRemain = square * square;
		mineNumRound = new int[square][square];
		isShown = new boolean[square][square];
		mines = new Mine[square][square];
		minePanel = new JPanel(new GridLayout(square, square));
		isRightSoFar = true;

		timeStarted = null;

		bottomPanel = new JPanel(new GridLayout(1, 2));
		timerLabel = new JLabel("Time: " + new Integer(0).toString(), JLabel.CENTER);
		timerLabel.setEnabled(true);
		minesRemainLabel = new JLabel("Mines Remain: " + new Integer(mineNumTotal).toString());
		bottomPanel.add(timerLabel);
		bottomPanel.add(minesRemainLabel);
		this.add(bottomPanel, "South");


		timer = new Thread(this);

		setTitle("Mine Sweeping");
		setSize(square * 45, square * 45);
		setDefaultCloseOperation(EXIT_ON_CLOSE);


		generateMines();
		
		for (int i = 0; i < square; i++) {
			for (int j = 0; j < square; j++) {
				mines[i][j] = new Mine(i, j);
				mines[i][j].addMouseListener(this);
				minePanel.add(mines[i][j]);
			}
		}

		add(minePanel);
		show();
	}

	public void mouseClicked(MouseEvent e)
	{
		if (timeStarted == null)
		{
			timeStarted = new Date();
			timer.start();
		}

		if (e.getSource() instanceof JButton) {
			Mine m = (Mine)e.getSource();
			switch(e.getButton()) {
				case MouseEvent.BUTTON1:
					if (mineNumRound[m.x][m.y] == 9) {
						mineNumRound[m.x][m.y]++;
						showAll();
					}
					else if (mineNumRound[m.x][m.y] == 0)
						autoShown(m.x, m.y);
					else
					{
						showOneBlank(m.x, m.y);
						isShown[m.x][m.y] = true;
						blanksRemain--;
					}
					break;
				case MouseEvent.BUTTON3:
					if (mineNumRound[m.x][m.y] > 10) {
						mineNumRound[m.x][m.y] -= 11;
						isShown[m.x][m.y] = false;
						m.setIcon(new ImageIcon(""));
						blanksRemain++;
						mineSweeped--;
					} 
					else {
						if (mineNumRound[m.x][m.y] != 9)
							isRightSoFar = false;
						mineNumRound[m.x][m.y] += 11;
						isShown[m.x][m.y] = true;
						m.setIcon(new ImageIcon("flag.JPG"));
						blanksRemain--;
						mineSweeped++;
					}
					break;
			}
			if (mineNumTotal - mineSweeped >= 0)
				minesRemainLabel.setText("Mines Remain: " + (mineNumTotal - mineSweeped));
			if ((blanksRemain + mineSweeped == mineNumTotal) || (mineSweeped == mineNumTotal && isRightSoFar)) {
				timer.stop();
				JOptionPane.showMessageDialog(this, "你赢了!再来一局？");
				this.hide();
				MineFrame ms = new MineFrame();
			}
		}
	}

	public void mouseExited(MouseEvent e)
	{}

	public void mouseEntered(MouseEvent e)
	{}

	public void mouseReleased(MouseEvent e)
	{}

	public void mousePressed(MouseEvent e)
	{}

	private void generateMines()
	{   
		RandomPoint rp;
		for (int i = 0; i < mineNumTotal; i++) {
			rp = new RandomPoint(square);
			if (mineNumRound[rp.x][rp.y] == 9) {
				i--;
				continue;
			}
			mineNumRound[rp.x][rp.y] = 9;
			for (int j = rp.x - 1; j < rp.x + 2; j++)
				for (int k = rp.y - 1; k < rp.y + 2; k++)
					if (isInBounds(j, k) && mineNumRound[j][k] != 9)
						mineNumRound[j][k]++;
		}
	}

	private void autoShown(int x, int y)
	{
		for (int i = x - 1; i < x + 2; i++)
			for (int j = y - 1; j < y + 2; j++)
				if (isInBounds(i, j) && !isShown[i][j] && setIsShown(i, j))
					autoShown(i, j);
	}

	private boolean setIsShown(int x, int y)
	{
		if (mineNumRound[x][y] != 9)
		{
			if (!isShown[x][y])
				blanksRemain--;
			isShown[x][y] = true;
			showOneBlank(x, y);
		}

		if (mineNumRound[x][y] == 0)
			return true;
		else
			return false;
	}

	private void showOneBlank(int x, int y)
	{
		int numRound = mineNumRound[x][y];
		if (numRound < 11) {
			JLabel jl;
			if (numRound == 0)
				jl = new JLabel("", JLabel.CENTER);
			else if (numRound == 9)
				jl = new JLabel(new ImageIcon("mine2.JPG"), JLabel.CENTER);
			else if (numRound == 10) 
				jl = new JLabel(new ImageIcon("mine1.JPG"), JLabel.CENTER);
			else {
				jl = new JLabel(mineNumRound[x][y] + "", JLabel.CENTER);
				jl.setForeground(new Color((numRound * 40) % 255, (numRound * 600) % 255, (numRound * 80000) % 255));
			}

			int pos = x * square + y;
			jl.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			jl.setVisible(true);
			minePanel.getComponent(pos).setVisible(false);
			minePanel.remove(pos);
			minePanel.add(jl, pos);
		}
	}

	private void showAll()
	{
		timer.stop();
		for (int i = 0; i < square; i++)	
			for (int j = 0; j < square; j++) {
				if (mineNumRound[i][j] > 10)
					mineNumRound[i][j] -= 11;
				isShown[i][j] = true;
				showOneBlank(i, j);
			} 
		JOptionPane.showMessageDialog(this, "你输了!再来一局？");
		this.hide();
		MineFrame ms = new MineFrame();
	}

	private boolean isInBounds(int x, int y)
	{
		return (x >= 0 && x < square) && (y >= 0 && y < square);
	}

	public void run()
	{
		while (true) {
			Date NowTime = new Date();
			long second = (NowTime.getTime() - timeStarted.getTime()) / 1000;
			timerLabel.setText("Time: " + second);
		}
	}

	private int square;
	private int blanksRemain;
	private int mineNumTotal;
	private int mineSweeped;
	private int[][] mineNumRound;
	private boolean[][] isShown;
	private Mine[][] mines;
	private JPanel minePanel;
	private boolean isRightSoFar;

	private Date timeStarted;
	private JPanel bottomPanel;
	private JLabel timerLabel;
    private JLabel minesRemainLabel;

	private Thread timer;
}

class RandomPoint
{
	public int x;
	public int y;

	RandomPoint(int square)
	{
		Random creator = new Random();
		x = creator.nextInt(square);
		y = creator.nextInt(square);
	}
}

class Mine extends JButton
{
	public int x;
	public int y;
	public JButton mineButton;

	Mine(int x, int y)
	{
		this.x = x;
		this.y = y;
		mineButton = new JButton();
	}
}
