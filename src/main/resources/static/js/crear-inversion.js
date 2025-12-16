//Variable global
let activos = [];


//Cargar todos los activos
async function cargarActivos() {

    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/activos`);

        if (!response.ok) {
            throw new Error('Error al cargar los activos');
        }

        activos = await response.json();
        renderizarActivos();

    } catch (error) {
        console.error('Error:', error);
        mostrarError('No se pudieron cargar los activos');
    }
}

//Renderizar activos en el select
function renderizarActivos() {
    const select = document.getElementById('activo');

    if (activos.length === 0) {
        select.innerHTML = '<option value="">No hay activos disponibles</option>';
        return;
    }

    select.innerHTML = '<option value="">Seleccionar activo</option>';

    activos.forEach(activo => {
        const option = document.createElement('option');
        option.value = activo.ticker;
        option.textContent = `${activo.ticker} - ${activo.nombreCompleto}`;
        select.appendChild(option);
    });
}

//Envia formulario de compra
async function enviarCompra(event) {
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

    //Prepara los datos para enviar
    const datos = {
        ticker: ticker,
        precio: precio,
        cantidad: cantidad,
        fechaTransaccion: fecha ? `${fecha}T00:00:00` : null
    };

    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/inversiones/crear`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(datos)
        });

        if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message || 'Error al crear la inversión');
        }

        const resultado = await response.json();

        mostrarExito('Inversión creada');

        //Limpia formulario después de 1.5 segundos
        setTimeout(() => {
            limpiarFormulario();
        }, 1500);

        //Redirige al portfolio después de 2 segundos
        setTimeout(() => {
            goToInversiones();
        }, 2000);

    } catch (error) {
        console.error('Error:', error);
        mostrarError(error.message || 'Error al crear la inversión');
    }
}

//Limpia formulario
function limpiarFormulario() {
    document.getElementById('formCompra').reset();
    renderizarActivos();
}

//Mostrar mensaje de error
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

//Oculta todos los mensajes
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
    establecerFechaActual();
    cargarActivos();
});