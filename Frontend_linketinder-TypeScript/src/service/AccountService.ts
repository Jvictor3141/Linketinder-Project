import type { Tipo } from "../model/Perfis";
import type { Candidato, Empresa, Vaga } from "../model/Perfis";
import { login, usuarioAtual, logout } from "./SectionService";
import { listar, chave, salvar, remover } from "../repository/UserRepository";

export const emailExiste = (tipo: Tipo, email: string) =>
  listar<Candidato | Empresa>(tipo).some(p => p.email === email.toLowerCase());

export function criarConta<T extends Candidato | Empresa>(perfil: T) {
  salvar(chave(perfil.tipo, perfil.id), perfil);
  login(perfil.tipo, perfil.id);
}

/** Executa a exclusão imediatamente; a interface deve obter a confirmação antes de chamar. */
export function excluirConta(): boolean {
  const u = usuarioAtual();
  if (!u) return false;
  if (u.tipo === 'empresa') listar<Vaga>('vaga').filter(v => v.empresaId === u.id).forEach(v => remover(chave('vaga', v.id)));
  remover(chave(u.tipo, u.id));
  logout();
  return true;
}

export const criarVaga = (v: Omit<Vaga, 'id'>) => {
  const id = crypto.randomUUID();
  salvar(chave('vaga', id), { id, ...v });
};

export const deletarVaga = (id: string) => {
  remover(chave('vaga', id));
};
