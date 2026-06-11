# GitHub Publication Guide

This guide explains how to publish the Content Navigator Components project to GitHub using either **GitHub Desktop** (recommended for beginners) or **Command Line**.

## Prerequisites

- GitHub account
- **Option A**: [GitHub Desktop](https://desktop.github.com/) installed (recommended)
- **Option B**: Git command line tools installed

---

# Method 1: Using GitHub Desktop (Recommended)

GitHub Desktop provides a user-friendly interface for Git operations. Follow these steps:

## Step 1: Install GitHub Desktop

1. Download from https://desktop.github.com/
2. Install and launch GitHub Desktop
3. Sign in with your GitHub account (File → Options → Accounts → Sign in)

## Step 2: Add Your Project to GitHub Desktop

1. In GitHub Desktop, click **File → Add Local Repository**
2. Click **Choose...** and navigate to:
   ```
   /Users/massyc/Tech/Bob/Projects/Content Navigator
   ```
3. Click **Add Repository**

**If you see "This directory does not appear to be a Git repository":**
1. Click **Create a repository** instead
2. The path should already be filled in
3. **Uncheck** "Initialize this repository with a README" (we already have one)
4. Click **Create Repository**

## Step 3: Review Changes

In GitHub Desktop, you'll see:
- **Left panel**: List of changed files
- **Right panel**: Diff showing what changed in selected file
- Files excluded by `.gitignore` won't appear (build directories, JAR files, etc.)

**Verify that these are NOT listed** (they should be ignored):
- `build-war/` directories
- `*.jar` files
- `dist/` directories
- `.vscode/` directories

## Step 4: Create Initial Commit

1. In the **Summary** field (bottom left), enter:
   ```
   Initial commit: Content Navigator Components
   ```

2. In the **Description** field, enter:
   ```
   - ItalianLocationEDS: Production-ready EDS for Italian locations
   - ICN EDS Builder: Bob custom mode for EDS development
   - Project structure with PascalCase naming convention
   - Comprehensive documentation
   - Build scripts and configuration
   ```

3. Click **Commit to main** button

## Step 5: Publish to GitHub

1. Click **Publish repository** button (top right)
2. In the dialog:
   - **Name**: `content-navigator-components` (or your preferred name)
   - **Description**: `IBM Content Navigator components including EDS, Plugins, and development tools`
   - **Keep this code private**: Uncheck for public repository (or check for private)
   - **Organization**: Select if publishing to an organization
3. Click **Publish Repository**

GitHub Desktop will create the repository on GitHub and push your code!

## Step 6: View on GitHub

1. In GitHub Desktop, click **Repository → View on GitHub**
2. Your repository will open in your browser

## Step 7: Configure Repository on GitHub

### Add Topics (Tags)

1. On your repository page, click the ⚙️ icon next to "About"
2. Add topics:
   - `ibm-content-navigator`
   - `filenet-p8`
   - `eds`
   - `java`
   - `enterprise`
   - `document-management`
3. Click **Save changes**

### Update Description

In the same dialog, ensure the description is clear:
```
IBM Content Navigator components including External Data Services (EDS), Plugins, and development tools. Features production-ready ItalianLocationEDS and Bob AI assistant mode for EDS development.
```

## Step 8: Create a Release (Optional)

1. On GitHub, go to **Releases** (right sidebar)
2. Click **Create a new release**
3. Click **Choose a tag** and type: `v1.0.0`
4. Click **Create new tag: v1.0.0 on publish**
5. **Release title**: `v1.0.0 - Initial Release`
6. **Description**:
```markdown
## 🎉 Initial Release

### Components

#### ItalianLocationEDS
- Production-ready EDS for Italian provinces and municipalities
- 107 provinces, 7,904 municipalities
- Hierarchical selection (Province → Municipality)
- Fully tested and deployed

#### ICN EDS Builder Mode
- Bob AI assistant custom mode
- Expert guidance for EDS development
- Complete project generation
- Troubleshooting assistance

### Features

- ✅ PascalCase naming convention
- ✅ Scalable project structure
- ✅ Comprehensive documentation
- ✅ Build scripts included
- ✅ Production-tested components

### Requirements

- IBM Content Navigator 3.0.7+
- FileNet P8 Content Engine
- Java 8+
- Apache Ant

### Installation

See [README.md](README.md) for installation instructions.
```
7. Click **Publish release**

## Making Changes with GitHub Desktop

### After Initial Publication

1. Make changes to your files in your editor
2. Open GitHub Desktop - it will show the changes
3. Review the changes in the diff view
4. Enter a commit message describing your changes
5. Click **Commit to main**
6. Click **Push origin** to upload changes to GitHub

### Creating Feature Branches

1. Click **Current Branch** dropdown (top)
2. Click **New Branch**
3. Name it (e.g., `feature/add-customer-eds`)
4. Click **Create Branch**
5. Make your changes and commit
6. Click **Publish branch** to push to GitHub
7. Click **Create Pull Request** to merge back to main

### Pulling Latest Changes

1. Click **Fetch origin** (top right) to check for updates
2. If updates are available, click **Pull origin**

---

# Method 2: Using Command Line

For users comfortable with command line, here are the Git commands:

## Step 1: Initialize Git Repository (if not already done)

```bash
cd "/Users/massyc/Tech/Bob/Projects/Content Navigator"
git init
```

## Step 2: Review Files to Commit

Check what will be committed:

```bash
git status
```

The `.gitignore` file is configured to exclude:
- Build directories (`build-war/`, `dist/`, `bin/`)
- IBM libraries (`*.jar` in lib directories)
- IDE files (`.vscode/`, `.idea/`)
- Compiled files (`*.class`)
- Generated WAR files

## Step 3: Add Files to Git

```bash
# Add all files (respecting .gitignore)
git add .

# Verify what will be committed
git status
```

## Step 4: Create Initial Commit

```bash
git commit -m "Initial commit: Content Navigator Components

- ItalianLocationEDS: Production-ready EDS for Italian locations
- ICN EDS Builder: Bob custom mode for EDS development
- Project structure with PascalCase naming convention
- Comprehensive documentation
- Build scripts and configuration"
```

## Step 5: Create GitHub Repository

### Option A: Via GitHub Web Interface

1. Go to https://github.com/new
2. Repository name: `content-navigator-components` (or your preferred name)
3. Description: "IBM Content Navigator components including EDS, Plugins, and development tools"
4. Choose Public or Private
5. **Do NOT** initialize with README, .gitignore, or license (we already have them)
6. Click "Create repository"

### Option B: Via GitHub CLI

```bash
# Install GitHub CLI if not already installed
# https://cli.github.com/

# Login to GitHub
gh auth login

# Create repository
gh repo create content-navigator-components \
  --public \
  --description "IBM Content Navigator components including EDS, Plugins, and development tools" \
  --source=. \
  --remote=origin
```

## Step 6: Connect Local Repository to GitHub

If you created the repo via web interface:

```bash
# Add remote (replace YOUR_USERNAME with your GitHub username)
git remote add origin https://github.com/YOUR_USERNAME/content-navigator-components.git

# Verify remote
git remote -v
```

## Step 7: Push to GitHub

```bash
# Push to main branch
git branch -M main
git push -u origin main
```

## Step 8: Configure Repository Settings

### Add Topics (Tags)

Go to your repository on GitHub and add topics:
- `ibm-content-navigator`
- `filenet-p8`
- `eds`
- `java`
- `enterprise`
- `document-management`

### Enable Issues

1. Go to Settings → Features
2. Enable "Issues"

### Add Repository Description

Add a clear description:
```
IBM Content Navigator components including External Data Services (EDS), Plugins, and development tools. Features production-ready ItalianLocationEDS and Bob AI assistant mode for EDS development.
```

### Configure Branch Protection (Optional)

For collaborative projects:
1. Go to Settings → Branches
2. Add rule for `main` branch
3. Enable:
   - Require pull request reviews
   - Require status checks to pass
   - Require branches to be up to date

## Step 9: Create Release (Optional)

### Via GitHub Web Interface

1. Go to Releases → Create a new release
2. Tag version: `v1.0.0`
3. Release title: `v1.0.0 - Initial Release`
4. Description:
```markdown
## 🎉 Initial Release

### Components

#### ItalianLocationEDS
- Production-ready EDS for Italian provinces and municipalities
- 107 provinces, 7,904 municipalities
- Hierarchical selection (Province → Municipality)
- Fully tested and deployed

#### ICN EDS Builder Mode
- Bob AI assistant custom mode
- Expert guidance for EDS development
- Complete project generation
- Troubleshooting assistance

### Features

- ✅ PascalCase naming convention
- ✅ Scalable project structure
- ✅ Comprehensive documentation
- ✅ Build scripts included
- ✅ Production-tested components

### Requirements

- IBM Content Navigator 3.0.7+
- FileNet P8 Content Engine
- Java 8+
- Apache Ant

### Installation

See [README.md](README.md) for installation instructions.
```

### Via GitHub CLI

```bash
gh release create v1.0.0 \
  --title "v1.0.0 - Initial Release" \
  --notes "Initial release with ItalianLocationEDS and ICN EDS Builder mode"
```

## Step 10: Add Badges to README (Optional)

Add badges at the top of README.md:

```markdown
# IBM Content Navigator - Components Repository

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![GitHub release](https://img.shields.io/github/release/YOUR_USERNAME/content-navigator-components.svg)](https://github.com/YOUR_USERNAME/content-navigator-components/releases)
[![GitHub issues](https://img.shields.io/github/issues/YOUR_USERNAME/content-navigator-components.svg)](https://github.com/YOUR_USERNAME/content-navigator-components/issues)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)
```

## Step 11: Set Up GitHub Pages (Optional)

For documentation hosting:

1. Go to Settings → Pages
2. Source: Deploy from a branch
3. Branch: `main`, folder: `/Docs`
4. Save

---

## GitHub Desktop: Common Tasks and Tips

### Quick Actions

#### View Repository on GitHub
- **Repository → View on GitHub** 
- Keyboard: `Ctrl+Shift+G` (Windows) or `Cmd+Shift+G` (Mac)

#### Open in Terminal
- **Repository → Open in Terminal**
- Keyboard: `Ctrl+`` (Windows) or `Cmd+`` (Mac)

#### Open in File Explorer/Finder
- **Repository → Show in Explorer** (Windows)
- **Repository → Show in Finder** (Mac)

#### View Commit History
1. Click **History** tab (top left)
2. Browse through all commits
3. Click any commit to see detailed changes
4. Right-click commits for more options

### Managing Changes

#### Discarding Uncommitted Changes
If you want to undo changes before committing:
1. Right-click the file in the changes list
2. Select **Discard Changes**
3. Confirm the action (this cannot be undone!)

#### Discarding All Changes
- Click **Discard All Changes** at the bottom of the changes panel
- Use with caution - this removes all uncommitted work

#### Stashing Changes
To temporarily save changes without committing:
1. Click the **Stash** dropdown (bottom of changes panel)
2. Select **Stash all changes**
3. Give it a descriptive name
4. Later: **Stashed Changes** menu → Right-click stash → **Restore**

### Working with Branches

#### Creating a New Branch
1. Click **Current Branch** dropdown (top center)
2. Click **New Branch**
3. Enter branch name (e.g., `feature/add-customer-eds`)
4. Choose base branch (usually `main`)
5. Click **Create Branch**

#### Switching Branches
1. Click **Current Branch** dropdown
2. Select the branch you want to switch to
3. GitHub Desktop will switch and show that branch's files

#### Merging Branches
1. Switch to the branch you want to merge INTO (e.g., `main`)
2. Click **Branch → Merge into Current Branch**
3. Select the branch to merge FROM
4. Click **Merge**
5. Resolve any conflicts if they appear
6. Push the merged changes

#### Deleting a Branch
1. Click **Current Branch** dropdown
2. Right-click the branch you want to delete
3. Select **Delete**
4. Confirm deletion

### Resolving Merge Conflicts

When GitHub Desktop shows conflicts:

1. **Identify Conflicted Files**: They'll be marked with a warning icon
2. **Open in Editor**: Click **Open in [your editor]** button
3. **Resolve Conflicts**: Look for conflict markers in your editor:
   ```
   <<<<<<< HEAD
   Your changes
   =======
   Incoming changes
   >>>>>>> branch-name
   ```
4. **Choose Resolution**: Keep one version, combine both, or write new code
5. **Save File**: Save the resolved file
6. **Mark as Resolved**: Return to GitHub Desktop
7. **Commit**: The conflicted files will now appear as resolved
8. **Complete Merge**: Commit the merge

### Syncing with GitHub

#### Fetching Updates
- Click **Fetch origin** (top right) to check for remote changes
- This doesn't change your local files, just checks what's new

#### Pulling Changes
- If **Fetch origin** shows updates, click **Pull origin**
- This downloads and merges remote changes into your local branch

#### Pushing Changes
- After committing locally, click **Push origin** to upload to GitHub
- The button shows how many commits will be pushed

### Undoing Mistakes

#### Undo Last Commit (Keep Changes)
1. Go to **History** tab
2. Right-click the most recent commit
3. Select **Undo Commit**
4. Changes return to uncommitted state

#### Revert a Commit (Create Opposite Commit)
1. Go to **History** tab
2. Right-click the commit to revert
3. Select **Revert Changes in Commit**
4. A new commit is created that undoes the changes

#### Amend Last Commit
1. Make additional changes
2. Stage them in GitHub Desktop
3. Check **Amend previous commit** (bottom left)
4. Update commit message if needed
5. Click **Commit to [branch]**

### Viewing Diffs

#### File Diff View
- Click any changed file to see line-by-line differences
- Green = added lines
- Red = removed lines
- Split view or unified view available

#### Viewing Specific Lines
- Click line numbers to see more context
- Use the diff view to review changes before committing

### Pull Requests

#### Creating a Pull Request
1. Push your feature branch to GitHub
2. Click **Create Pull Request** button (appears after push)
3. GitHub opens in browser with PR form
4. Fill in title and description
5. Click **Create Pull Request**

#### Viewing Pull Requests
- **Branch → View Pull Requests** to see all PRs
- Click a PR to view details in browser

---

## Troubleshooting

### GitHub Desktop Issues

#### Repository Not Showing Changes

**Symptoms**: Files changed but not appearing in GitHub Desktop

**Solutions**:
1. Click **Repository → Refresh** (or press `Ctrl+R` / `Cmd+R`)
2. Verify you're on the correct branch
3. Check if files are listed in `.gitignore`
4. Close and reopen GitHub Desktop
5. Check file permissions (files must be readable)

#### "Authentication Failed" Error

**Symptoms**: Can't push/pull, authentication error

**Solutions**:
1. **Sign out and back in**:
   - Windows: **File → Options → Accounts**
   - Mac: **GitHub Desktop → Preferences → Accounts**
   - Click **Sign out**, then **Sign in** again
2. **Check GitHub status**: Visit https://www.githubstatus.com/
3. **Use Personal Access Token**:
   - Generate at https://github.com/settings/tokens
   - Use token as password when prompted

#### Can't Push to GitHub

**Symptoms**: Push button grayed out or fails

**Solutions**:
1. **Fetch first**: Click **Fetch origin**
2. **Pull if needed**: If remote has changes, click **Pull origin**
3. **Resolve conflicts**: Fix any merge conflicts
4. **Check branch**: Ensure you're on the correct branch
5. **Verify permissions**: Ensure you have write access to repository

#### Repository Shows "Uncommitted Changes" but List is Empty

**Symptoms**: Changes indicator but no files shown

**Solutions**:
1. Check if all changed files are in `.gitignore`
2. Click **Repository → Refresh**
3. Look in **Stashed Changes** - changes might be stashed
4. Close and reopen GitHub Desktop
5. Check for file permission issues
6. Verify repository path is correct

#### "This Repository is Missing" Error

**Symptoms**: GitHub Desktop can't find repository

**Solutions**:
1. Verify folder wasn't moved or deleted
2. **Repository → Remove** then re-add the repository
3. Check if folder is on external drive that's disconnected
4. Clone repository again if necessary

#### Merge Conflicts Won't Resolve

**Symptoms**: Conflicts persist after editing files

**Solutions**:
1. Ensure you removed ALL conflict markers (`<<<<<<<`, `=======`, `>>>>>>>`)
2. Save the file after editing
3. Return to GitHub Desktop and verify file shows as resolved
4. If stuck, **Repository → Open in Terminal** and use:
   ```bash
   git status  # Check conflict status
   git add .   # Stage resolved files
   ```

#### Can't Switch Branches

**Symptoms**: Branch switch fails or is disabled

**Solutions**:
1. **Commit or stash changes**: You must commit or stash uncommitted changes first
2. **Stash changes**: Click **Stash all changes** before switching
3. **Check for conflicts**: Resolve any existing conflicts
4. **Force switch** (loses uncommitted changes):
   - **Repository → Open in Terminal**
   - Run: `git checkout -f branch-name`

### Command Line Issues


## Ongoing Maintenance

### Making Changes

```bash
# Create feature branch
git checkout -b feature/your-feature-name

# Make changes
# ...

# Commit changes
git add .
git commit -m "Description of changes"

# Push to GitHub
git push origin feature/your-feature-name

# Create pull request on GitHub
```

### Updating Main Branch

```bash
# Switch to main
git checkout main

# Pull latest changes
git pull origin main

# Merge feature branch (if not using PRs)
git merge feature/your-feature-name

# Push to GitHub
git push origin main
```

### Creating New Releases

```bash
# Tag new version
git tag -a v1.1.0 -m "Version 1.1.0 - Description"

# Push tag
git push origin v1.1.0

# Create release on GitHub
gh release create v1.1.0 --title "v1.1.0 - Release Title" --notes "Release notes"
```

## Best Practices

### Commit Messages

Follow conventional commits:
- `feat:` New feature
- `fix:` Bug fix
- `docs:` Documentation changes
- `refactor:` Code refactoring
- `test:` Adding tests
- `chore:` Maintenance tasks

Example:
```bash
git commit -m "feat(EDS): Add CustomerDataEDS component

- Implements customer data choice lists
- Supports filtering by region
- Includes comprehensive documentation"
```

### Branch Naming

- `feature/` - New features
- `fix/` - Bug fixes
- `docs/` - Documentation updates
- `refactor/` - Code refactoring

Example: `feature/add-customer-data-eds`

### Pull Requests

- Use descriptive titles
- Reference related issues
- Include test results
- Request reviews from maintainers

## Troubleshooting

### Large Files

If you accidentally committed large files:

```bash
# Remove from Git but keep locally
git rm --cached path/to/large/file

# Add to .gitignore
echo "path/to/large/file" >> .gitignore

# Commit
git commit -m "Remove large file from Git"
```

### Sensitive Data

If you committed sensitive data:

1. Remove from repository
2. Change any exposed credentials
3. Consider using `git filter-branch` or BFG Repo-Cleaner
4. Force push (if repository is private and you're the only user)

### Authentication Issues

If push fails with authentication error:

```bash
# Use personal access token
# Generate at: https://github.com/settings/tokens

# Update remote URL
git remote set-url origin https://YOUR_TOKEN@github.com/YOUR_USERNAME/content-navigator-components.git
```

Or use SSH:

```bash
# Generate SSH key if needed
ssh-keygen -t ed25519 -C "your_email@example.com"

# Add to GitHub: Settings → SSH and GPG keys

# Update remote URL
git remote set-url origin git@github.com:YOUR_USERNAME/content-navigator-components.git
```

## Repository Checklist

Before making repository public:

- [ ] `.gitignore` configured correctly
- [ ] No sensitive data in commits
- [ ] No IBM proprietary libraries committed
- [ ] LICENSE file present
- [ ] README.md comprehensive
- [ ] CONTRIBUTING.md present
- [ ] Documentation complete
- [ ] Build scripts tested
- [ ] Repository description set
- [ ] Topics/tags added
- [ ] Issues enabled

## Resources

- [GitHub Docs](https://docs.github.com/)
- [Git Documentation](https://git-scm.com/doc)
- [GitHub CLI](https://cli.github.com/)
- [Conventional Commits](https://www.conventionalcommits.org/)

---

**Ready to publish? Follow the steps above and share your Content Navigator components with the community! 🚀**