package modelo;

import excecoes.MatriculaInvalidaExcecao;
import java.util.regex.Pattern;

public class ValidadorMatricula {

    private static final Pattern FORMATO = Pattern.compile("^[A-Z]{3} \\d{3} [A-Z]{2}$");

    public static final String EXEMPLO = "ABC 123 MC";

    private ValidadorMatricula() {}

    public static boolean isValida(String matricula) {
        if (matricula == null) {
            return false;
        }
        return FORMATO.matcher(normalizar(matricula)).matches();
    }

    public static String normalizar(String matricula) {
        if (matricula == null) {
            return null;
        }
        return matricula.trim().toUpperCase().replaceAll("[\\s-]+", " ");
    }

    public static String validar(String matricula) throws MatriculaInvalidaExcecao {
        if (!isValida(matricula)) {
            throw new MatriculaInvalidaExcecao(
                "Matrícula inválida. Formato esperado: " + EXEMPLO);
        }
        return normalizar(matricula);
    }
}