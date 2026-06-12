# Configurazione edsPlugin.jar per Italian Location EDS

## Prerequisiti

- WAR `ItalianLocationEDS.war` deployato e funzionante in WebSphere
- Plugin `edsPlugin.jar` disponibile

## Passo 1: Verifica che il WAR sia accessibile

Test rapido:
```bash
curl -k https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/CartellaPersona \
  -X POST -H "Content-Type: application/json" \
  -d '{"repositoryId":"ECM","objectId":"test","requestMode":"initialNewObject","properties":[{"symbolicName":"Provinciadinascita","value":""}]}'
```

Deve restituire JSON con le province.

## Passo 2: Carica edsPlugin.jar in ICN

1. **Accedi alla Console Admin ICN**
   ```
   https://rocky.example.com:20002/navigator/?desktop=admin
   ```

2. **Vai su Plug-ins**
   - Menu laterale → **Plug-ins**
   - Click su **New Plug-in**

3. **Carica il JAR**
   - **JAR file path**: Scegli una delle opzioni:
     
     **Opzione A - Path locale** (se ICN può accedere al filesystem):
     ```
     /opt/IBM/ECMClient/plugins/edsPlugin.jar
     ```
     
     **Opzione B - URL** (se il file è nella webapp deployata):
     ```
     https://rocky.example.com:20002/navigator/plugin/edsPlugin.jar
     ```
     
     **Nota**: Per l'opzione B, devi copiare `edsPlugin.jar` in:
     ```
     /opt/IBM/WebSphere/AppServer/profiles/AppSrv01/installedApps/icnNode01Cell/ICN.ear/navigator.war/plugin/
     ```

4. **Click Load**

## Passo 3: Configura il Plugin

Dopo il caricamento, appare la schermata di configurazione:

### Configurazione Richiesta

**External data service URL**:
```
https://rocky.example.com:20002/ItalianLocationEDS
```

**IMPORTANTE**: 
- ✅ Usa l'URL BASE del WAR (senza `/UpdateObjectTypeServlet`)
- ✅ Usa HTTPS se il server usa HTTPS
- ✅ Includi la porta (20002)
- ❌ NON includere il path del servlet

### Esempio di Configurazione Corretta

```
┌─────────────────────────────────────────────────────────┐
│ External Data Service Plugin Configuration               │
├─────────────────────────────────────────────────────────┤
│                                                          │
│ External data service URL:                               │
│ ┌─────────────────────────────────────────────────────┐ │
│ │ https://rocky.example.com:20002/ItalianLocationEDS  │ │
│ └─────────────────────────────────────────────────────┘ │
│                                                          │
│ [ ] Use SSL                                              │
│                                                          │
│ [Save and Close]  [Cancel]                               │
└─────────────────────────────────────────────────────────┘
```

## Passo 4: Associa il Plugin al Desktop

1. **Vai su Desktops**
   - Menu laterale → **Desktops**
   - Seleziona il desktop che usi (es. "admin" o "default")

2. **Edit Desktop**
   - Tab **Plug-ins**
   - Trova **External Data Service Plugin** nella lista
   - ✅ Spunta la checkbox per abilitarlo
   - Click **Save and Close**

## Passo 5: Associa il Plugin al Repository

1. **Vai su Repositories**
   - Menu laterale → **Repositories**
   - Seleziona il repository (es. "ECM")

2. **Edit Repository**
   - Tab **Plug-ins**
   - Trova **External Data Service Plugin** nella lista
   - ✅ Spunta la checkbox per abilitarlo
   - Click **Save and Close**

## Passo 6: Riavvia ICN

**IMPORTANTE**: Dopo aver configurato il plugin, devi riavviare l'applicazione ICN:

```bash
# WebSphere Console
https://rocky.example.com:9043/ibm/console

# Applications → Application Types → WebSphere enterprise applications
# Seleziona "IBM_Content_Navigator" o "ICN"
# Click "Stop"
# Attendi che si fermi
# Click "Start"
```

## Passo 7: Verifica la Configurazione

### Test 1: Verifica che il plugin sia caricato

1. Console Admin ICN → **Plug-ins**
2. Cerca **External Data Service Plugin**
3. Stato deve essere: **Loaded** ✅

### Test 2: Verifica i log

Controlla i log di ICN per messaggi del plugin:

```bash
tail -f /opt/IBM/WebSphere/AppServer/profiles/AppSrv01/logs/server1/SystemOut.log | grep -i "eds"
```

Dovresti vedere:
```
[INFO] External Data Service Plugin loaded successfully
[INFO] EDS URL configured: https://rocky.example.com:20002/ItalianLocationEDS
```

### Test 3: Verifica le combo box

1. Apri ICN (non admin)
2. Crea nuovo documento classe **CartellaPersona**
3. Verifica:
   - Campo **Provinciadinascita** deve essere una **combo box** (non text box)
   - Deve contenere 107 province
   - Selezionando una provincia, **Comunedinascita** deve popolarsi con i comuni

## Troubleshooting

### Problema: I campi sono ancora text box

**Possibili cause**:

1. **Plugin non associato al desktop**
   - Verifica: Desktop → Plug-ins → External Data Service Plugin ✅

2. **Plugin non associato al repository**
   - Verifica: Repository → Plug-ins → External Data Service Plugin ✅

3. **ICN non riavviato**
   - Riavvia l'applicazione ICN in WebSphere

4. **URL EDS errato**
   - Verifica che l'URL sia: `https://rocky.example.com:20002/ItalianLocationEDS`
   - NON deve includere `/UpdateObjectTypeServlet`

5. **Cache del browser**
   - Svuota la cache del browser (Ctrl+Shift+Del)
   - Ricarica ICN (Ctrl+F5)

### Problema: Errore "EDS service not available"

**Verifica**:

1. Il WAR è avviato in WebSphere
2. L'URL è accessibile:
   ```bash
   curl -k https://rocky.example.com:20002/ItalianLocationEDS/UpdateObjectTypeServlet/CartellaPersona
   ```

### Problema: Le province non appaiono

**Verifica nei log**:

```bash
tail -f /opt/IBM/WebSphere/AppServer/profiles/AppSrv01/logs/server1/SystemOut.log
```

Cerca errori come:
- `404 Not Found` → URL EDS errato
- `Connection refused` → WAR non avviato
- `JSON parse error` → Problema nel formato della risposta

## Configurazione Avanzata

### File di Configurazione del Plugin

Il plugin edsPlugin può essere configurato anche tramite file:

**Percorso**: `/opt/IBM/ECMClient/configure.properties`

```properties
# External Data Service Configuration
eds.service.url=https://rocky.example.com:20002/ItalianLocationEDS
eds.connection.timeout=30000
eds.read.timeout=60000
```

### Debug Mode

Per abilitare il debug del plugin:

1. Console Admin ICN → **Settings** → **Logging**
2. Imposta livello: **DEBUG**
3. Riavvia ICN

## Checklist Finale

Prima di testare, verifica:

- [ ] WAR deployato e funzionante
- [ ] edsPlugin.jar caricato in ICN
- [ ] URL EDS configurato: `https://rocky.example.com:20002/ItalianLocationEDS`
- [ ] Plugin associato al desktop
- [ ] Plugin associato al repository
- [ ] ICN riavviato
- [ ] Cache browser svuotata
- [ ] Log controllati per errori

## Risultato Atteso

Dopo la configurazione corretta:

```
┌────────────────────────────────────────────────────────┐
│ Crea Nuovo Documento - CartellaPersona                 │
├────────────────────────────────────────────────────────┤
│                                                         │
│ Provinciadinascita: *                                   │
│ ┌─────────────────────────────────────────────────┐   │
│ │ Seleziona Provincia                        ▼    │   │
│ └─────────────────────────────────────────────────┘   │
│   ↓ (Dopo selezione provincia, es. "Milano")          │
│                                                         │
│ Comunedinascita: *                                      │
│ ┌─────────────────────────────────────────────────┐   │
│ │ Seleziona Comune                           ▼    │   │
│ └─────────────────────────────────────────────────┘   │
│   (133 comuni della provincia di Milano)               │
│                                                         │
│ [Salva]  [Annulla]                                      │
└────────────────────────────────────────────────────────┘
```

---

**Versione**: 1.0.0  
**Data**: 15 Maggio 2026  
**Autore**: Bob (AI Assistant)