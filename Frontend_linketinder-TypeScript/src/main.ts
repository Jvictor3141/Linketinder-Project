import { esc, tags } from "./utils/utils";
import { usuarioAtual, logout } from "./service/SectionService";
import Chart from 'chart.js/auto';
import type { Candidato, Empresa, Tipo, Vaga } from './model/Perfis';
import { cadastrarCandidato, cadastrarEmpresa } from "./service/AccountService";
import { autenticar } from "./service/AuthService";
import { atualizarCandidato, atualizarEmpresa, listarCandidatos } from "./service/ProfileService";
import { criarVaga, deletarVaga, listarVagas, listarVagasDaEmpresa } from "./service/VacancyService";
import { contarCandidatosPorCompetencia } from "./service/CompetencyService";
import { aoEnviarFormulario, lerDadosCandidato, lerDadosEmpresa, lerDadosVaga, lerForm, mostrarErro } from "./ui/formularios";

const $ = <T extends HTMLElement>(sel: string) => document.querySelector(sel) as T;

const stage = $(".stage");
const selectScreen = $(".screen--select");
const loginScreen = $(".screen--login");
const loginForm = $<HTMLFormElement>(".login-form");
const roleLabel = $(".login-role");
const backLink = $(".back-link");
const cadLink = $("#cad-link");
const cadCandidato = $<HTMLFormElement>("#aba-cad-candidato");
const cadEmpresa = $<HTMLFormElement>("#aba-cad-empresa");
const profileButton = $<HTMLButtonElement>("#profile-button");
const logoutButton = $<HTMLButtonElement>("#logout-button");
const feedButton = $<HTMLButtonElement>("#feed-button");
const profileScreen = $("#profile-screen");
const feedSection = $(".feed-section");
const candidateProfileForm = $<HTMLFormElement>("#candidate-profile-form");
const companyProfileForm = $<HTMLFormElement>("#company-profile-form");
const vacancyForm = $<HTMLFormElement>("#vacancy-form");
const companyVacancies = $("#company-vacancies");
let profileOpen = false;
let loginType: Tipo | null = null;
let feedType: Tipo | null = null;
let feedCards: string[] = [];
let feedPosition = 0;
let graficoCompetencias: Chart | null = null;

function renderGrafico(candidatos: Candidato[]) {
  const dados = contarCandidatosPorCompetencia(candidatos);
  const canvas = document.querySelector<HTMLCanvasElement>('#grafico-competencias');
  if (!canvas) return;

  graficoCompetencias?.destroy();
  graficoCompetencias = null;
  const vazio = $('.chart-empty');
  const container = $('.competency-chart-container');
  if (dados.length === 0) {
    vazio.hidden = false;
    container.hidden = true;
    return;
  }
  vazio.hidden = true;
  container.hidden = false;

  graficoCompetencias = new Chart(canvas, {
    type: 'bar',
    data: {
      labels: dados.map(item => item.rotulo),
      datasets: [{
        label: 'Candidatos',
        data: dados.map(item => item.quantidade),
        backgroundColor: '#4fa69c',
        borderRadius: 6,
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } },
      plugins: {
        legend: { display: false },
        tooltip: { callbacks: { label: context => `${context.parsed.y} candidato(s)` } },
      },
    },
  });
}

function setField(form: HTMLFormElement, name: string, value: string) {
  const field = form.elements.namedItem(name) as HTMLInputElement | HTMLTextAreaElement | null;
  if (field) field.value = value;
}

function renderCompanyVacancies(empresa: Empresa) {
  const vagas = listarVagasDaEmpresa(empresa.id);
  companyVacancies.innerHTML = vagas.length
    ? vagas.map(vaga => `
      <article class="company-vacancy">
        <div><strong>${esc(vaga.titulo)}</strong><p>${esc(vaga.descricao)}</p>${tags(vaga.competencias)}</div>
        <button class="danger" type="button" data-delete-vacancy="${esc(vaga.id)}">Excluir</button>
      </article>`).join('')
    : '<p class="empty">Você ainda não cadastrou vagas.</p>';
}

function renderPerfil(usuario: Candidato | Empresa) {
  candidateProfileForm.hidden = usuario.tipo !== 'candidato';
  companyProfileForm.hidden = usuario.tipo !== 'empresa';
  $('.competency-chart-section').hidden = usuario.tipo !== 'empresa';
  $("#company-vacancy-management").hidden = usuario.tipo !== 'empresa';

  if (usuario.tipo === 'candidato') {
    graficoCompetencias?.destroy();
    graficoCompetencias = null;
    setField(candidateProfileForm, 'nome', usuario.nome);
    setField(candidateProfileForm, 'email', usuario.email);
    setField(candidateProfileForm, 'senha', '');
    setField(candidateProfileForm, 'cpf', usuario.cpf);
    setField(candidateProfileForm, 'idade', String(usuario.idade));
    setField(candidateProfileForm, 'estado', usuario.estado);
    setField(candidateProfileForm, 'cep', usuario.cep);
    setField(candidateProfileForm, 'formacao', usuario.formacao);
    setField(candidateProfileForm, 'competencias', usuario.competencias.join(', '));
    setField(candidateProfileForm, 'descricao', usuario.descricao);
    return;
  }

  setField(companyProfileForm, 'nome', usuario.nome);
  setField(companyProfileForm, 'email', usuario.email);
  setField(companyProfileForm, 'senha', '');
  setField(companyProfileForm, 'cnpj', usuario.cnpj);
  setField(companyProfileForm, 'pais', usuario.pais);
  setField(companyProfileForm, 'estado', usuario.estado);
  setField(companyProfileForm, 'cep', usuario.cep);
  setField(companyProfileForm, 'descricao', usuario.descricao);
  renderCompanyVacancies(usuario);
  renderGrafico(listarCandidatos());
}

profileButton.addEventListener('click', () => {
  profileOpen = true;
  atualizarTela();
});

logoutButton.addEventListener('click', () => {
  logout();
  atualizarTela();
});

feedButton.addEventListener('click', () => {
  profileOpen = false;
  atualizarTela();
});

aoEnviarFormulario(candidateProfileForm, async form => {
  await atualizarCandidato(lerDadosCandidato(form));
  atualizarTela();
});

aoEnviarFormulario(companyProfileForm, async form => {
  await atualizarEmpresa(lerDadosEmpresa(form));
  atualizarTela();
});

aoEnviarFormulario(vacancyForm, form => {
  criarVaga(lerDadosVaga(form));
  form.reset();
  atualizarTela();
});

companyVacancies.addEventListener('click', event => {
  const button = (event.target as HTMLElement).closest<HTMLButtonElement>('[data-delete-vacancy]');
  const id = button?.dataset.deleteVacancy;
  if (!id) return;

  try {
    if (deletarVaga(id)) atualizarTela();
  } catch (erro) {
    mostrarErro(erro);
  }
});

document.querySelectorAll<HTMLElement>(".role-card").forEach((card) => {
  card.addEventListener("click", () => {
    const role = card.dataset.role;
    if (role === 'candidato' || role === 'empresa') {
      loginType = role;
      stage.dataset.screen = role;
      loginScreen.dataset.role = role;
      roleLabel.textContent = role;
      selectScreen.setAttribute("aria-hidden", "true");
      loginScreen.setAttribute("aria-hidden", "false");
    }
  });
});

backLink.addEventListener("click", () => {
  loginType = null;
  stage.dataset.screen = "select";
  selectScreen.setAttribute("aria-hidden", "false");
  loginScreen.setAttribute("aria-hidden", "true");
  loginForm.classList.add("ativa");
  cadCandidato.classList.remove("ativa");
  cadEmpresa.classList.remove("ativa");
});

aoEnviarFormulario(loginForm, async form => {
  if (!loginType) return;
  const dados = lerForm(form);
  await autenticar(loginType, dados.email, dados.senha);
  form.reset();
  atualizarTela();
});

cadLink.addEventListener('click', event => {
  event.preventDefault();
  loginForm.classList.toggle("ativa");

  if (loginType === 'candidato') {
    cadCandidato.classList.toggle("ativa");
  } else if (loginType === 'empresa') {
    cadEmpresa.classList.toggle("ativa");
  }
})

aoEnviarFormulario(cadCandidato, async form => {
  await cadastrarCandidato(lerDadosCandidato(form));
  form.reset();
  atualizarTela();
});

aoEnviarFormulario(cadEmpresa, async form => {
  await cadastrarEmpresa(lerDadosEmpresa(form));
  form.reset();
  atualizarTela();
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

function renderCurrentCard() {
  if (feedCards.length === 0) {
    feedSection.innerHTML = `<p class="empty">${feedType === 'candidato' ? 'Nenhuma vaga cadastrada ainda.' : 'Nenhum candidato cadastrado ainda.'}</p>`;
    return;
  }

  if (feedPosition >= feedCards.length) {
    feedSection.innerHTML = feedType === 'candidato'
      ? '<p class="empty">Você viu todas as vagas disponíveis.</p>'
      : '<p class="empty">Você viu todos os candidatos disponíveis.</p>';
    return;
  }

  feedSection.innerHTML = feedCards[feedPosition];
}

function renderPainelCandidato() {
  if (feedType !== 'candidato') {
    feedType = 'candidato';
    feedPosition = 0;
    feedCards = listarVagas().map(cardVaga);
  }
  renderCurrentCard();
}

function renderPainelEmpresa() {
  if (feedType !== 'empresa') {
    feedType = 'empresa';
    feedPosition = 0;
    feedCards = listarCandidatos().map(cardCandidato);
  }
  renderCurrentCard();
}

feedSection.addEventListener('click', event => {
  const button = (event.target as HTMLElement).closest<HTMLButtonElement>('[data-action]');
  if (!button || !['like', 'dislike'].includes(button.dataset.action ?? '')) return;
  feedPosition++;
  renderCurrentCard();
});

function atualizarTela() {
  const u = usuarioAtual();
  stage.style.display = u ? 'none' : 'block';
  $('.user-logado').style.display = u ? 'flex' : 'none';
  $('#user_name').textContent = u ? u.nome : '';
  if (!u) {
    graficoCompetencias?.destroy();
    graficoCompetencias = null;
    loginType = null;
    profileOpen = false;
    feedType = null;
    feedCards = [];
    feedPosition = 0;
    stage.dataset.screen = 'select';
    selectScreen.setAttribute('aria-hidden', 'false');
    loginScreen.setAttribute('aria-hidden', 'true');
    loginForm.reset();
    candidateProfileForm.reset();
    companyProfileForm.reset();
    vacancyForm.reset();
    loginForm.classList.add('ativa');
    cadCandidato.classList.remove('ativa');
    cadEmpresa.classList.remove('ativa');
    return;
  }

  feedSection.hidden = profileOpen;
  profileScreen.hidden = !profileOpen;
  if (u.tipo === 'candidato') renderPainelCandidato(); else renderPainelEmpresa();
  if (profileOpen) renderPerfil(u);
}

atualizarTela()
