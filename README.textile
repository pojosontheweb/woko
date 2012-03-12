== Welcome to Woko2

Woko is a **Full-Stack Web Framework** for the Java Platform that aims to put your POJOs
on the Web for nothing (or almost !). The main idea behind Woko is "do more with less" :
it provides some plumbing for the most common features needed in webapp development, including :

* Persistence and Full Text Search
* Users and Roles (Security and User-profiling)
* MVC (URLs, Forms, Data binding & Validation, Templating, ...)
* RPC-friendly APIs (JSON, JavaScript)

**Woko is Domain Driven** : it uses metadata whenever possible in order to provide as much as it can out of the box. You write the Domain Classes, and Woko manages most of the recurrent issues for you, from the persistence layer up to the browser.

**Woko is Iterative** : it provides various extension points that allow you to change the default behavior without generating code, through its powerful facets system.

== Getting started
1. Download and install Woko tools

2. Create a new Woko project
    woko init

3. Change directory to +myapp+, where "myapp" corresponds to the artifactId
    cd myapp

3. Start your woko application in Jetty embed container
    woko start

4. Go to http://localhost:8080/myapp

5. Get more documentation on our Wiki to start developing your application

* The entire {Woko tutorial}[https://github.com/vankeisb/woko2/wiki/Tutorial]
* The all {documentation index}[https://github.com/vankeisb/woko2/wiki]
* The {Woko screencasts}[https://github.com/vankeisb/woko2/wiki/Screencasts]

== Licence

