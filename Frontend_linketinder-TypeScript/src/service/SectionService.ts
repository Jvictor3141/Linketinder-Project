import type { Candidato, Empresa, Sessao, Tipo } from "../model/Perfis";
import { ler } from "../repository/UserRepository"
import { CHAVE_SESSAO, chave, remover, salvar } from "../repository/UserRepository";
import { atualizarTela } from "../main";

export function usuarioAtual(): Candidato | Empresa | null {
  const s = ler<Sessao>(CHAVE_SESSAO);
  if (!s) return null;
  const perfil = ler<Candidato | Empresa>(chave(s.tipo, s.id));
  if (!perfil) remover(CHAVE_SESSAO);
  return perfil;
}

export const login = (tipo: Tipo, id: string) => { salvar(CHAVE_SESSAO, { tipo, id } satisfies Sessao); atualizarTela(); };
export const logout = () => { remover(CHAVE_SESSAO); atualizarTela(); };
