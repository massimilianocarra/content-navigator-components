# Bob Custom Modes for Content Navigator Development

This directory contains custom modes for Bob AI assistant to help with Content Navigator component development.

## Available Modes

### ICN EDS Builder Mode
**File**: `icn-eds-builder.mode.yaml`  
**Status**: ✅ Ready  
**Version**: 1.0.0

An expert mode for developing External Data Service (EDS) plugins for IBM Content Navigator.

**Features**:
- Complete EDS project generation
- Servlet implementation templates
- Configuration file generation
- Build script creation
- Troubleshooting assistance
- Best practices guidance

**Installation**: See [README_MODE_INSTALLATION.md](README_MODE_INSTALLATION.md)  
**Usage Guide**: See [ICN_EDS_BUILDER_MODE_GUIDE.md](ICN_EDS_BUILDER_MODE_GUIDE.md)

## Using Bob Modes

1. Install the mode following the installation guide
2. Activate the mode in Bob
3. Ask Bob to help with your development task
4. Bob will provide expert guidance and generate code

## Adding New Modes

To add a new Bob mode:
1. Create a `.mode.yaml` file in this directory
2. Include comprehensive role definition
3. Provide usage examples
4. Document installation and usage
5. Update this README

## Naming Convention

- Use kebab-case for mode file names
- Include descriptive name indicating purpose
- Example: `icn-plugin-builder.mode.yaml`, `icn-workflow-designer.mode.yaml`

## Resources

- [Bob Documentation](https://bob.build)
- [Project Main README](../../README.md)