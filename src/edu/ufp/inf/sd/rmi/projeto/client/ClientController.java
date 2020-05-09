package edu.ufp.inf.sd.rmi.projeto.client;

import edu.ufp.inf.sd.rmi.projeto.server.UserFactoryRI;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handlerLogin(ActionEvent actionEvent) throws IOException {
        System.out.println("oalsodlasolsoadldConteoor");
        if(client != null){
            client.handlerLogin(actionEvent);
        }
//        // se os campos nao tiverem vazios
//        if(!username.getText().trim().isEmpty() && !password.getText().trim().isEmpty()){
//            String usr = username.getText().trim();
//            String psw = password.getText().trim();
//            UserSessionRI sessionRI = this.userFactoryRI.login(usr,psw);
//            if(sessionRI != null){
//                text.setText("Sessão iniciada");
//            }
//        }
    }
}
