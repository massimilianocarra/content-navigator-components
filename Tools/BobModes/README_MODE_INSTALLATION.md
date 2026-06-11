# IBM Content Navigator EDS Builder Mode - Installation Guide

This guide explains how to install and use the ICN EDS Builder custom mode in Bob.

## What is this Mode?

The **ICN EDS Builder** is a specialized Bob mode that provides expert guidance for developing External Data Service (EDS) plugins for IBM Content Navigator. It was created based on real-world experience building production EDS implementations, including the Italian Location EDS plugin in this repository.

## Features

This mode helps you create EDS plugins with:

- ✅ **Choice Lists** from external data sources (JSON, databases, REST APIs)
- ✅ **Dependent Choice Lists** with hierarchical filtering (e.g., Province → Municipality)
- ✅ **Regex Validation** for document properties
- ✅ **Property Prefilling** based on context or business rules
- ✅ **Cross-Property Validation** with custom error messages
- ✅ **Complete Project Generation** including servlets, configs, and build scripts
- ✅ **Troubleshooting Guidance** for common EDS issues

## Installation Methods

### Method 1: Import via Bob UI (Recommended)

1. Open Bob in your IDE
2. Switch to **Mode Writer** mode (✍️)
3. Tell Bob: "Import the custom mode from icn-eds-builder.mode.yaml"
4. Bob will add it to your `.bob/custom_modes.yaml` file
5. The mode will be available immediately

### Method 2: Manual Installation

1. Open the file `.bob/custom_modes.yaml` in your project
2. If the file doesn't exist, create it with this content:
   ```yaml
   customModes: []
   ```
3. Open `icn-eds-builder.mode.yaml` and copy its entire content
4. Paste the content into `.bob/custom_modes.yaml` under the `customModes:` array
5. Save the file
6. Restart Bob or reload the workspace

### Method 3: Direct Copy

If you prefer to keep the mode in a separate file:

1. Keep `icn-eds-builder.mode.yaml` in your project root
2. Reference it when needed by telling Bob: "Use the ICN EDS Builder mode defined in icn-eds-builder.mode.yaml"

## How to Use

Once installed, you can activate the mode by:

1. **Via Mode Selector**: Click the mode dropdown in Bob and select "🔌 ICN EDS Builder"
2. **Via Command**: Tell Bob "Switch to ICN EDS Builder mode"
3. **Automatic**: Bob may suggest this mode when you mention EDS or Content Navigator

## Example Usage

After activating the mode, you can ask Bob things like:

```
"Create an EDS plugin for managing customer data with country and city selection"

"I need an EDS that loads product categories from a database"

"Help me troubleshoot why my EDS combo boxes are empty"

"Generate an EDS with validation for email and phone number fields"

"Create an EDS with dependent dropdowns: Department → Team → Employee"
```

## What Bob Will Do

When you request an EDS, Bob will:

1. **Ask clarifying questions** about your requirements
2. **Generate complete project structure** with all necessary files
3. **Create servlet implementations** following IBM best practices
4. **Provide configuration files** (web.xml, JSON configs)
5. **Generate build scripts** (Ant build.xml)
6. **Include testing commands** (curl examples)
7. **Provide deployment instructions** for WebSphere

## Critical Knowledge Embedded in This Mode

The mode includes hard-won knowledge from real EDS development:

- ✅ EDS **must** use HttpServlet, not PluginService
- ✅ URL patterns **must** be `/types` and `/type/*` (hardcoded in IBM's edsPlugin.jar)
- ✅ GetObjectTypesServlet **must** return a direct JSON array, not wrapped
- ✅ Proper implementation of dependent choice lists with `dependentOn`
- ✅ Correct JSON structure for PropertyData and choice lists
- ✅ Common pitfalls and how to avoid them

## Reference Implementation

This repository includes a complete working example:

- **ItalianLocationPlugin/** - Full EDS implementation
  - Manages 107 Italian provinces
  - Manages 7,904 Italian municipalities
  - Implements hierarchical Province → Municipality selection
  - Includes all source code, configs, and documentation

You can study this implementation to understand how EDS plugins work in practice.

## Requirements

To use this mode effectively, you should have:

- IBM Content Navigator 3.0.7 or higher
- FileNet P8 Content Engine
- WebSphere Application Server
- Java 8 or higher
- Apache Ant (for building)
- Required IBM JARs (navigatorAPI.jar, edsPlugin.jar)

## Troubleshooting

### Mode Not Appearing

- Check that `.bob/custom_modes.yaml` exists and is valid YAML
- Verify the mode is properly indented under `customModes:`
- Restart Bob or reload the workspace

### Mode Not Working as Expected

- Make sure you've activated the mode before asking questions
- The mode works best with specific EDS-related requests
- Try asking "What can you help me with?" after activating the mode

### Need Help?

- Review the Italian Location EDS plugin in this repository
- Check the documentation files in `ItalianLocationPlugin/`
- Ask Bob for help while in ICN EDS Builder mode

## Version History

- **v1.0.0** (2026-05-27) - Initial release based on Italian Location EDS project

## License

This mode definition is provided as-is for use with Bob. The knowledge and patterns are derived from IBM Content Navigator documentation and real-world implementation experience.

## Contributing

If you improve this mode or find issues:

1. Test your changes thoroughly
2. Document what you changed and why
3. Share your improvements with the community

---

**Happy EDS Development! 🔌**