package Controller;

import static io.javalin.apibuilder.ApiBuilder.delete;
import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.patch;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.SocialMediaDao;
import Model.Account;
import Model.Message;
import Service.SocialMediaServices;
import io.javalin.Javalin;
import io.javalin.http.Context;

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
            app.routes(()->{
                path("/messages", ()->{
                    get(this::handleGetAllMessages);
                    post(this::handleCreateMessage);
                    path("/{message_id}", ()->{
                        get(this::getMessageHandler);
                        patch(this::handleupdateMessage);
                        delete(this::deleteMessageHandler);
                    });
                });
            });
            app.post("/register", this::handleRegister);
            app.post("/login", this::handleLogin);
            app.get("/accounts/{account_id}/messages", this::getAllMessagesOneUserHandler);

        //}

        return app;
    }

    private void handleRegister(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = services.registerUser(account);
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
            Account newAccount = services.loginUser(account);
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
        try{
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(ctx.body(), Message.class);
            Message newMessage = services.createMessage(message);
            if (newMessage != null){
                ctx.status(200);
                ctx.json(mapper.writeValueAsString(newMessage));
            } else {
                ctx.status(400);
            }  
        } catch (JsonProcessingException e){
            System.out.println(e.getStackTrace());
        } 

    }

    private void handleupdateMessage(Context ctx) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            String message_text = mapper.readTree(ctx.body()).get("message_text").asText();
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = new Message();
            message.setMessage_id(message_id);
            message.setMessage_text(message_text);

            Message updatedMessage = services.updateMessage(message);
            if (updatedMessage != null){
                ctx.status(200);
                
                ctx.json(mapper.writeValueAsString(updatedMessage));
            } else {
                ctx.status(400);
            }  
        } catch (JsonProcessingException e){
            System.out.println(e.getStackTrace());
        }
    }

    private void deleteMessageHandler(Context ctx) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = new Message();
            message.setMessage_id(message_id);

            Message deletedMessage = services.deleteMessage(message);
            ctx.status(200);
            String s = deletedMessage==null? "" : mapper.writeValueAsString(deletedMessage);
            ctx.json(s);
        } catch (JsonProcessingException e){
            System.out.println(e.getStackTrace());
        }

    }

    private void getMessageHandler(Context ctx) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            int message_id = Integer.parseInt(ctx.pathParam("message_id"));
            Message message = new Message();
            message.setMessage_id(message_id);

            Message retrievedMessage = services.getMessage(message);
            
            ctx.status(200);
            String s = retrievedMessage==null? "" : mapper.writeValueAsString(retrievedMessage);
            ctx.json(s);
            
        } catch (JsonProcessingException e){
            System.out.println(e.getStackTrace());
        }

    }

    private void handleGetAllMessages(Context ctx){
        try{
            ObjectMapper mapper = new ObjectMapper();

            List<Message> retrievedMessage = services.getAllMessages();
            
            ctx.status(200);
            String s = retrievedMessage==null? "" : mapper.writeValueAsString(retrievedMessage);
            ctx.json(s);
            
        } catch (JsonProcessingException e){
            System.out.println(e.getStackTrace());
        }
    }

    private void getAllMessagesOneUserHandler(Context ctx) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            int posted_by = Integer.parseInt(ctx.pathParam("account_id"));
            Message message = new Message();
            message.setPosted_by(posted_by);

            List<Message> retrievedMessage = services.getAllMessagesFromUser(message);
            
            ctx.status(200);
            String s = retrievedMessage==null? "" : mapper.writeValueAsString(retrievedMessage);
            ctx.json(s);
            
        } catch (JsonProcessingException e){
            System.out.println(e.getStackTrace());
        }

    }

}