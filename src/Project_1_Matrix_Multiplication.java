public class Project_1_Matrix_Multiplication {

    public static void main(String[] args) {
        int[][] test_array = new int[][]{{2, 2, 1, 1}, {2, 2, 1, 1}, {3, 3, 4, 4}, {3, 3, 4, 4}};
        Matrix test_matrix = new Matrix(test_array);
        Matrix test_matrix2 = new Matrix(test_array);
        Matrix multiplied_matrix = divide_and_conquer(test_matrix, test_matrix2);
        System.out.println(test_matrix);
        System.out.println(multiplied_matrix);
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
        this.data = data.clone();
    }

    /**
     * Initializes a new Matrix given a preexisting matrix
     * @param matrix
     */
    public Matrix(Matrix matrix){
        data = matrix.data.clone();
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