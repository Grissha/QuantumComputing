public class Gates {
    public static double[][] IDENTITY_MATRIX = {{1, 0}, {0, 1}};
    public static double[][] NOT = {{0, 1}, {1, 0}};
    public static double[][] CNOT = {{1, 0, 0, 0}, {0, 1, 0, 0}, {0, 0, 0, 1}, {0, 0, 1, 0}};
    public static double[][] ADAMARA = {{1/Math.sqrt(2), 1/Math.sqrt(2)}, {1/Math.sqrt(2), -1/Math.sqrt(2)}};

    public static double[][] makeCNOTMatrix (int control, int change, int dim) {
        double[][] matrixCNOT = new double[(int)Math.pow(2, dim)][(int)Math.pow(2, dim)];
        int range = 0;
        Qubit[] qubits = new Qubit[dim];
        for(int i = 0; i < qubits.length; i++) {
            qubits[i] = new Qubit(0);
        }

        for(int i = 0; i < matrixCNOT.length; i++) {
            double[][] matrixQubit = Core.getMatrixFromQubit(qubits);
            for (int j = 0; j < matrixQubit.length; j++) {
                matrixCNOT[j][i] = matrixQubit[j][0];
            }

            range++;
            String s = Integer.toBinaryString(range);
            for(int j = 0; j < qubits.length; j++) {
                if (qubits.length - j > s.length()) {
                    qubits[j] = new Qubit(0);
                } else {
                    if (s.charAt(j - qubits.length + s.length()) == '0') {
                        qubits[j] = new Qubit(0);
                    } else {
                        qubits[j] = new Qubit(1);
                    }
                }
            }

            if (qubits[control].getY() == 1) {
                if (qubits[change].getY() == 1) {
                    qubits[change] = new Qubit(0);
                    } else {
                    qubits[change] = new Qubit(1);
                }
            }
        }

        Core.printMatrix(matrixCNOT);
        return matrixCNOT;
    }
}
