package ar.edu.utn.frc.backend;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ar.edu.utn.frc.backend.modelo.Auto;

public class Collection {

	/*
	 * Devuelve la cantidad de autos de una marca y un año determinado
	 *
	 * @param autos array de autos
	 * @param marca marca a buscar
	 * @param anio año a buscar
	 * @return cantidad de autos de una marca y un año determinado
	 */
	public static long obtenerCantidadPorMarcaYAnio(List<Auto> autos, String marca, int anio) {

		return autos
				.stream()
				.filter(auto -> Objects.equals(auto.getMarca(), marca) && auto.getAnio() == anio)
				.count();
	}

	/*
	 * Devuelve la cantidad de autos convertibles
	 *
	 * @param autos array de autos
	 * @return cantidad de autos convertibles
	 */
	public static long obtenerCantidadDeConvertibles(List<Auto> autos) {

		List<Auto> autosConvertibles = autos
				.stream()
				.filter(auto-> auto.getTipos()
						.stream()
						.anyMatch(tipo-> tipo.equalsIgnoreCase( "Convertible")))
				.collect((Collectors.toList()));
				// ó .count() de una

		return autosConvertibles
				.size();
	}


	/*
	 * Devuelve un array con las marcas que vendan sedanes
	 *
	 * @param autos array de autos
	 * @return array de marcas
	 */
	public static Set<String> obtenerLasMarcasQueVendanSedanes(List<Auto> autos) {

		return autos
				.stream()
				.filter(auto -> auto.getTipos()
						.stream()
						.anyMatch(tipo-> tipo.equalsIgnoreCase("Sedan")))
				.map(Auto::getMarca)
				.collect(Collectors.toSet());
	}

	/*
	 * Devuelve un map con la cantidad de autos por marca
	 *
	 * @param autos array de autos
	 * @return map con la cantidad de autos por marca
	 */
	public static Map<String, Long> obtenerCantidadDeAutosPorMarca(List<Auto> autos) {
		/*
		Map<String, Long> autosPorMarcas = new HashMap<>();

		autos.forEach((auto)-> {
			long cantidad = autosPorMarcas.getOrDefault(auto.getMarca(), 0L);

			autosPorMarcas.put(auto.getMarca(), cantidad + 1);
		});
		return autosPorMarcas;*/

		return autos
				.stream()
				.collect(Collectors.groupingBy(Auto::getMarca, Collectors.counting()));
	}

	/*
	 * Devuelve un map con la cantidad de autos por año
	 *
	 * @param autos array de autos
	 * @return map con la cantidad de autos por año
	 */
	public static Map<Integer, Long> obtenerCantidadDeAutosPorAnio(List<Auto> autos) {

		return autos
				.stream()
				.collect(Collectors.groupingBy(Auto::getAnio, Collectors.counting()));

	}

	/*
	 * Devuelve los autos filtrados
	 * @param autos array de autos
	 * @param filtro filtro a aplicar
	 * @return autos filtrados
	 */
	public static List<Auto> filtrarAutos(List<Auto> autos, Predicate<Auto> filtro) {
		return autos
				.stream()
				.filter(filtro)
				.collect((Collectors.toList()));
	}

	/**
	 * Devuelve una lista de autos que no sean del tipo especificado
	 *
	 * @param autos array de autos
	 * @param tipo  tipo a filtrar
	 * @return lista de autos que no sean del tipo especificado
	 */
	public static List<Auto> obtenerTodosLosAutosQueNoSeanDelTipo(List<Auto> autos, String tipo) {
		return autos
				.stream()
				.filter((auto)->auto.getTipos()
						.stream()
						.noneMatch(tipoAuto -> tipoAuto.equalsIgnoreCase(tipo)))
				.collect(Collectors.toList());
	}

	/*
	 * Obtener las marcas que tengan modelos con números en el nombre
	 * @param autos array de autos
	 * @return marcas que tengan modelos con números en el nombre
	 */

	public static Set<String> obtenerLasMarcasQueTenganModelosConNumeros(List<Auto> autos) {
		return autos
				.stream()
				.filter(auto -> auto.getModelo().chars().anyMatch(Character::isDigit))
				.map(Auto::getMarca)
				.collect(Collectors.toSet());
		/*
		ó
		return autos
            .stream()
            .filter(auto -> auto.getModelo().chars().anyMatch(Character::isDigit))
            .map(Auto::getMarca)
            .collect(Collectors.toSet());
		 */
	}

	/*
	 * Leer el archivo autos.csv y devuelve un array de autos
	 * @return array de autos
	 */
	public static List<Auto> obtenerAutos() {
		try {
			Path path = Path.of(
						ClassLoader.getSystemResource("autos.csv").toURI()
			);
			Stream<String> stream = Files.lines(path);

			List<Auto> autos = stream.map(Auto::fromString).toList();

			stream.close();

			return autos;
		}
		catch (URISyntaxException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static String obtenerLaMarcaQueVendaMasConvertibles(List<Auto> autos) {
		    return autos
			.stream()
			.filter(auto -> auto.getTipos()
			.stream()
			.anyMatch(tipo -> tipo.equalsIgnoreCase("Convertible")))
			.collect(Collectors.groupingBy(Auto::getMarca, Collectors.counting()))
			.entrySet()
			.stream()
			.max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
	}
}
