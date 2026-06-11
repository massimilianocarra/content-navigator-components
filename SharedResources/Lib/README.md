# Shared Libraries

This directory contains libraries shared across multiple Content Navigator components.

## Required IBM Libraries

The following IBM libraries are required but not included in this repository (due to licensing):

### For EDS Development
- `navigatorAPI.jar` - IBM Content Navigator API
- `edsPlugin.jar` - EDS Plugin framework
- `JSON4J.jar` - JSON processing library

**Location**: Typically found in ICN installation directory:
- `/opt/IBM/ECMClient/lib/` (Linux)
- `C:\Program Files\IBM\ECMClient\lib\` (Windows)

### For Plugin Development
- `navigatorAPI.jar` - IBM Content Navigator API
- Additional plugin-specific libraries as needed

## Setup Instructions

1. Locate the required JARs in your ICN installation
2. Copy them to this directory, or
3. Set the `ICN_LIB_DIR` environment variable to point to your ICN lib directory

Example:
```bash
export ICN_LIB_DIR=/opt/IBM/ECMClient/lib
```

## Library Versions

Ensure library versions match your Content Navigator version:
- ICN 3.0.7 or higher recommended
- Check compatibility with your FileNet P8 version

## Resources

- [IBM Content Navigator Documentation](https://www.ibm.com/docs/en/content-navigator)
- [Project Main README](../../README.md)