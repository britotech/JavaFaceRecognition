package tech.brito.javafacerecognition.core;

import static java.util.Objects.isNull;

public class Utils {

    private static String diretorioResources;

    public static String getDiretorioResources() {
        if (isNull(diretorioResources)) {
            diretorioResources = new java.io.File("").getAbsolutePath() + "\\src\\main\\resources";
        }

        return diretorioResources;
    }

    public static String getDiretorioImagens() {
        return String.format("%s\\imagens", getDiretorioResources());
    }
}
