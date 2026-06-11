# IBM Content Navigator Plugins

This directory contains standard ICN plugin implementations (PluginService-based).

## Available Plugins

🚧 **Coming Soon**

Standard ICN plugins will be added here. Each plugin will have its own directory with complete source code and documentation.

## Plugin Structure

Each plugin should follow this structure:

```
PluginNamePlugin/
├── src/                    # Java source code
├── resources/              # Plugin resources
├── WebContent/            # Web resources
├── build.xml              # Ant build script
└── README.md              # Plugin documentation
```

## Naming Convention

- Use PascalCase for directory names
- Add `Plugin` suffix to identify component type
- Example: `DocumentViewerPlugin`, `WorkflowManagerPlugin`

## Adding a New Plugin

1. Create a new directory: `Plugins/YourPluginNamePlugin/`
2. Implement the plugin following IBM guidelines
3. Include comprehensive README.md
4. Update this README with your component

## Plugin Development

For plugin development guidance:
- Review IBM Content Navigator Plugin Development Guide
- Check existing plugins for reference
- Follow project development guidelines

## Resources

- [IBM Content Navigator Plugin Development](https://www.ibm.com/docs/en/content-navigator)
- [Project Main README](../README.md)