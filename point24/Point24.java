import java.util.*;

public class Point24 {
	private static final double PRECISION = 1E-6;
	private static final int COUNT = 4;
	private static final int RESULT = 24;
	private static final int MAX = 15;

	private static double[] number = new double[COUNT];
	private static String[] expression = new String[COUNT];

	private static double[][] problem = 
	{
		{5, 5, 5, 1},
		{3, 8, 3, 8},
		{7, 3, 3, 7},
		{4, 10, 4, 10},
		{1, 4, 5, 6},
		{1, 5, 11, 11},
		{5, 5, 10, 2},
		{1, 3, 4, 6},
		{7, 4, 4, 7},
		{1, 11, 12, 13},
		{2, 6, 9, 9},
		{3, 8, 8, 10},
		{1, 2, 7, 7},
		{7, 8, 8, 10},
		{1, 7, 13, 13},
		{2, 4, 10, 10}
	};

	public static void main(String[] args) {
		System.out.print("Welome to 24 point game, choose mode (1.by me 2.by you): ");
		Scanner in1 = new Scanner(System.in);
		int mode = in1.nextInt();

		if (mode == 1) {
			int i = 0;
			int index = (int)(Math.random() * MAX);
			number = problem[index];

			System.out.print("Give you four figures: ");
			for (i = 0; i < COUNT; ++i) {
				//number[i] = Math.random() * (MAX - 1) + 1;
				expression[i] = (int)number[i] + "";
				System.out.print(expression[i] + " ");
			}

			System.out.println();
			System.out.print("Please give a solution: ");

			Scanner in2 = new Scanner(System.in);
			String nifixExpression = in2.nextLine();

			Stack stack = new Stack();
			String digit = new String();
			ArrayList postfix_expression = new ArrayList();

			i = 0;
			while (i < nifixExpression.length()) {
				char c = nifixExpression.charAt(i);
				while (c >= '0' && c <= '9' && i < nifixExpression.length()) {
					digit += c;
					if (++i == nifixExpression.length())
						break;
					c = nifixExpression.charAt(i);
				} 
				if (!digit.equals(""))
					postfix_expression.add(digit);
				digit = "";

				switch (c) {
					case '(':
						stack.push(c);
						break;
					case ')':
						while (stack.peek() != '(')
							postfix_expression.add(stack.pop());
						stack.pop();
						break;
					case '+':
					case '-':
						if (stack.empty() || stack.peek() == '(')
							stack.push(c);
						else {
							while (!stack.empty() &&
									(stack.peek() == '+' || stack.peek() == '-' ||
									 stack.peek() == '*' || stack.peek() == '/'))
								postfix_expression.add(stack.pop());
							stack.push(c);
						} 					
						break;
					case '*':
					case '/':
						if (stack.empty() || stack.peek() == '+' ||
								stack.peek() == '-' || stack.peek() == '(')
							stack.push(c);
						else {
							while (!stack.empty() && 
									(stack.peek() == '*' || stack.peek() == '/'))
								postfix_expression.add(stack.pop());
							stack.push(c);
						} 			
						break;
				}
				++i;
			}

			while (!stack.empty())
				postfix_expression.add(stack.pop());

			i = 0;
			for (; i < postfix_expression.size(); ++i) {
				String s = postfix_expression.get(i) + "";
				char c = s.charAt(0);
				if (c >= '0' && c <= '9') 
					stack.push(Double.parseDouble(s));
				else {
					double rvalue = (double)stack.pop();
					double lvalue = (double)stack.pop();
					switch (c) {
						case '+':
							stack.push(lvalue + rvalue);
							break;
						case '-':
							stack.push(lvalue - rvalue);
							break;
						case '*':
							stack.push(lvalue * rvalue);
							break;
						case '/':
							stack.push(lvalue / rvalue);
							break;
					}
				}
				//System.out.println(postfix_expression.get(i));
			}

			if (Math.abs((double)stack.pop() - RESULT) < PRECISION)
				System.out.println("Congratulations, you got it!");
			else {
				if (Search(COUNT))
					System.out.println(" = 24");
				else
					System.out.println("Maybe, there is no solution!");
			}
		} else if (mode == 2) {
			Scanner in3 = new Scanner(System.in);
			System.out.print("Give me four figures: ");
			for (int i = 0; i < COUNT; ++i) {
				//number[i] = Math.random() * (MAX - 1) + 1;
				number[i] = in3.nextDouble();
				expression[i] = (int)number[i] + "";
			}
			if (Search(COUNT))
				System.out.println(" = 24");
			else
				System.out.println("Maybe, there is no solution!");
		}
	}

	private static boolean Search(int n) {
		if (n == 1) {
			if (Math.abs(number[0] - RESULT) < PRECISION) {
				System.out.print("A possible solution: ");
				System.out.print(expression[0].substring(1, expression[0].length() - 1));
				return true;
			} else
				return false;
		}

		for (int i = 0; i < n; ++i) {
			for (int j = i + 1; j < n; ++j) {
				double numi, numj;
				String expi, expj;

				numi = number[i];
				numj = number[j];
				number[j] = number[n - 1];

				expi = expression[i];
				expj = expression[j];
				expression[j] = expression[n - 1];

				number[i] = numi + numj;
				expression[i] = '(' + expi + '+' + expj + ')';
				if (Search(n - 1))
					return true;

				number[i] = numi - numj;
				expression[i] = '(' + expi + '-' + expj + ')';
				if (Search(n - 1))
					return true;

				number[i] = numj - numi;
				expression[i] = '(' + expj + '-' + expi + ')';
				if (Search(n - 1))
					return true;

				number[i] = numi * numj;
				expression[i] = '(' + expi + '*' + expj + ')';
				if (Search(n - 1))
					return true;
				
				if (numj != 0) {
					number[i] = numi / numj;
					expression[i] = '(' + expi + '/' + expj + ')';
					if (Search(n - 1))
						return true;
				}

				if (numi != 0) {
					number[i] = numj / numi;
					expression[i] = '(' + expj + '/' + expi + ')';
					if (Search(n - 1))
						return true;
				}

				number[i] = numi;
				number[j] = numj;
				expression[i] = expi;
				expression[j] = expj;
			}
		}
		return false;
	}
}
