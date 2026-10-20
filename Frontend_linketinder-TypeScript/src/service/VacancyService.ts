import type { DadosVaga } from "../model/DadosPerfil";
import { ErroAplicacao } from "../model/ErroAplicacao";
import type { Empresa, Vaga } from "../model/Perfis";
import { chave, listar, remover, salvar } from "../repository/UserRepository";
import { usuarioAtual } from "./SectionService";

function empresaAtual(): Empresa {
  const usuario = usuarioAtual();
  if (!usuario || usuario.tipo !== 'empresa') {
    throw new ErroAplicacao('É necessário entrar como empresa para gerenciar vagas.');
  }
  return usuario;
}

export const listarVagas = (): Vaga[] => listar<Vaga>('vaga');

export const listarVagasDaEmpresa = (empresaId: string): Vaga[] =>
  listarVagas().filter(vaga => vaga.empresaId === empresaId);

export function criarVaga(dados: DadosVaga): Vaga {
  const empresa = empresaAtual();
  const vaga: Vaga = {
    id: crypto.randomUUID(), empresaId: empresa.id, titulo: dados.titulo,
    descricao: dados.descricao, competencias: [...dados.competencias],
  };
  salvar(chave('vaga', vaga.id), vaga);
  return vaga;
}

export function deletarVaga(id: string): boolean {
  const empresa = empresaAtual();
  const vaga = listarVagasDaEmpresa(empresa.id).find(item => item.id === id);
  if (!vaga) return false;
  remover(chave('vaga', vaga.id));
  return true;
}

export function excluirVagasDaEmpresa(empresaId: string): void {
  listarVagasDaEmpresa(empresaId).forEach(vaga => remover(chave('vaga', vaga.id)));
}
