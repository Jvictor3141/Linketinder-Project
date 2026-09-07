package org.linketinder.data

import groovy.transform.Canonical
import org.linketinder.model.Candidato

@Canonical
class CandidatoData {
    static List<Candidato> candidatos = [
            new Candidato(
                    'João Silva',
                    'joao.silva@email.com',
                    '111.222.333-44',
                    25,
                    'SP',
                    '01001-000',
                    'Desenvolvedor Java com foco em back-end.',
                    ['Java', 'Spring', 'SQL']
            ),

            new Candidato(
                    'Maria Oliveira',
                    'maria.oliveira@email.com',
                    '222.333.444-55',
                    28,
                    'RJ',
                    '20010-000',
                    'Desenvolvedora front-end apaixonada por interfaces.',
                    ['JavaScript', 'React', 'HTML', 'CSS']
            ),

            new Candidato(
                    'Carlos Santos',
                    'carlos.santos@email.com',
                    '333.444.555-66',
                    23,
                    'MG',
                    '30110-000',
                    'Desenvolvedor full stack com experiência em aplicações web.',
                    ['Java', 'Spring', 'JavaScript', 'React']
            ),

            new Candidato(
                    'Ana Costa',
                    'ana.costa@email.com',
                    '444.555.666-77',
                    30,
                    'PR',
                    '80010-000',
                    'Desenvolvedora Python com experiência em APIs e dados.',
                    ['Python', 'Django', 'SQL', 'Git']
            ),

            new Candidato(
                    'Lucas Pereira',
                    'lucas.pereira@email.com',
                    '555.666.777-88',
                    26,
                    'SC',
                    '88010-000',
                    'Desenvolvedor front-end com experiência em aplicações modernas.',
                    ['JavaScript', 'Angular', 'TypeScript', 'HTML', 'CSS']
            )
    ]
}
