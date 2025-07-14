document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('btnEnviarReclamo').addEventListener('click', function (event) {
        event.preventDefault();

        const fechaPedido = document.getElementById('fechaPedido').value;
        const motivoReclamo = document.getElementById('motivoReclamo').value;
        const detalle = document.getElementById('detalle').value.trim();
        const checkTerminos = document.getElementById('flexCheckDefault').checked;

        if (!fechaPedido || !motivoReclamo || motivoReclamo === "Seleccione un motivo" || !detalle) {
            Swal.fire('Campos vacíos', 'Por favor, completa todos los campos del reclamo', 'error');
            return;
        }

        if (!checkTerminos) {
            Swal.fire('Términos no aceptados', 'Debes aceptar los términos y condiciones para continuar', 'warning');
            return;
        }

        const reclamoDTO = {
            fechapedido: fechaPedido,
            motivoReclamo: motivoReclamo,
            detalle: detalle
        };

const csrfTokenMeta = document.querySelector('meta[name="_csrf"]');
const csrfHeaderMeta = document.querySelector('meta[name="_csrf_header"]');

if (!csrfTokenMeta || !csrfHeaderMeta) {
    console.error("No se encontraron los metadatos CSRF.");
    Swal.fire('Error interno', 'No se pudo validar el token CSRF.', 'error');
    return;
}

const csrfToken = csrfTokenMeta.getAttribute('content');
const csrfHeader = csrfHeaderMeta.getAttribute('content');
console.log("CSRF Header:", csrfHeader);
console.log("CSRF Token:", csrfToken);
        fetch('/Nuevo', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [csrfHeader]: csrfToken // ✅ Enviar CSRF token en el header
            },
            body: JSON.stringify(reclamoDTO)
        })
        .then(response => {
            if (!response.ok) {
                return response.text().then(errorMessage => {
                    Swal.fire('Error', errorMessage || 'No se pudo registrar el reclamo', 'error');
                    throw new Error(errorMessage);
                });
            }
            return response.text();
        })
        .then(data => {
            Swal.fire('¡Reclamo enviado!', 'Tu reclamo ha sido registrado con éxito', 'success')
                .then(() => window.location.href = '/inicio');
        })
        .catch(error => {
            console.error('Error al enviar el reclamo:', error);
        });
    });
});