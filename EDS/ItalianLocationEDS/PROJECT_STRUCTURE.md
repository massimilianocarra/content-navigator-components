# Struttura Progetto Italian Location EDS

## Struttura Directory

```
ItalianLocationPlugin/
│
├── src/                                    # Codice sorgente Java
│   └── com/ibm/icn/extensions/servlets/
│       ├── GetObjectTypesServlet.java      # Servlet per lista classi EDS (URL: /types)
│       └── UpdateObjectTypeServlet.java    # Servlet per dati proprietà (URL: /type/*)
│
├── resources/                              # Risorse e configurazioni
│   ├── ObjectTypes.json                    # Lista classi che usano EDS
│   ├── CartellaPersona_PropertyData.json   # Configurazione proprietà CartellaPersona
│   ├── gi_province.json                    # 107 province italiane
│   ├── gi_comuni.json                      # 7.904 comuni italiani
│   ├── ObjectTypes.json.example            # Template per nuove classi
│   ├── PropertyData.json.example           # Template per nuove proprietà
│   └── README_CONFIGURATION.md             # Guida configurazione
│
├── WebContent/                             # Contenuto web application
│   └── WEB-INF/
│       └── web.xml                         # Configurazione servlet (URL: /types e /type/*)
│
├── build-war/                              # Directory build temporanea (generata)
│   └── WEB-INF/
│       ├── web.xml
│       └── classes/                        # Classi compilate e risorse
│
├── lib/                                    # Librerie esterne (da configurare)
│   ├── navigatorAPI.jar                    # IBM Content Navigator API
│   └── j2ee.jar                            # Java EE API
│
├── dist/                                   # File distribuibili (generati)
│   └── ItalianLocationEDS.war              # WAR file pronto per deploy
│
├── build-war.xml                           # Script Ant per build WAR
├── test-servlet.sh                         # Script test servlet
│
└── docs/                                   # Documentazione
    ├── README.md                           # Guida principale
    ├── DEPLOYMENT_GUIDE_WAR.md             # Guida deployment WAR
    ├── EDSPLUGIN_CONFIGURATION.md          # Configurazione edsPlugin
    ├── TECHNICAL_NOTES.md                  # Note tecniche
    ├── LIBRERIE_NECESSARIE.md              # Librerie richieste
    ├── SETUP_LIBRARIES.md                  # Setup librerie
    ├── CONTRIBUTING.md                     # Guida contribuzione
    └── PROJECT_STRUCTURE.md                # Questo file
```

## File Principali

### Codice Sorgente

| File | Descrizione | URL |
|------|-------------|-----|
| `GetObjectTypesServlet.java` | Restituisce lista classi che usano EDS | `/types` |
| `UpdateObjectTypeServlet.java` | Restituisce dati proprietà e choice lists | `/type/*` |

### Configurazioni

| File | Descrizione |
|------|-------------|
| `ObjectTypes.json` | Lista classi documentali che usano EDS |
| `CartellaPersona_PropertyData.json` | Configurazione proprietà per CartellaPersona |
| `web.xml` | Configurazione servlet e URL mapping |

### Dati

| File | Descrizione | Dimensione |
|------|-------------|------------|
| `gi_province.json` | 107 province italiane | ~30 KB |
| `gi_comuni.json` | 7.904 comuni italiani | ~3.2 MB |

## File Generati

Questi file sono generati automaticamente durante il build e non devono essere modificati manualmente:

- `build-war/` - Directory temporanea di build
- `dist/ItalianLocationEDS.war` - WAR file finale

## File Rimossi

I seguenti file sono stati rimossi perché obsoleti (approccio PluginService non funzionante):

- ❌ `src/com/ibm/icn/extensions/ItalianLocationPlugin.java`
- ❌ `src/com/ibm/icn/extensions/eds/` (intera directory)
- ❌ `src/com/ibm/icn/extensions/filters/OpenContentClassResponseFilter.java`
- ❌ `build.xml` (build plugin JAR)
- ❌ `plugin.xml` (configurazione plugin JAR)
- ❌ `META-INF/MANIFEST.MF`
- ❌ `DEPLOYMENT_GUIDE.md` (sostituito da DEPLOYMENT_GUIDE_WAR.md)
- ❌ `.classpath.example`
- ❌ `entry-template-example.json`

## Dipendenze Esterne

### Librerie Richieste (da inserire in `lib/`)

1. **navigatorAPI.jar** - IBM Content Navigator API
   - Percorso: `/opt/IBM/ECMClient/lib/navigatorAPI.jar`
   - Versione: 3.0.7+

2. **j2ee.jar** - Java EE API
   - Percorso: `/opt/IBM/WebSphere/AppServer/lib/j2ee.jar`
   - Versione: 1.4+

### Plugin IBM Richiesto

- **edsPlugin.jar** - Plugin IBM per integrazione EDS
  - Fornito da IBM con Content Navigator
  - Da configurare in ICN Admin Console

## Build e Deploy

### Build WAR

```bash
# Imposta variabile d'ambiente
export ICN_LIB_DIR="/path/to/ICN/lib"

# Build
ant -f build-war.xml

# Output: dist/ItalianLocationEDS.war
```

### Deploy

1. Deploy WAR in WebSphere (context root: `/ItalianLocationEDS`)
2. Configura edsPlugin in ICN (URL: `https://server:port/ItalianLocationEDS`)
3. Riavvia ICN
4. Test

## URL Endpoint

Una volta deployato, i servlet sono accessibili a:

- **GetObjectTypes**: `https://server:port/ItalianLocationEDS/types`
- **UpdateObjectType**: `https://server:port/ItalianLocationEDS/type/CartellaPersona`

⚠️ **Importante**: Gli URL `/types` e `/type/*` sono hardcoded in edsPlugin e non possono essere modificati.

## Estensibilità

### Aggiungere Nuove Classi

1. Aggiungi la classe in `resources/ObjectTypes.json`:
   ```json
   [
     {"symbolicName": "CartellaPersona"},
     {"symbolicName": "NuovaClasse"}
   ]
   ```

2. Crea `resources/NuovaClasse_PropertyData.json` con la configurazione proprietà

3. Ricompila il WAR e redeploy

### Aggiungere Nuove Proprietà

Modifica il file `*_PropertyData.json` della classe e aggiungi la nuova proprietà con la sua configurazione.

## Manutenzione

### Aggiornamento Dati

Per aggiornare province o comuni:

1. Sostituisci `resources/gi_province.json` o `resources/gi_comuni.json`
2. Ricompila il WAR
3. Redeploy

### Aggiornamento Codice

1. Modifica i file sorgente in `src/`
2. Ricompila con `ant -f build-war.xml`
3. Redeploy il nuovo WAR

## Note Tecniche

- **Architettura**: WAR separato + edsPlugin.jar (IBM)
- **Formato JSON**: Array diretto per GetObjectTypes, non oggetto con chiave
- **URL Pattern**: Obbligatori `/types` e `/type/*`
- **Selezione Gerarchica**: Configurata tramite `dependentOn` in PropertyData.json

## Supporto

Per problemi o domande, consultare:
- `README.md` - Guida principale
- `TECHNICAL_NOTES.md` - Analisi tecnica dettagliata
- `DEPLOYMENT_GUIDE_WAR.md` - Guida deployment completa