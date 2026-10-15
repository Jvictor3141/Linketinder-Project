package org.linketinder.dao

import org.linketinder.model.Vaga

interface IVagaDAO {
    List<Vaga> listarVagas()
    Vaga inserirVaga(Vaga vaga)
    void deletaVaga(Integer idVaga)
    Vaga buscaPorIdVaga(Integer idVaga)
    Vaga atualizaVaga(Vaga vaga)

}