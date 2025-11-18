package Persistencia;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

import usuario.*;
import tiquete.Tiquete;

public class PersistenciaUsuarios {

    private static final String RUTA = "data/usuarios.json";

    public PersistenciaUsuarios() {
        File f = new File(RUTA);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs();
                FileWriter fw = new FileWriter(f);
                fw.write("{\n  \"usuarios\": []\n}");
                fw.close();
            } catch (IOException e) {}
        }
    }

    public String cargar() {
    	try {
            String contenido = TextoUtils.cargarTexto(RUTA);
         
            return contenido;
        } catch (Exception e) {
            System.out.println("ERROR cargando: " + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }


    public void guardar(String json) {
        try {
            Writer w = new OutputStreamWriter(new FileOutputStream(RUTA), StandardCharsets.UTF_8);
            w.write(json);
            w.close();
        } catch (Exception e) {}
    }

    public List<Usuario> reconstruir(String json, List<Tiquete> todosTiquetes) {
        List<Usuario> usuarios = new ArrayList<>();

       
        // obtener bloque de usuarios
        String bloque = TextoUtils.obtenerBloque(json, "\"usuarios\"");
     
   
        
        
        List<String> objetos = TextoUtils.dividirBloques(bloque);

        for (int idx = 0; idx < objetos.size(); idx++) {
            String obj = objetos.get(idx);
        

            String login = TextoUtils.obtener(obj, "login");
            String password = TextoUtils.obtener(obj, "password");
            String tipo = TextoUtils.obtener(obj, "tipo");
            String saldoStr = TextoUtils.obtener(obj, "saldo");


            double saldo = 0;
            if (!saldoStr.equals("")) {
                try {
                    saldo = Double.parseDouble(saldoStr);
                } catch (NumberFormatException e) {
                    saldo = 0;
                }
            }

            Usuario u = null;
            switch (tipo.toUpperCase()) {
                case "CLIENTE": u = new Cliente(login, password, saldo, tipo); break;
                case "PROMOTOR": u = new Promotor(login, password, saldo, tipo); break;
                case "ORGANIZADOR": u = new Organizador(login, password, saldo, tipo); break;
                case "ADMINISTRADOR": u = new Administrador(login, password, tipo); break;
                default: continue;
            }

            // reconstruir tiquetes del usuario usando idsTiquetes
            String bloqueIds = TextoUtils.obtenerBloque(obj, "\"idsTiquetes\"");
            List<String> ids = TextoUtils.dividirArraySimple(bloqueIds);

            
            

            for (String idRef : ids) {
                String id = TextoUtils.obtener(idRef, null); // valor literal
                
                boolean encontrado = false;
                for (Tiquete t : todosTiquetes) {
                    if (t.getId().equals(id)) {
                        ((IDuenoTiquetes) u).agregarTiquete(t);
                        encontrado = true;
                        break;
                    }
                }
                
                if (!encontrado) {
                    System.out.println("    ✗ NO ENCONTRADO");
                }
                 
            }
            
            usuarios.add(u);
        }
        
        System.out.println("Total usuarios reconstruidos: " + usuarios.size());
        System.out.println("========================\n");

        return usuarios;
    }

}

