package com.ibm.icn.extensions.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Servlet for getting the list of object types that use EDS.
 * This servlet is called by the edsPlugin to determine which classes
 * should have their properties enhanced with external data.
 */
public class GetObjectTypesServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles GET requests to retrieve the list of object types.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("ItalianLocationEDS.GetObjectTypesServlet: Request received");
        
        try {
            // Load ObjectTypes.json from classpath
            InputStream objectTypesStream = this.getClass().getResourceAsStream("/ObjectTypes.json");
            
            if (objectTypesStream == null) {
                System.err.println("ItalianLocationEDS.GetObjectTypesServlet: ObjectTypes.json not found");
                sendErrorResponse(response, "ObjectTypes.json not found");
                return;
            }
            
            // Parse JSON - returns array directly like IBM sample
            JSONArray objectTypes = JSONArray.parse(objectTypesStream);
            
            System.out.println("ItalianLocationEDS.GetObjectTypesServlet: Loaded " + objectTypes.size() + " object types");
            
            // Send response - return array directly, not wrapped in object
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            PrintWriter writer = response.getWriter();
            objectTypes.serialize(writer);
            writer.flush();
            
            System.out.println("ItalianLocationEDS.GetObjectTypesServlet: Response sent successfully");
            
        } catch (Exception e) {
            System.err.println("ItalianLocationEDS.GetObjectTypesServlet: Error processing request");
            e.printStackTrace();
            sendErrorResponse(response, "Error: " + e.getMessage());
        }
    }
    
    /**
     * Sends an error response.
     */
    private void sendErrorResponse(HttpServletResponse response, String errorMessage) throws IOException {
        JSONObject errorResponse = new JSONObject();
        errorResponse.put("error", errorMessage);
        
        response.setStatus(500);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter writer = response.getWriter();
        errorResponse.serialize(writer);
        writer.flush();
    }
}

// Made with Bob
