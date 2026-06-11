# IBM Content Navigator - Components Repository

A comprehensive repository of IBM Content Navigator components including External Data Services (EDS), Plugins, and development tools.

## 📋 Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Components](#components)
- [Getting Started](#getting-started)
- [Development Tools](#development-tools)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)

## 🎯 Overview

This repository contains production-ready components for IBM Content Navigator:

- **External Data Services (EDS)**: Provide dynamic data to Content Navigator entry templates
- **Plugins**: Extend Content Navigator functionality
- **Development Tools**: Bob custom modes and utilities for component development
- **Shared Resources**: Common data files and libraries

## 📁 Project Structure

```
Content Navigator/
├── EDS/                        # External Data Services
│   └── ItalianLocationEDS/     # Italian provinces and municipalities EDS
├── Plugins/                    # ICN Plugins (future)
├── Tools/                      # Development tools
│   ├── BobModes/              # Bob AI assistant custom modes
│   └── Scripts/               # Utility scripts
├── Docs/                       # Project documentation
├── SharedResources/            # Shared data and libraries
│   ├── Data/                  # Common data files
│   └── Lib/                   # Shared libraries
└── README.md                  # This file
```

**Naming Convention**: All directories use PascalCase. Component directories include type suffix (e.g., `ItalianLocationEDS`, `WorkflowManagerPlugin`).

For detailed structure information, see [Docs/PROJECT_REORGANIZATION_PLAN.md](Docs/PROJECT_REORGANIZATION_PLAN.md).

## 🔌 Components

### External Data Services (EDS)

#### ItalianLocationEDS
**Status**: ✅ Production Ready
**Location**: `EDS/ItalianLocationEDS/`

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

[📖 Full Documentation](EDS/ItalianLocationEDS/README.md)

### Plugins

**Status**: 🚧 Coming Soon

Standard ICN plugins will be added here. Each plugin will have its own directory with complete source code and documentation.

## 🚀 Getting Started

### Prerequisites

- IBM Content Navigator 3.0.7 or higher
- FileNet P8 Content Engine
- WebSphere Application Server
- Java 8 or higher
- Apache Ant (for building)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd "Content Navigator"
   ```

2. **Choose a component**
   - For EDS: Navigate to `eds/<component-name>/`
   - For Plugins: Navigate to `plugins/<component-name>/`

3. **Follow component-specific README**
   Each component has detailed build and deployment instructions.

### For New Developers

1. Read [Docs/GETTING_STARTED.md](Docs/GETTING_STARTED.md) (to be created)
2. Review [Docs/DEVELOPMENT_GUIDE.md](Docs/DEVELOPMENT_GUIDE.md) (to be created)
3. Study the ItalianLocationEDS as a reference implementation
4. Use the ICN EDS Builder mode in Bob for EDS development

## 🛠️ Development Tools

### Bob Custom Modes

**Location**: `Tools/BobModes/`

#### ICN EDS Builder Mode
An AI-powered development assistant mode for Bob that provides expert guidance on creating EDS plugins.

**Features**:
- Complete EDS project generation
- Servlet implementation templates
- Configuration file generation
- Build script creation
- Troubleshooting assistance

**Installation**:
See [Tools/BobModes/README_MODE_INSTALLATION.md](Tools/BobModes/README_MODE_INSTALLATION.md)

**Usage Guide**:
See [Tools/BobModes/ICN_EDS_BUILDER_MODE_GUIDE.md](Tools/BobModes/ICN_EDS_BUILDER_MODE_GUIDE.md)

## 📚 Documentation

### Project Documentation

- [Docs/PROJECT_STRUCTURE.md](Docs/PROJECT_STRUCTURE.md) - Detailed structure explanation (to be created)
- [Docs/GETTING_STARTED.md](Docs/GETTING_STARTED.md) - Quick start guide (to be created)
- [Docs/DEVELOPMENT_GUIDE.md](Docs/DEVELOPMENT_GUIDE.md) - Development best practices (to be created)
- [Docs/DEPLOYMENT_GUIDE.md](Docs/DEPLOYMENT_GUIDE.md) - Deployment procedures (to be created)

### Component Documentation

Each component includes:
- `README.md` - Component overview and quick start
- Build instructions
- Configuration guide
- Deployment guide
- Technical notes

## 🤝 Contributing

### Adding a New EDS

1. Create directory: `EDS/YourComponentNameEDS/`
2. Follow the structure of `EDS/ItalianLocationEDS/`
3. Include comprehensive README.md
4. Update `EDS/README.md` with your component
5. Submit a pull request

### Adding a New Plugin

1. Create directory: `Plugins/YourPluginNamePlugin/`
2. Include standard plugin structure
3. Include comprehensive README.md
4. Update `Plugins/README.md` with your component
5. Submit a pull request

### Adding Tools or Documentation

1. Tools go in `Tools/`
2. Documentation goes in `Docs/`
3. Include clear usage instructions
4. Submit a pull request

### Development Guidelines

- Follow IBM Content Navigator best practices
- Include comprehensive documentation
- Provide working examples
- Test thoroughly before submitting
- Use the ICN EDS Builder mode for EDS development

## 🏗️ Architecture

### EDS Architecture

External Data Services use a WAR-based architecture with HttpServlets:

```
EDS Component
├── GetObjectTypesServlet    # Returns list of classes using EDS
├── UpdateObjectTypeServlet  # Returns property data and choice lists
├── Configuration Files      # JSON files for object types and properties
└── Data Files              # JSON files with choice list data
```

**Critical Requirements**:
- Must use HttpServlet, not PluginService
- URL patterns must be `/types` and `/type/*`
- GetObjectTypes must return direct JSON array

For detailed architecture, see component-specific documentation.

### Plugin Architecture

Standard ICN plugins follow the PluginService architecture (to be documented when plugins are added).

## 📦 Shared Resources

### Data Files

**Location**: `SharedResources/Data/`

Common data files used across multiple components:
- `gi_province.json` - Italian provinces (107 entries)
- `gi_comuni.json` - Italian municipalities (7,904 entries)

### Libraries

**Location**: `SharedResources/Lib/`

Shared libraries and dependencies (to be populated as needed).

## 🔍 Component Status

| Component | Type | Status | Version |
|-----------|------|--------|---------|
| ItalianLocationEDS | EDS | ✅ Production | 1.0.0 |
| ICN EDS Builder Mode | Tool | ✅ Ready | 1.0.0 |

## 📝 Version History

### v1.0.0 (2026-05-27)
- Initial repository structure with PascalCase naming convention
- ItalianLocationEDS (production ready)
- ICN EDS Builder mode for Bob
- Project reorganization for scalability

## 🆘 Support

### Getting Help

1. **Component Issues**: Check component-specific README and documentation
2. **EDS Development**: Use the ICN EDS Builder mode in Bob
3. **General Questions**: Review project documentation in `Docs/`
4. **Bug Reports**: Submit an issue with detailed information

### Resources

- IBM Content Navigator Knowledge Center
- FileNet P8 Documentation
- WebSphere Application Server Documentation
- This repository's documentation

## 📄 License

This repository contains components for IBM Content Navigator. Please ensure you have appropriate IBM licenses for Content Navigator and FileNet P8.

The code and documentation in this repository are provided as-is for use with IBM Content Navigator.

## 🎯 Roadmap

### Planned Components

- [ ] Additional EDS for common use cases
- [ ] Standard ICN plugins
- [ ] Utility scripts for deployment
- [ ] Additional Bob modes for plugin development
- [ ] Comprehensive testing framework

### Planned Documentation

- [ ] Complete getting started guide
- [ ] Development best practices guide
- [ ] Deployment procedures guide
- [ ] Troubleshooting guide
- [ ] API reference documentation

## 👥 Contributors

This repository was created to support IBM Content Navigator development and includes production-tested components.

---

**Ready to build Content Navigator components? Start with ItalianLocationEDS or use the ICN EDS Builder mode! 🚀**