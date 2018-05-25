/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pd_ifpb;

import Model.GrupoMensagens;
import com.sun.faces.util.CollectionsUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 * Como acessar a aplicação ws://localhost:8080/miniProjetoChat/endpoint/a
 *
 * @author alexs
 */
@ServerEndpoint("/endpoint/{sala}")
public class EndPoint {

    private static List<GrupoMensagens> gps = new ArrayList<>();

    @OnOpen
    public void onOpen(Session s, @PathParam("sala") String sala) throws IOException {
        boolean nova = true;
        if (gps.isEmpty()) {
            gps.add(new GrupoMensagens(sala, s));
            try {
                s.getBasicRemote().sendText("Criou a sala" + sala );
            } catch (IOException ex) {
                Logger.getLogger(EndPoint.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            for (GrupoMensagens gp : gps) {
                if (gp.getSala().equals(sala)) {
                    nova = false;
                    gp.addSession(s);
                    s.getBasicRemote().sendText("Entrou na sala: " + sala);
                }
            }

            if (nova == true) {
                try {
                    gps.add(new GrupoMensagens(sala, s));
                    s.getBasicRemote().sendText("Criou uma nova sala: " + sala);
                } catch (IOException ex) {
                    Logger.getLogger(EndPoint.class.getName()).log(Level.SEVERE, null, ex);
                }
            } 
        }
    }

    @OnMessage
    public String onMessage(String message, @PathParam("sala") String sala) {

        for (GrupoMensagens gm : gps) {
            if (gm.getSala().equals(sala)) {
                for (Session sg : gm.getUsuarios()) {
                    try {
                        sg.getBasicRemote().sendText(message);
                    } catch (IOException ex) {
                        Logger.getLogger(EndPoint.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return null;
    }
    
    @OnClose
    public void onClose(Session s,@PathParam("sala") String sala){
        for (GrupoMensagens gm : gps) {
            if (gm.getSala().equals(sala)) {
                gm.getUsuarios().remove(s);                
            }
        }
    }
}
