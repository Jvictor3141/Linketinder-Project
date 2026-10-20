import type { DadosCandidato, DadosEmpresa } from "../model/DadosPerfil";
import { ErroAplicacao } from "../model/ErroAplicacao";
import type { Candidato, Empresa, Tipo } from "../model/Perfis";
import { chave, listar, salvar } from "../repository/UserRepository";
import { hash } from "../utils/utils";
import { usuarioAtual } from "./SectionService";

export const listarCandidatos = (): Candidato[] => listar<Candidato>('candidato');

export function emailExiste(tipo: Tipo, email: string, idIgnorado?: string): boolean {
  const emailNormalizado = email.trim().toLowerCase();
  return listar<Candidato | Empresa>(tipo).some(perfil =>
    perfil.id !== idIgnorado && perfil.email.trim().toLowerCase() === emailNormalizado,
  );
}

export async function atualizarCandidato(dados: DadosCandidato): Promise<Candidato> {
  const usuario = usuarioAtual();
  if (!usuario || usuario.tipo !== 'candidato') {
    throw new ErroAplicacao('É necessário entrar como candidato para atualizar o perfil.');
  }

  const senhaHash = dados.senha ? await hash(dados.senha) : usuario.senhaHash;
  const atual = usuarioAtual();
  if (!atual || atual.tipo !== 'candidato' || atual.id !== usuario.id) {
    throw new ErroAplicacao('A sessão foi alterada. Entre novamente para atualizar o perfil.');
  }
  const email = dados.email.trim().toLowerCase();
  if (emailExiste('candidato', email, atual.id)) {
    throw new ErroAplicacao('Já existe candidato com esse e-mail.');
  }

  const candidato: Candidato = {
    id: atual.id, tipo: 'candidato', nome: dados.nome, email, senhaHash,
    cpf: dados.cpf, idade: dados.idade, estado: dados.estado, cep: dados.cep,
    descricao: dados.descricao, formacao: dados.formacao, competencias: [...dados.competencias],
  };
  salvar(chave('candidato', candidato.id), candidato);
  return candidato;
}

export async function atualizarEmpresa(dados: DadosEmpresa): Promise<Empresa> {
  const usuario = usuarioAtual();
  if (!usuario || usuario.tipo !== 'empresa') {
    throw new ErroAplicacao('É necessário entrar como empresa para atualizar o perfil.');
  }

  const senhaHash = dados.senha ? await hash(dados.senha) : usuario.senhaHash;
  const atual = usuarioAtual();
  if (!atual || atual.tipo !== 'empresa' || atual.id !== usuario.id) {
    throw new ErroAplicacao('A sessão foi alterada. Entre novamente para atualizar o perfil.');
  }
  const email = dados.email.trim().toLowerCase();
  if (emailExiste('empresa', email, atual.id)) {
    throw new ErroAplicacao('Já existe empresa com esse e-mail.');
  }

  const empresa: Empresa = {
    id: atual.id, tipo: 'empresa', nome: dados.nome, email, senhaHash,
    cnpj: dados.cnpj, pais: dados.pais, estado: dados.estado, cep: dados.cep,
    descricao: dados.descricao,
  };
  salvar(chave('empresa', empresa.id), empresa);
  return empresa;
}
