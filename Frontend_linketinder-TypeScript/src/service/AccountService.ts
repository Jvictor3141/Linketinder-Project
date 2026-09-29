import type { Tipo } from "../model/Perfis";
import type { Candidato, Empresa, Vaga } from "../model/Perfis";
import { login,usuarioAtual, logout } from "./SectionService";
import { listar, chave, salvar, remover } from "../repository/UserRepository";
import { atualizarTela } from "../main";

// E-mail é a "identidade" de login: não pode repetir dentro do mesmo tipo
export const emailExiste = (tipo: Tipo, email: string) =>
  listar<Candidato | Empresa>(tipo).some(p => p.email === email.toLowerCase());

// Cria o perfil (uma chave nova no localStorage) e já inicia a sessão
export function criarConta<T extends Candidato | Empresa>(perfil: T) {
  salvar(chave(perfil.tipo, perfil.id), perfil);
  login(perfil.tipo, perfil.id);
}

// Exclui a conta logada; se for empresa, leva as vagas dela junto
export function excluirConta() {
  const u = usuarioAtual();
  if (!u || !confirm('Excluir sua conta definitivamente?')) return;
  if (u.tipo === 'empresa') listar<Vaga>('vaga').filter(v => v.empresaId === u.id).forEach(v => remover(chave('vaga', v.id)));
  remover(chave(u.tipo, u.id));
  logout();
}

export const criarVaga = (v: Omit<Vaga, 'id'>) => { const id = crypto.randomUUID(); salvar(chave('vaga', id), { id, ...v }); atualizarTela(); };
export const deletarVaga = (id: string) => { remover(chave('vaga', id)); atualizarTela(); };