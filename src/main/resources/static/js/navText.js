document.addEventListener('DOMContentLoaded', () => {
    const path = window.location.pathname; 

    if (path.startsWith('/index')) {
        let btn = document.getElementById("navButtonInicio");
        let img = document.getElementById("navImgInicio");
        if (btn) btn.style.fontWeight = "700";
        if (img) img.style.display = "";
    }

    if (path.startsWith('/transacciones')) {
        let btn = document.getElementById("navButtonTransacciones");
        let img = document.getElementById("navImgTransacciones");
        if (btn) btn.style.fontWeight = "700";
        if (img) img.style.display = "";
    }

    if (path.startsWith('/retos')) {
        let btn = document.getElementById("navButtonRetos");
        let img = document.getElementById("navImgRetos");
        if (btn) btn.style.fontWeight = "700";
        if (img) img.style.display = "";
    }

    if (path.startsWith('/inversiones')) {
        let btn = document.getElementById("navButtonInversiones");
        let img = document.getElementById("navImgInversiones");
        if (btn) btn.style.fontWeight = "700";
        if (img) img.style.display = "";
    }

    if (path.startsWith('/presupuestos')) {
        let btn = document.getElementById("navButtonPresupuestos");
        let img = document.getElementById("navImgPresupuesto");
        if (btn) btn.style.fontWeight = "700";
        if (img) img.style.display = "";
    }

    if (path.startsWith('/perfil')) {
        let btn = document.getElementById("navButtonPerfil");
        let img = document.getElementById("navImgPerfil");
        if (btn) btn.style.fontWeight = "700";
        if (img) img.style.display = "";
    }
});