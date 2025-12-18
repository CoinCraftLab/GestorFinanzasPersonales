function goToTransacciones() {
  window.location.href = '/transacciones'; // llama al controller WebTransactionController#getToTransacciones
}

function goToIndex() {
  window.location.href = '/index';
}

function goToInversiones(){
  window.location.href = '/inversiones';
}

function goToPresupuesto(){
  window.location.href = '/presupuestos/prueba';
}

function goToRetos(){
  window.location.href = '/retos';
}

function goToPerfil(){
  window.location.href = '/perfil';
}

function goToHistorial(){
  window.location.href = '/inversiones/historial';
}

function goToVenta(){
  window.location.href = '/inversiones/vender';
}

function goToCompra(){
  window.location.href = '/inversiones/crear';
}

function goToCrearReto(){
  window.location.href = '/retos/crearReto';
}


document.addEventListener('DOMContentLoaded', () => {
  // Los links funcionand direcamtente con el onclick no hace falta esto pero lo dejo por si acaso 
  /*
    const navButtonTransacciones = document.getElementById('navButtonTransacciones');
    if (navButtonTransacciones) navButtonTransacciones.onclick = goToTransacciones; 

    const navButtonInicio = document.getElementById('navButtonInicio');
    if (navButtonInicio) navButtonInicio.onclick = goToIndex; 

    const navButtonRetos = document.getElementById('navButtonRetos');
    if (navButtonRetos) navButtonRetos.onclick = goToRetos; 

    const navButtonInversiones = document.getElementById('navButtonInversiones');
    if (navButtonInversiones) btn.navButtonInversiones = goToInversiones; 

    const navButtonPresupuestos = document.getElementById('navButtonPresupuestos');
    if (navButtonPresupuestos) navButtonPresupuestos.onclick = goToPresupuesto; 

    const navButtonPerfil = document.getElementById('navButtonPerfil');
    if (navButtonPerfil) navButtonPerfil.onclick = goToPerfil; 
    */
});