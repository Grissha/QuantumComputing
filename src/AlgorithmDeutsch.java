public class AlgorithmDeutsch {
    public static void deutsch (int numberFunction) {
        Qubit qubit1 = new Qubit(0);
        Qubit qubit2 = new Qubit(1);
        Qubit[] qubits = {qubit1, qubit2};
        double[][] matrix = Core.getMatrixFromQubit(qubits);

        System.out.println("Строим матрицу операторов");
        double[][] operatorMatrix = Core.tenzorialMultiplyMatrix(Gates.ADAMARA, Gates.ADAMARA);
        System.out.println("Применяем оператор Адамара для каждого бита");
        matrix = Core.scalarMultiplyMatrix(operatorMatrix, matrix);

        System.out.println("Выполняем функцию");
        switch (numberFunction) {
            case 1:
                System.out.println("Выполняем один раз функцию f(x) = 0");
                matrix = f1(matrix);
                break;
            case 2:
                System.out.println("Выполняем один раз функцию f(x) = 1");
                matrix = f2(matrix);
                break;
            case 3:
                System.out.println("Выполняем один раз функцию f(x) = x");
                matrix = f3(matrix);
                break;
            case 4:
                System.out.println("Выполняем один раз функцию f(x) = not(x)");
                matrix = f4(matrix);
                break;
        }

        System.out.println("Строим матрицу операторов");
        operatorMatrix = Core.tenzorialMultiplyMatrix(Gates.ADAMARA, Gates.IDENTITY_MATRIX);
        System.out.println("Применяем оператор Адамара для 1-ого кубита");
        matrix = Core.scalarMultiplyMatrix(operatorMatrix, matrix);

        if ((matrix[0][0] == 0)&&(matrix[1][0] == 0)) {
            System.out.println("Первый кубит равен 1 => Функция константна");
        }
        else {
            System.out.println("Первый кубит равен 0 => Функция сбалансирована");
        }
    }

    private static double[][] f1(double[][] matrix) {
        System.out.println("Строим матрицу операторов");
        double[][] operatorMatrix = Core.tenzorialMultiplyMatrix(Gates.IDENTITY_MATRIX, Gates.IDENTITY_MATRIX);
        System.out.println("Применяем оператор функции");
        matrix = Core.scalarMultiplyMatrix(operatorMatrix, matrix);
        return matrix;
    }

    private static double[][] f2(double[][] matrix) {
        System.out.println("Строим матрицу операторов");
        double[][] operatorMatrix = Core.tenzorialMultiplyMatrix(Gates.IDENTITY_MATRIX, Gates.NOT);
        System.out.println("Применяем оператор функции");
        matrix = Core.scalarMultiplyMatrix(operatorMatrix, matrix);
        return matrix;
    }

    private static double[][] f3(double[][] matrix) {
        System.out.println("Применяем оператор функции");
        matrix = Core.scalarMultiplyMatrix(Gates.CNOT, matrix);
        return matrix;
    }

    private static double[][] f4(double[][] matrix) {
        System.out.println("Строим матрицу операторов");
        double[][] operatorMatrix = Core.tenzorialMultiplyMatrix(Gates.NOT, Gates.IDENTITY_MATRIX);
        System.out.println("Применяем оператор NOT");
        matrix = Core.scalarMultiplyMatrix(operatorMatrix, matrix);
        System.out.println("Применяем оператор CNOT");
        matrix = Core.scalarMultiplyMatrix(Gates.CNOT, matrix);
        System.out.println("Применяем оператор NOT");
        matrix = Core.scalarMultiplyMatrix(operatorMatrix, matrix);
        return matrix;
    }
}
