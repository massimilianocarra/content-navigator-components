# Italian Location EDS Plugin per IBM Content Navigator

Plugin External Data Service (EDS) per la gestione di stati, province e comuni italiani in IBM Content Navigator.

## Descrizione

Questo plugin fornisce combo box dinamiche per la selezione gerarchica di località italiane ed estere:
- **Stato**: Combo box con 239 stati (Italia prioritaria, poi ordine alfabetico)
- **Provincia**: Combo box con 107 province italiane (abilitata solo se Stato = Italia)
- **Comune**: Combo box con comuni filtrati in base alla provincia selezionata (abilitata solo se Stato = Italia)

## Caratteristiche

✅ **Selezione Gerarchica a 3 Livelli**: Stato → Provincia → Comune
✅ **Logica Condizionale**: Provincia e Comune abilitati solo per l'Italia
✅ **Dati Completi**: 239 stati, 107 province e 7.904 comuni italiani
✅ **Attributi Estesi**: Ogni stato include nome inglese e codice fiscale
✅ **Calcolo Codice Fiscale**: Generazione automatica del Codice Fiscale italiano
✅ **Integrazione Nativa**: Utilizza l'architettura EDS standard di IBM
✅ **Performance**: Caricamento dinamico dei dati solo quando necessario
✅ **Configurabilità**: Nomi delle proprietà personalizzabili tramite file JSON

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

- **Statodinascita**: Combo box con 239 stati (Italia prioritaria)
- **Provinciadinascita**: Combo box con 107 province italiane (abilitata solo se Stato = Italia)
- **Comunedinascita**: Combo box con comuni filtrati per provincia (abilitata solo se Stato = Italia)

### Logica Condizionale

Quando l'utente seleziona uno **stato diverso dall'Italia**:
- I campi Provincia e Comune vengono **automaticamente disabilitati**
- I valori precedenti vengono **svuotati**

Quando l'utente seleziona **Italia**:
- I campi Provincia e Comune vengono **riabilitati**
- La selezione gerarchica Provincia → Comune funziona normalmente

### Aggiungere Altre Classi

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
ItalianLocationEDS/
├── src/
│   └── com/ibm/icn/extensions/
│       ├── servlets/
│       │   ├── GetObjectTypesServlet.java
│       │   └── UpdateObjectTypeServlet.java
│       └── utils/
│           └── FiscalCodeCalculator.java (NEW)
├── resources/
│   ├── ObjectTypes.json
│   ├── CartellaPersona_PropertyData.json
│   ├── FiscalCodeConfig.json (NEW)
│   ├── PropertyNamesConfig.json
│   ├── ForeignStateConfig.json
│   ├── gi_stati.json (239 stati con attributi estesi)
│   ├── gi_province.json (107 province)
│   └── gi_comuni.json (7.904 comuni con codici Belfiore)
├── WebContent/WEB-INF/
│   └── web.xml
├── dist/
│   └── ItalianLocationEDS.war
└── docs/
    ├── DEPLOYMENT_GUIDE_WAR.md
    ├── EDSPLUGIN_CONFIGURATION.md
    ├── TECHNICAL_NOTES.md
    ├── FISCAL_CODE_FEATURE.md (NEW)
    ├── CHANGELOG_STATO.md
    └── CHANGELOG_FISCAL_CODE.md (NEW)
```

### Formato File gi_stati.json

Ogni stato include i seguenti attributi:

```json
{
  "displayName": "Italia",
  "value": "ITA",
  "english_country_name": "Italy",
  "taxcode_country_code": ""
}
```

- **displayName**: Nome italiano formattato (Title Case)
- **value**: Codice ISO 3166-1 alpha-3
- **english_country_name**: Nome inglese dello stato
- **taxcode_country_code**: Codice fiscale italiano (per uso futuro)

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

### Test Servlet UpdateObjectType - Caricamento Iniziale

```bash
curl -k -X POST https://your-server:port/ItalianLocationEDS/type/CartellaPersona \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": []
  }'
```

Risposta attesa: JSON con 239 stati (Italia prima).

### Test con Stato = Italia

```bash
curl -k -X POST https://your-server:port/ItalianLocationEDS/type/CartellaPersona \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "Statodinascita", "value": "ITA"}
    ]
  }'
```

Risposta attesa: Provincia e Comune abilitati (`displayMode: "readwrite"`).

### Test Calcolo Codice Fiscale

```bash
curl -k -X POST https://your-server:port/ItalianLocationEDS/type/CartellaPersona \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "NomePersona", "value": "Mario"},
      {"symbolicName": "CognomePersona", "value": "Rossi"},
      {"symbolicName": "DatadiNascita", "value": "10/10/1985"},
      {"symbolicName": "SessoPersona", "value": "M"},
      {"symbolicName": "Statodinascita", "value": "ITA"},
      {"symbolicName": "Provinciadinascita", "value": "PD"},
      {"symbolicName": "Comunedinascita", "value": "Abano Terme"}
    ]
  }'
```

Risposta attesa: JSON con proprietà `CodiceFiscale` valorizzata a `RSSMRA85R10A001S`.

### Test con Stato Estero

```bash
curl -k -X POST https://your-server:port/ItalianLocationEDS/type/CartellaPersona \
  -H "Content-Type: application/json" \
  -d '{
    "repositoryId": "FNOS",
    "requestMode": "initialNewObject",
    "properties": [
      {"symbolicName": "Statodinascita", "value": "FRA"}
    ]
  }'
```

Risposta attesa: Provincia e Comune disabilitati (`displayMode: "readonly"`).

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
1. Il campo `Statodinascita` sia configurato con `hasDependentProperties: true`
2. Il campo `Provinciadinascita` sia configurato con `dependentOn: "Statodinascita"` e `hasDependentProperties: true`
3. Il campo `Comunedinascita` sia configurato con `dependentOn: "Provinciadinascita"`
4. I nomi simbolici delle proprietà corrispondano esattamente

### Provincia e Comune non si disabilitano con stato estero

Verifica che:
1. Il valore dello stato sia esattamente "ITA" (case-sensitive)
2. Il servlet UpdateObjectTypeServlet sia stato aggiornato con la logica condizionale
3. I log del servlet mostrino i messaggi di debug sulla disabilitazione

### Il Codice Fiscale non viene calcolato

Verifica che:
1. Tutti i campi obbligatori siano compilati (Nome, Cognome, Data di nascita, Sesso, Stato, Comune)
2. Il formato della data sia corretto (`dd/MM/yyyy` o `yyyy-MM-dd`)
3. Il comune esista nel file `gi_comuni.json` con il codice Belfiore
4. Per nascite estere, lo stato abbia un codice fiscale in `gi_stati.json`
5. I log del servlet mostrino i messaggi di debug del calcolo

### Codice Fiscale errato

Verifica che:
1. I dati inseriti siano corretti (nome, cognome, data, sesso)
2. Il codice Belfiore del comune sia corretto in `gi_comuni.json`
3. Per nascite estere, il codice fiscale dello stato sia corretto in `gi_stati.json`

## Funzionalità Codice Fiscale

L'EDS include il calcolo automatico del Codice Fiscale italiano. Quando l'utente compila i seguenti campi:
- Nome
- Cognome
- Data di nascita
- Stato di nascita
- Provincia di nascita (se Italia)
- Comune di nascita
- Sesso

Il sistema calcola automaticamente il Codice Fiscale e lo inserisce nel campo dedicato.

### Esempio

**Input**:
- Nome: Mario
- Cognome: Rossi
- Data di nascita: 10/10/1985
- Sesso: M
- Stato: Italia
- Provincia: PD (Padova)
- Comune: Abano Terme

**Output**: `RSSMRA85R10A001S`

### Configurazione

I nomi delle proprietà utilizzate per il calcolo sono configurabili nel file [`FiscalCodeConfig.json`](resources/FiscalCodeConfig.json):

```json
{
  "firstNamePropertyName": "NomePersona",
  "lastNamePropertyName": "CognomePersona",
  "birthDatePropertyName": "DatadiNascita",
  "birthStatePropertyName": "Statodinascita",
  "birthProvincePropertyName": "Provinciadinascita",
  "birthMunicipalityPropertyName": "Comunedinascita",
  "genderPropertyName": "SessoPersona",
  "fiscalCodePropertyName": "CodiceFiscale"
}
```

Per maggiori dettagli, consulta la [documentazione completa del Codice Fiscale](FISCAL_CODE_FEATURE.md).

## Documentazione Aggiuntiva

- [DEPLOYMENT_GUIDE_WAR.md](DEPLOYMENT_GUIDE_WAR.md) - Guida dettagliata al deployment
- [EDSPLUGIN_CONFIGURATION.md](EDSPLUGIN_CONFIGURATION.md) - Configurazione edsPlugin
- [TECHNICAL_NOTES.md](TECHNICAL_NOTES.md) - Note tecniche e analisi architetturale
- [FISCAL_CODE_FEATURE.md](FISCAL_CODE_FEATURE.md) ⭐ **NEW** - Funzionalità Codice Fiscale
- [CHANGELOG_FISCAL_CODE.md](CHANGELOG_FISCAL_CODE.md) ⭐ **NEW** - Changelog Codice Fiscale
- [CHANGELOG_STATO.md](CHANGELOG_STATO.md) - Changelog Stati
- [CHANGELOG_CONFIGURABLE_NAMES.md](CHANGELOG_CONFIGURABLE_NAMES.md) - Changelog Nomi Configurabili
- [CHANGELOG_FOREIGN_STATE.md](CHANGELOG_FOREIGN_STATE.md) - Changelog Stato Estero

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