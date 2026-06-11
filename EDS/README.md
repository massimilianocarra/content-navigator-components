# External Data Services (EDS)

This directory contains External Data Service implementations for IBM Content Navigator.

## Available EDS

### ItalianLocationEDS
**Directory**: `ItalianLocationEDS/`  
**Status**: ✅ Production Ready  
**Version**: 1.0.0

Provides hierarchical selection of Italian provinces and municipalities for Content Navigator entry templates.

**Features**:
- 107 Italian provinces
- 7,904 Italian municipalities
- Dependent choice lists (Province → Municipality)
- Fully tested and deployed

**Quick Start**:
```bash
cd EDS/ItalianLocationEDS
ant -f build-war.xml
# Deploy the generated WAR to WebSphere
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