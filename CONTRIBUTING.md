# Contributing to Content Navigator Components

Thank you for your interest in contributing to this project! This document provides guidelines for contributing.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Guidelines](#development-guidelines)
- [Submitting Changes](#submitting-changes)
- [Reporting Issues](#reporting-issues)

## Code of Conduct

This project follows a code of conduct to ensure a welcoming environment for all contributors. Please be respectful and professional in all interactions.

## Getting Started

### Prerequisites

Before contributing, ensure you have:

1. **IBM Content Navigator** (version 3.0.7 or higher)
2. **FileNet P8** Content Engine
3. **Java Development Kit** (JDK 8 or higher)
4. **Apache Ant** for building
5. **Git** for version control
6. **IBM Content Navigator libraries** (navigatorAPI.jar, etc.)

### Setting Up Development Environment

1. Fork the repository
2. Clone your fork:
   ```bash
   git clone https://github.com/YOUR_USERNAME/content-navigator-components.git
   cd content-navigator-components
   ```
3. Set up IBM libraries:
   ```bash
   export ICN_LIB_DIR=/path/to/icn/lib
   ```
4. Build existing components to verify setup:
   ```bash
   cd EDS/ItalianLocationEDS
   ant -f build-war.xml
   ```

## How to Contribute

### Types of Contributions

We welcome:

1. **New EDS Components**: External Data Services for specific use cases
2. **New Plugins**: Standard ICN plugins
3. **Tools**: Development utilities and Bob modes
4. **Documentation**: Improvements to guides and examples
5. **Bug Fixes**: Corrections to existing components
6. **Enhancements**: Improvements to existing functionality

### Contribution Workflow

1. **Check existing issues** to avoid duplicate work
2. **Create an issue** describing your proposed contribution
3. **Wait for feedback** before starting significant work
4. **Create a feature branch** from `main`
5. **Implement your changes** following guidelines below
6. **Test thoroughly** in a real ICN environment
7. **Submit a pull request** with clear description

## Development Guidelines

### Naming Conventions

#### Directories
- Use **PascalCase** for all directory names
- Include component type suffix:
  - EDS: `ComponentNameEDS`
  - Plugins: `ComponentNamePlugin`
  - Tools: Descriptive names (e.g., `BobModes`, `Scripts`)

#### Files
- Java classes: PascalCase (e.g., `GetObjectTypesServlet.java`)
- Configuration files: lowercase with underscores (e.g., `build-war.xml`)
- Documentation: SCREAMING_SNAKE_CASE (e.g., `README.md`, `CONTRIBUTING.md`)

### Code Style

#### Java
- Follow standard Java conventions
- Use meaningful variable and method names
- Add JavaDoc comments for public methods
- Keep methods focused and concise
- Handle exceptions appropriately

Example:
```java
/**
 * Loads choice list data from JSON file.
 * 
 * @param propertyName The name of the property
 * @return JSONArray containing choice list items
 * @throws IOException if file cannot be read
 */
private JSONArray loadInitialChoices(String propertyName) throws IOException {
    String filename = "/" + propertyName + "_choices.json";
    InputStream stream = getClass().getResourceAsStream(filename);
    if (stream == null) {
        return new JSONArray();
    }
    return JSONArray.parse(stream);
}
```

#### XML
- Use proper indentation (2 or 4 spaces)
- Add comments for complex configurations
- Keep build scripts maintainable

#### JSON
- Use proper formatting with indentation
- Validate JSON syntax before committing
- Add comments in documentation about JSON structure

### Project Structure

When adding new components, follow the established structure:

#### For EDS Components

```
EDS/YourComponentNameEDS/
├── src/
│   └── com/company/icn/eds/servlets/
│       ├── GetObjectTypesServlet.java
│       └── UpdateObjectTypeServlet.java
├── resources/
│   ├── ObjectTypes.json
│   ├── ClassName_PropertyData.json
│   └── [data files]
├── WebContent/WEB-INF/
│   └── web.xml
├── lib/
├── build-war.xml
└── README.md
```

#### For Plugins

```
Plugins/YourComponentNamePlugin/
├── src/
├── resources/
├── WebContent/
├── build.xml
└── README.md
```

### Documentation Requirements

Every component must include:

1. **README.md** with:
   - Component description
   - Features list
   - Installation instructions
   - Configuration guide
   - Usage examples
   - Troubleshooting section

2. **Code comments** explaining:
   - Complex logic
   - Business rules
   - Integration points
   - Known limitations

3. **Build instructions** in README or separate guide

### Testing Requirements

Before submitting:

1. **Build successfully** without errors
2. **Test in ICN environment**:
   - Deploy to WebSphere
   - Configure in ICN Admin Console
   - Test all functionality
   - Verify error handling
3. **Document test results** in PR description
4. **Include test data** if applicable

### EDS-Specific Guidelines

When creating EDS components:

1. **Servlet Requirements**:
   - Implement `GetObjectTypesServlet` (returns object types array)
   - Implement `UpdateObjectTypeServlet` (returns property data)
   - Use URL patterns `/types` and `/type/*` (hardcoded in edsPlugin)

2. **JSON Format**:
   - GetObjectTypes must return direct array: `[{"symbolicName":"ClassName"}]`
   - PropertyData must include all required fields
   - Choice lists must follow proper structure

3. **Error Handling**:
   - Log errors to System.out/System.err
   - Return appropriate HTTP status codes
   - Provide meaningful error messages

4. **Performance**:
   - Cache static data when possible
   - Avoid loading data on every request
   - Use efficient data structures

## Submitting Changes

### Pull Request Process

1. **Update documentation** to reflect changes
2. **Update CHANGELOG** (if exists) with your changes
3. **Ensure all tests pass**
4. **Create pull request** with:
   - Clear title describing the change
   - Detailed description of what and why
   - Reference to related issues
   - Screenshots/logs if applicable
   - Test results

### Pull Request Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] New EDS component
- [ ] New Plugin
- [ ] Bug fix
- [ ] Enhancement
- [ ] Documentation
- [ ] Tool/Utility

## Testing
Describe testing performed:
- Build results
- ICN deployment results
- Functional testing results

## Checklist
- [ ] Code follows project conventions
- [ ] Documentation updated
- [ ] Build succeeds
- [ ] Tested in ICN environment
- [ ] No breaking changes (or documented)
```

### Review Process

1. Maintainers will review your PR
2. Address any feedback or requested changes
3. Once approved, your PR will be merged
4. Your contribution will be credited

## Reporting Issues

### Bug Reports

When reporting bugs, include:

1. **Component name** and version
2. **ICN version** and environment details
3. **Steps to reproduce** the issue
4. **Expected behavior**
5. **Actual behavior**
6. **Error messages** or logs
7. **Screenshots** if applicable

### Feature Requests

When requesting features, include:

1. **Use case** description
2. **Proposed solution** (if any)
3. **Alternatives considered**
4. **Additional context**

### Issue Template

```markdown
## Issue Type
- [ ] Bug Report
- [ ] Feature Request
- [ ] Documentation Issue
- [ ] Question

## Description
Clear description of the issue

## Environment
- ICN Version:
- Component:
- Java Version:
- OS:

## Steps to Reproduce (for bugs)
1. Step 1
2. Step 2
3. ...

## Expected Behavior

## Actual Behavior

## Additional Context
```

## Development Tips

### Using Bob AI Assistant

This project includes a custom Bob mode for EDS development:

1. Install the mode from `Tools/BobModes/icn-eds-builder.mode.yaml`
2. Activate "ICN EDS Builder" mode
3. Ask Bob for help with:
   - Generating EDS components
   - Troubleshooting issues
   - Best practices guidance

### Common Pitfalls

1. **URL Patterns**: Must use `/types` and `/type/*` (hardcoded in IBM's edsPlugin)
2. **JSON Format**: GetObjectTypes must return direct array, not wrapped object
3. **Libraries**: Don't commit IBM libraries (licensing issues)
4. **Testing**: Always test in real ICN environment, not just build

### Resources

- [IBM Content Navigator Documentation](https://www.ibm.com/docs/en/content-navigator)
- [Project Documentation](Docs/PROJECT_STRUCTURE.md)
- [EDS Guidelines](EDS/README.md)
- [Plugin Guidelines](Plugins/README.md)

## Questions?

If you have questions:

1. Check existing documentation
2. Search closed issues
3. Ask in a new issue
4. Use Bob with ICN EDS Builder mode

## License

By contributing, you agree that your contributions will be licensed under the MIT License (see LICENSE file).

---

Thank you for contributing to Content Navigator Components! 🎉