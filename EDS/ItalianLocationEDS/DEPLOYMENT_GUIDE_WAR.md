# Guida al Deployment - Italian Location EDS (Architettura Corretta)

## Architettura IBM EDS

L'External Data Service di IBM richiede **DUE componenti separati**:

1. **WAR File** (`ItalianLocationEDS.war`) - Applicazione web con i servlet EDS
2. **edsPlugin.jar** - Plugin ICN che configura l'integrazione con il WAR

```
┌─────────────────────────────────────────────────────────────┐
│                    IBM Content Navigator                     │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │           edsPlugin.jar (IBM Plugin)                │    │
│  │  - Configura URL del servizio EDS                   │    │
│  │  - Intercetta richieste openContentClass            │    │
│  │  - Modifica response per abilitare choice lists     │    │
│  └────────────────────────────────────────────────────┘    │
│                          │                                   │
│                          │ HTTP Request                      │
│                          ▼                                   │
└─────────────────────────────────────────────────────────────┘
                           │
                           │
                           ▼
┌─────────────────────────────────────────────────────────────┐
│              WebSphere Application Server                    │
│                                                              │
│  ┌────────────────────────────────────────────────────┐    │
│  │      ItalianLocationEDS.war (Nostra Applicazione)   │    │
│  │                                                      │    │
│  │  Servlets:                                           │    │
│  │  - GetObjectTypesServlet                             │    │
│  │  - UpdateObjectTypeServlet                           │    │
│  │                                                      │    │
│  │  Resources:                                          │    │
│  │  - ObjectTypes.json                                  │    │
│  │  - CartellaPersona_PropertyData.json                 │    │
│  │  - gi_province.json (107 province)                   │    │
│  │  - gi_comuni.json (7,904 comuni)                     │    │
│  └────────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────────┘
```

## File Generati

### 1. WAR File
- **Percorso**: `dist/ItalianLocationEDS.war`
- **Contenuto**:
  - Servlet: `UpdateObjectTypeServlet.class`
  - Configurazione: `WEB-INF/web.xml`
  - Dati: JSON files (province, comuni, configurazioni)

### 2. Plugin JAR (Opzionale - se vogliamo personalizzare)
- **Percorso**: `dist/ItalianLocationPlugin.jar`
- **Nota**: Possiamo usare l'`edsPlugin.jar` di IBM invece di crearne uno custom

## Procedura di Deployment

### Fase 1: Deploy del WAR in WebSphere

1. **Accedi alla Console Amministrativa di WebSphere**
   ```
   https://rocky.example.com:9043/ibm/console
   ```

2. **Deploy dell'Applicazione**
   - Vai su: **Applications** → **New Application** → **New Enterprise Application**
   - Carica il file: `ItalianLocationEDS.war`
   - Context root: `/ItalianLocationEDS`
   - Clicca **Next** e segui il wizard
   - **Save** e **Synchronize**

3. **Avvia l'Applicazione**
   - Vai su: **Applications** → **Application Types** → **WebSphere enterprise applications**
   - Seleziona `ItalianLocationEDS`
   - Clicca **Start**

4. **Verifica il Deployment**
   ```bash
   curl -k https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/CartellaPersona \
     -X POST \
     -H "Content-Type: application/json" \
     -d '{
       "repositoryId": "ECM",
       "objectId": "test",
       "requestMode": "initialNewObject",
       "properties": [
         {"symbolicName": "Provinciadinascita", "value": ""},
         {"symbolicName": "Comunedinascita", "value": ""}
       ]
     }'
   ```
   
   **Risposta attesa**: JSON con 107 province italiane

### Fase 2: Configurazione del Plugin edsPlugin

1. **Localizza edsPlugin.jar**
   
   Il file si trova in:
   ```
   /opt/IBM/ECMClient/plugins/edsPlugin.jar
   ```
   
   O su Windows:
   ```
   C:\Program Files\IBM\ECMClient\plugins\edsPlugin.jar
   ```

2. **Carica il Plugin in ICN**
   
   - Accedi alla console admin ICN
   - Vai su: **Plug-ins** → **New Plug-in**
   - Carica: `edsPlugin.jar`

3. **Configura il Plugin**
   
   Dopo il caricamento, configura:
   
   **URL del Servizio EDS**:
   ```
   https://rocky.example.com:20002/ItalianLocationEDS
   ```
   
   Questo è l'URL base dove è deployato il WAR.

4. **Salva e Riavvia**
   - Clicca **Save and Close**
   - Riavvia il server applicativo ICN

### Fase 3: Verifica Funzionamento

1. **Test del Servlet**
   ```bash
   curl -k https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/CartellaPersona \
     -X POST \
     -H "Content-Type: application/json" \
     -d '{
       "repositoryId": "ECM",
       "objectId": "test",
       "requestMode": "initialNewObject",
       "properties": [
         {"symbolicName": "Provinciadinascita", "value": ""},
         {"symbolicName": "Comunedinascita", "value": ""}
       ]
     }'
   ```

2. **Test delle Combo Box in ICN**
   - Apri Content Navigator
   - Crea un nuovo documento della classe `CartellaPersona`
   - Verifica che:
     - **Provinciadinascita** mostri una combo box con 107 province
     - Dopo aver selezionato una provincia, **Comunedinascita** mostri solo i comuni di quella provincia

## Struttura del WAR

```
ItalianLocationEDS.war
├── WEB-INF/
│   ├── web.xml                              # Configurazione servlet
│   └── classes/
│       ├── com/ibm/icn/extensions/servlets/
│       │   └── UpdateObjectTypeServlet.class
│       ├── ObjectTypes.json                 # Classi abilitate per EDS
│       ├── CartellaPersona_PropertyData.json # Configurazione proprietà
│       ├── gi_province.json                 # 107 province italiane
│       └── gi_comuni.json                   # 7,904 comuni italiani
└── META-INF/
    └── MANIFEST.MF
```

## URL dei Servlet

Dopo il deployment, i servlet saranno accessibili a:

### GetObjectTypesServlet
```
GET https://rocky.example.com:20002/ItalianLocationEDS/GetObjectTypesServlet
```

Restituisce la lista delle classi abilitate per EDS.

### UpdateObjectTypeServlet
```
POST https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/{objectType}
```

Esempio per CartellaPersona:
```
POST https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/CartellaPersona
```

## Flusso di Funzionamento

### 1. Apertura Entry Template

```
User: "Nuovo Documento" → CartellaPersona
    ↓
ICN → POST /p8/openContentClass
    ↓
edsPlugin intercetta la risposta
    ↓
edsPlugin modifica JSON response:
  - Aggiunge "dataSourceUrl" alle proprietà
  - URL: https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/CartellaPersona
    ↓
ICN riceve response modificata
    ↓
ICN → POST https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/CartellaPersona
  Body: {
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "Provinciadinascita", "value": ""},
      {"symbolicName": "Comunedinascita", "value": ""}
    ]
  }
    ↓
UpdateObjectTypeServlet.doPost()
    ↓
Carica gi_province.json
    ↓
Restituisce 107 province
    ↓
ICN mostra combo box Provinciadinascita con province
```

### 2. Selezione Provincia

```
User: Seleziona "MI" (Milano)
    ↓
ICN → POST https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/CartellaPersona
  Body: {
    "requestMode": "inProgressChanges",
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
Carica gi_comuni.json
    ↓
Filtra: comune.sigla_provincia == "MI"
    ↓
Restituisce 134 comuni della provincia di Milano
    ↓
ICN aggiorna combo box Comunedinascita
```

## Troubleshooting

### Il WAR non si avvia

**Verifica**:
1. Log di WebSphere: `/opt/IBM/WebSphere/AppServer/profiles/AppSrv01/logs/server1/SystemOut.log`
2. Cerca errori relativi a `ItalianLocationEDS`
3. Verifica che tutte le librerie necessarie siano presenti

### Il servlet restituisce 404

**Verifica**:
1. L'applicazione è avviata in WebSphere
2. Il context root è corretto: `/ItalianLocationEDS`
3. L'URL è: `https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/CartellaPersona`

### Le combo box non appaiono

**Verifica**:
1. Il plugin `edsPlugin.jar` è caricato in ICN
2. L'URL del servizio EDS è configurato correttamente nel plugin
3. Il servlet risponde correttamente (testa con curl)
4. I log di ICN per vedere se ci sono errori

### I comuni non si filtrano

**Verifica**:
1. Il file `gi_comuni.json` contiene il campo `sigla_provincia`
2. La proprietà `dependentOn` è configurata in `CartellaPersona_PropertyData.json`
3. Il valore della provincia viene passato correttamente nella richiesta

## File di Configurazione

### ObjectTypes.json
```json
[
  {"symbolicName": "CartellaPersona"}
]
```

### CartellaPersona_PropertyData.json
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

## Riferimenti

- IBM Content Navigator 3.2.0 Documentation
- IBM EDS Sample: https://github.com/ibm-ecm/ibm-content-navigator-samples/tree/master/sampleEDSService
- WebSphere Application Server Documentation

---

**Versione**: 1.0.0  
**Data**: 15 Maggio 2026  
**Autore**: Bob (AI Assistant)