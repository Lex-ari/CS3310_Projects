public class Project_1_Matrix_Multiplication {

    public static void main(String[] args) {

    }

    public Matrix traditional_matrix_multiplication(Matrix a, Matrix b){
        int n = a.length();
        Matrix c = new Matrix(n, true);
        for (int i = 0; i < n; i++){
            for (int j = 0; j < n; j++){
                for (int k = 0; k < n; k++){
                    c.setValue(i, j, a.getValue(i, k) * b.getValue(k,j));
                }
            }
        }
        return c;
    }

    public Matrix divide_and_conquer(Matrix a, Matrix b){
        int n = a.length();
        Matrix c = new Matrix(n, true);
        if (n == 1){
            c.setValue(0,0, a.getValue(0, 0) * b.getValue(0, 0));
        }
        return new Matrix(0, false); //TODO: Finish implementation
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

        public void setValue(int row, int col, int value){
            data[row][col] = value;
        }

        public int getValue(int row, int col){
            return data[row][col];
        }

        public int length(){
            return data.length;
        }

        public Matrix addMatrix(Matrix b){
            int n = b.length();
            Matrix c = new Matrix(n, true);
            for (int row = 0; row < n; row++){
                for (int col = 0; col < n; col++){
                    c.setValue(row, col, getValue(row, col) + b.getValue(row, col));
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
                int quadrant_edit_offset = 0;
                if (row >= halfN){
                    quadrant_edit_offset += 2;
                }
                int row_offset = row % halfN;
                for (int col = 0; col < n; col++){
                    if (col >= halfN){
                        quadrant_edit_offset += 1;
                    }
                    int col_offset = col % halfN;
                    quadrants[quadrant_edit_offset].setValue(row_offset, col_offset, getValue(row, col));
                }
            }
            return quadrants;
        }
    }
}

