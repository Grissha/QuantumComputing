public class Core {
    // Тензорное произведение матриц (первая матрица всегда меньше или равна второй!)
    public static double[][] tenzorialMultiplyMatrix(double[][] matrix1, double[][] matrix2) {
        double[][] newMatrix = new double[matrix1.length*matrix2.length][matrix1[0].length*matrix2[0].length];
        for(int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix1[0].length; j++) {
                for (int p = 0; p < matrix2.length; p++) {
                    for (int q = 0; q < matrix2[0].length; q++) {
                        newMatrix[p+i*matrix2.length][q+j*matrix2[q].length] = matrix1[i][j]*matrix2[p][q];
                    }
                }
            }
        }
        System.out.println("Результат тензорного произведения матриц:");
        printMatrix(newMatrix);
        return newMatrix;
    }

    // Скалярное произведение матриц (первая матрица всегда меньше или равна второй!)
    public static double[][] scalarMultiplyMatrix(double[][] matrix1, double[][] matrix2) {
        if (matrix1[0].length != matrix2.length) {
            System.out.println("Умножение невозможно. Матрицы разных размеров");
            return null;
        }
        double[][] newMatrix = new double[matrix1.length][matrix2[0].length];
        for(int i = 0; i < matrix1.length; i++) {
            for (int j = 0; j < matrix2[0].length; j++) {
                for (int p = 0; p < matrix1[0].length; p++) {
                    newMatrix[i][j] = newMatrix[i][j] + matrix1[i][p] * matrix2[p][j];
                }
            }
        }
        System.out.println("Результат скалярного произведения матриц:");
        printMatrix(newMatrix);
        return newMatrix;
    }

    // Получение матрицы кубита
    public static double[][] getMatrixFromQubit (Qubit qubit) {
        double[][] matrix = new double[2][1];
        matrix[0][0] = qubit.getX();
        matrix[1][0] = qubit.getY();

        return matrix;
    }

    // Получение матрицы системы кубитов
    public static double[][] getMatrixFromQubit (Qubit[] qubits) {
        double[][] matrix = getMatrixFromQubit(qubits[0]);
        for (int i = 1; i < qubits.length; i++) {
            matrix = tenzorialMultiplyMatrix(matrix, getMatrixFromQubit(qubits[i]));
        }

        return matrix;
    }

    // Печать матрицы в консоль
    public static void printMatrix (double[][] matrix) {
        System.out.println("-------------------");
        System.out.println("Матрица:");
        for(int i = 0; i < matrix.length; i++) {
            System.out.print("\t| ");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j]);
                if (j != matrix[i].length - 1) {
                    System.out.print(" ");
                }
            }
            System.out.println("|");
        }
        System.out.println("-------------------");
    }

    public static Qubit[] getQubitsFromMatrix (double[][] matrix) {
        int length = (int)(Math.log(matrix.length)/Math.log(2));
        Qubit[] qubits = new Qubit[length];
        int kolvoNotZero = 0;
        int indexNotZero = 0;

        for(int i = 0; i < matrix.length; i++) {
            if((matrix[i][0] > 0.99) && (matrix[i][0] < 1.01)) {
                kolvoNotZero++;
                indexNotZero = i;
            }
        }

        if (kolvoNotZero == 1) {
            int j = 0;
            String s = Integer.toBinaryString(indexNotZero);
            for(int i = 0; i < qubits.length; i++) {
                if((qubits.length - s.length() - i) > 0) {
                    qubits[i] = new Qubit(0);
                } else {
                    switch (s.charAt(j)) {
                        case '0':
                            qubits[i] = new Qubit(0);
                            break;
                        case '1':
                            qubits[i] = new Qubit(1);
                            break;
                    }
                    j++;
                }
            }

            return qubits;
        } else {
            System.out.println("Can't make qubits from matrix");
            return null;
        }
    }
}
