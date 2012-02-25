package woko.tooling.cli

class LongHelpMessages {

    static MSGS = [

      "run": """Packages and runs the application in a local
web container. """,

      "stop": """Stops the local container if running.""",

      "list": """Lists components of your application by using runtime information.
The command accepts one argument that can be  :
  * facets   : inits the facets of your app and lists them
  * roles    : lists all the roles defined in your application facets""",

       "create": """Create new elements (facets, entities, etc.) in your Woko project.
Basic usage involves one argument, depending on the type of element
you want to create :
  * facet    : allows for easy creation of facets and associated views if any.
  * entity   : create persistent POJOs and associated default facets."""
     ]


}
