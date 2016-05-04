package com.walmart.ticket.resources;

import com.walmart.ticket.dao.ITicketDao;
import com.walmart.ticket.exception.ErrorCategory;
import com.walmart.ticket.exception.ServiceErrorCode;
import com.walmart.ticket.exception.ServiceRuntimeException;
import com.walmart.ticket.model.*;
import com.walmart.ticket.services.ITicketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


/**
 * Created by bmaria001c on 4/30/16.
 */
@Api(value="/ticket",description = "APIs to hold,reserve and check availability of seats for ticketing system")
@Path("/ticket")
@Component
public class TicketResource {
    @Autowired
    private ITicketService ticketService;

    @ApiOperation(value = "Get Venue Detail",notes = "Provides Venue details with levels and seats structure",response = List.class )
    @ApiResponses(value = { @ApiResponse(code = 200 ,message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Unexpected server error")})
    @GET
    @Path("/venuedetail")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVenueDetails(){
        List<VenueDetail> venueDetails = ticketService.getVenueDetail().toBlocking().single();
        return Response.ok(venueDetails).build();
    }
    @ApiOperation(value = "Get seats availability",notes = "Provides seat availability for a given level id",response = AvailableStatus.class )
    @ApiResponses(value = { @ApiResponse(code = 200 ,message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Unexpected server error")})
    @GET
    @Path("/venue/{levelId}/availableseats")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableSeats(@PathParam("levelId") int levelId){
        AvailableStatus availabilityStatus  = ticketService.getAvailableSeats(levelId).toBlocking().single();
        return Response.ok(availabilityStatus).build();
    }
    @ApiOperation(value = "Hold seats",notes = "Holds seats based on availability",response = Void.class )
    @ApiResponses(value = { @ApiResponse(code = 204 ,message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Unexpected server error")})
    @POST
    @Path("/hold")
    @Produces(MediaType.APPLICATION_JSON)
    public Response holdTicket(Reservation reservation){
        int status  = ticketService.holdTicket(reservation);
        return Response.noContent().build();
    }
    @ApiOperation(value = "Find And Hold",notes = "Finds availability of seats within the level specified and Hold",response = Void.class )
    @ApiResponses(value = { @ApiResponse(code = 204 ,message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Unexpected server error")})
    @POST
    @Path("/findandhold")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAndHold(FindAndHold findAndHold){
        if(findAndHold.getMaxLevel() < findAndHold.getMinLevel()){
            throw new ServiceRuntimeException(ErrorCategory.DATA, ServiceErrorCode.INVALID_INPUT_FOR_LEVEL);
        }
        Venue[] venues = Venue.getVenues(findAndHold.getMinLevel(),findAndHold.getMaxLevel());
        ticketService.findAndHold(findAndHold.getEmailId(),findAndHold.getNumberOfSeats(),venues);
        return Response.noContent().build();
    }
    @GET
    @Path("/onhold/{emailId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response holdTicket(@PathParam("emailId") String emailId){
        List<Reservation> reservation = ticketService.getTicketOnHold(emailId);
        return Response.ok(reservation).build();
    }
    @ApiOperation(value = "Reserve seats",notes = "Reserves seats for the hold specified",response = Void.class )
    @ApiResponses(value = { @ApiResponse(code = 204 ,message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Unexpected server error")})
    @PUT
    @Path("/reserve/{reservationId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reserveTicket(@PathParam("reservationId") int reservationId){
        ticketService.reserveTicket(reservationId);
        return Response.noContent().build();
    }

}
