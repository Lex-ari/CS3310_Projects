

public class Project_1_Matrix_Multiplication {

    public static void main(String[] args) {
    }



    class Matrix {
        private int[][] data;
        private int[][][] quadrants;

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
    }
}

