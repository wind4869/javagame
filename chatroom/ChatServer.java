import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer
{
	public static void main(String[] args)
	{
		ChatServer cs = new ChatServer();
	}

	public ChatServer()
	{
		try
		{
			ServerSocket s = new ServerSocket(1234);

			while (true)
			{
				Socket incoming = s.accept();
				clients.add(incoming);

				Runnable r = new ChatServerHandler(incoming);
				Thread t = new Thread(r);
				t.start();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private ArrayList<Socket> clients = new ArrayList<Socket>();
	private ArrayList<String> usersOnline = new ArrayList<String>();

	class ChatServerHandler implements Runnable
	{
		public ChatServerHandler(Socket incoming)
		{
			this.incoming = incoming;
		}

		public void run()
		{
			try
			{
				InputStream inStream = incoming.getInputStream();
				OutputStream outStream = incoming.getOutputStream();

				Scanner in = new Scanner(inStream);
				PrintWriter out = new PrintWriter(outStream, true);

				while (in.hasNextLine())
				{
					String message = in.nextLine();
					String flag = message.substring(0, 3);

					if (flag.equals("INF"))
					{
						String userName = message.substring(5);
						for (String s : usersOnline)
						{
							if (s.equals(userName))
							{
								userName += "_";
								break;
							}
						}
						usersOnline.add(userName);
						out.println("RET: Dollars: Hi, " + userName);
						//System.out.println("RET: Dollars: Hi, " + userName);
					}
					else if (flag.equals("MSG"))
					{
						for (Socket s : clients)
						{
							PrintWriter o = new PrintWriter(s.getOutputStream(), true);
							o.println(message);
						}
					}
		
					//System.out.println(message);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		private Socket incoming;
	}
}
