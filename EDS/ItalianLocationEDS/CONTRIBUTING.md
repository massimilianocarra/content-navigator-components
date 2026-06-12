# Guida per Contribuire

Grazie per il tuo interesse nel contribuire a questo progetto!

## Setup Ambiente di Sviluppo

### 1. Clona il Repository

```bash
git clone https://github.com/YOUR_USERNAME/italian-location-plugin-icn.git
cd italian-location-plugin-icn
```

### 2. Configura i File Locali

I seguenti file contengono configurazioni specifiche per ogni sviluppatore e **non sono tracciati da Git**:

#### `.classpath`

Copia il file di esempio e modifica i path:

```bash
cp .classpath.example .classpath
```

Poi modifica `.classpath` con il percorso delle tue librerie IBM Content Navigator:

```xml
<classpathentry kind="lib" path="/YOUR/PATH/TO/navigatorAPI.jar"/>
```

#### `.vscode/settings.json`

Copia il file di esempio:

```bash
cp .vscode/settings.json.example .vscode/settings.json
```

Poi modifica con i tuoi path locali.

### 3. Configura le Librerie per la Compilazione

Hai tre opzioni per configurare il percorso delle librerie ICN:

#### Opzione A: Variabile d'Ambiente (Consigliata)

```bash
# Linux/macOS
export ICN_LIB_DIR="/path/to/IBM/Content/Navigator/lib"

# Windows
set ICN_LIB_DIR=C:\path\to\IBM\Content\Navigator\lib
```

Aggiungi al tuo `.bashrc` o `.zshrc` per renderla permanente.

#### Opzione B: Parametro da Riga di Comando

```bash
ant build -Dicn.lib.dir=/path/to/IBM/Content/Navigator/lib
```

#### Opzione C: File build.properties (Non Committato)

Crea un file `build.properties` nella root del progetto:

```properties
icn.lib.dir=/path/to/IBM/Content/Navigator/lib
```

Questo file è già escluso da `.gitignore`.

### 4. Compila il Plugin

```bash
ant clean build
```

Il JAR verrà creato in `dist/ItalianLocationPlugin.jar`.

## Struttura del Progetto

```
ItalianLocationPlugin/
├── src/                          # Codice sorgente Java
│   └── com/ibm/icn/extensions/
│       ├── ItalianLocationPlugin.java
│       └── eds/
│           └── ItalianLocationEDS.java
├── resources/                    # Dati JSON
│   ├── gi_province.json         # 107 province italiane
│   └── gi_comuni.json           # 7904 comuni italiani
├── build.xml                     # Script di build Ant
├── plugin.xml                    # Configurazione plugin ICN
└── README.md                     # Documentazione principale
```

## Linee Guida per i Contributi

### Commit Messages

Usa commit messages descrittivi:

```
feat: Aggiungi supporto per CAP
fix: Correggi ordinamento comuni
docs: Aggiorna README con nuove istruzioni
refactor: Ottimizza caricamento dati JSON
```

### Code Style

- Indentazione: 4 spazi
- Encoding: UTF-8
- Line endings: LF (Unix)
- Commenti in italiano per coerenza con il dominio

### Testing

Prima di fare un commit:

1. Compila il progetto: `ant clean build`
2. Verifica che non ci siano errori di compilazione
3. Testa il plugin su Content Navigator se possibile

## Pull Request

1. Crea un branch per la tua feature: `git checkout -b feature/nome-feature`
2. Fai commit delle tue modifiche
3. Push del branch: `git push origin feature/nome-feature`
4. Apri una Pull Request su GitHub

## Domande?

Apri una Issue su GitHub per qualsiasi domanda o problema.

## Licenza

Contribuendo a questo progetto, accetti che i tuoi contributi siano rilasciati sotto la stessa licenza del progetto.