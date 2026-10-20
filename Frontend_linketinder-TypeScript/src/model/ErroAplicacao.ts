export class ErroAplicacao extends Error {
  constructor(mensagem: string) {
    super(mensagem);
    this.name = 'ErroAplicacao';
  }
}
