package com.osvaldo;

import com.osvaldo.exception.ValidationsException;
import com.osvaldo.service.MutantService;
import com.osvaldo.utils.Validations;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/mutant")
public class GeneticController {
    private static final Logger LOGGER = Logger.getLogger(GeneticController.class.getName());
    @Inject
    MutantService mutantService;
    @Inject
    Validations validations;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response execute(String payload) {
        LOGGER.log(Level.INFO, "Payload => {0}", payload);
        Response response;
        try {
            response = mutantService.isMutant(payload) ? Response.status(Response.Status.OK).build() : Response.status(Response.Status.FORBIDDEN).build();
        } catch (ValidationsException mve) {
            LOGGER.log(Level.SEVERE, "Validation exception: {0}", mve.getMessage());
            response = Response.status(Response.Status.FORBIDDEN).build();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error: ", e);
            response = Response.status(Response.Status.FORBIDDEN).build();
        }
        return response;
    }
}