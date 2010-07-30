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

#module(ui_basic)

************************************************************************ */

/**
 * The Label widget displays plain text or HTML text.
 *
 * Most complex qooxdoo widgets use instances of Label to display text.
 * The label supports auto sizing and internationalization.
 *
 * @appearance label
 */
qx.Class.define("qx.ui.basic.Label",
{
  extend : qx.ui.basic.Terminator,




  /*
  *****************************************************************************
     CONSTRUCTOR
  *****************************************************************************
  */

  /**
   * @param text {String} The text of the label (see property {@link #text}).
   * @param mnemonic {String} The mnemonic of the label (see property {@link #mnemonic}).
   * @param mode {String} The mode of the label (see property {@link #mode}).
   */
// TODO [rh] unused: replacement for below (no qx code calls 3-args ctor)   
  construct : function(text)
//  construct : function(text, mnemonic, mode)
  {
    this.base(arguments);

    if (text != null) {
      this.setText(text);
    }

// TODO [rh] unused
/*
     if (mode != null) {
      this.setMode(mode);
    }

    if (text != null) {
      this.setText(text);
    }

    if (mnemonic != null) {
      this.setMnemonic(mnemonic);
    }
*/

    // Property init
    this.initWidth();
    this.initHeight();
    this.initSelectable();
    this.initCursor();
    this.initWrap();
  },




  /*
  *****************************************************************************
     STATICS
  *****************************************************************************
  */

  statics :
  {
    /**
     * Create a DOM element, which can be used to measure the needed width of the label
     *
     * @internal
     * @type static
     * @return {Element} measure node
     */
    _getMeasureNode : function()
    {
      var node = this._measureNode;

      if (!node)
      {
        node = document.createElement("div");
        var style = node.style;

        style.width = style.height = "auto";
        style.visibility = "hidden";
        style.position = "absolute";
        style.zIndex = "-1";

        document.body.appendChild(node);

        this._measureNode = node;
      }

      return node;
    }
  },




  /*
  *****************************************************************************
     PROPERTIES
  *****************************************************************************
  */

  properties :
  {
    appearance :
    {
      refine : true,
      init : "label"
    },

    width :
    {
      refine : true,
      init : "auto"
    },

    height :
    {
      refine : true,
      init : "auto"
    },

    allowStretchX :
    {
      refine : true,
      init : false
    },

    allowStretchY :
    {
      refine : true,
      init : false
    },

    selectable :
    {
      refine : true,
      init : false
    },

// RAP [rst] qx bug 455 http://bugzilla.qooxdoo.org/show_bug.cgi?id=455
//    cursor :
//    {
//      refine : true,
//      init : "default"
//    },



    /**
     * The text of the label. How the text is interpreted depends on the value of the
     * property {@link #mode}.
     */
    text :
    {
      apply : "_applyText",
      init : "",
      dispose : true,
// TODO [rh] unused      
//      event : "changeText",
      check : "Label"
    },


    /**
     * Whether the text should be automatically wrapped into the next line
     */
    wrap :
    {
      check : "Boolean",
      init : false,
      nullable : true,
      apply : "_applyWrap"
    },


    /**
     * The alignment of the text inside the box
     */
    textAlign :
    {
      check : [ "left", "center", "right", "justify" ],
      nullable : true,
      themeable : true,
      apply : "_applyTextAlign"
    },


    /**
     * Whether an ellipsis symbol should be rendered if there is not enough room for the full text.
     *
     * Please note: If enabled this conflicts with a custom overflow setting.
     */
    textOverflow :
    {
      check : "Boolean",
      init : true
// TODO [rh] unused: removed as the corresponding impl was also removed    
//      apply : "_applyText"
    },

    /**
     * Set how the label text should be interpreted
     *
     * <ul>
     *   <li><code>text</code> will set the text verbatim. Leading and trailing white space will be reserved.</li>
     *   <li><code>html</code> will interpret the label text as html.</li>
     *   <li><code>auto</code> will try to guess whether the text represents an HTML string or plain text.
     *       This is how older qooxdoo versions treated the text.
     *   </li>
     * <ul>
     */
    mode :
    {
      check : [ "html", "text", "auto" ],
      init : "auto"
// TODO [rh] unused: replace with empty get/setMode functions      
//      apply : "_applyText"
    } // , TODO [rh] unused: removed trailing comma, see below


    /** A single character which will be underlined inside the text. */
// TODO [rh] unused    
//    mnemonic :
//    {
//      check : "String",
//      nullable : true,
//      apply : "_applyMnemonic"
//    }
  },




  /*
  *****************************************************************************
     MEMBERS
  *****************************************************************************
  */

  members :
  {
    _content : "",
    // TODO [rh] unused
//    _isHtml : false,

    /**
     * Deprecated text setter.
     *
     * @param html {String} new value of the label.
     *
     * @deprecated please use {@link #setText} instead.
     */
// TODO [rh] unused     
//    setHtml : function(html)
//    {
//      qx.log.Logger.deprecatedMethodWarning(arguments.callee, "please use setText() instead.");
//      this.setText(html);
//    },


    /**
     * Deprecated text getter.
     *
     * @return {String} current value of the label.
     * @deprecated please use {@link #getText} instead.
     */
// TODO [rh] unused     
//    getHtml : function()
//    {
//      qx.log.Logger.deprecatedMethodWarning(arguments.callee, "please use getText() instead.");
//      return this.getText();
//    },






    /*
    ---------------------------------------------------------------------------
      TEXTALIGN SUPPORT
    ---------------------------------------------------------------------------
    */

    _applyTextAlign : function(value, old) {
      value === null ? this.removeStyleProperty("textAlign") : this.setStyleProperty("textAlign", value);
    },




    /*
    ---------------------------------------------------------------------------
      FONT SUPPORT
    ---------------------------------------------------------------------------
    */

    _applyFont : function(value, old) {
      qx.theme.manager.Font.getInstance().connect(this._styleFont, this, value);
    },


    /**
     * Apply the font to the label.
     *
     * @type member
     * @param font {qx.ui.core.Font} new font.
     */
    _styleFont : function(font)
    {
      this._invalidatePreferredInnerDimensions();
      font ? font.render(this) : qx.ui.core.Font.reset(this);
    },




    /*
    ---------------------------------------------------------------------------
      TEXT COLOR SUPPORT
    ---------------------------------------------------------------------------
    */

    _applyTextColor : function(value, old) {
      qx.theme.manager.Color.getInstance().connect(this._styleTextColor, this, value);
    },

    /**
     * Apply the text color to the label.
     *
     * @type member
     * @param value {String} any acceptable CSS color
     */
    _styleTextColor : function(value) {
      value ? this.setStyleProperty("color", value) : this.removeStyleProperty("color");
    },




    /*
    ---------------------------------------------------------------------------
      WRAP SUPPORT
    ---------------------------------------------------------------------------
    */

    _applyWrap : function(value, old) {
      value == null ? this.removeStyleProperty("whiteSpace") : this.setStyleProperty("whiteSpace", value ? "normal" : "nowrap");
    },




    /*
    ---------------------------------------------------------------------------
      TEXT HANDLING
    ---------------------------------------------------------------------------
    */


    /**
     * TODOC
     *
     * @type member
     * @param value {var} Current value
     * @param old {var} Previous value
     */
    _applyText : function(value, old) {
//      qx.locale.Manager.getInstance().connect(this._syncText, this, this.getText());
      // DONT USE 'value' as this func is misued by other properties than text
      this._syncText( this.getText() ); 
    },


    /**
     * Apply a new label text
     *
     * @param text {String} new label text
     */
    _syncText : function(text)
    {
      /*
      var mode = this.getMode();

      if (mode === "auto") {
        mode = qx.util.Validation.isValidString(text) && text.match(/<.*>/) ? "html" : "text";
      }

      switch (mode)
      {
        case "text":
          var escapedText = qx.html.String.escape(text).replace(/(^ | $)/g, "&nbsp;").replace(/  /g, "&nbsp;&nbsp;");
          this._isHtml = escapedText !== text;
          this._content = escapedText;
          break;

        case "html":
          this._isHtml = true;
          this._content = text;
          break;
      }
      */
      
          this._content = text;
          
      if (this._isCreated) {
        this._renderContent();
      } 
    },


    /**
     * TODOC
     *
     * @type member
     * @param value {var} Current value
     * @param old {var} Previous value
     */
// TODO [rh] unused
/*     
    _applyMnemonic : function(value, old)
    {
      this._mnemonicTest = value ? new RegExp("^(((<([^>]|" + value + ")+>)|(&([^;]|" + value + ")+;)|[^&" + value + "])*)(" + value + ")", "i") : null;

      if (this._isCreated) {
        this._renderContent();
      }
    },
*/




    /*
    ---------------------------------------------------------------------------
      PREFERRED DIMENSIONS
    ---------------------------------------------------------------------------
    */

    /**
     * Computes the needed dimension for the current text.
     *
     * @type member
     */
    _computeObjectNeededDimensions : function()
    {
      // get node
      var element = this.self(arguments)._getMeasureNode();
      var style = element.style;

      // sync styles
      var source = this._styleProperties;
      style.fontFamily = source.fontFamily || "";
      style.fontSize = source.fontSize || "";
      style.fontWeight = source.fontWeight || "";
      style.fontStyle = source.fontStyle || "";

      // apply html
// TODO [rh] unused: replacement for below      
      element.innerHTML = this._content;
/*
      if (this._isHtml)
      {
        element.innerHTML = this._content;
      }
      else
      {
        element.innerHTML = "";
        qx.dom.Element.setTextContent(element, this._content);
      }
*/

      // store values
      this._cachedPreferredInnerWidth = element.scrollWidth;
      this._cachedPreferredInnerHeight = element.scrollHeight;
    },


    /**
     * overridden
     * @return {Integer}
     */
    _computePreferredInnerWidth : function()
    {
      this._computeObjectNeededDimensions();
      return this._cachedPreferredInnerWidth;
    },


    /**
     * overridden
     * @return {Integer}
     */
    _computePreferredInnerHeight : function()
    {
      this._computeObjectNeededDimensions();
      return this._cachedPreferredInnerHeight;
    },




    /*
    ---------------------------------------------------------------------------
      LAYOUT APPLY
    ---------------------------------------------------------------------------
    */

    /**
     * Creates an HTML fragment for the overflow symbol
     *
     * @param html {String} html string of the label
     * @param inner {Integer} inner width of the label
     * @return {String} html Fragment of the label with overflow symbol
     */
// TODO [rh] unused as not called anymore from replaced _postApply     
//    __patchTextOverflow : function(html, inner) {
//      return (
//        "<div style='float:left;width:" + (inner-14) +
//        "px;overflow:hidden;white-space:nowrap'>" + html +
//        "</div><span style='float:left'>&hellip;</span>"
//      );
//    },


    // TODO [rh] replacement for original function below
    _postApply : function() {
      var html = this._content;
      var element = this._getTargetNode();
      if( html == null ) {
        element.innerHTML = "";
      } else {
        var style = element.style;
        if( !this.getWrap() ) {
          if( this.getInnerWidth() < this.getPreferredInnerWidth() ) {
            style.overflow = "hidden";
          } else {
            style.overflow = "";
          }
        }
        element.innerHTML = html;
      }
    }

    /*
    // overridden
    _postApply : function()
    {
      var html = this._content;
      var element = this._getTargetNode();

      if (html == null)
      {
        element.innerHTML = "";
        return;
      }

      if (this.getMnemonic())
      {
        if (this._mnemonicTest.test(html))
        {
          html = RegExp.$1 + "<span style=\"text-decoration:underline\">" + RegExp.$7 + "</span>" + RegExp.rightContext;
          this._isHtml = true;
        }
        else
        {
          html += " (" + this.getMnemonic() + ")";
        }
      }
      
      var style = element.style;

      if (this.getTextOverflow() && !this.getWrap())
      {
        if (this.getInnerWidth() < this.getPreferredInnerWidth())
        {
          style.overflow = "hidden";

          if (qx.core.Variant.isSet("qx.client", "mshtml|webkit"))
          {
            style.textOverflow = "ellipsis";
          }
          else if (qx.core.Variant.isSet("qx.client", "opera"))
          {
            style.OTextOverflow = "ellipsis";
          }
          else
          {
            html = this.__patchTextOverflow(html, this.getInnerWidth());
            this._isHtml = true;
          }
        }
        else
        {
          style.overflow = "";

          if (qx.core.Variant.isSet("qx.client", "mshtml|webkit"))
          {
            style.textOverflow = "";
          }
          else if (qx.core.Variant.isSet("qx.client", "opera"))
          {
            style.OTextOverflow = "";
          }
        }
      }

      if (this._isHtml)
      {
        element.innerHTML = html;
      }
      else
      {
        element.innerHTML = "";
        qx.dom.Element.setTextContent(element, html);
      }
    }
      */
  }
});
