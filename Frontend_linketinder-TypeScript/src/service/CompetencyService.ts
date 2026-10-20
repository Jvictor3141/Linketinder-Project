import type { Candidato } from '../model/Perfis';

export interface ContagemCompetencia {
  rotulo: string;
  quantidade: number;
}

export function contarCandidatosPorCompetencia(
  candidatos: Pick<Candidato, 'competencias'>[],
): ContagemCompetencia[] {
  const contagem = new Map<string, ContagemCompetencia>();

  candidatos.forEach(candidato => {
    const competenciasUnicas = new Map<string, string>();
    candidato.competencias.forEach(competencia => {
      const normalizada = competencia.trim().toLocaleLowerCase();
      if (normalizada && !competenciasUnicas.has(normalizada)) {
        competenciasUnicas.set(normalizada, competencia.trim());
      }
    });

    competenciasUnicas.forEach((rotulo, chave) => {
      const atual = contagem.get(chave) ?? { rotulo, quantidade: 0 };
      atual.quantidade++;
      contagem.set(chave, atual);
    });
  });

  return [...contagem.values()].sort((a, b) => b.quantidade - a.quantidade);
}
