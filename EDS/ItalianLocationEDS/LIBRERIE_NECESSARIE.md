# Librerie Necessarie per la Compilazione

## Librerie Richieste

Per compilare il plugin Italian Location EDS servono le seguenti librerie JAR:

### 1. **navigatorAPI.jar** ✅ (Già presente)
- **Descrizione**: API principale di IBM Content Navigator
- **Contiene**: Classi base per plugin, ExternalDataService, PluginLogger, ecc.
- **Percorso attuale**: `/Users/massyc/Documenti Lavoro/SWG/Prodotti/Prodotti DBA/IBM Content Navigator/v3.2.0/ECMClient/lib/navigatorAPI.jar`
- **Dimensione**: ~746 KB

### 2. **javax.servlet-api.jar** ❌ (Mancante)
- **Descrizione**: Java Servlet API
- **Contiene**: HttpServletRequest, HttpServletResponse, ServletRequest
- **Versione richiesta**: 3.1 o superiore
- **Dove trovarla**:
  - Sul server Content Navigator in: `[WebSphere]/lib` o `[Tomcat]/lib`
  - Download Maven: https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar
- **Dimensione**: ~95 KB

### 3. **json4j.jar** ❌ (Mancante - ma potrebbe essere inclusa in navigatorAPI)
- **Descrizione**: IBM JSON for Java
- **Contiene**: com.ibm.json.java.JSONObject, JSONArray
- **Dove trovarla**:
  - Sul server Content Navigator in: `[ICN_HOME]/lib` o `[WebSphere]/lib`
  - Potrebbe essere già inclusa in navigatorAPI.jar
- **Dimensione**: ~200 KB

### 4. **Jace.jar** ❌ (Opzionale per questo plugin)
- **Descrizione**: FileNet Content Engine API
- **Nota**: Non strettamente necessaria per questo plugin EDS
- **Dove trovarla**: Sul server FileNet P8

## Come Verificare le Librerie

### Controlla il contenuto di navigatorAPI.jar

```bash
jar tf "/Users/massyc/Documenti Lavoro/SWG/Prodotti/Prodotti DBA/IBM Content Navigator/v3.2.0/ECMClient/lib/navigatorAPI.jar" | grep -E "(json|servlet)"
```

Se vedi classi `com/ibm/json/java/`, allora json4j è già inclusa.

## Soluzioni Rapide

### Opzione A: Scaricare javax.servlet-api.jar

```bash
cd ItalianLocationPlugin/lib
curl -O https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar
```

Poi modifica `build.xml` per includere anche la directory `lib`:
```xml
<property name="icn.lib.dir" location="/Users/massyc/Documenti Lavoro/SWG/Prodotti/Prodotti DBA/IBM Content Navigator/v3.2.0/ECMClient/lib"/>
```

### Opzione B: Cercare sul Server Content Navigator

Le librerie potrebbero essere in:
```
[ICN_SERVER]/opt/IBM/WebSphere/AppServer/lib/
[ICN_SERVER]/opt/IBM/WebSphere/AppServer/plugins/
[ICN_SERVER]/opt/IBM/ECMClient/lib/
```

### Opzione C: Usare Maven (Consigliato)

Posso creare un `pom.xml` che scarica automaticamente tutte le dipendenze necessarie.

## Riepilogo Errori di Compilazione

Gli errori che vedi sono dovuti a:

1. **javax.servlet.http.HttpServletRequest** - Manca servlet-api.jar
2. **javax.servlet.http.HttpServletResponse** - Manca servlet-api.jar  
3. **com.ibm.json.java.JSONObject** - Potrebbe essere in navigatorAPI.jar o serve json4j.jar separato
4. **com.ibm.json.java.JSONArray** - Come sopra

## Prossimi Passi

1. **Verifica se json4j è in navigatorAPI.jar**:
   ```bash
   jar tf "/Users/massyc/Documenti Lavoro/SWG/Prodotti/Prodotti DBA/IBM Content Navigator/v3.2.0/ECMClient/lib/navigatorAPI.jar" | grep "com/ibm/json"
   ```

2. **Scarica servlet-api.jar** nella directory `lib/`:
   ```bash
   cd ItalianLocationPlugin/lib
   curl -O https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar
   ```

3. **Ricompila**:
   ```bash
   cd ItalianLocationPlugin
   ant clean build
   ```

## Alternative

Se preferisci, posso:
1. Creare un `pom.xml` Maven che gestisce automaticamente le dipendenze
2. Modificare il codice per non usare le classi servlet (meno elegante ma funzionale)
3. Fornirti uno script per scaricare tutte le librerie necessarie

Dimmi quale soluzione preferisci!