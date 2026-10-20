import type { DadosCandidato, DadosEmpresa, DadosVaga } from '../model/DadosPerfil';
import { ErroAplicacao } from '../model/ErroAplicacao';
import { parseLista } from '../utils/utils';

export const lerForm = (form: HTMLFormElement) =>
  Object.fromEntries(new FormData(form)) as Record<string, string>;

export function lerDadosCandidato(form: HTMLFormElement): DadosCandidato {
  const dados = lerForm(form);
  return {
    nome: dados.nome, email: dados.email, senha: dados.senha,
    cpf: dados.cpf, idade: Number(dados.idade), estado: dados.estado, cep: dados.cep,
    formacao: dados.formacao, competencias: parseLista(dados.competencias),
    descricao: dados.descricao,
  };
}

export function lerDadosEmpresa(form: HTMLFormElement): DadosEmpresa {
  const dados = lerForm(form);
  return {
    nome: dados.nome, email: dados.email, senha: dados.senha,
    cnpj: dados.cnpj, pais: dados.pais, estado: dados.estado, cep: dados.cep,
    descricao: dados.descricao,
  };
}

export function lerDadosVaga(form: HTMLFormElement): DadosVaga {
  const dados = lerForm(form);
  return {
    titulo: dados.titulo, descricao: dados.descricao,
    competencias: parseLista(dados.competencias),
  };
}

export function mostrarErro(erro: unknown): void {
  if (erro instanceof ErroAplicacao) {
    alert(erro.message);
    return;
  }
  console.error(erro);
  alert('Não foi possível concluir a operação. Tente novamente.');
}

export function aoEnviarFormulario(
  form: HTMLFormElement,
  acao: (form: HTMLFormElement) => void | Promise<void>,
): void {
  let enviando = false;

  form.addEventListener('submit', async event => {
    event.preventDefault();
    if (enviando) return;

    enviando = true;
    const botoes = [...form.querySelectorAll<HTMLButtonElement>('button[type="submit"]')];
    const desabilitados = botoes.map(botao => botao.disabled);
    botoes.forEach(botao => { botao.disabled = true; });

    try {
      await acao(form);
    } catch (erro) {
      mostrarErro(erro);
    } finally {
      enviando = false;
      botoes.forEach((botao, indice) => { botao.disabled = desabilitados[indice]; });
    }
  });
}
