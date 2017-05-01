import java.util.Random;

public class MatrixMultMethodExtract {
	public static void main(String[] args) {

		final int FIRST_R0WS = 1000;
		final int FIRST_COLS = 1000;
		final int SECOND_ROWS = FIRST_COLS;
		final int SECOND_COLS = 1000;
		int[][] first = new int[FIRST_R0WS][FIRST_COLS];
		int[][] second = new int[SECOND_ROWS][SECOND_COLS];
		int[][] result = new int[FIRST_R0WS][SECOND_COLS];

		initializeMatrix(first, FIRST_R0WS, FIRST_COLS);
		initializeMatrix(second, SECOND_ROWS, SECOND_COLS);
		matrixMultiply(first, second, result, FIRST_R0WS, FIRST_COLS, SECOND_COLS);
		printMatrix(first, FIRST_R0WS, FIRST_COLS, "First Matrix");
		printMatrix(second, SECOND_ROWS, SECOND_ROWS, "Second Matrix");
		printMatrix(result, FIRST_R0WS, SECOND_COLS, "Resulting Matrix");
	}

	private static void matrixMultiply(int[][] first, int[][] second, int[][] result, int firstRows, int firstCols, int secondCols) {
		for (int i = 0; i < firstRows; ++i) {
			for (int j = 0; j < secondCols; ++j) {
				result[i][j] = 0;
				for (int k = 0; k < firstCols; ++k) {
					result[i][j] += first[i][k] * second[k][j];
				}
			}
		}
	}

	private static void initializeMatrix(int[][] matrix, int rows, int cols) {
		Random rand = new Random();
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				matrix[i][j] = rand.nextInt(6);
			}
		}
	}

	private static void printMatrix(int[][] matrix, int rows, int cols, String name) {
		System.out.println(name + ":");
		for (int i = 0; i < rows; ++i) {
			for (int j = 0; j < cols; ++j) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}
}