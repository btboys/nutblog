/*!
* Hashable jQuery plugin
* http://www.thepiepers.net/
*
* This jQuery plugin enables a website owner to easily bind to the hash value changes.
* If the Modernizr v2.0+ library is present and the browser supports the hashchange event,
* it will fire based on change, rather than timer.
*
* Usage:
* <script type="text/javascript">
*  // the callback will receive the new and old hash values as arguments
*  $(window).hashChange(callbackFunction);
*  
*  // or you can bind to the "hashChange" event
*  $(window).bind("hashChange", function(e, newHash, oldHash) {
*      console.log("new: " +  newHash);
*      console.log("old: " + oldHash);
*  });
* </script>
*
* Copyright (c) 2011, Bryan Pieper
* Released under the MIT license.
* http://www.opensource.org/licenses/mit-license.php
*/
(function(b){b.fn.hashChange=function(f,g){function h(a){a?a.substring(0,1)=="#"&&(a=a.substring(1)):a="";return a}function d(){var a=h(window.location.hash);if(a!=c){var b=c;c=a;i.trigger("hashChange",[a,b]);typeof f!=="undefined"&&f(a,b)}}var e={initialHash:"",delay:100},c="",i=b(this);typeof g!=="undefined"&&b.extend(e,g);c=h(e.initialHash);typeof Modernizr!=="undefined"&&Modernizr.hashchange?(i.bind("hashchange",d),d()):setInterval(d,e.delay)}})(jQuery);