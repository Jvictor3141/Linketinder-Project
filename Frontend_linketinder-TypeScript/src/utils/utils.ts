export const esc = (t: string) =>
  t.replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]!));

export const parseLista = (t: string) => [...new Set(t.split(',').map(s => s.trim()).filter(Boolean))];
export const tags = (l: string[]) => l.map(c => `<span class="tag">${esc(c)}</span>`).join('');

export async function hash(texto: string) {
  const buf = await crypto.subtle.digest('SHA-256', new TextEncoder().encode(texto));
  return [...new Uint8Array(buf)].map(b => b.toString(16).padStart(2, '0')).join('');
}

export const lerForm = (f: HTMLFormElement) => Object.fromEntries(new FormData(f)) as Record<string, string>;