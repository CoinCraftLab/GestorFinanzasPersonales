// Variables globales
let portfolioData = null;
let historialData = [];
let inversionAEditar = null;

// Colores para iconos (igual que en inversiones.js)
const COLORS = [
    '#F4E185', '#B8A5D8', '#9B7EBD', '#7D5BA6',
    '#C4C4C4', '#E8B4B8', '#A5D8B8', '#F4A460'
];

function getColorForTicker(ticker) {
    const index = ticker.charCodeAt(0) % COLORS.length;
    return COLORS[index];
}

// Cargar portfolio (activos en cartera)
async function cargarPortfolio() {
    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/portfolio`);

        if (!response.ok) {
            throw new Error('Error al cargar el portfolio');
        }

        portfolioData = await response.json();
        renderizarActivos();

    } catch (error) {
        console.error('Error:', error);
        document.getElementById('activosLista').innerHTML =
            '<p class="loading" style="color: #dc3545;">Error al cargar activos</p>';
    }
}

// Cargar historial de movimientos
async function cargarHistorial() {
    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/historial`);

        if (!response.ok) {
            throw new Error('Error al cargar el historial');
        }

        historialData = await response.json();
        renderizarHistorial();

    } catch (error) {
        console.error('Error:', error);
        document.getElementById('historialBody').innerHTML =
            '<tr><td colspan="6" class="loading" style="color: #dc3545;">Error al cargar historial</td></tr>';
    }
}

// Renderizar activos en cartera (igual que portfolio)
function renderizarActivos() {
    const container = document.getElementById('activosLista');

    if (!portfolioData || portfolioData.activos.length === 0) {
        container.innerHTML = '<p class="loading">No tienes activos en cartera</p>';
        return;
    }

    container.innerHTML = '';

    portfolioData.activos.forEach(activo => {
        const gananciaPerdida = activo.valorActual - activo.valorInvertido;
        const porcentajeGananciaPerdida = activo.valorInvertido !== 0
            ? ((activo.valorActual - activo.valorInvertido) / activo.valorInvertido) * 100
            : 0;

        const isPositive = porcentajeGananciaPerdida >= 0;
        const changeClass = isPositive ? 'positive' : 'negative';
        const arrow = isPositive ? '↑' : '↓';

        const card = document.createElement('div');
        card.className = 'activo-card';

        card.innerHTML = `
            <div class="activo-header">
                <div class="activo-icon" style="background-color: ${getColorForTicker(activo.ticker)}">
                    ${activo.ticker.substring(0, 2)}
                </div>
                <div class="activo-title">
                    <p class="activo-ticker">${activo.ticker}</p>
                    <p class="activo-cantidad">${activo.cantidad.toFixed(4)} ${activo.ticker}</p>
                </div>
                <div class="activo-value">
                    <p class="activo-price">${formatCurrency(activo.valorActual)}</p>
                    <span class="activo-change ${changeClass}">
                        ${formatPercentage(porcentajeGananciaPerdida)} ${arrow}
                    </span>
                </div>
            </div>
            <div class="activo-details">
                <div class="detail-row">
                    <span class="detail-label">Nombre completo:</span>
                    <span class="detail-value">${activo.nombre}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Valor invertido:</span>
                    <span class="detail-value">${formatCurrency(activo.valorInvertido)}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Ganancia/Pérdida:</span>
                    <span class="detail-value" style="color: ${isPositive ? '#28a745' : '#dc3545'}">
                        ${formatCurrency(gananciaPerdida)}
                    </span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">% del portfolio:</span>
                    <span class="detail-value">${((activo.valorActual / portfolioData.balanceTotal) * 100).toFixed(2)}%</span>
                </div>
            </div>
        `;

        container.appendChild(card);
    });
}

// Renderizar historial de movimientos - Al hacer clic en fila abre modal
function renderizarHistorial() {
    const tbody = document.getElementById('historialBody');

    if (historialData.length === 0) {
        tbody.innerHTML = '<tr><td colspan="6" class="loading">No hay movimientos registrados</td></tr>';
        return;
    }

    tbody.innerHTML = '';

    historialData.forEach(mov => {
        const tr = document.createElement('tr');

        const fecha = new Date(mov.fechaTransaccion);
        const fechaFormateada = fecha.toLocaleDateString('es-ES');

        const tipoClass = mov.tipo ? 'tipo-compra' : 'tipo-venta';

        tr.innerHTML = `
            <td>${fechaFormateada}</td>
            <td class="${tipoClass}">${mov.tipoTexto}</td>
            <td>${mov.categoria}</td>
            <td>${mov.ticker}</td>
            <td>${formatCurrency(mov.precio)}</td>
            <td>${mov.cantidad.toFixed(4)}</td>
        `;

        // Al hacer clic en la fila, abrir modal para editar
        tr.style.cursor = 'pointer';
        tr.onclick = () => abrirModalEditar(mov);

        tbody.appendChild(tr);
    });
}

// Abrir modal de editar con la inversión seleccionada
function abrirModalEditar(inversion) {
    inversionAEditar = inversion;

    // Rellenar el formulario
    document.getElementById('editTipo').value = inversion.tipoTexto;
    document.getElementById('editActivo').value = `${inversion.ticker} - ${inversion.nombreCompleto}`;
    document.getElementById('editPrecio').value = inversion.precio;
    document.getElementById('editCantidad').value = inversion.cantidad;

    const fecha = new Date(inversion.fechaTransaccion);
    const fechaStr = fecha.toISOString().split('T')[0];
    document.getElementById('editFecha').value = fechaStr;

    // Limpiar mensajes
    document.getElementById('mensajeErrorEdit').style.display = 'none';
    document.getElementById('mensajeExitoEdit').style.display = 'none';

    document.getElementById('modalEditar').style.display = 'block';
}

// Cerrar modal de editar
function cerrarModalEditar() {
    document.getElementById('modalEditar').style.display = 'none';
    inversionAEditar = null;
}

// Guardar edición
async function guardarEdicion(event) {
    event.preventDefault();

    if (!inversionAEditar) return;

    const precio = parseFloat(document.getElementById('editPrecio').value);
    const cantidad = parseFloat(document.getElementById('editCantidad').value);
    const fecha = document.getElementById('editFecha').value;

    if (precio <= 0 || cantidad <= 0) {
        mostrarErrorEdit('El precio y la cantidad deben ser mayores a 0');
        return;
    }

    const datos = {
        ticker: inversionAEditar.ticker,
        precio: precio,
        cantidad: cantidad,
        fechaTransaccion: fecha ? `${fecha}T00:00:00` : null
    };

    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/update/${inversionAEditar.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Error al editar la inversión');
        }

        mostrarExitoEdit('Inversión actualizada correctamente');

        // Recargar datos después de 1 segundo
        setTimeout(async () => {
            await cargarPortfolio();
            await cargarHistorial();
            cerrarModalEditar();
        }, 1000);

    } catch (error) {
        console.error('Error:', error);
        mostrarErrorEdit(error.message || 'Error al editar la inversión');
    }
}

// Confirmar eliminación (desde el modal)
async function confirmarEliminar() {
    if (!inversionAEditar) return;

    if (!confirm(`¿Estás seguro de que deseas eliminar esta inversión de ${inversionAEditar.ticker}?`)) {
        return;
    }

    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/eliminar/${inversionAEditar.id}`, {
            method: 'DELETE'
        });

        if (!response.ok) {
            throw new Error('Error al eliminar la inversión');
        }

        mostrarExitoEdit('Inversión eliminada correctamente');

        // Recargar datos después de 1 segundo
        setTimeout(async () => {
            await cargarPortfolio();
            await cargarHistorial();
            cerrarModalEditar();
        }, 1000);

    } catch (error) {
        console.error('Error:', error);
        mostrarErrorEdit('Error al eliminar la inversión');
    }
}

// Mostrar error en modal editar
function mostrarErrorEdit(mensaje) {
    const div = document.getElementById('mensajeErrorEdit');
    div.textContent = mensaje;
    div.style.display = 'block';

    setTimeout(() => {
        div.style.display = 'none';
    }, 5000);
}

// Mostrar éxito en modal editar
function mostrarExitoEdit(mensaje) {
    const div = document.getElementById('mensajeExitoEdit');
    div.textContent = mensaje;
    div.style.display = 'block';

    setTimeout(() => {
        div.style.display = 'none';
    }, 3000);
}

// Formatear moneda
function formatCurrency(value) {
    return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'EUR',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(value);
}

// Formatear porcentaje
function formatPercentage(value) {
    const sign = value >= 0 ? '+' : '';
    return `${sign}${value.toFixed(2)}%`;
}

// Cerrar modal al hacer clic fuera
window.onclick = function(event) {
    const modalEditar = document.getElementById('modalEditar');

    if (event.target === modalEditar) {
        cerrarModalEditar();
    }
}

// Inicializar al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    cargarPortfolio();
    cargarHistorial();
});