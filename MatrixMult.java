import java.util.Random;

public class MatrixMult {
	public static void main(String[] args) {

		final int FIRST_R0WS = 1000;
		final int FIRST_COLS = 1000;
		final int SECOND_ROWS = FIRST_COLS;
		final int SECOND_COLS = 1000;
		int[][] first = new int[FIRST_R0WS][FIRST_COLS];
		int[][] second = new int[SECOND_ROWS][SECOND_COLS];
		int[][] result = new int[FIRST_R0WS][SECOND_COLS];

		Random rand = new Random();

		for (int i = 0; i < FIRST_R0WS; ++i) {
			for (int j = 0; j < FIRST_COLS; ++j) {
				first[i][j] = rand.nextInt(10);
			}
		}

		System.out.println("First Matrix:");
		for (int i = 0; i < FIRST_R0WS; ++i) {
			for (int j = 0; j < FIRST_COLS; ++j) {
				System.out.print(first[i][j] + " ");
			}
			System.out.println();
		}

		for (int i = 0; i < SECOND_ROWS; ++i) {
			for (int j = 0; j < SECOND_COLS; ++j) {
				second[i][j] = rand.nextInt(10);
			}
		}

		System.out.println("\nSecond Matrix:");
		for (int i = 0; i < SECOND_ROWS; ++i) {
			for (int j = 0; j < SECOND_COLS; ++j) {
				System.out.print(second[i][j] + " ");
			}
			System.out.println();
		}

		for (int i = 0; i < FIRST_R0WS; ++i) {
			for (int j = 0; j < SECOND_COLS; ++j) {
				result[i][j] = 0;
				for (int k = 0; k < FIRST_COLS; ++k) {
					result[i][j] += first[i][k] * second[k][j];
				}
			}
		}

		System.out.println("\nResulting Matrix:");
		for (int i = 0; i < FIRST_R0WS; ++i) {
			for (int j = 0; j < SECOND_COLS; ++j) {
				System.out.print(result[i][j] + " ");
			}
			System.out.println();
		}
	}
}