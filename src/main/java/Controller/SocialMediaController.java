package Controller;

import Model.Account;
import Service.SocialMediaServices;
import io.javalin.Javalin;
import io.javalin.http.Context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.SocialMediaDao;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private static Javalin app;
    private SocialMediaServices services;

    public SocialMediaController(){
        this(new SocialMediaServices(), new SocialMediaDao());
    }

    public SocialMediaController(SocialMediaServices services, SocialMediaDao dao){
        this.services = services;
        services.setDao(dao);
    }



    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        //if (app == null){
            SocialMediaController.app = Javalin.create();
            app.post("/register", this::handleRegister);
            app.post("/login", this::handleLogin);
            app.post("/messages", this::handleCreateMessage);
        //}

        return app;
    }

    private void handleRegister(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = services.serviceRegister(account);
        if (newAccount != null){
            ctx.status(200);
            ctx.json(mapper.writeValueAsString(newAccount));
        } else {
            ctx.status(400);
        }
    }

    private void handleLogin(Context ctx) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            Account account = mapper.readValue(ctx.body(), Account.class);
            Account newAccount = services.serviceLogin(account);
            if (newAccount != null){
                ctx.status(200);
                ctx.json(mapper.writeValueAsString(newAccount));
            } else {
                ctx.status(401);
            }  
        } catch (JsonProcessingException e){
            System.out.println(e.getStackTrace());
        } 
    }

    private void handleCreateMessage(Context ctx) {

    }

    private void handleupdateMessage(Context ctx) {

    }

    private void deleteMessageHandler(Context ctx) {

    }

    private void getMessageHandler(Context ctx) {

    }

    private void getAllMessagesHandler(Context ctx){

    }

    private void getAllMessagesOneUserHandler(Context ctx) {

    }

}