package org.linketinder.ui

import org.linketinder.model.Competencia

class CompetenciasView {
    private final ConsoleIO console

    CompetenciasView(ConsoleIO console) {
        this.console = console
    }

    List<Competencia> ler(boolean obrigatorias) {
        String nome = console.lerTexto(obrigatorias
                ? 'Digite uma competência: '
                : 'Digite novas competências (ou deixe vazio para manter as atuais): ')

        while (obrigatorias && nome.isEmpty()) {
            nome = console.lerTexto('Aviso: precisa digitar pelo menos uma competência: ')
        }
        if (nome.isEmpty()) {
            return null
        }

        List<Competencia> competencias = []
        while (!nome.isEmpty()) {
            competencias << new Competencia(competencia: nome)
            nome = console.lerTexto('Digite outra competência (ou deixe vazio para finalizar): ')
        }
        return competencias
    }
}
