package org.linketinder.ui

class ConsoleIO {
    private final Scanner entrada
    private final PrintStream saida

    ConsoleIO(Scanner entrada, PrintStream saida) {
        this.entrada = entrada
        this.saida = saida
    }

    String lerTexto(String mensagem) {
        saida.print(mensagem)
        saida.flush()
        if (!temEntrada()) {
            throw new EOFException('Entrada encerrada.')
        }
        return entrada.nextLine().trim()
    }

    Integer lerInteiro(String mensagem) {
        return Integer.parseInt(lerTexto(mensagem))
    }

    boolean temEntrada() {
        return entrada.hasNextLine()
    }

    void exibir(String mensagem) {
        saida.println(mensagem)
    }
}
