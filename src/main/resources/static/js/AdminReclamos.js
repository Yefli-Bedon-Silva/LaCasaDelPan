const modalEditar = document.getElementById('modalEditar');
modalEditar.addEventListener('show.bs.modal', function (event) {
    const button = event.relatedTarget;
    document.getElementById('edit-id').value = button.getAttribute('data-id');
    document.getElementById('edit-cliente').value = button.getAttribute('data-cliente');
    document.getElementById('edit-fecha').value = button.getAttribute('data-fecha');
    document.getElementById('edit-motivo').value = button.getAttribute('data-motivo');
    document.getElementById('edit-detalle').value = button.getAttribute('data-detalle');
    document.getElementById('edit-estado').value = button.getAttribute('data-estado');
});