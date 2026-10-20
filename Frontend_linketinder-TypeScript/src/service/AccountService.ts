import type { DadosCandidato, DadosEmpresa } from "../model/DadosPerfil";
import { ErroAplicacao } from "../model/ErroAplicacao";
import type { Candidato, Empresa } from "../model/Perfis";
import { login, usuarioAtual, logout } from "./SectionService";
import { chave, salvar, remover } from "../repository/UserRepository";
import { hash } from "../utils/utils";
import { emailExiste } from "./ProfileService";
import { excluirVagasDaEmpresa } from "./VacancyService";

export async function cadastrarCandidato(dados: DadosCandidato): Promise<Candidato> {
  const email = dados.email.trim().toLowerCase();
  const senhaHash = await hash(dados.senha);
  if (emailExiste('candidato', email)) {
    throw new ErroAplicacao('Já existe candidato com esse e-mail.');
  }

  const candidato: Candidato = {
    id: crypto.randomUUID(), tipo: 'candidato', nome: dados.nome, email, senhaHash,
    cpf: dados.cpf, idade: dados.idade, estado: dados.estado, cep: dados.cep,
    descricao: dados.descricao, formacao: dados.formacao, competencias: [...dados.competencias],
  };
  salvar(chave('candidato', candidato.id), candidato);
  login('candidato', candidato.id);
  return candidato;
}

export async function cadastrarEmpresa(dados: DadosEmpresa): Promise<Empresa> {
  const email = dados.email.trim().toLowerCase();
  const senhaHash = await hash(dados.senha);
  if (emailExiste('empresa', email)) {
    throw new ErroAplicacao('Já existe empresa com esse e-mail.');
  }

  const empresa: Empresa = {
    id: crypto.randomUUID(), tipo: 'empresa', nome: dados.nome, email, senhaHash,
    cnpj: dados.cnpj, pais: dados.pais, estado: dados.estado, cep: dados.cep,
    descricao: dados.descricao,
  };
  salvar(chave('empresa', empresa.id), empresa);
  login('empresa', empresa.id);
  return empresa;
}

export function excluirConta(): boolean {
  const u = usuarioAtual();
  if (!u) return false;
  if (u.tipo === 'empresa') excluirVagasDaEmpresa(u.id);
  remover(chave(u.tipo, u.id));
  logout();
  return true;
}
