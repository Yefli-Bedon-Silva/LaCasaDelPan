document.getElementById('btnListarPedidos').addEventListener('click', listarpedido);

async function listarpedido() {
  try {
    const res = await fetch('/api/adminventas/listar'); // URL API REST
    if (!res.ok) throw new Error("Error cargando pedidos");
    const pedidos = await res.json();

    mostrarPedidos(pedidos);
  } catch (e) {
    alert(e.message);
  }
}

function mostrarPedidos(pedidos) {
  const cont = document.getElementById('tablaResultados');
  cont.innerHTML = ""; // limpio todo antes

  const tabla = document.createElement('table');
  tabla.className = "table table-sm table-striped table-hover align-middle text-center";

  tabla.innerHTML = `
    <thead class="table-dark">
      <tr>
        <th>ID</th>
        <th>Cliente</th>
        <th>Fecha</th>
        <th>Pedido</th>
        <th>Cantidad</th> 
        <th>Monto($)</th>
        <th>Estado</th>
        <th>Acciones</th>
      </tr>
    </thead>
    <tbody></tbody>
  `;

  const tbody = tabla.querySelector('tbody');

  pedidos.forEach(pedido => {
       // Calculamos la cantidad total sumando cantidades de los items del pedido
    const cantidadTotal = pedido.items?.reduce((acc, item) => acc + (item.cantidad || 0), 0) ?? 0;

    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${pedido.id}</td>
      <td>${pedido.cliente?.nombreCli ?? ''} ${pedido.cliente?.apellidosCli ?? ''}</td>
      <td>${pedido.fecha?.substring(0, 10) ?? ''}</td>
      <td>${pedido.items?.map(i => i.nombreProducto).join(", ") ?? ''}</td>
      <td>${cantidadTotal}</td>
      <td>$${(pedido.items?.reduce((acc, item) => acc + (item.precioUnitario * item.cantidad), 0) ?? 0).toFixed(2)}</td>
      <td id="estado-${pedido.id}">${pedido.estado}</td>
      <td>
        <button class="btn btn-warning btn-sm" onclick="EditarPedido(${pedido.id})">
          <i class="fas fa-edit"></i>
        </button>
        <button class="btn btn-danger btn-sm" onclick="eliminarPedido(${pedido.id})">
          <i class="fas fa-trash"></i>
        </button>
      </td>
    `;
    tbody.appendChild(tr);
  });

  cont.appendChild(tabla);
}

async function EditarPedido(id) {
  try {
    const res = await fetch(`/api/adminventas/${id}`);
    if (!res.ok) throw new Error("Pedido no encontrado");
    const pedido = await res.json();

    // Crear modal solo si no existe
    let modalDiv = document.getElementById('modalEditarPedido');
    if (modalDiv) modalDiv.remove();

    modalDiv = document.createElement('div');
    modalDiv.className = "modal fade";
    modalDiv.id = "modalEditarPedido";
    modalDiv.tabIndex = -1;
    modalDiv.setAttribute("aria-labelledby", "modalEditarPedidoLabel");
    modalDiv.setAttribute("aria-hidden", "true");

    modalDiv.innerHTML = `
      <div class="modal-dialog">
        <div class="modal-content">
          <form id="formEditarPedido">
            <div class="modal-header">
              <h5 class="modal-title" id="modalEditarPedidoLabel">Editar Estado del Pedido</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
            </div>
            <div class="modal-body">
              <input type="hidden" id="pedidoId" name="pedidoId" value="${pedido.id}" />
              <div class="mb-3">
                <label for="estadoPedido" class="form-label">Estado</label>
                <select class="form-select" id="estadoPedido" name="estado" required>
                  <option value="Pendiente">Pendiente</option>
                  <option value="En proceso">En proceso</option>
                  <option value="Enviado">Enviado</option>
                  <option value="Entregado">Entregado</option>
                  <option value="Cancelado">Cancelado</option>
                </select>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
              <button type="submit" class="btn btn-primary">Guardar cambios</button>
            </div>
          </form>
        </div>
      </div>
    `;

    document.body.appendChild(modalDiv);

    document.getElementById('estadoPedido').value = pedido.estado;

    const modal = new bootstrap.Modal(modalDiv);
    modal.show();

    document.getElementById('formEditarPedido').addEventListener('submit', async (e) => {
      e.preventDefault();
      await guardarEdicionPedido(modal);
    });

  } catch (e) {
    alert(e.message);
  }
}

async function guardarEdicionPedido(modal) {
  const id = document.getElementById('pedidoId').value;
  const estado = document.getElementById('estadoPedido').value;

  try {
    const res = await fetch(`/api/adminventas/editar/${id}`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'Accept': 'application/json'
      },
      body: new URLSearchParams({ estado: estado })
    });
    if (!res.ok) throw new Error("Error al guardar cambios");

    alert("Estado actualizado correctamente");
    modal.hide();
    listarpedido();
  } catch (e) {
    alert(e.message);
  }
}

async function eliminarPedido(id) {
  if (!confirm("¿Seguro que deseas eliminar este pedido?")) return;

  try {
    const res = await fetch(`/api/adminventas/eliminar/${id}`, {
      method: 'DELETE'
    });
    if (!res.ok) throw new Error("Error al eliminar pedido");

    alert("Pedido eliminado");
    listarpedido();
  } catch (e) {
    alert(e.message);
  }
}

document.getElementById('btnDetallePedido').addEventListener('click', async () => {
  const { value: idPedido } = await Swal.fire({
    title: 'Ingrese el ID del pedido',
    input: 'number',
    inputLabel: 'ID del pedido',
    inputPlaceholder: 'Ejemplo: 1',
    showCancelButton: true,
    inputValidator: value => {
      if (!value) return 'Por favor ingresa un ID válido';
      if (value <= 0) return 'El ID debe ser mayor que 0';
    }
  });

  if (!idPedido) return; // Cancelado

  try {
    const res = await fetch(`/api/adminventas/${idPedido}`);
    if (!res.ok) throw new Error('Pedido no encontrado');

    const pedido = await res.json();
    mostrarModalDetallePedido(pedido);

  } catch (error) {
    Swal.fire('Error', error.message, 'error');
  }
});


function mostrarModalDetallePedido(pedido) {
  // Crear modal si no existe
  let modalDiv = document.getElementById('modalDetallePedido');
  if (modalDiv) modalDiv.remove();

  modalDiv = document.createElement('div');
  modalDiv.className = "modal fade";
  modalDiv.id = "modalDetallePedido";
  modalDiv.tabIndex = -1;
  modalDiv.setAttribute("aria-labelledby", "modalDetallePedidoLabel");
  modalDiv.setAttribute("aria-hidden", "true");

  // Construimos lista de productos con cantidad, precio y subtotal
  const itemsHTML = pedido.items?.map(item => `
    <tr>
      <td>${item.nombreProducto}</td>
      <td>${item.cantidad}</td>
      <td>$${item.precioUnitario.toFixed(2)}</td>
      <td>$${(item.cantidad * item.precioUnitario).toFixed(2)}</td>
    </tr>
  `).join('') || '';

  modalDiv.innerHTML = `
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="modalDetallePedidoLabel">Detalle del Pedido #${pedido.id}</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
        </div>
        <div class="modal-body">
          <p><strong>Cliente:</strong> ${pedido.cliente?.nombreCli ?? ''} ${pedido.cliente?.apellidosCli ?? ''}</p>
          <p><strong>Correo:</strong> ${pedido.cliente?.correo ?? ''}</p>
          <p><strong>Fecha:</strong> ${pedido.fecha?.substring(0,10) ?? ''}</p>
          <p><strong>Estado:</strong> ${pedido.estado}</p>
          <p><strong>Total:</strong> $${(pedido.items?.reduce((acc, item) => acc + (item.precioUnitario * item.cantidad), 0) ?? 0).toFixed(2)}</p>


          <h6>Productos:</h6>
          <table class="table table-sm table-bordered">
            <thead>
              <tr>
                <th>Producto</th>
                <th>Cantidad</th>
                <th>Precio Unitario</th>
                <th>Subtotal</th>
              </tr>
            </thead>
            <tbody>
              ${itemsHTML}
            </tbody>
          </table>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
        </div>
      </div>
    </div>
  `;

  document.body.appendChild(modalDiv);
  const modal = new bootstrap.Modal(modalDiv);
  modal.show();
}