function goToTransacciones() {
  window.location.href = '/transacciones'; // llama al controller WebTransactionController#getToTransacciones
}

document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('navButtonTransacciones');
    if (btn) btn.onclick = goToTransacciones; // asigna el onclick
});