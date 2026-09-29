import { lerForm, esc, tags, hash, parseLista } from "./utils/utils";
import { usuarioAtual, login, logout } from "./service/SectionService";
import type { Candidato, Empresa, Tipo, Vaga } from './model/Perfis';
import { chave, listar, salvar } from "./repository/UserRepository";
import { criarVaga, deletarVaga, emailExiste, criarConta } from "./service/AccountService";


const $ = <T extends HTMLElement>(sel: string) => document.querySelector(sel) as T;

const stage = $(".stage");
const selectScreen = $(".screen--select");
const loginScreen = $(".screen--login");
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

function setField(form: HTMLFormElement, name: string, value: string) {
  const field = form.elements.namedItem(name) as HTMLInputElement | HTMLTextAreaElement | null;
  if (field) field.value = value;
}

function renderCompanyVacancies(empresa: Empresa) {
  const vagas = listar<Vaga>('vaga').filter(vaga => vaga.empresaId === empresa.id);
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
  $("#company-vacancy-management").hidden = usuario.tipo !== 'empresa';

  if (usuario.tipo === 'candidato') {
    setField(candidateProfileForm, 'nome', usuario.nome);
    setField(candidateProfileForm, 'email', usuario.email);
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
  setField(companyProfileForm, 'cnpj', usuario.cnpj);
  setField(companyProfileForm, 'pais', usuario.pais);
  setField(companyProfileForm, 'estado', usuario.estado);
  setField(companyProfileForm, 'cep', usuario.cep);
  setField(companyProfileForm, 'descricao', usuario.descricao);
  renderCompanyVacancies(usuario);
}

profileButton.addEventListener('click', () => {
  profileOpen = true;
  atualizarTela();
});

logoutButton.addEventListener('click', () => logout());

feedButton.addEventListener('click', () => {
  profileOpen = false;
  atualizarTela();
});

candidateProfileForm.addEventListener('submit', async event => {
  event.preventDefault();
  const usuario = usuarioAtual();
  if (!usuario || usuario.tipo !== 'candidato') return;

  const dados = lerForm(candidateProfileForm);
  const email = dados.email.trim().toLowerCase();
  if (listar<Candidato>('candidato').some(c => c.id !== usuario.id && c.email === email)) {
    alert('Já existe candidato com esse e-mail.');
    return;
  }

  const atualizado: Candidato = {
    ...usuario, nome: dados.nome, email, cpf: dados.cpf, idade: Number(dados.idade),
    estado: dados.estado, cep: dados.cep, formacao: dados.formacao,
    competencias: parseLista(dados.competencias), descricao: dados.descricao,
    senhaHash: dados.senha ? await hash(dados.senha) : usuario.senhaHash,
  };
  salvar(chave('candidato', usuario.id), atualizado);
  atualizarTela();
});

companyProfileForm.addEventListener('submit', async event => {
  event.preventDefault();
  const usuario = usuarioAtual();
  if (!usuario || usuario.tipo !== 'empresa') return;

  const dados = lerForm(companyProfileForm);
  const email = dados.email.trim().toLowerCase();
  if (listar<Empresa>('empresa').some(e => e.id !== usuario.id && e.email === email)) {
    alert('Já existe empresa com esse e-mail.');
    return;
  }

  const atualizado: Empresa = {
    ...usuario, nome: dados.nome, email, cnpj: dados.cnpj, pais: dados.pais,
    estado: dados.estado, cep: dados.cep, descricao: dados.descricao,
    senhaHash: dados.senha ? await hash(dados.senha) : usuario.senhaHash,
  };
  salvar(chave('empresa', usuario.id), atualizado);
  atualizarTela();
});

vacancyForm.addEventListener('submit', event => {
  event.preventDefault();
  const usuario = usuarioAtual();
  if (!usuario || usuario.tipo !== 'empresa') return;

  const dados = lerForm(vacancyForm);
  criarVaga({
    empresaId: usuario.id, titulo: dados.titulo, descricao: dados.descricao,
    competencias: parseLista(dados.competencias),
  });
  vacancyForm.reset();
});

companyVacancies.addEventListener('click', event => {
  const button = (event.target as HTMLElement).closest<HTMLButtonElement>('[data-delete-vacancy]');
  const usuario = usuarioAtual();
  if (!button || !usuario || usuario.tipo !== 'empresa') return;

  const id = button.dataset.deleteVacancy;
  const vaga = listar<Vaga>('vaga').find(item => item.id === id && item.empresaId === usuario.id);
  if (vaga) deletarVaga(vaga.id);
});

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
  if (!u) {
    profileOpen = false;
    stage.dataset.screen = 'select';
    selectScreen.setAttribute('aria-hidden', 'false');
    loginScreen.setAttribute('aria-hidden', 'true');
    $('.login-form').classList.add('ativa');
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
