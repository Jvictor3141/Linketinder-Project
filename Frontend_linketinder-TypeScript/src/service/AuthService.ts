import { ErroAplicacao } from "../model/ErroAplicacao";
import type { Candidato, Empresa, Tipo } from "../model/Perfis";
import { listar } from "../repository/UserRepository";
import { hash } from "../utils/utils";
import { login } from "./SectionService";

export async function autenticar(tipo: Tipo, email: string, senha: string): Promise<Candidato | Empresa> {
  const emailNormalizado = email.trim().toLowerCase();
  const senhaHash = await hash(senha);
  const usuario = listar<Candidato | Empresa>(tipo).find(perfil =>
    perfil.email.trim().toLowerCase() === emailNormalizado && perfil.senhaHash === senhaHash,
  );
  if (!usuario) throw new ErroAplicacao('E-mail ou senha inválidos.');

  login(tipo, usuario.id);
  return usuario;
}
