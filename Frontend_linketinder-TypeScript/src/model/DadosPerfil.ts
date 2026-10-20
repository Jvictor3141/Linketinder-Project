import type { Candidato, Empresa, Vaga } from './Perfis';

export type DadosCandidato = Omit<Candidato, 'id' | 'tipo' | 'senhaHash'> & {
  senha: string;
};

export type DadosEmpresa = Omit<Empresa, 'id' | 'tipo' | 'senhaHash'> & {
  senha: string;
};

export type DadosVaga = Pick<Vaga, 'titulo' | 'descricao' | 'competencias'>;
