# Guida alla Configurazione delle Librerie IBM Content Navigator

Gli errori Java che vedi sono normali perché mancano le librerie di IBM Content Navigator necessarie per la compilazione. Ecco come risolverli.

## Opzione 1: Hai Content Navigator Installato Localmente

Se hai IBM Content Navigator 3.2.0 installato sulla tua macchina:

### 1. Trova le Librerie

Le librerie si trovano tipicamente in:

**Linux/Unix:**
```bash
/opt/IBM/ECMClient/lib/
```

**Windows:**
```
C:\Program Files\IBM\ECMClient\lib\
```

**macOS (se installato):**
```bash
/Applications/IBM/ECMClient/lib/
```

### 2. Verifica le Librerie Necessarie

Controlla che esistano questi file:
```bash
ls -l /opt/IBM/ECMClient/lib/navigatorAPI.jar
ls -l /opt/IBM/ECMClient/lib/j2ee.jar
ls -l /opt/IBM/ECMClient/lib/jace.jar
ls -l /opt/IBM/ECMClient/lib/json4j.jar
```

### 3. Configura build.xml

Modifica il file `build.xml` alla riga 21:

```xml
<property name="icn.lib.dir" location="/opt/IBM/ECMClient/lib"/>
```

Sostituisci con il percorso corretto del tuo sistema.

### 4. Compila

```bash
cd ItalianLocationPlugin
ant build
```

## Opzione 2: Non Hai Content Navigator Installato Localmente

Se Content Navigator è installato solo su un server remoto:

### Metodo A: Copia le Librerie dal Server

1. **Connettiti al server** dove è installato Content Navigator:
   ```bash
   ssh user@server-icn
   ```

2. **Trova le librerie**:
   ```bash
   cd /opt/IBM/ECMClient/lib/
   ls -l *.jar
   ```

3. **Copia le librerie necessarie** nella directory `lib/` del plugin:
   ```bash
   # Dal server
   scp /opt/IBM/ECMClient/lib/navigatorAPI.jar user@tua-macchina:/path/to/ItalianLocationPlugin/lib/
   scp /opt/IBM/ECMClient/lib/j2ee.jar user@tua-macchina:/path/to/ItalianLocationPlugin/lib/
   scp /opt/IBM/ECMClient/lib/jace.jar user@tua-macchina:/path/to/ItalianLocationPlugin/lib/
   scp /opt/IBM/ECMClient/lib/json4j.jar user@tua-macchina:/path/to/ItalianLocationPlugin/lib/
   ```

4. **Modifica build.xml** per usare la directory locale:
   ```xml
   <property name="icn.lib.dir" location="lib"/>
   ```

### Metodo B: Compila Direttamente sul Server

1. **Copia il progetto sul server**:
   ```bash
   scp -r ItalianLocationPlugin user@server-icn:/tmp/
   ```

2. **Connettiti al server**:
   ```bash
   ssh user@server-icn
   ```

3. **Configura e compila**:
   ```bash
   cd /tmp/ItalianLocationPlugin
   # Modifica build.xml con il percorso corretto
   ant build
   ```

4. **Scarica il JAR compilato**:
   ```bash
   scp user@server-icn:/tmp/ItalianLocationPlugin/dist/ItalianLocationPlugin.jar .
   ```

## Opzione 3: Configurazione VSCode per Ignorare gli Errori

Se vuoi solo eliminare gli errori visivi in VSCode (il codice compilerà comunque sul server):

### 1. Crea un file `.classpath` nella root del progetto:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
    <classpathentry kind="src" path="ItalianLocationPlugin/src"/>
    <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/>
    <classpathentry kind="output" path="bin"/>
</classpath>
```

### 2. Oppure configura VSCode per ignorare gli errori:

Crea/modifica `.vscode/settings.json`:

```json
{
    "java.configuration.updateBuildConfiguration": "disabled",
    "java.errors.incompleteClasspath.severity": "ignore"
}
```

## Opzione 4: Usa Maven (Alternativa)

Se preferisci Maven ad Ant, posso creare un `pom.xml` che scarica automaticamente le dipendenze da repository Maven (se disponibili).

## Librerie Necessarie - Dettagli

| Libreria | Descrizione | Dimensione Tipica |
|----------|-------------|-------------------|
| navigatorAPI.jar | API principale di Content Navigator | ~2-5 MB |
| j2ee.jar | Java EE APIs | ~1-2 MB |
| jace.jar | FileNet Content Engine API | ~5-10 MB |
| json4j.jar | IBM JSON for Java | ~200 KB |

## Verifica Configurazione

Dopo aver configurato le librerie, verifica con:

```bash
cd ItalianLocationPlugin
ant info
```

Dovresti vedere:
```
===========================================
Plugin: ItalianLocationPlugin
Versione: 1.0.0
JAR: ItalianLocationPlugin.jar
===========================================
Directory sorgenti: src
Directory build: build
Directory distribuzione: dist
Directory librerie ICN: /opt/IBM/ECMClient/lib
===========================================
```

## Risoluzione Problemi Comuni

### Errore: "Cannot find navigatorAPI.jar"

**Causa**: Il percorso in `build.xml` non è corretto.

**Soluzione**:
1. Verifica il percorso reale delle librerie
2. Aggiorna `icn.lib.dir` in `build.xml`
3. Oppure copia le librerie in `ItalianLocationPlugin/lib/`

### Errore: "Package com.ibm.ecm does not exist"

**Causa**: Le librerie non sono nel classpath.

**Soluzione**:
1. Verifica che le librerie esistano nel percorso specificato
2. Controlla i permessi di lettura sui file JAR
3. Prova a compilare con `ant -v build` per vedere il classpath usato

### Gli errori in VSCode persistono ma la compilazione funziona

**Causa**: VSCode non ha le librerie nel suo classpath.

**Soluzione**: Questo è normale. Gli errori scompariranno dopo la prima compilazione riuscita, oppure puoi ignorarli seguendo l'Opzione 3.

## Prossimi Passi

Una volta risolti gli errori delle librerie:

1. ✅ Compila il plugin: `ant build`
2. ✅ Verifica il JAR: `ls -l dist/ItalianLocationPlugin.jar`
3. ✅ Carica il JAR in Content Navigator
4. ✅ Configura l'Entry Template
5. ✅ Testa la funzionalità

## Hai Bisogno di Aiuto?

Dimmi quale opzione preferisci e posso aiutarti con i comandi specifici per il tuo caso!