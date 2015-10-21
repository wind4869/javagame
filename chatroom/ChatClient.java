import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.event.*;

public class ChatClient extends JFrame implements Runnable
{
	public static void main(String[] args)
	{
		ChatClient cc = new ChatClient();
	}

	public ChatClient()
	{
		chatRecord = new JTextArea();
		messageInput = new JTextField();
		//usersOnline = new java.awt.List();

		chatRecord.setEditable(false);

		add(chatRecord);
		//add(usersOnline, "East");
		add(messageInput, "South");

		Map<String, String> map = System.getenv();
	    userName = map.get("USERNAME");

		try
		{
			socket = new Socket("localhost", 1234);
			inStream = socket.getInputStream();
			outStream = socket.getOutputStream();
			
			in = new Scanner(inStream);
			out = new PrintWriter(outStream, true);

			out.println("INF: " + userName);

			client = new Thread(this);
			client.start();

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		messageInput.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent ev) {
				if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
					out.println("MSG: " + userName + ": " + messageInput.getText());
					//System.out.println("MSG: " + userName + ": " + messageInput.getText());
					messageInput.setText("");
				}
			}
			public void keyReleased(KeyEvent ev) {}
			public void keyTyped(KeyEvent ev) {}
		});

		setSize(400, 300);
		setTitle("Welcome to Dollars");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		show();
	}

	public void run()
	{
		while (true)
		{
			while (in.hasNextLine())
			{
				String message = in.nextLine();
				String flag = message.substring(0, 3);

				if (flag.equals("RET"))
				{
					userName = message.substring(18);
				}
				
				chatRecord.append(message.substring(5) + "\n");
				//System.out.println(message);
			}
		}
	}

	private JTextArea chatRecord;
	private JTextField messageInput;
	//private java.awt.List usersOnline;

	private Socket socket;
	private Thread client;

	private InputStream inStream;
	private OutputStream outStream;
	private Scanner in;
	private PrintWriter out;

	private String userName; 
}
