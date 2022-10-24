package tech.brito.javafacerecognition.repository;

import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import tech.brito.javafacerecognition.model.Usuario;

public class UserRepository {

    private static final Map<Integer, Usuario> USERS_STORE = new ConcurrentHashMap();

    public static Integer getNextId() {
        Optional<Integer> max = USERS_STORE.keySet().stream().max(Comparator.naturalOrder());
        if (max.isEmpty()) {
            return 1;
        }

        return max.get() + 1;
    }

    public static void create(Usuario user) {
        USERS_STORE.put(user.getId(), user);
    }

    public static Usuario findById(Integer id) {
        var user = USERS_STORE.get(id);
        return new Usuario(user.getId(), user.getNome());
    }
}
