import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Tetris extends JFrame
{
	public static void main(String[] args)
	{
		Tetris t = new Tetris();
	}

	public Tetris()
	{
		TetrisPanel tb = new TetrisPanel();
		addKeyListener(tb);
		add(tb);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 500);
		setTitle("Tetris");
		setVisible(true);
		setResizable(false);
	}
}

class TetrisPanel extends JPanel implements KeyListener 
{
	public TetrisPanel()
	{
		newMap();
		newBlock();
		
		Timer timer = new Timer(1000, new TimerListener());
		timer.start();
	}

	private void newMap()
	{
		score = 0;
		for (int i = 0; i < 12; i++)
			for (int j = 0; j < 22; j++)
				if (i == 0 || i == 11 || j == 21)
					panelMap[i][j] = 2;
				else
					panelMap[i][j] = 0;
	}

	private void newBlock()
	{
		color = new Color(
				(new Double(Math.random() * 128)).intValue() + 128,   
				(new Double(Math.random() * 128)).intValue() + 128,   
				(new Double(Math.random() * 128)).intValue() + 128);

		blockType = (int)(Math.random() * 1000) % 7;
		rotateTimes = (int)(Math.random() * 1000) % 4;
		x = 4;
		y = 0;
		current = blocks[blockType];
		for (int i = 0; i < rotateTimes; i++)
			rotate(current);

		if (!isValid(x, y, current)) {
			JOptionPane.showMessageDialog(this, "NEW GAME?");
			newMap();
		}
	}
	
	private void rotate(int[][] block)
	{
		int[][] temp = new int[5][5];
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				if (block[i][j] == 1)
					temp[j][4 - i] = 1;

		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				block[i][j] = temp[i][j];
	}

	private void addToMap(int x, int y, int[][] block)
	{
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				if (block[i][j] == 1)
					panelMap[x + j][y + i] = 1;
	}

	private void killLine()
	{
		int count = 0;
		for (int j = 0; j < 22; j++)
		{
			for (int i = 0; i < 12; i++)
				if (panelMap[i][j] == 1 && ++count == 10)
				{
					for (int l = j; l > 0; l--)
						for (int k = 0; k < 11; k++)
							panelMap[k][l] = panelMap[k][l - 1];
					score += 1;
				}
			count = 0;
		}
	}

	private boolean isValid(int x, int y, int[][] block)
	{
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				if (block[i][j] == 1 && panelMap[x + j][y + i] != 0)
					return false;
		return true;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.setColor(color);
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 5; j++)
				if (current[i][j] == 1)
					g.fillRect((x + j) * 20 + 25, (y + i) * 20 + 10, 20, 20);

		for (int i = 0; i < 12; i++)
			for (int j = 0; j < 22; j++)
			{
				if (panelMap[i][j] == 1) 
				{
					g.setColor(Color.green);
					g.fillRect(i * 20 + 25, j * 20 + 10, 20, 20);
				}
				if (panelMap[i][j] == 2)
				{
					g.setColor(Color.blue);
					g.drawRect(i * 20 + 25, j * 20 + 10, 20, 20);
				}
			}

		g.setColor(Color.red);
		g.setFont(new Font("Tahoma", Font.BOLD, 50));
		g.drawString(score + "", 300, 200);
	}

	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_J:
				if (isValid(x, y + 1, current)) y++;
				killLine();
				break;
			case KeyEvent.VK_K:
				int[][] temp = new int[5][5];
				for (int i = 0; i < 5; i++)
					for (int j = 0; j < 5; j++)
						temp[i][j] = current[i][j];
				rotate(temp);
				if (isValid(x, y, temp) && blockType != 6)
					rotate(current);
				break;
			case KeyEvent.VK_L:
				if (isValid(x + 1, y, current)) x++;
				break;
			case KeyEvent.VK_H:
				if (isValid(x - 1, y, current)) x--;
				break;
			case KeyEvent.VK_SPACE:
				while (isValid(x, y + 1, current)) y++;
				killLine();
				break;
			default:
				break;
		}
		repaint();
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}

	class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			repaint();
			if (isValid(x, y + 1, current))
			{
				y++;
				killLine();
			}
			else
			{
				addToMap(x, y, current);
				killLine();
				newBlock();
			}
		}
	}

	private int blockType;
	private int rotateTimes;
	private int[][] current;

	private int x;
	private int y;

	private Color color;
	private int score;
	private int[][] panelMap = new int[12][22];

	private static final int[][][] blocks = 
	{
		{
			{0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0},
			{0, 1, 1, 1, 0},
			{0, 0, 1, 0, 0},
			{0, 0, 0, 0, 0}
		},
		{
			{0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0},
			{0, 0, 1, 1, 0},
			{0, 1, 1, 0, 0},
			{0, 0, 0, 0, 0}
		},
		{
			{0, 0, 0, 0, 0}, 
			{0, 0, 0, 0, 0},
			{0, 1, 1, 0, 0},
			{0, 0, 1, 1, 0},
			{0, 0, 0, 0, 0}
		},
		{
			{0, 0, 0, 0, 0}, 
			{0, 0, 1, 0, 0},
			{0, 0, 1, 0, 0},
			{0, 0, 1, 1, 0},
			{0, 0, 0, 0, 0}
		},
		{
			{0, 0, 0, 0, 0}, 
			{0, 0, 1, 0, 0},
			{0, 0, 1, 0, 0},
			{0, 1, 1, 0, 0},
			{0, 0, 0, 0, 0}
		},
		{
			{0, 0, 1, 0, 0}, 
			{0, 0, 1, 0, 0},
			{0, 0, 1, 0, 0},
			{0, 0, 1, 0, 0},
			{0, 0, 0, 0, 0}
		},
		{
			{0, 0, 0, 0, 0}, 
			{0, 1, 1, 0, 0},
			{0, 1, 1, 0, 0},
			{0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0}
		}
	};
}
