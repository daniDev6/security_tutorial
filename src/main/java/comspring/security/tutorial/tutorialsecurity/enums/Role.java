package comspring.security.tutorial.tutorialsecurity.enums;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMIN(Arrays.asList(Persmisos.ELIMINAR,Persmisos.TRAER_USUARIO,Persmisos.GUARDAR_USUARIO)),
    USER(Arrays.asList(Persmisos.TRAER_USUARIO));
    private List<Persmisos> persmisosLista;

    Role(List<Persmisos> persmisosLista) {
        this.persmisosLista = persmisosLista;
    }

    public List<Persmisos> getPersmisosLista() {
        return persmisosLista;
    }

    public void setPersmisosLista(List<Persmisos> persmisosLista) {
        this.persmisosLista = persmisosLista;
    }
}
