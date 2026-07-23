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
import com.ibm.icn.extensions.utils.FiscalCodeCalculator;

/**
 * Servlet implementation for Italian Location EDS.
 * This servlet implements the Update Object Type EDS service for managing
 * Italian provinces and municipalities with hierarchical selection.
 */
public class UpdateObjectTypeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Default value for foreign state fields (can be customized via ForeignStateConfig.json)
    private static String foreignStateDefaultValue = "Stato di nascita estero";
    
    // Configurable property names (can be customized via PropertyNamesConfig.json)
    private static String statePropertyName = "Statodinascita";
    private static String provincePropertyName = "Provinciadinascita";
    private static String municipalityPropertyName = "Comunedinascita";
    
    // Configurable fiscal code property names (can be customized via FiscalCodeConfig.json)
    private static String firstNamePropertyName = "NomePersona";
    private static String lastNamePropertyName = "CognomePersona";
    private static String birthDatePropertyName = "DatadiNascita";
    private static String birthStatePropertyName = "Statodinascita";
    private static String birthProvincePropertyName = "Provinciadinascita";
    private static String birthMunicipalityPropertyName = "Comunedinascita";
    private static String genderPropertyName = "SessoPersona";
    private static String fiscalCodePropertyName = "CodiceFiscale";
    
    // Static initializer to load configuration
    static {
        loadForeignStateConfig();
        loadPropertyNamesConfig();
        loadFiscalCodeConfig();
    }
    
    /**
     * Loads the foreign state configuration from ForeignStateConfig.json
     */
    private static void loadForeignStateConfig() {
        try {
            InputStream configStream = UpdateObjectTypeServlet.class.getResourceAsStream("/ForeignStateConfig.json");
            if (configStream != null) {
                JSONObject config = JSONObject.parse(configStream);
                if (config.containsKey("foreignStateDefaultValue")) {
                    foreignStateDefaultValue = config.get("foreignStateDefaultValue").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded foreignStateDefaultValue=" + foreignStateDefaultValue);
                }
            } else {
                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: ForeignStateConfig.json not found, using default value");
            }
        } catch (Exception e) {
            System.err.println("ItalianLocationEDS.UpdateObjectTypeServlet: Error loading ForeignStateConfig.json: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads the property names configuration from PropertyNamesConfig.json
     */
    private static void loadPropertyNamesConfig() {
        try {
            InputStream configStream = UpdateObjectTypeServlet.class.getResourceAsStream("/PropertyNamesConfig.json");
            if (configStream != null) {
                JSONObject config = JSONObject.parse(configStream);
                if (config.containsKey("statePropertyName")) {
                    statePropertyName = config.get("statePropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded statePropertyName=" + statePropertyName);
                }
                if (config.containsKey("provincePropertyName")) {
                    provincePropertyName = config.get("provincePropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded provincePropertyName=" + provincePropertyName);
                }
                if (config.containsKey("municipalityPropertyName")) {
                    municipalityPropertyName = config.get("municipalityPropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded municipalityPropertyName=" + municipalityPropertyName);
                }
            } else {
                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: PropertyNamesConfig.json not found, using default names");
            }
        } catch (Exception e) {
            System.err.println("ItalianLocationEDS.UpdateObjectTypeServlet: Error loading PropertyNamesConfig.json: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Loads the fiscal code configuration from FiscalCodeConfig.json
     */
    private static void loadFiscalCodeConfig() {
        try {
            InputStream configStream = UpdateObjectTypeServlet.class.getResourceAsStream("/FiscalCodeConfig.json");
            if (configStream != null) {
                JSONObject config = JSONObject.parse(configStream);
                if (config.containsKey("firstNamePropertyName")) {
                    firstNamePropertyName = config.get("firstNamePropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded firstNamePropertyName=" + firstNamePropertyName);
                }
                if (config.containsKey("lastNamePropertyName")) {
                    lastNamePropertyName = config.get("lastNamePropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded lastNamePropertyName=" + lastNamePropertyName);
                }
                if (config.containsKey("birthDatePropertyName")) {
                    birthDatePropertyName = config.get("birthDatePropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded birthDatePropertyName=" + birthDatePropertyName);
                }
                if (config.containsKey("birthStatePropertyName")) {
                    birthStatePropertyName = config.get("birthStatePropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded birthStatePropertyName=" + birthStatePropertyName);
                }
                if (config.containsKey("birthProvincePropertyName")) {
                    birthProvincePropertyName = config.get("birthProvincePropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded birthProvincePropertyName=" + birthProvincePropertyName);
                }
                if (config.containsKey("birthMunicipalityPropertyName")) {
                    birthMunicipalityPropertyName = config.get("birthMunicipalityPropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded birthMunicipalityPropertyName=" + birthMunicipalityPropertyName);
                }
                if (config.containsKey("genderPropertyName")) {
                    genderPropertyName = config.get("genderPropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded genderPropertyName=" + genderPropertyName);
                }
                if (config.containsKey("fiscalCodePropertyName")) {
                    fiscalCodePropertyName = config.get("fiscalCodePropertyName").toString();
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded fiscalCodePropertyName=" + fiscalCodePropertyName);
                }
            } else {
                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: FiscalCodeConfig.json not found, using default names");
            }
        } catch (Exception e) {
            System.err.println("ItalianLocationEDS.UpdateObjectTypeServlet: Error loading FiscalCodeConfig.json: " + e.getMessage());
            e.printStackTrace();
        }
    }

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
                        
                        // Special handling for Provincia and Comune based on Stato
                        if (symbolicName.equals(provincePropertyName)) {
                            // Get Stato value
                            String statoValue = getPropertyValue(requestProperties, statePropertyName);
                            
                            // Provincia is enabled ONLY when Stato = Italia
                            if (statoValue == null || statoValue.isEmpty()) {
                                // Stato is empty, disable Provincia and clear value
                                overrideProperty.put("displayMode", "readonly");
                                overrideProperty.put("value", "");
                                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Provincia disabled because Stato is empty");
                            } else if (!statoValue.equals("ITA")) {
                                // Stato is foreign (not Italia), provide single choice with foreign state value
                                overrideProperty.put("displayMode", "readwrite");
                                overrideProperty.put("value", foreignStateDefaultValue);
                                // Create a single-choice list with the foreign state value
                                choices = new JSONArray();
                                JSONObject foreignChoice = new JSONObject();
                                foreignChoice.put("displayName", foreignStateDefaultValue);
                                foreignChoice.put("value", foreignStateDefaultValue);
                                choices.add(foreignChoice);
                                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Provincia set to single choice '" + foreignStateDefaultValue + "' because Stato is foreign (Stato=" + statoValue + ")");
                            } else {
                                // Stato is Italia, enable Provincia and load all provinces
                                overrideProperty.put("displayMode", "readwrite");
                                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: loading all provinces because Stato = Italia");
                                choices = loadInitialChoices(symbolicName);
                            }
                        } else if (symbolicName.equals(municipalityPropertyName)) {
                            // Get Stato and Provincia values
                            String statoValue = getPropertyValue(requestProperties, statePropertyName);
                            String provinciaValue = getPropertyValue(requestProperties, provincePropertyName);
                            
                            // Comune is enabled ONLY when Stato = Italia AND Provincia has a value
                            if (statoValue == null || statoValue.isEmpty()) {
                                // Stato is empty, disable Comune and clear value
                                overrideProperty.put("displayMode", "readonly");
                                overrideProperty.put("value", "");
                                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Comune disabled because Stato is empty");
                            } else if (!statoValue.equals("ITA")) {
                                // Stato is foreign (not Italia), provide single choice with foreign state value
                                overrideProperty.put("displayMode", "readwrite");
                                overrideProperty.put("value", foreignStateDefaultValue);
                                // Create a single-choice list with the foreign state value
                                choices = new JSONArray();
                                JSONObject foreignChoice = new JSONObject();
                                foreignChoice.put("displayName", foreignStateDefaultValue);
                                foreignChoice.put("value", foreignStateDefaultValue);
                                choices.add(foreignChoice);
                                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Comune set to single choice '" + foreignStateDefaultValue + "' because Stato is foreign (Stato=" + statoValue + ")");
                            } else if (provinciaValue == null || provinciaValue.isEmpty()) {
                                // Stato is Italia but Provincia is empty, disable Comune and clear value
                                overrideProperty.put("displayMode", "readonly");
                                overrideProperty.put("value", "");
                                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Comune disabled because Provincia is empty");
                            } else {
                                // Stato is Italia AND Provincia has a value, enable Comune and load municipalities
                                overrideProperty.put("displayMode", "readwrite");
                                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: loading municipalities for Provincia=" + provinciaValue);
                                choices = loadDependentChoices(symbolicName, "Provinciadinascita", provinciaValue);
                            }
                        } else {
                            // Normal dependent property handling
                            if (parentValue != null && !parentValue.toString().isEmpty()) {
                                String parentValueStr = parentValue.toString();
                                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: loading dependent choices for " + symbolicName + " based on " + dependentOn + "=" + parentValueStr);
                                
                                // Load choices based on parent value
                                choices = loadDependentChoices(symbolicName, dependentOn, parentValueStr);
                            }
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
        
        // Calculate fiscal code if all required fields are present
        calculateAndSetFiscalCode(requestProperties, responseProperties, propertyData);
        
        // Send the response - edsPlugin expects {"properties":[...]} wrapped format
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
            if (propertyName.equals(municipalityPropertyName) && parentProperty.equals(provincePropertyName)) {
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
            if (propertyName.equals(statePropertyName)) {
                // Load all states
                InputStream statiStream = this.getClass().getResourceAsStream("/gi_stati.json");
                if (statiStream != null) {
                    JSONArray allStati = JSONArray.parse(statiStream);
                    
                    // The file is already in the correct format with displayName and value
                    for (int i = 0; i < allStati.size(); i++) {
                        choices.add(allStati.get(i));
                    }
                    
                    System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Loaded " + choices.size() + " states");
                }
            } else if (propertyName.equals(provincePropertyName)) {
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
    
    /**
     * Helper method to get property value from request properties array.
     */
    private String getPropertyValue(JSONArray properties, String symbolicName) {
        for (int i = 0; i < properties.size(); i++) {
            JSONObject prop = (JSONObject) properties.get(i);
            if (prop.get("symbolicName").toString().equals(symbolicName)) {
                Object value = prop.get("value");
                return value != null ? value.toString() : null;
            }
        }
        return null;
    }
    
    /**
     * Calculates the fiscal code if all required fields are present and adds it to response properties.
     * Always adds the CodiceFiscale property to the response (empty string if not yet calculable)
     * so that ICN updates the field in the form on every EDS call.
     */
    private void calculateAndSetFiscalCode(JSONArray requestProperties, JSONArray responseProperties, JSONArray propertyData) {
        
        // Always ensure CodiceFiscale is in the response so ICN refreshes the field in the form.
        // Find or build the property object from propertyData.
        JSONObject fiscalCodeProperty = findPropertyBySymbolicName(propertyData, fiscalCodePropertyName);
        if (fiscalCodeProperty == null) {
            fiscalCodeProperty = new JSONObject();
            fiscalCodeProperty.put("symbolicName", fiscalCodePropertyName);
            fiscalCodeProperty.put("displayMode", "readonly");
        }
        
        // Default to empty — will be overwritten if calculation succeeds
        fiscalCodeProperty.put("value", "");
        
        try {
            // Get all required values
            String firstName = getPropertyValue(requestProperties, firstNamePropertyName);
            String lastName = getPropertyValue(requestProperties, lastNamePropertyName);
            String birthDate = getPropertyValue(requestProperties, birthDatePropertyName);
            System.out.println("ItalianLocationEDS.FiscalCode: RAW birthDate string from ICN='" + birthDate + "'");
            String birthState = getPropertyValue(requestProperties, birthStatePropertyName);
            String birthMunicipality = getPropertyValue(requestProperties, birthMunicipalityPropertyName);
            String gender = getPropertyValue(requestProperties, genderPropertyName);
            
            System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Checking fiscal code calculation");
            System.out.println("  firstName=" + firstName + ", lastName=" + lastName
                + ", birthDate=" + birthDate + ", birthState=" + birthState
                + ", birthMunicipality=" + birthMunicipality + ", gender=" + gender);
            
            // Check if all required fields are present
            if (firstName == null || firstName.trim().isEmpty() ||
                lastName == null || lastName.trim().isEmpty() ||
                birthDate == null || birthDate.trim().isEmpty() ||
                birthState == null || birthState.trim().isEmpty() ||
                birthMunicipality == null || birthMunicipality.trim().isEmpty() ||
                gender == null || gender.trim().isEmpty()) {
                System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Not all required fields present, skipping fiscal code calculation");
            } else {
                // Determine birth place code (Belfiore code)
                String birthPlaceCode = null;
                
                if (birthState.equals("ITA")) {
                    InputStream comuniStream = this.getClass().getResourceAsStream("/gi_comuni.json");
                    if (comuniStream != null) {
                        JSONArray allComuni = JSONArray.parse(comuniStream);
                        birthPlaceCode = FiscalCodeCalculator.findMunicipalityBelfioreCode(birthMunicipality, allComuni);
                    }
                } else {
                    InputStream statiStream = this.getClass().getResourceAsStream("/gi_stati.json");
                    if (statiStream != null) {
                        JSONArray allStati = JSONArray.parse(statiStream);
                        birthPlaceCode = FiscalCodeCalculator.findCountryTaxCode(birthState, allStati);
                    }
                }
                
                if (birthPlaceCode == null) {
                    System.err.println("ItalianLocationEDS.UpdateObjectTypeServlet: Could not determine birth place code for municipality/state: " + birthMunicipality + "/" + birthState);
                } else {
                    String fiscalCode = FiscalCodeCalculator.calculate(
                        lastName, firstName, birthDate, gender, birthMunicipality, birthPlaceCode
                    );
                    
                    if (fiscalCode != null) {
                        fiscalCodeProperty.put("value", fiscalCode);
                        System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: Calculated fiscal code=" + fiscalCode);
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("ItalianLocationEDS.UpdateObjectTypeServlet: Error calculating fiscal code: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Add CodiceFiscale to response if not already present (it may have been added
        // earlier in the main loop if CodiceFiscale was in requestProperties)
        boolean alreadyInResponse = false;
        for (int i = 0; i < responseProperties.size(); i++) {
            JSONObject prop = (JSONObject) responseProperties.get(i);
            if (prop.get("symbolicName").toString().equals(fiscalCodePropertyName)) {
                // Update value in the existing entry instead of adding a duplicate
                prop.put("value", fiscalCodeProperty.get("value"));
                alreadyInResponse = true;
                break;
            }
        }
        if (!alreadyInResponse) {
            responseProperties.add(fiscalCodeProperty);
        }
        
        System.out.println("ItalianLocationEDS.UpdateObjectTypeServlet: CodiceFiscale in response, value=" + fiscalCodeProperty.get("value"));
    }
}

// Made with Bob
