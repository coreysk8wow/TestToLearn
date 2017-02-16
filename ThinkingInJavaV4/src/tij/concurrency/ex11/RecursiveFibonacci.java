package tij.concurrency.ex11;

public class RecursiveFibonacci {

	public static int fib(int n) {
		if(n == 0)
			return 0;
		else if (n == 1)
			return 1;
		else if (n >= 2)
			return fib(n - 1) + fib(n - 2);
		else
			return -1;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			System.out.print(fib(i) + ", ");
		}
	}

}
