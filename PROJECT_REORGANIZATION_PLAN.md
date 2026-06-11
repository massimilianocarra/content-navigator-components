# Content Navigator Project - Reorganization Plan

## Overview

This document describes how to reorganize the Content Navigator project to support multiple components (EDS, Plugins, etc.) in a scalable structure.

## Current Structure

```
Content Navigator/
├── .bob/
├── ItalianLocationPlugin/          # EDS for Italian locations
├── resources/                       # Duplicate resources
├── icn-eds-builder.mode.yaml       # Bob custom mode
├── README_MODE_INSTALLATION.md     # Mode installation guide
└── ICN_EDS_BUILDER_MODE_GUIDE.md  # Mode usage guide
```

## Proposed New Structure

```
Content Navigator/
├── .bob/                           # Bob configuration (unchanged)
│
├── eds/                            # External Data Services
│   ├── README.md                   # EDS overview and index
│   └── italian-location/           # Italian Location EDS
│       ├── src/
│       ├── resources/
│       ├── WebContent/
│       ├── lib/
│       ├── build-war.xml
│       └── README.md
│
├── plugins/                        # ICN Plugins (PluginService)
│   ├── README.md                   # Plugins overview and index
│   └── [future plugins here]
│
├── tools/                          # Development tools and utilities
│   ├── bob-modes/                  # Bob custom modes
│   │   ├── icn-eds-builder.mode.yaml
│   │   ├── README_MODE_INSTALLATION.md
│   │   └── ICN_EDS_BUILDER_MODE_GUIDE.md
│   └── scripts/                    # Utility scripts
│       └── [future scripts]
│
├── docs/                           # Project-wide documentation
│   ├── PROJECT_STRUCTURE.md        # This file
│   ├── GETTING_STARTED.md          # Quick start guide
│   ├── DEVELOPMENT_GUIDE.md        # Development guidelines
│   └── DEPLOYMENT_GUIDE.md         # Deployment procedures
│
├── shared-resources/               # Shared resources across components
│   ├── data/                       # Common data files
│   │   ├── gi_province.json
│   │   └── gi_comuni.json
│   └── lib/                        # Shared libraries
│       └── README.md               # Library documentation
│
└── README.md                       # Main project README
```

## Migration Steps

### Step 1: Create New Directory Structure

```bash
# Create main directories
mkdir -p eds plugins tools/bob-modes tools/scripts docs shared-resources/data shared-resources/lib

# Create README placeholders
touch eds/README.md
touch plugins/README.md
touch tools/bob-modes/README.md
touch shared-resources/lib/README.md
```

### Step 2: Move Italian Location EDS

```bash
# Move and rename ItalianLocationPlugin
mv ItalianLocationPlugin eds/italian-location

# The structure will be:
# eds/italian-location/
#   ├── src/
#   ├── resources/
#   ├── WebContent/
#   ├── lib/
#   ├── build-war.xml
#   └── README.md (already exists)
```

### Step 3: Move Bob Mode Files

```bash
# Move mode files to tools directory
mv icn-eds-builder.mode.yaml tools/bob-modes/
mv README_MODE_INSTALLATION.md tools/bob-modes/
mv ICN_EDS_BUILDER_MODE_GUIDE.md tools/bob-modes/
```

### Step 4: Move Shared Resources

```bash
# Move duplicate resources to shared location
mv resources/gi_province.json shared-resources/data/
mv resources/gi_comuni.json shared-resources/data/

# Remove old resources directory
rmdir resources
```

### Step 5: Create Documentation Files

Create the following files in the `docs/` directory:
- `PROJECT_STRUCTURE.md` - Detailed structure explanation
- `GETTING_STARTED.md` - Quick start for new developers
- `DEVELOPMENT_GUIDE.md` - Development best practices
- `DEPLOYMENT_GUIDE.md` - Deployment procedures

### Step 6: Create Main README

Create a comprehensive `README.md` at the project root that:
- Describes the project purpose
- Links to all components
- Provides quick navigation
- Explains how to contribute

### Step 7: Update References

After moving files, update references in:
- `eds/italian-location/build-war.xml` - Update paths if needed
- `tools/bob-modes/README_MODE_INSTALLATION.md` - Update file paths
- `tools/bob-modes/ICN_EDS_BUILDER_MODE_GUIDE.md` - Update example paths
- `.bob/custom_modes.yaml` - Update mode file path if imported

## Benefits of New Structure

### 1. Scalability
- Easy to add new EDS components in `eds/`
- Easy to add new plugins in `plugins/`
- Clear separation of concerns

### 2. Organization
- Related components grouped together
- Tools separated from implementations
- Documentation centralized

### 3. Maintainability
- Each component is self-contained
- Shared resources in one location
- Clear project structure

### 4. Discoverability
- README files guide navigation
- Logical directory names
- Consistent structure across components

## Component Guidelines

### Adding a New EDS

1. Create directory: `eds/your-eds-name/`
2. Follow the structure of `eds/italian-location/`
3. Include comprehensive README.md
4. Update `eds/README.md` with new component

### Adding a New Plugin

1. Create directory: `plugins/your-plugin-name/`
2. Include standard plugin structure
3. Include comprehensive README.md
4. Update `plugins/README.md` with new component

### Adding Tools

1. Bob modes go in `tools/bob-modes/`
2. Scripts go in `tools/scripts/`
3. Include documentation for each tool

### Adding Documentation

1. Project-wide docs go in `docs/`
2. Component-specific docs stay with component
3. Keep docs up to date with changes

## Migration Checklist

- [ ] Create new directory structure
- [ ] Move Italian Location EDS to `eds/italian-location/`
- [ ] Move Bob mode files to `tools/bob-modes/`
- [ ] Move shared resources to `shared-resources/data/`
- [ ] Create `eds/README.md`
- [ ] Create `plugins/README.md`
- [ ] Create `tools/bob-modes/README.md`
- [ ] Create `shared-resources/lib/README.md`
- [ ] Create `docs/PROJECT_STRUCTURE.md`
- [ ] Create `docs/GETTING_STARTED.md`
- [ ] Create `docs/DEVELOPMENT_GUIDE.md`
- [ ] Create `docs/DEPLOYMENT_GUIDE.md`
- [ ] Create main `README.md`
- [ ] Update all file path references
- [ ] Test that Italian Location EDS still builds
- [ ] Test that Bob mode still works
- [ ] Commit changes to version control

## Next Steps

After reorganization:

1. **Test Everything**: Ensure all components still work
2. **Update Documentation**: Reflect new structure in all docs
3. **Communicate Changes**: Inform team members
4. **Add New Components**: Start adding new EDS/plugins using new structure

## Questions?

If you have questions about the reorganization:
- Review the structure diagram above
- Check component-specific README files
- Consult the development guide in `docs/`
- Ask Bob (using ICN EDS Builder mode for EDS questions)

---

**Ready to reorganize? Follow the steps above and enjoy a cleaner, more scalable project structure!**