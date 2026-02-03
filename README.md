<h1 align="center">
<img src="https://raw.githubusercontent.com/Sunnit-M/Fern/refs/heads/master/assets/FernIcon.png" alt="Project Logo" width="200" height="200"/>
<br/>
Fern
<hr/>
</h1>

## Disclaimer
The project is not at all finished and is still in early development.

## Overview
Fern is a JSON based Permission System made to be easy to use and implement, with different data storage methoads.

## Setup
1. Add Fern to your server.
2. Run Server And Allow Fern To Generate Default Config Files.
3. Stop The Server.
4. Select a data type using the table below in config.json
5. Start The Server Again.

## Data Storage Types
| Type | Description                                                             |
|------|-------------------------------------------------------------------------|
| `0`  | Stores Data With Permissions In Player (Player {Assigned Perms})        |
| `1`  | Stores Data With Players In Permissions (Permission {Players Assigned}) |

And data does not port to new data type yet.

## Usage
`Build.Gradle`
```gradle
repositories {
    maven {
        url = "https://api.modrinth.com/maven"
    }
}

dependencies {
    modImplementation "maven.modrinth:fernapi:${fern_version}"
}
```
`properties.gradle`
```properties
fern_version=0.4.1
```
All versions below 0.4.1 are not functional.


## What's Next
- Utility Mod
- WIKI
- Using Jackson for speed
- Port Between Data Storage Types
- Support for more platforms







