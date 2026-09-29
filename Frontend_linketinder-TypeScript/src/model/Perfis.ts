export type Tipo = 'candidato' | 'empresa';

export interface Candidato {
  id: string; tipo: 'candidato'; nome: string; email: string; senhaHash: string;
  cpf: string; idade: number; estado: string; cep: string;
  descricao: string; formacao: string; competencias: string[];
}

export interface Empresa {
  id: string; tipo: 'empresa'; nome: string; email: string; senhaHash: string;
  cnpj: string; pais: string; estado: string; cep: string; descricao: string;
}
export interface Vaga {
  id: string; empresaId: string; titulo: string; descricao: string; competencias: string[];
}

export interface Sessao { tipo: Tipo; id: string; }
