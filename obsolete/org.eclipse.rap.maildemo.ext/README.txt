Please make sure that you are using the RAP 1.1 M3 build 
or the latest CVS Head code from RAP, otherwise you will
get many compile errors as this project is using the 
latest RAP functionality!

Please follow the instructions to run this example:
Create the RAP Mail template project (New Project - 
Plugin Project - RAP Mail template from the templates)
and call it org.eclipse.rap.maildemo.

Export the org.eclipse.rap.maildemo package using the
MANIFEST.MF editor.

There should be one remaining compile error in the class
ApplicationActionBarAdvisor of the .ext project that can be
resolved with a quick fix: Make the constructor of 
OpenViewAction visible.

Now you can launch the project: Create a new 
RAP Application launch configuration and select
"mailext" as Servlet name and Entrypoint. The 
org.eclipse.rap.maildemo and org.eclipse.rap.maildemo.ext
bundles need to be active. 