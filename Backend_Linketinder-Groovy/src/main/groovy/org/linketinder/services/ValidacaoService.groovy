package org.linketinder.services

import org.linketinder.model.Competencia

/** Shared input checks used by the application services before persistence. */
class ValidacaoService {

    private ValidacaoService() {}

    static void validarCamposObrigatorios(String entidade, Map<String, Object> campos) {
        List<String> ausentes = campos.findAll { String nome, Object valor ->
            valor == null || (valor instanceof CharSequence && valor.toString().trim().isEmpty())
        }.keySet().toList()

        if (ausentes) {
            throw new IllegalArgumentException("Campos obrigatórios de ${entidade}: ${ausentes.join(', ')}.")
        }
    }

    static void validarCompetencias(String entidade,
                                    List<Competencia> competencias,
                                    boolean obrigatorias = true) {
        if (competencias == null || competencias.isEmpty()) {
            if (obrigatorias) {
                throw new IllegalArgumentException("Informe ao menos uma competência válida para ${entidade}.")
            }
            return
        }

        boolean invalidas = competencias.any { Competencia competencia ->
            competencia == null || !competencia.competencia || competencia.competencia.trim().isEmpty()
        }

        if (invalidas) {
            throw new IllegalArgumentException("Informe ao menos uma competência válida para ${entidade}.")
        }
    }

    static void validarIdPositivo(String entidade, Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("O ID de ${entidade} deve ser um número positivo.")
        }
    }
}
