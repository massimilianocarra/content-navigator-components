# Content Navigator Project Structure

## Overview

This document describes the complete structure of the Content Navigator components repository after reorganization.

**Last Updated**: 2026-05-27  
**Version**: 1.0.0

## Directory Structure

```
Content Navigator/
├── .bob/                           # Bob AI configuration
│
├── EDS/                            # External Data Services
│   ├── README.md                   # EDS overview and guidelines
│   └── ItalianLocationEDS/         # Italian Location EDS
│       ├── src/                    # Java servlet source code
│       ├── resources/              # JSON configuration and data
│       ├── WebContent/WEB-INF/     # web.xml
│       ├── lib/                    # Project-specific libraries
│       ├── build-war.xml           # Ant build script
│       └── README.md               # Component documentation
│
├── Plugins/                        # ICN Plugins (PluginService-based)
│   └── README.md                   # Plugins overview and guidelines
│
├── Tools/                          # Development tools and utilities
│   ├── BobModes/                   # Bob custom modes
│   │   ├── icn-eds-builder.mode.yaml
│   │   ├── README_MODE_INSTALLATION.md
│   │   ├── ICN_EDS_BUILDER_MODE_GUIDE.md
│   │   └── README.md
│   └── Scripts/                    # Utility scripts (future)
│
├── Docs/                           # Project-wide documentation
│   └── PROJECT_STRUCTURE.md        # This file
│
├── SharedResources/                # Shared resources across components
│   ├── Data/                       # Common data files
│   │   ├── gi_province.json        # Italian provinces
│   │   └── gi_comuni.json          # Italian municipalities
│   └── Lib/                        # Shared libraries
│       └── README.md               # Library setup instructions
│
└── README.md                       # Main project README
```

## Naming Conventions

### Directory Names
- **PascalCase**: All directory names use PascalCase (e.g., `EDS`, `SharedResources`, `BobModes`)
- **Component Suffix**: Component directories include type suffix for clarity
  - EDS components: `ComponentNameEDS` (e.g., `ItalianLocationEDS`)
  - Plugins: `ComponentNamePlugin` (e.g., `WorkflowManagerPlugin`)
  - Tools: Descriptive names (e.g., `BobModes`, `Scripts`)

### File Names
- **Mode files**: kebab-case with `.mode.yaml` extension (e.g., `icn-eds-builder.mode.yaml`)
- **Documentation**: SCREAMING_SNAKE_CASE for guides (e.g., `README_MODE_INSTALLATION.md`)
- **Code files**: Follow language conventions (Java: PascalCase for classes, camelCase for methods)

## Component Types

### 1. External Data Services (EDS)

**Location**: `EDS/`

EDS components provide dynamic data to Content Navigator entry templates through HttpServlet-based WAR applications.

**Structure**:
```
ComponentNameEDS/
├── src/                            # Java source code
│   └── com/company/icn/eds/
│       └── servlets/
│           ├── GetObjectTypesServlet.java
│           └── UpdateObjectTypeServlet.java
├── resources/                      # Configuration and data
│   ├── ObjectTypes.json
│   ├── ClassName_PropertyData.json
│   └── [data files]
├── WebContent/WEB-INF/
│   └── web.xml                     # Servlet configuration
├── lib/                            # Component-specific libraries
├── build-war.xml                   # Ant build script
└── README.md                       # Component documentation
```

**Key Files**:
- `GetObjectTypesServlet.java`: Returns list of classes using EDS
- `UpdateObjectTypeServlet.java`: Returns property data and choice lists
- `web.xml`: Must use URL patterns `/types` and `/type/*`
- `build-war.xml`: Builds WAR file for deployment

### 2. Plugins

**Location**: `Plugins/`

Standard ICN plugins extending Content Navigator functionality through PluginService architecture.

**Structure** (to be defined when first plugin is added):
```
ComponentNamePlugin/
├── src/                            # Java source code
├── resources/                      # Plugin resources
├── WebContent/                     # Web resources
├── build.xml                       # Ant build script
└── README.md                       # Plugin documentation
```

### 3. Tools

**Location**: `Tools/`

Development tools and utilities to assist with component development.

#### Bob Modes (`Tools/BobModes/`)

Custom modes for Bob AI assistant providing expert guidance for specific development tasks.

**Current Modes**:
- `icn-eds-builder.mode.yaml`: Expert mode for EDS development

**Adding New Modes**:
1. Create `.mode.yaml` file with comprehensive role definition
2. Include usage examples and documentation
3. Update `Tools/BobModes/README.md`

#### Scripts (`Tools/Scripts/`)

Utility scripts for common tasks (to be populated as needed).

### 4. Shared Resources

**Location**: `SharedResources/`

Resources shared across multiple components.

#### Data (`SharedResources/Data/`)

Common data files used by multiple components:
- `gi_province.json`: Italian provinces (107 entries)
- `gi_comuni.json`: Italian municipalities (7,904 entries)

#### Libraries (`SharedResources/Lib/`)

Shared libraries and dependencies. IBM libraries are not included due to licensing and must be provided by the user.

**Required IBM Libraries**:
- `navigatorAPI.jar`: IBM Content Navigator API
- `edsPlugin.jar`: EDS Plugin framework (for EDS development)
- `JSON4J.jar`: JSON processing library

**Setup**: Set `ICN_LIB_DIR` environment variable pointing to ICN installation lib directory.

### 5. Documentation

**Location**: `Docs/`

Project-wide documentation including guides, best practices, and reference materials.

**Current Documentation**:
- `PROJECT_STRUCTURE.md`: This file

**Planned Documentation**:
- `GETTING_STARTED.md`: Quick start guide for new developers
- `DEVELOPMENT_GUIDE.md`: Development best practices
- `DEPLOYMENT_GUIDE.md`: Deployment procedures

## Adding New Components

### Adding an EDS

1. Create directory: `EDS/YourComponentNameEDS/`
2. Follow the structure of `EDS/ItalianLocationEDS/`
3. Implement required servlets:
   - `GetObjectTypesServlet`: Returns object types array
   - `UpdateObjectTypeServlet`: Returns property data
4. Create configuration files:
   - `ObjectTypes.json`: List of classes
   - `ClassName_PropertyData.json`: Property definitions
   - Data files for choice lists
5. Create `build-war.xml` for building WAR
6. Write comprehensive `README.md`
7. Update `EDS/README.md` with new component

### Adding a Plugin

1. Create directory: `Plugins/YourComponentNamePlugin/`
2. Implement plugin following IBM PluginService architecture
3. Include all necessary resources and web content
4. Create `build.xml` for building plugin JAR
5. Write comprehensive `README.md`
6. Update `Plugins/README.md` with new component

### Adding Tools

1. Bob modes go in `Tools/BobModes/`
2. Scripts go in `Tools/Scripts/`
3. Include clear documentation for each tool
4. Update respective README files

### Adding Documentation

1. Project-wide docs go in `Docs/`
2. Component-specific docs stay with component
3. Use clear, descriptive filenames
4. Keep documentation up to date with changes

## Build and Deployment

### Building EDS Components

```bash
cd EDS/ComponentNameEDS
export ICN_LIB_DIR=/path/to/icn/lib
ant -f build-war.xml
```

Output: `dist/ComponentNameEDS.war`

### Building Plugins

```bash
cd Plugins/ComponentNamePlugin
ant -f build.xml
```

Output: `dist/ComponentNamePlugin.jar`

## Development Workflow

### For EDS Development

1. Use the ICN EDS Builder mode in Bob for guidance
2. Follow the structure of `ItalianLocationEDS`
3. Test with curl before deploying to ICN
4. Deploy WAR to WebSphere
5. Configure edsPlugin in ICN Admin Console

### For Plugin Development

1. Follow IBM Content Navigator Plugin Development Guide
2. Use existing plugins as reference
3. Test thoroughly before deployment
4. Deploy JAR to ICN plugins directory

## Best Practices

### Code Organization

- Keep components self-contained
- Use shared resources for common data
- Follow naming conventions consistently
- Document all components thoroughly

### Version Control

- Commit components separately
- Use meaningful commit messages
- Tag releases appropriately
- Keep `.gitignore` updated

### Documentation

- Update README files when adding components
- Document configuration requirements
- Include usage examples
- Keep documentation in sync with code

### Testing

- Test components in isolation
- Verify integration with ICN
- Document test procedures
- Include test data when appropriate

## Migration History

### v1.0.0 (2026-05-27)

**Initial Reorganization**:
- Adopted PascalCase naming convention
- Created structured directory hierarchy
- Moved `ItalianLocationPlugin` → `EDS/ItalianLocationEDS/`
- Moved Bob modes to `Tools/BobModes/`
- Created comprehensive README files
- Established component guidelines

**Benefits**:
- Clear separation of component types
- Scalable structure for future components
- Consistent naming across project
- Easy navigation and discovery
- Professional organization

## Resources

### Internal Documentation
- [Main README](../README.md)
- [EDS Guidelines](../EDS/README.md)
- [Plugins Guidelines](../Plugins/README.md)
- [Tools Documentation](../Tools/BobModes/README.md)

### External Resources
- [IBM Content Navigator Documentation](https://www.ibm.com/docs/en/content-navigator)
- [FileNet P8 Documentation](https://www.ibm.com/docs/en/filenet-p8-platform)
- [WebSphere Application Server Documentation](https://www.ibm.com/docs/en/was)

## Support

For questions or issues:
1. Check component-specific README files
2. Review project documentation in `Docs/`
3. Use Bob with ICN EDS Builder mode for EDS questions
4. Consult IBM documentation for platform-specific issues

---

**Document Version**: 1.0.0  
**Last Updated**: 2026-05-27  
**Maintained By**: Project Team