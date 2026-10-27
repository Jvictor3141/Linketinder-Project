package org.linketinder.view

import spock.lang.Specification

class CompetenciasViewSpec extends Specification {
    def 'cadastro pede novamente a primeira competência vazia e coleta as seguintes'() {
        given:
        ByteArrayOutputStream saida = new ByteArrayOutputStream()
        ConsoleIO console = new ConsoleIO(new Scanner('  \nGroovy\n SQL \n\n'),
                new PrintStream(saida, true, 'UTF-8'))

        when:
        def competencias = new CompetenciasView(console).ler(true)

        then:
        competencias*.competencia == ['Groovy', 'SQL']
        saida.toString('UTF-8').contains('precisa digitar pelo menos uma competência')
    }

    def 'edição vazia indica que os vínculos atuais devem ser preservados'() {
        given:
        ConsoleIO console = new ConsoleIO(new Scanner('\n'), new PrintStream(new ByteArrayOutputStream()))

        expect:
        new CompetenciasView(console).ler(false) == null
    }
}
