package com.ibm.icn.extensions.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.json.java.JSONArray;
import com.ibm.json.java.JSONObject;

/**
 * Servlet implementation for Italian Location EDS.
 * This servlet implements the Update Object Type EDS service for managing
 * Italian provinces and municipalities with hierarchical selection.
 */
public class UpdateObjectTypeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Handles POST requests for updating object type property data.
     * This is called by ICN when opening an entry template.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Get the object type from the request path
        String objectType = request.getPathInfo().substring(1);
        
        System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: objectType=" + objectType);
        
        // Get the request JSON
        InputStream requestInputStream = request.getInputStream();
        JSONObject jsonRequest = JSONObject.parse(requestInputStream);
        
        // Get request parameters
        String requestMode = jsonRequest.get("requestMode").toString();
        JSONArray requestProperties = (JSONArray) jsonRequest.get("properties");
        JSONArray responseProperties = new JSONArray();
        
        // Get property data for this object type
        JSONArray propertyData = getPropertyData(objectType, request.getLocale());
        
        System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: requestMode=" + requestMode);
        System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: requestProperties count=" + requestProperties.size());
        
        // Process each requested property
        for (int i = 0; i < requestProperties.size(); i++) {
            JSONObject requestProperty = (JSONObject) requestProperties.get(i);
            String symbolicName = requestProperty.get("symbolicName").toString();
            
            System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: processing property=" + symbolicName);
            
            // Find the override property in our property data
            JSONObject overrideProperty = findPropertyBySymbolicName(propertyData, symbolicName);
            
            if (overrideProperty != null) {
                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: found override for " + symbolicName);
                
                // Load choices for this property
                JSONObject choiceList = (JSONObject) overrideProperty.get("choiceList");
                if (choiceList != null) {
                    JSONArray choices = null;
                    
                    // Check if this property has dependent properties
                    if (overrideProperty.containsKey("dependentOn")) {
                        String dependentOn = overrideProperty.get("dependentOn").toString();
                        
                        // Get the value of the parent property
                        Object parentValue = null;
                        for (int j = 0; j < requestProperties.size(); j++) {
                            JSONObject prop = (JSONObject) requestProperties.get(j);
                            if (prop.get("symbolicName").toString().equals(dependentOn)) {
                                parentValue = prop.get("value");
                                break;
                            }
                        }
                        
                        // If parent has a value, load dependent choices
                        if (parentValue != null && !parentValue.toString().isEmpty()) {
                            String parentValueStr = parentValue.toString();
                            System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: loading dependent choices for " + symbolicName + " based on " + dependentOn + "=" + parentValueStr);
                            
                            // Load choices based on parent value
                            choices = loadDependentChoices(symbolicName, dependentOn, parentValueStr);
                        }
                    } else {
                        // Load initial choices for properties without dependencies
                        System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: loading initial choices for " + symbolicName);
                        choices = loadInitialChoices(symbolicName);
                    }
                    
                    // Update the choice list if choices were loaded
                    if (choices != null) {
                        choiceList.put("choices", choices);
                    }
                }
                
                responseProperties.add(overrideProperty);
            }
        }
        
        // Send the response
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("properties", responseProperties);
        
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter writer = response.getWriter();
        jsonResponse.serialize(writer);
        writer.flush();
        
        System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: response sent with " + responseProperties.size() + " properties");
    }
    
    /**
     * Loads property data from JSON file for the given object type.
     */
    private JSONArray getPropertyData(String objectType, Locale locale) {
        try {
            // Load the property data file
            String resourcePath = "/" + objectType + "_PropertyData.json";
            InputStream propertyDataStream = this.getClass().getResourceAsStream(resourcePath);
            
            if (propertyDataStream == null) {
                System.err.println("ItalianLocationEDS.UpdateObjectTypeServlet: Property data file not found: " + resourcePath);
                return new JSONArray();
            }
            
            JSONArray jsonPropertyData = JSONArray.parse(propertyDataStream);
            System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded " + jsonPropertyData.size() + " property overrides from " + resourcePath);
            
            return jsonPropertyData;
            
        } catch (Exception e) {
            System.err.println("ItalianLocationEDS.UpdateObjectTypeServlet: Error loading property data: " + e.getMessage());
            e.printStackTrace();
            return new JSONArray();
        }
    }
    
    /**
     * Finds a property by symbolic name in the property data array.
     */
    private JSONObject findPropertyBySymbolicName(JSONArray propertyData, String symbolicName) {
        for (int i = 0; i < propertyData.size(); i++) {
            JSONObject property = (JSONObject) propertyData.get(i);
            if (property.get("symbolicName").toString().equals(symbolicName)) {
                return property;
            }
        }
        return null;
    }
    
    /**
     * Loads dependent choices based on parent property value.
     * For example, loads municipalities based on selected province.
     */
    private JSONArray loadDependentChoices(String propertyName, String parentProperty, String parentValue) {
        JSONArray choices = new JSONArray();
        
        try {
            if (propertyName.equals("Comunedinascita") && parentProperty.equals("Provinciadinascita")) {
                // Load municipalities for the selected province
                InputStream comuniStream = this.getClass().getResourceAsStream("/gi_comuni.json");
                if (comuniStream != null) {
                    JSONArray allComuni = JSONArray.parse(comuniStream);
                    
                    // Filter municipalities by province
                    for (int i = 0; i < allComuni.size(); i++) {
                        JSONObject comune = (JSONObject) allComuni.get(i);
                        String siglaProvincia = comune.get("sigla_provincia").toString();
                        
                        if (siglaProvincia.equals(parentValue)) {
                            JSONObject choice = new JSONObject();
                            choice.put("displayName", comune.get("denominazione_ita"));
                            choice.put("value", comune.get("denominazione_ita"));
                            choices.add(choice);
                        }
                    }
                    
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded " + choices.size() + " municipalities for province " + parentValue);
                }
            }
        } catch (Exception e) {
            System.err.println("ItalianLocationEDS.UpdateObjectTypeServlet: Error loading dependent choices: " + e.getMessage());
            e.printStackTrace();
        }
        
        return choices;
    }
    
    /**
     * Loads initial choices for properties without dependencies.
     * For example, loads all provinces for the province property.
     */
    private JSONArray loadInitialChoices(String propertyName) {
        JSONArray choices = new JSONArray();
        
        try {
            if (propertyName.equals("Provinciadinascita")) {
                // Load all provinces
                InputStream provinceStream = this.getClass().getResourceAsStream("/gi_province.json");
                if (provinceStream != null) {
                    JSONArray allProvince = JSONArray.parse(provinceStream);
                    
                    // Convert to choice list format
                    for (int i = 0; i < allProvince.size(); i++) {
                        JSONObject provincia = (JSONObject) allProvince.get(i);
                        JSONObject choice = new JSONObject();
                        choice.put("displayName", provincia.get("denominazione_provincia"));
                        choice.put("value", provincia.get("sigla_provincia"));
                        choices.add(choice);
                    }
                    
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded " + choices.size() + " provinces");
                }
            }
        } catch (Exception e) {
            System.err.println("ItalianLocationEDS.UpdateObjectTypeServlet: Error loading initial choices: " + e.getMessage());
            e.printStackTrace();
        }
        
        return choices;
    }
}

// Made with Bob
