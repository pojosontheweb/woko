<center>
<img src="woko3d.png"/>
</center>


# POJOs on the Web! #
Woko is a Full-Stack, Domain Driven framework for building webapps efficiently in Java. It provides transversal plumbing and solutions to the recurrent webapp programming issues, as well as a unique approach for developing iteratively and in a consistent manner.

Woko is about displaying Domain Objects to end users, and allow them to interact. That's what every application is about. We use GUI paradigms and widgets in order to represent some state to the end user, allow her(him) to change that state and trigger behavior based upon user interaction. 

Strangely, few frameworks or tools are making it obvious, because business logic is often buried under loads of glue code. You often spend more time on recurrent, non-productive issues instead of solving the problem. And you often end up with non-expressive code, so far from the requirement that all intent is lost. 

Woko is designed around a few basic principles that allow to code better webapps faster :

* Domain and Role Driven : Woko works with Objects, and aims at allowing your users to work with them too.
* Use metadata : Woko tries to grab the most it can from your code, so that it can provide many features out of the box. It does everything dynamically, without code generation.
* Sensible defaults : No config, excepted when you really need it. 
* KISS : Woko is simple, that's why it works.

# The Object Oriented Wiki #
Woko stands for "The Object Oriented Wiki". The idea is that, like in a regular wiki, end users can view/edit/delete (the usual CRUD suspects) pages, and navigate from a page to another using hyperlinks. Wikis usually provides ACLs or other persmissions so that admins can decide "who can do what" with the pages. 

Woko applies this principle to Object Oriented Programming : instead of manipulating "Pages", end users deal directly with Domain Objects in their browser. Woko handles all CRUD operations out of the box on your Domain Objects, and lets you fine-tune everything as you want so that your users can interact with your Objects. 

<center>
<img src="bigpicture.png"/>
</center>

More than only plumbing and glue code, Woko offers a real framework and high-level semantics for developing Domain-Driven, user-aware web applications. It provides a foundation that you build upon in order to avoid re-inventing another wheel.
## Facets ##
Woko uses Facets in order to perform profile-based operations. They sit in between the user and the objects of the application. Woko includes many "built-in" facets for performing the generic operations (CRUD etc.), that you can override by yours in order to change the behavior. Most of the customization in Woko is done via facet override. The framework does the plumbing tasks, and delegates to facets for actual business logic. 

The facets are assigned to the Domain Objects and Roles of the application. They carry lots of semantics compared to regular web app components (like a servlet) : they tell you what they do for a given type of target Object and a role. 
 
Different categories exist in Woko's built-in facets, and handle different tasks. Some act as Controllers, responding to http requests, others are used to generate HTML fragments. Together they provide a simple yet powerful basis to start from, and customize for your own use cases.

## Users and Roles ##
Woko handles users and roles as a first class concept. It defines abstractions for users/roles management, and heavily relies on it in order to retrieve the various facets. The abstraction is very simple and can adapt to virtually any user management system.
JEE container authentication, as well as "built-in" (application-stored users) authentication are available by default. 

User Management built-in facets allows developers and other granted users to benefit of user admin facilities. Plugins can be added in order to achieve various usual user management operations like registration, password change or reset, etc.  
## Persistence ##
Woko manages your POJOs, and uses a pluggable Object Store to persist the state into a database, allowing storage-independant web tier. It has support for Hibernate by default, but it can be completely replaced if needed. The contract only defines required CRUD operations and is pretty simple.
## Reuse ##
Woko handles most of the recurrent issues related to web development. It provides generic, reusable features and glue code between all subsystems, from the persistence layer up to the web tier. It lets you focus on the essential : the data and business logic you need in order to allow the user interactions you want. The aim is to reuse best practises and libraries in order to increase productivity. 
# Architecture #
Woko itself doesn't do much. It's nothing but a good mix of various technologies combined alltogether. The core runtime has very few dependencies, basically Stripes and JFacets only. Then, pluggable components provide the necessary services, like persistence or user management.

The overall architecture looks like this :

<center>
<img src="architecture.png"/>
</center>

## MVC Layer ##
The MVC layer is built on top of the fantastic Stripes framework. It's one of the few libs that Woko exposes directly, and that you will have to use when developing a Woko application.
	
Woko uses a main `ActionBean` for serving the requests (excepted for static resources of course), along with several Stripes extensions that make binding, validation etc. work directly your Domain Objects and users.  

### WokoActionBean ###
It's the main Controller for the Woko app. Kind-of a "super dispatcher" that handles all requests and delegates to the appropriate facet. Unlike a typical Stripes app, a Woko application doesn't use many `ActionBean`s. They are replaced by `ResolutionFacet`s : facets that handle the http request, and return a Stripes `Resolution`.   

`WokoActionBean` also defines Woko's URL scheme. It responds to all requests that match its URL binding : 

    {facetName}[/{className}[/{key}]]

The following URLs are typical Woko URLs that WokoActionBean will handle by delegating to the appropriate components :

* `/view/Product/123`
* `/list/Product`
* `/home`
* `/doSomeFunkyStuff/MyClass/123?facet.myProp=123&object.myOtherProp=cool`

The URL scheme is an important part of Woko. The URLs reflect what they mean, they show the intent and target object. The different parts of the URL (`facetName`, `className` and `key`) are used by WokoActionBean in order to resolve the target object and Resolution Facet to be applied.

All parameters are prefixed with either `facet.` or `object.` : they will be bound respectively to the Resolution Facet and target Object, provided they satisfy the validation constraints if any. Like their cousins Action Beans, Resolution Facets has at least one event handler method, that returns a `Resolution`.
### Stripes extensions ###
Woko adds several extensions to Stripes in order to make Resolution Facets work like Action Beans, with respect to Binding & Validation, Security, etc. They are implemented as Stripes `Interceptor`s and other components. Woko also registers custom `TypeConverter`s for transparent binding of Domain Objects.
### The Woko instance ###
There's only one Woko ! At least in your webapp… 

When the application starts up, a `Woko` instance is created, initialized, and bound to the `ServletContext`. Then, from anywhere in the app, the `Woko` instance can be retrieved and used as an top-level entry point for executing various tasks.

There are various ways to configure and boot Woko. TODO link to startup section in dev guide.   
### Mandatory Components ###
Woko delegates most of the job to sub-components :

* `ObjectStore` : Manages Object-Oriented persistence for your POJOs. Implements basic CRUD operations used by the default Woko features. Woko ships with a fully functional Hibernate implementation that uses JPA annotations for the mapping. 
* `UserManager` : Handles users/roles and authentication. Simple contract that allows the framework to obtain the roles of the currently logged in user. Woko handles container (JEE) authentication and roles, as well as a built-in implementation for storing users in the database.
* `Facets` : a configured JFacets instance with built-in and application facets. By default Woko uses Annotated Facets (`@FacetKey`) and classpath scanning in order to avoid configuration.

Those components are made available to Woko following the Inversion Of Control (IoC) principle. A container holds all the components (mandatory and user-defined if needed) and can manage their dependencies. Woko retrieves the required components from the IoC container when needed.  

Of course, all those components are configurable, and completely pluggable. They are defined as interfaces and you can replace their implementation as you see fit.
## Typical Request Handling flow ##
Here is a dynamic view of a typical Woko request handling  :

<center>
<img src="requestflow.png"/>
</center>

It's a typical Stripes flow, spiced up with target object and facet loading :

1. __Before__ - Stripes has created a WokoActionBean instance for the request, and invokes the _before_ interceptor. 
    * Request parameters `className` and `key` are used for retrieving the target Object using the `ObjectStore`. When using Hibernate, this ends up calling `session.load()` for the class and primary key. Here, we load the `Product` object with ID `123`.
    * Once the target `Product` object is loaded, it is used in order to retrieve the `ResolutionFacet` for requested name (`facetName` request parameter). If no facet is found, a 404 is raised. 
2. __Binding__ - Stripes binds the request parameters, with type converters and dynamic validation on the facet and target object :
	* `object.price=10` sets the `price` property of the `Product` target object
	* `facet.coupon=XYZ` sets the `coupon` property of the `ResolutionFacet` object  
3. __Event execution__ - Stripes invokes the event handler on `WokoActionBean`. This one delegates to the `ResolutionFacet`'s event handler, returning the `Resolution` to be used.
4. __Resolution execution__ - Stripes executes the returned `Resolution`, producing the HTTP response.

# Tutorial #

This tutorial aims at covering the main aspects of Woko through practical examples.

> We'll be using the default "Reference Implementation" (hibernate etc.), but the same concepts applies to other implementations of `ObjectStore`, `UserManager` etc. 

## Environment setup

You'll need to install the `woko` shell script to go through this tutorial. TODO link to section 

Make sure the `woko` command is available in your PATH before you start.

## Let's go !

First off, open a command prompt, switch to a folder of your choice and initiate a new project :

```
$ woko init
```

The command will ask you for some basic info about your project. You can pick default values for everything excepted the `groupId` and `artifactId`  :

```
$ woko init
__       __     _  __
\ \  _  / /___ | |/ / ___
 \ \/ \/ // o \|   K /   \
  \__W__/ \___/|_|\_\\_o_/  2.0
             POJOs on the Web !

Initializing project
> Project name ? myapp
> Maven groupId ? com.myco.myapp
> Your project's version ? [1.0-SNAPSHOT] 
| Generating your project, please wait, it can take a while to download everything...
> Would you like to use Bootstrap for UI ? [y] :
> Would you like to use Groovy ? [y] :
> Specify your default package name [com.myco.myapp] :
> Would you like enable the woko 'push' command ? [y] :
|  - web.xml file created : src/main/webapp/WEB-INF/web.xml
|  - Layout facet created : com.myco.myapp.facets.MyLayout
|  - resource bundle created : myapp/src/main/resources/application.properties
|  
|  Your project has been generated in : myapp.
|  Run 'woko start' in order to launch your app in a local Jetty container  

```

This creates a `myapp` project in the current directory. The project contains a sample Domain Class, and is ready for use.

## Domain Classes

The very first step when starting a Woko app is to define the Domain Classes : the entities that represent your model. 

Here's the example that has been generated in our project :

```
// src/main/groovy/myapp/com/myco/myapp/model/MyEntity.groovy

package com.myco.myapp.model

import org.compass.annotations.Searchable
import org.compass.annotations.SearchableId
import org.compass.annotations.SearchableProperty
import javax.validation.constraints.NotNull

import javax.persistence.Entity
import javax.persistence.Id

@Entity
@Searchable
class MyEntity {

    @Id
    @SearchableId
    Long id

    @NotNull
    @SearchableProperty
    String myProp

    Date myOtherProp

}
```

And that's it. A `MyEntity` POJO with persistence, validation and full-text search enabled. Your class will be scanned at startup, and ready for use. 

> The built-in `HibernateCompassStore` supports Hibernate, Hibernate Validator and Compass mapping annotations. Refer to their docs for more infos about them.

## Full Defaults !

You don't have to write anything more than a Domain Class to start playing with your application. Build the app, and start the server :

```
$ woko start
```

This will compile and build the project, start jetty, and deploy your application. You can now point your browser to :

[http://localhost:8080/myapp](http://localhost:8080/myapp)

### Guest Home

What you'll see when visiting the app is the guest home page. That's what unauthenticated users see of your application by default. 

![Guest Home](img/woko1.PNG)

We have chosen not to show Domain Objects to guest users by default, so there's nothing more to see than this home page at the moment. Of course, you can easily change the contents of the default guest home page. 

### The developer role

By default, your Woko application includes a specific user, of role `developer`. This user has all CRUD privileges on your Domain Objects, plus a few "power features" that we'll explain later.

You can use the default credentials in order to log-in :

* username : wdevel
* password : wdevel

> Of course, you'll change these later in order to avoid a big security hole in your app ! Check the section TODO link for more info.

As you can see, developers also have their home page, but this time with a few items in the nav bar :

![Guest Home](img/woko2.PNG)

For now, let's try the CRUD features on our `MyEntity` Domain Class. 

### Zero-LOC CRUD 

Let's first create an instance of our Domain Class. Click the _create_ link in the nav bar :

![Guest Home](img/woko3.PNG)

As you can see, Woko has found your Domain Class, you can select `MyEntity` from the list, and submit :

![Guest Home](img/woko4.PNG)

A FORM is generated for your POJO, with input fields for first-level properties :

![Guest Home](img/woko5.PNG)

A few things to notice here :

* Constraint Validations are taken into account (the `@NotNull` on `myProp`)
* Input fields are generated based on the type of the object's properties (there's even a date picker component for `myOtherProp`

![Guest Home](img/woko6.PNG)

Woko has dynamically introspected your Domain Class, and rendered a FORM that allows to change its state. 

Now fill in some values and save :

![Guest Home](img/woko7.PNG)

The object has been saved. We can now close the edit mode :

![Guest Home](img/woko7-1.PNG)

And now get a "read-only" view of our `MyEntity` instance :

![Guest Home](img/woko8.PNG)

As you can see, Woko has now generated plain HTML for the object's properties. Again, it's using the types and metadatas found on the object (e.g. the formatted date). 

Developer users can also list objects by class :

![Guest Home](img/woko9.PNG)

![Guest Home](img/woko10.PNG)

![Guest Home](img/woko11.PNG)

And use full text search, as defined in the POJO's annotations :

![Guest Home](img/woko12.PNG)

In short : all CRUD operations on your Domain Classes (plus full text search) are available for free. Woko generates the User Interface dynamically without you writing any single line of code for it... unless you start customizing stuff !

# Developer Guide #
Dev guide
# Performance #
Fine tuning
# Security #
Strict binding etc.