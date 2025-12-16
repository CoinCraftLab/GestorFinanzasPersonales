//Variables globales
let activos = [];
let portfolioData = null;

//Cargar datos del portfolio para verificar cantidades disponibles
async function cargarPortfolio() {
    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/portfolio`);

        if (!response.ok) {
            throw new Error('Error al cargar el portfolio');
        }

        portfolioData = await response.json();
        renderizarActivos(); //Primero carga portfolio y luego renderiza los activos

    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudo cargar el portfolio');
    }
}

//Carga los activos
async function cargarActivos() {
    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/activos`);

        if (!response.ok) {
            throw new Error('Error al cargar los activos');
        }

        activos = await response.json();


    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudieron cargar los activos');
    }
}

//Renderiza en el select los activos que el usuario tiene
function renderizarActivos() {
    const select = document.getElementById('activo');

    if (activos.length === 0 || !portfolioData) {
        select.innerHTML = '<option value="">No hay activos disponibles</option>';
        return;
    }

    //Filtra los activos que tiene el usuario
    const activosDisponibles = activos.filter(activo => {
        return portfolioData.activos.some(a => a.ticker === activo.ticker && a.cantidad > 0);
    });

    if (activosDisponibles.length === 0) {
        select.innerHTML = '<option value="">No tiene activos para vender</option>';
        return;
    }

    select.innerHTML = '<option value="">Seleccionar activo</option>';

    activosDisponibles.forEach(activo => {
        const option = document.createElement('option');
        option.value = activo.ticker;
        option.textContent = `${activo.ticker} - ${activo.nombreCompleto}`;
        select.appendChild(option);
    });
}

//Muestra la cantidad disponible del activo seleccionado
function mostrarCantidadDisponible() {
    const ticker = document.getElementById('activo').value;
    const infoDiv = document.getElementById('infoDisponible');
    const cantidadSpan = document.getElementById('cantidadDisponible');

    if (!ticker || !portfolioData) {
        infoDiv.style.display = 'none';
        return;
    }

    const activoPortfolio = portfolioData.activos.find(a => a.ticker === ticker);

    if (activoPortfolio) {
        cantidadSpan.textContent = `${activoPortfolio.cantidad.toFixed(4)} ${ticker}`;
        infoDiv.style.display = 'block';
    } else {
        infoDiv.style.display = 'none';
    }
}

//Envia formulario de venta
async function enviarVenta(event) {
    event.preventDefault();

    ocultarMensajes();

    const ticker = document.getElementById('activo').value;
    const precio = parseFloat(document.getElementById('precio').value);
    const cantidad = parseFloat(document.getElementById('cantidad').value);
    const fecha = document.getElementById('fecha').value;

    //Validaciones
    if (!ticker) {
        mostrarError('Debe seleccionar un activo');
        return;
    }

    if (precio <= 0 || cantidad <= 0) {
        mostrarError('El precio y la cantidad deben ser mayores a 0');
        return;
    }

    //Valida que no venda más de lo que tiene
    const activoPortfolio = portfolioData.activos.find(a => a.ticker === ticker);
    if (activoPortfolio && cantidad > activoPortfolio.cantidad) {
        mostrarError(`No puedes vender más de ${activoPortfolio.cantidad.toFixed(4)} ${ticker}`);
        return;
    }

    //Prepara los datos necesarios para enviar
    const datos = {
        ticker: ticker,
        precio: precio,
        cantidad: cantidad,
        fechaTransaccion: fecha ? `${fecha}T00:00:00` : null
    };

    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/vender`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Error al vender la inversión');
        }

        const resultado = await response.json();

        mostrarExito('Venta realizada');

        //Recargar portfolio
        await cargarPortfolio();

        //Limpia el formulario después de 1.5 segundos
        setTimeout(() => {
            limpiarFormulario();
        }, 1500);

        //Redirige al portfolio después de 2 segundos
        setTimeout(() => {
            goToInversiones();
        }, 2000);

    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.message || 'Error al vender la inversión');
    }
}

//Limpiar formulario
function limpiarFormulario() {
    document.getElementById('formVenta').reset();
    document.getElementById('infoDisponible').style.display = 'none';
    renderizarActivos(); //Para restaurar el select al limpiar el formulario
}

//Muestra mensaje de error
function mostrarError(mensaje) {
    const divError = document.getElementById('mensajeError');
    divError.textContent = mensaje;
    divError.style.display = 'block';

    //Ocultar después de 5 segundos
    setTimeout(() => {
        divError.style.display = 'none';
    }, 5000);
}

//Mostrar mensaje de éxito
function mostrarExito(mensaje) {
    const divExito = document.getElementById('mensajeExito');
    divExito.textContent = mensaje;
    divExito.style.display = 'block';

    //Ocultar después de 3 segundos
    setTimeout(() => {
        divExito.style.display = 'none';
    }, 3000);
}

//Ocultar todos los mensajes
function ocultarMensajes() {
    document.getElementById('mensajeError').style.display = 'none';
    document.getElementById('mensajeExito').style.display = 'none';
}

//Establece fecha actual por defecto
function establecerFechaActual() {
    const hoy = new Date();
    const fechaStr = hoy.toISOString().split('T')[0];
    document.getElementById('fecha').value = fechaStr;
}

//Inicializa la página
document.addEventListener('DOMContentLoaded', () => {
    cargarActivos().then(() =>{ cargarPortfolio(); });
    establecerFechaActual();
});