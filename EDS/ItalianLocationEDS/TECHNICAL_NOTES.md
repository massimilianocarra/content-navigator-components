# Note Tecniche - Italian Location EDS Plugin

## Riepilogo Finale

✅ **Plugin EDS completato e funzionante!**

Il plugin fornisce combo box dinamiche per la selezione di province e comuni italiani in IBM Content Navigator, con selezione gerarchica (la scelta della provincia filtra i comuni).

## Problemi Risolti Durante lo Sviluppo

### Problema 1: Formato Risposta JSON GetObjectTypesServlet
**Sintomo**: edsPlugin riceveva 404 anche se il servlet rispondeva correttamente con curl.

**Causa**: Il servlet restituiva `{"objectTypes":[...]}` invece dell'array diretto `[...]`

**Soluzione**: Modificato per restituire direttamente il JSONArray come nell'esempio IBM.

### Problema 2: URL Pattern dei Servlet (CRITICO)
**Sintomo**: edsPlugin continuava a ricevere 404 anche dopo la correzione del formato JSON.

**Causa**: I servlet usavano URL pattern personalizzati (`/GetObjectTypesServlet` e `/UpdateObjectTypeServlet/*`) invece degli URL standard IBM.

**Soluzione**: Cambiati gli URL pattern in:
- `/types` per GetObjectTypesServlet
- `/type/*` per UpdateObjectTypeServlet

**Nota Importante**: edsPlugin è hardcoded per chiamare questi URL specifici. Non è possibile usare URL personalizzati.

## Analisi dell'Esempio IBM

### Scoperte Chiave dall'Esempio Ufficiale

Dopo aver analizzato l'esempio ufficiale IBM (`sampleEDSService`), abbiamo identificato le seguenti differenze critiche rispetto alla nostra implementazione iniziale:

#### 1. Architettura: HttpServlet vs PluginService

**Implementazione Errata (Prima)**:
```java
public class ItalianLocationEDS extends PluginService {
    @Override
    public void execute(PluginServiceCallbacks callbacks,
                       HttpServletRequest request,
                       HttpServletResponse response) {
        // Codice del servizio
    }
}
```

**Problema**: I `PluginService` non sono accessibili direttamente via HTTP. Sono pensati per essere chiamati internamente da ICN, non come endpoint REST.

**Implementazione Corretta (Ora)**:
```java
public class UpdateObjectTypeServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        // Codice del servizio
    }
}
```

**Soluzione**: Usare `HttpServlet` standard registrato in `web.xml`, accessibile via HTTP come qualsiasi servlet Java EE.

#### 2. Registrazione del Servlet

**Prima**: Tentativo di registrare tramite `getServices()` nel plugin
```java
@Override
public PluginService[] getServices() {
    return new PluginService[] {
        new ItalianLocationEDS()
    };
}
```

**Ora**: Registrazione in `web.xml`
```xml
<servlet>
    <servlet-name>UpdateObjectTypeServlet</servlet-name>
    <servlet-class>com.ibm.icn.extensions.servlets.UpdateObjectTypeServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>UpdateObjectTypeServlet</servlet-name>
    <url-pattern>/UpdateObjectTypeServlet/*</url-pattern>
</servlet-mapping>
```

#### 3. URL Pattern del Servizio

**Prima**: URL non funzionante
```
/navigator/plugin/ItalianLocationEDSPlugin/ItalianLocationEDS?action=getProvinces
```

**Ora**: URL corretto secondo pattern IBM
```
/navigator/plugin/ItalianLocationEDSPlugin/UpdateObjectTypeServlet/CartellaPersona
```

Pattern: `/plugin/<pluginId>/<servletPath>/<objectType>`

#### 4. Struttura della Richiesta JSON

**Richiesta da ICN**:
```json
{
  "repositoryId": "FNOS",
  "objectId": "idd_XXXXXXXX",
  "requestMode": "initialNewObject",
  "properties": [
    {
      "symbolicName": "Provinciadinascita",
      "value": ""
    },
    {
      "symbolicName": "Comunedinascita",
      "value": ""
    }
  ],
  "clientContext": {
    "userId": "admin",
    "locale": "it-IT",
    "desktop": "admin"
  }
}
```

**Risposta del Servlet**:
```json
{
  "properties": [
    {
      "symbolicName": "Provinciadinascita",
      "displayMode": "readwrite",
      "required": true,
      "choiceList": {
        "displayName": "Seleziona Provincia",
        "choices": [
          {"displayName": "Agrigento", "value": "AG"},
          {"displayName": "Alessandria", "value": "AL"},
          ...
        ]
      },
      "hasDependentProperties": true
    },
    {
      "symbolicName": "Comunedinascita",
      "displayMode": "readwrite",
      "required": true,
      "choiceList": {
        "displayName": "Seleziona Comune",
        "choices": []
      }
    }
  ]
}
```

#### 5. Metodo getPropertyData

**Implementazione IBM**:
```java
private JSONArray getPropertyData(String objectType, Locale locale) {
    // Carica <ObjectType>_PropertyData.json dalla cartella resources
    String resourcePath = "/" + objectType + "_PropertyData.json";
    InputStream propertyDataStream = this.getClass()
        .getResourceAsStream(resourcePath);
    
    if (propertyDataStream == null) {
        return new JSONArray();
    }
    
    return JSONArray.parse(propertyDataStream);
}
```

Questo metodo:
- Carica dinamicamente il file JSON per ogni tipo di oggetto
- Usa il pattern `<ObjectType>_PropertyData.json`
- Restituisce la configurazione delle proprietà

#### 6. Gestione delle Dipendenze

**Proprietà con Dipendenze**:
```json
{
  "symbolicName": "Comunedinascita",
  "dependentOn": "Provinciadinascita",
  "choiceList": {
    "displayName": "Seleziona Comune",
    "choices": []
  }
}
```

**Logica nel Servlet**:
```java
if (overrideProperty.containsKey("dependentOn")) {
    String dependentOn = overrideProperty.get("dependentOn").toString();
    
    // Trova il valore della proprietà padre
    Object parentValue = null;
    for (int j = 0; j < requestProperties.size(); j++) {
        JSONObject prop = (JSONObject) requestProperties.get(j);
        if (prop.get("symbolicName").toString().equals(dependentOn)) {
            parentValue = prop.get("value");
            break;
        }
    }
    
    // Se il padre ha un valore, carica le scelte dipendenti
    if (parentValue != null && !parentValue.toString().isEmpty()) {
        JSONArray choices = loadDependentChoices(
            symbolicName, dependentOn, parentValue.toString()
        );
        choiceList.put("choices", choices);
    }
}
```

## Flusso di Esecuzione Dettagliato

### 1. Inizializzazione Plugin

```
ICN Server Startup
    ↓
Load ItalianLocationPlugin.jar
    ↓
Register ResponseFilter (OpenContentClassResponseFilter)
    ↓
Register Servlet (UpdateObjectTypeServlet) via web.xml
    ↓
Plugin Ready
```

### 2. Apertura Entry Template

```
User: "Nuovo Documento" → CartellaPersona
    ↓
ICN → POST /p8/openContentClass
    ↓
OpenContentClassResponseFilter.filter()
    ↓
Legge ObjectTypes.json
    ↓
Trova "CartellaPersona" nella lista
    ↓
Legge CartellaPersona_PropertyData.json
    ↓
Modifica JSON response:
  - Aggiunge "dataSourceUrl" alle proprietà
  - Configura "hasDependentProperties"
    ↓
ICN riceve response modificata
    ↓
ICN → POST /plugin/.../UpdateObjectTypeServlet/CartellaPersona
    ↓
UpdateObjectTypeServlet.doPost()
    ↓
Carica province.json
    ↓
Restituisce choices per Provinciadinascita
    ↓
ICN mostra combo box con province
```

### 3. Selezione Provincia

```
User: Seleziona "MI" (Milano)
    ↓
ICN → POST /plugin/.../UpdateObjectTypeServlet/CartellaPersona
  Body: {
    "properties": [
      {"symbolicName": "Provinciadinascita", "value": "MI"},
      {"symbolicName": "Comunedinascita", "value": ""}
    ]
  }
    ↓
UpdateObjectTypeServlet.doPost()
    ↓
Trova dependentOn: "Provinciadinascita"
    ↓
Legge valore: "MI"
    ↓
loadDependentChoices("Comunedinascita", "Provinciadinascita", "MI")
    ↓
Carica comuni.json
    ↓
Filtra: comune.sigla_provincia == "MI"
    ↓
Restituisce 134 comuni della provincia di Milano
    ↓
ICN aggiorna combo box Comunedinascita
```

## Configurazione delle Proprietà

### File: CartellaPersona_PropertyData.json

```json
[
  {
    "symbolicName": "Provinciadinascita",
    "displayMode": "readwrite",
    "required": true,
    "choiceList": {
      "displayName": "Seleziona Provincia",
      "choices": []
    },
    "hasDependentProperties": true
  },
  {
    "symbolicName": "Comunedinascita",
    "displayMode": "readwrite",
    "required": true,
    "choiceList": {
      "displayName": "Seleziona Comune",
      "choices": []
    },
    "dependentOn": "Provinciadinascita"
  }
]
```

**Campi Chiave**:

- `symbolicName`: Nome simbolico della proprietà in FileNet
- `displayMode`: Modalità di visualizzazione (`readwrite`, `readonly`, `hidden`)
- `required`: Se il campo è obbligatorio
- `choiceList`: Configurazione della lista di scelte
  - `displayName`: Etichetta mostrata all'utente
  - `choices`: Array di scelte (popolato dinamicamente)
- `hasDependentProperties`: Indica che altre proprietà dipendono da questa
- `dependentOn`: Nome della proprietà padre da cui dipende

## Struttura dei Dati JSON

### province.json
```json
[
  {
    "nome": "Agrigento",
    "sigla": "AG",
    "regione": "Sicilia"
  },
  ...
]
```

### comuni.json
```json
[
  {
    "nome": "Abbadia Cerreto",
    "codice_catastale": "A004",
    "sigla_provincia": "LO",
    "cap": "26834"
  },
  ...
]
```

## Vantaggi dell'Approccio HttpServlet

1. **Standard Java EE**: Usa tecnologie standard, ben documentate
2. **Accessibilità HTTP**: Il servlet è accessibile via HTTP standard
3. **Debugging Facilitato**: Possibile testare con curl o Postman
4. **Compatibilità**: Segue l'architettura ufficiale IBM
5. **Manutenibilità**: Codice più chiaro e manutenibile
6. **Scalabilità**: Può gestire richieste concorrenti

## Limitazioni e Considerazioni

### Performance

- **Caricamento File JSON**: I file vengono caricati ad ogni richiesta
- **Ottimizzazione Possibile**: Implementare caching in memoria
- **Dimensione Dati**: 7.904 comuni = ~500KB JSON

### Sicurezza

- **Autenticazione**: Gestita da ICN (sessione utente)
- **Autorizzazione**: Verificare permessi utente se necessario
- **Input Validation**: Validare sempre i parametri in input

### Estensibilità

Per aggiungere nuove classi documentali:

1. Creare `<ClassName>_PropertyData.json`
2. Aggiungere la classe in `ObjectTypes.json`
3. Implementare logica specifica in `loadDependentChoices()` se necessario

## Riferimenti

- IBM Content Navigator 3.2.0 Documentation
- IBM ECM Samples: https://github.com/ibm-ecm/ibm-content-navigator-samples
- Sample EDS Service: https://github.com/ibm-ecm/ibm-content-navigator-samples/tree/master/sampleEDSService
- Java Servlet Specification 3.0

---

**Autore**: Bob (AI Assistant)  
**Data**: 15 Maggio 2026  
**Versione**: 1.0