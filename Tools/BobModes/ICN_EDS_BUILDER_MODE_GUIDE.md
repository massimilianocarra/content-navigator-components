# ICN EDS Builder Mode - Quick Start Guide

## 🎯 What This Mode Does

The **ICN EDS Builder** mode transforms Bob into an expert IBM Content Navigator EDS developer. It provides specialized knowledge and code generation capabilities for creating External Data Service plugins.

## 🚀 Quick Start

### 1. Install the Mode

Choose one method:

**Option A - Let Bob do it:**
```
Switch to Mode Writer mode and tell Bob:
"Import the custom mode from icn-eds-builder.mode.yaml"
```

**Option B - Manual:**
Copy the content of `icn-eds-builder.mode.yaml` into `.bob/custom_modes.yaml`

### 2. Activate the Mode

```
"Switch to ICN EDS Builder mode"
```

Or select "🔌 ICN EDS Builder" from the mode dropdown.

### 3. Start Building

Ask Bob to create your EDS:

```
"Create an EDS plugin for managing Italian provinces and municipalities"
```

Bob will guide you through the entire process!

## 📋 Common Use Cases

### Use Case 1: Simple Choice List

**Request:**
```
"Create an EDS that loads a list of countries from a JSON file"
```

**Bob will generate:**
- Complete servlet implementations
- JSON configuration files
- Build script
- Testing commands
- Deployment instructions

### Use Case 2: Dependent Choice Lists

**Request:**
```
"Create an EDS with hierarchical selection: Country → State → City"
```

**Bob will generate:**
- Servlets with dependency logic
- PropertyData.json with dependentOn configuration
- Choice list JSON files with parent-child relationships
- Complete working example

### Use Case 3: Database Integration

**Request:**
```
"Create an EDS that loads product categories from an Oracle database"
```

**Bob will generate:**
- Servlets with JDBC connection handling
- Database query examples
- Connection pooling recommendations
- Error handling for database operations

### Use Case 4: Validation

**Request:**
```
"Create an EDS with email and phone number validation"
```

**Bob will generate:**
- PropertyData.json with regex patterns
- Validation logic in servlet
- Custom error messages
- Testing examples

### Use Case 5: Property Prefilling

**Request:**
```
"Create an EDS that prefills the current date and logged-in user"
```

**Bob will generate:**
- Logic to extract user context
- Date formatting code
- Request mode handling
- Default value computation

## 🎓 Learning from Examples

### Example 1: Italian Location Plugin (In This Repository)

This is a complete, working EDS implementation:

```
ItalianLocationPlugin/
├── src/                          # Java servlet source code
├── resources/                    # JSON data files
├── WebContent/WEB-INF/          # web.xml configuration
└── build-war.xml                # Build script
```

**What it does:**
- Loads 107 Italian provinces
- Loads 7,904 Italian municipalities
- Implements Province → Municipality hierarchical selection
- Fully functional and tested

**How to study it:**
1. Read `ItalianLocationPlugin/README.md` for overview
2. Examine `GetObjectTypesServlet.java` for object types handling
3. Study `UpdateObjectTypeServlet.java` for choice list logic
4. Review `PropertyData.json` for dependent configuration
5. Check `web.xml` for servlet registration

### Example 2: Ask Bob for More Examples

While in ICN EDS Builder mode:

```
"Show me an example of loading data from a REST API"
"Show me an example of caching choice lists for performance"
"Show me an example of cross-property validation"
```

## 🔧 Development Workflow

When you ask Bob to create an EDS, here's what happens:

### Step 1: Requirements Gathering
Bob asks clarifying questions:
- Which document classes need EDS?
- What properties need dynamic behavior?
- What are your data sources?
- Any property dependencies?
- Validation rules needed?

### Step 2: Project Generation
Bob creates complete structure:
```
YourEDSProject/
├── src/com/company/icn/eds/servlets/
│   ├── GetObjectTypesServlet.java
│   └── UpdateObjectTypeServlet.java
├── resources/
│   ├── ObjectTypes.json
│   ├── ClassName_PropertyData.json
│   └── [your data files]
├── WebContent/WEB-INF/
│   └── web.xml
└── build-war.xml
```

### Step 3: Configuration Files
Bob generates all JSON configs:
- ObjectTypes.json (classes using EDS)
- PropertyData.json (property definitions)
- Choice list JSON files (data sources)

### Step 4: Build & Test
Bob provides:
- Ant build script
- Curl test commands
- Deployment instructions

### Step 5: Deployment Guide
Bob explains:
- How to build the WAR
- How to deploy to WebSphere
- How to configure edsPlugin
- How to test in ICN

## 💡 Pro Tips

### Tip 1: Be Specific
Instead of: "Create an EDS"
Better: "Create an EDS for document class 'Invoice' with dependent dropdowns for Vendor → Department"

### Tip 2: Mention Your Data Source
Tell Bob where your data comes from:
- "Load from JSON files"
- "Load from Oracle database"
- "Load from REST API at https://api.example.com"

### Tip 3: Describe Dependencies
Be clear about relationships:
- "Country filters State, State filters City"
- "Department determines available Cost Centers"

### Tip 4: Specify Validation
Mention validation requirements:
- "Email must be valid format"
- "Phone must be 10 digits"
- "Postal code must match country format"

### Tip 5: Ask for Explanations
Bob can explain concepts:
- "Explain how dependent choice lists work"
- "Why must I use /types and /type/* URL patterns?"
- "How does edsPlugin communicate with my servlet?"

## 🐛 Troubleshooting

### Problem: Empty Combo Boxes

**Ask Bob:**
```
"My combo boxes are empty. Help me troubleshoot."
```

**Bob will check:**
- JSON format in choice lists
- Servlet response structure
- URL patterns in web.xml
- edsPlugin configuration

### Problem: Dependencies Not Working

**Ask Bob:**
```
"My dependent choice list isn't filtering. What's wrong?"
```

**Bob will verify:**
- `dependentOn` property in PropertyData.json
- `parentValue` in choice list items
- Parent value extraction logic
- Request/response flow

### Problem: 404 Errors

**Ask Bob:**
```
"I'm getting 404 errors when ICN calls my EDS"
```

**Bob will check:**
- URL patterns (must be /types and /type/*)
- WAR deployment context root
- edsPlugin URL configuration
- Servlet registration in web.xml

### Problem: Performance Issues

**Ask Bob:**
```
"My EDS is slow. How can I optimize it?"
```

**Bob will suggest:**
- Caching strategies
- Database connection pooling
- Lazy loading techniques
- Response size optimization

## 📚 Advanced Topics

### Topic 1: Multiple Data Sources

**Ask Bob:**
```
"Create an EDS that combines data from a database and a REST API"
```

### Topic 2: Dynamic Validation

**Ask Bob:**
```
"Create an EDS where validation rules change based on document type"
```

### Topic 3: Computed Values

**Ask Bob:**
```
"Create an EDS that calculates a property value based on other properties"
```

### Topic 4: User Context

**Ask Bob:**
```
"Create an EDS that uses the logged-in user's department to filter choices"
```

### Topic 5: Internationalization

**Ask Bob:**
```
"Create an EDS that supports multiple languages for choice list labels"
```

## 🎯 Best Practices (Bob Knows These)

Bob will automatically apply these best practices:

✅ **Correct Architecture**: Always uses HttpServlet, never PluginService
✅ **Standard URL Patterns**: Always uses /types and /type/*
✅ **Proper JSON Format**: GetObjectTypes returns direct array
✅ **Error Handling**: Comprehensive try-catch with logging
✅ **Performance**: Suggests caching for static data
✅ **Security**: Validates input, sanitizes external data
✅ **Testing**: Provides curl commands before ICN testing
✅ **Documentation**: Comments critical code sections

## 🔗 Related Resources

### In This Repository
- `ItalianLocationPlugin/` - Complete working example
- `ItalianLocationPlugin/README.md` - Project overview
- `ItalianLocationPlugin/TECHNICAL_NOTES.md` - Implementation details
- `ItalianLocationPlugin/DEPLOYMENT_GUIDE_WAR.md` - Deployment steps

### IBM Documentation
- IBM Content Navigator Knowledge Center
- FileNet P8 Documentation
- WebSphere Application Server Documentation

### Ask Bob
While in ICN EDS Builder mode, Bob can:
- Explain IBM concepts
- Generate code examples
- Troubleshoot issues
- Suggest optimizations
- Provide best practices

## 🎉 Success Stories

### Italian Location Plugin
**Challenge**: Manage 107 provinces and 7,904 municipalities with hierarchical selection

**Solution**: Bob (in EDS Builder mode) generated:
- Complete servlet implementation
- Proper dependent choice list configuration
- Efficient JSON data structure
- Working build and deployment process

**Result**: Fully functional EDS deployed in production

### Your Project Could Be Next!
Activate ICN EDS Builder mode and start building your EDS today.

## 📞 Getting Help

### From Bob
```
"I need help with [specific EDS issue]"
"Explain how [EDS concept] works"
"Show me an example of [EDS feature]"
```

### From This Repository
- Study the Italian Location Plugin
- Read the technical documentation
- Review the servlet implementations

### From the Community
- Share your EDS implementations
- Contribute improvements to this mode
- Help others with EDS development

---

**Ready to build your EDS? Activate ICN EDS Builder mode and let's get started! 🚀**