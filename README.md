<h1 align="center"> Java Spring & React Library </h1>

# 1. Java Spring
## Overview
- Base Table entity to be extended, shorten table creation time.
- Utilities: Data serializer, Date, String.
- Advanced data tranfer object (DTO) called 'Record'. It extends LinkedHashMap<String, Object>, easy to use and flexible.
- Modules for special use case:
  - Database connection utils using Java Connection & DataSource.
  - Replace JPA deletion with custom DeleteGraph, works like binary tree. Helps decrease delete execution time in large records deletion.
  - Http with BaseController ready for extend. Support RESTful & Remote procedure call.
  - Excel helpers.
## Requirements
- JDK 21
- Gradle 8.x
## Build
```
cd <where build.gradle is placed>
gradle clean build publishToMavenLocal -x test
```
 
# 2. React
## Overview
- Data Table
  - Based on Tanstack's headless table  
  - with toolbar [ create, delete, search bar(call api) ], footer, rows and cols (resizeable). I designed for easy custimization.
- File Explorer.
- Input fields.
- Buttons with style.
- Utilities:
  - Date Time.
## Requirements
- NodeJS
- pnpm
## Build
```
cd ./ui/lib
pnpm run build
```
