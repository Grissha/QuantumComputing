import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;

public class Window extends JFrame{
    double[][] matrixQubit;
    double[][] operationMatrix;
    public Window (String s) {
        super (s);

        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new GridBagLayout());
        panelCenter.setPreferredSize(new Dimension(200, 200));
        //createPanelCenter(panelCenter, 0, 0);

        JPanel panelSouth = new JPanel();
        panelSouth.setPreferredSize(new Dimension(200, 150));
        createPanelSouth(panelSouth, panelCenter);

        JPanel panelNorth = new JPanel();
        panelNorth.setPreferredSize(new Dimension(200, 40));
        createPanelNorth(panelNorth, panelCenter);

        this.add(panelNorth, BorderLayout.NORTH);
        this.add(panelCenter, BorderLayout.CENTER);
        this.add(panelSouth, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(1000, 500);
        this.setVisible(true);
    }

    private void createPanelNorth(JPanel panelNorth, JPanel panelCenter) {
        JLabel numberQubitLabel = new JLabel("Кол-во кубит");
        JTextField numberQubitText = new JTextField("0", 2);

        JLabel numberOperationLabel = new JLabel("Кол-во операций");
        JTextField numberOperationText = new JTextField("0", 2);

        JButton createSchemeButton = new JButton("Создать");
        createSchemeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPanelCenter(panelCenter, Integer.parseInt(numberQubitText.getText()),
                        Integer.parseInt(numberOperationText.getText()));
            }
        });

        panelNorth.add(numberQubitLabel);
        panelNorth.add(numberQubitText);
        panelNorth.add(numberOperationLabel);
        panelNorth.add(numberOperationText);
        panelNorth.add(createSchemeButton);
    }

    private void createPanelCenter(JPanel panelCenter, int lines, int columns) {
        panelCenter.removeAll();
        GridBagConstraints constraints = new GridBagConstraints();

        String[] qubitItems = new String[2];
        qubitItems[0] = "|0>";
        qubitItems[1] = "|1>";
        for(int i = 0; i < lines; i++) {
            constraints.gridx = 0;
            constraints.gridy = i;
            JComboBox qubitBox = new JComboBox(qubitItems);
            panelCenter.add(qubitBox, constraints);
        }

        String[] operationItems = new String[5];
        operationItems[0] = "---";
        operationItems[1] = "NOT";
        operationItems[2] = "CNOT";
        operationItems[3] = "H";
        operationItems[4] = "*";
        for(int j = 1; j < columns + 1; j++) {
            for(int i = 0; i < lines; i++) {
                constraints.gridx = j;
                constraints.gridy = i;
                JComboBox operationBox = new JComboBox(operationItems);
                panelCenter.add(operationBox, constraints);
            }
        }

        for(int i = 0; i < lines; i++) {
            constraints.gridx = columns + 1;
            constraints.gridy = i;
            JComboBox qubitBox = new JComboBox(qubitItems);
            panelCenter.add(qubitBox, constraints);
        }
        panelCenter.revalidate();
        panelCenter.repaint();
    }

    private void createPanelSouth(JPanel panelSouth, JPanel panelCenter) {
        panelSouth.removeAll();
        JLabel matrixLabel = new JLabel("Матрица:");
        JTextArea matrixTextArea = new JTextArea(9, 20);
        int dim = 0;
        Vector<JComboBox> components = new Vector<>();

        if (panelCenter.getComponents().length != 0) {
            double[][] matrix = null;
            components.add((JComboBox) panelCenter.getComponent(0));
            if (components.get(0).isEnabled()) {
                for (int i = 1; i < panelCenter.getComponents().length; i++) {
                    components.add((JComboBox) panelCenter.getComponent(i));
                }

                for (int i = 0; i < components.size(); i++) {
                    String s = (String) components.get(i).getSelectedItem();
                    if (s.equals("|0>") || s.equals("|1>")) {
                        dim++;
                    } else {
                        break;
                    }
                }

                if (dim != 0) {
                    Qubit[] qubits = new Qubit[dim];
                    for (int i = 0; i < dim; i++) {
                        String s = (String) components.get(i).getSelectedItem();
                        if (s.charAt(1) == '0') {
                            qubits[i] = new Qubit(0);
                        } else {
                            qubits[i] = new Qubit(1);
                        }
                    }

                    matrix = Core.getMatrixFromQubit(qubits);
                }

                matrixQubit = matrix;

                if (matrix != null) {
                    for (int i = 0; i < matrix.length; i++) {
                        matrixTextArea.append(Double.toString(matrix[i][0]) + "\n");
                    }
                }

                for (int i = 0; i < dim; i++) {
                    components.get(i).setEnabled(false);
                }
            } else {
                for (int i = 1; i < panelCenter.getComponents().length; i++) {
                    components.add((JComboBox) panelCenter.getComponent(i));
                }

                dim = (int)(Math.log(matrixQubit.length)/Math.log(2));
                matrix = Core.scalarMultiplyMatrix(operationMatrix, matrixQubit);
                matrixQubit = matrix;

                if (matrix != null) {
                    for (int i = 0; i < matrix.length; i++) {
                        matrixTextArea.append(Double.toString(matrix[i][0]) + "\n");
                    }
                }
            }
        }

        JButton nextStepButton = new JButton("Следующий шаг");
        nextStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createPanelSouth(panelSouth, panelCenter);
            }
        });

        JLabel matrixOperationLabel = new JLabel("Оператор:");
        JTextArea matrixOperationTextArea = new JTextArea(9, 20);

        if(dim != 0) {
            int numberOperation = 0;
            for(int i = components.size() - dim; i >= 0; i-=dim) {
                if (!components.get(i).isEnabled()) {
                    numberOperation = i + dim;
                    break;
                }
            }

            if (!components.get(components.size()-1).isEnabled()) {
                panelSouth.add(matrixLabel);
                panelSouth.add(new JScrollPane(matrixTextArea));
                panelSouth.add(matrixOperationLabel);
                panelSouth.add(new JScrollPane(matrixOperationTextArea));
                panelSouth.add(nextStepButton);

                panelSouth.revalidate();
                panelSouth.repaint();
                return;
            }
            if (numberOperation == components.size() - dim) {
                Qubit[] qubits = Core.getQubitsFromMatrix(matrixQubit);
                int j = 0;
                if(qubits != null) {
                    for (int i = numberOperation; i < numberOperation + dim; i++) {
                        components.get(i).setSelectedIndex((int)qubits[j].getY());
                        j++;
                    }
                } else {
                    for (int i = numberOperation; i < numberOperation + dim; i++) {
                        components.get(i).setSelectedItem("---");
                    }
                }

                for (int i = numberOperation; i < numberOperation + dim; i++) {
                    components.get(i).setEnabled(false);
                }

                panelSouth.add(matrixLabel);
                panelSouth.add(new JScrollPane(matrixTextArea));
                panelSouth.add(matrixOperationLabel);
                panelSouth.add(new JScrollPane(matrixOperationTextArea));
                panelSouth.add(nextStepButton);

                panelSouth.revalidate();
                panelSouth.repaint();
                return;
            }

            double[][] matrixOperation = {{1}};
            int flag = -1;
            double[][] newMatrixOperation = {{1}};
            for(int i = numberOperation; i < numberOperation + dim; i++) {
                switch ((String)components.get(i).getSelectedItem()) {
                    case "---":
                        matrixOperation = Gates.IDENTITY_MATRIX;
                        break;
                    case "NOT":
                        matrixOperation = Gates.NOT;
                        break;
                    case "CNOT":
                        if (flag != -1) {
                            matrixOperation = Gates.makeCNOTMatrix(0, i-flag, i-flag+1);
                            flag = -1;
                        } else {
                            flag = i;
                        }
                        break;
                    case "H":
                        matrixOperation = Gates.ADAMARA;
                        break;
                    case "*":
                        if (flag != -1) {
                            matrixOperation = Gates.makeCNOTMatrix(i-flag, 0, i-flag+1);
                            flag = -1;
                        } else {
                            flag = i;
                        }
                        break;
                }

                if(flag == -1) {
                    newMatrixOperation = Core.tenzorialMultiplyMatrix(newMatrixOperation, matrixOperation);
                }
                components.get(i).setEnabled(false);
            }

//            for (int i = 1; i < matrixOperation.length; i++) {
//                newMatrixOperation = Core.tenzorialMultiplyMatrix(newMatrixOperation, matrixOperation[i]);
//            }

            operationMatrix = newMatrixOperation;

            for (int i = 0; i < newMatrixOperation.length; i++) {
                for (int j = 0; j < newMatrixOperation[i].length; j++) {
                    matrixOperationTextArea.append(Double.toString(newMatrixOperation[i][j]) + " ");
                }
                matrixOperationTextArea.append("\n");
            }
        }

        panelSouth.add(matrixLabel);
        panelSouth.add(new JScrollPane(matrixTextArea));
        panelSouth.add(matrixOperationLabel);
        panelSouth.add(new JScrollPane(matrixOperationTextArea));
        panelSouth.add(nextStepButton);

        panelSouth.revalidate();
        panelSouth.repaint();
    }

    public static void main(String[] args) {
        JFrame window = new Window("Эмулятор квантовых вычислений");
    }
}
