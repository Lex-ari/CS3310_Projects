import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

/***
 * Code by Alex Mariano
 * Design and Analysis of Algorithms (CS3310)
 * Dr. Gilbert Young
 * California State Polytechnic University, Pomona
 * Project 1
 */

public class Project_1_Matrix_Multiplication {

    public static void main(String[] args) throws IOException {

        //Test Initialization to ensure that the matrix multiplication works as expected
        int[][] test_array = new int[][]{{0, 1, 2, 3}, {3, 2, 1, 0}, {0, 1, 2, 3}, {3, 2, 1, 0}};
        int[][] test_array2 = new int[][]{{3, 2, 1, 0}, {0, 1, 2, 3}, {3, 2, 1, 0}, {0, 1, 2, 3}};
        System.out.println("==TEST==");
        Matrix test_matrix = new Matrix(test_array);
        Matrix test_matrix2 = new Matrix(test_array2);
        System.out.println("Matrix A");
        System.out.println(test_matrix);
        System.out.println("Matrix B");
        System.out.println(test_matrix);

        System.out.println("Traditional Multiplication:");
        System.out.println(traditional_matrix_multiplication(test_matrix, test_matrix2));

        System.out.println("Divide and Conquer:");
        System.out.println(divide_and_conquer(test_matrix, test_matrix2));

        System.out.println("Strassen Method:");
        System.out.println(strassen(test_matrix, test_matrix2));

        // test loggers in the event that the program crashes
        CustomFileWriter traditionalLog = new CustomFileWriter("traditionalLog");
        CustomFileWriter dAndCLog = new CustomFileWriter("dAndCLog");
        CustomFileWriter strassenLog = new CustomFileWriter("strassenLog");
        int n = 2;
        while (true){
            long[] averages = doTestCases(n, 10);
            System.out.println("==COMPLETED N = " + n + " ==");
            traditionalLog.write(n + ": " + averages[0] + "\n");
            System.out.println(n + ": " + "traditional: " + averages[0]);
            dAndCLog.write(n + ": " + averages[1] + "\n");
            System.out.println(n + ": " + "dAndCLog: " + averages[1]);
            strassenLog.write(n + ": " + averages[2] + "\n");
            System.out.println(n + ": " + "strassenLog: " + averages[2]);
            n = n << 1;
        }
    }

    /**
     * Does test cases and returns a double[3] array containing average time of traditional, dAndC, and strassen
     * @param n
     * @param numTests
     * @return double[3]
     */
    public static long[] doTestCases(int n, int numTests){
        try {
            long[] traditional_time = new long[numTests];
            long[] dAndC_time = new long[numTests];
            long[] strassen_time = new long[numTests];
            for (int i = 0; i < numTests; i++) {
                Matrix a = new Matrix(n, false);
                Matrix b = new Matrix(n, false);
                Matrix c;
                long startTime = System.currentTimeMillis();

                c = traditional_matrix_multiplication(a, b);
                traditional_time[i] = System.currentTimeMillis() - startTime;
                System.out.println("Traditional Received:" + traditional_time[i]);
                startTime = System.currentTimeMillis();

                c = divide_and_conquer(a, b);
                dAndC_time[i] = System.currentTimeMillis() - startTime;
                System.out.println("dAndC Received:" + dAndC_time[i]);
                startTime = System.currentTimeMillis();

                c = strassen(a, b);
                strassen_time[i] = System.currentTimeMillis() - startTime;
                System.out.println("Strassen Received:" + strassen_time[i]);
            }
            return new long[]{getTrialAverage(traditional_time), getTrialAverage(dAndC_time), getTrialAverage(strassen_time)};

        } catch (Exception e){
            System.out.println("Test Failed" + e);
        }
        return null;
    }

    /**
     * Sums a double[] of the time trials and returns the average by excluding the best and worst case
     * @param timeTrials
     * @return long
     */
    public static long getTrialAverage(long[] timeTrials){
        long max = timeTrials[0];
        long min = timeTrials[0];
        long sum = 0;
        for (int i = 0; i < timeTrials.length; i++){
            sum += timeTrials[i];
            if (timeTrials[i] > max){
                max = timeTrials[i];
            }
            if (timeTrials[i] < min){
                min = timeTrials[i];
            }
        }
        sum -= max;
        sum -= min;
        return sum / (timeTrials.length - 2);
    }

    /**
     * Traditional Matrix Multiplication by summing each row multiplied by each col of a and b
     * @param a
     * @param b
     * @return matrix c
     */
    public static Matrix traditional_matrix_multiplication(Matrix a, Matrix b){
        int n = a.length();
        Matrix c = new Matrix(n, true);
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                for (int k = 0; k < n; k++){
                    c.setValue(i, j, c.getValue(i, j) + a.getValue(i, k) * b.getValue(k,j));
                }
            }
        }
        return c;
    }

    /**
     * Divide and Conquer Algorithm, recursively divides itself into smaller sub problems.
     * @param a
     * @param b
     * @return Matrix c
     */
    public static Matrix divide_and_conquer(Matrix a, Matrix b){
        int n = a.length();
        Matrix c = new Matrix(n, true);
        if (n == 1){
            c.setValue(0,0, a.getValue(0, 0) * b.getValue(0, 0));
            return c;
        }
        Matrix[] a_quadrants = a.getEfficientQuadrants();    //TL, TR, BL, BR //11, 12, 21, 22
        Matrix[] b_quadrants = b.getEfficientQuadrants();    //TL, TR, BL, BR

        Matrix c11 = Matrix.addMatrix(divide_and_conquer(a_quadrants[0], b_quadrants[0]), divide_and_conquer(a_quadrants[1], b_quadrants[2]));
        Matrix c12 = Matrix.addMatrix(divide_and_conquer(a_quadrants[0], b_quadrants[1]), divide_and_conquer(a_quadrants[1], b_quadrants[3]));
        Matrix c21 = Matrix.addMatrix(divide_and_conquer(a_quadrants[2], b_quadrants[0]), divide_and_conquer(a_quadrants[3], b_quadrants[2]));
        Matrix c22 = Matrix.addMatrix(divide_and_conquer(a_quadrants[2], b_quadrants[1]), divide_and_conquer(a_quadrants[3], b_quadrants[3]));
        Matrix ret = new Matrix(new Matrix[]{c11, c12, c21, c22});
        return ret;
    }

    /**
     * Strassen method
     * @param a
     * @param b
     * @return
     */
    public static Matrix strassen(Matrix a, Matrix b){
        int n = a.length();
        if (n <= 2){
            return divide_and_conquer(a, b);
        }
        Matrix[] a_quadrants = a.getEfficientQuadrants();    //TL, TR, BL, BR        11, 12, 21, 22
        Matrix[] b_quadrants = b.getEfficientQuadrants();    //TL, TR, BL, BR    ind 0   1   2   3

        Matrix p = strassen(Matrix.addMatrix(a_quadrants[0], a_quadrants[3]), Matrix.addMatrix(b_quadrants[0], b_quadrants[3])); //Strassen (A11+A22, B11+B22)
        Matrix q = strassen(Matrix.addMatrix(a_quadrants[2], a_quadrants[3]), b_quadrants[0]); //Strassen (A21 + A22, B11)
        Matrix r = strassen(a_quadrants[0], Matrix.subMatrix(b_quadrants[1], b_quadrants[3])); //Strassen(A11, B12 - B22)
        Matrix s = strassen(a_quadrants[3], Matrix.subMatrix(b_quadrants[2], b_quadrants[0])); //Strassen(A22, B21 - B11)
        Matrix t = strassen(Matrix.addMatrix(a_quadrants[0], a_quadrants[1]), b_quadrants[3]); //Strassen(A11 + A12, B22)
        Matrix u = strassen(Matrix.subMatrix(a_quadrants[2], a_quadrants[0]), Matrix.addMatrix(b_quadrants[0], b_quadrants[1])); //Strassen(A21 - A11, B11 + B12)
        Matrix v = strassen(Matrix.subMatrix(a_quadrants[1], a_quadrants[3]), Matrix.addMatrix(b_quadrants[2], b_quadrants[3])); //Strassen(A12 - A22, B21 + B22)
        Matrix c11 = Matrix.addMatrix(Matrix.addMatrix(p, v), Matrix.subMatrix(s, t)); //P + S - T + V
        Matrix c12 = Matrix.addMatrix(r, t); //  R + T
        Matrix c21 = Matrix.addMatrix(q, s); // Q + S
        Matrix c22 = Matrix.addMatrix(Matrix.addMatrix(p, u), Matrix.subMatrix(r, q)); // P + R - Q + U
        Matrix ret = new Matrix(new Matrix[]{c11, c12, c21, c22});
        return ret;
    }
}

class CustomFileWriter {
    File file;
    FileWriter fileWriter;

    /**
     * Constructor that allows initialization of a filewriter given a name.
     * @param name
     * @throws IOException
     */
    public CustomFileWriter(String name) throws IOException {
        try {
            file = new File(name + ".txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
            fileWriter = new FileWriter(name + ".txt", true);
        } catch (IOException e){
            System.out.println("IOException" + e);
        }
    }

    /**
     * Simple write method to append to text file. close() repeated each time to save.
     * @param message
     */
    public void write(String message) {
        try {
            fileWriter = new FileWriter(file, true);
            fileWriter.write(message);
            fileWriter.close();
        } catch (IOException e){
            System.out.println("IOException" + e);
        }
    }
}

class Matrix {
    private int[][] data;
    private Matrix[] quadrants;
    private int start_row = 0;
    private int start_col = 0;
    private int length;

    /**
     * Initializes a random Matrix of size n with values 0 - 10
     * @param n
     * @param isEmpty, Set True to 0 values, False to Random values
     */
    public Matrix(int n, boolean isEmpty){
        data = new int[n][n];
        if (!isEmpty){
            for (int row = 0; row < n; row++){
                for (int col = 0; col < n; col++){
                    data[row][col] = (int)(Math.random() * 101);     //Random number between 0-10
                }
            }
        }
        this.length = data.length;
    }

    /**
     * Initializes a new Matrix given an int[][] array
     * @param data
     */
    public Matrix(int[][] data){
        this.data = data;
        this.length = data.length;
    }

    /**
     * Constructor used for efficient copy
     * @param matrix
     */
    public Matrix(Matrix matrix, int length, int row_offset, int col_offset){
        this.data = matrix.data;
        this.length = length;
        this.start_row = row_offset;
        this.start_col = col_offset;
    }

    /**
     * Initializes a new Matrix given quadrants
     * @param quadrants in TL, TR, BL, BR (2, 1, 3, 4) format
     */
    public Matrix(Matrix[] quadrants){
        int n = quadrants[0].length() * 2;
        int halfN = quadrants[0].length();
        data = new int[n][n];
        for (int row = 0; row < n; row++){
            int first_quadrant_offset = 0;
            if (row >= halfN){
                first_quadrant_offset = 2;
            }
            int row_offset = row % halfN;
            for (int col = 0; col < n; col++){
                int second_quadrant_offset = first_quadrant_offset;
                if (col >= halfN){
                    second_quadrant_offset = first_quadrant_offset + 1;
                }
                int col_offset = col % halfN;
                data[row][col] = quadrants[second_quadrant_offset].getValue(row_offset, col_offset);
            }
        }
        this.length = n;
    }

    public void setValue(int row, int col, int value){
        data[row + start_row][col + start_col] = value;
    }

    public int getValue(int row, int col){
        return data[row + start_row][col + start_col];
    }

    public int length(){
        return length;
    }

    public static Matrix addMatrix(Matrix a, Matrix b){
        int n = b.length();
        Matrix c = new Matrix(n, true);
        for (int row = 0; row < n; row++){
            for (int col = 0; col < n; col++){
                c.setValue(row, col, a.getValue(row, col) + b.getValue(row, col));
            }
        }
        return c;
    }

    public static Matrix subMatrix(Matrix a, Matrix b){
        int n = b.length();
        Matrix c = new Matrix(n, true);
        for (int row = 0; row < n; row++){
            for (int col = 0; col < n; col++){
                c.setValue(row, col, a.getValue(row, col) - b.getValue(row, col));
            }
        }
        return c;
    }

    /**
     * Will return quadrants in this specific order:
     * TL, TR, BL, BR
     * Quadrant 2, 1, 3, 4
     * @return Matrix array of the 4 quadrants
     */
    public Matrix[] getQuadrants(){
        if (quadrants != null){
            return quadrants;
        }
        int halfN = length() / 2;
        int n = length();
        Matrix quad2 = new Matrix(halfN, true);
        Matrix quad1 = new Matrix(halfN, true);
        Matrix quad3 = new Matrix(halfN, true);
        Matrix quad4 = new Matrix(halfN, true);
        quadrants = new Matrix[]{quad2, quad1, quad3, quad4};
        for (int row = 0; row < n; row++){
            int first_quadrant_offset = 0;
            if (row >= halfN){
                first_quadrant_offset = 2;
            }
            int row_offset = row % halfN;
            for (int col = 0; col < n; col++){
                int second_quadrant_offset = first_quadrant_offset;
                if (col >= halfN){
                    second_quadrant_offset = first_quadrant_offset + 1;
                }
                int col_offset = col % halfN;
                quadrants[second_quadrant_offset].setValue(row_offset, col_offset, getValue(row, col));
            }
        }
        return quadrants;
    }

    /**
     * Returns new matrices with ref to original matrix, but with different row and col offsets.
     * TL, TR, BL, BR
     * Quadrant 2, 1, 3, 4
     * @return Matrix[] of the 4 quadrants
     */
    public Matrix[] getEfficientQuadrants(){
        if (quadrants != null){
            return quadrants;
        }
        int n = this.length;
        int halfN = n / 2;
        Matrix quad2 = new Matrix(this, halfN, start_row, start_col);
        Matrix quad1 = new Matrix(this, halfN, start_row, start_col + halfN);
        Matrix quad3 = new Matrix(this, halfN, start_row + halfN, start_col);
        Matrix quad4 = new Matrix(this, halfN, start_row + halfN, start_col + halfN);

        quadrants = new Matrix[]{quad2, quad1, quad3, quad4};
        return quadrants;
    }

    @Override
    public String toString(){
        int n = length();
        String ret = "";
        for (int row = 0; row < n; row++){
            ret += "[";
            for (int col = 0; col < n; col++){
                ret += getValue(row, col);
                if (col == n - 1){
                    ret += "]";
                } else {
                    ret += ", ";
                }
            }
            ret += "\n";
        }
        return ret;
    }
}