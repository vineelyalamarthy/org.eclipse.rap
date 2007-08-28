This plug-in is provisional. Don't use this in any production environment.

As the Google API needs an API key, we prepared one for you in order to
use the GMaps widget in your local RAP installation. The API key does only
work for the standard RAP deployment on localhost:9090.

If you want to use it on another host/port, you need to generate your own API
key. See http://www.google.com/apis/maps/signup.html

To use the newly generated key the GMaps widget looks for it
in the "org.eclipse.rap.gmaps.key" system property.