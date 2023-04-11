
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class Project_1_Matrix_Multiplication {

    public static void main(String[] args) throws IOException {

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


        CustomFileWriter traditionalLog = new CustomFileWriter("traditionalLog");
        CustomFileWriter dAndCLog = new CustomFileWriter("dAndCLog");
        CustomFileWriter strassenLog = new CustomFileWriter("strassenLog");
        int n = 2;
        while (true){
            long[] averages = doTestCases(n, 10);
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
                startTime = System.currentTimeMillis();

                c = divide_and_conquer(a, b);
                dAndC_time[i] = System.currentTimeMillis() - startTime;
                startTime = System.currentTimeMillis();

                c = strassen(a, b);
                strassen_time[i] = System.currentTimeMillis() - startTime;
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
     * @return
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

    public static Matrix divide_and_conquer(Matrix a, Matrix b){
        int n = a.length();
        Matrix c = new Matrix(n, true);
        if (n == 1){
            c.setValue(0,0, a.getValue(0, 0) * b.getValue(0, 0));
            return c;
        }
        Matrix[] a_quadrants = a.getQuadrants();    //TL, TR, BL, BR //11, 12, 21, 22
        Matrix[] b_quadrants = b.getQuadrants();    //TL, TR, BL, BR

        Matrix c11 = Matrix.addMatrix(divide_and_conquer(a_quadrants[0], b_quadrants[0]), divide_and_conquer(a_quadrants[1], b_quadrants[2]));
        Matrix c12 = Matrix.addMatrix(divide_and_conquer(a_quadrants[0], b_quadrants[1]), divide_and_conquer(a_quadrants[1], b_quadrants[3]));
        Matrix c21 = Matrix.addMatrix(divide_and_conquer(a_quadrants[2], b_quadrants[0]), divide_and_conquer(a_quadrants[3], b_quadrants[2]));
        Matrix c22 = Matrix.addMatrix(divide_and_conquer(a_quadrants[2], b_quadrants[1]), divide_and_conquer(a_quadrants[3], b_quadrants[3]));
        Matrix ret = new Matrix(new Matrix[]{c11, c12, c21, c22});
        return ret;
    }

    public static Matrix strassen(Matrix a, Matrix b){
        int n = a.length();
        if (n <= 2){
            return divide_and_conquer(a, b);
        }
        Matrix[] a_quadrants = a.getQuadrants();    //TL, TR, BL, BR        11, 12, 21, 22
        Matrix[] b_quadrants = b.getQuadrants();    //TL, TR, BL, BR    ind 0   1   2   3

        Matrix p = strassen(Matrix.addMatrix(a_quadrants[0], a_quadrants[3]), Matrix.addMatrix(b_quadrants[0], b_quadrants[3]));
        Matrix q = strassen(Matrix.addMatrix(a_quadrants[2], a_quadrants[3]), b_quadrants[0]);
        Matrix r = strassen(a_quadrants[0], Matrix.subMatrix(b_quadrants[1], b_quadrants[3]));
        Matrix s = strassen(a_quadrants[3], Matrix.subMatrix(b_quadrants[2], b_quadrants[0]));
        Matrix t = strassen(Matrix.addMatrix(a_quadrants[0], a_quadrants[1]), b_quadrants[3]);
        Matrix u = strassen(Matrix.subMatrix(a_quadrants[2], a_quadrants[0]), Matrix.addMatrix(b_quadrants[0], b_quadrants[1]));
        Matrix v = strassen(Matrix.subMatrix(a_quadrants[1], a_quadrants[3]), Matrix.addMatrix(b_quadrants[2], b_quadrants[3]));
        Matrix c11 = Matrix.addMatrix(Matrix.addMatrix(p, v), Matrix.subMatrix(s, t));
        Matrix c12 = Matrix.addMatrix(r, t);
        Matrix c21 = Matrix.addMatrix(q, s);
        Matrix c22 = Matrix.addMatrix(Matrix.addMatrix(p, u), Matrix.subMatrix(r, q));
        Matrix ret = new Matrix(new Matrix[]{c11, c12, c21, c22});
        return ret;
    }
}

class CustomFileWriter {
    File file;
    FileWriter fileWriter;


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
                    data[row][col] = (int)(Math.random() * 11);     //Random number between 0-10
                }
            }
        }
    }

    /**
     * Initializes a new Matrix given an int[][] array
     * @param data
     */
    public Matrix(int[][] data){
        this.data = data;
    }

    /**
     * Initializes a new Matrix given a preexisting matrix
     * @param matrix
     */
    public Matrix(Matrix matrix){
        data = matrix.data;
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
    }

    public void setValue(int row, int col, int value){
        data[row][col] = value;
    }

    public int getValue(int row, int col){
        return data[row][col];
    }

    public int length(){
        return data.length;
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