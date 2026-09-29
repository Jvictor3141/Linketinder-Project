import type { Tipo } from "../model/Perfis";

const PREFIXO = 'linketinder:';
export const CHAVE_SESSAO = `${PREFIXO}sessao`;
export const chave = (kind: Tipo | 'vaga', id: string) => `${PREFIXO}${kind}:${id}`;

export const salvar = (k: string, valor: unknown) => localStorage.setItem(k, JSON.stringify(valor));
export const remover = (k: string) => localStorage.removeItem(k);

// Lê e converte uma chave; devolve null se não existir ou se o JSON estiver corrompido
export function ler<T>(k: string): T | null {
  try { return JSON.parse(localStorage.getItem(k) ?? 'null') as T | null; } catch { return null; }
}

// Percorre TODAS as chaves do navegador e devolve as que começam com o prefixo (ex: todos os candidatos)
export function listar<T>(kind: Tipo | 'vaga'): T[] {
  const inicio = `${PREFIXO}${kind}:`;
  const itens: T[] = [];
  for (let i = 0; i < localStorage.length; i++) {
    const k = localStorage.key(i)!;
    if (k.startsWith(inicio)) { const v = ler<T>(k); if (v) itens.push(v); }
  }
  return itens;
}