// Variables globales
let portfolioChart = null;

// Colores para el gráfico (puedes personalizarlos)
const COLORS = [
  "#FF0000", // Rojo intenso
  "#00FF00", // Verde lima
  "#0066FF", // Azul eléctrico
  "#FFFF00", // Amarillo puro
  "#8000FF", // Morado vibrante
  "#FF6600", // Naranja
  "#000000", // Negro absoluto
  "#FF00AA", // Rosa fucsia
  "#5A2D0C", // Marrón chocolate
  "#00FFFF", // Cian brillante
  "#FFD700", // Dorado
  "#FF1493", // Rosa fuerte
  "#8B0000", // Rojo oscuro
  "#32CD32", // Verde brillante
  "#1E90FF"  // Azul vivo
];

const assignedColors = {};

// Función para asignar color a un icono según el ticker
function getColorForTicker(ticker) {
    // Si ya tiene color asignado, lo devolvemos
    if (assignedColors[ticker]) {
        return assignedColors[ticker];
    }

    // Filtramos los colores que aún no se han usado
    const available = COLORS.filter(c => !Object.values(assignedColors).includes(c));

    // Si se acaban los colores, generamos uno nuevo con HSL aleatorio
    if (available.length === 0) {
        const newColor = `hsl(${Math.floor(Math.random() * 360)}, 100%, 50%)`;
        assignedColors[ticker] = newColor;
        return newColor;
    }

    // Elegimos uno aleatorio de los disponibles
    const color = available[Math.floor(Math.random() * available.length)];
    assignedColors[ticker] = color;
    return color;
}


// Función para formatear moneda
function formatCurrency(value) {
    return new Intl.NumberFormat('es-ES', {
        style: 'currency',
        currency: 'EUR',
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
    }).format(value);
}

// Función para formatear porcentaje
function formatPercentage(value) {
    const sign = value >= 0 ? '+' : '';
    return `${sign}${value.toFixed(2)}%`;
}

// Función para cargar datos del portfolio
async function cargarPortfolio() {
    try {
        // Obtener la URL base del contexto (por si tienes context path)
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/portfolio`);

        if (!response.ok) {
            throw new Error('Error al cargar el portfolio');
        }

        const data = await response.json();

        // Renderizar el gráfico
        renderizarGrafico(data);

        // Renderizar la lista de activos
        renderizarActivos(data);

    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudo cargar el portfolio. Por favor, intenta de nuevo.');
    }
}

// Función para renderizar el gráfico de dona
function renderizarGrafico(data) {
    const ctx = document.getElementById('portfolioChart');

    // Actualizar balance total
    document.getElementById('balanceTotal').textContent = formatCurrency(data.balanceTotal);

    // Destruir gráfico anterior si existe
    if (portfolioChart) {
        portfolioChart.destroy();
    }

    // Preparar datos para el gráfico
    const labels = data.activos.map(a => a.ticker);
    const valores = data.activos.map(a => a.valorActual);
    const colores = data.activos.map(a => getColorForTicker(a.ticker));

    // Crear nuevo gráfico
    portfolioChart = new Chart(ctx, {
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
            cutout: '75%',
            plugins: {
                legend: {
                    display: false
                },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            const label = context.label || '';
                            const value = context.parsed || 0;
                            const percentage = ((value / data.balanceTotal) * 100).toFixed(2);
                            return `${label}: ${formatCurrency(value)} (${percentage}%)`;
                        }
                    }
                }
            }
        }
    });
}

// Función para renderizar la lista de activos
function renderizarActivos(data) {
    const container = document.getElementById('assetsList');
    container.innerHTML = '';

    if (data.activos.length === 0) {
        container.innerHTML = '<p class="loading">No tienes inversiones activas</p>';
        return;
    }

    data.activos.forEach(activo => {
        const card = crearCardActivo(activo, data.balanceTotal);
        container.appendChild(card);
    });
}

// Función para crear una card de activo
function crearCardActivo(activo, balanceTotal) {
    const card = document.createElement('div');
    card.className = 'asset-card';

    // Calcular porcentajes usando los métodos del DTO
    const porcentajePortfolio = (activo.valorActual / balanceTotal) * 100;
    const gananciaPerdida = activo.valorActual - activo.valorInvertido;
    const porcentajeGananciaPerdida = activo.valorInvertido !== 0
        ? ((activo.valorActual - activo.valorInvertido) / activo.valorInvertido) * 100
        : 0;

    const isPositive = porcentajeGananciaPerdida >= 0;
    const changeClass = isPositive ? 'positive' : 'negative';
    const arrow = isPositive ? '↑' : '↓';

    // Construir HTML
    card.innerHTML = `
        <div class="asset-header">
            <div class="asset-icon" style="background-color: ${getColorForTicker(activo.ticker)}">
                ${activo.ticker.substring(0, 2)}
            </div>
            <div class="asset-title">
                <p class="asset-ticker">${activo.ticker}</p>
                <p class="asset-cantidad">${activo.cantidad.toFixed(4)} ${activo.ticker}</p>
            </div>
            <div class="asset-value">
                <p class="asset-price">${formatCurrency(activo.valorActual)}</p>
                <span class="asset-change ${changeClass}">
                    ${formatPercentage(porcentajeGananciaPerdida)} ${arrow}
                </span>
            </div>
        </div>
        <div class="asset-details">
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
                <span class="detail-value">${porcentajePortfolio.toFixed(2)}%</span>
            </div>
        </div>
    `;

    return card;
}

// Función para mostrar error
function mostrarError(mensaje) {
    const container = document.getElementById('assetsList');
    container.innerHTML = `<p class="loading" style="color: #dc3545;">${mensaje}</p>`;
}

// Funciones de navegación para los botones
function goToHistorial() {
    window.location.href = '/inversiones/historial';
}

function goToVenta() {
    window.location.href = '/inversiones/vender';
}

function goToCompra() {
    window.location.href = '/inversiones/crear';
}

// Cargar datos al iniciar la página
document.addEventListener('DOMContentLoaded', () => {
    cargarPortfolio();
});