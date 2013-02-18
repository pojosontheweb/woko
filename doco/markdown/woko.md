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

In short : all CRUD operations on your Domain Classes (plus full text search) are available for free. Woko generates the User Interface dynamically without you writing any single line of code for it.

### RPC and JavaScript ###
Woko includes out of the box support for RPC. All default features are available as JSON/HTTP services by default. Every feature covered by this tutorial so far can be realized using any code that speaks HTTP and JSON, using a simple protocol. 

With the app still running, log in as wdevel and try this :

[http://localhost:8080/myapp/save/MyEntity?object.id=123&object.myProp=foobar&createTransient=true&isRpc=true](http://localhost:8080/myapp/save/MyEntity?object.id=123&object.myProp=foobar&createTransient=true&isRpc=true)

The `isRpc` request parameter tells Woko that the request is to be handled as JSON/HTTP. Woko does the same job as usual, except that it returns the result of the operation as JSON. In this example, it returns the freshly created object :

```
{
    "id": 123,
    "myProp": "foobar",
    "class": "MyEntity",
    "_wokoInfo": {
        "title": "123",
        "className": "MyEntity",
        "key": "123"
    }
}
```

Again, all the features are available out of the box. For AJAX situations, it's even simpler using the `woko.rpc.Client` JavaScript API. It provides all default features, plus arbitrary facet invocation. 

Log in again as developer, open firebug and go to :

[http://localhost:8080/myapp/save/MyEntity?object.id=123&object.myProp=foobar&createTransient=true](http://localhost:8080/myapp/save/MyEntity?object.id=123&object.myProp=foobar&createTransient=true)

You have saved the object. Now go to firebug console and play :

```
// load MyEntity with ID 123 and store to global for the example
wokoClient.loadObject("MyEntity", 123, { 
    onSuccess: function(o) { 
        myEntity = o;
        console.log(myEntity);
    }
});
```

You can even update the object :

```
myEntity.myProp = "this is funky";
wokoClient.saveObject({
    obj:myEntity, 
    onSuccess:function(savedObject) {
        console.log(savedObject.myProp); 
    }
});
```

And check that it's been saved :

```
wokoClient.loadObject("MyEntity", 123, { 
    onSuccess: function(o) { 
        console.log(o.myProp);
    }
});
```
   

## Overriding the defaults ##
This section explains how the default features can be customized. It shows some typical examples of changing Woko's behavior when you need it. It also gives a glance of how you can write Resolution Facets in your app to perform various tasks.
### Overriding the Object Rendrerer ###
TODO explain how you can override the title, properties etc. Show an example in edit mode.
### Resolution Facets ###
TODO explain how to completely override /view, and how to write a sample Resolution Facet that does something
### Templating ###
TODO explain how to change the layout for a given role (and object ?)
# Iterative-friendly #
Any tool that makes your life easier is iterative-friendly. But Woko goes beyond gluecode, minimal config and and helper libraries only. The core design fits perfectly with Iterative Development, thanks to the high level of semantics of its facet-driven development model.

## Follow the use cases ##
Most of agile, iterative-driven development methodologies have a strong focus on Use Cases (or User Stories). They give a glance of "who does what" in the application. Of course, Use Cases and User Stories are non formal : we talk them out with end users (or customers) and try to get the best possible understanding, so that we can go for the implementation, and come back with something so show. Once we all agree the Use Case is realized (the app works as expected), we move to the next iteration (or Sprint, or whatever).  

Woko fully supports this model thanks to the semantics carried by the Facets and Domain Objects. Facets written in your Woko application have an _intent_ (usually conveyed by the facet's name), they apply to _roles_ and _target objects_. Instead of having your code scattered around the codebase with no meaning, Woko proposes a consistent approach where the semantics in your code allow to easily relate it to a Use Case. 

The typical Woko iteration goes like this :

<center>
<img src="iterative.png"/>
</center>

You basically start from the requirements (Use Cases or User Stories). Then, for each Use Case, you iterate on these 3 :

1. Write the POJOs 
2. Add some facets
3. Validate everything 
   * Yourself for "micro iterations"
   * With the product owner when the result is testable
  
Of course, the number of (micro) iterations depends on the complexity and requirements, but the principle always applies.

The many out-of-the-box, zero-LOC features that Woko provides also helps smaller iterations. Sensible defaults, convention over configuration, tooling etc. makes coding easier, thus shortening the cycles. The Object Renderer helps you quickly validate the main principles of a feature before you get your hands in the real dirt. 

Most of the useful time used developing a good application is often spent in "details that matter". User interface, polishing… there are the differenciators today. The common, recurrent principles should not require effort in order to work directly for you. Woko helps focusing on those details that matter, by providing a solid foundation you can build upon iteratively for everything else.   

> Semantics and expressivity in the code have many positive side effects. It is the basis of Woko's Object Renderer : using metadata in order to show Objects dynamically at runtime, without code generation of any sort. It also allows for better tooling, like the `woko` command-line script, the integrated Facet Studio, or the IntelliJ IDEA plugin : all rely on metadata in order to perform their tasks.  

## Quick validation ##
We all know that requirements are very hard to capture, and nothing speaks more to an end user than working software. Words are often subject to interpretation. Code rarely is. Showing working code is, to us, an essential part of requirements capture, as it allows to make sure we don't implement things based on a misunderstanding. 

With its built-in CRUD, Object Renderer and other "out of the box" features, Woko makes Domain Driven development a reality. You can actually show many things and validate concepts with your end users before you start customizing anything. This makes you save an enormous amount of time. You can try out various solutions to solve your problems, without loosing time on building small prototypes that you trash when they don't pass the user's validation.

## Prototyping and beyond ##
Many developers still consider "prototyping" as a one-shot-and-sent-to-trash activity. In that scenario, the prototype is only supposed to help understand the requirements, and is used only as a "pre-validation" step, before serious coding begins. Then it's trashed because it simply cannot be turned to working, shippable software.

We don't agree. Woko proves that prototyping is part of the development cycle and should not be considered as a parallel, one-shot activity. 

Woko is great in that prototyping phase, because it doesn't require you to write much code in order to have something to play with. But that's just the beginning of your journey !

Once the feature has been validated with the userand you know where you're going, then the prototype is a part of the application, and you build upon it in order to make it look and behave exactly as you want. You don't trash the Domain Objects, and you don't necessarily need to trash the Facets that you wrote. Of course you'll refine things, probably do some GUI etc. But you don't trash the base feature that you designed while prototyping : you build on it. 

In the next iteration, you'll probably extend the features already coded by reusing what's in place and adding to it, but following the same process : quickly model and test with your customers, and then take time for the (often very important) details.

The term "prototype", when developing a Woko application, only refers to a _phase_ of the project. It ain't no other technological or conceptual meaning.   

## Customization ##
Almost everything in Woko is designed to be pluggable. The main components like ObjectStore or UserManager can be completely or partly replaced. Need another ORM ? Wanna go NoSQL ? Upgrading to LDAP ? All this can be adapted. Those abstractions makes the base system very agile as the application doesn't require much changes even if you replace some underlying system by another. The contracts are very simple and easy to implement. 

Also, the customizable Object Renderer and CRUD features makes it easy to build upon solid foundations, and stop wasting time reinventing wheels. Customization hooks range from very small parts to the whole mechanism, so you can "cut the Woko branch" whenever you feel it's more pain that benefit. 
# Developer Guide #
## Main Components ##
Woko depends on several mandatory components in order to work. It uses abstractions (interfaces) for transversal services that can be implented differently depending on your context. Concrete implementations of these components are shipped with Woko (e.g. `HibernateStore`), and you can of course write new ones.

These components must be supplied at startup to the Woko instance, via the IoC.

### Object Store ###
The ObjectStore manages Object persistence for your Domain Objects (POJOs). It provides the base CRUD operations on managed POJOs :

* provide a list of the "managed" Classes
* load/save/update/delete a managed POJO
* list all instances of a given managed Class (paginated)
* full-text search for managed POJOs (optional)

The contract of Object Store is defined by the interface `woko.persistence.ObjectStore`.

#### Transactions & OSIV ####

Woko's persistence layer also includes abstraction for Transactions. If your store implements `woko.persistence.TransactionalStore`, then transactions will be automatically handled following the Open Session In View (OSIV) pattern (see `woko.actions.WokoTxInterceptor`).  

### User Manager ###
The UserManager handles users/roles for the application. It is a very simple view of the underlying user management system that only provides two methods :

* get the roles for a username : allows Woko to lookup facets for the user's roles
* authenticate a user

Woko ships with in-memory, container and hibernate enabled `UserManager`s. The contract is defined by interface `woko.users.UserManager`.

> Woko doesn't __manage__ your users : the UserManager is a read-only, and very small view of the underlying user management system. That's why it's very simple to implement and wrap any user management system. 

### UserNameResolutionStrategy ###
This class allows for pluggable authentication and user session management. It allows to change the way Woko retrieves the current username. 

For example, when using container authentication, it gets the username by calling `request.getRemoteUser()`, as per the servlet spec. Other implementations can use the http session, cookies, mocks for tests, or whatever else. 
### Facet Descriptor Manager ###
Facets in your application are scanned from the classpath by the Facet Descriptor Manager. It has to be configured with the base package(s) to scan (e.g. `com.myco.myapp.facets`).

You should not need to, but can also replace this component, in order to use a different form of facets (e.g. XML descriptor instead of Annotations). 

### Inversion of Control ###
Woko delegates management of the various sub components to an Inversion of Control "container". This allows to plug any component easier, to manage its dependencies and lifecycle if needed, and serves as a registry for any optional component, so that it can be accessed everywhere in the application. The woko instance itself is not in the container : it's an "application singleton" that is initialized at startup.

The IoC container is defined by interface `woko.ioc.WokoIocContainer`, and is pluggable. Woko ships with a default implementation, and a Pico adapter.
### Startup ###
Woko (and its sub componenents) is created and initialized at application startup, using a servlet context listener. A base abstract init listener class is provided (`woko.WokoIocInitListener`), that can be extended in order to configure your own Woko.

Here is an example using the `SimpleWokoIocContainer` (Groovy version) :


```
package com.myco.myapp

class MyWokoInitListener extends WokoIocInitListener {

    @Override
    protected WokoIocContainer createIocContainer() {
    	
    	// create and init our store
    	ObjectStore store = new MyStore()
    
    	// create and init our user manager
    	UserManager userManager = 
    		new MyUserManager().addUser("wdevel", "wdevel", ["developer"])
    
    	// create and init the U.N.R.S (container auth)
    	UsernameResolutionStrategy unrs = new RemoteUserStrategy()
    
    	// create the annotated facet descriptor using 
    	// superclass method : it inits the F.D.M. with 
    	// the facet packages found in web.xml
    	IFacetDescriptorManager fdm = createAnnotatedFdm()
    
    	// create and return the IoC container
        return new SimpleWokoIocContainer(
                store,
                userManager,
                unrs,
                fdm);
    }
    
    @Override
    protected List<String> createFallbackRoles() {
    	// return the default role(s) for our app
        return ["myguest"]
    }


}

```

The init listener has to be configured in web.xml :

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://java.sun.com/xml/ns/j2ee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/j2ee
         http://java.sun.com/xml/ns/j2ee/web-app_2_4.xsd"
         version="2.4">

	…

	<!-- Woko init listener : starts up Woko -->
    <listener>
        <display-name>WokoInitListener</display-name>
        <listener-class>com.myco.myapp.MyWokoInitListener</listener-class>
    </listener>

    … 

    <!-- facet packages -->
    <context-param>
        <param-name>Woko.Facet.Packages</param-name>
        <param-value>com.myco.myapp.facets</param-value>
    </context-param>
    
    …
    
    
    
</web-app>
```

#### Groovy Init ####
Groovy Init is an alternative, more flexible way to startup Woko. It also uses a Servlet Context Listener in order to create Woko when the application starts, but this one delegates all the initialization to a Groovy script.

This is particularly handly when used in combination with [environments](Environments), so that you can create various flavors of Woko with the full power of a programming language, and depending on the context (test, prod, etc.).

To enable it, first you need to add the dependency to your ```pom.xml``` :

```
<dependency>
  <groupId>com.pojosontheweb</groupId>
  <artifactId>woko-groovy-init</artifactId>
  <version>${woko.version}</version>
</dependency>  
```

Then declare the init listener in ```web.xml``` (instead of a `WokoIocInitListener` subclass) :

```
<listener>
  <display-name>WokoInitListener</display-name>
  <listener-class>woko.groovyinit.GroovyInitListener</listener-class>
</listener>
```

Last, you need a ```/woko-init.groovy``` available in your CLASSPATH, that creates, configures and returns a Woko instance. 

Here is an example :

```
// store
MyStore store = new MyStore()

// user manager
MyUserManager userManager = new MyUserManager(...).createDefaultUsers()

// facets
def facetPackages = ["com.myco.myapp.facets"] + Woko.DEFAULT_FACET_PACKAGES
AnnotatedFacetDescriptorManager fdm = new AnnotatedFacetDescriptorManager(facetPackages)
    .setDuplicatedKeyPolicy(DuplicatedKeyPolicyType.FirstScannedWins)
    .initialize()

// ioc
SimpleWokoIocContainer ioc = new SimpleWokoIocContainer(store, userManager, new RemoteUserStrategy(), fdm)

// create and return woko !
return new Woko(ioc, ["myguest"])
```

## Domain Objects ##
Domain Objects in Woko are POJOs. We don't use no meta-framework of any kind, only the Java type system. 

Woko manages your POJOs through the [Object Store](ObjectStore), which tandles the persistence of your objects seamlessly. The store is created at startup and connects to an underlying database in order to save the state and provide access to your Objects. This can be implemented in many different ways, using an ORM, custom DAOs, or whatever you can think of. 

> The ObjectStore only implements the basic CRUD operations by default, but it's a good entry point to place more specialized accessors to your domain objects (e.g. queries) when you'll need them. Like other Woko components, it is accessible everywhere in your application. 

Woko heavily uses introspection (`java.lang.reflect`) in order to determine the properties to display etc. In general, your Domain Objects should be regular POJOs that follow the JavaBean convention. Woko's ObjectRenderer will work directly with objects that 

* have a default constructor (if you want to be able to create instances via the default generated interface)
* expose their properties with accessors following the JavaBean convention
* use generics for Collections and Maps (e.g. `List<MyClass>`) so that Woko knows the compound types

Woko ships with a `HibernateStore` that uses automatic classpath scanning, JPA annotations for the mapping, and `javax.validation`. 

## Resolution Facets ##
`ResolutionFacet`s are to Woko what `ActionBean`s are to Stripes : they are the Controllers in the MVC. They basically respond to an URL, handle the http request, and return a Stripes `Resolution` that generates the response. 

Nevertheless, there are several important differences between the two :

* facets are identified by their key (name,profile,targetType) : they apply on a given object (or class), for a given role
* resolution facets have a consistent URL scheme (like `/view/MyClass/123`)
* resolution facets are loaded dynamically (ActionBeans are loaded at startup once and for all)

### URL Scheme ###
`WokoActionBean` dispatches incoming requests to Resolution Facets using the following URL binding :

```
@UrlBinding("/{facetName}/{className}/{key}")
```

When a matching request is handled, WokoActionBean first loads the target object using `className` and `key` (if provided). Then it retrieves the resolution facet with name `facetName` using the target object. If no `key` is provided, then Woko tries to find the facet by type. If no `className` is provided, then Woko tries to find the facet for the type `java.lang.Object`. 

The `@FacetKey` in Resolution Facets determines its URL. Here are a few examples :

```
/*
	/foo/MyClass/123
	/foo/MyClass
*/
@FacetKey(name="foo", profileId="myrole", targetObjectType=MyClass.class)
class MyResolutionFacet extends BaseResolutionFacet { 

	@Override
    Resolution getResolution(ActionBeanContext abc) {
    	return new StreamingResolution("text/plain", "bar !")
    }

}


/*
	/bar/MyClass/123
	/bar/OtherClass/456
	/bar
*/
@FacetKey(name="bar", profileId="myrole") // no targetObjectType defaults to java.lang.Object
class MyResolutionFacet extends BaseResolutionFacet { 

	@Override
    Resolution getResolution(ActionBeanContext abc) {
    	return new StreamingResolution("text/plain", "bar !")
    }

}
```

Of course Resolution Facets can return any type of Stripes `Resolution` (foward, redirect, stream, etc.).

### Accessing the Facet Context ###

The Facet Context can be accessed at runtime in order to retrieve various informations about the facet. It allows to get the target object of the facet : the one that was used to lookup for the facet. 

Here is an example of a Resolution Facet that retrieves the target object, calls `toString()` on it, and streams that back to the caller :

```
@FacetKey(name="toString", profileId="myrole", targetObjectType=MyClass.class) 
class ToStringResolutionFacet extends BaseResolutionFacet { 

	@Override
    Resolution getResolution(ActionBeanContext abc) {
    	
    	// retrieve the target object in facet context
    	MyClass my = (MyClass)getFacetContext().getTargetObject()
    	
    	// the object can be null (if the facet was requested with 
    	// classname only) so we check that...
    	String result = my != null ? my.toString() : "the object is null"
    	
    	// and we stram back result
    	return new StreamingResolution("text/plain", result)
    }

}

```  

### Data binding and Validation ###

Data binding and Validation works on Target Objects and Resolution Facets like on regular Stripes ActionBeans. The main difference is that prefixes must be used for request parameters, because Stripes binds on `wokoActionBean.getFacet()` and `getObject()` :

* `object.prop` binds on `targetObject.setProp(converted_value)`
* `facet.prop` binds on `facet.setProp(converted_value)`

Here is an example.

The POJO :

```
class MyClass {

	...
	String foo = "init value"

}
```

The Resolution Facet :

```
@FacetKey(name="doIt", profileId="myrole", targetObjectType=MyClass.class)
class DoIt extends BaseResolutionFacet {

	@Validate(required=true)
	String bar

	@Override
    Resolution getResolution(ActionBeanContext abc) {
    	MyClass c = (MyClass)getFacetContext().getTargetObject()
		return new StreamingResolution("text/plain", "$c.foo $bar !")
    }
    
}
```

The request :

```
GET /doIt/MyClass/123?object.foo=no&facet.bar=way
```

And the response :

```
no way !
```

For that request, `MyClass.foo` and `DoIt.bar` have been bound using the parameters `facet.foo` and `object.bar`.

#### Type Converters for your POJOs ####

Woko automatically registers Type Converters into Stripes for your managed POJOs. This means that you can bind objects from the ObjectStore using only their ID. 

The following example binds a List of MyClass objects :

```
// no target type needed for this demo, applies
// to all Objects or null
@FacetKey(name="bindMyPojo", profileId="myrole")
class BindMyPojo extends BaseResolutionFacet {

	List<MyClass> myClass

	@Override
    Resolution getResolution(ActionBeanContext abc) {
		return new StreamingResolution("text/plain", "count=${myClass.size()}")
    }
    
}
```

The request :

```
GET /bindMyPojo?facet.myClass[0]=123&facet.myClass[1]=456
```

And the response :

```
count=2
```

Woko's Type Converters use supplied ID and introspected property types in order to load your POJOs from the store during the binding/validation phase.  

#### Nested, Dynamic Validation ####

TODO explain dynamic validation metadata provider

### Event handlers ###

Like Stripes ActionBeans, Woko's ResolutionFacets can have several event handlers. Woko will invoke one of them based on the presence of a request parameter. 


## Fragment Facets ##
## Views and Tags ##
## Object Renderer ##
## Localization ##
## Build ##
### Dependencies and War Overlays ###
### Environments ###
## Tooling ##
## Unit Testing ##
Woko includes utility classes for out-of-container unit testing (see `woko.mock.MockUtil`). It is based on Stripes' [MockRoundtrip](http://www.stripesframework.org/display/stripes/Unit+Testing) and allows to emulate a running WokoActionBean and unit-test your facets.

The beauty of Mockroundtrip is that you can invoke ActionBeans via URLs in your unit tests, with all Stripes features enabled (binding/validation, type conversion etc). 

Here's an example (Groovy version) :

```
import woko.mock.MockUtil
...

class MyTest {

	// create, init and return a new Woko : you can configure 
	// your testing Woko as you want. Either the same as your webapp
	// or using different components/configurations
	Woko createWoko(String username) {
		return new Woko(...) 
	}
	
	@Test
	void testIt() {
		// need a Woko to run our tests... 
		Woko woko = createWoko('myuser')
		
		// use Callback to wrap execution and use an automatically
		// initialized/destroyed MockServletContext 
        new MockUtil().withServletContext(createWoko(), { MockServletContext ctx ->
        
        	// trip and retrieve a facet via its URL        
			def myFacet = MockUtil.tripAndGetFacet(ctx, "/doIt/MyClass/123")
			// make sure the facet class is the expected one
			assert myFacet.getClass() == MyFacet.class
			// make sure myFacet.myProp is null
			assert myFacet.myProp == null
			
			// now trip with parameters
			myFacet = MockUtil.tripAndGetFacet(ctx, "/doIt/MyClass/123", ['facet.myProp':'foobar'])
			// make sure the facet class is still the expected one
			assert myFacet.getClass() == MyFacet.class
			// make sure myFacet.myProp has been bound
			assert myFacet.myProp == 'foobar'
			
        } as MockUtil.Callback)
	}

}

```

As you can see, `MockUtil` provides a simple way to create the MockServletContext (and close it automatically), as well as several static methods that eases test writing. The only thing you need to do is to create a `Woko` instance and feed it to MockUtil.

Here's an example of how you can do this easily (Groovy again), with your own Object Store etc. :

```
Woko createWoko(String username) {

	// create store using my own ObjectStore implementation
	// and pre-populated with some testing objects
	ObjectStore myStore = createObjectStoreWithTestObjects()
	
	// same for user manager : init with test users/roles  
    UserManager userManager = createUserManagerWithTestUsers()
    
    // use mock UNRS so that we can emulate logged in users
	UsernameResolutionStrategy unrs = new MockUsernameResolutionStrategy(username)
	
	// create FDM : we provide our facet packages just like in web.xml
	List facetPackages = ['com.myco.myapp.facets'] + Woko.DEFAULT_FACET_PACKAGES
	IFacetDescriptorManager fdm = Woko.createFacetDescriptorManager(facetPackages)
	
	// create IoC
    SimpleWokoIocContainer ioc = new SimpleWokoIocContainer(
            store,
            userManager,
            unrs)
            
    // create and return Woko with fallback guest roles
    return new Woko(ioc, ['myguest'])
}
```

> If your ObjectStore is a `TransactionalStore`, the mock testing will use `WokoTxInterceptor`, and demarcate the transactions for each call to `tripXyz()`, just like it'd be done in a regular servlet request handling. On the other hand, be careful to properly handle transactions for all code that uses your ObjectStore in your test besides calls to `tripXyz()` : the tx interceptor is fired only when MockRoundtrip executes, so other calls in your tests should handle the transactions themselves. 
 
# Add-ons #
## User Management ##

# Performance #
Fine tuning
