/* ************************************************************************

   qooxdoo - the new era of web development

   http://qooxdoo.org

   Copyright:
     2004-2008 1&1 Internet AG, Germany, http://www.1und1.de

   License:
     LGPL: http://www.gnu.org/licenses/lgpl.html
     EPL: http://www.eclipse.org/org/documents/epl-v10.php
     See the LICENSE file in the project's top-level directory for details.

   Authors:
     * Sebastian Werner (wpbasti)
     * Andreas Ecker (ecker)

************************************************************************ */

/* ************************************************************************

#module(ui_core)

************************************************************************ */

qx.Class.define("qx.html.Scroll",
{
  /*
  *****************************************************************************
     STATICS
  *****************************************************************************
  */

  statics :
  {
    /**
     * TODOC
     *
     * @type static
     * @param el {Element} TODOC
     * @return {var} TODOC
     */
    getLeftSum : function(el)
    {
      var sum = 0;
      var p = el.parentNode;

      while (p.nodeType == 1)
      {
        sum += p.scrollLeft;
        p = p.parentNode;
      }

      return sum;
    },


    /**
     * TODOC
     *
     * @type static
     * @param el {Element} TODOC
     * @return {var} TODOC
     */
    getTopSum : function(el)
    {
      var sum = 0;
      var p = el.parentNode;

      while (p.nodeType == 1)
      {
        sum += p.scrollTop;
        p = p.parentNode;
      }

      return sum;
    },


    /**
     * Disables browser-native scrolling
     *
     * @type static
     * @param el {Element} html-element
     * @return {void}
     */
    disableScrolling : function(el) 
    {
      this.enableScrolling(el);
      el.scrollLeft = 0;
      el.scrollTop = 0;
      qx.html.EventRegistration.addEventListener(el, "scroll", this._onscroll);
    },


    /**
     * Re-enables browser-native scrolling
     *
     * @type static
     * @param el {Element} html-element
     * @return {void}
     */
    enableScrolling : function(el) 
    {;
      qx.html.EventRegistration.removeEventListener(el, "scroll", this._onscroll);
    },


    /**
     * Handler for the scroll-event
     *
     * @type static
     * @param ev {event} scroll-event
     * @return {void}
     */    
    _onscroll : function(ev) 
    {
      var el = ev.target || ev.srcElement;
      if(ev.scrollLeft != 0 || ev.scrollTop != 0) {
        el.scrollLeft = 0;
        el.scrollTop = 0;
      }
    }
  }
});
