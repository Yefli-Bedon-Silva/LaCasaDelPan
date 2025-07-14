    document.addEventListener('DOMContentLoaded', async () => {
        try {
            const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            const res = await fetch('/api/adminventas/mis-pedidos', {
                method: 'GET',
                headers: {
                    [header]: token
                },
                credentials: 'same-origin'
            });

            if (!res.ok) throw new Error("Debes iniciar sesión para ver tus pedidos.");

            const pedidos = await res.json(); // 🔧 Aquí faltaba esta línea
            const container = document.getElementById('pedidos-container');

            if (pedidos.length === 0) {
                container.innerHTML = '<p class="text-center">No tienes pedidos aún.</p>';
                return;
            }

            pedidos.forEach(pedido => {
                const card = document.createElement('div');
                card.className = 'card shadow-sm mb-4';

                const cardHeader = document.createElement('div');
                cardHeader.className = 'card-header bg-primary text-white';
                cardHeader.textContent = `Pedido #${pedido.id} - Fecha: ${pedido.fecha.substring(0, 10)}`;
                card.appendChild(cardHeader);

                const cardBody = document.createElement('div');
                cardBody.className = 'card-body';

                const estadoP = document.createElement('p');
                estadoP.innerHTML = `<strong>Estado:</strong> ${pedido.estado}`;
                cardBody.appendChild(estadoP);

                const tabla = document.createElement('table');
                tabla.className = 'table';

                tabla.innerHTML = `
                    <thead>
                        <tr>
                            <th>Nombre</th>
                            <th>Cantidad</th>
                            <th>Precio Unitario</th>
                            <th>Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${pedido.items.map(item => `
                            <tr>
                                <td>${item.nombreProducto}</td>
                                <td>${item.cantidad}</td>
                                <td>S/. ${item.precioUnitario.toFixed(2)}</td>
                                <td>S/. ${(item.cantidad * item.precioUnitario).toFixed(2)}</td>
                            </tr>
                        `).join('')}
                    </tbody>
                `;
                cardBody.appendChild(tabla);

                const totalP = document.createElement('p');
                totalP.className = 'text-end fw-bold fs-5 price';
                const totalCalculado = pedido.items.reduce((acc, i) => acc + i.cantidad * i.precioUnitario, 0);
                totalP.innerHTML = `Total a pagar: S/. ${totalCalculado.toFixed(2)}`;
                cardBody.appendChild(totalP);

                const botonesDiv = document.createElement('div');
                botonesDiv.className = 'd-flex justify-content-end gap-2 mt-3';

                const btnImprimir = document.createElement('button');
                btnImprimir.className = 'btn btn-success';
                btnImprimir.textContent = 'Imprimir Recibo';
                btnImprimir.onclick = () => window.print();

                const btnPDF = document.createElement('button');
                btnPDF.className = 'btn btn-primary';
                btnPDF.textContent = 'Descargar PDF';
                btnPDF.disabled = true;

                botonesDiv.appendChild(btnImprimir);
                botonesDiv.appendChild(btnPDF);
                cardBody.appendChild(botonesDiv);

                const alertInfo = document.createElement('div');
                alertInfo.className = 'alert alert-info mt-4 mb-0';
                alertInfo.textContent = 'Recibirás un correo con los detalles y la fecha estimada de entrega.';
                cardBody.appendChild(alertInfo);

                card.appendChild(cardBody);
                container.appendChild(card);
            });

        } catch (err) {
            console.error(err);
            alert(err.message);
        }
    });
