import type { Candidato, Empresa, Sessao, Tipo } from "../model/Perfis";
import { CHAVE_SESSAO, chave, ler, remover, salvar } from "../repository/UserRepository";

export function usuarioAtual(): Candidato | Empresa | null {
  const s = ler<Sessao>(CHAVE_SESSAO);
  if (!s) return null;
  const perfil = ler<Candidato | Empresa>(chave(s.tipo, s.id));
  if (!perfil) remover(CHAVE_SESSAO);
  return perfil;
}

export const login = (tipo: Tipo, id: string) => {
  salvar(CHAVE_SESSAO, { tipo, id } satisfies Sessao);
};

export const logout = () => {
  remover(CHAVE_SESSAO);
};
