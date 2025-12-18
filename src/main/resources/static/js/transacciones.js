// Variables globales
let todasTransacciones = [];

// Función para formatear moneda
function formatCurrency(value) {
    return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'EUR',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(value);
}

// Función para formatear fecha
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString('es-ES', {
        day: '2-digit',
        month: '2-digit',
        year: 'numeric'
    });
}

// Cargar transacciones
async function cargarTransacciones() {
    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/transaction`);

        if (!response.ok) {
            throw new Error('Error al cargar transacciones');
        }

        todasTransacciones = await response.json();
        renderizarTransacciones();

    } catch (error) {
        console.error('Error:', error);
        document.getElementById('historialBody').innerHTML =
            '<tr><td colspan="5" class="loading" style="color: #dc3545;">Error al cargar transacciones</td></tr>';
    }
}

// Renderizar transacciones en la tabla
function renderizarTransaccionesFiltradas(transacciones) {
    const tbody = document.getElementById('historialBody');

    if (transacciones.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="loading">No hay transacciones para mostrar</td></tr>';
        return;
    }

    tbody.innerHTML = '';

    transacciones.forEach(t => {
        const tr = document.createElement('tr');
        tr.dataset.id = t.id;

        const tipoClass = t.tipoTransferenciaNombre === 'Ingreso' ? 'tipo-ingreso' : 'tipo-gasto';
        const cantidadFormateada = formatCurrency(t.cantidad);

        tr.innerHTML = `
            <td class="col-fecha">${formatDate(t.fechaTransaccion)}</td>
            <td class="col-categoria">${t.categoriaTransferenciaNombre || '-'}</td>
            <td class="col-tipo ${tipoClass}">${t.tipoTransferenciaNombre}</td>
            <td class="col-cantidad">${cantidadFormateada}</td>
            <td class="col-descripcion" title="${t.descripcion || ''}">${t.descripcion || '-'}</td>
        `;

        tr.onclick = () => {
            window.location.href = `/transacciones/edit/${t.id}`;
        };

        tbody.appendChild(tr);
    });
}

//Establece mes y año en curso al entrar pro primera vez
function establecerMesYAnioActual() {
    const hoy = new Date();
    const mesActual = hoy.getMonth() + 1; // Enero = 0
    const anioActual = hoy.getFullYear();

    const selectMes = document.getElementById('filtroMes');
    const selectAnio = document.getElementById('filtroAnio');

    if (selectMes) {
        selectMes.value = mesActual.toString();
    }
    if (selectAnio) {
        selectAnio.value = anioActual.toString();
    }

    // Renderiza con el filtro aplicado
    filtrarPorMesYAnio();
}


//Filtrar los resultados por mes y año
function filtrarPorMesYAnio() {
    const mesSeleccionado = document.getElementById('filtroMes').value;
    const anioSeleccionado = document.getElementById('filtroAnio').value;

    let transaccionesFiltradas = todasTransacciones;

    if (mesSeleccionado || anioSeleccionado) {
        transaccionesFiltradas = todasTransacciones.filter(t => {
            const fecha = new Date(t.fechaTransaccion);
            const mes = fecha.getMonth() + 1; // Enero = 0
            const anio = fecha.getFullYear();

            const coincideMes = mesSeleccionado ? mes === parseInt(mesSeleccionado) : true;
            const coincideAnio = anioSeleccionado ? anio === parseInt(anioSeleccionado) : true;

            return coincideMes && coincideAnio;
        });
    }

    renderizarTransaccionesFiltradas(transaccionesFiltradas);
}


// Establecer fecha actual en el formulario
function establecerFechaActual() {
    const inputFecha = document.getElementById('fecha');
    if (inputFecha && !inputFecha.value) {
        const hoy = new Date();
        const fechaStr = hoy.toISOString().split('T')[0];
        inputFecha.value = fechaStr;
    }
}

// Inicializar
document.addEventListener('DOMContentLoaded', () => {
    establecerFechaActual();
    cargarTransacciones().then(() => {
        establecerMesYAnioActual();
        document.getElementById('filtroMes').addEventListener('change', filtrarPorMesYAnio);
        document.getElementById('filtroAnio').addEventListener('change', filtrarPorMesYAnio);
    });
});