package com.taller.modiesel.integration;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class VentaRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:procesarVenta")
                .doTry()
                .log("📦 Procesando venta ${body}")
                .to("bean:inventarioService?method=actualizarStockDesdeVenta")
                .to("bean:facturaService?method=generarFacturaDesdeVenta")
                .log("✅ Venta procesada completamente")
                .doCatch(Exception.class)
                .log("❌ Error procesando venta: ${exception.message}")
                .end();
    }
}