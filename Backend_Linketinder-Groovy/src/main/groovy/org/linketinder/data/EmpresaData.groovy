package org.linketinder.data

import groovy.transform.Canonical
import org.linketinder.model.Candidato
import org.linketinder.model.Empresa

@Canonical
class EmpresaData {
    static List<Candidato> empresas = [
            new Empresa(
                    'Tech Solutions',
                    'rh@techsolutions.com',
                    '12.345.678/0001-90',
                    'Brasil',
                    'SP',
                    '01310-000',
                    'Empresa especializada em desenvolvimento de sistemas.',
                    ['Java', 'Spring', 'SQL']
            ),

            new Empresa(
                    'WebDev Brasil',
                    'rh@webdev.com',
                    '23.456.789/0001-01',
                    'Brasil',
                    'RJ',
                    '22010-000',
                    'Empresa focada no desenvolvimento de aplicações web.',
                    ['JavaScript', 'React', 'HTML', 'CSS']
            ),

            new Empresa(
                    'CodeFactory',
                    'talentos@codefactory.com',
                    '34.567.890/0001-12',
                    'Brasil',
                    'MG',
                    '30120-000',
                    'Software house especializada em soluções corporativas.',
                    ['Java', 'Spring', 'React', 'SQL']
            ),

            new Empresa(
                    'DataTech',
                    'rh@datatech.com',
                    '45.678.901/0001-23',
                    'Brasil',
                    'PR',
                    '80220-000',
                    'Empresa especializada em dados, automação e inteligência de negócios.',
                    ['Python', 'Django', 'SQL', 'Git']
            ),

            new Empresa(
                    'Digital Innovation',
                    'careers@digitalinnovation.com',
                    '56.789.012/0001-34',
                    'Brasil',
                    'SC',
                    '88020-000',
                    'Empresa de tecnologia focada em produtos digitais.',
                    ['Angular', 'TypeScript', 'JavaScript', 'HTML', 'CSS']
            )
    ]
}
