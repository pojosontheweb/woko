# Introduction #

Woko is a Full-Stack, [Domain Driven](http://en.wikipedia.org/wiki/Domain-driven_design) framework for building JEE webapps efficiently. It provides solutions to the recurrent webapp programming issues, as well as a unique approach for developing iteratively and in a consistent manner.

Woko is about displaying Domain Objects to end users, and allow them to interact. That's what every application is about. We use GUI paradigms and widgets in order to represent some state to the end user, allow her(him) to change that state and trigger behavior based upon user interaction. 

Strangely, few frameworks or tools are making it that obvious, because business logic is often buried under loads of glue code. You often spend more time trying to solve recurrent, non-productive issues instead of the actual problem. And you often end up with non-expressive code, so far from the requirement that all intent is lost. 

Woko is designed around a few basic principles that allow to code better webapps, and to do it faster :

* _Domain_ and _Role Driven_ : Woko works with Objects, and aims at allowing your users to work with them too.
* Use _metadata_ : Woko tries to grab the most it can from your code, so that it can provide many features out of the box.
* Sensible _defaults_ : Works out of the box, without config or extension, but allows to customize everything when you need it. 
* _KISS_ : Woko is simple, that's why it works.

# The Object Oriented Wiki #

Woko stands for "The Object Oriented Wiki". The idea is that, like in a regular wiki, end users can view/edit/delete (the usual CRUD suspects) pages, and navigate from a page to another using hyperlinks. Wikis usually provides ACLs or other permissions so that admins can decide "who can do what" with the pages. 

Woko applies this principle to Object Oriented Programming : instead of manipulating "Pages", end users deal directly with Domain Objects in their browser. Woko handles all CRUD operations out of the box on your Domain Objects, and lets you fine-tune everything as you want so that your users can interact with your model. 

<center>
<img src="bigpicture.png"/>
</center>

More than only plumbing and glue code, Woko offers a real framework and high-level semantics for developing Domain-Driven, user-aware web applications. It provides a foundation that you build upon in order to avoid re-inventing yet another wheel.

## Facets ##

Woko uses Facets in order to perform profile-based operations. They sit in between the user and the objects. Woko includes many "built-in" facets for performing the generic operations (CRUD etc.), that you can override by yours in order to change the behavior. The framework does the plumbing tasks, and delegates to facets for actual business logic. 

The facets are assigned to the Domain Objects and Roles of the application. They carry lots of semantics compared to regular web app components (like a servlet) : they tell you what they do for a given target Object and a role. 
 
Different categories exist in Woko's built-in facets, and handle different tasks. Some act as Controllers, responding to http requests, others are used to generate HTML fragments. Together they provide a simple yet powerful basis to start from, and customize for your own use cases.

## Users and Roles ##

Woko handles users and roles as a first class concept. It defines abstractions for users/roles management, and heavily relies on those abstractions in order to retrieve the various facets. The contract is very simple and can be implemented on top of virtually any user management system.
JEE container authentication, as well as "built-in" (application-stored users) authentication are available by default. 

User Management built-in facets allows developers and other granted users to benefit of user admin facilities. Plugins can be added in order to achieve various usual user management operations like registration, password change or reset, etc.  

## Persistence ##

Woko manages your POJOs, and uses a pluggable Object Store to persist the state into a database, allowing storage-independent web tier. It has support for Hibernate by default, but it can be completely replaced if needed. The contract only defines required CRUD operations and is pretty simple.

## Reuse ##

Woko handles most of the recurrent issues related to web development. It provides generic, reusable features and glue code between all subsystems, from the persistence layer up to the web tier. It also provides solutions to common use cases like user management (registration, password change/reset etc.) or asynchronous jobs.

It lets you focus on the essential : the data and business logic you need in order to allow the user interactions you want. The aim is to reuse best practices and libraries in order to increase productivity. 

# Iterative-friendly #

Any tool that makes your life easier can be considered iterative-friendly. Time you save it more time for developing new features, refactoring etc. 

But Woko goes beyond glue-code, minimal config and and helper libraries only. The programming model fits perfectly with Iterative Development, thanks to the high level of semantics of the facet-driven approach.

## Follow the use cases ##

Most of agile, iterative-driven development methodologies have a strong focus on Use Cases (or User Stories). They give a glance of "who does what" in the application. Of course, Use Cases and User Stories are non formal : we talk them out with end users (or customers) and try to get the best possible understanding, so that we can go for the implementation, and come back with something to show. Once we all agree the Use Case is realized (the app works as expected), we move forward to the next iteration (or Sprint, or whatever).  

Woko fully supports this model thanks to the semantics carried by the Facets and Domain Objects. Facets written in your Woko application have an _intent_ (usually conveyed by the facet's name), they apply to _roles_ and _target objects_. Instead of having your code scattered around the codebase with no meaning, Woko proposes a consistent approach where the semantics in your code allow to easily relate it to a Use Case. 

The typical Woko iteration goes like this :

<center>
<img src="iterative.png"/>
</center>

You basically start from the requirements (Use Cases or User Stories). Then, for each Use Case, you iterate on these 3 steps :

1. Write the POJOs 
2. Add some facets
3. Validate everything (either yourself for "micro iterations", or with the product owner when the result is testable)
  
Of course, the number of (micro) iterations depends on the complexity and requirements, but the principle always applies.

The many out-of-the-box, zero-LOC features that Woko provides also helps smaller iterations. Sensible defaults, convention over configuration, tooling etc. makes coding easier, thus shortening the cycles. The [Object Renderer](Object Renderer) helps you quickly validate the main principles of a feature before you get your hands in the real dirt. 

Most of the useful time used developing a good application is often spent in "details that matter". User interface, polishing… these are the differentiators today. The common, recurrent stuff should not require effort in order to work directly for you. Woko helps focusing on those details that matter, by providing a solid foundation you can build upon, iteratively.   

> Semantics and expressivity in the code have many positive side effects. It is the basis of Woko's [Object Renderer](Object Renderer) : using metadata in order to show Objects dynamically at runtime, without code generation of any sort. It also allows for better tooling, like the `woko` command-line script, the integrated Facet Studio, or the IntelliJ IDEA plugin : all rely on metadata in order to perform their tasks.  

## Quick validation ##

Requirements are a hard thing to capture. Nothing speaks more to an end user than working software, because words are often subject to interpretation. Code rarely is. Showing working code is, according to us, an essential part of requirements capture, as it allows to make sure we don't implement things based on a misunderstanding. 

With its built-in CRUD, Object Renderer and other "out of the box" features, Woko makes Domain Driven development a pragmatic reality. You can actually show many things and validate concepts with your end users before you start customizing anything. This makes you save an enormous amount of time. You can try out various solutions to solve your problems, without loosing time on building small prototypes that you trash away when they don't pass the user's validation.

## Prototyping and beyond ##

Many developers still consider "prototyping" as a one-shot-and-send-to-trash activity. In that scenario, the prototype is only supposed to help understand the requirements, and is used only as a "pre-validation" step, before serious coding begins. It usually comes under the form of mockups with text descriptions. Then the "prototype" is simply trashed because it simply cannot be turned to working, shippable software. Or it eventually gets out of sync with the actual code, unless you spend time updating the proto when you update the code...

We don't agree with this process. Woko proves that prototyping is part of the development cycle and should not be considered as a parallel, one-shot activity. 

Woko really shines in that early prototyping phase, because it doesn't require you to write much code in order to have something to play with. But that's just the beginning of your journey !

Once the feature has been roughly validated with the user and you know where you're going, then the prototype is a part of the application, and you build upon it in order to make it look and behave exactly as you want. You don't trash the Domain Objects, and you don't necessarily need to trash the Facets that you wrote. Of course you'll refine things, probably do some GUI etc. But you don't trash the base feature that you designed while prototyping : you build on it. It _is_ part of your application since the beginning.

In the next iteration, you'll probably extend the features already coded by reusing what's in place and adding to it, but following the same process : quickly model and test with your customers, and then take time for the (often very important) details.

The term "prototype", when developing a Woko application, only refers to a _phase_ of the project. It ain't no other technological or conceptual meaning, and requires no additional tools or methodology. It's all just coding.

## Customization

Almost everything in Woko is designed to be pluggable. The main components like [Object Store](Object Store) or [User Manager](User Manager) can be completely or partly replaced. Need another ORM ? Wanna go NoSQL ? Upgrading to LDAP ? All this can be adapted. Those abstractions makes the base system very agile as the application doesn't require much changes even if you replace some underlying system by another. The contracts are very simple and easy to implement. 

Also, the [Object Renderer](Object Renderer) and CRUD features are very flexible and can be customized easily. Hooks range from very small parts to the whole mechanism, so you can "cut the Woko branch" whenever you feel it's more pain than benefit. 
