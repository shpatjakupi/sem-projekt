package rest;

import DTO.travelsDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errorhandling.NotFoundException;
import facades.travelsFacade;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 */
@Path("travels")
public class travelResource {
   private travelsFacade facade = new travelsFacade();
   private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    
    @Context
    private UriInfo context;
    
    @Path ("all")
    @Produces(MediaType.APPLICATION_JSON)
    @GET
    public Map getPersons() throws Exception {
        return facade.apiDataAll();
    }
}