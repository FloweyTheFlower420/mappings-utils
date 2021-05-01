# mappings-utils
Utilites for my modded-mc related projects (java)
Features:
- mappings processing
- shorthands for less verbose code

Install:
Gradle:
```
repositories {
    ... whatever you had here before
    maven { url = "http://maven.floweytf.com/releases/" }
}
```
Add the dep
```
dependencies {
    implementation 'com.floweytf.utils:utils:${version}'
}
```
Maven: 
```xml
<repository>
      <id>flowey</id>
      <name>whatever</name>
      <url>http://maven.floweytf.com/releases/</url>
</repository>
```
Add the dep
```xml
<dependency>
      <groupId>com.floweytf.utils</groupId>
      <artifactId>utils</artifactId>
      <version>${version}</version>
</dependency>
```
