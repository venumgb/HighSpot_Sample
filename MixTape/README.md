**HighSpot App**

Created a java application to tackle the playlist problem. 
This application reads two input files (mixtape.json, changes.json) and prints them.
It applies the changes in the changes.json file to the mixtape.json and outputs the changed file
into output.json.

Three functions created :

ReadInputFile()
UpdateMixTape()
WriteToOutputFile()

There are some private helper functions to avoid duplicate code.


**What you’ll need**

A favorite text editor or IDE

JDK 11 or later

Install Gradle

**Setup**

Running this project from Command line with JDK 11 or higher installed.

java -Dfile.encoding=UTF-8 -jar ./MixTape/out/artifacts/MixTape_jar/MixTape.jar mixtape.json changes.json

**Dependencies**

This project has a dependency on the JSON.simple Java library for JSON processing, read and write JSON data and full compliance with JSON specification (RFC4627)


**Scalability**

To deal with large input files, first thing that comes to mind is do not read the whole file into memory . using GSON library you can adopt the Streaming technique to parse a large file in chunks.
You can also use Jackson library in java  that provides facility to improve application performance by using it’s streaming API which uses very less memory and low CPU overhead.
If it is a really huge file then dump the file in a NoSql Database and read the contents as needed and manipulate the data

Another way to deal with large file is to process is split the file into chunks and process them parallel. Splitting can be 
based on size, lines markers etc..

This would require a distributed processing model and can be used for heavy lifting .




