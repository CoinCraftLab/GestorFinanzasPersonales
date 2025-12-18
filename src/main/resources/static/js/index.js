// Variables globales
let balanceChart = null;
let ahorroChart = null;
let liquidoChart = null;

// Colores para el gráfico principal
const MAIN_COLORS = {
    inversiones: '#4CAF50',  // Verde
    ahorro: '#F4E185',       // Amarillo
    liquido: '#C77B7B',      // Rojo/Marrón
    retos: '#7BC8E5'         // Azul claro
};

// Función para formatear moneda
function formatCurrency(value) {
    return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'EUR',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(value);
}

// Cargar todos los datos del dashboard
async function cargarDashboard() {
    try {
        const contextPath = window.location.origin;

        // Cargar datos en paralelo
        const [portfolioRes, retosRes, presupuestosRes, transaccionesRes] = await Promise.all([
            fetch(`${contextPath}/api/inversiones/portfolio`),
            fetch(`${contextPath}/api/reto/listar`),
            fetch(`${contextPath}/api/presupuestos`),
            fetch(`${contextPath}/api/transaction`)
        ]);

        const portfolio = await portfolioRes.json();
        const retos = await retosRes.json();
        const presupuestos = await presupuestosRes.json();
        const transacciones = await transaccionesRes.json();

        // Calcular valores
        const totalInversiones = portfolio.balanceTotal || 0;
        const totalRetos = calcularTotalRetos(retos);
        const { ahorro, liquido } = calcularAhorroYLiquido(transacciones);
        const balanceTotal = totalInversiones + ahorro + liquido;

        // Renderizar todo
        renderizarBalance(balanceTotal);
        renderizarGraficoCircular({
            inversiones: totalInversiones,
            ahorro: ahorro,
            liquido: liquido
        });
        renderizarGraficosLinea(transacciones);
        renderizarPresupuestos(presupuestos);

    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudo cargar el dashboard');
    }
}

// Calcular total de retos
function calcularTotalRetos(retos) {
    return retos.reduce((sum, reto) => sum + (reto.retoCantidadActual || 0), 0);
}

// Calcular ahorro y líquido basado en transacciones
function calcularAhorroYLiquido(transacciones) {
    let ahorro = 0;
    let liquido = 0;

    transacciones.forEach(t => {
        // Asumiendo que tipoTransferenciaId: 1 = Ingreso, 2 = Gasto
        // y categorías específicas para ahorro
        if (t.tipoTransferenciaId === 1) {
            // Si es ingreso, suma a líquido
            liquido += t.cantidad;
        } else if (t.tipoTransferenciaId === 2) {
            // Si es gasto, resta de líquido
            liquido -= t.cantidad;
        }

        // Puedes ajustar esta lógica según tus categorías reales
        if (t.categoriaTransferenciaNombre && t.categoriaTransferenciaNombre.toLowerCase().includes('ahorro')) {
            ahorro += t.cantidad;
        }
    });

    return { ahorro, liquido };
}

// Renderizar balance total
function renderizarBalance(total) {
    document.getElementById('balanceTotal').textContent = formatCurrency(total);
}

// Renderizar gráfico circular principal
function renderizarGraficoCircular(datos) {
    const ctx = document.getElementById('mainChart');

    if (balanceChart) {
        balanceChart.destroy();
    }

    const labels = ['Inversiones', 'Ahorro', 'Liquido'];
    const valores = [datos.inversiones, datos.ahorro, datos.liquido];
    const colores = [
        MAIN_COLORS.inversiones,
        MAIN_COLORS.ahorro,
        MAIN_COLORS.liquido,
    ];

    balanceChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: valores,
                backgroundColor: colores,
                borderWidth: 0,
                spacing: 2
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: true,
            cutout: '70%',
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const label = context.label || '';
                            const value = context.parsed || 0;
                            return `${label}: ${formatCurrency(value)}`;
                        }
                    }
                }
            }
        }
    });

    // Actualizar leyenda
    document.getElementById('inversionesValue').textContent = formatCurrency(datos.inversiones);
    document.getElementById('ahorroValue').textContent = formatCurrency(datos.ahorro);
    document.getElementById('liquidoValue').textContent = formatCurrency(datos.liquido);
}

// Renderizar gráficos de línea (Ahorro y Líquido)
function renderizarGraficosLinea(transacciones) {
    // Procesar datos por mes
    const datosPorMes = procesarDatosMensuales(transacciones);

    renderizarGraficoAhorro(datosPorMes);
    renderizarGraficoLiquido(datosPorMes);
}

// Procesar datos mensuales
function procesarDatosMensuales(transacciones) {
    const meses = {};

    transacciones.forEach(t => {
        const fecha = new Date(t.fechaTransaccion);
        const mesKey = `${fecha.getFullYear()}-${String(fecha.getMonth() + 1).padStart(2, '0')}`;

        if (!meses[mesKey]) {
            meses[mesKey] = { ahorro: 0, liquido: 0 };
        }

        if (t.tipoTransferenciaId === 1) {
            meses[mesKey].liquido += t.cantidad;
        } else {
            meses[mesKey].liquido -= t.cantidad;
        }

        if (t.categoriaTransferenciaNombre && t.categoriaTransferenciaNombre.toLowerCase().includes('ahorro')) {
            meses[mesKey].ahorro += t.cantidad;
        }
    });

    return meses;
}

// Renderizar gráfico de ahorro
function renderizarGraficoAhorro(datosPorMes) {
    const ctx = document.getElementById('ahorroChart');

    if (ahorroChart) {
        ahorroChart.destroy();
    }

    const labels = Object.keys(datosPorMes).sort();
    const valores = labels.map(mes => datosPorMes[mes].ahorro);

    ahorroChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Ahorro',
                data: valores,
                borderColor: MAIN_COLORS.ahorro,
                backgroundColor: `${MAIN_COLORS.ahorro}33`,
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// Renderizar gráfico de líquido
function renderizarGraficoLiquido(datosPorMes) {
    const ctx = document.getElementById('liquidoChart');

    if (liquidoChart) {
        liquidoChart.destroy();
    }

    const labels = Object.keys(datosPorMes).sort();
    const valores = labels.map(mes => datosPorMes[mes].liquido);

    liquidoChart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: labels,
            datasets: [{
                label: 'Líquido',
                data: valores,
                borderColor: MAIN_COLORS.liquido,
                backgroundColor: `${MAIN_COLORS.liquido}33`,
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

// Renderizar presupuestos
function renderizarPresupuestos(presupuestos) {
    const container = document.getElementById('presupuestosGrid');

    if (presupuestos.length === 0) {
        container.innerHTML = '<p class="loading">No hay presupuestos configurados</p>';
        return;
    }

    // Tomar los primeros 4 presupuestos
    const presupuestosMostrar = presupuestos.slice(0, 4);

    container.innerHTML = '';

    presupuestosMostrar.forEach(p => {
        // Calcular porcentaje
        const porcentaje = Math.min(100, Math.floor(Math.random() * 100)); // Placeholder

        const card = document.createElement('div');
        card.className = 'presupuesto-card';

        card.innerHTML = `
            <h4>${p.categoriaTransferenciaName || 'Categoría'}</h4>
            <div class="presupuesto-circle">
                <canvas id="presupuesto-${p.id}"></canvas>
                <div class="presupuesto-percentage">${porcentaje}%</div>
            </div>
        `;

        container.appendChild(card);

        // Crear gráfico circular pequeño
        setTimeout(() => {
            const ctx = document.getElementById(`presupuesto-${p.id}`);
            new Chart(ctx, {
                type: 'doughnut',
                data: {
                    datasets: [{
                        data: [porcentaje, 100 - porcentaje],
                        backgroundColor: ['#8B7BC8', '#E0E0E0'],
                        borderWidth: 0
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: true,
                    cutout: '80%',
                    plugins: {
                        legend: { display: false },
                        tooltip: { enabled: false }
                    }
                }
            });
        }, 100);
    });
}

// Mostrar error
function mostrarError(mensaje) {
    console.error(mensaje);
    document.getElementById('balanceTotal').textContent = 'Error';
}

// Inicializar al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    cargarDashboard();
});