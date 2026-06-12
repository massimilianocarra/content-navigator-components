# Italian Location EDS Plugin per IBM Content Navigator

Plugin External Data Service (EDS) per la gestione di province e comuni italiani in IBM Content Navigator.

## Descrizione

Questo plugin fornisce combo box dinamiche per la selezione di province e comuni italiani con selezione gerarchica:
- **Provincia**: Combo box con 107 province italiane
- **Comune**: Combo box con comuni filtrati in base alla provincia selezionata

## Caratteristiche

✅ **Selezione Gerarchica**: La scelta della provincia filtra automaticamente i comuni  
✅ **Dati Completi**: 107 province e 7.904 comuni italiani  
✅ **Integrazione Nativa**: Utilizza l'architettura EDS standard di IBM  
✅ **Performance**: Caricamento dinamico dei dati solo quando necessario  

## Architettura

Il plugin è composto da due componenti:

1. **ItalianLocationEDS.war**: Applicazione web con servlet EDS
   - `GetObjectTypesServlet` (URL: `/types`)
   - `UpdateObjectTypeServlet` (URL: `/type/*`)

2. **edsPlugin.jar**: Plugin IBM fornito per l'integrazione con ICN

## Requisiti

- IBM Content Navigator 3.0.7 o superiore
- IBM WebSphere Application Server
- IBM FileNet P8 Content Engine
- Java 8 o superiore

## Installazione

### 1. Deploy del WAR in WebSphere

```bash
# Copia il WAR
cp dist/ItalianLocationEDS.war /path/to/websphere/

# Deploy tramite console WebSphere
# Applications → New Application → New Enterprise Application
# Context root: /ItalianLocationEDS
```

### 2. Configurazione edsPlugin in ICN

1. Accedi alla console di amministrazione ICN
2. Vai a **Plugins**
3. Carica `edsPlugin.jar` (fornito da IBM)
4. Configura il plugin:
   - **URL**: `https://your-server:port/ItalianLocationEDS`
   - **Repository**: Seleziona il repository FileNet
   - **Desktop**: Associa al desktop

### 3. Riavvio

1. Riavvia ICN
2. Svuota la cache del browser
3. Testa creando un nuovo documento della classe `CartellaPersona`

## Configurazione Classe Documentale

Il plugin è configurato per la classe `CartellaPersona` con le seguenti proprietà:

- **Provinciadinascita**: Combo box con province italiane
- **Comunedinascita**: Combo box con comuni (filtrati per provincia)

Per aggiungere altre classi, modifica `resources/ObjectTypes.json`:

```json
[
  {"symbolicName": "CartellaPersona"},
  {"symbolicName": "AltraClasse"}
]
```

E crea il file di configurazione corrispondente in `resources/AltraClasse_PropertyData.json`.

## Struttura File

```
ItalianLocationPlugin/
├── src/
│   └── com/ibm/icn/extensions/servlets/
│       ├── GetObjectTypesServlet.java
│       └── UpdateObjectTypeServlet.java
├── resources/
│   ├── ObjectTypes.json
│   ├── CartellaPersona_PropertyData.json
│   ├── gi_province.json (107 province)
│   └── gi_comuni.json (7.904 comuni)
├── WebContent/WEB-INF/
│   └── web.xml
├── dist/
│   └── ItalianLocationEDS.war
└── docs/
    ├── DEPLOYMENT_GUIDE_WAR.md
    ├── EDSPLUGIN_CONFIGURATION.md
    └── TECHNICAL_NOTES.md
```

## Build

Per ricompilare il WAR:

```bash
# Imposta la variabile d'ambiente
export ICN_LIB_DIR="/path/to/ICN/lib"

# Build
ant -f build-war.xml
```

Il WAR sarà generato in `dist/ItalianLocationEDS.war`.

## Test

### Test Servlet GetObjectTypes

```bash
curl -k https://your-server:port/ItalianLocationEDS/types
```

Risposta attesa:
```json
[{"symbolicName":"CartellaPersona"}]
```

### Test Servlet UpdateObjectType

```bash
curl -k -X POST https://your-server:port/ItalianLocationEDS/type/CartellaPersona \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": []
  }'
```

Risposta attesa: JSON con le 107 province.

## Troubleshooting

### Le combo box non appaiono

1. Verifica che edsPlugin sia caricato e configurato correttamente
2. Controlla i log di ICN per errori 404
3. Verifica che il WAR sia deployato e avviato
4. Testa i servlet con curl
5. Svuota completamente la cache del browser

### Errore 404 nei log

Verifica che gli URL pattern nel `web.xml` siano:
- `/types` (non `/GetObjectTypesServlet`)
- `/type/*` (non `/UpdateObjectTypeServlet/*`)

Questi URL sono hardcoded in edsPlugin e non possono essere modificati.

### I comuni non si filtrano

Verifica che:
1. Il campo `Provinciadinascita` sia configurato con `hasDependentProperties: true`
2. Il campo `Comunedinascita` sia configurato con `dependentOn: "Provinciadinascita"`
3. I nomi simbolici delle proprietà corrispondano esattamente

## Documentazione Aggiuntiva

- [DEPLOYMENT_GUIDE_WAR.md](DEPLOYMENT_GUIDE_WAR.md) - Guida dettagliata al deployment
- [EDSPLUGIN_CONFIGURATION.md](EDSPLUGIN_CONFIGURATION.md) - Configurazione edsPlugin
- [TECHNICAL_NOTES.md](TECHNICAL_NOTES.md) - Note tecniche e analisi architetturale

## Licenza

Questo plugin è fornito come esempio per IBM Content Navigator.

## Autore

Creato con Bob - AI Assistant

## Note Importanti

⚠️ **URL Pattern Obbligatori**: edsPlugin richiede gli URL `/types` e `/type/*`. Non usare URL personalizzati.

⚠️ **Formato JSON**: GetObjectTypesServlet deve restituire un array diretto `[...]`, non un oggetto con chiave.

⚠️ **Cache Browser**: Dopo modifiche alla configurazione, svuotare sempre la cache del browser.

## Supporto

Per problemi o domande, consultare:
- [IBM Content Navigator Knowledge Center](https://www.ibm.com/docs/en/content-navigator)
- [IBM ECM Samples GitHub](https://github.com/ibm-ecm/ibm-content-navigator-samples)