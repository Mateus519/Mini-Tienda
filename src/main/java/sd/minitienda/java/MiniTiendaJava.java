package sd.minitienda.java;


import javax.swing.*;
import java.util.*;

public class MiniTiendaJava {
    // TASK 1: Modelo de datos
    static ArrayList<String> nombres = new ArrayList<>();
    static double[] precios = new double[0]; // array sincronizado con nombres
    static HashMap<String, Integer> stock = new HashMap<>();
    static double totalVentas = 0;

    public static void main(String[] args) {
        while (true) {
            String opcion;
            opcion = JOptionPane.showInputDialog(null, """
                                                       === MINI-TIENDA ===
                                                       1. Agregar producto
                                                       2. Listar inventario
                                                       3. Comprar producto
                                                       4. Mostrar estad\u00edsticas
                                                       5. Buscar producto
                                                       6. Salir
                                                       Elige opci\u00f3n:""");

            if (opcion == null) break; // cancelar = salir
            switch (opcion) {
                case "1" -> agregarProducto();
                case "2" -> listarInventario();
                case "3" -> comprarProducto();
                case "4" -> mostrarEstadisticas();
                case "5" -> buscarProducto();
                case "6" -> {
                    salir(); return;
                }
                default -> JOptionPane.showMessageDialog(null, "Opción inválida");
            }
        }
    }

    // ===== Métodos utilitarios =====
    static void agregarProducto() {
        try {
            String nombre = JOptionPane.showInputDialog("Nombre del producto:");
            if (nombre == null || nombre.isBlank()) return;
            if (nombres.contains(nombre)) {
                JOptionPane.showMessageDialog(null, "❌ Producto duplicado");
                return;
            }
            double precio;
            do {
                try {
                    precio = Double.parseDouble(JOptionPane.showInputDialog("Precio:"));
                    if (precio < 0) {
                        JOptionPane.showMessageDialog(null, "⚠ El precio no puede ser negativo");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "⚠ Entrada inválida");
                        precio = -1; // fuerza a repetir el ciclo
                }
            } while (precio < 0);

            int cantidad;
            do {
                try {
                    cantidad = Integer.parseInt(JOptionPane.showInputDialog("Stock inicial:"));
                    if (cantidad < 0) {
                        JOptionPane.showMessageDialog(null, "⚠ El stock no puede ser negativo");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "⚠ Entrada inválida");
                    cantidad = -1; // fuerza a repetir el ciclo
                }
            } while (cantidad < 0);


            nombres.add(nombre);
            precios = expandPrecios(precios, precio);
            stock.put(nombre, cantidad);

            JOptionPane.showMessageDialog(null, "✅ Producto agregado");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "⚠ Entrada inválida");
        }
    }

    static void listarInventario() {
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inventario vacío");
            return;
        }
        StringBuilder sb = new StringBuilder("=== INVENTARIO ===\n");
        for (int i = 0; i < nombres.size(); i++) {
            sb.append(String.format("%s - $%.2f - Stock: %d\n",
                    nombres.get(i), precios[i], stock.get(nombres.get(i))));
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    static void comprarProducto() {
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inventario vacío");
            return;
        }
        String nombre = JOptionPane.showInputDialog("Producto a comprar:");
        int idx = indexOfNombre(nombre);
        if (idx == -1) {
            JOptionPane.showMessageDialog(null, "❌ No encontrado");
            return;
        }
        try {
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));
            int disponible = stock.get(nombres.get(idx));
            if (cantidad > disponible) {
                JOptionPane.showMessageDialog(null, "❌ Stock insuficiente");
                return;
            }
            stock.put(nombres.get(idx), disponible - cantidad);
            double subtotal = precios[idx] * cantidad;
            totalVentas += subtotal;
            JOptionPane.showMessageDialog(null, "✅ Compra realizada\nTotal: $" + subtotal);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "⚠ Entrada inválida");
        }
    }

    static void mostrarEstadisticas() {
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inventario vacío");
            return;
        }
        double min = precios[0], max = precios[0];
        String prodMin = nombres.get(0), prodMax = nombres.get(0);

        for (int i = 1; i < precios.length; i++) {
            if (precios[i] < min) { min = precios[i]; prodMin = nombres.get(i); }
            if (precios[i] > max) { max = precios[i]; prodMax = nombres.get(i); }
        }
        JOptionPane.showMessageDialog(null,
                "Producto más barato: " + prodMin + " ($" + min + ")\n" +
                "Producto más caro: " + prodMax + " ($" + max + ")");
    }

    static void buscarProducto() {
        if (nombres.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Inventario vacío");
            return;
        }
        String busqueda = JOptionPane.showInputDialog("Buscar producto por nombre:");
        if (busqueda == null || busqueda.isBlank()) return;

        StringBuilder sb = new StringBuilder("Resultados:\n");
        for (int i = 0; i < nombres.size(); i++) {
            if (nombres.get(i).toLowerCase().contains(busqueda.toLowerCase())) {
                sb.append(String.format("%s - $%.2f - Stock: %d\n",
                        nombres.get(i), precios[i], stock.get(nombres.get(i))));
            }
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    static void salir() {
        JOptionPane.showMessageDialog(null,
                """
                Gracias por usar la Mini-Tienda
                Total de ventas: $""" + totalVentas);
    }

    // = Helpers =
    static double[] expandPrecios(double[] original, double nuevo) {
        double[] copia = Arrays.copyOf(original, original.length + 1);
        copia[copia.length - 1] = nuevo;
        return copia;
    }

    static int indexOfNombre(String nombre) {
        for (int i = 0; i < nombres.size(); i++) {
            if (nombres.get(i).equalsIgnoreCase(nombre)) return i;
        }
        return -1;
    }
}
