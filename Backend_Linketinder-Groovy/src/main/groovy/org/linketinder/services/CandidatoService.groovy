package org.linketinder.services

import org.linketinder.dao.ICandidatoDAO
import org.linketinder.model.Candidato

import java.time.LocalDate
import java.time.Period

class CandidatoService {

    private final ICandidatoDAO dao

    CandidatoService(ICandidatoDAO dao) {
        this.dao = dao
    }

    Candidato criaCandidato ( Candidato candidato ) {
        validarDados(candidato, false)

        dao.inserirCandidato(candidato)

        return candidato
    }

    List<Candidato> listaCandidatos() {
        return dao.listarCandidatos()
    }

    void deletaCandidato(Integer idCandidato) {
        ValidacaoService.validarIdPositivo('candidato', idCandidato)
        Candidato candidato = dao.buscaCandidatoPorId(idCandidato)
        if(!candidato) {
            throw new IllegalArgumentException('Candidato não encontrado.')
        }
        dao.deletaCandidato(idCandidato)
    }

    Candidato atualizaCandidato(Candidato candidato) {
        validarDados(candidato, true)
        dao.atualizaCandidato(candidato)
        return candidato
    }

    Candidato buscaCandidatoPorId(Integer idCandidato) {
        ValidacaoService.validarIdPositivo('candidato', idCandidato)
        Candidato candidato = dao.buscaCandidatoPorId(idCandidato)
        if (!candidato) {
            throw new IllegalArgumentException('Candidato não encontrado.')
        }
        return candidato
    }

    private static void validarDados(Candidato candidato, boolean exigeId) {
        if (candidato == null) {
            throw new IllegalArgumentException('Os dados do candidato são obrigatórios.')
        }
        if (exigeId) {
            ValidacaoService.validarIdPositivo('candidato', candidato.id)
        }

        ValidacaoService.validarCamposObrigatorios('candidato', [
                'nome'             : candidato.nome,
                'sobrenome'        : candidato.sobrenome,
                'e-mail'           : candidato.email,
                'CPF'              : candidato.cpf,
                'data de nascimento': candidato.dataNascimento,
                'estado'           : candidato.estado,
                'CEP'              : candidato.cep,
                'descrição'        : candidato.descricao
        ])
        ValidacaoService.validarCompetencias('candidato', candidato.competencias, !exigeId)

        if (!candidato.dataNascimento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException('A data de nascimento deve ser anterior à data de hoje.')
        }
        if (Period.between(candidato.dataNascimento, LocalDate.now()).getYears() < 18) {
            throw new IllegalArgumentException('O candidato deve ter 18 anos ou mais para se cadastrar.')
        }
    }
}
