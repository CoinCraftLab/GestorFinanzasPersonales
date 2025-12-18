// Funciones para manejar modales

// Asignar colores diferentes a cada círculo
function asignarColoresCirculos() {
    const circulos = document.querySelectorAll('.circle');

    circulos.forEach((circulo, index) => {
        // Asignar clase de color según el índice (0-15 colores diferentes)
        const colorIndex = index % 16;
        circulo.classList.add(`circle-color-${colorIndex}`);

        // Obtener el porcentaje del texto dentro del círculo
        const span = circulo.querySelector('span');
        if (span) {
            const porcentajeTexto = span.textContent.trim();
            const porcentaje = parseInt(porcentajeTexto.replace('%', ''));

            // Establecer la variable CSS --progreso
            circulo.style.setProperty('--progreso', `${porcentaje}%`);
        }
    });
}

// Ejecutar al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    asignarColoresCirculos();
});

// Abrir modal de crear reto
function abrirModalCrearReto() {
    document.getElementById('modalCrearReto').style.display = 'block';
}

// Cerrar modal de crear reto
function cerrarModalCrearReto() {
    document.getElementById('modalCrearReto').style.display = 'none';
}

// Abrir modal de editar reto
function abrirModalEditarReto() {
    document.getElementById('modalEditarReto').style.display = 'block';
}

// Cerrar modal de editar reto
function cerrarModalEditarReto() {
    document.getElementById('modalEditarReto').style.display = 'none';
}

// Cerrar modales al hacer clic fuera
window.onclick = function(event) {
    const modalCrear = document.getElementById('modalCrearReto');
    const modalEditar = document.getElementById('modalEditarReto');

    if (event.target === modalCrear) {
        cerrarModalCrearReto();
    }
    if (event.target === modalEditar) {
        cerrarModalEditarReto();
    }
}

// Navegación a detalle de reto
function irADetalleReto(retoId) {
    window.location.href = `/retos/${retoId}`;
}

// Función para ir a crear reto
function goToCrearReto() {
    window.location.href = '/retos/crearReto';
}

// Validar formulario antes de enviar
function validarFormularioReto(event) {
    const nombre = document.querySelector('input[name="nombre"]').value;
    const cantidad = document.querySelector('input[name="retoCantidad"]').value;
    const fecha = document.querySelector('input[name="fechaFin"]').value;

    if (!nombre || !cantidad || !fecha) {
        alert('Por favor, completa los campos obligatorios');
        event.preventDefault();
        return false;
    }

    if (parseFloat(cantidad) <= 0) {
        alert('La cantidad objetivo debe ser mayor a 0');
        event.preventDefault();
        return false;
    }

    return true;
}

// Confirmar eliminación de reto
function confirmarEliminarReto(event) {
    if (!confirm('¿Está seguro de querer eliminar el reto? Esta acción no se puede deshacer.')) {
        event.preventDefault();
        return false;
    }
    return true;
}