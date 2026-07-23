# External Data Services (EDS)

This directory contains External Data Service implementations for IBM Content Navigator.

## Available EDS

### ItalianLocationEDS
**Directory**: `ItalianLocationEDS/`
**Status**: ✅ Production Ready
**Version**: 1.1.0

Provides hierarchical selection of Italian states, provinces and municipalities for Content Navigator entry templates, with automatic Italian fiscal code (Codice Fiscale) calculation.

**Features**:

*Geographical selection*
- 239 world states with ISO codes (choice list)
- 107 Italian provinces (dependent on state = Italy)
- 7,904 Italian municipalities with Belfiore codes (dependent on province)
- Foreign state handling: when a non-Italian state is selected, province and municipality are automatically set to a configurable placeholder value

*Fiscal code calculation*
- Automatic calculation of the Italian Codice Fiscale from: surname, first name, date of birth, gender, and municipality/state of birth
- Real-time update in the form as the user fills in the fields
- Correct handling of ICN date serialization (UTC timezone conversion)
- Support for both Italian birth (municipality Belfiore code) and foreign birth (country tax code)
- Configurable symbolic names for all fields via `FiscalCodeConfig.json`

*Configuration*
- All property symbolic names configurable via JSON files (no recompilation needed)
- Foreign state placeholder value configurable via `ForeignStateConfig.json`

**Quick Start**:
```bash
# Requires JDK 8 and ICN lib directory
export ICN_LIB_DIR="/path/to/ECMClient/lib"
export JAVA8_HOME="/path/to/jdk1.8.0"
cd EDS/ItalianLocationEDS && ant -f build-war.xml
# Deploy dist/ItalianLocationEDS.war to WebSphere with context root /ItalianLocationEDS
```

[📖 Full Documentation](ItalianLocationEDS/README.md)

## Adding a New EDS

1. Create a new directory: `EDS/YourComponentNameEDS/`
2. Follow the structure of `ItalianLocationEDS/`
3. Include:
   - `src/` - Java servlet source code
   - `resources/` - JSON configuration and data files
   - `WebContent/WEB-INF/` - web.xml
   - `build-war.xml` - Ant build script
   - `README.md` - Component documentation
4. Update this README with your component

## Naming Convention

- Use PascalCase for directory names
- Add `EDS` suffix to identify component type
- Example: `CustomerDataEDS`, `ProductCatalogEDS`

## EDS Development

Use the **ICN EDS Builder** mode in Bob for guided EDS development:
- Location: `../Tools/BobModes/icn-eds-builder.mode.yaml`
- Installation: See `../Tools/BobModes/README_MODE_INSTALLATION.md`
- Usage Guide: See `../Tools/BobModes/ICN_EDS_BUILDER_MODE_GUIDE.md`

## Resources

- [IBM Content Navigator Knowledge Center](https://www.ibm.com/docs/en/content-navigator)
- [Project Main README](../README.md)
- [Shared Resources](../SharedResources/)