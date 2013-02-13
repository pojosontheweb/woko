# Woko : POJOs on the Web! #
Woko is a Full-Stack, Domain Driven framework for building webapps efficiently in Java. It provides transversal plumbing and solutions to the recurrent webapp programming issues, as well as a unique approach for developing iteratively and in a consistent manner.
## Some History ##
Y2K, corbaweb etc.
# Big Picture #
Woko is basically about displaying Domain Objects to end users, and allow them to interact. That's what every application is about. We use GUI paradigms and widgets in order to represent some state to the end user, allow her(him) to change that state and trigger behavior based upon user interaction. Strangely, few frameworks or tools are making it obvious, because business logic is often buried under loads of glue code. Woko is designed around this basic principle.
## Domain Driven, Object-Oriented User Interface ##

TODO explainhow Woko inspires from Naked Objects for OOUI

## The Object Oriented Wiki ##
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
There's only one `Woko`! At least in your webapp… 

When the application starts up, a `Woko` instance is created, initialized, and bound to the `ServletContext`. Then, from anywhere in the app, the `Woko` instance can be retrieved and used as an top-level entry point for executing various tasks.

There are various ways to configure and boot Woko. TODO link to startup section in dev guide.   
### Mandatory Components ###
Woko delegates most of the job to sub-components :

* `ObjectStore` : Manages Object-Oriented persistence for your POJOs. Implements basic CRUD operations used by the default Woko features.
* `UserManager` : Handles users/roles and authentication. Simple contract that allows the framework to obtain the roles of the currently logged in user.   


## Typical Request Handling flow ##

# Tutorial #
Step-by step tutorial 
# Developer Guide #
Dev guide
# Performance #
Fine tuning
# Security #
Strict binding etc.
