package tech.brito.javafacerecognition.repository;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Comparator;
import java.util.HashSet;
import static java.util.Objects.isNull;
import java.util.Set;
import static tech.brito.javafacerecognition.core.Utils.getDiretorioResources;
import tech.brito.javafacerecognition.model.Usuario;

public class UserRepository {

    private static Set<Usuario> usuariosRegistrados;
    private static String arquivoRegistroUsuarios;

    public static Integer getNextId() {
        var max = getUsuariosRegistrados()
                .stream()
                .map(u -> u.getId())
                .max(Comparator.naturalOrder());

        if (max.isEmpty()) {
            return 1;
        }

        return max.get() + 1;
    }

    public static Usuario findById(Integer id) {

        var optionalUsuario = getUsuariosRegistrados().stream()
                .filter(u -> u.getId().equals(id))
                .findFirst();

        if (optionalUsuario.isEmpty()) {
            return new Usuario(null, "Usuário não encontrado");
        }

        var usuario = optionalUsuario.get();
        return new Usuario(usuario.getId(), usuario.getNome());
    }

    public static void create(Usuario usuario) {
        getUsuariosRegistrados().add(usuario);
        salvarArquivoUsuarios();
    }

    private static void salvarArquivoUsuarios() {

        try {

            var gson = new GsonBuilder().setPrettyPrinting().create();
            var usuariosJson = gson.toJson(getUsuariosRegistrados());
            var os = new OutputStreamWriter(new FileOutputStream(getArquivoRegistroUsuarios()), "UTF-8");
            os.write(usuariosJson);
            os.flush();
            os.close();
        } catch (IOException e) {
        }
    }

    private static String getArquivoRegistroUsuarios() {
        if (isNull(arquivoRegistroUsuarios)) {
            arquivoRegistroUsuarios = String.format("%s\\registroUsuarios.json", getDiretorioResources());
        }

        return arquivoRegistroUsuarios;
    }

    private static Set<Usuario> getUsuariosRegistrados() {
        if (isNull(usuariosRegistrados)) {
            carregarUsuariosRegistrados();
        }

        return usuariosRegistrados;
    }

    private static void carregarUsuariosRegistrados() {
        try {
            usuariosRegistrados = new HashSet<>();
            JsonElement jsonElement = JsonParser.parseReader(new FileReader(getArquivoRegistroUsuarios()));
            if (jsonElement instanceof JsonNull) {
                return;
            }

            var jsonArray = jsonElement.getAsJsonArray();
            for (var usuarioElement : jsonArray) {
                usuariosRegistrados.add(converterJsonParaUsuario(usuarioElement.getAsJsonObject()));
            }

        } catch (FileNotFoundException ex) {
            System.out.println("Erro -> " + ex);
        }
    }

    private static Usuario converterJsonParaUsuario(JsonObject json) {
        var usuario = new Usuario();
        usuario.setId(json.get("id").getAsInt());
        usuario.setNome(json.get("nome").getAsString());
        return usuario;
    }
}
