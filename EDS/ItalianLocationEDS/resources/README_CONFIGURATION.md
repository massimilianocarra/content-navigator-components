# EDS Configuration Files

This directory contains configuration files for the External Data Service (EDS) plugin.

## Required Configuration Files

To use this plugin, you need to create two configuration files based on the provided examples:

### 1. ObjectTypes.json

**Purpose**: Specifies which document/folder classes will use the EDS functionality.

**How to create**:
```bash
cp ObjectTypes.json.example ObjectTypes.json
```

**Then edit** `ObjectTypes.json` and replace `YOUR_CLASS_SYMBOLIC_NAME` with your actual class symbolic name from ACCE.

**Example**:
```json
[
  {
    "symbolicName": "ClaimFolder"
  }
]
```

You can add multiple classes:
```json
[
  {
    "symbolicName": "ClaimFolder"
  },
  {
    "symbolicName": "PolicyFolder"
  }
]
```

### 2. <ClassName>_PropertyData.json

**Purpose**: Defines the properties and their behavior for each class that uses EDS.

**How to create**:
```bash
cp PropertyData.json.example YourClassName_PropertyData.json
```

**Important**: The filename MUST follow the pattern `<ClassName>_PropertyData.json` where `<ClassName>` matches the symbolic name in ObjectTypes.json.

**Example**: If your class is `ClaimFolder`, the file must be named:
```
ClaimFolder_PropertyData.json
```

**Content**: The example file already contains the correct configuration for Provincia and Comune properties. You only need to verify that:
- The property symbolic names match your ACCE property template names
- The display names are appropriate for your use case

## Finding Your Class Symbolic Name

1. Open **ACCE** (Administration Console for Content Engine)
2. Navigate to **Data Design** → **Classes**
3. Find your document or folder class
4. Look for the **Symbolic Name** field
5. Use this exact value in your configuration files

## Property Names

The default configuration uses:
- **Provincia** - For the province property
- **Comune** - For the municipality property

If your ACCE property templates have different names, update the `symbolicName` values in the PropertyData JSON file accordingly.

## File Structure

After configuration, your resources directory should contain:

```
resources/
├── README_CONFIGURATION.md              ← This file
├── ObjectTypes.json.example             ← Template (committed to git)
├── PropertyData.json.example            ← Template (committed to git)
├── ObjectTypes.json                     ← Your config (NOT committed to git)
├── YourClassName_PropertyData.json      ← Your config (NOT committed to git)
├── gi_province.json                     ← Province data (committed to git)
└── gi_comuni.json                       ← Municipality data (committed to git)
```

## Why These Files Are Not in Git

The `ObjectTypes.json` and `*_PropertyData.json` files are excluded from git (via .gitignore) because they contain project-specific configuration that varies between installations. Each user should create their own based on their ACCE class structure.

## Verification

After creating your configuration files, rebuild the plugin:

```bash
cd ItalianLocationPlugin
ant clean build
```

Then verify the files are included in the JAR:

```bash
jar tf dist/ItalianLocationPlugin.jar | grep json
```

You should see:
- ObjectTypes.json
- YourClassName_PropertyData.json
- gi_province.json
- gi_comuni.json

## Troubleshooting

**Problem**: Plugin loads but properties don't show choice lists

**Solution**: 
- Verify ObjectTypes.json contains your class symbolic name
- Verify the PropertyData filename matches: `<ClassName>_PropertyData.json`
- Verify property symbolic names in PropertyData match your ACCE properties
- Check ICN logs for errors

**Problem**: "Class not found" in logs

**Solution**: The symbolic name in ObjectTypes.json must exactly match the class symbolic name in ACCE (case-sensitive).

## More Information

See the main README.md for complete deployment and usage instructions.