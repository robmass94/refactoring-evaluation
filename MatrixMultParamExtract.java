import java.util.Random;

class Matrix {
	public int[][] data;
	public int rows;
	public int cols;
	public String name;

	public Matrix(int rows, int cols, String name) {
		data = new int[rows][cols];
		this.rows = rows;
		this.cols = cols;
		this.name = name;
	}
}

public class MatrixMultParamExtract {
	public static void main(String[] args) {
		Matrix m1 = new Matrix(1000, 1000, "Matrix 1");
		Matrix m2 = new Matrix(1000, 1000, "Matrix 2");
		initializeMatrix(m1);
		initializeMatrix(m2);
		printMatrix(m1);
		printMatrix(m2);
		Matrix result = multiply(m1, m2);
		printMatrix(result);
	}

	private static void initializeMatrix(Matrix m) {
		Random rand = new Random();
		for (int i = 0; i < m.rows; ++i) {
			for (int j = 0; j < m.cols; ++j) {
				m.data[i][j] = rand.nextInt(6);
			}
		}
	}

	private static Matrix multiply(Matrix m1, Matrix m2) {
		Matrix result = new Matrix(m1.rows, m2.cols, "Resulting matrix");
		for (int i = 0; i < m1.rows; ++i) {
			for (int j = 0; j < m2.cols; ++j) {
				result.data[i][j] = 0;
				for (int k = 0; k < m1.cols; ++k) {
					result.data[i][j] += m1.data[i][k] * m2.data[k][j];
				}
			}
		}
		return result;
	}

	private static void printMatrix(Matrix m) {
		System.out.println(m.name + ":");
		for (int i = 0; i < m.rows; ++i) {
			for (int j = 0; j < m.cols; ++j) {
				System.out.print(m.data[i][j] + " ");
			}
			System.out.println();
		}
	}
}