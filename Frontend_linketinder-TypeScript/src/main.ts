import { lerForm, esc, tags, hash, parseLista } from "./utils/utils";
import { usuarioAtual, login } from "./service/SectionService";
import type { Candidato, Empresa, Tipo, Vaga } from './model/Perfis';
import { listar } from "./repository/UserRepository";
import { emailExiste, criarConta } from "./service/AccountService";


const $ = <T extends HTMLElement>(sel: string) => document.querySelector(sel) as T;

const stage = $(".stage");
const selectScreen = $(".screen--select");
const loginScreen = $(".screen--login");
const roleLabel = $(".login-role");
const backLink = $(".back-link");
const cadLink = $("#cad-link");
const cadCandidato = $<HTMLFormElement>("#aba-cad-candidato");
const cadEmpresa = $<HTMLFormElement>("#aba-cad-empresa");

document.querySelectorAll<HTMLElement>(".role-card").forEach((card) => {
  card.addEventListener("click", () => {
    const role = card.dataset.role;
    if(role) {
      stage.dataset.screen = role;
      loginScreen.dataset.role = role;
      roleLabel.textContent = role;
      selectScreen.setAttribute("aria-hidden", "true");
      loginScreen.setAttribute("aria-hidden", "false");
    }
  });
});

backLink.addEventListener("click", () => {
  stage.dataset.screen = "select";
  selectScreen.setAttribute("aria-hidden", "false");
  loginScreen.setAttribute("aria-hidden", "true");
  $(".login-form").classList.add("ativa");
  cadCandidato.classList.remove("ativa");
  cadEmpresa.classList.remove("ativa");
});

$<HTMLFormElement>(".login-form").addEventListener('submit', async e => {
  e.preventDefault();
  const d = lerForm(e.target as HTMLFormElement);
  const tipo = roleLabel.textContent as Tipo;
  const h = await hash(d.senha);
  const achado = listar<Candidato | Empresa>(tipo).find(p => p.email === d.email.trim().toLowerCase() && p.senhaHash === h);
  if (!achado) return alert('E-mail ou senha inválidos.');
  login(tipo, achado.id);
});

cadLink.addEventListener('click', () => {
  $(".login-form").classList.toggle("ativa");

  if(roleLabel.textContent == "candidato") {
    cadCandidato.classList.toggle("ativa");
  } else if (roleLabel.textContent == "empresa") {
    cadEmpresa.classList.toggle("ativa");
  }
})

cadCandidato.addEventListener('submit', async e => {
  e.preventDefault();
  const f = e.target as HTMLFormElement, d = lerForm(f);
  if (emailExiste('candidato', d.email)) return alert('Já existe candidato com esse e-mail.');
  criarConta<Candidato>({
    id: crypto.randomUUID(), tipo: 'candidato', nome: d.nome, email: d.email.trim().toLowerCase(),
    senhaHash: await hash(d.senha), cpf: d.cpf, idade: Number(d.idade), estado: d.estado, cep: d.cep,
    descricao: d.descricao, formacao: d.formacao, competencias: parseLista(d.competencias),
  });
  f.reset();
});

cadEmpresa.addEventListener('submit', async e => {
  e.preventDefault();
  const f = e.target as HTMLFormElement, d = lerForm(f);
  if (emailExiste('empresa', d.email)) return alert('Já existe empresa com esse e-mail.');
  criarConta<Empresa>({
    id: crypto.randomUUID(), tipo: 'empresa', nome: d.nome, email: d.email.trim().toLowerCase(),
    senhaHash: await hash(d.senha), cnpj: d.cnpj, pais: d.pais, estado: d.estado, cep: d.cep, descricao: d.descricao,
  });
  f.reset();
});

function cardCandidato(candidato: Candidato, indice: number) {
  return `
    <article class="card card--candidato" data-id="${esc(candidato.id)}">
      <header class="card__header">
        <div class="card__avatar" aria-hidden="true">👤</div>
        <span class="card__badge">Candidato</span>
      </header>
      <div class="card__body">
        <h2 class="card__name">Candidato anônimo #${indice + 1}</h2>
        <p class="card__subtitle">${esc(candidato.formacao)}</p>
        <div class="card__section">
          <p class="card__label">Competências</p>
          <div class="tags">${tags(candidato.competencias)}</div>
        </div>
        <div class="card__section">
          <p class="card__label">Sobre</p>
          <p class="card__text">${esc(candidato.descricao)}</p>
        </div>
        <div class="card__section">
          <p class="card__label">Localização</p>
          <p class="card__text">${esc(candidato.estado)}</p>
        </div>
      </div>
      <footer class="card__actions">
        <button class="btn-action btn-action--dislike" data-action="dislike" aria-label="Descartar candidato">✕</button>
        <button class="btn-action btn-action--like" data-action="like" aria-label="Curtir candidato">♥</button>
      </footer>
    </article>`;
}

function cardVaga(vaga: Vaga, indice: number) {
  return `
    <article class="card card--vaga" data-id="${esc(vaga.id)}">
      <header class="card__header">
        <div class="card__avatar" aria-hidden="true">🏢</div>
        <span class="card__badge">Vaga</span>
      </header>
      <div class="card__body">
        <h2 class="card__name">${esc(vaga.titulo)}</h2>
        <p class="card__subtitle">Oportunidade #${indice + 1}</p>
        <div class="card__section">
          <p class="card__label">Descrição</p>
          <p class="card__text">${esc(vaga.descricao)}</p>
        </div>
        ${vaga.competencias.length ? `<div class="card__section"><p class="card__label">Competências</p><div class="tags">${tags(vaga.competencias)}</div></div>` : ''}
      </div>
      <footer class="card__actions">
        <button class="btn-action btn-action--dislike" data-action="dislike" aria-label="Descartar vaga">✕</button>
        <button class="btn-action btn-action--like" data-action="like" aria-label="Curtir vaga">♥</button>
      </footer>
    </article>`;
}

function renderPainelCandidato() {
  const vagas = listar<Vaga>('vaga');
  $('.feed-section').innerHTML = vagas.length
    ? vagas.map(cardVaga).join('')
    : '<p class="empty">Nenhuma vaga cadastrada ainda.</p>';
}

function renderPainelEmpresa() {
  const candidatos = listar<Candidato>('candidato');
  $('.feed-section').innerHTML = candidatos.length
    ? candidatos.map(cardCandidato).join('')
    : '<p class="empty">Nenhum candidato cadastrado ainda.</p>';
}

export function atualizarTela() {
  const u = usuarioAtual();
  stage.style.display = u ? 'none' : 'block';
  $('.user-logado').style.display = u ? 'flex' : 'none';
  $('#user_name').textContent = u ? u.nome : '';
  if (!u) return;
  if (u.tipo === 'candidato') renderPainelCandidato(); else renderPainelEmpresa();
}

atualizarTela()
