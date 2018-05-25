/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.websocket.Session;

/**
 *
 * @author alexs
 */
public class GrupoMensagens {
    String sala;
    private List<Session> usuarios = new ArrayList<Session>();    

    public GrupoMensagens(String sala, Session s) {
        this.sala = sala;
        this.usuarios.add(s);
    }

    public String getSala() {
        return sala;
    }

    public void setSala(String sala) {
        this.sala = sala;
    }

    public List<Session> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Session> usuarios) {
        this.usuarios = usuarios;
    }            
  
    public void addSession(Session s){
        usuarios.add(s);
    }        
}
