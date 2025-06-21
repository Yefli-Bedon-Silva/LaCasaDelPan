document.addEventListener('DOMContentLoaded', async () => {
    try {
        const res = await fetch('/api/adminventas/mis-pedidos');
        if (!res.ok) throw new Error("Debes iniciar sesión para ver tus pedidos.");
        const pedidos = await res.json();

        const container = document.getElementById('pedidos-container');
        container.innerHTML = '';

        if (pedidos.length === 0) {
            container.innerHTML = `
                <div class="empty-state">
                    <i class="bi bi-bag-x"></i>
                    <p>No tienes pedidos aún.</p>
                </div>`;
            return;
        }

        pedidos.forEach((pedido, index) => {
            const numeroPedidoCliente = index + 1;
            const totalPedido = pedido.items.reduce((acc, item) => acc + item.precioUnitario * item.cantidad, 0);

            const pedidoHTML = `
                <div class="pedido-item">
                    <div class="pedido-header">
                        <div class="pedido-id">
                            <i class="bi bi-receipt me-1"></i>
                            Pedido #${numeroPedidoCliente}
                        </div>
                        <div class="pedido-status" style="background: ${pedido.estado === 'Confirmado' ? '#28a745' : '#ffc107'};">
                            <i class="bi ${pedido.estado === 'Confirmado' ? 'bi-check-circle' : 'bi-hourglass-split'} me-1"></i>
                            ${pedido.estado}
                        </div>
                    </div>

                    <div class="pedido-details">
                        ${pedido.items.map(item => `
                            <div class="detail-row">
                                <span class="detail-label">
                                    <i class="bi bi-box me-1"></i> Producto:
                                </span>
                                <span class="detail-value producto-name">${item.nombreProducto}</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">
                                    <i class="bi bi-123 me-1"></i> Cantidad:
                                </span>
                                <span class="detail-value">${item.cantidad} unidad${item.cantidad > 1 ? 'es' : ''}</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">
                                    <i class="bi bi-tag me-1"></i> Precio unitario:
                                </span>
                                <span class="detail-value">S/ ${item.precioUnitario.toFixed(2)}</span>
                            </div>
                        `).join('')}
                        <div class="detail-row">
                            <span class="detail-label">
                                <i class="bi bi-calculator me-1"></i> Total:
                            </span>
                            <span class="detail-value">S/ ${totalPedido.toFixed(2)}</span>
                        </div>
                    </div>

                    <div class="btn-actions">
                        ${pedido.estado !== 'Confirmado' ? `
                            <button class="btn btn-ver-detalle" onclick="confirmarPedido(${pedido.id})">
                                <i class="bi bi-check2-circle"></i> Confirmar Pedido
                            </button>
                            <button class="btn btn-cancelar" onclick="cancelarPedido(${pedido.id})">
                                <i class="bi bi-x-circle"></i> Cancelar Pedido
                            </button>
                            </button>
                            <button class="btn btn-pdf">
                                <i class="bi bi-file-earmark-pdf-fill"></i> Descargar PDF
                            </button>`
                        : ''}
                    </div>
                </div>
            `;

            container.insertAdjacentHTML('beforeend', pedidoHTML);
        });

    } catch (err) {
        console.error(err);
        alert(err.message);
    }
});


async function confirmarPedido(id) {
    if (!confirm(`¿Deseas confirmar el pedido?`)) return;
    try {
        const formData = new URLSearchParams();
        formData.append("estado", "Confirmado");

        const res = await fetch(`/api/adminventas/editar/${id}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: formData
        });

        if (!res.ok) throw new Error("Error al confirmar el pedido.");
        alert("Pedido confirmado correctamente.");
        location.reload();
    } catch (err) {
        alert(err.message);
    }
}

// Cancelar pedido: llamada real al backend
async function cancelarPedido(id) {
    if (!confirm(`¿Estás seguro de cancelar el pedido?`)) return;
    try {
        const res = await fetch(`/api/adminventas/eliminar/${id}`, {
            method: "DELETE"
        });

        if (!res.ok) throw new Error("Error al cancelar el pedido.");
        alert("Pedido cancelado correctamente.");
        location.reload();
    } catch (err) {
        alert(err.message);
    }
}