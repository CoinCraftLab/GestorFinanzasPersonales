//Cara el usuario activo
async function cargarUsuarioActivo() {
    try {
        const response = await fetch(`${window.location.origin}/api/user/perfil`);
        if (!response.ok) throw new Error('Error al cargar usuario activo');

        const usuario = await response.json();
        document.getElementById('userName').textContent = usuario.name || '-';
        document.getElementById('userEmail').textContent = usuario.email || '-';
    } catch (error) {
        console.error(error);
        document.getElementById('userName').textContent = 'Error';
        document.getElementById('userEmail').textContent = 'Error';
    }
}


// Abrir modal
function abrirModalEditarPerfil() {
    document.getElementById('modalEditarPerfil').style.display = 'block';
}

// Cerrar modal
function cerrarModalEditarPerfil() {
    document.getElementById('modalEditarPerfil').style.display = 'none';
}

// Cambiar pestaña activa
function cambiarTab(tab) {
    document.querySelectorAll('.tab-button').forEach(btn => btn.classList.remove('active'));
    document.querySelectorAll('.tab-content').forEach(content => content.classList.remove('active'));

    document.querySelector(`.tab-button[onclick="cambiarTab('${tab}')"]`).classList.add('active');
    document.getElementById(`tab-${tab}`).classList.add('active');
}

// Cambiar username
async function cambiarUsername(event) {
    event.preventDefault();
    const nuevoUsername = document.getElementById('newUsername').value.trim();

    if (!nuevoUsername) {
        mostrarMensaje('mensajeUsername', 'El nombre de usuario es obligatorio', 'error');
        return;
    }

    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/user/changeUserName`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ newName: nuevoUsername })
        });

        if (!response.ok) throw new Error();

        document.getElementById('userName').textContent = nuevoUsername;
        mostrarMensaje('mensajeUsername', 'Nombre de usuario actualizado correctamente', 'success');
    } catch {
        mostrarMensaje('mensajeUsername', 'Error al actualizar el nombre de usuario', 'error');
    }
}

// Cambiar email
async function cambiarEmail(event) {
    event.preventDefault();
    const nuevoEmail = document.getElementById('newEmail').value.trim();

    if (!nuevoEmail) {
        mostrarMensaje('mensajeEmail', 'El email es obligatorio', 'error');
        return;
    }

    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/user/changeUserEmail`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ newEmail: nuevoEmail })
        });

        if (!response.ok) throw new Error();

        document.getElementById('userEmail').textContent = nuevoEmail;
        mostrarMensaje('mensajeEmail', 'Email actualizado correctamente', 'success');
    } catch {
        mostrarMensaje('mensajeEmail', 'Error al actualizar el email', 'error');
    }
}

// Cambiar contraseña
async function cambiarPassword(event) {
    event.preventDefault();
    const oldPassword = document.getElementById('oldPassword').value.trim();
    const newPassword = document.getElementById('newPassword').value.trim();
    const confirmPassword = document.getElementById('confirmPassword').value.trim();

    if (!oldPassword || !newPassword || !confirmPassword) {
        mostrarMensaje('mensajePassword', 'Todos los campos son obligatorios', 'error');
        return;
    }
    if (newPassword !== confirmPassword) {
        mostrarMensaje('mensajePassword', 'Las contraseñas no coinciden', 'error');
        return;
    }

    try {
        const contextPath = window.location.origin;
        const response = await fetch(`${contextPath}/api/user/changeUserPassword`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                        oldPassword,
                        newPassword,
                        newConfirmationPassword: confirmPassword
                  })
        });

        if (!response.ok) throw new Error();

        mostrarMensaje('mensajePassword', 'Contraseña actualizada correctamente', 'success');
        cerrarModalEditarPerfil();
    } catch {
        mostrarMensaje('mensajePassword', 'Error al actualizar la contraseña', 'error');
    }
}

// Mostrar mensajes en cada tab
function mostrarMensaje(id, texto, tipo) {
    const div = document.getElementById(id);
    div.textContent = texto;
    div.className = `mensaje ${tipo}`;
    div.style.display = 'block';
}

// Inicializar
document.addEventListener('DOMContentLoaded', () => {
    cargarUsuarioActivo();
});
