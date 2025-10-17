package Persistencia;

public interface IPersistencia<T extends IFormateo> {
	void crearArchivo();
    void cargar(T newObjeto);
    void salvar(String jsonFormatted);
}
