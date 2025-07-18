package com.otterdev.application.handler.ops;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Path;

@Path("/sale")
@ApplicationScoped
public class SaleHandler {
    
    // This class will handle sale-related operations
    // It will use InternalSaleUsecase to perform operations like creating, updating, deleting sales, etc.
    
    // Inject InternalSaleUsecase here and implement methods for handling sale operations
    // For example:
    // - createSale
    // - updateSale
    // - deleteSale
    // - getSaleById
    // - getAllSalesByUserId
    // - addFileToSaleById
    // - deleteFileFromSaleById
    // - getAllImagesRelatedById
    // - getAllPdfRelatedById
    // - getAllOtherFileRelatedById
    // - getAllFilesRelatedById

    // Each method will return a Uni<Response> similar to the AuthHandler and UserHandler classes.

}
