package rest;

import DTO.FlightInfoDTO;
import DTO.PlacesDTO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.exceptions.UnirestException;
import entities.User;
import errorhandling.NotFoundException;
import facades.SkyscannerFacade;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;
import utils.EMF_Creator;

/**
 * REST Web Service
 *
 */
@Path("flights")
public class SkyScannerResource{
    
   private SkyscannerFacade skyFacade = new SkyscannerFacade();
   private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    
    @Context
    private UriInfo context;
    
    @Context
    SecurityContext securityContext;
    
     @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            List<User> users = em.createQuery("select user from User user").getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }
    
     @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("flightdata/{inboundDate}/{cabinClass}/{originPlace}/{destinationPlace}/{outboundDate}/{adults}")
    public List<FlightInfoDTO> getFlightInfo(
            @PathParam("inboundDate") String inboundDate, 
            @PathParam("cabinClass") String cabinClass, 
            @PathParam("originPlace") String originPlace, 
            @PathParam("destinationPlace") String destinationPlace, 
            @PathParam("outboundDate") String outboundDate,
            @PathParam("adults") int adults) 
            throws UnirestException {
        System.out.println(outboundDate);
        System.out.println(cabinClass);
        System.out.println(originPlace);
        System.out.println(destinationPlace);
        System.out.println(adults);

        return skyFacade.getFlightSearch(inboundDate, cabinClass, originPlace, destinationPlace, outboundDate, adults);
            
    }
    
    

    /* @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed("user")
    public String getFromUser() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to User: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello to (admin) User: " + thisuser + "\"}";
    }
     */

}