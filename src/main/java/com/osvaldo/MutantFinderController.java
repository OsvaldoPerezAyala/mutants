package com.osvaldo;

import com.osvaldo.exception.CustomizedException;
import com.osvaldo.service.Service;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/mutant")
public class MutantFinderController {
    private static final Logger LOGGER = Logger.getLogger(MutantFinderController.class.getName());
    @Inject
    Service service;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response execute(String payload) {
        LOGGER.log(Level.INFO, "Payload => {0}", payload);
        try {
            JSONObject payloadObject = new JSONObject(payload);

            if (payloadObject.has("dna")) {
                var isMutant = service.isMutant(payloadObject);

                if (isMutant) {
                    LOGGER.log(Level.INFO, "Mutant found: {0}", payload);
                    return Response.status(Response.Status.OK).entity("Mutant found.").build();
                } else {
                    LOGGER.log(Level.INFO, "Mutant not found: {0}", payload);
                    return Response.status(Response.Status.FORBIDDEN).entity("Mutant not found.").build();
                }
            } else {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
        } catch (CustomizedException ce) {
            return Response.status(Response.Status.FORBIDDEN).entity(new CustomizedException(ce.getMessage())).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: ", e);
            return Response.status(Response.Status.FORBIDDEN).entity(new CustomizedException("Not valid")).build();
        }
    }
}