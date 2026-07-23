# Guida al Deployment - Italian Location EDS

## Build del WAR

### Prerequisiti

| Strumento | Versione richiesta | Note |
|-----------|-------------------|-------|
| JDK | **1.8** | WebSphere 9 non carica classi compilate con Java > 8 |
| `navigatorAPI.jar` | 3.2.0 | Fornito con IBM Content Navigator |
| `j2ee.jar` | — | Fornito con IBM Content Navigator |

### ⚠️ Regola critica: usare sempre `cd` prima di compilare

Il path del progetto contiene spazi (`Content Navigator`). Se si invoca `javac` con path relativi senza prima entrare nella directory del progetto, il compilatore risolve i path in modo errato e scrive i `.class` nella posizione sbagliata — oppure usa i `.class` già presenti da una compilazione precedente con Java 21.

**Comando di build corretto — da eseguire sempre così:**

```bash
cd "/Users/massyc/Tech/Bob/Projects/Content Navigator/EDS/ItalianLocationEDS" && \
  JAVAC8="/Library/Java/JavaVirtualMachines/jdk1.8.0_311.jdk/Contents/Home/bin/javac" && \
  LIB="/Users/massyc/Documenti Lavoro/SWG/Prodotti/Prodotti DBA/IBM Content Navigator/v3.2.0/ECMClient/lib" && \
  rm -rf build-war/WEB-INF/classes/com && \
  "$JAVAC8" -source 1.8 -target 1.8 \
    -cp "$LIB/navigatorAPI.jar:$LIB/j2ee.jar" \
    -d build-war/WEB-INF/classes \
    src/com/ibm/icn/extensions/utils/FiscalCodeCalculator.java \
    src/com/ibm/icn/extensions/servlets/GetObjectTypesServlet.java \
    src/com/ibm/icn/extensions/servlets/UpdateObjectTypeServlet.java \
  && jar -cf dist/ItalianLocationEDS.war -C build-war . \
  && echo "Build OK: dist/ItalianLocationEDS.war"
```

Il WAR viene generato in `dist/ItalianLocationEDS.war`.

### Verifica versione classi — obbligatoria dopo ogni build

Tutti e tre i `.class` devono essere versione **52 (Java 8)**. Versione 65 (Java 21) causa `UnsupportedClassVersionError` su WebSphere 9.

```bash
cd "/Users/massyc/Tech/Bob/Projects/Content Navigator/EDS/ItalianLocationEDS" && \
  for CLASS in \
    "WEB-INF/classes/com/ibm/icn/extensions/utils/FiscalCodeCalculator.class" \
    "WEB-INF/classes/com/ibm/icn/extensions/servlets/GetObjectTypesServlet.class" \
    "WEB-INF/classes/com/ibm/icn/extensions/servlets/UpdateObjectTypeServlet.class"; do
    BYTE=$(unzip -p dist/ItalianLocationEDS.war "$CLASS" | od -An -tx1 -N8 | tr -d ' \n')
    MAJOR="${BYTE:12:2}${BYTE:14:2}"
    echo "Major version: 0x$MAJOR ($(printf '%d' 0x$MAJOR)) — $(basename $CLASS)"
  done
# Atteso per ogni riga: Major version: 0x0034 (52)
```

---

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

Dopo il deployment, i servlet sono accessibili agli URL seguenti (hardcoded nel protocollo EDS):

### GetObjectTypesServlet
```
GET https://rocky.example.com:20002/ItalianLocationEDS/types
```

Restituisce la lista delle classi abilitate per EDS.

### UpdateObjectTypeServlet
```
POST https://rocky.example.com:20002/ItalianLocationEDS/type/{objectType}
```

Esempio per CartellaPersona:
```
POST https://rocky.example.com:20002/ItalianLocationEDS/type/CartellaPersona
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

### Il WAR non si avvia — `SRVE0303E`

WebSphere non trova la mapping del servlet. Causa: configurazione stantia nel profilo.

**Soluzione — pulizia completa via wsadmin:**
```bash
/opt/IBM/WebSphere9/AppServer/bin/wsadmin.sh -lang jython \
  -host localhost -port 8879 -username wasadmin -password <password>
```
```python
AdminApp.uninstall('ItalianLocationEDS_war')
AdminConfig.save()
quit
```
Poi elimina i residui dal filesystem:
```bash
WAS_PROFILE=/opt/IBM/WebSphere9/AppServer/profiles/ICN
CELL=icnNode01Cell
rm -rf $WAS_PROFILE/config/cells/$CELL/applications/ItalianLocationEDS_war.ear
rm -rf $WAS_PROFILE/config/cells/$CELL/blas/ItalianLocationEDS_war
rm -rf $WAS_PROFILE/config/cells/$CELL/cus/ItalianLocationEDS_war
rm -rf $WAS_PROFILE/installedApps/$CELL/ItalianLocationEDS_war.ear
find $WAS_PROFILE -name "*ItalianLocation*" 2>/dev/null  # deve essere vuoto
```
Reinstalla come nuova applicazione dalla Admin Console.

### Il servlet restituisce 404 — `contextRoot` mancante

Se l'installazione non ha impostato il context root, aggiungilo manualmente nel `deployment.xml`:

```bash
/opt/IBM/WebSphere9/AppServer/bin/stopServer.sh icnServer -username wasadmin -password <password>

sed -i 's/uri="ItalianLocationEDS.war" containsEJBContent="0"/uri="ItalianLocationEDS.war" contextRoot="\/ItalianLocationEDS" containsEJBContent="0"/' \
  /opt/IBM/WebSphere9/AppServer/profiles/ICN/config/cells/icnNode01Cell/applications/ItalianLocationEDS_war.ear/deployments/ItalianLocationEDS_war/deployment.xml

/opt/IBM/WebSphere9/AppServer/bin/startServer.sh icnServer
```

### Il servlet restituisce 404 — `SRVE0202E` classe corrotta

Le classi nel WAR sono state compilate con Java > 8. WebSphere 9 accetta solo classi Java 8.

**Verifica:**
```bash
hexdump -C /opt/IBM/WebSphere9/AppServer/profiles/ICN/installedApps/icnNode01Cell/\
ItalianLocationEDS_war.ear/ItalianLocationEDS.war/WEB-INF/classes/com/ibm/icn/\
extensions/servlets/GetObjectTypesServlet.class | head -1
# byte 6-7 deve essere 00 34 (Java 8), non 00 41 (Java 21)
```

**Soluzione:** esegui la build con `JAVA8_HOME` impostato al JDK 8 (vedi sezione Build).

### Le combo box non appaiono

1. Verifica che il plugin `edsPlugin.jar` sia caricato in ICN
2. L'URL del servizio EDS nel plugin deve essere: `https://rocky.example.com:20002/ItalianLocationEDS`
3. Testa il servlet direttamente con curl (vedi sezione Test)
4. Controlla i log ICN per errori

### I comuni non si filtrano

1. Verifica che `gi_comuni.json` contenga il campo `sigla_provincia`
2. Verifica che `dependentOn: "Provinciadinascita"` sia presente in `CartellaPersona_PropertyData.json`
3. Controlla i log WebSphere — devono apparire righe come:
   ```
   ItalianLocationEDS.UpdateObjectTypeServlet: loading municipalities for Provincia=MI
   ItalianLocationEDS.UpdateObjectTypeServlet: Loaded 134 municipalities for province MI
   ```

### Il codice fiscale non viene calcolato

Verifica nei log WebSphere che tutti i campi richiesti siano presenti:
```
ItalianLocationEDS.UpdateObjectTypeServlet: Checking fiscal code calculation
  firstName=Mario
  lastName=Rossi
  birthDate=1985-10-10T00:00:00Z
  ...
```
Se uno dei campi è `null`, il calcolo viene saltato. ICN invia le date nel formato `yyyy-MM-ddTHH:mm:ssZ` — questo formato è supportato.

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

## Test di verifica

Dopo ogni deployment, esegui questi test per confermare il corretto funzionamento:

```bash
# Test 1 — GetObjectTypes (deve restituire array diretto, NON oggetto wrappato)
curl -k https://rocky.example.com:20002/ItalianLocationEDS/types
# Atteso: [{"symbolicName":"CartellaPersona"}]

# Test 2 — Calcolo codice fiscale completo
curl -k -X POST https://rocky.example.com:20002/ItalianLocationEDS/type/CartellaPersona \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId":"ECM",
    "requestMode":"initialNewObject",
    "properties":[
      {"symbolicName":"NomePersona","value":"Mario"},
      {"symbolicName":"CognomePersona","value":"Rossi"},
      {"symbolicName":"DatadiNascita","value":"1985-10-10T00:00:00Z"},
      {"symbolicName":"SessoPersona","value":"M"},
      {"symbolicName":"Statodinascita","value":"ITA"},
      {"symbolicName":"Provinciadinascita","value":"PD"},
      {"symbolicName":"Comunedinascita","value":"Abano Terme"}
    ]
  }'
# Atteso nel risultato: {"symbolicName":"CodiceFiscale","value":"RSSMRA85R10A001S",...}

# Test 3 — Nascita all'estero
curl -k -X POST https://rocky.example.com:20002/ItalianLocationEDS/type/CartellaPersona \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId":"ECM",
    "requestMode":"initialNewObject",
    "properties":[
      {"symbolicName":"NomePersona","value":"John"},
      {"symbolicName":"CognomePersona","value":"Smith"},
      {"symbolicName":"DatadiNascita","value":"1980-01-01T00:00:00Z"},
      {"symbolicName":"SessoPersona","value":"M"},
      {"symbolicName":"Statodinascita","value":"USA"},
      {"symbolicName":"Provinciadinascita","value":"Stato di nascita estero"},
      {"symbolicName":"Comunedinascita","value":"Stato di nascita estero"}
    ]
  }'
# Atteso: CodiceFiscale calcolato con codice paese Z404 (USA)
```

## Riferimenti

- IBM Content Navigator 3.2.0 Documentation
- IBM EDS Sample: https://github.com/ibm-ecm/ibm-content-navigator-samples/tree/master/sampleEDSService
- WebSphere Application Server 9.0 Documentation

---

**Versione**: 1.1.0
**Data**: 21 Luglio 2026
**Autore**: Bob (AI Assistant)